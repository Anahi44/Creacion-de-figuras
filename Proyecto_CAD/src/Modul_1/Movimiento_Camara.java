package Modul_1;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class Movimiento_Camara {
	
    private PanelDibujo panelDibujo;
    private boolean isFist = false;
    private Point previousPosition = new Point(0, 0);

    public Movimiento_Camara(PanelDibujo panelDibujo) {
        this.panelDibujo = panelDibujo;
    }

    public Mat detectHandGestures(Mat frame) {
        Mat hsvFrame = new Mat();
        Imgproc.cvtColor(frame, hsvFrame, Imgproc.COLOR_BGR2HSV);

        Scalar lower = new Scalar(0, 48, 80);
        Scalar upper = new Scalar(20, 255, 255);

        Mat mask = new Mat();
        Core.inRange(hsvFrame, lower, upper, mask);

        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(11, 11));
        Imgproc.erode(mask, mask, kernel);
        Imgproc.dilate(mask, mask, kernel);

        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        Point currentPosition = new Point(0, 0);
        for (MatOfPoint contour : contours) {
            Rect rect = Imgproc.boundingRect(contour);
            if (isHand(rect)) {
                Imgproc.rectangle(frame, rect.tl(), rect.br(), new Scalar(0, 255, 0), 2);
                currentPosition = new Point(rect.x + rect.width / 2, rect.y + rect.height / 2);
                if (isFist(rect)) {
                    if (!isFist) {
                        isFist = true;
                        panelDibujo.addFigura(new Triangulo(100, 100, 50, 50, Color.ORANGE));
                        break;
                    }
                } else {
                    isFist = false;
                    panelDibujo.addFigura(new Rectangulo(100, 100, 50, 70, Color.BLACK));
                    break;
                }
            }
        }

        if (previousPosition.y > currentPosition.y && isFist) {
            panelDibujo.addFigura(new Circulo(300, 100, 50, 50, Color.BLUE));
        }

        previousPosition = currentPosition;
        return frame;
    }

    private boolean isHand(Rect rect) {
        return rect.width > 50 && rect.height > 50;
    }

    private boolean isFist(Rect rect) {
        // Mejora esta lógica para diferenciar entre una palma y un puño
        return rect.width > 50 && rect.height > 50;
    }
}


