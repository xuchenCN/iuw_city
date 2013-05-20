package com.iuwcity.chat.web.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

@WebServlet(urlPatterns = "/chat_2", asyncSupported = true)
public class ChatServlet_2 extends HttpServlet {

	private static final long serialVersionUID = -6741151503530066933L;

	private Map<String, AsyncContext> connections = new ConcurrentHashMap<String, AsyncContext>();
	private BlockingQueue<Event> queue = new LinkedBlockingQueue<Event>();
	private Thread broadcaster = new Thread(new Runnable() {
		@Override
		public void run() {
			while (true) {
				try {
					Event event = queue.take();
					for (Entry<String, AsyncContext> entry : connections.entrySet()) {
						try {
							send(entry.getValue(), event);
						} catch (IOException ex) {
							fire(new Event("close").socket(entry.getKey()));
						}
					}
				} catch (InterruptedException e) {
					break;
				}
			}
		}
	});

	@Override
	public void init() throws ServletException {
		broadcaster.setDaemon(true);
		broadcaster.start();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final String id = request.getParameter("id");
		String transport = request.getParameter("transport");
		AsyncContext asyncContext = request.startAsync();

		response.setCharacterEncoding("utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("text/" + ("sse".equals(transport) ? "event-stream" : "plain"));
		asyncContext.addListener(new AsyncListener() {
			public void onStartAsync(AsyncEvent event) throws IOException {

			}

			public void onComplete(AsyncEvent event) throws IOException {
				cleanup(event);
			}

			public void onTimeout(AsyncEvent event) throws IOException {
				cleanup(event);
			}

			public void onError(AsyncEvent event) throws IOException {
				cleanup(event);
			}

			private void cleanup(AsyncEvent event) {
				fire(new Event("close").socket(id));
			}
		});

		PrintWriter writer = response.getWriter();
		for (int i = 0; i < 2000; i++) {
			writer.print(' ');
		}
		writer.print("\n");
		writer.flush();

		connections.put(id, asyncContext);
		fire(new Event("open").socket(id));
	}

	private void send(AsyncContext asyncContext, Event event) throws IOException {
		String data = new Gson().toJson(event);
		PrintWriter writer = asyncContext.getResponse().getWriter();

		for (String datum : data.split("\r\n|\r|\n")) {
			writer.print("data: ");
			writer.print(datum);
			writer.print("\n");
		}

		writer.print("\n");
		writer.flush();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");

		String data = request.getReader().readLine();
		if (data != null) {
			data = data.substring("data=".length());
			fire(new Gson().fromJson(data, Event.class));
		}
	}

	private void fire(Event event) {
		if (event.type.equals("close")) {
			connections.remove(event.socket);
		}

		handle(event);
	}

	private void handle(Event event) {
		if (event.type.equals("message")) {
			queue.offer(new Event("message").data(event.data));
		}
	}

	private static class Event {
		private String socket;
		private String type;
		private Object data;

		@SuppressWarnings("unused")
		public Event() {

		}

		public Event(String type) {
			this.type = type;
		}

		public Event data(Object data) {
			this.data = data;
			return this;
		}

		public Event socket(String socket) {
			this.socket = socket;
			return this;
		}
	}

}
