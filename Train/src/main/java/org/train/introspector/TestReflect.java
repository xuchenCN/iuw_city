package org.train.introspector;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import com.iuwcity.storage.bean.UserInfo;

public class TestReflect {
	
	public static void main(String[] args) throws SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException, IntrospectionException {
		
		/*
		Constructor c = UserInfo.class.getConstructor(new Class[] {});
		Object newBean = c.newInstance(new Object[] {});
		
		BeanInfo beanInfo = Introspector.getBeanInfo(newBean.getClass());
		PropertyDescriptor[] props = beanInfo.getPropertyDescriptors();
		for(PropertyDescriptor p : props){
			System.out.println(p.getDisplayName() + ":" + p.getPropertyType() + ":" + p.getReadMethod().getName());
			if(p.getName().equals("age")){
				Method wm = p.getWriteMethod();
				 Object oo = wm.invoke(newBean, new Object[]{55});
				 System.out.println(((UserInfo)newBean).getAge());
			}
		}
		*/
		
		Constructor c = UserInfo.class.getConstructor(new Class[] {});
		Object newBean = c.newInstance(new Object[] {});
		BeanInfo beanInfo = Introspector.getBeanInfo(newBean.getClass());
		PropertyDescriptor[] props = beanInfo.getPropertyDescriptors();
		for(PropertyDescriptor p : props){
			System.out.println(p.getName());
		}
		System.out.println("----------");
		for(Field f :newBean.getClass().getDeclaredFields()){
			System.out.println(f.getName() + ":" + f.get(newBean));
		}
		
	}
//		Constructor c = clazz.getDeclaredConstructor(null);
//		Animal d = c.newInstance(null);
//		System.out.println(d);
}
