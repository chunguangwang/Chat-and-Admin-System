package com.qq.client.view;

import com.qq.common.*;
import com.qq.client.model.*;
import com.qq.client.tools.*;

import java.io.*;
import java.net.*;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.*;


import java.awt.*;
import java.awt.event.*;

public class qqClientLogin extends JFrame implements ActionListener{
	
	
	//define northern components
	JLabel jbl1;
	
	JPanel jp2;
	JLabel jp2_jbl1,jp2_jbl2;
	JTextField jp2_jtf;
	JPasswordField jp2_jpf;
	JCheckBox jp2_jcb1,jp2_jcb2;


	//define southern components
	JPanel jp1;
	JButton jp1_jb1,jp1_jb2;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		qqClientLogin  qqClientLogin=new qqClientLogin();
	    
	}
	
	public qqClientLogin()
	{   
		InputStream inputStream =null;
		String ipad = null;
		
		 try {
             
	            Properties props = new Properties();
	            String propertiesFileName = "C:/ipaddress.properties";
	  
	            inputStream =new FileInputStream(propertiesFileName);
	  
	            if (inputStream != null) {
	                props.load(inputStream);
	            } else {
	                throw new FileNotFoundException("property file '" + propertiesFileName + "' not found");
	            }
	            ipad = props.getProperty("ipAddress");
	        } catch (Exception e) {
	            System.out.println("Exception: " + e);
	        } finally {
	            try {
					inputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
		 
		 if(ipad==null){
			 ipad = "127.0.0.1";
		 }
		 MyQqClientConServer.ipaddress = ipad;
		 System.out.println(ipad);
		//deal with northern part
		try {
			InputStream input = ResourceLoader.load("qqbanner.jpg");
			ImageIcon icon= new ImageIcon(ImageIO.read(input));
			System.out.println(icon);
			jbl1=new JLabel(icon);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		
		
		
		//deal with center parts, 3 JPanels		
		jp2=new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		jp2_jbl1=new JLabel("ID",JLabel.CENTER);
		gbc.gridx = 0;
		gbc.gridy = 0;
		jp2.add(jp2_jbl1, gbc);
		
		jp2_jbl2=new JLabel("PASSWORD",JLabel.CENTER);
		gbc.gridx = 0;
		gbc.gridy = 1;
		jp2.add(jp2_jbl2, gbc);
		
		jp2_jtf=new JTextField(15);
		
		gbc.gridx = 10;
		gbc.gridy = 0;
		jp2.add(jp2_jtf, gbc);
		
		jp2_jpf=new JPasswordField(15);
		
		
		gbc.gridx = 10;
		gbc.gridy = 1;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		jp2.add(jp2_jpf, gbc);
		
		
		jp2_jcb1=new JCheckBox("AUTO LOGIN");
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 8;
		
		jp2.add(jp2_jcb1, gbc);
		
		jp2_jcb2=new JCheckBox("KEEP PASS");
		gbc.gridx = 10;
		gbc.gridy = 2;
		gbc.gridwidth = 8;
		jp2.add(jp2_jcb2, gbc);

       
		//southern part
		jp1=new JPanel(new FlowLayout());
		jp1_jb1=new JButton();
	
		jp1_jb1.setText("Login");
		
		//add action listener
		jp1_jb1.addActionListener(this);
		jp1_jb2=new JButton();
		jp1_jb2.setText("Quit");
		jp1_jb2.addActionListener(this);


		//add buttons to jpanel
		jp1.add(jp1_jb1);
		jp1.add(jp1_jb2);


		this.add(jbl1,"North");
		this.add(jp2,"Center");
		//add jp1 to south
		this.add(jp1,"South");
		this.setSize(325,310);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();  
		Dimension frameSize = this.getPreferredSize();
		this.setLocation((screenSize.width - frameSize.width)/2, (screenSize.height - frameSize.height)/2);//±£³Ö´°¿Úµ¯³öÎ»ÖÃ¾ÓÖÐ
		this.setIconImage((new ImageIcon("head.GIF").getImage()));
		this.setTitle("QQ Chat");
		this.setVisible(true);
	}
	@Override
public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==jp1_jb1){
			qqClientUser qqclientuser = new qqClientUser();
			User u=new User();
			u.setName(jp2_jtf.getText().trim());
			u.setPass(new String(jp2_jpf.getPassword()));
			//System.out.println(u.getPass());
			boolean correct = qqclientuser.checkUser(u);
		
		
			if(correct){
				//create friendlist view
				qqFriendList qf = new qqFriendList(u.getName());
				ManageQqFriendList.addQqFriendList(u.getName(), qf);
				//send a get_online_friend message
				
				try {
					ObjectOutputStream oos = new ObjectOutputStream(ManagerClientConServerThread.getClientConServerThread(u.getName()).getS().getOutputStream());
					
					// createa a message
					Message m =new Message();
					m.setMesType(MessageType.message_get_onLineFriend);
					m.setSender(u.getName());
					oos.writeObject(m);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
				//close current window
				this.dispose();
				
			}else{
				JOptionPane.showMessageDialog(this, "Creditials wrong");
			}
		}else if(e.getSource()==jp1_jb2){
			this.dispose();
		}
	}

}
