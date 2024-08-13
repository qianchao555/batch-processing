package com.knowledge.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2023/5/4 21:44
 * @version:1.0
 */
public class BIOServer {
    public static void main(String[] args) throws IOException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        ServerSocket serverSocket=new ServerSocket(6666);

        while (true){
            //监听，等待客户端连接
            System.out.println("阻塞等待客户端的连接。。。");
            Socket socket=serverSocket.accept();
            System.out.println("连接到一个客户端");
            Runnable runnable=new Runnable() {
                @Override
                public void run() {
                    handler(socket);
                }
            };
            executorService.execute(runnable);
        }
    }

    /**
     * 和客户端通讯
     * @param socket
     */
    public static void handler(Socket socket){
        byte[] bytes = new byte[1024];
        //获取输入流
        try{

            InputStream inputStream = socket.getInputStream();
            while (true){
                System.out.println("当前线程id："+Thread.currentThread().getId());
                System.out.println("阻塞等待客户端的消息。。。。");
                int read = inputStream.read(bytes);
                if(read!=-1){
                    System.out.print("输出客户端发送的数据:");
                    System.out.println(new String(bytes,0,read));
                }else {
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            System.out.println("关闭和Client的连接");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
