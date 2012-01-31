package net.metadata.openannotation.lorestore.util;

import java.util.Random;

public class UIDGenerator {

    private final Random prng = new Random();


    public String newUID() {
        long next;
        synchronized(prng) {
            next = prng.nextLong();
        }
        char[] chars = new char[16];
        for (int i = 0; i < 16; i++) {
            int nibble = (int) ((next >>> (i * 4)) & 0xf);
            if (nibble < 10) {
                chars[i] = (char) ('0' + nibble);
            } else {
                chars[i] = (char) ('A' + nibble - 10);
            }
        }
        return new String(chars);
    }

    public String getSampleUID() {
        return "1A2B3C4D1A2B3C4F";
    }
}
