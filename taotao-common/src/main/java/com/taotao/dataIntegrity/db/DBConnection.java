package com.taotao.dataIntegrity.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection  
    {  
        private String dbDriver="com.mysql.jdbc.Driver";   
        private String dbUrl="jdbc:mysql://localhost:3306/taotao";//����ʵ������仯
        private String dbUser="root";  
        private String dbPass="qq062525";
        public Connection getConn()  
        {  
            Connection conn=null;  
            try  
            {  
                Class.forName(dbDriver);  
            }  
            catch (ClassNotFoundException e)  
            {  
                e.printStackTrace();  
            }  
            try  
            {  
                conn = DriverManager.getConnection(dbUrl,dbUser,dbPass);//ע������������  
            }  
            catch (SQLException e)  
            {  
                e.printStackTrace();  
            } 
            if(conn==null)
            	System.out.println("connect database failure!");
            return conn;  
        }  
    }  