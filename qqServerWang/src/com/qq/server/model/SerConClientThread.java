/*
 * function: between server and client communication thread
 */

package com.qq.server.model;

import java.net.*;
import java.util.HashMap;
import java.util.Iterator;

import com.qq.common.*;
import com.qq.server.view.MyServerFrame;

import java.io.*;


import java.io.*;
public class SerConClientThread extends Thread{
	public Socket s;
	public boolean closeb;
	public SerConClientThread(Socket s){
		// assign the connection to s
		this.s = s;
	}
	
	//let the thread notify other users
	public void notifyOther(String iam){
		//notify the administrator the user is online
		MyServerFrame.jbls2[Integer.parseInt(iam)-1].setEnabled(true);
		
		//obtain all the online users
		HashMap hm=ManagerClientThread.hm;
		Iterator it = hm.keySet().iterator();
		while(it.hasNext()){
			Message m=new Message();
			m.setCon(iam);
			m.setMesType(MessageType.message_ret_onlineFriend);
			//obtain the online users
			String onLineUserId=it.next().toString();
			try {
				ObjectOutputStream oos = new ObjectOutputStream(ManagerClientThread.getClientThread(onLineUserId).s.getOutputStream());
				m.setGetter(onLineUserId);
				oos.writeObject(m);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void notifyOtherOut(String logoutid){
		//notify the administrator the user is offline
		MyServerFrame.jbls2[Integer.parseInt(logoutid)-1].setEnabled(false);		
		
		//notify other online users that a user has logged out
		HashMap hm = ManagerClientThread.hm;
		SerConClientThread scc = (SerConClientThread) hm.get(logoutid);
		try {
			scc.s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		hm.remove(logoutid);
		Iterator it = hm.keySet().iterator();
		while(it.hasNext()){
			Message m=new Message();
			m.setCon(logoutid);
			m.setMesType(MessageType.message_log_out);
			//obtain the online users
			String onLineUserId=it.next().toString();
			try {
				ObjectOutputStream oos = new ObjectOutputStream(ManagerClientThread.getClientThread(onLineUserId).s.getOutputStream());
				m.setGetter(onLineUserId);
				oos.writeObject(m);
		
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void run(){
		while(!closeb){
			// the thread will receive message from client
			try{
				ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
				Message m = (Message)ois.readObject();
				
				
				//tell the type of the message
				if(m.getMesType().equals(MessageType.message_comm_mes)){
					//finish transferring message
					//get receiver's communication thread				
					SerConClientThread sc = ManagerClientThread.getClientThread(m.getGetter());
					ObjectOutputStream oos=new ObjectOutputStream(sc.s.getOutputStream());
					oos.writeObject(m);
				}else if(m.getMesType().equals(MessageType.message_get_onLineFriend)){
					String res= ManagerClientThread.getAllOnLineUserid();
					Message m2 = new Message();
					m2.setMesType(MessageType.message_ret_onlineFriend);
					m2.setCon(res);
					m2.setGetter(m.getSender());
					
					ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
					oos.writeObject(m2);
				}else if(m.getMesType().equals(MessageType.message_log_out)){
					closeb = true;
					System.out.println(m.getSender()+" logged out.");
					notifyOtherOut(m.getSender());
				}
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
