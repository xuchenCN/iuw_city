package com.iuwcity.dao;

import java.util.List;

import com.iuwcity.storage.bean.Feed;

public interface FeedDAO {
	
	public List<Feed> getAll(int page, int pageSize) ;
	public int countUserInfo();

	public void addNewUser(Feed feed) ;
}
