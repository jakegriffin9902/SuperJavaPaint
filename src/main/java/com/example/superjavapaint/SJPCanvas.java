package com.example.superjavapaint;

import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;

import java.io.File;

import static com.example.superjavapaint.PaintApp.*;

/**
 * Extends the Canvas class to contain additional information and responses to mouse events.
 * This class communicates with Draw for many actions rather than containing them within this class itself - this
 * is done to improve readability.
 */
public class SJPCanvas extends Canvas {

    private UndoRedo undoRedo;
    private Boolean isSaved;
    private int vCount;
    private double[] xVals, yVals;
    private Image capture, rotatedCapture;
    private SJPCanvasSettings canvasSettings;
    private File storedFile;

    public SJPCanvas() {
        super(800, 600);
        canvasSettings = new SJPCanvasSettings();
        vCount = 0;
        xVals = new double[50];
        yVals = new double[50];
        undoRedo = new UndoRedo(this);
        storedFile = null;

        getGraphicsContext2D().setImageSmoothing(false);
        setIsSaved(true);

        this.getGraphicsContext2D().drawImage(undoRedo.getUndoImage().peek(), 0, 0);

        //Whenever the mouse is pressed, a new vertex's x and y coordinates are added to their respective arrays, indexed by "vertexCounter".
        //vertexCounter is always reset to zero when a new Tool is selected or when a new item (shape/line/etc.) is added to the canvas (meaning a new item is starting)
        this.setOnMousePressed(mouseEvent -> {
            xVals[vCount] = mouseEvent.getX();
            yVals[vCount] = mouseEvent.getY();

            switch (canvasSettings.getType()) {
                case "Draw" -> Draw.drawPress(this, mouseEvent.getX(), mouseEvent.getY());
                case "Erase" -> Draw.erasePress(this, mouseEvent.getX(), mouseEvent.getY());
                case "Eyedropper" -> canvasSettings.setColor((this.getRegion(mouseEvent.getX(), mouseEvent.getY(),
                        mouseEvent.getX() + 1, mouseEvent.getY() + 1)).getPixelReader().getColor(0, 0));

                //TRIANGLE, like most other shapes, starts the counter at zero, and force stops at 2, drawing a triangle with the 3 collected points
                case "Triangle" -> {
                    if (vCount < 2) {vCount++;}
                    else { // vertexCounter == 2
                        Draw.triangle(this, xVals, yVals);
                        vCount = 0;
                        undoRedo.updateStacks(this);
                    }
                }

                /*
                Shape uses the arrays of x- and y- coordinates to draw connected lines and closes the shape when either
                the vertex counter reaches 50 (the size of the arrays) or the user clicks within a certain distance of
                the initial point.

                When a shape is drawn, the final point is set to be equal to the first point, correcting user error
                and/or auto closing a shape that could have been left open if the 50th point was not equal to the first.
                 */
                case "Shape" -> {
                    vCount++;
                    if (vCount > 1 && (Math.abs(xVals[vCount - 1] - xVals[0]) >= 5 || Math.abs(yVals[vCount - 1] - yVals[0]) >= 5)) {
                        Draw.line(this, xVals[vCount - 2], yVals[vCount - 2], xVals[vCount - 1], yVals[vCount - 1]);
                    }
                    if (vCount >= 50 || ((Math.abs(xVals[vCount - 1] - xVals[0]) < 5 && Math.abs(yVals[vCount - 1] - yVals[0]) < 5) && vCount > 1)) {
                        xVals[vCount - 1] = xVals[0];
                        yVals[vCount - 1] = yVals[0];
                        Draw.shape(this, vCount, xVals, yVals);
                        vCount = 0;
                    }
                    undoRedo.updateStacks(this);
                }

                case "Rectangle" -> {
                    if (vCount == 0) {vCount++;}
                    else {
                        Draw.rectangle(this, xVals[vCount - 1], yVals[vCount - 1], xVals[vCount], yVals[vCount]);
                        vCount = 0;
                        undoRedo.updateStacks(this);
                    }
                }

                case "Round Rectangle" -> {
                    if (vCount == 0) {vCount++;}
                    else {
                        Draw.roundRectangle(this, xVals[vCount - 1], yVals[vCount - 1], xVals[vCount], yVals[vCount]);
                        vCount = 0;
                        undoRedo.updateStacks(this);
                    }
                }

                case "Square" -> {
                    if (vCount == 0) {vCount++;}
                    else {
                        Draw.square(this, xVals[vCount - 1], yVals[vCount - 1], xVals[vCount], yVals[vCount]);
                        vCount = 0;
                        undoRedo.updateStacks(this);
                    }
                }

                case "Oval" -> {
                    if (vCount == 0) {vCount++;}
                    else {
                        Draw.ellipse(this, xVals[vCount - 1], yVals[vCount - 1], xVals[vCount], yVals[vCount]);
                        vCount = 0;
                        undoRedo.updateStacks(this);
                    }
                }

                case "Circle" -> {
                    if (vCount == 0) {vCount++;}
                    else {
                        Draw.circle(this, xVals[vCount - 1], yVals[vCount - 1], xVals[vCount], yVals[vCount]);
                        vCount = 0;
                        undoRedo.updateStacks(this);
                    }
                }

                case "Line" -> {
                    if (vCount == 0) {vCount++;}
                    else {
                        Draw.line(this, xVals[vCount - 1], yVals[vCount - 1], xVals[vCount], yVals[vCount]);
                        vCount = 0;
                        undoRedo.updateStacks(this);
                    }
                }

                case "Text" -> {
                    GraphicsContext graphicsContext = Draw.prepGC(this, StrokeLineCap.ROUND);
                    graphicsContext.setFont(Font.font(15));
                    graphicsContext.fillText(mainTextField.getText(), mouseEvent.getX(), mouseEvent.getY());
                    undoRedo.updateStacks(this);
                }

                case "Paste" -> {
                    GraphicsContext graphicsContext = Draw.prepGC(this, StrokeLineCap.SQUARE);
                    if (rotatedCapture == null) {rotatedCapture = capture;}
                    xVals[0] = mouseEvent.getX();
                    yVals[0] = mouseEvent.getY();
                    graphicsContext.drawImage(rotatedCapture, mouseEvent.getX()-rotatedCapture.getWidth()/2, mouseEvent.getY()-rotatedCapture.getHeight()/2);
                    undoRedo.updateStacks(this);
                }

                case "Copy" -> {
                    if (vCount == 0) {vCount++;}
                    else {
                        mainToolbar.setRotationAngle(0);
                        capture = getRegion(
                                Math.min(xVals[vCount - 1], xVals[vCount])+1, Math.min(yVals[vCount -1], yVals[vCount])+1,
                                Math.max(xVals[vCount-1], xVals[vCount])-1, Math.max(yVals[vCount -1], yVals[vCount])-1);
                        rotatedCapture = capture;
                        vCount = 0;
                    }
                }

                case "Cut" -> {
                    if (vCount == 0) {vCount++;}
                    else {
                        mainToolbar.setRotationAngle(0);
                        updateLiveDraw();
                        capture = getRegion(
                                Math.min(xVals[vCount - 1], xVals[vCount])+1, Math.min(yVals[vCount -1], yVals[vCount])+1,
                                Math.max(xVals[vCount-1], xVals[vCount])-1, Math.max(yVals[vCount -1], yVals[vCount])-1);
                        rotatedCapture = capture;
                        SJPCanvasSettings tempSettings = getCanvasSettings();
                        canvasSettings = new SJPCanvasSettings(1, true, false, Color.WHITE, "Cut");
                        Draw.rectangle(this, Math.min(xVals[vCount - 1], xVals[vCount])+1, Math.min(yVals[vCount -1], yVals[vCount])+1,
                                Math.max(xVals[vCount-1], xVals[vCount])-1, Math.max(yVals[vCount -1], yVals[vCount])-1);
                        canvasSettings = tempSettings;
                        undoRedo.updateStacks(this);
                        vCount = 0;
                    }
                }
            }

            //Updates the ColorPicker to match the color held by the canvas
            //This prevents the eyedropper from creating an inconsistency between the two components.
            if (mainToolbar.getColorPicker() != mainCanvas.canvasSettings.getColor()) {mainToolbar.setColorPicker(mainCanvas.canvasSettings.getColor());}
        });


        this.setOnMouseMoved(mouseEvent -> {
            updateLiveDraw();
            switch (canvasSettings.getType()) {
                case "Triangle" -> {
                    if (vCount == 1) {Draw.line(this, xVals[0], yVals[0], mouseEvent.getX(), mouseEvent.getY());}
                    if (vCount == 2) {
                        xVals[2] = mouseEvent.getX();
                        yVals[2] = mouseEvent.getY();
                        Draw.triangle(this, xVals, yVals);
                    }
                }

                case "Shape" -> {
                    if (vCount > 0 && (Math.abs(mouseEvent.getX() - xVals[0]) >= 5 || Math.abs(mouseEvent.getY() - yVals[0]) >= 5)) {
                        Draw.line(this, xVals[vCount - 1], yVals[vCount - 1], mouseEvent.getX(), mouseEvent.getY());
                    }
                    if (vCount >= 50 || ((Math.abs(mouseEvent.getX() - xVals[0]) < 5 && Math.abs(mouseEvent.getY() - yVals[0]) < 5) && vCount > 1)) {
                        Draw.shape(this, vCount, xVals, yVals);
                    }
                }

                case "Rectangle" -> {
                    if (vCount > 0) {Draw.rectangle(this, xVals[0], yVals[0], mouseEvent.getX(), mouseEvent.getY());}
                }

                case "Round Rectangle" -> {
                    if (vCount > 0) {Draw.roundRectangle(this, xVals[0], yVals[0], mouseEvent.getX(), mouseEvent.getY());}
                }

                case "Square" -> {
                    if (vCount > 0) {Draw.square(this, xVals[0], yVals[0], mouseEvent.getX(), mouseEvent.getY());}
                }

                case "Oval" -> {
                    if (vCount > 0) {Draw.ellipse(this, xVals[0], yVals[0], mouseEvent.getX(), mouseEvent.getY());}
                }

                case "Circle" -> {
                    if (vCount == 1) {Draw.circle(this, xVals[0], yVals[0], mouseEvent.getX(), mouseEvent.getY());}
                }

                case "Line" -> {
                    if (vCount == 1) {Draw.line(this, xVals[0], yVals[0], mouseEvent.getX(), mouseEvent.getY());}
                }

                case "Text" -> {
                    GraphicsContext graphicsContext = Draw.prepGC(this, StrokeLineCap.ROUND);
                    graphicsContext.setFont(Font.font(15));
                    graphicsContext.fillText(mainTextField.getText(), mouseEvent.getX(), mouseEvent.getY());
                }

                case "Paste" -> {
                    GraphicsContext graphicsContext = Draw.prepGC(this, StrokeLineCap.SQUARE);
                    xVals[0] = mouseEvent.getX();
                    if (rotatedCapture == null) {rotatedCapture = capture;}
                    xVals[1] = xVals[0] + rotatedCapture.getWidth();
                    yVals[0] = mouseEvent.getY();
                    yVals[1] = yVals[0] + rotatedCapture.getHeight();
                    graphicsContext.drawImage(rotatedCapture, mouseEvent.getX()-rotatedCapture.getWidth()/2, mouseEvent.getY()-rotatedCapture.getHeight()/2);
                }

                case "Copy" -> {
                    if (vCount == 1) {
                        SJPCanvasSettings tempSettings = getCanvasSettings();
                        canvasSettings = new SJPCanvasSettings(1, false, true, Color.RED, "Copy");
                        Draw.rectangle(this, xVals[0], yVals[0], mouseEvent.getX(), mouseEvent.getY());
                        canvasSettings = tempSettings;
                    }
                }

                case "Cut" -> {
                    if (vCount == 1) {
                        SJPCanvasSettings tempSettings = getCanvasSettings();
                        canvasSettings = new SJPCanvasSettings(1, false, true, Color.RED, "Cut");
                        Draw.rectangle(this, xVals[0], yVals[0], mouseEvent.getX(), mouseEvent.getY());
                        canvasSettings = tempSettings;
                    }
                }
            }
        });

        this.setOnMouseDragged(mouseEvent -> {
            if (canvasSettings.getType().equals("Draw")) { Draw.drawDrag(this, mouseEvent.getX(), mouseEvent.getY()); }
            if (canvasSettings.getType().equals("Erase")) {Draw.eraseDrag(this, mouseEvent.getX(), mouseEvent.getY());}
        });


        // This prevents unexpected behavior when using "Draw" and "Erase".
        // It ensures that the stroke path closes when the mouse is released so that distinct lines aren't joined.
        this.setOnMouseReleased(mouseEvent -> {
            if (canvasSettings.getType().equals("Draw") || canvasSettings.getType().equals("Erase")) {
                getGraphicsContext2D().closePath();
                undoRedo.updateStacks(this);
            }
        });
    }

    /**
     *  Takes 2 sets of coordinates to capture a rectangular section of the canvas and create an image.
     * @param x1 the initial x coordinate of the desired region
     * @param y1 the initial y coordinate of the desired region
     * @param x2 the final x coordinate of the desired region
     * @param y2 the final y coordinate of the desired region
     * @return the image captured
     */
    public Image getRegion(double x1, double y1, double x2, double y2) {
        double width = Math.abs(x1 - x2);
        double height = Math.abs(y1 - y2);

        SnapshotParameters sp = new SnapshotParameters();
        WritableImage writableImage = new WritableImage((int) width, (int) height);

        Rectangle2D rectangle2D = new Rectangle2D((Math.min(x1, x2)), (Math.min(y1, y2)), width, height);
        sp.setViewport(rectangle2D);
        this.snapshot(sp, writableImage);
        return writableImage;
    }

    /**
     * Updates the canvas based on the image stored in Undo. This is done to prevent every shape developed during live
     * draw from staying on the final canvas.
     */
    public void updateLiveDraw() {
        GraphicsContext graphicsContext = getGraphicsContext2D();
        graphicsContext.drawImage(getUndoRedo().getUndoImage().peek(), 0, 0);
    }

    public void setIsSaved(Boolean isSaved) {this.isSaved = isSaved;}
    public void setVCount(int shapeVertexCounter) {this.vCount = shapeVertexCounter;}
    public void setCanvasSettings(SJPCanvasSettings canvasSettings) {this.canvasSettings = canvasSettings;}
    public void setRotatedCapture(Image rotatedCapture) {this.rotatedCapture = rotatedCapture;}
    public void setStoredFile(File file) {storedFile = file;}

    public Boolean getIsSaved() {return isSaved;}
    public SJPCanvasSettings getCanvasSettings() {return canvasSettings;}
    public Image getCapture() {return capture;}
    public UndoRedo getUndoRedo() {return undoRedo;}
    public File getStoredFile() {return storedFile;}
}