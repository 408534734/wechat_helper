package pub;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Send_mail {
	//�ʼ�����ģ��
	public Send_mail(){
		//���캯��Ϊ��
	}

	public boolean send(String mail_address, String tittle, String contents) {
		//�����ʼ����ܣ����ʼ�����ʧ�ܵ�ʱ�򷵻�false
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
            return false;
        }
		return true;
	}

	public static void main(String[] args) {
		long start_time = System.currentTimeMillis();
		if (new Send_mail().send("408534734", "����һ������ʼ�", "�Ժ����������Ǿ��ʼ���ϵ�� :)")==false)
			System.out.println("ȷ�Ϸ���false��");
		long end_time = System.currentTimeMillis();
		System.out.println("finish in " + (end_time - start_time));
	}

}
