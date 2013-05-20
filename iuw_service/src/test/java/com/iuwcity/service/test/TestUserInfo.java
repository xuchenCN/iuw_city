package com.iuwcity.service.test;

import java.util.Map;

import com.iuwcity.service.IUserService;
import com.iuwcity.service.impl.UserService;
import com.iuwcity.service.relations.Relations;
import com.iuwcity.service.relations.Relations.UserRelations;
import com.iuwcity.storage.StorageService;
import com.iuwcity.storage.bean.UserInfo;

public class TestUserInfo {
	
	private Helper helper = Helper.getInstance();
	
	public void testUserReg() throws Exception{
		UserInfo user = new UserInfo();
		user.setName("testercc");
		user.setPassword("123456");
		IUserService userService = (IUserService)helper.getCtx().getBean(UserService.class);
		System.out.println(userService.registerUser(user, null));
//		StorageService storageService = (StorageService)helper.getCtx().getBean(StorageService.class);
		
	}
	
	public void testAddFollower() throws Exception {
		IUserService userService = (IUserService)helper.getCtx().getBean(UserService.class);
		userService.createRelationship(3, 2, Relations.UserRelations.FOLLOW, null);
	}
	
	public void testGetFollower() throws Exception {
		IUserService userService = (IUserService)helper.getCtx().getBean(UserService.class);
		Map<String, UserInfo> result = userService.getOutgoingUsersByRelationship(1,Relations.UserRelations.FOLLOW, 0, 5, null);
		System.out.println(result);
	}
	
	public void testGetFollower_RE() throws Exception {
		IUserService userService = (IUserService)helper.getCtx().getBean(UserService.class);
		Map<String, UserInfo> result = userService.getIncomingUsersByRelationship(2,Relations.UserRelations.FOLLOW, 0, 5, null);
		System.out.println(result);
	}
	
	public UserInfo testGet (UserInfo user){
		StorageService storageService = (StorageService)helper.getCtx().getBean(StorageService.class);
		return (UserInfo)storageService.getBean(user.getId(), UserInfo.class);
	}
	
	public void testInterRelaUser(){
		IUserService userService = (IUserService)helper.getCtx().getBean(UserService.class);
		Map<String, UserInfo> result = userService.getInterRelationshipUser(UserRelations.FOLLOW, 0, 50, new Integer[]{3,1});
	}
	
	public static void main(String[] args) throws Exception {
		TestUserInfo test = new TestUserInfo();
		System.out.println("lanuch");
//		test.testUserReg();
//		test.testAddFollower();
//		test.testGetFollower();
//		test.testGetFollower_RE();
		test.testInterRelaUser();
//		UserInfo user = new UserInfo();
//		user.setId(4);
//		System.out.println(test.testGet(user));
	}
	
}
