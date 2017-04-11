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
		try {
			return DATE_FORMATTER.parse(dateString, LocalDate::from);
		} catch (DateTimeParseException e) {
			return null;
		}
	}

	public static boolean validDate(String dateString) {
		return DateUtil.parse(dateString) != null;
	}
}
