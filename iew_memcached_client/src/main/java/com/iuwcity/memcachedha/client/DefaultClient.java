package com.iuwcity.memcachedha.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.iuwcity.memcachedha.protocol.binary.BinaryProtocolConst;
import com.iuwcity.memcachedha.protocol.binary.BinaryProtocolParser;

public class DefaultClient implements Client {

	private Queue<Command> commandQueue = new ConcurrentLinkedQueue<Command>();

	private ExecutorService workPool = Executors.newCachedThreadPool();

	private String ip;

	private int port;

	private int workPoolSize;

	private int opTimeout;

	private void initWorkPool() throws IOException {
		for (int i = 0; i < workPoolSize; i++) {
			workPool.execute(new CommandSender());
		}
	}

	public DefaultClient(String ip, int port, int workPoolSize, int opTimeout) throws IOException {
		this.ip = ip;
		this.port = port;
		this.workPoolSize = workPoolSize;
		this.opTimeout = opTimeout;
		initWorkPool();
	}

	public Object sendCommand(final Command command) {
		this.commandQueue.add(command);
		Object response = null;
		try {
			if(command.getLatch().await(opTimeout, TimeUnit.MILLISECONDS)){
				new TimeoutException("wati for response time out!");
			}
			
			ResponseCommand responseCommand = command.getResponse();
			if(responseCommand != null){
				BinaryProtocolParser parser = new BinaryProtocolParser();
				response = parser.parserObject(responseCommand);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return response;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public class CommandSender implements Runnable {

		Selector selector;

		public CommandSender() throws IOException {
			SocketChannel sc = SocketChannel.open();
			sc.configureBlocking(false);
			selector = Selector.open();
			sc.register(selector, SelectionKey.OP_CONNECT);
			sc.connect(new InetSocketAddress(ip, port));
		}

		@Override
		public void run() {

			Command command = null;

			for (;;) {
				try {
					selector.select();

					Iterator<SelectionKey> iter = selector.selectedKeys().iterator();

					while (iter.hasNext()) {
						SelectionKey key = iter.next();
						iter.remove();
						if (key.isConnectable()) {
							SocketChannel channel = (SocketChannel) key.channel();
							if (channel.isConnectionPending())
								channel.finishConnect();

							channel.register(selector, SelectionKey.OP_WRITE);
						} else if (key.isWritable()) {
							SocketChannel channel = (SocketChannel) key.channel();

							if (command == null) {
								command = commandQueue.poll();
							}

							if (command != null) {
								ByteBuffer requestBuffer = command.getBuffer();

								if (channel.write(requestBuffer) > 0) {
									channel.register(selector, SelectionKey.OP_READ);
								} else {
									channel.register(selector, SelectionKey.OP_WRITE);
								}

							} else {
								channel.register(selector, SelectionKey.OP_WRITE);
							}

						} else if (key.isReadable()) {
							SocketChannel channel = (SocketChannel) key.channel();
							ByteBuffer responseHeaderBuffer = ByteBuffer.allocate(BinaryProtocolConst.HEADER_LEN);

							if (channel.read(responseHeaderBuffer) > 0) {
								ResponseCommand response = new ResponseCommand();
								response.setHeaderBuf(responseHeaderBuffer);

								BinaryProtocolParser protocolParser = new BinaryProtocolParser();
								protocolParser.fillHeaderProperties(responseHeaderBuffer);

								ByteBuffer responseBody = null;
								if(protocolParser.getTotalBodyLength() > 0){
									responseBody = ByteBuffer.allocate(protocolParser.getTotalBodyLength());
									channel.read(responseBody);
								}
								
								// todo fix me
								response.setBodyBuf(responseBody);
								command.setResponse(response);
								command.getLatch().countDown();
								command = null;
								// todo decode body
								channel.register(selector, SelectionKey.OP_WRITE);
							} else {
								channel.register(selector, SelectionKey.OP_WRITE);
							}
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
