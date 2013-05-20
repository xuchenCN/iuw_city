package com.iuwcity.web.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WebUtil {
	
	public static final String UID_COOKE_KEY = "iuwcity_uid";
	
	public static void setLoginCookie(String uid ,HttpServletResponse response){
		Cookie cookie = new Cookie(UID_COOKE_KEY,uid);
		cookie.setMaxAge(Integer.MAX_VALUE);
		cookie.setPath("/");
		response.addCookie(cookie);
	}
	
	public static void removeLoginCookie(HttpServletResponse response){
		Cookie cookie = new Cookie(UID_COOKE_KEY,null);
		cookie.setMaxAge( -1 );
		cookie.setPath("/");
		response.addCookie(cookie);
	}
	
	public static String getLoginCookie(HttpServletRequest request){
		Cookie[] cookies = request.getCookies();
		if(cookies != null){
			for(Cookie  c : cookies){
				if(UID_COOKE_KEY.equals(c.getName())){
					return c.getValue();
				}
			}
		}
		
		return null;
	}
}
