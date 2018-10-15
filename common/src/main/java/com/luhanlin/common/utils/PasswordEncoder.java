package com.luhanlin.common.utils;

import java.security.MessageDigest;

/**
 * 盐加密
 * <类详细描述>
 *
 * @author luhanlin
 * @version [V_1.0.0, 2018/5/10 17:39]
 */
public class PasswordEncoder {
    private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    private Object salt;
    private String algorithm;

    public PasswordEncoder(Object salt, String algorithm) {
        this.salt = salt;
        this.algorithm = algorithm;
    }

    public String encode(String rawPass) {
        String result = null;
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            //加密后的字符串
            result = byteArrayToHexString(md.digest(mergePasswordAndSalt(rawPass).getBytes("utf-8")));
        } catch (Exception ex) {
        }
        return result;
    }

    public boolean isPasswordValid(String encPass, String rawPass) {
        String pass1 = "" + encPass;
        String pass2 = encode(rawPass);

        return pass1.equals(pass2);
    }

    private String mergePasswordAndSalt(String password) {
        if (password == null) {
            password = "";
        }

        if ((salt == null) || "".equals(salt)) {
            return password;
        } else {
            return password + "{" + salt.toString() + "}";
        }
    }

    /**
     * 转换字节数组为16进制字串
     * @param b 字节数组
     * @return 16进制字串
     */
    private static String byteArrayToHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0)
            n = 256 + n;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }


//    public static void main(String[] args) {
//        String salt = "be5e0323a9195ade5f56695ed9f2eb6b036f3e6417115d0cbe2fb9d74d8740406838dc84f152014b39a2414fb3530a40bc028a9e87642bd03cf5c36a1f70801e";
//        PasswordEncoder encoderMd5 = new PasswordEncoder(salt, "MD5");
//        String encode = encoderMd5.encode("test");
//        System.out.println(encode);
//        boolean passwordValid = encoderMd5.isPasswordValid("c21feb87d79fd42e4336e4c231785ff9", "test");
//        System.out.println(passwordValid);
//
//        PasswordEncoder encoderSha = new PasswordEncoder(salt, "SHA");
//        String pass2 = encoderSha.encode("test");
//        System.out.println(pass2);
//        boolean passwordValid2 = encoderSha.isPasswordValid("409cf43cbdc92e1979018b2e2fdc60c7f07673e9", "test");
//        System.out.println(passwordValid2);
//
//    }

}