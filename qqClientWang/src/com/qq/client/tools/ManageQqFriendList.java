/*
 * manage friend, blacklist views
 */
package com.qq.client.tools;
import java.util.*;

import com.qq.client.view.qqFriendList;

import java.io.*;
public class ManageQqFriendList {
	
	public static HashMap<String, qqFriendList> hm = new HashMap<String, qqFriendList>();
	
	public static void addQqFriendList(String qqid, qqFriendList qqFriendList){
		hm.put(qqid, qqFriendList);
	}
	
	public static qqFriendList getQqFriendList(String qqId){
		return (qqFriendList)hm.get(qqId);
	}
}
