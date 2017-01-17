package com.gesture.forms;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import com.gesture.image.CamStream;
import com.gesture.utils.Console;

@SuppressWarnings("serial")
public class CameraOutput extends JFrame {

	private JScrollPane scrollPaneConsole;
	private JPanel panelConsole;
	private JPanel panelBtns;
	private JButton btnStart;
	private JButton btnStop;
	private JButton btnExit;
	private CamStream camStream;
	private ShowImage panelOrginalImage;
	private ShowImage panelProcessedImage;

	public CameraOutput() {
		
		// BUTTONS
		btnStart = new JButton("Start");
		btnStart.setBounds(0, 6, 89, 23);
		btnStop = new JButton("Stop");
		btnStop.setBounds(0, 35, 89, 23);
		btnExit = new JButton("Exit");
		btnExit.setBounds(0, 64, 89, 23);

		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				camStream.stopVideo();
			}
		});

		// ACTIONS
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				camStream.startVideo();
			}
		});

		// SCROLLPANE
		scrollPaneConsole = new JScrollPane();
		scrollPaneConsole.setBounds(10, 21, 545, 96);
		scrollPaneConsole.setViewportView(Console.getInstance());

		// PANEL
		panelBtns = new JPanel();
		panelBtns.setLayout(null);
		panelBtns.setBounds(565, 21, 89, 96);
		panelBtns.add(btnStart);
		panelBtns.add(btnStop);
		panelBtns.add(btnExit);
		panelOrginalImage = new ShowImage("Result Image");
		panelOrginalImage.setSize(660, 520);
		panelOrginalImage.setLocation(10, 2);
		panelProcessedImage = new ShowImage("Processed Image");
		panelProcessedImage.setBounds(680, 2, 660, 520);
		
		panelConsole = new JPanel();
		panelConsole.setBounds(6, 524, 664, 128);
		panelConsole.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Console", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panelConsole.setLayout(null);
		panelConsole.add(panelBtns);
		panelConsole.add(scrollPaneConsole);

		camStream = new CamStream(panelOrginalImage, panelProcessedImage);
		
		initialize();
	}

	private void initialize() {

		// FORM
		setVisible(true);
		setTitle("CAMERA OUTPUT");
		setBounds(100, 100, 1366, 693);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		getContentPane().add(panelOrginalImage);
		getContentPane().add(panelProcessedImage);
		getContentPane().add(panelConsole);
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			Console.getInstance().log(this.getClass().getSimpleName() + " err: " + e.getMessage());
		}
	}

	public void doBtnStartClick() {
		btnStart.doClick();
	}
}
