package com.gesture.image;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

import com.gesture.forms.ShowImage;
import com.gesture.logic.HandCtrl;
import com.gesture.utils.Console;

public class CamStream {

	private ShowImage panelManageableImage;
	private ShowImage panelControlImage;
	private HandCtrl handShape;
	private Thread thread;

	public CamStream(ShowImage panelOrginalImage, ShowImage panelProcessedImage) {
		this.panelManageableImage = panelOrginalImage;
		this.panelControlImage = panelProcessedImage;

		handShape = new HandCtrl();

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
							panelControlImage.drawImage(handShape.controlImage(mat));
							panelManageableImage.drawImage(handShape.manageableImage(mat));
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
