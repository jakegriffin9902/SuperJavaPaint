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
     * Uses a canvas's canvasSettings object and a linecap to set up the Graphics Context before drawing on the canvas
     * @param canvas an SJPCanvas, which comes from a subclass of Canvas and contains a GraphicsContext
     * @return the graphicsContext after modifications are applied, so it can be used by other Draw methods
     */
    public static GraphicsContext prepGC(SJPCanvas canvas, StrokeLineCap lineCap) {
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.setLineWidth(canvas.getCanvasSettings().getLineWidth());
        graphicsContext.setLineCap(lineCap);

        if (canvas.getCanvasSettings().isDashed()) {graphicsContext.setLineDashes(4);}
        else {graphicsContext.setLineDashes(1);}

        graphicsContext.setStroke(canvas.getCanvasSettings().getColor());
        graphicsContext.setFill(canvas.getCanvasSettings().getColor());
        return graphicsContext;
    }

    /**
     * Draws on the canvas at the mouse's location when pressed
     * @param canvas the SJPCanvas on which to draw
     * @param x the x point at which to draw
     * @param y the y point at which to draw
     */
    public static void drawPress(SJPCanvas canvas, double x, double y) {
        GraphicsContext graphicsContext = prepGC(canvas, StrokeLineCap.ROUND);
        graphicsContext.beginPath();
        graphicsContext.moveTo(x, y);
        graphicsContext.stroke();
    }

    /**
     * Draws on the canvas at the mouse's location when the mouse is dragged, connecting the newly added points to
     * the previously drawn points.
     * @param canvas the SJPCanvas on which to draw
     * @param x the x point at which to draw
     * @param y the y point at which to draw
     */
    public static void drawDrag(SJPCanvas canvas, double x, double y) {
        GraphicsContext graphicsContext = prepGC(canvas, StrokeLineCap.ROUND);
        graphicsContext.lineTo(x, y);
        graphicsContext.stroke();
        graphicsContext.closePath();
        graphicsContext.beginPath();
        graphicsContext.lineTo(x, y);
    }

    /**
     * Temporarily changes the stroke color to white, and then calls the drawPress function to act as an eraser.
     * Once the erasing is complete, the canvas's stroke color is changed back to the initial color.
     * @param canvas the SJPCanvas on which to erase
     * @param x the x point at which to erase
     * @param y the y point at which to erase
     */
    public static void erasePress(SJPCanvas canvas, double x, double y) {
        Color temp = canvas.getCanvasSettings().getColor();
        canvas.getCanvasSettings().setColor(Color.WHITE);
        drawPress(canvas, x, y);
        canvas.getCanvasSettings().setColor(temp);
    }

    /**
     * Temporarily changes the stroke color to white, and then calls the drawDrag function to act as an eraser.
     * Once the erasing is complete, the canvas's stroke color is changed back to the initial color.
     * @param canvas the SJPCanvas on which to erase
     * @param x the x point at which to erase
     * @param y the y point at which to erase
     */
    public static void eraseDrag(SJPCanvas canvas, double x, double y) {
        Color temp = canvas.getCanvasSettings().getColor();
        canvas.getCanvasSettings().setColor(Color.WHITE);
        drawDrag(canvas, x, y);
        canvas.getCanvasSettings().setColor(temp);
    }

    /**
     * Takes in two x- and y- coordinates, determines the minimum and maximum coordinates for each direction, and draws
     * a rectangle using the canvas's settings from the minimum point to the maximum point.
     * @param canvas the SJPCanvas on which to draw the rectangle
     * @param x1 the initial x coordinate
     * @param y1 the initial y coordinate
     * @param x2 the final x coordinate
     * @param y2 the final y coordinate
     */
    public static void rectangle(SJPCanvas canvas, double x1, double y1, double x2, double y2) {
        double initialX = Math.min(x1, x2);
        double initialY = Math.min(y1, y2);

        double width = Math.abs(x1 - x2);
        double height = Math.abs(y1 - y2);

        GraphicsContext graphicsContext = prepGC(canvas, StrokeLineCap.SQUARE);
        graphicsContext.strokeRect(initialX, initialY, width, height);

        if (canvas.getCanvasSettings().isFilled()) {
            graphicsContext.fillRect(initialX, initialY, width, height);
        }
    }

    // This behaves identically to the "rectangle" function, but draws a round rectangle instead of a regular one.
    public static void roundRectangle(SJPCanvas canvas, double x1, double y1, double x2, double y2) {

        double initialX = Math.min(x1, x2);
        double initialY = Math.min(y1, y2);

        double width = Math.abs(x1 - x2);
        double height = Math.abs(y1 - y2);

        GraphicsContext graphicsContext = prepGC(canvas, StrokeLineCap.ROUND);
        graphicsContext.strokeRoundRect(initialX, initialY, width, height, (height+width)/12, (height+width)/12);

        if (canvas.getCanvasSettings().isFilled()) {
            graphicsContext.fillRoundRect(initialX, initialY, width, height, (height+width)/12, (height+width)/12);
        }
    }

    /*
    Takes as arguments 4 doubles, the x- and y-coordinates of 2 mouseEvents
    Calculates the absolute value differences of the x coordinates and the y coordinates to find the average.
    The average is then used as the square's side length.
     */

    /**
     * Draws a square on a canvas by determining the distance between the first and last x- and y- points and averaging
     * the two results. This average is used as the side length of the square.
     * @param canvas the canvas on which to draw the square
     * @param x1 the initial x coordinate
     * @param y1 the initial y coordinate
     * @param x2 the final x coordinate
     * @param y2 the final y coordinate
     */
    public static void square(SJPCanvas canvas, double x1, double y1, double x2, double y2) {
        double sideLength = (Math.abs(x1 - x2) + Math.abs(y1 - y2)) / 2;
        if (x2 > x1) {
            if (y2 > y1) {Draw.rectangle(canvas, x1, y1, x1+sideLength, y1+sideLength);}
            else {Draw.rectangle(canvas, x1, y1-sideLength, x1+sideLength, y1);}
        }
        else {
            if (y2 > y1) {Draw.rectangle(canvas, x1-sideLength, y1, x1, y1+sideLength);}
            else {Draw.rectangle(canvas, x1-sideLength, y1-sideLength, x1, y1);}
        }
    }

    // This function behaves identically to the rectangle function, but it draws a different shape.
    public static void ellipse(SJPCanvas canvas, double x1, double y1, double x2, double y2) {

        double initialX = Math.min(x1, x2);
        double initialY = Math.min(y1, y2);

        double width = Math.abs(x1 - x2);
        double height = Math.abs(y1 - y2);

        GraphicsContext graphicsContext = prepGC(canvas, StrokeLineCap.ROUND);
        graphicsContext.strokeOval(initialX, initialY, width, height);

        if (canvas.getCanvasSettings().isFilled()) {
            graphicsContext.fillOval(initialX, initialY, width, height);
        }
    }


    // This function behaves identically to the square function, except it draws a circle
    public static void circle(SJPCanvas canvas, double x1, double y1, double x2, double y2) {
        double diameter = (Math.abs(x1 - x2) + Math.abs(y1 - y2)) / 2;
        if (x2 > x1) {
            if (y2 > y1) {Draw.ellipse(canvas, x1, y1, x1+diameter, y1+diameter);}
            else {Draw.ellipse(canvas, x1, y1-diameter, x1+diameter, y1);}
        }
        else {
            if (y2 > y1) {Draw.ellipse(canvas, x1-diameter, y1, x1, y1+diameter);}
            else {Draw.ellipse(canvas, x1-diameter, y1-diameter, x1, y1);}
        }
    }

    /**
     * Takes in two coordinate arrays and a canvas, drawing a triangle between the first three points in the arrays.
     * @param canvas the canvas on which to draw the triangle
     * @param x the array of x-coordinates
     * @param y the array of y-coordinates
     */
    public static void triangle(SJPCanvas canvas, double[] x, double[] y) {
        GraphicsContext graphicsContext = prepGC(canvas, StrokeLineCap.ROUND);
        graphicsContext.strokePolygon(x, y, 3);
        if (canvas.getCanvasSettings().isFilled()) {
            graphicsContext.fillPolygon(x, y, 3);
        }
    }

    public static void line(SJPCanvas canvas, double x1, double y1, double x2, double y2) {
        GraphicsContext graphicsContext = prepGC(canvas, StrokeLineCap.ROUND);
        graphicsContext.strokeLine(x1, y1, x2, y2);
    }

    /**
     * Draws a shape by connecting all points from two coordinate arrays
     * @param canvas the canvas on which to draw the shape
     * @param vertexCount an integer used to define the number of points to use
     * @param x the array of x-values
     * @param y the array of y-values
     */
    public static void shape(SJPCanvas canvas, int vertexCount, double[] x, double[] y) {
        GraphicsContext graphicsContext = prepGC(canvas, StrokeLineCap.ROUND);
        graphicsContext.strokePolygon(x, y, vertexCount);
        if (canvas.getCanvasSettings().isFilled()) {
            graphicsContext.fillPolygon(x, y, vertexCount);
        }
    }
}