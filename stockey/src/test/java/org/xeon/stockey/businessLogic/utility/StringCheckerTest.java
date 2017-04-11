package org.xeon.stockey.businessLogic.utility;

import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * A UnitTest for the StringChecker tool
 * Created by Sissel on 2016/5/31.
 */
public class StringCheckerTest
{

    @Test
    public void testAllCharsValid() throws Exception
    {
        // init charset
        Character[] chars = new Character[]{'a', 'b', 'c', '1', '2', '3', '!', '$', '@', '_'};
        Set<Character> charSet = new HashSet<>();
        Collections.addAll(charSet, chars);

        assertEquals(true, StringChecker.allCharsValid("", charSet));
        assertEquals(true, StringChecker.allCharsValid("abc123!@$_", charSet));
        assertEquals(true, StringChecker.allCharsValid("aabbc321", charSet));
        assertEquals(false, StringChecker.allCharsValid("dabc", charSet));
        assertEquals(false, StringChecker.allCharsValid("abc 123", charSet));
        assertEquals(false, StringChecker.allCharsValid("abc\n123", charSet));
    }

    @Test
    public void testHasUppercase() throws Exception
    {
        assertEquals(true, StringChecker.hasUppercase("ABCLJI"));
        assertEquals(false, StringChecker.hasUppercase("123143"));
        assertEquals(true, StringChecker.hasUppercase("Abc123"));
        assertEquals(false, StringChecker.hasUppercase("abc123"));
        assertEquals(false, StringChecker.hasUppercase("abcdfad"));
        assertEquals(false, StringChecker.hasUppercase(""));
        assertEquals(false, StringChecker.hasUppercase("  "));
        assertEquals(false, StringChecker.hasUppercase("\n\t"));
    }

    @Test
    public void testHasLowercase() throws Exception
    {
        assertEquals(false, StringChecker.hasLowercase("ABCLJI"));
        assertEquals(false, StringChecker.hasLowercase("123143"));
        assertEquals(true, StringChecker.hasLowercase("Abc123"));
        assertEquals(true, StringChecker.hasLowercase("abc123"));
        assertEquals(true, StringChecker.hasLowercase("abcdfad"));
        assertEquals(false, StringChecker.hasLowercase(""));
        assertEquals(false, StringChecker.hasLowercase("  "));
        assertEquals(false, StringChecker.hasLowercase("\n\t"));
    }

    @Test
    public void testHasNumber() throws Exception
    {
        assertEquals(false, StringChecker.hasNumber("ABCLJI"));
        assertEquals(true, StringChecker.hasNumber("123143"));
        assertEquals(true, StringChecker.hasNumber("Abc123"));
        assertEquals(true, StringChecker.hasNumber("abc123"));
        assertEquals(false, StringChecker.hasNumber("abcdfad"));
        assertEquals(false, StringChecker.hasNumber(""));
        assertEquals(false, StringChecker.hasNumber("  "));
        assertEquals(false, StringChecker.hasNumber("\n\t"));
    }

    @Test
    public void testIsEmailFormat() throws Exception
    {
        assertEquals(true, StringChecker.isEmailFormat("chengbutang@163.com"));
        assertEquals(true, StringChecker.isEmailFormat("chengbutang@1.om"));
        assertEquals(true, StringChecker.isEmailFormat("wjr14@software.nju.edu.cn"));
        assertEquals(false, StringChecker.isEmailFormat("chengbutang1334"));
    }
}