package org.xeon.stockey.ui.utility;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.text.DecimalFormat;
import java.util.regex.Pattern;

/**
 * Created by yuminchen on 16/4/13.
 */
public class OtherUtil {

    public static String double2String(double src){
        DecimalFormat decimalFormat = new DecimalFormat("############.##########");
        return decimalFormat.format(src);
    }

    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

}
