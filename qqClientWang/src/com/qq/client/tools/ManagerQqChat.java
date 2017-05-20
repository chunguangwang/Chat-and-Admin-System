/*
 * class managing chat views
 */
package com.qq.client.tools;
import java.util.*;
import com.qq.client.view.*;
public class ManagerQqChat {
	public static HashMap hm=new HashMap<String, qqChat>();
	
	//add
	public static void addqqChat(String loginIdAndFriendId, qqChat qc){
		hm.put(loginIdAndFriendId, qc);
	}
	//get
	public static qqChat getqqChat(String loginIdAndFriendId){
		return (qqChat)hm.get(loginIdAndFriendId);
	}
}
