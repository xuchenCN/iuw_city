package com.iuwcity.dao;

import java.util.List;

import com.iuwcity.storage.bean.UserInfo;

public interface UserInfoDAO {
	
	public List<UserInfo> getAll(int page, int pageSize) ;

	public void addNewUser(UserInfo userInfo) ;
	
	public int countUserInfo();
}
