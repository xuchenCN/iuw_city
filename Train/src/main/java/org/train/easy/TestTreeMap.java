package org.train.easy;

import java.util.TreeMap;

public class TestTreeMap {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TreeMap<Integer,String> treeMap = new TreeMap<Integer,String>();
		treeMap.put(10, "a");
		treeMap.put(20, "b");
		treeMap.put(30, "c");
		treeMap.put(40, "d");
		
		System.out.println(treeMap.higherKey(5));
	}

}
