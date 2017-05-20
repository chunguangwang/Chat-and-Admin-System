package com.qq.client.model;

import com.qq.common.User;

public class qqClientUser {
	public boolean checkUser(User u){
		
		return new MyQqClientConServer().sendLoginInfoToServer(u);
	}
}
