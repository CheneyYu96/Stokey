package org.xeon.stockey.businessLogic.utility;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

/**
 * Created by Sissel on 2016/3/8.
 */
public class DateTypeConverter
{
    public static LocalDate convert(Calendar calendar)
    {
        return LocalDate.of
                (calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
    }

    public static Calendar convert(LocalDate localDate)
    {
        Calendar calendar = UtilityTools.String2Cal(localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        return calendar;
    }
}
