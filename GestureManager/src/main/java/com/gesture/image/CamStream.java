package com.gesture.image;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import com.gesture.forms.ShowImage;
import com.gesture.logic.HandShape;
import com.gesture.utils.Console;

public class CamStream {

	private ShowImage panelOrginalImage;
	private ShowImage panelProcessedImage;
	private HandShape handShape;
	private Thread thread;

	public CamStream(ShowImage panelOrginalImage, ShowImage panelProcessedImage) {
		this.panelOrginalImage = panelOrginalImage;
		this.panelProcessedImage = panelProcessedImage;

		handShape = new HandShape();

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	@SuppressWarnings("deprecation")
	public void startVideo() {
		try {
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
							panelProcessedImage.drawImage(handShape.processImage(mat));
							panelOrginalImage.drawImage(handShape.controlImage(mat));
						}
					}
				}
			});

			thread.start();

		} catch (Exception e) {
			Console.getInstance().log(this.getClass().getSimpleName() + " err: " + e.getMessage());
			thread.stop();
			thread.start();
		}
	}

	@SuppressWarnings("deprecation")
	public void stopVideo() {
		Console.getInstance().log("Stopping video stream...");
		thread.stop();
	}
}
