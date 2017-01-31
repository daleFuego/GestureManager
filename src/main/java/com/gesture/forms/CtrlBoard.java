package com.gesture.forms;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;

import javax.swing.JFrame;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import com.gesture.utils.Console;

@SuppressWarnings("serial")
public class CtrlBoard extends JFrame {

	private BlackBoard panelBlackBoard;
	private int width, height;

	public CtrlBoard(int width, int height) {
		this.width = width;
		this.height = height;

		panelBlackBoard = new BlackBoard(width, height);

		getContentPane().add(panelBlackBoard, BorderLayout.CENTER);
		setBounds(700, 700, 570, 432);
		setResizable(false);
	}

	public boolean drawCoursor(Point point, int WINDOW_SCALER) {
		
		boolean result = false;

		try {
			Mat mat = new Mat(height, width, CvType.CV_8UC3);

			org.opencv.core.Point matPoint = new org.opencv.core.Point(point.getX() / WINDOW_SCALER,
					point.getY() / WINDOW_SCALER);
			
			if (matPoint.x < mat.width() && matPoint.y < mat.height()) {
				Imgproc.circle(mat, matPoint, 10, new Scalar(255, 255, 255), 2);

				BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

				WritableRaster writableRaster = bufferedImage.getRaster();
				DataBufferByte dataBufferByte = (DataBufferByte) writableRaster.getDataBuffer();

				byte[] data = dataBufferByte.getData();
				mat.get(0, 0, data);

				panelBlackBoard.drawImage(bufferedImage);
				
				result = true;
			}
			
		} catch (Exception e) {
			Console.getInstance().log(this.getClass().getSimpleName() + " err: " + e.getMessage());
		}
		
		return result;
	}
}
