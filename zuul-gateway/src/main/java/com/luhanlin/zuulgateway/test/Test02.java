package com.luhanlin.zuulgateway.test;

/**
 * 类详细描述：
 *
 * @author Mr_lu
 * @version 1.0
 * @mail allen_lu_hh@163.com
 * 创建时间：2018/11/6 9:52 PM
 */
public class Test02 {

    public static void main(String[] args) {
        int a =1 , b = 1;
        while (a<=1000){
            int c = 0;
            while(a > b ){
                if (a % b == 0){
//                    System.out.println("b= " + b);
                    c += b;
                }
                b ++;
            }
            if (c == a ){
                System.out.println(a);
                System.out.println("=======");
            }
            a++;
            b = 1;
//            System.out.println( " a   =======" + a);
        }
//        for (int a = 1; a < 1000; a++) {
//            int c = 0;
//            for (int b = 1; b < a; b++) {
//                if (a % b == 0){
//                    System.out.println("b = " + b);
//                    c += b;
//                }
//            }
//            if (c == a){
//                System.out.println(a);
//            }
//            System.out.println("bianli a " + a);
//        }

    }


}
