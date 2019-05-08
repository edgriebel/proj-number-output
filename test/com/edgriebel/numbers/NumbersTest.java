package com.edgriebel.numbers;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;

public class NumbersTest {

    public static final String [][] RANDOMS = {
            {"10", "Ten"},
            {"12", "Twelve"},
            {"42", "Forty two"},
            {"0", "Zero"},
            {"13", "Thirteen"},
            {"1234", "One thousand two hundred and thirty four"},
            {"12345", "Twelve thousand three hundred and forty five"},
            {"123456", "One hundred and twenty three thousand four hundred and fifty six"}, 
            {"1234567", "One million two hundred and thirty four thousand five hundred and sixty seven"}, 
            {"300", "Three hundred"},
            {"200", "Two hundred" },
            {"20", "Twenty" },
            {"9", "Nine"},
            {"312", "Three hundred and twelve"},
            {"321", "Three hundred and twenty one"},
    };

    private Numbers n;

    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();

    @Before
    public void setUp() {
        // create a fresh instance for each test
        n = new Numbers();
    }

    @Test
    public void testUnwrap() {
        assertThat("null list", Numbers.unwrap(null), is(""));
        assertThat("[]", Numbers.unwrap(Arrays.asList()), is(""));
        assertThat("[x]", Numbers.unwrap(Arrays.asList("x")), is("x"));
        assertThat("[x,y]", Numbers.unwrap(Arrays.asList("x","y")), is("x y"));
        assertThat("[x,y,z]", Numbers.unwrap(Arrays.asList("x","y","z")), is("x y z"));
    }

    @Test
    public void testWrap() {
        assertThat("null", Numbers.wrap(null), notNullValue());
        assertThat("null", Numbers.wrap(null).size(), is(0));
        assertThat("wrapped \"x\"", Numbers.wrap("x"), everyItem(is("x")));
    }

    @Test
    public void testOnesToNum_FromArray() {
        for (int i=0; i<10; i++) {
            String ret = n.onesToNum(i);
            errorCollector.checkThat("Incorrect translation for single-digit", Numbers.ONES_NUMBERS[i], is(ret));
        }
    }

    /*
     * This is similar to testOnesToNum_FromArray except that this 
     * will catch incorrect changes in the ONES_NUMBERS array.
     * This test helps us avoid a test like "1+1 == 1+1" instead of desired "1+1 == 2"
     * This will also look for misspellings in ONES_NUMBERS
     */ 
    @Test
    public void testOnesToNum_Hardcoded() {
        assertThat(n.onesToNum(1), is("one"));
        assertThat(n.onesToNum(2), is("two"));
        assertThat(n.onesToNum(3), is("three"));
        assertThat(n.onesToNum(4), is("four"));
        assertThat(n.onesToNum(5), is("five"));
        assertThat(n.onesToNum(6), is("six"));
        assertThat(n.onesToNum(7), is("seven"));
        assertThat(n.onesToNum(8), is("eight"));
        assertThat(n.onesToNum(9), is("nine"));
    }

    @Test
    public void testTensToNum_Hardcoded() {
        assertThat(n.tensToNum(10), hasItem("ten"));
        assertThat(n.tensToNum(20), hasItem("twenty"));
        assertThat(n.tensToNum(30), hasItem("thirty"));
        assertThat(n.tensToNum(40), hasItem("forty"));
        assertThat(n.tensToNum(50), hasItem("fifty"));
        assertThat(n.tensToNum(60), hasItem("sixty"));
        assertThat(n.tensToNum(70), hasItem("seventy"));
        assertThat(n.tensToNum(80), hasItem("eighty"));
        assertThat(n.tensToNum(90), hasItem("ninety"));
    }

    @Test
    public void testTensToNum_x0() {
        for (int i=1; i<10; i++) {
            List<String> ret = n.tensToNum(i*10);
            errorCollector.checkThat("List shouldn't be null: " + i, 
                    ret, notNullValue());
            errorCollector.checkThat("There should be only one array element: " + i, 
                    ret.size(), is(1));
            errorCollector.checkThat("Incorrect translation for tens-numbers: " +i, 
                    ret.get(0), is(Numbers.TENS_NUMBERS[i-1]));
        }
    }

    @Test
    public void testTensToNum_1_to_99() {
        // 10,20,30,etc. is being tested in testTensToNum_x0
        for (int i=2; i<10; i++) {
            for (int j=1; j<10; j++) {
                int testNum = i*10 + j;
                List<String> ret = n.tensToNum(testNum);
                errorCollector.checkThat("List shouldn't be null: " + testNum, 
                        ret, notNullValue());
                errorCollector.checkThat("There should be two array elements: " + testNum, 
                        ret.size(), is(2));
                errorCollector.checkThat("Incorrect translation for tens place: " + testNum, 
                        ret.get(0), is(Numbers.TENS_NUMBERS[i-1]));
                errorCollector.checkThat("Incorrect translation for ones place: " + testNum + ":"+ret, 
                        ret.get(1), is(Numbers.ONES_NUMBERS[j]));
            }
        }
    }

    @Test
    public void testThousands_x00() {
        for (int i=1; i<10; i++) {
            List<String> ret = n.thousands(i*100);

            errorCollector.checkThat("List shouldn't be null: " + i + ": " + ret, 
                    ret, notNullValue());
            errorCollector.checkThat("There should be one element: 'x00 hundred': " + i + ": " + ret, 
                    ret.size(), is(2));
            errorCollector.checkThat("has hundreds-number " + (i*100), 
                    ret.get(0), is(Numbers.ONES_NUMBERS[i]));
            errorCollector.checkThat("has 'hundreds': " + i + ": " + ret,
                    ret.get(1), is("hundred"));
        }
    }

    @Test
    public void testToEnglish_HasAnd() {
        assertThat("100 shouldn't have and", n.toEnglish(100), is("One hundred"));
        assertThat("101 *should*  have and", n.toEnglish(101), is("One hundred and one"));
        assertThat("99 shouldn't have and", n.toEnglish(99), is("Ninety nine"));
        assertThat("100,000 shouldn't have and", n.toEnglish(100_000), is("One hundred thousand"));
        assertThat("100,001 shouldn't have and", n.toEnglish(100_001), is("One hundred thousand one"));
        assertThat("101,001 *should*  have and", n.toEnglish(101_001), is("One hundred and one thousand one"));
        assertThat("99 shouldn't have and", n.toEnglish(99), is("Ninety nine"));
    }

    @Test
    public void testToEnglish() {
        assertThat(n.toEnglish(100_000), is("One hundred thousand"));
        assertThat(n.toEnglish(1_000_000), is("One million"));
        assertThat(n.toEnglish(1_000_000_000), is("One billion"));
        assertThat(n.toEnglish(1_000_000_000_000L), is("One trillion"));
        assertThat(n.toEnglish(1_000_000_000_000_000L), is("One quadrillion"));
        assertThat(n.toEnglish(1_000_000_000_000_000_000L), is("One quintillion"));
    }

    @Test
    public void testToEnglish_GivenExamples() {
        assertThat(n.toEnglish(0), is("Zero"));
        assertThat(n.toEnglish(13), is("Thirteen"));
        assertThat(n.toEnglish(85), is("Eighty five"));
        assertThat(n.toEnglish(5237), is("Five thousand two hundred and thirty seven"));
    }

    @Test
    public void testToEnglish_Hardcoded() {
        for (String [] val : RANDOMS) {
            long currNum = Long.parseLong(val[0]);
            errorCollector.checkThat(val[0], 
                    n.toEnglish(currNum), is(val[1]));
            // now negate and try again....
            if (!val[0].equals("0"))
                errorCollector.checkThat("-"+val[0], 
                        n.toEnglish(-1*currNum), is("Negative " + val[1].toLowerCase()));
        }
    }
    
    @Test
    public void testToEnglish_Limits() { 
        assertThat("For 0", n.toEnglish(0), is("Zero"));
        assertThat("For " + String.format("%,d",Integer.MAX_VALUE), n.toEnglish(Integer.MAX_VALUE), 
                is("Two billion one hundred and forty seven million four hundred and eighty three thousand six hundred and forty seven"));
        assertThat("For " + String.format("%,d",Integer.MIN_VALUE), n.toEnglish(Integer.MIN_VALUE), 
                is("Negative two billion one hundred and forty seven million four hundred and eighty three thousand six hundred and forty eight"));
        assertThat("For " + String.format("%,d",Long.MAX_VALUE), n.toEnglish(Long.MAX_VALUE), 
                is("Nine quintillion "
                        + "two hundred and twenty three quadrillion three hundred and seventy two trillion "
                        + "thirty six billion eight hundred and fifty four million "
                        + "seven hundred and seventy five thousand eight hundred and seven"));
        assertThat("For " + String.format("%,d",Long.MIN_VALUE), n.toEnglish(Long.MIN_VALUE), 
                is("Negative nine quintillion "
                        + "two hundred and twenty three quadrillion three hundred and seventy two trillion "
                        + "thirty six billion eight hundred and fifty four million "
                        + "seven hundred and seventy five thousand eight hundred and eight"));
    }

}
