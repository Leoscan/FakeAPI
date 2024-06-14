package main;

import java.io.IOException;

import classes.BDManager;
import server.ServerHandler;

public class Main {
	public static void main(String[] args) throws IOException {
	    String url = "jdbc:mysql://localhost:3306/MySQLFakeAPI";
	    String user = "root";
	    String password = "";
		BDManager bdConn = new BDManager(url, user, password);

		ServerHandler s1 = new ServerHandler(bdConn);
		s1.CreateServer(7000);
	} 
}
