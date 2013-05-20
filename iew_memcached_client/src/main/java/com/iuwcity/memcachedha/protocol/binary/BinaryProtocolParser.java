package com.iuwcity.memcachedha.protocol.binary;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.iuwcity.memcachedha.client.CommandData;
import com.iuwcity.memcachedha.client.CommandPrototype;
import com.iuwcity.memcachedha.client.ResponseCommand;
import com.iuwcity.memcachedha.client.trans.JdkSerializer;
import com.iuwcity.memcachedha.client.trans.Serializer;
import com.iuwcity.memcachedha.protocol.CommandEnum;

public class BinaryProtocolParser {

	public static ByteOrder bo = ByteOrder.BIG_ENDIAN;

	public static final String DEFAULT_CHARSET_NAME = "utf-8";

	public static Serializer serilizer = new JdkSerializer();

	// header
	private byte magic;
	private byte opcode;
	private short keyLength;
	private byte extrasLength;
	private byte dataType;
	private short reserved;
	private int totalBodyLength;
	private int opaque;
	private long cas;
	// extras
	private int flags;
	private int expiry;

	private byte[] key;
	private byte[] value;

	public ByteBuffer parserBuffer(CommandEnum commandEnum, CommandPrototype prototype) {
		if (commandEnum == CommandEnum.Version) {
			magic = BinaryProtocolConst.Magic.REQUEST_MAIGIC;
			opcode = BinaryProtocolConst.Opcodes.VERSION;
			keyLength = BinaryProtocolConst.EMPTY_SHORT;
			extrasLength = BinaryProtocolConst.EMPTY_BYTE;
			dataType = BinaryProtocolConst.DataTypes.RAW_BYES;
			reserved = BinaryProtocolConst.EMPTY_SHORT;
			totalBodyLength = BinaryProtocolConst.EMPTY_INT;
			opaque = BinaryProtocolConst.EMPTY_INT;
			cas = BinaryProtocolConst.EMPTY_LONG;

		} else if (commandEnum == CommandEnum.Add) {

			CommandData data = serilizer.encode(prototype.getValue());

			magic = BinaryProtocolConst.Magic.REQUEST_MAIGIC;
			opcode = BinaryProtocolConst.Opcodes.ADD;
			keyLength = (short) prototype.getKey().length();
			extrasLength = (byte) 0x08;
			dataType = BinaryProtocolConst.DataTypes.RAW_BYES;
			reserved = BinaryProtocolConst.EMPTY_SHORT;
			totalBodyLength = keyLength + extrasLength + data.getValueData().length;
			opaque = BinaryProtocolConst.EMPTY_INT;
			cas = BinaryProtocolConst.EMPTY_LONG;
			flags = data.getFlag();
			expiry = prototype.getExpiry();

			key = prototype.getKeyBytes(DEFAULT_CHARSET_NAME);
			value = data.getValueData();

		} else if (commandEnum == CommandEnum.Get) {
			magic = BinaryProtocolConst.Magic.REQUEST_MAIGIC;
			opcode = BinaryProtocolConst.Opcodes.GET;
			keyLength = (short) prototype.getKey().length();
			extrasLength = BinaryProtocolConst.EMPTY_BYTE;
			dataType = BinaryProtocolConst.DataTypes.RAW_BYES;
			reserved = BinaryProtocolConst.EMPTY_SHORT;
			totalBodyLength = keyLength;
			opaque = BinaryProtocolConst.EMPTY_INT;
			cas = BinaryProtocolConst.EMPTY_LONG;

			key = prototype.getKeyBytes(DEFAULT_CHARSET_NAME);
		}

		return warpBuffer(commandEnum);
	}

	private ByteBuffer warpBuffer(CommandEnum commandEnum) {

		ByteBuffer requestBuffer = null;

		if (commandEnum == CommandEnum.Version) {

			requestBuffer = ByteBuffer.allocate(BinaryProtocolConst.HEADER_LEN);

			requestBuffer.put(this.getMagic()); // magic
			requestBuffer.put(this.getOpcode()); // opcode
			requestBuffer.putShort(this.getKeyLength()); // keylength
			requestBuffer.put(this.getExtrasLength()); // extraslength
			requestBuffer.put(this.getDataType()); // datatype
			requestBuffer.putShort(this.getReserved()); // reserved | VBucket
			requestBuffer.putInt(this.getTotalBodyLength()); // totalBodyLength
			requestBuffer.putInt(this.getOpaque()); // opaque
			requestBuffer.putLong(this.getCas()); // cas

		} else if (commandEnum == CommandEnum.Add) {

			requestBuffer = ByteBuffer.allocate(BinaryProtocolConst.HEADER_LEN + totalBodyLength);

			requestBuffer.put(this.getMagic()); // magic
			requestBuffer.put(this.getOpcode()); // opcode
			requestBuffer.putShort(this.getKeyLength()); // keylength
			requestBuffer.put(this.getExtrasLength()); // extraslength
			requestBuffer.put(this.getDataType()); // datatype
			requestBuffer.putShort(this.getReserved()); // reserved | VBucket
			requestBuffer.putInt(this.getTotalBodyLength()); // totalBodyLength
			requestBuffer.putInt(this.getOpaque()); // opaque
			requestBuffer.putLong(this.getCas()); // cas
			requestBuffer.putInt(this.getFlags());
			requestBuffer.putInt(this.getExpiry());
			requestBuffer.put(this.getKey());
			requestBuffer.put(this.getValue());

		} else if (commandEnum == CommandEnum.Get) {
			requestBuffer = ByteBuffer.allocate(BinaryProtocolConst.HEADER_LEN + totalBodyLength);
			requestBuffer.put(this.getMagic()); // magic
			requestBuffer.put(this.getOpcode()); // opcode
			requestBuffer.putShort(this.getKeyLength()); // keylength
			requestBuffer.put(this.getExtrasLength()); // extraslength
			requestBuffer.put(this.getDataType()); // datatype
			requestBuffer.putShort(this.getReserved()); // reserved | VBucket
			requestBuffer.putInt(this.getTotalBodyLength()); // totalBodyLength
			requestBuffer.putInt(this.getOpaque()); // opaque
			requestBuffer.putLong(this.getCas()); // cas
			requestBuffer.put(this.getKey());
		}

		requestBuffer.flip();

		return requestBuffer;

	}

	public BinaryProtocolParser() {
	}

	public void fillHeaderProperties(ByteBuffer byteBuffer) {
		byteBuffer.flip();

		magic = byteBuffer.get();
		opcode = byteBuffer.get();
		keyLength = byteBuffer.getShort();
		extrasLength = byteBuffer.get();
		dataType = byteBuffer.get();
		reserved = byteBuffer.getShort();
		totalBodyLength = byteBuffer.getInt();
		opaque = byteBuffer.getInt();
		cas = byteBuffer.getInt();
	}

	public Object parserObject(ResponseCommand response) {

		fillHeaderProperties(response.getHeaderBuf());
		ByteBuffer bodyBuffer = response.getBodyBuf();

		try {

			if (opcode == BinaryProtocolConst.Opcodes.ADD) {
				if (reserved == BinaryProtocolConst.ResponseStatus.NO_ERROR) {
					return true;
				} else if(reserved == BinaryProtocolConst.ResponseStatus.KEY_EXISTS){
					return false;
				}
			} else if (opcode == BinaryProtocolConst.Opcodes.GET) {
				bodyBuffer.flip();
				int flag = bodyBuffer.getInt();
				byte[] value = new byte[totalBodyLength - keyLength - extrasLength];
				bodyBuffer.get(value);
				CommandData cd = new CommandData(flag, value);
				return serilizer.decode(cd);
			} else if (opcode == BinaryProtocolConst.Opcodes.VERSION) {
				bodyBuffer.flip();
				return new String(bodyBuffer.array(), DEFAULT_CHARSET_NAME);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	public byte getMagic() {
		return magic;
	}

	public void setMagic(byte magic) {
		this.magic = magic;
	}

	public byte getOpcode() {
		return opcode;
	}

	public void setOpcode(byte opcode) {
		this.opcode = opcode;
	}

	public short getKeyLength() {
		return keyLength;
	}

	public void setKeyLength(short keyLength) {
		this.keyLength = keyLength;
	}

	public byte getExtrasLength() {
		return extrasLength;
	}

	public void setExtrasLength(byte extrasLength) {
		this.extrasLength = extrasLength;
	}

	public byte getDataType() {
		return dataType;
	}

	public void setDataType(byte dataType) {
		this.dataType = dataType;
	}

	public short getReserved() {
		return reserved;
	}

	public void setReserved(short reserved) {
		this.reserved = reserved;
	}

	public int getTotalBodyLength() {
		return totalBodyLength;
	}

	public void setTotalBodyLength(int totalBodyLength) {
		this.totalBodyLength = totalBodyLength;
	}

	public int getOpaque() {
		return opaque;
	}

	public void setOpaque(int opaque) {
		this.opaque = opaque;
	}

	public long getCas() {
		return cas;
	}

	public void setCas(long cas) {
		this.cas = cas;
	}

	public int getFlags() {
		return flags;
	}

	public void setFlags(int flags) {
		this.flags = flags;
	}

	public int getExpiry() {
		return expiry;
	}

	public void setExpiry(int expiry) {
		this.expiry = expiry;
	}

	public byte[] getKey() {
		return key;
	}

	public void setKey(byte[] key) {
		this.key = key;
	}

	public byte[] getValue() {
		return value;
	}

	public void setValue(byte[] value) {
		this.value = value;
	}

}
