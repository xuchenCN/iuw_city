package org.train.easy;

import java.util.TreeMap;

/**
 * ���ձ��ʻ��������ʹ�õ�Map 
 * 
 * <b>NOTE : �˷������̰߳�ȫ<b>
 * 
 * @author xuchen
 * @param <V>
 */
public class PercentMap<V> extends TreeMap<Integer,V>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8699770684810659300L;
	
	private int currentRate = 0;
	
	private int totalRage = 0;
	
	/**
	 * ��ʼ��һ���ٷֱȵ�Map ����Ԫ��ʱָ����Ԫ�صĳ��ֱ���
	 */
	public PercentMap(){
		super();
	}
	
	/**
	 * ���ø�Map��totalRage �������� totalRage����currentRate ���ó��� totalRage
	 * @param totalRage
	 */
	public PercentMap(final int totalRage){
		super();
		if(totalRage <= 0){
			throw new IllegalArgumentException("totalRage must be greater than 0");
		}
		this.totalRage = totalRage;
		
	}
	
	/**
	 * ���ݳ��ּ��ʴ��һ��Ԫ��
	 * �������� totalRage����currentRate ���ó��� totalRage
	 * @param rate
	 * @param value
	 * @return
	 * @throws IllegalArgumentException
	 */
	public V put(int rate, V value) throws IllegalArgumentException{
		
		if(totalRage > 0 && rate + currentRate > totalRage){
			throw new IllegalArgumentException("currentRate is out of totalRage " +totalRage);
		}
		
		if(rate <= 0){
			return super.put(0, value);
		}
		
		currentRate += rate;
		return super.put(currentRate, value);
	}
	
	/**
	 * ���ݼ��ʻ��Ӧ�����ֵ�һ��Ԫ��
	 * @param rate
	 * @return
	 */
	public V rateGet(int rate) {
		Integer key = super.ceilingKey(rate);
		if(key != null){
			return super.get(key);
		}
		return null;
	}
	
	/**
	 * �������õļ���������һ��Ԫ��
	 * �������� totalRage ����  1 to totalRage ������������ʳ�ȡ
	 * ��û������totalRage ����  1 to currentRate ���������ȡ
	 * @return
	 */
	public V rateGet() {
		int seed = totalRage > 0 ? (int)(totalRage * Math.random() + 1) : (int)(currentRate * Math.random() + 1)  ;
		Integer key = super.ceilingKey(seed);
		if(key != null){
			return super.get(key);
		}
		return null;
	}
	
	/**
	 * ��� totalRage
	 * @return
	 */
	public int getTotalRage() {
		return totalRage;
	}
	
	/**
	 * ��� currentRate
	 * @return
	 */
	public int getCurrentRate() {
		return currentRate;
	}

	public static void main(String[] args) {
		PercentMap<String> percentMap = new PercentMap<String>();
		percentMap.put(40, "a");
		percentMap.put(20, "b");
		percentMap.put(30, "c");
		percentMap.put(0, "d");
		percentMap.put(10, "e");
		
		int a=0,b=0,c=0,d=0,e=0;
		
		for(int i = 1 ; i <= 100 ; i ++){
//			int seed = (int)(100 * Math.random()) ;
			String value = percentMap.rateGet();
			if("a".equals(value)){
				a ++;
			}else if("b".equals(value)){
				b ++;
			}else if("c".equals(value)){
				c ++;
			}else if("d".equals(value)){
				d ++;
			}
			else if("e".equals(value)){
				e ++;
			}
//			System.out.println("seed : " + seed + " -- " + value);
		}
		
		System.out.println("a : " + a);
		System.out.println("b : " + b);
		System.out.println("c : " + c);
		System.out.println("d : " + d);
		System.out.println("e : " + e);
	}

}

