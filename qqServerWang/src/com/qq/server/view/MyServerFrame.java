/**
 * server view, start and stop
 * show online users, manage and monitor users 
 */
package com.qq.server.view;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;


import com.qq.common.Message;
import com.qq.common.MessageType;
import com.qq.common.ResourceLoader;
import com.qq.server.model.ManagerClientThread;
import com.qq.server.model.SerConClientThread;
import com.qq.server.model.qqServer;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class MyServerFrame extends JFrame implements ActionListener, MouseListener{
	//define northern components
	JLabel jl1;
	
	//define southern components
	JPanel jp1;
	JButton jb1, jb2;
	qqServer qs;
	
	//define center components
	//online users
	JPanel jp2;
	JScrollPane jsp2;
	public static JLabel[] jbls2;
	
	
	
	public static void main(String[] args){
		MyServerFrame sf=new MyServerFrame();
	}
	
	public MyServerFrame(){
		jp2 = new JPanel(new GridLayout(20,1,4,4));
		jp2.setBorder(new EmptyBorder(10,10,10,10));
		jsp2 = new JScrollPane(jp2);
		jbls2=new JLabel[20];
		for(int i=0;i<jbls2.length;i++){
			try {
				jbls2[i]=new JLabel(i+1+"", new ImageIcon(ImageIO.read(ResourceLoader.load("/com/qq/server/view/meimei.gif"))), JLabel.LEFT);
				jbls2[i].setToolTipText("Double click to kick the user off line.");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			jbls2[i].setEnabled(false);
			jbls2[i].addMouseListener(this);
			jp2.add(jbls2[i]);
		}
		
		
		
		
		jp1 = new JPanel();
		jb1 = new JButton("Start Server");
		jb1.addActionListener(this);
		jb2 = new JButton("Close Server");
		jb2.addActionListener(this);
		jb2.setEnabled(false);
		jp1.add(jb1);
		jp1.add(jb2);
		
		
		
		try {
			InputStream input = ResourceLoader.load("/com/qq/server/view/qqbanner.jpg");
			ImageIcon icon= new ImageIcon(ImageIO.read(input));
			System.out.println(icon);
			jl1=new JLabel(icon);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		
		this.add(jl1, BorderLayout.NORTH);
		this.add(jsp2,BorderLayout.CENTER);
		this.add(jp1, BorderLayout.SOUTH);
		this.setVisible(true);
		this.setSize(305, 400);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Server Panel");
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getSource()==jb1){
			qs=new qqServer();
			Thread t= new Thread(qs);
			t.start();
			jb1.setEnabled(false);
			jb2.setEnabled(true);
		}else if(arg0.getSource()==jb2){
			
			System.out.println("close socket");
			
				qs.close();
				jb2.setEnabled(false);
				jb1.setEnabled(true);
			
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		//respond to double click event, and kick the user off line.
		if(arg0.getClickCount()==2){
			
				//get the user's id
				String userNo = ((JLabel)arg0.getSource()).getText();
				
				//make the user icon on server panel grey
				MyServerFrame.jbls2[Integer.parseInt(userNo)-1].setEnabled(false);
				Message m = new Message();
				m.setMesType(MessageType.message_force_log_out);
				m.setGetter(userNo);
				
				ObjectOutputStream oos;
				try {
					oos = new ObjectOutputStream((ManagerClientThread.getClientThread(userNo)).s.getOutputStream());
					oos.writeObject(m);
					(ManagerClientThread.getClientThread(userNo)).closeb = true;
					(ManagerClientThread.getClientThread(userNo)).s.close();
					ManagerClientThread.hm.remove(userNo);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
		
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
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
