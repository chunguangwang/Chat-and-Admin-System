package com.qq.client.view;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.qq.client.model.LeaveMessage;
import com.qq.client.tools.*;
import com.qq.common.Message;
import com.qq.common.MessageType;


public class qqFriendList extends JFrame implements ActionListener, MouseListener{
	//deal with first card
	JPanel jphy1, jphy2, jphy3;
	JButton jphy_jb1, jphy_jb2, jphy_jb3;
	JScrollPane jsp1;
	public JLabel[] jbls1;
	//deal with second card
	JPanel jpmsr1, jpmsr2, jpmsr3;
	JButton jpmsr_jb1, jpmsr_jb2, jpmsr_jb3;
	JScrollPane jsp2;
	JLabel[] jbls2;
	String ownerId;
	//set JFrame using CardLayout
	CardLayout cl;
	/*public static void main(String[] args){
		qqFriendList fl=new qqFriendList();
	}*/
	
	public qqFriendList(String ownerId){
		//deal with first card
		this.ownerId = ownerId;
		jphy_jb1=new JButton("My Friends");
		
		jphy_jb2=new JButton("Strangers");
		jphy_jb2.addActionListener(this);
		jphy_jb3=new JButton("Black List");
		jphy1 = new JPanel(new BorderLayout());
		//initialize 50 friends assumed
		jphy2 = new JPanel(new GridLayout(50,1,4,4));
		jphy2.setBorder(new EmptyBorder(10,10,10,10));
		jbls1=new JLabel[50];
		for(int i=0;i<jbls1.length;i++){
			try {
				jbls1[i]=new JLabel(i+1+"", new ImageIcon(ImageIO.read(ResourceLoader.load("meimei.gif"))), JLabel.LEFT);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if(jbls1[i].getText().equals(ownerId)){
				jbls1[i].setEnabled(true);
			}else{
				jbls1[i].setEnabled(false);
			}
			jbls1[i].addMouseListener(this);
			jphy2.add(jbls1[i]);
		}
		
		
		jphy3 = new JPanel(new GridLayout(2,1));	
		//add two buttons to jphy3
		jphy3.add(jphy_jb2);
		jphy3.add(jphy_jb3);
		
		
		jsp1=new JScrollPane(jphy2);
		
		//initialize jphy1
		jphy1.add(jphy_jb1, "North");
		jphy1.add(jsp1,"Center");
		jphy1.add(jphy3,"South");
		
		
		
		
		
		//deal with second card
		jpmsr_jb1=new JButton("My Friends");
		jpmsr_jb1.addActionListener(this);
		jpmsr_jb2=new JButton("Strangers");
		jpmsr_jb3=new JButton("Black List");
		jpmsr1 = new JPanel(new BorderLayout());
		//initialize 20 strangers assumed
		jpmsr2 = new JPanel(new GridLayout(20,1,4,4));
		jpmsr2.setBorder(new EmptyBorder(10,10,10,10));
		jbls2=new JLabel[20];
		for(int i=0;i<jbls2.length;i++){
			jbls2[i]=new JLabel(i+1+"", new ImageIcon("image/meimei.gif"), JLabel.LEFT);
			jbls2[i].addMouseListener(this);
			jpmsr2.add(jbls2[i]);
		}
		
		
		jpmsr3 = new JPanel(new GridLayout(2,1));	
		//add two buttons to jphy3
		jpmsr3.add(jpmsr_jb1);
		jpmsr3.add(jpmsr_jb2);
		
		
		jsp2=new JScrollPane(jpmsr2);
		
		//initialize jphy1
		jpmsr1.add(jpmsr3, "North");
		jpmsr1.add(jsp2,"Center");
		jpmsr1.add(jpmsr_jb3,"South");
		
		
		cl=new CardLayout();
		this.setLayout(cl);
		this.add(jphy1, "1");
		this.add(jpmsr1, "2");
		
		
		
		this.setVisible(true);
		this.setSize(300,550);
		this.setTitle("QQ2017-"+ownerId);
		this.setLocation(900,80);
		this.setResizable(false);
		this.setIconImage((new ImageIcon("image/head.GIF").getImage()));
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			   public void windowClosing(WindowEvent e) {
			    int a = JOptionPane.showConfirmDialog(null, "Exit", "Hint",
			      JOptionPane.YES_NO_OPTION);
			    if (a == JOptionPane.YES_OPTION) {  
			     try {
			    	Message m = new Message();
			    	m.setMesType(MessageType.message_log_out);
			    	m.setSender(ownerId);
			    	ManagerClientConServerThread.getClientConServerThread(ownerId).closeb=true;
			    	ObjectOutputStream oos=new ObjectOutputStream(ManagerClientConServerThread.getClientConServerThread(ownerId).getS().getOutputStream());
			    	oos.writeObject(m);
			    	System.out.println(ownerId + " logged out.");
					ManagerClientConServerThread.getClientConServerThread(ownerId).getS().close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}finally{
					 System.exit(0);  //close
				}
			     
				}
			    
			   }
			  });
	}

	



	//update online friends
	public void updateFriend(Message m){
		if(m.getMesType().equals(MessageType.message_ret_onlineFriend)){
			String onLineFriend[] = m.getCon().split(" ");
			for(int i=0; i<onLineFriend.length;i++){
				jbls1[Integer.parseInt(onLineFriend[i])-1].setEnabled(true);
			}
		}else if(m.getMesType().equals(MessageType.message_log_out)){
			jbls1[Integer.parseInt(m.getCon())-1].setEnabled(false);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getSource()==jphy_jb2){
			cl.show(this.getContentPane(), "2");
		}else if(arg0.getSource()==jpmsr_jb1){
			cl.show(this.getContentPane(), "1");
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		//respond to double click event, and get id of friend
		if(arg0.getClickCount()==2){
			//get the friend's id
			String friendNo = ((JLabel)arg0.getSource()).getText();
			System.out.println(ownerId + " are chatting with " + friendNo);
			qqChat qc=new qqChat(ownerId, friendNo);
			
			//add chat view to manager class
			ManagerQqChat.addqqChat(this.ownerId+" "+friendNo, qc);
			if(LeaveMessage.hm.containsKey(this.ownerId+" "+friendNo)){
				ArrayList<Message> temp = (ArrayList<Message>)LeaveMessage.hm.get(this.ownerId+" "+friendNo);
				for(int i=0;i<temp.size();i++){
					qc.showMessage((Message)temp.get(i));
				}
				LeaveMessage.hm.remove(this.ownerId+" "+friendNo);
				jbls1[Integer.parseInt(friendNo)-1].setForeground(Color.black);
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		JLabel j1=(JLabel)e.getSource();
		j1.setForeground(Color.red);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		JLabel j1=(JLabel)e.getSource();
		j1.setForeground(Color.black);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
