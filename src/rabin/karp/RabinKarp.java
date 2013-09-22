/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rabin.karp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author shen
 */
public class RabinKarp
{

    private final int BASE = 2;
    private final Map<Integer, Integer> POWER_LOOKUP;

    public RabinKarp()
    {
        POWER_LOOKUP = new HashMap<>();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        RabinKarp rk = new RabinKarp();
        rk.run();
    }
    
    private String randomString(String append)
    {
        int stringLen = 5000000;
        StringBuilder sb = new StringBuilder(stringLen);
        Random rng = new Random(System.currentTimeMillis());
        String validChars = "qwertyuiopasdfghjklzxcvbnm,/.,][';1234567890-=!@#$%^&*()7465312798465312132twgz8ukivybt4~`][{}<>|?:>><_+)/*-;";
        for(int i = 0 ; i < stringLen; i ++)
        {
            sb.append(validChars.charAt(rng.nextInt(validChars.length())));
        }
        sb.append(append);
        return sb.toString();
    }

    public void run()
    {
        List<Long> naiveTimes = new ArrayList<>();
        List<Long> rkTimes = new ArrayList<>();
        List<TestCase> testCases = new ArrayList<>();
        
        testCases.add(new TestCase(randomString("cabcab"), "bc"));
        testCases.add(new TestCase(randomString("u4orejinhelloworldv,r3uiowjefksnd"), "helloworld"));
        testCases.add(new TestCase(randomString("applefishy"), "fishy"));
        testCases.add(new TestCase(randomString("apple24242424242424244424244442424fishy"), "42"));
        testCases.add(new TestCase(randomString("9r8y3whfesgre88u2jannckjd"), "ann"));
        testCases.add(new TestCase(randomString("withjusticeforall"), "justice"));
        testCases.add(new TestCase(randomString("withjusticeforall"), "ju"));
        testCases.add(new TestCase(randomString("92874972395ythikwejfsd"), "orangesbannanananananann"));
        testCases.add(new TestCase(randomString("9287nnnnaananananythikwejfsd"), "m"));
        testCases.add(new TestCase(randomString("lahfkwfgrthiokjwds976t4wge35st46gw35h2dn4gw65hrdtefhoigegjklesfjdkjoieghrbjfvcm"), "976t4wge35st46gw35h2dn4gw65hrdtm"));
        testCases.add(new TestCase(randomString("capitalfistringweo29rstristringngu0weofhidjv"), "string"));
        
        for (int i = 0; i < 10; i++)
        {
            for (TestCase testCase : testCases)
            {
                String s = testCase.getS();
                String sub = testCase.getSub();
                if (Math.random() > 0.5)
                {
                    naiveTimes.add(timeNaiveSearch(s, sub));
                    rkTimes.add(timeRabinKarp(s, sub));
                }
                else
                {
                    rkTimes.add(timeRabinKarp(s, sub));
                    naiveTimes.add(timeNaiveSearch(s, sub));
                }
            }
        }
        
        long avgNaive = avgTime(naiveTimes);
        long avgRK = avgTime(rkTimes);
        System.out.println("Naive :: " + avgNaive);
        System.out.println("RK :: " + avgRK);
        
        
    }
    
    private long avgTime(List<Long> times)
    {
        long total = 0;
        for(long l : times)
        {
            total += l;
        }
        return total / times.size();
    }

    private long timeNaiveSearch(String s, String sub)
    {
        long start = System.currentTimeMillis();
        NaiveSearch(s, sub);
        long stop = System.currentTimeMillis();
        return stop - start;
    }

    private long timeRabinKarp(String s, String sub)
    {
        long start = System.currentTimeMillis();
        RabinKarp(s, sub);
        long stop = System.currentTimeMillis();
        return stop - start;
    }

    public int NaiveSearch(String s, String sub)
    {
        int n = s.length();
        int m = sub.length();
        if (m > n)
        {
            return -1;
        }
        outer:
        for (int i = 0; i < n - m + 1; i++)
        {
            inner:
            for (int j = 0; j < m; j++)
            {
                if (s.charAt(i + j) != sub.charAt(j))
                {
                    continue outer;
                }
            }
            return i;
        }
        return -1;
    }

    public int RabinKarp(String s, String sub)
    {
        int n = s.length();
        int m = sub.length();
        if (m > n)
        {
            return -1;
        }
        String prev = s.substring(0, m);
        int hsub = initialHash(sub);
        int hs = initialHash(prev);
        for (int i = 0; i < n - m + 1; i++)
        {
            String check = s.substring(i, i + m);
            if (i == 0)
            {
                hs = initialHash(check);
            }
            else
            {
                hs = rollingHash(hs, prev, check);
            }
            if (hs == hsub)
            {
                if (check.equals(sub))
                {
                    return i;
                }
            }
            prev = check;
        }
        return -1;
    }

    /**
     * Rolling hash function. Hashes via base conversion on each letter using a
     * large prime as the base. Each roll is computed by substracting the value
     * for the first character, then multiplying by the base, then adding the
     * last character on.
     *
     * @param val value of the previous hash
     * @param previous the previous substring
     * @param current the current substring (to hash)
     * @return the hash value of current
     */
    public int rollingHash(int val, String previous, String current)
    {
        int length = current.length();
        int total = val;
        int toSubstract = (previous.charAt(0) - 97) * getPower(length - 1);
        total -= toSubstract;
        total *= BASE;
        total += current.charAt(length - 1) - 97;
        return total;

    }

    public int initialHash(String s)
    {
        int total = 0;
        for (int i = s.length() - 1; i >= 0; i--)
        {
            int asciiValue = s.charAt(i) - 97;
            int multiplier = getPower(s.length() - i - 1);

            total += multiplier * asciiValue;
        }
        return total;
    }

    private int getPower(int i)
    {
        int multiplier;
        if (POWER_LOOKUP.containsKey(i))
        {
            multiplier = POWER_LOOKUP.get(i);
        }
        else
        {
            multiplier = (int) Math.pow(BASE, i);
            POWER_LOOKUP.put(i, multiplier);
        }
        return multiplier;
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
