package pub;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Send_mail {

	public Send_mail(String mail_address, String tittle, String contents) {
		try {  
            Properties props = new Properties();
            Configure configure = new Configure();
            props.put("username", configure.get_system_mail_username());   
            props.put("password", configure.get_system_mail_password());   
            props.put("mail.transport.protocol", "smtp" );  
            props.put("mail.smtp.host", "smtp.163.com");  
            props.put("mail.smtp.port", "25" );  
            Session mailSession = Session.getDefaultInstance(props); 

            Message msg = new MimeMessage(mailSession);     
            msg.setFrom(new InternetAddress(configure.get_system_mail_username()));  
            msg.addRecipients(Message.RecipientType.TO, InternetAddress.parse(mail_address));   
            msg.setSubject(tittle);
            msg.setContent("<h3>" + contents + "</h3>","text/html;charset=UTF-8");

            msg.saveChanges();  

            Transport transport = mailSession.getTransport("smtp");  
            transport.connect(props.getProperty("mail.smtp.host"), props  
                    .getProperty("username"), props.getProperty("password"));   
            transport.sendMessage(msg, msg.getAllRecipients());  
            transport.close();     
        } catch (Exception e) {  
            new Record_error("�ʼ�����ʧ�ܣ��ʼ����͵�ַΪ��" + mail_address);
            System.out.println("�ʼ�����ʧ�ܣ��ʼ����͵�ַΪ��" + mail_address);
        }
	}

	public static void main(String[] args) {
		long start_time = System.currentTimeMillis();
		new Send_mail("914989053@qq.com", "����һ������ʼ�", "�Ժ����������Ǿ��ʼ���ϵ�� :)");
		long end_time = System.currentTimeMillis();
		System.out.println("finish in " + (end_time - start_time));
	}

}
