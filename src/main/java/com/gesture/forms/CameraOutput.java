package com.gesture.forms;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.gesture.image.CamStream;
import com.gesture.utils.Console;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;

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
	public JLabel lblHandStatus;
	public JCheckBox chckbxMouseClick;
	public JCheckBox chckbxEnableMouseTracking;
	public JCheckBox chckbxChangeBackground;
	private JLabel lblStopMouseClick;
	private JLabel lblCoordinates;

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

		initialize();

		camStream = new CamStream(panelOrginalImage, panelProcessedImage, chckbxEnableMouseTracking, chckbxMouseClick,
				lblHandStatus, chckbxChangeBackground, lblCoordinates);

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

		JPanel panelCtrls = new JPanel();
		panelCtrls.setBorder(new TitledBorder(null, "Controls", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelCtrls.setBounds(680, 524, 664, 128);
		getContentPane().add(panelCtrls);
		panelCtrls.setLayout(null);

		JLabel lblStopEvents = new JLabel("To stop mouse tracking press T");
		lblStopEvents.setBounds(194, 28, 155, 14);
		panelCtrls.add(lblStopEvents);

		chckbxChangeBackground = new JCheckBox("White background");
		chckbxChangeBackground.setBounds(6, 98, 149, 23);
		panelCtrls.add(chckbxChangeBackground);

		JLabel lblChangeBackground = new JLabel("To change background press B");
		lblChangeBackground.setBounds(194, 102, 155, 14);
		panelCtrls.add(lblChangeBackground);

		JLabel lblHandDetectionStatus = new JLabel("Hand detection status:");
		lblHandDetectionStatus.setBounds(444, 28, 116, 14);
		panelCtrls.add(lblHandDetectionStatus);

		chckbxMouseClick = new JCheckBox("Enable mouse click");
		chckbxMouseClick.setBounds(6, 24, 149, 23);
		chckbxMouseClick.setEnabled(false);
		panelCtrls.add(chckbxMouseClick);

		chckbxEnableMouseTracking = new JCheckBox("Enable mouse tracking");
		chckbxEnableMouseTracking.setBounds(6, 61, 149, 23);
		chckbxEnableMouseTracking.setSelected(false);
		chckbxEnableMouseTracking.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				chckbxMouseClick.setEnabled(chckbxEnableMouseTracking.isSelected());				
			}
		});
		panelCtrls.add(chckbxEnableMouseTracking);

		lblHandStatus = new JLabel(" ");
		lblHandStatus.setHorizontalAlignment(SwingConstants.CENTER);
		lblHandStatus.setBounds(416, 57, 173, 31);
		panelCtrls.add(lblHandStatus);

		lblStopMouseClick = new JLabel("To stop mouse click press C");
		lblStopMouseClick.setBounds(194, 65, 155, 14);
		panelCtrls.add(lblStopMouseClick);
		
		lblCoordinates = new JLabel(" ");
		lblCoordinates.setHorizontalAlignment(SwingConstants.CENTER);
		lblCoordinates.setBounds(416, 102, 173, 14);
		panelCtrls.add(lblCoordinates);

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			Console.getInstance().log(this.getClass().getSimpleName() + " err: " + e.getMessage());
		}

		this.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == 'B' || e.getKeyChar() == 'b') {
					chckbxChangeBackground.setSelected(!chckbxChangeBackground.isSelected());
				}
				if (e.getKeyChar() == 'T' || e.getKeyChar() == 't') {
					chckbxEnableMouseTracking.setSelected(!chckbxEnableMouseTracking.isSelected());
				}
				if (e.getKeyChar() == 'C' || e.getKeyChar() == 'c' && chckbxEnableMouseTracking.isSelected()) {
					chckbxMouseClick.setSelected(!chckbxMouseClick.isSelected());
				}
				if (e.getKeyChar() == 'Q' || e.getKeyChar() == 'q' && chckbxEnableMouseTracking.isSelected()) {
					System.exit(0);
				}	
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		});
		
		java.awt.EventQueue.invokeLater(new Runnable() {
		    @Override
		    public void run() {
		        toFront();
		        repaint();
		    }
		});
	}

	public void doBtnStartClick() {
		btnStart.doClick();
	}
}
