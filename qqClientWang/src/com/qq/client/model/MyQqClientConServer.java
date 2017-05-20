package com.qq.client.model;

import java.util.*;

import com.qq.common.Message;
import com.qq.common.User;
import com.qq.client.tools.*;
import java.net.*;
import java.io.*;

public class MyQqClientConServer {
	
	public Socket s; 
	public static String ipaddress = null;
	//send the first request
	public boolean sendLoginInfoToServer(Object o){
		boolean b=false;
		try{
			s = new Socket(ipaddress ,9999); 
			ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
			oos.writeObject(o);
			
			ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
		
			Message ms = (Message)ois.readObject();
			//login successfully
			if(ms.getMesType().equals("1")){
				//build a thread for client and server
				ClientConServerThread ccst=new ClientConServerThread(s);
				//start the thread
				ccst.start();
				ManagerClientConServerThread.addClientConServerThread(((User)o).getName(), ccst);
				b=true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
		}
		return b;
	}
	
	
	public void sendInfoServer(Object o){
	}
}
