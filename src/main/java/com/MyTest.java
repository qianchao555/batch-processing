package com;

/**
 * @ClassName Test
 * @Author qianchao
 * @Date 2022/6/17
 * @Version  V1.0
 **/
public class MyTest {
    public static String ss=null;
    final int i;
    public MyTest(){
        i=0;
    }
    public static void main(String[] args) {
        Object []t={};
        System.out.println(t.length);
//        final int i=10;
//        String[] split = "UData,OWData,Sc,IData".split(",");
//
//        if(ArrayUtils.contains(split,"ABC")){
//            System.out.println("ABC在："+ Arrays.toString(split)+"数组中");
//        }else {
//            System.out.println("ABC不在："+Arrays.toString(split)+"数组中");
//        }
//
//        List<Integer> collect = Stream.of("UData","OWData","Sc","IData")
//                .map(String::length)
//                .collect(Collectors.toList());
//        System.out.println(collect);
//
//        MyTest myTest=new MyTest();
//        myTest.tttAb();

    }
    public void tttAb(){
        ss="abc";
        System.out.println(ss);
    }



}
