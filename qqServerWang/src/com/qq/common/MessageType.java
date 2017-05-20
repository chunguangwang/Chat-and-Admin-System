/*
 * define message types
 */
package com.qq.common;

public interface MessageType {
	String message_success="1";//login successfully
	String message_login_fail="2";//failed to login
	String message_comm_mes="3";//common message
	String message_get_onLineFriend="4";//get online friends
	String message_ret_onlineFriend="5";//return online friends
	String message_log_out="6";//log out message
	String message_force_log_out="7";//force to log out message
}
