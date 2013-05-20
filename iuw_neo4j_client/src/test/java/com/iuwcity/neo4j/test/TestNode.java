package com.iuwcity.neo4j.test;

import org.neo4j.kernel.EmbeddedGraphDatabase;

public class TestNode {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		EmbeddedGraphDatabase graphdb = new EmbeddedGraphDatabase("target/neo");
		Thread.sleep(10 * 1000);
		graphdb.beginTx();
		graphdb.createNode();
	}

}
