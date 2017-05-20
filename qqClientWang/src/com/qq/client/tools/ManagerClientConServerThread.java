/*
 * This is a manager for thread between client and server
 */
package com.qq.client.tools;

import java.util.*;
public class ManagerClientConServerThread {

	private static HashMap<String, ClientConServerThread> hm=new HashMap<String, ClientConServerThread>();
	
	//add the established ClientConServerThread to hm
	public static void addClientConServerThread(String qqId, ClientConServerThread ccst){
		hm.put(qqId,  ccst);
	}
	
	//obtain qqId thread
	public static ClientConServerThread getClientConServerThread(String qqId){
		return (ClientConServerThread)hm.get(qqId);
	}
}
