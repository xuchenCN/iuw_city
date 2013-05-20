package com.iuwcity.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.iuwcity.service.IFeedService;
import com.iuwcity.service.relations.Relations.UserRelations;
import com.iuwcity.storage.bean.Feed;
import com.iuwcity.web.utils.WebUtil;

@Controller
public class FeedController {

	@Autowired
	private IFeedService feedService;

	@RequestMapping(value = "/feed/push")
	public void list(String content, HttpServletRequest request,
			HttpServletResponse response) {
		String uid = WebUtil.getLoginCookie(request);
		try {
			if (uid != null && !"".equals(uid.trim())) {
				Feed feed = new Feed();
				feed.setContent(content);
				feed.setRelation(UserRelations.FOLLOW.toString());
				feed.setUserId(Integer.parseInt(uid));

				feedService.userPushNewFeed(Integer.parseInt(uid),
						UserRelations.FOLLOW, feed, null);

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
