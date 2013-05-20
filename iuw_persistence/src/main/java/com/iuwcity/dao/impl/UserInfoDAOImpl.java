package com.iuwcity.dao.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Repository;

import com.iuwcity.dao.UserInfoDAO;
import com.iuwcity.storage.bean.UserInfo;
import com.mop.querymap.springjdbc.BaseSpringDao;
import com.mop.querymap.springjdbc.SimpleOperations;

@Repository
public class UserInfoDAOImpl extends BaseSpringDao implements UserInfoDAO {

	public static final String SQL_USER_SELECT = "UserInfoDAO.SelectAll";

	public static final String SQL_USER_INSERT = "UserInfoDAO.Insert";
	
	public static final String SQL_USER_COUNT = "UserInfoDAO.CountAll";

	private SimpleOperations opSelectAll;

	private SimpleOperations opInsert;

	private SimpleOperations opCountAll;
	
	@Override
	protected Object getSubclassInstance() {
		return this;
	}

	@PostConstruct
	public void initDataOperations() {
		this.opSelectAll = this.getOperations(SQL_USER_SELECT);
		this.opInsert = this.getOperations(SQL_USER_INSERT);
		this.opCountAll = this.getOperations(SQL_USER_COUNT);
	}

	public List<UserInfo> getAll(int page, int pageSize) {
		return this.opSelectAll.query(new UserInfo(), new Object[] {page,pageSize});
	}
	
	public int countUserInfo(){
		return this.opCountAll.queryForInt(new Object[]{});
	}

	public void addNewUser(UserInfo userInfo) {
		this.opInsert.update(userInfo.getId(), userInfo.getName(), userInfo.getAge(), userInfo.getSex(), userInfo.getCreateTime(), new Date(),userInfo.getStatus(),userInfo.getHeadImg(),userInfo.getPassword());
	}

}
