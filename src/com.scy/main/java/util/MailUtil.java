package util;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * Created by SCY on 16/10/16.
 *
 * @fromMailAddr 是发件人邮箱
 * @fromMailPassword 是发件人邮箱密码
 * @mailTransportProtocal 是邮件服务协议  default = "smtp"
 * @mailSmtpHost 如果选择smtp协议,则需要提供smtp服务器地址, 如果是其他协议,则自行注释掉,换成其他协议服务器
 *
 *
 * @sendMail() 有单发的,有群发的,根据参数选择
 */

public class MailUtil {
    private final static Logger LOG = Logger.getLogger(MailUtil.class);

    private static Configuration config;
    private static Properties props = new Properties();
    private static String fromMailAddr ="";
    private static String fromMailPassword="";
    private static String mailTransportProtocal = "smtp";
    private static Integer mailSmtpPort = 25;

    static{
        try{
            config = new PropertiesConfiguration("server.properties");
            fromMailAddr = config.getString("mail.from.addr");
            fromMailPassword = config.getString("mail.from.password");
            String mailSmtpHost = config.getString("mail.smtp.host");
            props.put("mail.smtp.host",mailSmtpHost);
            props.put("mail.transport.protocol", mailTransportProtocal);
            props.put("mail.smtp.port",mailSmtpPort);
        }
        catch(Exception e){
            System.out.println("找不到对应的配置文件");
        }
    }


    public static void sendMail(String content, String mailTitle,List<InternetAddress> destMailAddr) throws Exception{
        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromMailAddr));
        InternetAddress []allAddr  = (InternetAddress[])destMailAddr.toArray(new InternetAddress[destMailAddr.size()]);
        message.setRecipients(Message.RecipientType.TO,allAddr);
        message.setSentDate(new Date());

        message.setContent(content,"text/html;charset=UTF-8");
        message.setSubject(mailTitle);
        //  message.saveChanges();

        Transport transport = session.getTransport();
        transport.connect(fromMailAddr,fromMailPassword);
        transport.sendMessage(message,message.getAllRecipients());
        transport.close();
    }


    public static void sendMail(List<String>destMailAddr,String content, String mailTitle) throws Exception{
        List<InternetAddress>realAddr = new ArrayList<InternetAddress>();
        for(String eachAddr : destMailAddr){
            InternetAddress tmpAddr = new InternetAddress(eachAddr);
            realAddr.add(tmpAddr);
        }
        sendMail(content,mailTitle,realAddr);
    }


    public static void sendMail(String content, String mailTitle, String dstAddr)throws Exception{
        List<String>addrList = new ArrayList<String>();
        addrList.add(dstAddr);
        sendMail(addrList,content,mailTitle);
    }
}