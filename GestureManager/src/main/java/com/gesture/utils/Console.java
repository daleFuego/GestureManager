package com.gesture.utils;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class Console extends JTextArea {

	private static Console console;

	private Console() {
		Font f = getFont();
		Font f2 = new Font(f.getFontName(), f.getStyle(), f.getSize() + 2);
		setFont(f2);
		setBackground(Color.WHITE);
		setEnabled(false);
		setForeground(Color.BLACK);
		setColumns(1);
	}

	public static Console getInstance() {
		if (console == null) {
			console = new Console();
		}
		return console;
	}

	public void log(String message) {
		append("\n>>> " + message);
		setCaretPosition(getDocument().getLength());
	}

}
