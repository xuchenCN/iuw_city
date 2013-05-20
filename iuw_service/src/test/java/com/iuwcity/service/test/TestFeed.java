package com.iuwcity.service.test;

import java.util.Date;
import java.util.Map;

import com.iuwcity.service.IFeedService;
import com.iuwcity.service.relations.Relations.UserRelations;
import com.iuwcity.storage.bean.Feed;

public class TestFeed {
	
	private Helper helper = Helper.getInstance();
	
	public void testPushFeed() throws Exception{
		Feed feed = new Feed();
		feed.setContent("����");
		feed.setCreateTime(new Date());
		feed.setUserId(2);
		
		IFeedService feedService = (IFeedService)helper.getCtx().getBean("feedService");
		
		System.out.println(feedService.userPushNewFeed(2, UserRelations.FOLLOW, feed, null));
	}
	
	public void testGetFeed() throws Exception{
		IFeedService feedService = (IFeedService)helper.getCtx().getBean("feedService");
		Map<String, Feed> result = feedService.getUserFeed(1, UserRelations.FOLLOW, new Date(), 0, 100, null);
		System.out.println(result);
	}

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		TestFeed tester = new TestFeed();
//		tester.testPushFeed();
		tester.testGetFeed();
	}

}
