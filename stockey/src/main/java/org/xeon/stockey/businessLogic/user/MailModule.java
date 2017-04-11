package org.xeon.stockey.businessLogic.user;

/**
 * 处理邮件相关的事务
 * Created by Sissel on 2016/6/5.
 */
public class MailModule
{


    /**
     * 发送验证邮件
     * @param receiverEmail 收件人的邮箱
     * @param verificationLink 完成验证需要的链接
     */
    public static void sendVerificationEmail(String receiverEmail, String verificationLink)
    {
        MailImpl connect = new MailImpl();
        String title = "StockEy Verification";
        String content = "用户您好，请点击以下链接完成邮箱验证： " + verificationLink;
        connect.setAddress("xeon_stockey@163.com", receiverEmail, title);
        connect.send("smtp.163.com", "xeon_stockey@163.com", "1234wssb2", content);
    }
}
