package com.contoller;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * @description:
 * @author:xiaoyige
 * @createTime:2022/9/19 22:18
 * @version:1.0
 */
public class QqEmailTest {
    public static void main(String[] args) throws MessagingException {
        Session session = createProperties();
        MimeMessage mimeMessage = getMimeMessage(session);
        sendMail(session, mimeMessage);
    }

    /**
     * 构造SMTP邮件服务器的基本环境
     * qq免费邮箱SMTP服务器 smtp.qq.com（端口：25）,使用SSL的加密端口：465
     *
     * @return
     */
    private static Session createProperties() {
        //设置邮件服务器
        Properties properties = new Properties();
        properties.setProperty("mail.host", "smtp.qq.com");
        properties.setProperty("mail.transport.protocol", "smtp");
        //需要验证身份
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.setProperty("mail.smtp.port", "465");
        Session session = Session.getDefaultInstance(properties);
        //debug模式
        session.setDebug(true);
        return session;
    }

    /**
     * 构造邮件
     *
     * @param session
     * @return
     * @throws MessagingException
     */
    private static MimeMessage getMimeMessage(Session session) throws MessagingException {
        MimeMessage mimeMessage = new MimeMessage(session);
        //设置收信人
        mimeMessage.addRecipients(Message.RecipientType.TO, "1376656873@qq.com");
        //抄送
        mimeMessage.addRecipients(Message.RecipientType.CC, "222@qq.com");
        InternetAddress address = new InternetAddress("xiaoyigedl@163.com");
        //邮件发送人
//        Address address = new Address() {
//            @Override
//            public String getType() {
//                return null;
//            }
//
//            @Override
//            public String toString() {
//                return "1376656873@qq.com";
//            }
//
//            @Override
//            public boolean equals(Object o) {
//                return false;
//            }
//        };
        //发送人
        mimeMessage.setFrom(address);

        //邮件主题
        mimeMessage.setSubject("测试邮件主题2");
        //正文
        mimeMessage.setContent("Hello,这是一封测试邮件2", "text/html;charset=utf-8");
        return mimeMessage;
    }

    /**
     * 发送邮件
     *
     * @param session
     * @param mimeMessage
     * @throws MessagingException
     */
    private static void sendMail(Session session, MimeMessage mimeMessage) throws MessagingException {
        Transport transport = session.getTransport();
        transport.connect("smtp.qq.com", "1376656873@qq.com", "imhamvhvogvihega");
        //发送邮件，第二个参数为收件人
        transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
        transport.close();
    }

}
