package org.train.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class Helper {
	private ApplicationContext ctx;
    private static Helper      HELPER = new Helper();
    
    private Helper() {
        String[] files = new String[] { "classpath*:config/applicationContext.xml" };
        this.ctx = new ClassPathXmlApplicationContext(files);
    }
    

    public static synchronized ApplicationContext getCtx () {
        return HELPER.ctx;
    }
    
    public static Helper getInstance(){
    	return Helper.HELPER;
    }
    
}
