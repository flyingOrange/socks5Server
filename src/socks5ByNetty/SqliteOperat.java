package socks5ByNetty;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
public class SqliteOperat {

	//根据用户名查找
	public static boolean check(String name,String pwd) {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs =null;
	    try {
	      Class.forName("org.sqlite.JDBC");
	      conn = DriverManager.getConnection("jdbc:sqlite:list.db");
	      stmt = conn.prepareStatement("SELECT * FROM user where userName=? and password=?");
	      stmt.setString(1, name);
	      stmt.setString(2, pwd);
	      
	      rs = stmt.executeQuery();
	      if ( rs.next() ) 
	      {
	         String userName = rs.getString("userName");
	         String password = rs.getString("password");
	         String deadline = rs.getString("deadline");
	         System.out.println( "userName=" + userName +",   password="+password +"   ,deadline="+deadline); 
	         //检查是否在有效期
	         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
	 	     Date date1 = sdf.parse(deadline);//有效期
	 	     Date nowDate = new Date();//当前时间
	         if(nowDate.before(date1))
	         {
	        	 System.out.println("deadline is valid!");
	        	 return true;
	         }else 
	        	 return false;
	        
	      }else
	      {
	    	  System.out.println("userName or password wrong!");
	    	  return false;
	      }
	    } catch ( Exception e ) {
	      //System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	      System.out.println("check error!");
	      return false;
	    }finally
	    {
	    	try {
				rs.close();
				stmt.close();
			    conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		    
	    }
	    
	    
	}

}
