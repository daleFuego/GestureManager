package com.gesture.forms;

import java.awt.Color;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public class ShowImage extends JPanel{
	
	private JLabel lblImage;
	
	public ShowImage(String title){
		setLayout(null);
		setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), title, TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		setBounds(3, 10, 434, 277);
		
		lblImage = new JLabel("Press Start to load camera stream");
		lblImage.setBounds(10, 22, 414, 244);
		add(lblImage);
		
	}

	public void drawImage(BufferedImage bufferedImage) {
		
        lblImage.setIcon(new ImageIcon(bufferedImage));
        
	}
}
