package com.taotao.dataIntegrity.tool;

import java.security.MessageDigest;

/**
 * Created by pei on 2017/8/26.
 */
public class HashUtil {


    public byte[] getSHA2Hex(byte[] str) {
        byte[] cipher_byte;
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(str);
            cipher_byte = md.digest();
            return cipher_byte;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
