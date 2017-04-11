package org.xeon.stockey.businessLogic.utility;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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

    public static String file2String(String filePath) throws IOException
    {
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        StringBuilder sb = new StringBuilder();
        br.lines().forEach(
                line ->
                {
                    sb.append(line);
                    sb.append("\n");
                } );
        br.close();

        return sb.toString();
    }

    public static String superTrim(String str)
    {
        char[] chars = str.toCharArray();
        int begin = 0, end = chars.length;
        for (int i = 0; i < chars.length; i++)
        {
            if (chars[i] != '\t' && chars[i] != '\n' && chars[i] != ' ' && chars[i] != '\r')
            {
                begin = i;
                break;
            }
        }

        for (int i = chars.length - 1; i >= 0; i--)
        {
            if (chars[i] != '\t' && chars[i] != '\n' && chars[i] != ' ' && chars[i] != '\r')
            {
                end = i + 1;
                break;
            }
        }

        return str.substring(begin, end);
    }

    public static String replaceStr(int start, int length, String origin, String replacement)
    {
        String head = origin.substring(0, start);
        String tail = origin.substring(start + length);
        return head + replacement + tail;
    }

	public static void main(String[] args)
	{
		Calendar cal = Calendar.getInstance();
		System.out.println(UtilityTools.Cal2String(cal));
	}

}
