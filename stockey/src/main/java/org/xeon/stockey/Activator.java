package org.xeon.stockey;

import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.xeon.stockey.data.impl.MySessionFactory;

import com.mysql.jdbc.Connection;

@SpringBootApplication
@Controller
@RequestMapping("/")
public class Activator {
	public static void main(String[] args){
		//MySessionFactory f = new MySessionFactory();
		SpringApplication.run(Activator.class);

		//getConn();
	}
	
	private static Connection getConn() {
	    String driver = "com.mysql.jdbc.Driver";
	    String url = "jdbc:mysql://localhost:3306/stockey";
	    String username = "root";
	    String password = "9990";
	    Connection conn = null;
	    try {
	        Class.forName(driver); //classLoader,加载对应驱动
	        conn = (Connection) DriverManager.getConnection(url, username, password);
	    } catch (ClassNotFoundException e) {
	        e.printStackTrace();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return conn;
	}
}
