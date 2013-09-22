/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rabin.karp;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import junit.framework.Assert;

/**
 *
 * @author shen
 */
public class RabinKarpTest
{

    /**
     * Quick test to see they actually work functionally.
     */
    @Test
    public void testFunctionality()
    {
        RabinKarp rk = new RabinKarp();

        List<TestCase> testCases = new ArrayList<>();
        testCases.add(new TestCase("cabcab", "bc"));
        testCases.add(new TestCase("u4orejinhelloworldv,r3uiowjefksnd", "helloworld"));
        testCases.add(new TestCase("applefishy", "fishy"));
        testCases.add(new TestCase("apple24242424242424244424244442424fishy", "42"));
        testCases.add(new TestCase("9r8y3whfesgre88u2jannckjd", "ann"));
        testCases.add(new TestCase("withjusticeforall", "justice"));
        testCases.add(new TestCase("withjusticeforall", "ju"));
        testCases.add(new TestCase("92874972395ythikwejfsd", "orangesbannanananananann"));
        testCases.add(new TestCase("9287nnnnaananananythikwejfsd", "m"));
        testCases.add(new TestCase("lahfkwfgrthiokjwds976t4wge35st46gw35h2dn4gw65hrdtefhoigegjklesfjdkjoieghrbjfvcm", "976t4wge35st46gw35h2dn4gw65hrdtm"));
        testCases.add(new TestCase("capitalfistringweo29rstristringngu0weofhidjv", "string"));

        for (TestCase testCase : testCases)
        {
            String s = testCase.getS();
            String sub = testCase.getSub();
            int expected = s.indexOf(sub);
            int actualRK = rk.RabinKarp(s, sub);
            int actualNS = rk.NaiveSearch(s, sub);
            Assert.assertEquals("RabinKarp..   looking for \n\t" + sub + "\nin\n\t" + s + "\n\texpected: " + expected + "\n\t actual: " + actualRK, expected, actualRK);
            Assert.assertEquals("NaiveSearch.. looking for \n\t" + sub + "\nin\n\t" + s + "\n\texpected: " + expected + "\n\t actual: " + actualNS, expected, actualNS);
        }
    }

    class TestCase
    {

        private String s;
        private String sub;

        public TestCase(String s, String sub)
        {
            this.s = s;
            this.sub = sub;
        }

        public String getS()
        {
            return s;
        }

        public String getSub()
        {
            return sub;
        }
    }
}