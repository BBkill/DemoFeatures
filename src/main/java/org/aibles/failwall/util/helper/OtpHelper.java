package org.aibles.failwall.util.helper;

import java.util.Random;

public class OtpHelper {

    private static StringBuilder otpCode = new StringBuilder();

    private static char[] number = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    private final static int SIZE_OF_OTP_NUMBER = 6;

    public static String generateOTP() {
        Random random = new Random();
        for (int i = 0; i < SIZE_OF_OTP_NUMBER; i++) {
            otpCode.append(number[random.nextInt(number.length)]);
        }
        return otpCode.toString();
    }
}
