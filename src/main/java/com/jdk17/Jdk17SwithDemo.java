package com.jdk17;

import org.junit.Test;

public class Jdk17SwithDemo {



    @Test
    public  void swithDemo() {
        String str = "hello";
        switch (str) {
            case "hello" -> System.out.println("hello");
            case "world" -> System.out.println("world");
            default -> System.out.println("default");
        }

        //instanceof 类型模式匹配,根据类型进行匹配
        Object obj = 12;
        switch (obj){
            case null -> System.out.println("null");
            case Integer integer -> System.out.println("integer");
            case String string -> System.out.println("string");
            default -> System.out.println("default");
        }

    }


    /**文本块 Text Blocks的标准化
     * 1.文本块是一个多行字符串，它可以包含换行符和引号，而不需要转义。
     */
    @Test
    public void testBlocks(){
        String html=
                """
                    <html>
                        <body>
                            <p>Hello, world</p>
                        </body>
                    </html>
                """;
        System.out.println(html);

    }
}
