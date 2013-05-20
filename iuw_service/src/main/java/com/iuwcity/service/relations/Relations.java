package com.iuwcity.service.relations;


public interface Relations {
	
	public enum UserRelations  {
		MATRIX, //宿主
		KNOW,//知道
		FOLLOW,//关注
		LOVE,//喜欢
	}
}
