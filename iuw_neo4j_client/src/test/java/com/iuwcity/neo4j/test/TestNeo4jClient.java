package com.iuwcity.neo4j.test;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ReturnableEvaluator;
import org.neo4j.graphdb.StopEvaluator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.TraversalPosition;
import org.neo4j.graphdb.Traverser;
import org.neo4j.graphdb.Traverser.Order;
import org.neo4j.kernel.EmbeddedGraphDatabase;

public class TestNeo4jClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		EmbeddedGraphDatabase graphdb = new EmbeddedGraphDatabase("target/neo");
		
		final RelationshipType KNOWS = DynamicRelationshipType.withName("KNOWS");
		final RelationshipType LOVING = DynamicRelationshipType.withName("LOVING");

		Transaction tx = graphdb.beginTx();

		try {
			Node node1 = graphdb.createNode();
			node1.setProperty("name", "tester1");

			Node node2 = graphdb.createNode();
			node2.setProperty("name", "tester2");

			Node node3 = graphdb.createNode();
			node3.setProperty("name", "tester3");

			node1.createRelationshipTo(node2, KNOWS);

			node3.createRelationshipTo(node2, KNOWS);

			node3.createRelationshipTo(node2, LOVING);

			node2.createRelationshipTo(node1, LOVING);

			Traverser whoKnows = node3.traverse(Order.DEPTH_FIRST, StopEvaluator.DEPTH_ONE,
					ReturnableEvaluator.ALL_BUT_START_NODE, KNOWS, Direction.BOTH);

			System.out.println("Who knows " + node3.getProperty("name") + "?");
			for (Node know : whoKnows) {
				System.out.println(know.getProperty("name"));
			}

			StopEvaluator twoSteps = new StopEvaluator() {
				public boolean isStopNode(final TraversalPosition position) {
					return position.depth() == 2;
				}
			};

			ReturnableEvaluator nodesAtDepthTwo = new ReturnableEvaluator() {
				public boolean isReturnableNode(TraversalPosition position) {
					return position.depth() == 2;
				}
			};

			whoKnows = node3.traverse(Order.DEPTH_FIRST, twoSteps, nodesAtDepthTwo, KNOWS, Direction.BOTH);

			System.out.println("Whos " + node3.getProperty("name") + " friend's friend?");
			for (Node know : whoKnows) {
				System.out.println(know.getProperty("name"));
			}

			ReturnableEvaluator findLove = new ReturnableEvaluator() {
				public boolean isReturnableNode(TraversalPosition position) {
					return position.currentNode().hasRelationship(LOVING, Direction.OUTGOING);
				}
			};

			List<Object> types = new ArrayList<Object>();
			// we have to consider all relationship types of the whole graph
			// (in both directions)
			for (RelationshipType type : graphdb.getRelationshipTypes()) {
				types.add(type);
				types.add(Direction.BOTH);
			}

			whoKnows = node3.traverse(Order.BREADTH_FIRST, StopEvaluator.END_OF_GRAPH, findLove, types.toArray());
			
			System.out.println("Who is in love ? ");
			for (Node know : whoKnows) {
				System.out.println(know.getProperty("name"));
			}

			tx.success();
		} catch (Exception ex) {
			ex.printStackTrace();
			tx.failure();
		} finally {
			tx.finish();
		}

	}
}
