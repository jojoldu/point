package com.github.jojoldu.point.core.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import static org.mariadb.jdbc.internal.com.send.ed25519.Utils.bytesToHex;

/**
 * Created by jojoldu@gmail.com on 2018. 3. 22.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

public abstract class KeyGenerator {
    public static String generateUuid() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest salt = MessageDigest.getInstance("SHA-256");
        salt.update(UUID.randomUUID().toString().getBytes("UTF-8"));
        return bytesToHex(salt.digest());
    }
}
