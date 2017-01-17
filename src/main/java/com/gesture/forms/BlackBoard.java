package com.gesture.forms;

import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class BlackBoard extends JPanel {

	private JLabel lblImage;

	public BlackBoard(int width, int height) {
		setLayout(null);
		setBounds(3, 10, width + 5, height + 5);
		setVisible(true);

		lblImage = new JLabel();
		lblImage.setBounds(10, 22, width, height);
		add(lblImage);
	}

	public void drawImage(BufferedImage bufferedImage) {
		lblImage.setIcon(new ImageIcon(bufferedImage));
	}
	
}
