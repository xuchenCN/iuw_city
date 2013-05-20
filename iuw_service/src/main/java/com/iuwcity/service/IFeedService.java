package com.iuwcity.service;

import java.util.Date;
import java.util.Map;

import com.iuwcity.service.relations.Relations.UserRelations;
import com.iuwcity.storage.bean.Feed;
import com.iuwcity.storage.bean.FeedReply;

public interface IFeedService {

	public static final String USER_FEED_CACHE_HEAD = "user_cache_feed:";

	public static final String FEED_CACHE_HEAD = "cache_feed:";
	
	public static final String FEED_REPLY_CACHE_HEAD = "cache_feed_reply:";
	
	public static final String FEED_REPLY_LIST_CACHE_HEAD = "cache_feed_reply_list:";

	/**
	 * 用户发送feed
	 */
	public int userPushNewFeed(final int userId, final UserRelations relation, final Feed feed, final Map<String, String> prop)
			throws Exception;

	
	/**
	 * 获得用户feed 注意 relation 和 createTime 可为null 如果为null 则不会采用此条件查询
	 * 
	 * @param userId
	 * @param relation
	 * @param createTime
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public Map<String, Feed> getUserFeed(final int userId, final UserRelations relation, final Date createTime, int page,
			int pageSize, Map<String, String> prop) throws Exception;
	
	/**
	 * 用户回复一个feed
	 * @param reply
	 * @param prop
	 * @return
	 * @throws Exception
	 */
	public int replyFeed(final FeedReply reply, Map<String, String> prop) throws Exception ;
	
	/**
	 * 获得feed下的回复
	 * @param feedId
	 * @param page
	 * @param pageSize
	 * @param prop
	 * @return
	 * @throws Exception
	 */
	public Map<String, FeedReply> getFeedReply(final int feedId, int page, int pageSize, Map<String, String> prop)
			throws Exception ;
	
	/**
	 * 用户删除feed
	 * @param userId
	 * @param feed
	 * @param prop
	 */
	public void removeFeed(final int userId , final Feed feed , Map<String,String> prop);
	
	/**
	 * 获得feed下的回复数量
	 * @param feedId
	 * @param page
	 * @param pageSize
	 * @param prop
	 * @return
	 * @throws Exception
	 */
	public long getFeedReplyCount(final int feedId, Map<String, String> prop);
	
	/**
	 * 删除feed下的回复
	 * @param feedId
	 * @param replyId
	 */
	public void removeFeedReply(final int feedId, final int replyId);
	
	/**
	 * 获得用户feed的数量 注意 relation 和 createTime 可为null 如果为null 则不会采用此条件查询
	 * 
	 * @param userId
	 * @param relation
	 * @param createTime
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	public long getUserFeedCount(final int userId, final UserRelations relation, final Date createTime, Map<String, String> prop) throws Exception;
}
