package com.luhanlin.zuulgateway.test;

/**
 * 类详细描述：
 *
 * @author Mr_lu
 * @version 1.0
 * @mail allen_lu_hh@163.com
 * 创建时间：2018/11/4 10:04 PM
 */
public class Test01 {

    private static String stringStatic = "old string";
    private static StringBuilder stringBuilderStatic = new StringBuilder("old string builder");

//    public static void main(String[] args){
//        Scanner scan = new Scanner(System.in);
//        String str = scan.nextLine();
//        StringBuffer sb = new StringBuffer(str);
//        sb.reverse();   // 反转
//        System.out.println(sb.toString());
//    }
    public static void method(String stringStatic){
        stringStatic = "new string";
    }

    public static void method(StringBuilder stringBuilderStatic1, StringBuilder stringBuilderStatic2){
        stringBuilderStatic1.append(".method.first=");
        stringBuilderStatic2.append("method.second=");
        // 重新引用变量
        stringBuilderStatic1 = new StringBuilder("new string builder");
        stringBuilderStatic1.append("new method`s append");
        stringBuilderStatic2.append("old method`s append");
    }

//    public static void main(String[] args) {
//        method(stringStatic);
//        System.out.println(stringStatic);
//
//        method(stringBuilderStatic, stringBuilderStatic);
//        System.out.println(stringBuilderStatic);
//    }

}
