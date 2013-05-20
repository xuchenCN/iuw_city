package com.iuwcity.neo4j.util;

import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.iuwcity.neo4j.service.Neo4jService;

@Component
public class NeoDBKeeper {
	
	@Autowired
	private Neo4jService neo4jService;
	
	public EmbeddedGraphDatabase getEmbeddedGraphDatabase(){
		return neo4jService.getEmbeddedGraphDatabase();
	}

	public void setNeo4jService(Neo4jService neo4jService) {
		this.neo4jService = neo4jService;
	}
	
}
