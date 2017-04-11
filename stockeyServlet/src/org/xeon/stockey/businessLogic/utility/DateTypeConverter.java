package org.xeon.stockey.businessLogic.utility;

import java.time.LocalDate;
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
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, localDate.getYear());
        calendar.set(Calendar.MONTH, localDate.getMonth().getValue()-1);
        calendar.set(Calendar.DAY_OF_MONTH, localDate.getDayOfMonth());
        return calendar;
    }
}
