package org.train.easy;

public class TestTor {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		char a = 'A';
		char b = (char)(a ^ 'R');
		System.out.println(b);
		System.out.println((char)(b ^ 'R'));
	}

}
