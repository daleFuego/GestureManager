package com.gesture.logic;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfInt4;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

import com.gesture.utils.Console;

public class HandCtrl {

	private SystemCtrl systemCtrl;
	private HashMap<Point, Scalar> circlesToDraw;
	private Map<Double, Point> fingertips;
	private Point midPalmCircle;
	private Rect boundingRect;
	private Scalar TOP_FINGER;
	private Scalar SIDE_FINGER;
	private boolean isDetected, isPressed;
	private double palmSize;
	private int biggestID;

	public HandCtrl() {
		circlesToDraw = new HashMap<Point, Scalar>();
		fingertips = new TreeMap<Double, Point>();
		systemCtrl = new SystemCtrl();

		TOP_FINGER = new Scalar(234, 217, 153);
		SIDE_FINGER = new Scalar(39, 127, 255);

		isDetected = false;
		isPressed = false;
	}

	public BufferedImage manageableImage(Mat mat) {
		for (Point point : circlesToDraw.keySet()) {
			Imgproc.circle(mat, point, 15, circlesToDraw.get(point));
		}

		try {
			if (isDetected) {
				Point clickCtrl = null;
				double side_Y = 1000;
				double side_X = 0;
				double curY = 0;

				for (Double val : fingertips.keySet()) {
					curY = fingertips.get(val).y;

					if (fingertips.get(val).x >= side_X) {
						side_Y = fingertips.get(val).y;
						side_X = fingertips.get(val).x;
					}
				}

				if (curY - side_Y == 0) {
					clickCtrl = new Point(side_X, side_Y);
					Imgproc.circle(mat, new Point(side_X, side_Y), 15, SIDE_FINGER, 2);
				}

				if (midPalmCircle != null) {
					Imgproc.circle(mat, midPalmCircle, (int) palmSize / 4, TOP_FINGER, 3);
				}

				if (clickCtrl != null) {
					systemCtrl.driveCoursor(midPalmCircle.x, midPalmCircle.y);

					if (fingertips.size() < 4 && new Random().nextInt(10) > 5 && boundingRect.area() > 40000) {
						 systemCtrl.doMouseClick();
					}

					if (!isPressed && fingertips.size() == 4 && new Random().nextInt(10) > 7
							&& boundingRect.area() > 40000) {
						Thread thread = new Thread(new Runnable() {
							public void run() {
								 systemCtrl.doMousePress();

								try {
									Thread.sleep(200);
								} catch (InterruptedException e) {
									e.printStackTrace();
									 systemCtrl.doMouseRelease();
								}

								 systemCtrl.doMouseRelease();
							}
						});

						thread.start();
					}
				}
			}
		} catch (Exception e) {
			Console.getInstance().log(this.getClass().getSimpleName() + " err: " + e.getMessage());
		}

		circlesToDraw.clear();

		return convertToBGRBufferedImage(mat);
	}

	public BufferedImage controlImage(Mat mat2) {
		Mat mat = mat2.clone();
		Mat hierarchy = new Mat(mat.rows(), mat.cols(), mat.type());
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();

		try {
			Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGB2GRAY);
			Imgproc.GaussianBlur(mat, mat, new Size(11, 11), 0);
			Imgproc.threshold(mat, mat, 127, 255, Imgproc.THRESH_OTSU);
			Imgproc.findContours(mat, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE);

			int idx = -1;
			int cNum = 0;

			for (int i = 0; i < contours.size(); i++) {
				int curNum = contours.get(i).toList().size();

				if (curNum > cNum) {
					idx = i;
					cNum = curNum;
				}
			}

			biggestID = idx;

			if (biggestID > -1) {
				MatOfInt convexHull = new MatOfInt();
				MatOfInt4 convexityDefects = new MatOfInt4();
				MatOfPoint2f approxPolyDP = new MatOfPoint2f();
				List<Point> contourPoints = new ArrayList<Point>();
				List<MatOfPoint> convexHullPoints = new ArrayList<MatOfPoint>();

				approxPolyDP.fromList(contours.get(biggestID).toList());
				Imgproc.approxPolyDP(approxPolyDP, approxPolyDP, 2, true);
				contours.get(biggestID).fromList(approxPolyDP.toList());
				Imgproc.drawContours(mat, contours, biggestID, new Scalar(255, 255, 255), 1);
				boundingRect = Imgproc.boundingRect(contours.get(biggestID));
				Imgproc.convexHull(contours.get(biggestID), convexHull, false);

				for (int i = 0; i < contours.size(); i++) {
					convexHullPoints.add(new MatOfPoint());
				}

				int[] cId = convexHull.toArray();
				Point[] contourPts = contours.get(biggestID).toArray();

				for (int i = 0; i < cId.length; i++) {
					contourPoints.add(contourPts[cId[i]]);
				}

				convexHullPoints.get(biggestID).fromList(contourPoints);
				contourPoints.clear();
				fingertips.clear();

				if ((contourPts.length >= 5) && isHandDetected(mat) && (cId.length >= 5)) {
					Imgproc.convexityDefects(contours.get(biggestID), convexHull, convexityDefects);
					List<Integer> defects = convexityDefects.toList();

					for (int i = 0; i < defects.size(); i++) {
						int id = i % 4;
						Point contourPoint;

						if (id == 2) {
							double diffDepth = (double) defects.get(i + 1) / 256.0;
							contourPoint = contourPts[defects.get(i)];

							Point tl = boundingRect.tl();
							Point br = boundingRect.br();
							Point contourSpat0 = contourPts[defects.get(i - 2)];
							Point contourSpat1 = contourPts[defects.get(i - 1)];

							Point spat0 = new Point(contourSpat0.x - contourPoint.x, contourSpat0.y - contourPoint.y);
							Point spat1 = new Point(contourSpat1.x - contourPoint.x, contourSpat1.y - contourPoint.y);
							double mid = spat0.x * spat1.x + spat0.y * spat1.y;
							double spat0Lenght = Math.sqrt(spat0.x * spat0.x + spat0.y * spat0.y);
							double spat1Lenght = Math.sqrt(spat1.x * spat1.x + spat1.y * spat1.y);
							double angle = mid / (spat0Lenght * spat1Lenght);

							midPalmCircle = new Point();
							midPalmCircle.x = tl.x + (br.x - tl.x) / 2;
							midPalmCircle.y = br.y + (tl.y - br.y) / 2;
							palmSize = (int) (br.x - tl.x) / 4;

							if ((diffDepth > palmSize * 0.7) && (angle >= -0.7)
									&& (!isInBounds(contourSpat0, mat))
									&& (!isInBounds(contourSpat1, mat))) {

								Point finVec0 = new Point(contourSpat0.x - midPalmCircle.x,
										contourSpat0.y - midPalmCircle.y);
								double finAngle0 = Math.atan2(finVec0.y, finVec0.x);
								Point finVec1 = new Point(contourSpat1.x - midPalmCircle.x,
										contourSpat1.y - midPalmCircle.y);
								double finAngle1 = Math.atan2(finVec1.y, finVec1.x);

								if (fingertips.size() == 0) {
									fingertips.put(finAngle0, contourSpat0);
									fingertips.put(finAngle1, contourSpat1);
								} else {
									fingertips.put(finAngle0, contourSpat0);
									fingertips.put(finAngle1, contourSpat1);
								}

								for (Double val : fingertips.keySet()) {
									circlesToDraw.put(fingertips.get(val), new Scalar(255, 0, 0));
								}
							}
						}
					}
				}

				if (isHandDetected(mat)) {
					isDetected = true;

					drawGravityCircle(mat, contours);
					Imgproc.rectangle(mat, boundingRect.tl(), boundingRect.br(), new Scalar(255, 0, 0), 2);
					Imgproc.drawContours(mat, convexHullPoints, biggestID, new Scalar(255, 0, 0));
				} else {
					isDetected = false;
				}
			}
		} catch (Exception e) {
			Console.getInstance().log(this.getClass().getSimpleName() + " err: " + e.getMessage());
		}

		return convertToGrayBufferedImage(mat);
	}

	private boolean isInBounds(Point point, Mat mat) {
		int margin = 10;
		boolean result = true;
		
		if ((point.x > margin) && (point.y > margin) && (point.x < mat.cols() - margin) && (point.y < mat.rows() - margin)) {
			result = false;
		}

		return result;
	}

	private boolean isHandDetected(Mat mat) {
		boolean result = false;

		if (boundingRect != null) {
			int midPointX = boundingRect.width;
			int midPointY = boundingRect.height;

			if (midPointX > mat.rows() / 3 && midPointY > mat.cols() / 4) {
				result = true;
			}
		}

		return result;
	}

	private BufferedImage convertToGrayBufferedImage(Mat mat) {
		BufferedImage bufferedImage = new BufferedImage(mat.width(), mat.height(), BufferedImage.TYPE_BYTE_GRAY);
		WritableRaster writableRaster = bufferedImage.getRaster();
		DataBufferByte dataBufferByte = (DataBufferByte) writableRaster.getDataBuffer();

		byte[] data = dataBufferByte.getData();
		mat.get(0, 0, data);

		return bufferedImage;
	}

	private BufferedImage convertToBGRBufferedImage(Mat mat) {
		int width = mat.width();
		int height = mat.height();

		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);

		WritableRaster writableRaster = bufferedImage.getRaster();
		DataBufferByte dataBufferByte = (DataBufferByte) writableRaster.getDataBuffer();

		byte[] data = dataBufferByte.getData();
		mat.get(0, 0, data);

		return bufferedImage;
	}

	private boolean drawGravityCircle(Mat mat, List<MatOfPoint> contours) {
		boolean result = false;

		try {
			Moments moments = Imgproc.moments(contours.get(biggestID), false);

			double center00 = moments.get_m00();
			double center10 = moments.get_m10();
			double center01 = moments.get_m01();

			if (center00 != 0) {
				int xCenter = (int) Math.round((center10 / center00));
				int yCenter = (int) Math.round((center01 / center00));

				circlesToDraw.put(new Point(xCenter, yCenter), new Scalar(0, 0, 255));

				result = true;
			}

		} catch (Exception e) {
			Console.getInstance().log(this.getClass().getSimpleName() + " err: " + e.getMessage());
		}

		return result;
	}
}
