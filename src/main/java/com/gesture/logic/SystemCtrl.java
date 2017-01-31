package com.gesture.logic;

import java.awt.AWTException;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;

import com.gesture.forms.CtrlBoard;
import com.gesture.utils.Console;

public class SystemCtrl {

	private static final int MOUSE_SCALER = 10;
	private static final int WINDOW_SCALER = 4;
	private static int DEVICE_WIDTH;
	private static int DEVICE_HEIGHT;
	private CtrlBoard board;
	private Rectangle bounds;
	private Point movingMouseLocation;
	private GraphicsDevice[] devices;
	private GraphicsDevice device;

	public SystemCtrl() {

		GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		devices = graphicsEnvironment.getScreenDevices();

		if (devices.length > 1) {
			device = devices[1];
		} else {
			device = devices[0];
		}

		DEVICE_WIDTH = device.getDisplayMode().getWidth();
		DEVICE_HEIGHT = device.getDisplayMode().getHeight();

		if (board == null) {
			board = new CtrlBoard(DEVICE_WIDTH / WINDOW_SCALER, DEVICE_HEIGHT / WINDOW_SCALER);
			board.setVisible(true);
		}

		GraphicsConfiguration[] configuration = device.getConfigurations();
		bounds = configuration[0].getBounds();
		movingMouseLocation = MouseInfo.getPointerInfo().getLocation();
	}

	public void driveCoursor(double top_X, double top_Y) {
		try {
			Point moveOver = new Point((int) (top_X - 200) * MOUSE_SCALER, (int) (top_Y - 250) * MOUSE_SCALER);
			Robot robot = new Robot(device);

			if (!bounds.contains(moveOver)) {
				if (moveOver.x >= DEVICE_WIDTH - 10) {
					moveOver.x = DEVICE_WIDTH - 10;
				}
				if (moveOver.x <= DEVICE_WIDTH + 10) {
					moveOver.x = DEVICE_WIDTH + 10;
				}
				if (moveOver.y >= DEVICE_HEIGHT - 10) {
					moveOver.y = DEVICE_HEIGHT - 10;
				}
				if (moveOver.y <= DEVICE_HEIGHT + 10) {
					moveOver.y = DEVICE_HEIGHT + 10;
				}
			}

			board.drawCoursor(new Point(moveOver.x, moveOver.y), WINDOW_SCALER);
			movingMouseLocation.setLocation(moveOver.x, moveOver.y);
			robot.mouseMove((int) (movingMouseLocation.getX()), (int) movingMouseLocation.getY());
		} catch (Exception e) {
			Console.getInstance().log(this.getClass().getSimpleName() + " err: " + e.getMessage());
		}
	}

	public void doMouseClick() {
		try {
			Robot robot = new Robot();
			robot.mousePress(InputEvent.BUTTON1_MASK);
			robot.mouseRelease(InputEvent.BUTTON1_MASK);
		} catch (AWTException e) {
			Console.getInstance().log(this.getClass().getSimpleName() + " err: " + e.getMessage());
		}
	}

}
