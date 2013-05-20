package org.train.introspector;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.train.introspector.bean.Dog;

public class IntrospectorTool {
	
	public static void main(String[] args) throws IntrospectionException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Dog dog = new Dog();
		dog.setTail("write tail");
		BeanInfo beanInfo = Introspector.getBeanInfo(dog.getClass());
		PropertyDescriptor[] props = beanInfo.getPropertyDescriptors();
		for(PropertyDescriptor p : props){
			System.out.println(p.getDisplayName() + ":" + p.getPropertyType() + ":" + p.getReadMethod().getName());
			if(p.getName().equals("age")){
				Method wm = p.getWriteMethod();
				 Object oo = wm.invoke(dog, new Object[]{55});
				 System.out.println(dog.getAge());
			}
			
			
		}
		
		MethodDescriptor[] methods  = beanInfo.getMethodDescriptors();
		for(MethodDescriptor md : methods){
			System.out.println(md.getMethod().getName());
			
		}
	}
}
