package com.iuwcity.service.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Helper {
	private ApplicationContext ctx;
    private static Helper      HELPER = new Helper();
    
    
    private Helper() {
        String[] files = new String[] { "classpath*:config/spring/beans.xml", 
        		"classpath*:config/iuw-persistence-spring.xml"};
        
//        String[] files = new String[] { "classpath*:config/spring/beans.xml" ,
//        		"classpath*:config/spring/springcontext-redis.xml",
//        		"classpath*:config/spring/springcontext-memcached.xml",
//        		"classpath*:config/spring/springcontext-storage.xml"};
        this.ctx = new ClassPathXmlApplicationContext(files);
    }
    

    public static synchronized ApplicationContext getCtx () {
        return HELPER.ctx;
    }
    
    public static Helper getInstance(){
    	return Helper.HELPER;
    }
    
}
