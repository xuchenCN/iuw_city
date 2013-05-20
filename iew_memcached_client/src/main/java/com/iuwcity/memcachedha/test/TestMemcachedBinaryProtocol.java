package com.iuwcity.memcachedha.test;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class TestMemcachedBinaryProtocol {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		// header
		byte[] magic = new byte[1];
		byte[] opcode = new byte[1];
		byte[] keyLength = new byte[2];
		byte[] extrasLength = new byte[1];
		byte[] dataType = new byte[1];
		byte[] reserved = new byte[2];
		byte[] totalBodyLength = new byte[4];
		byte[] opaque = new byte[4];
		byte[] cas = new byte[8];
		
		magic[0] = (byte) 0x80;
		opcode[0] = (byte) 0x0b;
		keyLength[0] = (byte) 0x00;
		keyLength[1] = (byte) 0x00;
		extrasLength[0] = (byte) 0x00;
		dataType[0] = (byte) 0x00;
		reserved[0] = (byte) 0x00;
		reserved[1] = (byte) 0x00;
		totalBodyLength[0] = (byte) 0x00;
		totalBodyLength[1] = (byte) 0x00;
		totalBodyLength[2] = (byte) 0x00;
		totalBodyLength[3] = (byte) 0x00;
		opaque[0] = (byte) 0x00;
		opaque[1] = (byte) 0x00;
		opaque[2] = (byte) 0x00;
		opaque[3] = (byte) 0x00;
		cas[0] = (byte) 0x00;
		cas[1] = (byte) 0x00;
		cas[2] = (byte) 0x00;
		cas[3] = (byte) 0x00;
		cas[4] = (byte) 0x00;
		cas[5] = (byte) 0x00;
		cas[6] = (byte) 0x00;
		cas[7] = (byte) 0x00;

		ByteBuffer requestBuffer = ByteBuffer.allocate(24);
		requestBuffer.clear();
		requestBuffer.put(magic);
		requestBuffer.put(opcode);
		requestBuffer.put(keyLength);
		requestBuffer.put(extrasLength);
		requestBuffer.put(dataType);
		requestBuffer.put(reserved);
		requestBuffer.put(totalBodyLength);
		requestBuffer.put(opaque);
		requestBuffer.put(cas);

		requestBuffer.flip();

		// 打开Socket通道
		SocketChannel sc = SocketChannel.open();
		// 设置为非阻塞模式
		sc.configureBlocking(false);
		// 打开选择器
		Selector selector = Selector.open();
		// 注册连接服务端socket动作
		sc.register(selector, SelectionKey.OP_CONNECT);
		// 连接
		sc.connect(new InetSocketAddress("127.0.0.1", 11211));
		
//		byte[] responseBody = new byte[29];
		ByteBuffer responseBuffer = ByteBuffer.allocate(29);

		
		for (;;) {  
            selector.select();  
            Iterator iter = selector.selectedKeys().iterator();  

            while (iter.hasNext()) {  
                SelectionKey key = (SelectionKey) iter.next();  
                iter.remove();  
                if (key.isConnectable()) {  
                    SocketChannel channel = (SocketChannel) key  
                            .channel();  
                    if (channel.isConnectionPending())  
                        channel.finishConnect();  
                    System.out.println(channel.write(requestBuffer));  
                    channel.register(selector, SelectionKey.OP_READ);  
                } else if (key.isReadable()) {  
                    SocketChannel channel = (SocketChannel) key  
                            .channel();  
                    int count = channel.read(responseBuffer);  
                    if (count > 0) {  
                    	System.out.println(new String(responseBuffer.array(),24,5));
                    	responseBuffer.clear();  
                    } else {  
                        break; 
                    }  
                }  
            }  
        }  

	}
	
	public static byte[] IntegerToBytes(){
		ByteBuffer bf = ByteBuffer.allocate(4);
		return null;
	}

}
