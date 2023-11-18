package com.casa.app.util.email;

import java.security.SecureRandom;
import java.util.Base64;

public class Random {
    public static String makeRandomString(int i) {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[i];
        random.nextBytes(bytes);
        Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
        String token = encoder.encodeToString(bytes);
        return token;
    }
}
