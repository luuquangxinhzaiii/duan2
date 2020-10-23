/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package da.dao;

/**
 *
 * @author Nugraha
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
 
public class RanDom {
 
    private static final String alpha = "abcdefghijklmnopqrstuvwxyz"; // a-z
    private static final String alphaUpperCase = alpha.toUpperCase(); // A-Z
    private static final String digits = "0123456789"; // 0-9  
    private static final String ALPHA_NUMERIC = alpha + alphaUpperCase + digits;
    
 
    private static Random generator = new Random();
    public String randomAlphaNumeric(int numberOfCharactor) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numberOfCharactor; i++) {
            int number = randomNumber(0, ALPHA_NUMERIC.length() - 1);
            char ch = ALPHA_NUMERIC.charAt(number);
            sb.append(ch);
        }
        return sb.toString();
    }
 
    public static int randomNumber(int min, int max) {
        return generator.nextInt((max - min) + 1) + min;
    }
     
    public static void main(String a[]){
        int numberOfCharactor = 8;
        RanDom rand = new RanDom();
         
        System.out.println("randomString1: " + rand.randomAlphaNumeric(numberOfCharactor));

    }
}
