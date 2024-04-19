package com.notetakingapp.api.Utils.crypto;

public abstract class Crypto {

    public static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02X", b));//formats the byte as a two-digit hexadecimal number with leading zeros if necessary.
        }
        return result.toString();
    }

}
