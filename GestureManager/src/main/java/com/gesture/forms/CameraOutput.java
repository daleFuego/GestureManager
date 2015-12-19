package com.gesture.forms;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import com.gesture.utils.Console;
import com.image.process.CamStream;

@SuppressWarnings("serial")
public class CameraOutput extends JFrame{

	private JScrollPane scrollPaneConsole;
	private ShowImage panelOrginalImage;
	private ShowImage panelProcessedImage;
	private JPanel panelConsole;
	private JButton btnStart;
	private JButton btnStop;
	private JButton btnExit;
	
	private CamStream camStream;

	public CameraOutput() {
		
		panelOrginalImage = new ShowImage("Orginal Image");
		panelOrginalImage.setLocation(7, 11);
		initialize();
		panelProcessedImage = new ShowImage("Processed Image");
		panelProcessedImage.setBounds(448, 11, 434, 277);
		getContentPane().add(panelProcessedImage);
		camStream = new CamStream(panelOrginalImage, panelProcessedImage);	
	}

	private void initialize() {

		//BUTTONS
		btnStart = new JButton("Start");
		btnStart.setBounds(451, 318, 89, 23);
		btnStop = new JButton("Stop");
		btnStop.setBounds(451, 341, 89, 23);
		btnExit = new JButton("Exit");
		btnExit.setBounds(451, 364, 89, 23);
		
		//ACTIONS
		btnStart.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				camStream.startVideo();
			}
		});
		
		btnStop.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				camStream.stopVideo();
			}
		});
		
		btnExit.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		//SCROLLPANE
		scrollPaneConsole = new JScrollPane();
		scrollPaneConsole.setBounds(10, 21, 414, 96);
		scrollPaneConsole.setViewportView(Console.getInstance());
		
		//PANEL
		panelConsole = new JPanel();
		panelConsole.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Console", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelConsole.setBounds(7, 297, 434, 128);
		panelConsole.setLayout(null);
		panelConsole.add(scrollPaneConsole);
	
		//FORM
		setVisible(true);
		setTitle("CAMERA OUTPUT");
		setBounds(100, 100, 906, 475);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().add(panelConsole);
		getContentPane().add(panelOrginalImage);
		getContentPane().add(btnStart);
		getContentPane().add(btnStop);
		getContentPane().add(btnExit);
		
	}
}
