
/*
 * this is qq server, it's listening, waiting for qq client to connect to
 */
package com.qq.server.model;
import java.net.*;
import java.io.*;
import java.util.*;

import com.qq.common.Message;
import com.qq.common.MessageType;
import com.qq.common.User;
import com.qq.server.model.*;

public class qqServer implements Runnable{
	ServerSocket ss;
	Socket s;
	boolean closeb=false;
	public qqServer(){
		
	}
	
	//method close socket
	public void close(){
		
			try {
				this.closeb= true;
				HashMap hm = ManagerClientThread.hm;
				Message m = new Message();
				m.setMesType(MessageType.message_force_log_out);
				Iterator it = hm.entrySet().iterator();
				while(it.hasNext()){
					Map.Entry pair = (Map.Entry)it.next();
					m.setGetter(pair.getKey().toString());
					ObjectOutputStream oos = new ObjectOutputStream(((SerConClientThread)pair.getValue()).s.getOutputStream());
					oos.writeObject(m);
					((SerConClientThread)pair.getValue()).closeb = true;
					((SerConClientThread)pair.getValue()).s.close();
				}
				ss.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
			//listening at port 9999
			System.out.println("Listening at port 9999");
			this.ss=new ServerSocket(9999);
			
			//block
			while(!closeb){
		
				this.s=ss.accept();
				
				//accept message from client using object stream
				// assuming client sends an object of user class
				ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
				User u= (User)ois.readObject();
			    Message m = new Message();
			    ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
				if((u.getPass()).equals("123456")){
					//return success message
					m.setMesType("1");
					oos.writeObject(m);
					
					//open a new thread for connection between that client and server
					SerConClientThread scct=new SerConClientThread(s);
					ManagerClientThread.addClientThread(u.getName(), scct);
					//thread for communication between server and client
					scct.start();
					
					//notify all online users
					scct.notifyOther(u.getName());
				}else{
					m.setMesType("2");
					oos.writeObject(m);
					s.close();
				}
				
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
		}
	}
}

