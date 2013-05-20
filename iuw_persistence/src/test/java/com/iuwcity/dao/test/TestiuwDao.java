package com.iuwcity.dao.test;

import com.iuwcity.dao.UserInfoDAO;



public class TestiuwDao {
	
	public static void main(String[] args) {
		Helper helper = new Helper();
		UserInfoDAO dao = (UserInfoDAO)helper.getCtx().getBean(UserInfoDAO.class);
		
		System.out.println(dao.getAll());
	}
}
