package com.iuwcity.dao.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import com.iuwcity.dao.FeedDAO;
import com.iuwcity.storage.bean.Feed;
import com.mop.querymap.springjdbc.BaseSpringDao;
import com.mop.querymap.springjdbc.SimpleOperations;

public class FeedDAOImpl extends BaseSpringDao implements FeedDAO {
	
	public static final String SQL_FEED_SELECT = "FeedDAO.SelectAll";

	public static final String SQL_FEED_INSERT = "FeedDAO.Insert";
	
	public static final String SQL_FEED_COUNT = "FeedDAO.CountAll";

	private SimpleOperations opSelectAll;

	private SimpleOperations opInsert;

	private SimpleOperations opCountAll;
	
	@PostConstruct
	public void initDataOperations() {
		this.opSelectAll = this.getOperations(SQL_FEED_SELECT);
		this.opInsert = this.getOperations(SQL_FEED_INSERT);
		this.opCountAll = this.getOperations(SQL_FEED_COUNT);
	}
	
	public List<Feed> getAll(int page, int pageSize) {
		return this.opSelectAll.query(new Feed(), new Object[] {page,pageSize});
	}
	
	public int countUserInfo(){
		return this.opCountAll.queryForInt(new Object[]{});
	}

	public void addNewUser(Feed feed) {
		this.opInsert.update(feed.getId(), feed.getUserId(), feed.getContent(), feed.getStatus(), feed.getCreateTime(), feed.getRelation());
	}
	
	@Override
	protected Object getSubclassInstance() {
		return this;
	}
}
