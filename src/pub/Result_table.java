package pub;
import pub.Record_error;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import pub.Database;

public class Result_table {
	//�������ݿⷵ�صĽ����װ�ɱ��ڲ��������ݽṹ
	//ע�⣡�����ĳһ���¼������ֵ��Ϊnull����ʱ��data[i][j]��Ϊһ���ն�����data[i][j]==null���ж�
	public int num_of_row, num_of_column;
	public String column[], data[][];
	
	public Result_table(ResultSet ori) throws Exception{
		try {
			ResultSetMetaData column_set = ori.getMetaData();
			num_of_column = column_set.getColumnCount();
			column = new String[num_of_column];
			for (int i = 0; i < num_of_column; i++)
				column[i] = column_set.getColumnName(i+1);
			ori.last();
			num_of_row = ori.getRow();
			ori.beforeFirst();
			data = new String[num_of_row][num_of_column];
			for (int i = 0; i < num_of_row; i++) {
				ori.next();
				for (int j = 0; j < num_of_column; j++)
					data[i][j] = ori.getString(j+1);
			}
		}catch (Exception e) {
			new Record_error("Result_table�ڽ����ݿⷵ�صĽ��ת��ʱ�����˴���");
			throw new Exception();
		}
	}
	public void person_express(StringBuilder collector, 
			Database database, String mother, String sheet_name, String user) throws Exception{
		for (int i = 0; i < this.num_of_row; i++) {
			for (int j = 1; j < this.num_of_column; j++) {
				if (this.column[j].startsWith("_"))
					continue;
				if (this.data[i][j] == null || this.data[i][j].isEmpty())
					continue;
				collector.append(this.column[j]);
				collector.append("��");
				collector.append(this.data[i][j]);
				collector.append("\n");
			}
			if (this.data[i][0].equals(user)) {
				collector.append(new Link().get_edit_person_link(
						database, mother, sheet_name, user, "����˴������޸���ĸ�����Ϣ"));
			}
			collector.append("\n");
		}
	}
	
	public void department_express(StringBuilder collector) {
		for (int i = 0; i < this.num_of_row; i++) {
			collector.append(this.data[i][0]);
			collector.append("��");
			collector.append(this.data[i][2]);
			collector.append("\n");
		}
	}
	
	void print() {
		System.out.println("num_of_row = " + num_of_row);
		System.out.println("num_of_column = " + num_of_column);
		for (int j = 0; j < num_of_column; j++) {
			System.out.print(column[j] + "\t");
		}System.out.println("");
		for (int i = 0; i < num_of_row; i++) {
			for (int j = 0; j < num_of_column; j++) {
				System.out.print(data[i][j] + "\t");
			}System.out.println("");
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			Database tmp = new Database("sdcs");
			Result_table result_table= tmp.select("sheet","����", "���");
			System.out.println(result_table.num_of_row + "," + result_table.num_of_column);
			for (int i = 0; i < result_table.num_of_column; i++)
				System.out.print(result_table.column[i]+"\t");
			for (int i = 0; i < result_table.num_of_row; i++) {
				for (int j = 0; j < result_table.num_of_column; j++)
					System.out.print(result_table.data[i][j]+"\t");
				System.out.println();
			}
			System.out.println("Success!");
		} catch (Exception e) {
			System.out.println("Error!");
		}
		
	}

}
