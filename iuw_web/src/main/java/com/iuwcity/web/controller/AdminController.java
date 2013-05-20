package com.iuwcity.web.controller;

import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.exception.MemcachedException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.iuwcity.service.IUserService;

@Controller
public class AdminController {
	
	@Autowired
	IUserService userService;
	
	@RequestMapping(value = "/admin/refreshUser")
	public void refreshUserInfo(){
		try {
			this.userService.refreshUserInfoToMemcache();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MemcachedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
