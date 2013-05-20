package com.iuwcity.web.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.iuwcity.service.IFeedService;
import com.iuwcity.service.IUserService;
import com.iuwcity.service.relations.Relations.UserRelations;
import com.iuwcity.service.stats.Stats;
import com.iuwcity.storage.bean.Feed;
import com.iuwcity.storage.bean.UserInfo;
import com.iuwcity.web.utils.WebUtil;
import com.iuwcity.web.view.FeedView;

@Controller
public class UserController {

	@Autowired
	IUserService userService;

	@Autowired
	IFeedService feedService;

	/**
	 * 用户登录
	 * 
	 * @param uname
	 * @param pwd
	 * @return
	 */
	@RequestMapping(value = "/user/login")
	public void userLogin(String uname, String pwd, String code,
			HttpServletRequest request, HttpServletResponse response) {
		//TODO 验证码系统
		try {
			PrintWriter out = response.getWriter();
			UserInfo user = this.userService.getUserInfoByName(uname);
			if(user != null){
				
				if(pwd.equals(user.getPassword())){
					WebUtil.setLoginCookie(user.getId() + "", response);
					out.print("0");//登陆成功
					return;
				}else {
					out.print("1");//密码错误
					return;
				}
			} else {
				out.print("2");//用户不存在
				return;
			}
			
		
		} catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
	
	/**
	 * 用户登出
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/user/logout")
	public void userLogout(HttpServletRequest request, HttpServletResponse response) {
		WebUtil.removeLoginCookie(response);
	}

	/**
	 * 用户注册
	 * 
	 * @param uid
	 * @param name
	 * @param age
	 * @param sex
	 * @param create_time
	 * @param last_login_date
	 * @param STATUS
	 * @param request
	 */
	@RequestMapping(value = "/user/reg")
	public String userRegister(String uid, String age, String sex, String email,
			String pwd, HttpServletRequest request, HttpServletResponse response) {
		try {
			if (request instanceof MultipartHttpServletRequest) {
				request = (MultipartHttpServletRequest) request;
			} else {

			}

			System.out.println("in user reg.." + uid + ":" + pwd + ":" + age);

			UserInfo user = new UserInfo();
			user.setName(uid);
			user.setAge(Integer.parseInt(age));
			user.setPassword(pwd);
			user.setCreateTime(new Date());
			user.setLastLoginDate(new Date());
			user.setHeadImg("/res/img/head.jpg");

			userService.registerUser(user, null);

			WebUtil.setLoginCookie(user.getId() + "", response);
			
			return "/user/list.do?page=0&size=10";

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "/pages/user/login.jsp";

	}
	
	
	@RequestMapping(value = "/user/follow")
	public void followUser(String fuid,String r ,HttpServletRequest request,
			HttpServletResponse response){
		String uid = WebUtil.getLoginCookie(request);
		
		this.userService.createRelationship(Integer.parseInt(uid), Integer.parseInt(fuid), UserRelations.FOLLOW, null);
		
		if("1".equals(r)){ //喜欢关系
			this.userService.createRelationship(Integer.parseInt(uid), Integer.parseInt(fuid), UserRelations.LOVE, null);
		}
	}

	/**
	 * 列表页
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/user/list")
	public String list(String page, String size, HttpServletRequest request,
			HttpServletResponse response) {

		String uid = WebUtil.getLoginCookie(request);

		if (uid == null || "".equals(uid)) {
			return "/pages/user/login.jsp";
		}

		UserInfo user;
		try {
			user = this.userService.getUserInfoById(Integer.parseInt(uid));

			Map<String, Feed> feeds = this.feedService.getUserFeed(user.getId(), null, null,
					Integer.parseInt(page), Integer.parseInt(size), null);
			
			Collection<FeedView> fvCollection = new ArrayList<FeedView>(feeds.size());
			
			for(Feed feed : feeds.values()){
				if(feed != null){
					fvCollection.add(this.feedViewBuilder(feed));
				}
			}
			
			request.setAttribute("feeds", fvCollection);
			request.setAttribute("user", user);

			return "/pages/user/list.jsp";

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "/pages/user/login.jsp";

	}

	/**
	 * 检查用户名是否存在
	 * 
	 * @param uname
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/user/checkUid")
	public void checkUid(String uname, HttpServletRequest request,
			HttpServletResponse response) {

		PrintWriter out;
		try {
			out = response.getWriter();

			if (uname == null || "".equals(uname.trim())) {
				out.print("1");
			}
			Stats.USER_CHECK_NAME_DUPLICATED result = this.userService
					.checkUserNameDuplicated(uname, null);
			if (Stats.USER_CHECK_NAME_DUPLICATED.name_duplicated == result) {
				out.print("2");
			} else if (Stats.USER_CHECK_NAME_DUPLICATED.unduplicated == result) {
				out.print("0");
			} else {
				out.print("3");
			}

			return;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private FeedView feedViewBuilder(Feed feed) throws Exception{
		
		FeedView fv = new FeedView();
		fv.setContent(feed.getContent());
		fv.setCreateTime(feed.getCreateTime());
		fv.setId(feed.getId());
		fv.setRelation(feed.getRelation());
		fv.setStatus(feed.getStatus());
		fv.setUserId(feed.getUserId());
		UserInfo user = this.userService.getUserInfoById(feed.getUserId());
		if(user != null){
			fv.setUserHeadImg(user.getHeadImg());
			fv.setUserName(user.getName());
		}
		
		return fv;
		
		
	}
}
