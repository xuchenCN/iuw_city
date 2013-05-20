package com.iuwcity.dao.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Helper {
    private ApplicationContext ctx;
    
    public Helper() {
        String[] files = new String[] { "config/iuw-persistence-spring.xml" };
        this.ctx = new ClassPathXmlApplicationContext(files);
    }


	public ApplicationContext getCtx() {
		return ctx;
	}

}
