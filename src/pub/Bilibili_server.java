package pub;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter; 
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import pub.Database;
import pub.Encrypt;
import pub.Result_table;

public class Bilibili_server {

	public Bilibili_server() {}

	public static void main(String[] args) {
		while (true) {
			/*������������*/
			ServerSocket server=null;
			try {
				/*��ʼ��*/
		        try {
		            server = new ServerSocket(50520);
		            //b)ָ���󶨵Ķ˿ڣ��������˶˿ڡ�
		            System.out.println("�����������ɹ�");
		            //����һ��ServerSocket�ڶ˿�5209�����ͻ�����
		        }catch(Exception e) {
		                System.out.println("û������������"+e);
		                //������ӡ������Ϣ
		        }
		        Socket socket=null;
		        
		        /*ѭ���ȴ�����*/
		        while (true) {
		        	try{
			            socket=server.accept();
			            //2������accept()������ʼ�������ȴ��ͻ��˵����� 
			            //ʹ��accept()�����ȴ��ͻ������пͻ�
			            //�����������һ��Socket���󣬲�����ִ��
			            //System.out.println("A new Connection!");
			        }catch(Exception e) {
			            System.out.println("Error."+e);
			            //������ӡ������Ϣ
			        }
			        //3����ȡ������������ȡ�ͻ�����Ϣ 
			        StringBuilder back = new StringBuilder("");
			        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			        //��Socket����õ�����������������Ӧ��BufferedReader����
			        //PrintWriter writer=new PrintWriter(socket.getOutputStream());
			        PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"utf-8"),true) ;
			        //��Socket����õ��������������PrintWriter����
			        //BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
			        //��ϵͳ��׼�����豸����BufferedReader����
			        
			        /*У���û���*/
			        /**************************************************/
			        /**************����Ķ�������һ��Ҫ�лس�***********/
			        /*************************************************/
			        String username = in.readLine();
			        username = new Encrypt().decode(username);
			        Database database;
			        //System.out.println("Client: " + username);
			        //�ڱ�׼����ϴ�ӡ�ӿͻ����û���
			        try {
			        	database = new Database(username);
			        } catch (Exception e) {
			        	back.append("�û�������");
			        	System.out.println("�û�������");
			        	continue;
			        }
			        
			        /*��ȡ��Ļ���ݣ���������ݿ�*/
			        database.select("_bilibili").bilibili_express(back);
			        database.execute("delete from _bilibili");
			        
			        /*��������*/
		            writer.println(back.toString());
		            //��ͻ���������ַ���
		            writer.flush();
		            //ˢ���������ʹClient�����յ����ַ���
		            //System.out.println("Server:" + back);
		            //��ϵͳ��׼����ϴ�ӡ������ַ���
			        
			        //5���ر���Դ 
			        writer.close(); //�ر�Socket�����
			        in.close(); //�ر�Socket������
			        socket.close(); //�ر�Socket
		            //System.out.println("close");
			        
		        }
		    }catch(Exception e) {//������ӡ������Ϣ
		        System.out.println("Error."+e);
		        try {server.close(); //�ر�ServerSocket
		        } catch (Exception ee) {}
		    }
		}
	}
}
