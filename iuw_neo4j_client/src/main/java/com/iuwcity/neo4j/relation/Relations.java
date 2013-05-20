package com.iuwcity.neo4j.relation;

import org.neo4j.graphdb.RelationshipType;

public interface Relations {
	
	public enum UserRelations implements RelationshipType {
		MATRIX, //宿主
		KNOW,//知道
		FOLLOW,//关注
		LOVE,//喜欢
	}
}
