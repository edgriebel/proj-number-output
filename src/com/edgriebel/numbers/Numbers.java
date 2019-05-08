package com.edgriebel.numbers;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class Numbers {
    private final Logger log = Logger.getLogger(this.getClass().getSimpleName());

    /**
     * Holds English values for 1 to 10. Zero is not filled in as 
     * it is handled as a special case.
     */
    protected static final String [] ONES_NUMBERS = { 
            "", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine" 
    };

    /**
     * Because teen number are different than >= 20 they come from here
     */
    protected static final String [] TEENS_NUMBERS = { 
            "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "ninteen"
    };

    /**
     * English equivalents for tens values.
     */
    protected static final String [] TENS_NUMBERS = {
            "ten", /* placeholder to make lookups work, this is actually handled by TEENS_NUMBERS */
            "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"
    };

    /**
     *  English names for groups of thousands
     */
    protected static final String [] BIG_NUMS = {
            "", "thousand", "million", "billion", "trillion", "quadrillion", "quintillion"
    };

    /**
     * Convenience method that takes a list and returns 
     * every element in a string with a space separating each element. <br/>
     * An empty list or null will return "". 
     *  
     * @param s
     * @return
     */
    protected static String unwrap(final List<String> in) {
        if (in == null) {
            return "";
        }

        return String.join(" ", in);
    }

    /**
     * Convenience method to wrap a single string into a list.
     * 
     * @param in a single string
     * @return the string wrapped into a {@link List}
     */
    protected static List<String> wrap(final String s) {
        if (s == null) {
            return new ArrayList<>();
        }
        return new ArrayList<String>(Arrays.asList(s));
    }

    /**
     * This method converts numbers from 0-9
     * 
     * @param num number between 0 and 9, must be positive
     * @return one, two, three, etc.
     */
    protected String onesToNum(final int num) {
        assert num < 10 : "num should be < 10";
        assert num >= 0 : "num should be >= 0";

        return ONES_NUMBERS[num];
    }

    /**
     * This method converts numbers from 1-99 into English.
     * 
     * @param num number between 1 and 99, must be positive 
     * @return A {@link List} of words, each element is a single word for the number
     */
    protected List<String> tensToNum(final int num) {
        assert num < 100 : "num should be < 100";
        assert num >= 0 : "num should be >= 0";

        List<String> buf = new ArrayList<String>();
        if (num < 10) {
            return wrap(onesToNum(num));
        }

        if (num < 20) {
            return wrap(TEENS_NUMBERS[num-10]);
        }

        buf.add(TENS_NUMBERS[num/10 - 1]);// we want integer division here
        if (num % 10 != 0)
            buf.add(onesToNum(num % 10));
        return buf; 
    }

    /**
     * This method converts numbers from 0-1000 into English 
     * 
     * @param num a number between 0 and 1000, must be positive
     * @return A {@link List} of words, each element is a word in the number 
     */
    protected List<String> thousands(final long num) {
        assert num < 1000 : "num should be < 1000";
        assert num >= 0 : "num should be >= 0";

        if (num == 0)
            return new ArrayList<>();
        List<String> buf = new ArrayList<>();
        if (num >= 100) {
            buf.add(onesToNum((int)num / 100)); // force integer division
            buf.add("hundred");
            if (num % 100 != 0)
                buf.add("and");
        }
        if (num % 100 != 0) { 
            buf.addAll(tensToNum((int)num % 100));
        }
        return buf;
    }

    /**
     * This method takes in a fixed-point number and converts it to English.<p/>
     * It does this by splitting the number into groups of thousands (three digits)
     * and getting the English value for that group. Then it adds the name of group
     * (thousands, millions, etc.)
     *  
     * @param numIn A number between {@link Long#MIN_VAL} and {@link Long#MAX_VAL}
     * @return a string for the number in English
     */
    public String toEnglish(final long numIn) {
        long num = numIn;
        final List<String> buf = new ArrayList<>();

        /*
         * We build up the English by splitting a number into groups of thousands 
         * starting at the right-most numbers (hundreds).
         * We get the English words for the group into a List of Strings, 
         * adds group name (thousands, millions, etc.),
         * and then adds this List to the start of a List of strings we're building up.
         * 
         * After all the numerals are converted, we check if the number was "zero" as a special case.
         * 
         * We then add "negative" to the beginning of the List if the 
         * number was originally negative.
         * 
         * Last, the list of words is transformed into a string.
         */
        
        if (num < 0) {
            /*
             * Originally I negated the numIn and just carried a "isNegative" flag 
             * around but if numIn is MIN_VAL, it can't be negated because abs(MIN_VAL)
             * is 1 greater than MAX_VAL and causes an overflow...back to MIN_VAL!
             */  
            log.fine("Number is negative: " + num);
        }

        /*
         * This loop does the splitting into successive groups of thousands.
         * It could also be done in a recursive style but the logic would have
         * to change, in particular housekeeping of which group each recursion was on.
         */
        final String ret;
        int numOfThousandsGroups = 0;
        while (num != 0) {
            final List<String> thisGroup = thousands(Math.abs(num % 1000));
            // look up group name but only if there are numbers in this group,
            // avoiding bug: 1,000,000 => "one million thousand"
            if (numOfThousandsGroups > 0 && num % 1000 != 0)
                thisGroup.add(BIG_NUMS[numOfThousandsGroups]);
            buf.addAll(0, thisGroup);
            numOfThousandsGroups++;
            log.fine(String.join(" ", buf) + " for number " + num);
            // deliberately truncate with integer division, this is where it eventually reaches zero
            num = num / 1000; 
            log.fine("num now " + num);
        }

        /*
         * If no entries in the list, it's because it's zero (because
         * zero is specifically excluded from the above while loop).
         */
        if (buf.isEmpty()) {
            buf.add("zero");
        }

        if (numIn < 0) {
            buf.add(0, "negative");
        }

        // capitalize the first word per the spec
        String first = buf.remove(0);
        first = Character.toUpperCase(first.charAt(0)) + first.substring(1);
        buf.add(0, first);

        // now convert the words in the buffer to a space-separated string
        ret = unwrap(buf);
        return ret;
    }

}
