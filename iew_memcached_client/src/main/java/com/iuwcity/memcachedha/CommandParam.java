package com.iuwcity.memcachedha;

public class CommandParam {
	
	private String methodName;
	private Class<?>[] types;
	private Object[] args;

	public CommandParam(String methodName, Class<?>[] types, Object[] args) {
		super();
		this.methodName = methodName;
		this.types = types;
		this.args = args;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public void setTypes(Class<?>[] types) {
		this.types = types;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

	public String getMethodName() {
		return methodName;
	}

	public Class<?>[] getTypes() {
		return types;
	}

	public Object[] getArgs() {
		return args;
	}

}
