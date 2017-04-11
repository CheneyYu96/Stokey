package org.xeon.stockey.businessLogic.utility;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A little  tool to judge if a string matches the demand
 * Created by Sissel on 2016/5/31.
 */
public class StringChecker
{
    public static boolean allCharsValid(String str, Set<Character> validSet)
    {
        for (char c : str.toCharArray())
        {
            if (!validSet.contains(c))
            {
                return false;
            }
        }
        return true;
    }

    public static boolean hasUppercase(String str)
    {
        Pattern pattern = Pattern.compile("[A-Z]");
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }

    public static boolean hasLowercase(String str)
    {
        Pattern pattern = Pattern.compile("[a-z]");
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }

    public static boolean hasNumber(String str)
    {
        Pattern pattern = Pattern.compile("[0-9]");
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }

    public static boolean isEmailFormat(String str)
    {
        // This regex is copied from the net /shamed
        String emailRegex = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(str).matches();
    }
}

