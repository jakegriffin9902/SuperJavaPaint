package com.example.superjavapaint;

import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

/**
 * Contains static methods used on SJPCanvases to alter the entire canvas image in various ways.
 */
public class Transformations {
    /**
     * Records an image of a canvas, and draws the image onto the original canvas with all y-coordinates inverted
     * @param canvas to be flipped
     */
    public static void verticalFlip(SJPCanvas canvas) {
        Image capture = canvas.getRegion(0, 0, canvas.getWidth(), canvas.getHeight());
        canvas.getGraphicsContext2D().drawImage(capture, 0, 0, capture.getWidth(),
                capture.getHeight(), 0,capture.getHeight(),capture.getWidth(),-capture.getHeight());
        canvas.getUndoRedo().updateStacks(canvas);
    }

    /**
     * Records an image of a canvas, and draws the image onto the original canvas with all x-coordinates inverted
     * @param canvas to be flipped
     */
    public static void horizontalFlip(SJPCanvas canvas) {
        Image capture = canvas.getRegion(0, 0, canvas.getWidth(), canvas.getHeight());
        canvas.getGraphicsContext2D().drawImage(capture, 0, 0, capture.getWidth(),
                capture.getHeight(), capture.getWidth(),0,-capture.getWidth(),capture.getHeight());
        canvas.getUndoRedo().updateStacks(canvas);
    }

    /**
     * Records an image of the canvas, swaps the width and height of the canvas, and rotates the image by 90 degrees
     * clockwise before drawing the image on the newly-resized canvas.
     * @param canvas The canvas to be rotated
     */
    public static void rotate(SJPCanvas canvas) {
        Image capture = canvas.getRegion(0, 0, canvas.getWidth(), canvas.getHeight());
        ImageView iv = new ImageView(capture);
        iv.setRotate(90);
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        Image rotatedCapture = iv.snapshot(params, null);

        //Resizes the canvas by swapping width and height
        double width = canvas.getWidth();
        canvas.setWidth(canvas.getHeight());
        canvas.setHeight(width);

        canvas.getGraphicsContext2D().drawImage(rotatedCapture, 0, 0);
        canvas.getUndoRedo().updateStacks(canvas);
    }
}
