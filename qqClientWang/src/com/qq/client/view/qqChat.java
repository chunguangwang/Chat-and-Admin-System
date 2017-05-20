/*
 * the view of chatting with friend;
 * always in reading state, should be a thread
 */
package com.qq.client.view;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

import com.qq.client.model.MyQqClientConServer;
import com.qq.client.tools.ManagerClientConServerThread;
import com.qq.common.Message;
import com.qq.common.MessageType;
import com.qq.client.tools.*;

import java.awt.*;
import java.awt.event.*;
import com.qq.client.model.*;
import java.io.*;

public class qqChat extends JFrame implements ActionListener {
	
	JTextPane jtp;
	JTextField jtf;
	JButton jb;
	JPanel jp;
	String ownerId;
	String friendId;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
     
	}
	
	public qqChat(String ownerId, String friend){
		this.ownerId = ownerId;
		this.friendId = friend;
		jtp = new JTextPane();
		jtf = new JTextField(15);
		jb= new JButton("Send");
		jb.addActionListener(this);
		jp = new JPanel();
		jp.add(jtf);
		jp.add(jb);
		
		this.add(jtp,"Center");
		this.add(jp, "South");
		this.setSize(400, 300);
		this.setVisible(true);
		this.setTitle("Chatting with " + friend);
		try {
			this.setIconImage(ImageIO.read(ResourceLoader.load("head.GIF")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			   public void windowClosing(WindowEvent e) {
			    int a = JOptionPane.showConfirmDialog(null, "Exit", "Hint",
			      JOptionPane.YES_NO_OPTION);
			    if (a == JOptionPane.YES_OPTION) {  
			     try {
			    	ManagerQqChat.getqqChat(ownerId+" "+friend).dispose();
			    	ManagerQqChat.hm.remove(ownerId+" "+friend);
			    	System.out.println("chat window closed");
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			     
				}
			    
			   }
			  });
	}
		
	
	//method displaying messages
	public void showMessage(Message m){
		String info =m.getSendTime()+'\n'+m.getCon();
	
	    StyledDocument document = jtp.getStyledDocument();

	    Style style = jtp.addStyle("I'm a Style", null);
	    StyleConstants.setAlignment(style, StyleConstants.ALIGN_LEFT);
	    StyleConstants.setForeground(style, Color.orange);
	    StyleConstants.setBold (style, true);
	    document.setParagraphAttributes(document.getLength(), info.length(), style, false);
		try {
			document.insertString(jtp.getDocument().getLength(),info+'\n',style);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		//if the user clicked the send button
		if(e.getSource()==jb){
			Message m=new Message();
			m.setSender(this.ownerId);
			m.setGetter(this.friendId);
			m.setCon(jtf.getText());
			m.setMesType(MessageType.message_comm_mes);
			m.setSendTime(new java.util.Date().toString());
			String info = m.getSendTime()+'\n'+m.getCon();
		    StyledDocument document1 = jtp.getStyledDocument();
		   
		    Style style1 = jtp.addStyle("I'm a Style", null);
		    StyleConstants.setAlignment(style1, StyleConstants.ALIGN_RIGHT);
		    StyleConstants.setForeground(style1, Color.green);
		    StyleConstants.setBold (style1, true);
		    document1.setParagraphAttributes(document1.getLength(), info.length(), style1, false);
			try {
				document1.insertString(document1.getLength(),info+'\n',style1);
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			this.jtf.setText("");
			//send to the server
			try {
				ObjectOutputStream oos = new ObjectOutputStream(ManagerClientConServerThread.getClientConServerThread(ownerId).getS().getOutputStream());
							oos.writeObject(m);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
	}

	

}
