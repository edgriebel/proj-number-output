package com.edgriebel.numbers;

import java.util.Scanner;

public class NumbersTester {
    /**
     * Simple test class for {@link Numbers}. <br/>
     * It repeatedly prompts users for a number, 
     * does some minimal error checking,
     * and prints the value of the number. <br/>
     * The user exits by entering a blank line.<br/>
     * The user can enter negative numbers and 
     * commas to separate thousands. 
     * @param args Command-line arguments are ignored
     */
    public static void main(String [] args) {
        @SuppressWarnings("resource")
        final Scanner in = new Scanner(System.in);
        final Numbers numbers = new Numbers();
        final int maxLength = (Long.MAX_VALUE + "").length();

        while (true) {
            System.out.print("Enter a number to convert or <enter> to quit: ");
            String numStr = in.nextLine();

            if (numStr.isEmpty()) {
                System.out.println("Thanks for playing!");
                break;
            }

            numStr = numStr.trim().replaceAll(",", "");
            if (numStr.isEmpty() || !numStr.matches("^-?[0-9]+$")) {
                System.out.println("Enter a positive or negative integer only, please try again...\n");
                continue;
            }
            
            if (numStr.replaceAll("^-", "").length() > maxLength) {
                System.out.printf("Please enter a number between %,d and %,d\n\n", 
                        Long.MIN_VALUE, Long.MAX_VALUE);
                continue;
            }

            final long num = Long.parseLong(numStr);
            System.out.printf("%,10d is %s\n\n", num, numbers.toEnglish(num));
        }
    }
}
