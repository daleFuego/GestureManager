package com.gesture.utils;

import java.awt.Color;

import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class Console extends JTextArea{

	private static Console console;
	
	private Console(){
		
		setBackground(Color.WHITE);
		setEnabled(false);
		setColumns(1);
		
	}
	
	public static Console getInstance(){
		
		if(console == null){
			console = new Console();
		}
		return console;
		
	}
	
	public void log(String message){
		
		append("\n>>> " + message);
		
	}
	
}
