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
			/*出错则自重启*/
			ServerSocket server=null;
			try {
				/*初始化*/
		        try {
		            server = new ServerSocket(50520);
		            //b)指定绑定的端口，并监听此端口。
		            System.out.println("服务器启动成功");
		            //创建一个ServerSocket在端口5209监听客户请求
		        }catch(Exception e) {
		                System.out.println("没有启动监听："+e);
		                //出错，打印出错信息
		        }
		        Socket socket=null;
		        
		        /*循环等待请求*/
		        while (true) {
		        	try{
			            socket=server.accept();
			            //2、调用accept()方法开始监听，等待客户端的连接 
			            //使用accept()阻塞等待客户请求，有客户
			            //请求到来则产生一个Socket对象，并继续执行
			            //System.out.println("A new Connection!");
			        }catch(Exception e) {
			            System.out.println("Error."+e);
			            //出错，打印出错信息
			        }
			        //3、获取输入流，并读取客户端信息 
			        StringBuilder back = new StringBuilder("");
			        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			        //由Socket对象得到输入流，并构造相应的BufferedReader对象
			        //PrintWriter writer=new PrintWriter(socket.getOutputStream());
			        PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),"utf-8"),true) ;
			        //由Socket对象得到输出流，并构造PrintWriter对象
			        //BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
			        //由系统标准输入设备构造BufferedReader对象
			        
			        /*校验用户名*/
			        /**************************************************/
			        /**************这里的读入里面一定要有回车***********/
			        /*************************************************/
			        String username = in.readLine();
			        username = new Encrypt().decode(username);
			        Database database;
			        //System.out.println("Client: " + username);
			        //在标准输出上打印从客户端用户名
			        try {
			        	database = new Database(username);
			        } catch (Exception e) {
			        	back.append("用户名错误！");
			        	System.out.println("用户名错误！");
			        	continue;
			        }
			        
			        /*获取弹幕数据，并清空数据库*/
			        database.select("_bilibili").bilibili_express(back);
			        database.execute("delete from _bilibili");
			        
			        /*发送数据*/
		            writer.println(back.toString());
		            //向客户端输出该字符串
		            writer.flush();
		            //刷新输出流，使Client马上收到该字符串
		            //System.out.println("Server:" + back);
		            //在系统标准输出上打印读入的字符串
			        
			        //5、关闭资源 
			        writer.close(); //关闭Socket输出流
			        in.close(); //关闭Socket输入流
			        socket.close(); //关闭Socket
		            //System.out.println("close");
			        
		        }
		    }catch(Exception e) {//出错，打印出错信息
		        System.out.println("Error."+e);
		        try {server.close(); //关闭ServerSocket
		        } catch (Exception ee) {}
		    }
		}
	}
}
