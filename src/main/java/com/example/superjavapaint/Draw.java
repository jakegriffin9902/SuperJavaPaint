package com.example.superjavapaint;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;

/**
 * Contains methods used by the SJPCanvas to set graphicsContext settings and draw shapes
 * All methods take the canvas as an input, and most take a pair of coordinates
 */

public class Draw {

    /**
     *
     * @param canvas an SJPCanvas, which comes from a subclass of Canvas and contains a GraphicsContext
     * @return the graphicsContext after modifications are applied, so it can be used by other Draw methods
     */
    public static GraphicsContext prepGC(SJPCanvas canvas) {
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.setLineWidth(canvas.getCanvasSettings().getLineWidth());
        graphicsContext.setLineCap(StrokeLineCap.ROUND);
        if (canvas.getCanvasSettings().isDashed()) {
            graphicsContext.setLineDashes(4);
        }
        else { //Line type is solid
            graphicsContext.setLineDashes(1);
        }
        graphicsContext.setStroke(canvas.getCanvasSettings().getColor());
        graphicsContext.setFill(canvas.getCanvasSettings().getColor());
        return graphicsContext;
    }

    //Function 1/3 for freehand draw
    public static void drawPress(SJPCanvas canvas, double x, double y) {
        GraphicsContext graphicsContext = prepGC(canvas);
        graphicsContext.beginPath();
        graphicsContext.moveTo(x, y);
        graphicsContext.stroke();
    }
    //Function 2/3 for freehand draw
    public static void drawDrag(SJPCanvas canvas, double x, double y) {
        GraphicsContext graphicsContext = prepGC(canvas);
        graphicsContext.lineTo(x, y);
        graphicsContext.stroke();
        graphicsContext.closePath();
        graphicsContext.beginPath();
        graphicsContext.lineTo(x, y);
    }
    //Function 3/3 for freehand draw
    public static void drawRelease(SJPCanvas canvas, double x, double y) {
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.lineTo(x, y);
        graphicsContext.stroke();
        graphicsContext.closePath();
    }

    public static void erasePress(SJPCanvas canvas, double x, double y) {
        Color temp = canvas.getCanvasSettings().getColor();
        canvas.getCanvasSettings().setColor(Color.WHITE);
        drawPress(canvas, x, y);
        canvas.getCanvasSettings().setColor(temp);
    }

    public static void eraseDrag(SJPCanvas canvas, double x, double y) {
        Color temp = canvas.getCanvasSettings().getColor();
        canvas.getCanvasSettings().setColor(Color.WHITE);
        drawDrag(canvas, x, y);
        canvas.getCanvasSettings().setColor(temp);
    }

    public static void eraseRelease(SJPCanvas canvas, double x, double y) {
        Color temp = canvas.getCanvasSettings().getColor();
        canvas.getCanvasSettings().setColor(Color.WHITE);
        drawRelease(canvas, x, y);
        canvas.getCanvasSettings().setColor(temp);
    }
    //takes as arguments an SJP canvas and 4 doubles
    //x1 and y1 are the initial x and y coordinates
    //x2 and y2 are the difference between start and end points (so the width and height)
    public static void drawRectangle(SJPCanvas canvas, double x1, double y1, double width, double height) {
        GraphicsContext graphicsContext = prepGC(canvas);
        graphicsContext.setLineCap(StrokeLineCap.SQUARE);
        graphicsContext.strokeRect(x1, y1, width, height);
        if (canvas.getCanvasSettings().isFilled()) {
            graphicsContext.fillRect(x1, y1, width, height);
        }
    }

    public static void drawRoundRect(SJPCanvas canvas, double x1, double y1, double width, double height) {
        GraphicsContext graphicsContext = prepGC(canvas);
        graphicsContext.setLineCap(StrokeLineCap.ROUND);
        graphicsContext.strokeRoundRect(x1, y1, width, height, (height+width)/12, (height+width)/12);
        if (canvas.getCanvasSettings().isFilled()) {
            graphicsContext.fillRoundRect(x1, y1, width, height, (height+width)/12, (height+width)/12);
        }
    }

    /*
    Takes as arguments 4 doubles, the x- and y-coordinates of 2 mouseEvents
    Calculates the absolute value differences of the x coordinates and the y coordinates to find the average.
    The average is then used as the square's side length.
     */
    public static void drawSquare(SJPCanvas canvas, double x1, double y1, double x2, double y2) {
        double sideLength = (Math.abs(x1 - x2) + Math.abs(y1 - y2)) / 2;
        if (x2 > x1) {
            if (y2 > y1) {
                Draw.drawRectangle(canvas, x1, y1, sideLength, sideLength);
            }
            else { // y position is less than initial coordinate
                Draw.drawRectangle(canvas, x1, y1-sideLength, sideLength, sideLength);
            }
        }
        else { // x position is less than initial coordinate
            if (y2 > y1) {
                Draw.drawRectangle(canvas, x1-sideLength, y1, sideLength, sideLength);
            }
            else { // y position is less than initial coordinate
                Draw.drawRectangle(canvas, x1-sideLength, y1-sideLength, sideLength, sideLength);
            }
        }
    }

    public static void drawEllipse(SJPCanvas canvas, double x1, double y1, double width, double height) {
        GraphicsContext graphicsContext = prepGC(canvas);
        graphicsContext.strokeOval(x1, y1, width, height);
        if (canvas.getCanvasSettings().isFilled()) {
            graphicsContext.fillOval(x1, y1, width, height);
        }
    }

    public static void drawCircle(SJPCanvas canvas, double x1, double y1, double x2, double y2) {
        double diameter = (Math.abs(x1 - x2) + Math.abs(y1 - y2)) / 2;
        if (x2 > x1) {
            if (y2 > y1) {
                Draw.drawEllipse(canvas, x1, y1, diameter, diameter);
            }
            else { // y position is less than initial coordinate
                Draw.drawEllipse(canvas, x1, y1-diameter, diameter, diameter);
            }
        }
        else { // x position is less than initial coordinate
            if (y2 > y1) {
                Draw.drawEllipse(canvas, x1-diameter, y1, diameter, diameter);
            }
            else { // y position is less than initial coordinate
                Draw.drawEllipse(canvas, x1-diameter, y1-diameter, diameter, diameter);
            }
        }
    }

    public static void drawTriangle(SJPCanvas canvas, double[] x, double[] y) {
        GraphicsContext graphicsContext = prepGC(canvas);
        graphicsContext.strokePolygon(x, y, 3);
        if (canvas.getCanvasSettings().isFilled()) {
            graphicsContext.fillPolygon(x, y, 3);
        }
    }

    public static void drawLine(SJPCanvas canvas, double x1, double y1, double x2, double y2) {
        GraphicsContext graphicsContext = prepGC(canvas);
        graphicsContext.strokeLine(x1, y1, x2, y2);
    }

    public static void drawShape(SJPCanvas canvas, int vertices, double[] x, double[] y) {
        GraphicsContext graphicsContext = prepGC(canvas);
        graphicsContext.strokePolygon(x, y, vertices);
        if (canvas.getCanvasSettings().isFilled()) {
            graphicsContext.fillPolygon(x, y, vertices);
        }
    }
}