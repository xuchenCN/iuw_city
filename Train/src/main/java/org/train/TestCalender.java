package org.train;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TestCalender {
	
	public static void main(String[] args) throws ParseException {
		Date s = new SimpleDateFormat("yyyy-MM-dd").parse("2011-05-30");
		Date d = new SimpleDateFormat("yyyy-MM-dd").parse("2011-05-31");

		System.out.println(s.compareTo(d));
	}
}
