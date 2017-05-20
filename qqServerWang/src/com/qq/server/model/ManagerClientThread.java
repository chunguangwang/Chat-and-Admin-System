package com.qq.server.model;
import java.util.*;
public class ManagerClientThread {
	public static HashMap hm=new HashMap<String, SerConClientThread>();
	
	//add client communication thread to hm
	public static void addClientThread(String uid, SerConClientThread ct){
		hm.put(uid, ct);
	}
	
	public static SerConClientThread getClientThread(String uid){
		return (SerConClientThread)hm.get(uid);
	}
	
	//return current online users
	public static String getAllOnLineUserid(){
		//use iterator
		Iterator it=hm.keySet().iterator();
		String res="";
		while(it.hasNext()){
			res+=it.next().toString()+" ";
		}
		return res;
	}
}
