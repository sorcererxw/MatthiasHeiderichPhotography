package com.sorcererxw.matthiasheidericphotography;

import com.sorcererxw.matthiasheiderichphotography.util.StringUtil;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        String test = "http://static1.squarespace.com/static/53711ac5e4b02c5d204b1870/5698c644b204d5c6658c87f3/5698c667b204d5c6658c8848/1452852843363/MHeiderich-Reflections_01.jpg";
        System.out.println(StringUtil.getFileNameFromLinkWithoutExtension(test));
    }
}