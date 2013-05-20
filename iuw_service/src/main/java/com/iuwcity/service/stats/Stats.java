package com.iuwcity.service.stats;

public interface Stats {
	
	public enum USER_REG_STAT{
		
		success,//成功
		fail,//注册失败服务器原因
		name_duplicated,//用户名重复
		illegal,//名称不符合要求
		hx,//名称含有不和谐音符容易被跨省
	};
	
	public enum USER_CHECK_NAME_DUPLICATED{
		
		name_duplicated,
		unduplicated,
	};
	
}
