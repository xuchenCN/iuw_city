package com.iuwcity.memcachedha.client.trans;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iuwcity.memcachedha.MemcachedClientDispatch;
import com.iuwcity.memcachedha.client.CommandData;

public class JdkSerializer extends BaseSerializer implements Serializer {

	private static final Log LOG = LogFactory.getLog(MemcachedClientDispatch.class);

	public void setPrimitiveAsString(boolean primitiveAsString) {
		this.primitiveAsString = primitiveAsString;
	}

	private boolean primitiveAsString;

	private final SerializerUtils serilizerUtils = new SerializerUtils(true);

	public SerializerUtils getSerilizerUtils() {
		return serilizerUtils;
	}

	// General flags
	public static final int SERIALIZED = 1;
	public static final int COMPRESSED = 2;

	// Special flags for specially handled types.
	public static final int SPECIAL_MASK = 0xff00;
	public static final int SPECIAL_BOOLEAN = (1 << 8);
	public static final int SPECIAL_INT = (2 << 8);
	public static final int SPECIAL_LONG = (3 << 8);
	public static final int SPECIAL_DATE = (4 << 8);
	public static final int SPECIAL_BYTE = (5 << 8);
	public static final int SPECIAL_FLOAT = (6 << 8);
	public static final int SPECIAL_DOUBLE = (7 << 8);
	public static final int SPECIAL_BYTEARRAY = (8 << 8);

	public final CommandData encode(Object o) {
		byte[] b = null;
		int flags = 0;
		if (o instanceof String) {
			b = encodeString((String) o);
		} else if (o instanceof Long) {
			if (this.primitiveAsString) {
				b = encodeString(o.toString());
			} else {
				b = this.serilizerUtils.encodeLong((Long) o);
			}
			flags |= SPECIAL_LONG;
		} else if (o instanceof Integer) {
			if (this.primitiveAsString) {
				b = encodeString(o.toString());
			} else {
				b = this.serilizerUtils.encodeInt((Integer) o);
			}
			flags |= SPECIAL_INT;
		} else if (o instanceof Boolean) {
			if (this.primitiveAsString) {
				b = encodeString(o.toString());
			} else {
				b = this.serilizerUtils.encodeBoolean((Boolean) o);
			}
			flags |= SPECIAL_BOOLEAN;
		} else if (o instanceof Date) {
			b = this.serilizerUtils.encodeLong(((Date) o).getTime());
			flags |= SPECIAL_DATE;
		} else if (o instanceof Byte) {
			if (this.primitiveAsString) {
				b = encodeString(o.toString());
			} else {
				b = this.serilizerUtils.encodeByte((Byte) o);
			}
			flags |= SPECIAL_BYTE;
		} else if (o instanceof Float) {
			if (this.primitiveAsString) {
				b = encodeString(o.toString());
			} else {
				b = this.serilizerUtils.encodeInt(Float.floatToRawIntBits((Float) o));
			}
			flags |= SPECIAL_FLOAT;
		} else if (o instanceof Double) {
			if (this.primitiveAsString) {
				b = encodeString(o.toString());
			} else {
				b = this.serilizerUtils.encodeLong(Double.doubleToRawLongBits((Double) o));
			}
			flags |= SPECIAL_DOUBLE;
		} else if (o instanceof byte[]) {
			b = (byte[]) o;
			flags |= SPECIAL_BYTEARRAY;
		} else {
			b = serialize(o);
			flags |= SERIALIZED;
		}
		assert b != null;
		if (this.primitiveAsString) {
			// It is not be SERIALIZED,so change it to string type
			if ((flags & SERIALIZED) == 0) {
				flags = 0;
			}
		}
		if (b.length > this.compressionThreshold) {
			byte[] compressed = compress(b);
			if (compressed.length < b.length) {
				if (LOG.isDebugEnabled()) {
					LOG.debug("Compressed " + o.getClass().getName() + " from " + b.length + " to " + compressed.length);
				}
				b = compressed;
				flags |= COMPRESSED;
			} else {
				if (LOG.isDebugEnabled()) {
					LOG.debug("Compression increased the size of " + o.getClass().getName() + " from " + b.length + " to "
							+ compressed.length);
				}
			}
		}

		return new CommandData(flags, b);

	}

	public final Object decode(CommandData commandData) {
		byte[] data = commandData.getValueData();

		int flags = commandData.getFlag();
		if ((flags & COMPRESSED) != 0) {
			data = decompress(commandData.getValueData());
		}
		flags = flags & SPECIAL_MASK;
		return decode0(commandData, data, flags);
	}

	protected final Object decode0(CommandData commandData, byte[] data, int flags) {
		Object rv = null;
		if ((commandData.getFlag() & SERIALIZED) != 0 && data != null) {
			rv = deserialize(data);
		} else {
			if (this.primitiveAsString) {
				if (flags == 0) {
					return decodeString(data);
				}
			}
			if (flags != 0 && data != null) {
				switch (flags) {
				case SPECIAL_BOOLEAN:
					rv = Boolean.valueOf(this.serilizerUtils.decodeBoolean(data));
					break;
				case SPECIAL_INT:
					rv = Integer.valueOf(this.serilizerUtils.decodeInt(data));
					break;
				case SPECIAL_LONG:
					rv = Long.valueOf(this.serilizerUtils.decodeLong(data));
					break;
				case SPECIAL_BYTE:
					rv = Byte.valueOf(this.serilizerUtils.decodeByte(data));
					break;
				case SPECIAL_FLOAT:
					rv = new Float(Float.intBitsToFloat(this.serilizerUtils.decodeInt(data)));
					break;
				case SPECIAL_DOUBLE:
					rv = new Double(Double.longBitsToDouble(this.serilizerUtils.decodeLong(data)));
					break;
				case SPECIAL_DATE:
					rv = new Date(this.serilizerUtils.decodeLong(data));
					break;
				case SPECIAL_BYTEARRAY:
					rv = data;
					break;
				default:
					LOG.warn(String.format("Undecodeable with flags %x", flags));
				}
			} else {
				rv = decodeString(data);
			}
		}
		return rv;
	}
}
