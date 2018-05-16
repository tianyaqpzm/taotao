package com.taotao.dataIntegrity.db;
import it.unisa.dia.gas.jpbc.Element;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class DBOperation {	
	public int insert(String table,String name,byte[]value)  
	{  
		Connection cnn=new DBConnection().getConn();
		int i=0;  
		String sql="insert into "+table+" values(?,?)";  
		try{  
			PreparedStatement preStmt =cnn.prepareStatement(sql);  
			preStmt.setString(1,name);  
			preStmt.setBytes(2,value); 
			i=preStmt.executeUpdate();  
		}  
		catch (SQLException e)  
		{  
			e.printStackTrace();  
		}  
		System.out.println(table+": insert "+i);
		return i;//����Ӱ���������1Ϊִ�гɹ�  
	}  
	
	public int insertBatch(String table,Map<String,byte[]>data)  
	{  

		Connection conn=new DBConnection().getConn(); 
		int commitSize=200;
		int i=0;  
		String sql="insert into "+table+" values(?,?)";
		System.out.println(sql);
		try{
			conn.setAutoCommit(false);
			PreparedStatement preStmt =conn.prepareStatement(sql);
			for (Map.Entry<String, byte[]> entry : data.entrySet()) {
				i++;
				preStmt.clearParameters();
				preStmt.setString(1, entry.getKey());
				preStmt.setBytes(2, entry.getValue());
				preStmt.execute();
				if (i % commitSize == 0)
				{ conn.commit(); }

			}
			conn.commit();

		}  
		catch (SQLException e)  
		{  
			e.printStackTrace();
		}
		System.out.println(table+": insert "+i);
		return i;//����Ӱ���������1Ϊִ�гɹ�
	}
	
	public int update (String table ,String name,byte[]value) 
	{  
		Connection cnn=new DBConnection().getConn();
		int i=0;  
		String sql="update "+table+" set value=? where name=��";//ע��Ҫ��where����  



		try{  
			PreparedStatement preStmt =cnn.prepareStatement(sql);  
			preStmt.setBytes(1,value);  
			preStmt.setString(2,name);             
			i=preStmt.executeUpdate();  
		}  
		catch (SQLException e)  
		{  
			e.printStackTrace();  
		}  
		System.out.println(table+": update "+i);
		return i;//����Ӱ���������1Ϊִ�гɹ�  
	}  
	//����name����value
	public byte[] select (String table,String name) 
	{  
		Connection conn=new DBConnection().getConn();
		String sql = "select value from "+table+" where name='"+name+"'";  
		byte[] s=null;
		try  
		{  
			Statement stmt = conn.createStatement();  
			ResultSet rs = stmt.executeQuery(sql);  

			if(rs.next())  
			{  
				s=rs.getBytes(1);//����Ϊrs.getString(1)���������ݿ����е�ֵ����ȷ��������Ϊ��һ��  

			} 			
		}  
		catch (SQLException e)  
		{  
			e.printStackTrace();  
		}          
		return s;  
	}  
	//������ѯ
	public Map<String,byte[]> selectBatch(String table,String name){
		Connection conn=new DBConnection().getConn();
		String sql = "select name,value from  "+table+" where name like '%"+name+"%'";  
//		System.out.println(sql);
		int i=0;
		Map<String,byte[]>results=new TreeMap<>();;
		try  
		{  
			Statement stmt = conn.createStatement();  
			ResultSet rs = stmt.executeQuery(sql);  

			while(rs.next())  
			{  
				i=i+1;
				results.put(rs.getString(1), rs.getBytes(2));
			}  
			//���Խ����ҵ���ֵд���࣬Ȼ�󷵻���Ӧ�Ķ���  
			//System.out.println(table+": select "+i);
		}  
		catch (SQLException e)  
		{  
			e.printStackTrace();  
		}          
		return results;  
	}

	public Map<String,byte[]> selectBatch(String table){
		Connection conn=new DBConnection().getConn();
		String sql = "select name,value from  "+table;
		System.out.println(sql);
		int i=0;
		Map<String,byte[]>results=new TreeMap<>();;
		try
		{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while(rs.next())
			{
				i=i+1;
				results.put(rs.getString(1), rs.getBytes(2));
			}
			//���Խ����ҵ���ֵд���࣬Ȼ�󷵻���Ӧ�Ķ���
			//System.out.println(table+": select "+i);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return results;
	}

	public int delete(String table,String name)  
	{  
		Connection conn=new DBConnection().getConn();
		String sql = "delete from  "+table+" where name like '%"+name+"%'";  
		int i=0;            
		try  
		{  
			Statement stmt = conn.createStatement();  
			i = stmt.executeUpdate(sql);  
		}  
		catch (SQLException e)  
		{  
			e.printStackTrace();  
		}  
		System.out.println(table+": delete"+i);
		return i;//������ص���1����ִ�гɹ�;  
	}  
	//���һ��������
	public static void clear(String ...tables){
		//��ʾʵ�θ����ɱ�
		System.out.println("clear up .....");
		DBOperation dbo=new DBOperation();		
		for(String table:tables)			
			dbo.delete(table, "");
	}
	
	public static void  storeData(String table,Element v,String desc){	
		DBOperation dbo=new DBOperation();
		dbo.insert(table, desc, v.toBytes());
	}
		
	public static void  storeData(String table,Element[] us,String desc){
		DBOperation dbo=new DBOperation();
		Map<String,byte[]>data=new HashMap<>();
		for(int i=0;i<us.length;i++)
			data.put(desc+String.valueOf(i+1), us[i].toBytes());
		dbo.insertBatch(table,data);
	}
	public static void  storeData(String table,Element[][] sectors,String desc){	
		DBOperation dbo=new DBOperation();
		Map<String,byte[]>data=new HashMap<>();
		for(int i=0;i<sectors.length;i++)
			//name=blockID_SectorID
			for(int j=0;j<sectors[0].length;j++)
			data.put(desc+String.valueOf(i+1)+"_"+String.valueOf(j+1),sectors[i][j].toBytes());	
		dbo.insertBatch(table,data);
	}
}
