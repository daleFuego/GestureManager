package com.image.process;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import com.gesture.forms.ShowImage;
import com.gesture.utils.Console;

public class CamStream {

	private ShowImage panelOrginalImage;
	private ShowImage panelProcessedImage;
	private Thread thread;

	public CamStream(ShowImage panelOrginalImage, ShowImage panelProcessedImage) {

		this.panelOrginalImage = panelOrginalImage;
		this.panelProcessedImage = panelProcessedImage;
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

	}

	@SuppressWarnings("deprecation")
	public void startVideo() {

		try{
		thread = new Thread(new Runnable() {

			public void run() {

				Console.getInstance().log("Starting video stream...");

				VideoCapture videoCapture = new VideoCapture(0);
				Mat mat = new Mat();

				if (!videoCapture.isOpened()) {
					Console.getInstance().log("Error while connecting to webcam!");
					return;
				}

				while (true) {
					if (videoCapture.read(mat)) {
						panelOrginalImage.drawImage(processImage(mat));
						panelProcessedImage.drawImage(processImage(mat));
					}
				}

			}
		});

		thread.start();
		} catch(Exception e){
			Console.getInstance().log(e.getMessage());
			thread.stop();
			thread.start();
		}

	}

	protected BufferedImage processImage(Mat mat) {

		int type = 0;
		
		if (mat.channels() == 1) {
			type = BufferedImage.TYPE_BYTE_GRAY;
		} else if (mat.channels() == 3) {
			type = BufferedImage.TYPE_3BYTE_BGR;
		}
		
		BufferedImage bufferedImage = new BufferedImage(mat.width(), mat.height(), type);
		WritableRaster writableRaster = bufferedImage.getRaster();
		DataBufferByte dataBufferByte = (DataBufferByte) writableRaster.getDataBuffer();
		
		byte[] data = dataBufferByte.getData();
		mat.get(0, 0, data);

		return bufferedImage;

	}

	@SuppressWarnings("deprecation")
	public void stopVideo() {
		Console.getInstance().log("Stopping video stream...");
		thread.stop();
	}
}
