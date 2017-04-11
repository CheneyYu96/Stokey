package org.xeon.stockey.ui.utility;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javafx.beans.property.SimpleStringProperty;

/**
 * @author ymc
 * @version 创建时间：2016年3月8日 下午9:05:28
 *
 */
public class DateUtil {

	private static final String DATE_PATTERN = "yyyy-mm-dd";

	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);

	public static String format(LocalDate date) {
		if (date == null) {
			return null;
		}

		return DATE_FORMATTER.format(date);
	}

	public static SimpleStringProperty formatProperty(LocalDate date) {
		if (date == null) {
			return null;
		}
//		System.out.println(date);
//		String dateString = DATE_FORMATTER.format(date);
		return new SimpleStringProperty(date.toString());
	}

	public static LocalDate parse(String dateString) {
		String[] splitDate = dateString.split("-");

		int[] ymd = {
				Integer.parseInt(splitDate[0]),
				Integer.parseInt(splitDate[1]),
				Integer.parseInt(splitDate[2])
		};

		try {
			return LocalDate.of(ymd[0],ymd[1],ymd[2]);
		} catch (DateTimeParseException e) {

			return null;
		}
	}

	public static String toString(LocalDate date){
		String result = date.toString();
		String[] spl = result.split("-");

		result = spl[0];
		for(int i = 1 ;i<spl.length; i++) {
			if (spl[i].charAt(0)=='0'){
				spl[i] = spl[i].substring(1);
			}
		}
		result = result+"-"+spl[1]+"-"+spl[2];

		return result;

	}

	public static boolean validDate(String dateString) {
		return DateUtil.parse(dateString) != null;
	}
}
