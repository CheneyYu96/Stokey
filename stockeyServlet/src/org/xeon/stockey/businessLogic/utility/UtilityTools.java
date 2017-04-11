package org.xeon.stockey.businessLogic.utility;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UtilityTools {
	public static String Cal2String(Object cal){
		if(cal==null) return null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(((Calendar)cal).getTime());
	}

	public static Calendar String2Cal(String s){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(sdf.parse(s));
		} catch (ParseException e) {System.out.println("parse failed!!!");}
		return cal;
	}
	public static String double2String(double src){
		DecimalFormat decimalFormat = new DecimalFormat("############.##########");
		return decimalFormat.format(src);  
	}
}
