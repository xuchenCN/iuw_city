package com.iuwcity.neo4j.service;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.kernel.EmbeddedGraphDatabase;

import com.iuwcity.storage.cache.Cacheable;

public interface Neo4jService {
	
	public static final String NODE_INDEX_NAME_USERS = "users";
	
	public static final String NODE_INDEX_NAME_KEY = "index_key";
	
	public static final String NODE_INDEX_NAME_USERNAME = "name";
	
	public EmbeddedGraphDatabase getEmbeddedGraphDatabase();
	
	public void createRelationToNode(Node node, Node otherNode, RelationshipType relationshipType);
	
	public Node createNodeFromBean(Cacheable bean) throws Exception;
	
	public Node updateNodeFromBean(Cacheable bean) throws Exception ;
	
	public Node getSingleNodeByProperty(final String property,final Object value) ;
	
}
