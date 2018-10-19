package com.luhanlin.demoservice;

import org.jasypt.encryption.StringEncryptor;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoServiceApplicationTests {

    @Autowired
    StringEncryptor encryptor;

    @Test
    public void getPass() {
        String name = encryptor.encrypt("luhanlin");
        String password = encryptor.encrypt("123");
//        gbmWUDwPFVCJSRyNmAzU14WX0fIKzmoQ----------------
//        rILkkAsljh/nwfxVnZ11yw==----------------
        System.out.println(name+"----------------");
        System.out.println(password+"----------------");
        Assert.assertTrue(name.length() > 0);
        Assert.assertTrue(password.length() > 0);
    }

    @Test
    public void contextLoads() {
    }

}
