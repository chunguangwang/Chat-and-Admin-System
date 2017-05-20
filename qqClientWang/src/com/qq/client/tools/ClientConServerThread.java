/*
 * thread to keep connection between client and server
 */


package com.qq.client.tools;

import java.awt.Color;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.qq.client.model.LeaveMessage;
import com.qq.client.view.ResourceLoader;
import com.qq.client.view.qqChat;
import com.qq.client.view.qqFriendList;
import com.qq.common.Message;
import com.qq.common.MessageType;

public class ClientConServerThread extends Thread{
	
	private Socket s;
	
	public Socket getS() {
		return s;
	}
    public boolean closeb = false;
	public void setS(Socket s) {
		this.s = s;
	}

	//constructor
	public ClientConServerThread(Socket s){
		this.s = s;
	}
	
	public void run(){
		while(!closeb){
			//reading message from server
			
			try {
				//System.out.println(closeb);
				ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
				try {
					Message m = (Message)ois.readObject();
					//System.out.println("Received message from"+m.getSender()+" to "+m.getGetter()+" : "+m.getCon());
					//show message received from server on corresponding view
					if(m.getMesType().equals(MessageType.message_comm_mes)){
						qqChat qc=ManagerQqChat.getqqChat(m.getGetter()+" "+m.getSender());
						if(qc != null){
							//display message
							qc.showMessage(m);
						}else{
							
							if(!LeaveMessage.hm.containsKey(m.getGetter()+" "+m.getSender())){
								ArrayList<Message> temp = new ArrayList<Message>();
								temp.add(m);
								LeaveMessage.hm.put(m.getGetter()+" "+m.getSender(), temp);
								System.out.println("not available");
							}else{
								
							     ArrayList<Message> temp = (ArrayList<Message>)LeaveMessage.hm.get(m.getGetter()+" "+m.getSender());
							     temp.add(m);
							     
							     LeaveMessage.hm.put(m.getGetter()+" "+m.getSender(), temp);
							}
							ManageQqFriendList.getQqFriendList(m.getGetter()).jbls1[Integer.parseInt(m.getSender())-1].setForeground(Color.RED);
							
						}
					
					}else if(m.getMesType().equals(MessageType.message_ret_onlineFriend)){
						String getter=m.getGetter();
						//modify relevant friend list
						qqFriendList qqFriendList = ManageQqFriendList.getQqFriendList(getter);
						//update online friends
						if(qqFriendList !=null){
							qqFriendList.updateFriend(m);
						}
					}else if(m.getMesType().equals(MessageType.message_log_out)){
						String getter=m.getGetter();
						//modify relevant friend list
						qqFriendList qqFriendList = ManageQqFriendList.getQqFriendList(getter);
						//update online friends
						if(qqFriendList !=null){
							qqFriendList.updateFriend(m);
						}
					}else if(m.getMesType().equals(MessageType.message_force_log_out)){
						 this.closeb = true;
						 System.out.println("Forced to be closed");
						 this.s.close();
						 qqFriendList qqFriendList = ManageQqFriendList.getQqFriendList(m.getGetter());
						 qqFriendList.dispose();
					}
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
