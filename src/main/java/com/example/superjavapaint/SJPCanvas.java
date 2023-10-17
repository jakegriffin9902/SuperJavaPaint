package com.example.superjavapaint;

import com.example.superjavapaint.menutools.EditControls;
import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.util.Stack;

import static com.example.superjavapaint.PaintApp.*;

public class SJPCanvas extends Canvas {

    private Stack<Image> undoImage, redoImage;
    private Stack<Double> undoWidth, undoHeight, redoWidth, redoHeight;
    private Boolean isSaved;
    private int vCount;
    private double[] xCoords, yCoords;
    private Image capture, rotatedCapture;
    private SJPCanvasSettings canvasSettings;

    //creates a Canvas object with extra information for use in the application
    public SJPCanvas() {
        super(800, 600);
        canvasSettings = new SJPCanvasSettings();
        vCount = 0;
        xCoords = new double[50];
        yCoords = new double[50];
        undoImage = new Stack<>();
        redoImage = new Stack<>();
        undoWidth = new Stack<>();
        undoHeight = new Stack<>();
        redoWidth = new Stack<>();
        redoHeight = new Stack<>();
        undoImage.add(getRegion(0, 0, this.getWidth(), this.getHeight()));
        undoWidth.add(getWidth());
        undoHeight.add(getHeight());
        getGraphicsContext2D().setImageSmoothing(false);
        setIsSaved(true);

        this.getGraphicsContext2D().drawImage(undoImage.peek(), 0, 0);

        //Whenever the mouse is pressed, a new vertex's x and y coordinates are added to their respective arrays, indexed by "vertexCounter".
        //vertexCounter is always reset to zero when a new Tool is selected or when a new item (shape/line/etc.) is added to the canvas (meaning a new item is starting)
        this.setOnMousePressed(mouseEvent -> {
            //Is it more efficient to put this within each switch, since DRAW doesn't use it? Does it matter?
            xCoords[vCount] = mouseEvent.getX();
            yCoords[vCount] = mouseEvent.getY();

            switch (canvasSettings.getType()) {
                //DRAW, ERASE, and EYEDROPPER are unique in that they use mouseEvent coordinates
                // explicitly and do not use vertexCounter or the coordinate arrays.
                // DRAW and ERASE also rely on 3 mouseEvents, not just one.
                case "Draw" -> Draw.drawPress(this, mouseEvent.getX(), mouseEvent.getY());
                case "Erase" -> Draw.erasePress(this, mouseEvent.getX(), mouseEvent.getY());
                case "Eyedropper" -> canvasSettings.setColor((this.getRegion(mouseEvent.getX(), mouseEvent.getY(), mouseEvent.getX() + 1, mouseEvent.getY() + 1)).getPixelReader().getColor(0, 0));

                //TRIANGLE, like most other shapes, starts the counter at zero, and force stops at 2, drawing a triangle with the 3 collected points
                case "Triangle" -> {
                    if (vCount < 2) {
                        vCount++;
                    } else { // vertexCounter == 2
                        Draw.drawTriangle(this, xCoords, yCoords);
                        vCount = 0;
                        updateCanvas();
                    }
                }

                //SHAPE starts with vertexCounter at 0, grabbing all coordinates and halting when either
                // 1. vertexCounter reaches 128 (the size of the coordinate arrays), or
                // 2. the user clicks back within a certain distance of the initial point of the shape, closing the shape.
                // This also adjusts the final mouseEvent coordinates with the initial point itself.
                case "Shape" -> {
                    vCount++;
                    if (vCount > 1 && (Math.abs(xCoords[vCount - 1] - xCoords[0]) >= 5 || Math.abs(yCoords[vCount - 1] - yCoords[0]) >= 5)) {
                        Draw.drawLine(this, xCoords[vCount - 2], yCoords[vCount - 2], xCoords[vCount - 1], yCoords[vCount - 1]);
                    }
                    if (vCount >= 50 || ((Math.abs(xCoords[vCount - 1] - xCoords[0]) < 5 && Math.abs(yCoords[vCount - 1] - yCoords[0]) < 5) && vCount > 1)) {
                        xCoords[vCount - 1] = xCoords[0];
                        yCoords[vCount - 1] = yCoords[0];
                        Draw.drawShape(this, vCount, xCoords, yCoords);
                        vCount = 0;
                        updateCanvas();
                    }
                }

                case "Rectangle" -> {
                    if (vCount == 0) {vCount++;}
                    else {
                        Draw.drawRectangle(this, xCoords[vCount - 1], yCoords[vCount - 1], xCoords[vCount], yCoords[vCount]);
                        vCount = 0;
                        updateCanvas();
                    }
                }

                case "RoundRect" -> {
                    if (vCount == 0) {vCount++;}
                    else {
                        Draw.drawRoundRect(this, xCoords[vCount - 1], yCoords[vCount - 1], xCoords[vCount], yCoords[vCount]);
                        vCount = 0;
                        updateCanvas();
                    }
                }

                case "Square" -> {
                    if (vCount == 0) {vCount++;}
                    else {
                        Draw.drawSquare(this, xCoords[vCount - 1], yCoords[vCount - 1], xCoords[vCount], yCoords[vCount]);
                        vCount = 0;
                        updateCanvas();
                    }
                }

                case "Ellipse" -> {
                    if (vCount == 0) {vCount++;}
                    else {
                        Draw.drawEllipse(this, xCoords[vCount - 1], yCoords[vCount - 1], xCoords[vCount], yCoords[vCount]);
                        vCount = 0;
                        updateCanvas();
                    }
                }

                case "Circle" -> {
                    if (vCount == 0) {vCount++;}
                    else {
                        Draw.drawCircle(this, xCoords[vCount - 1], yCoords[vCount - 1], xCoords[vCount], yCoords[vCount]);
                        vCount = 0;
                        updateCanvas();
                    }
                }

                case "Line" -> {
                    if (vCount == 0) {vCount++;}
                    else {
                        Draw.drawLine(this, xCoords[vCount - 1], yCoords[vCount - 1], xCoords[vCount], yCoords[vCount]);
                        vCount = 0;
                        updateCanvas();
                    }
                }

                case "Text" -> {
                    GraphicsContext graphicsContext = Draw.prepGC(this);
                    graphicsContext.strokeText(PaintApp.textField.getText(), mouseEvent.getX(), mouseEvent.getY());
                    updateCanvas();
                }

                case "Stamp" -> {
                    GraphicsContext graphicsContext = Draw.prepGC(this);
                    if (rotatedCapture == null) {
                        rotatedCapture = capture;
                    }
                    xCoords[0] = mouseEvent.getX();
                    yCoords[0] = mouseEvent.getY();
                    graphicsContext.drawImage(rotatedCapture, mouseEvent.getX()-rotatedCapture.getWidth()/2, mouseEvent.getY()-rotatedCapture.getHeight()/2);
                    updateCanvas();
                }

                case "Select" -> {
                    if (vCount == 0) {
                        vCount++;
                    }
                    else {
                        mainToolbar.setRotationAngle(0);
                        capture = getRegion(
                                Math.min(xCoords[vCount - 1], xCoords[vCount])+1, Math.min(yCoords[vCount -1], yCoords[vCount])+1,
                                Math.max(xCoords[vCount-1], xCoords[vCount])-1, Math.max(yCoords[vCount -1], yCoords[vCount])-1);
                        rotatedCapture = capture;
                        vCount = 0;
                    }
                }
            }

            //Updates the ColorPicker to match the color held by the canvas
            //This prevents the eyedropper from creating an inconsistency between the two components.
            if (mainToolbar.getColorPicker() != mainCanvas.canvasSettings.getColor()) {mainToolbar.setColorPicker(mainCanvas.canvasSettings.getColor());}
        });


        this.setOnMouseMoved(mouseEvent -> {
            EditControls.updateLiveDraw(this);
            switch (canvasSettings.getType()) {
                case "Triangle" -> {
                    if (vCount == 1) {
                        Draw.drawLine(this, xCoords[0], yCoords[0], mouseEvent.getX(), mouseEvent.getY());
                    }
                    if (vCount == 2) {
                        xCoords[2] = mouseEvent.getX();
                        yCoords[2] = mouseEvent.getY();
                        Draw.drawTriangle(this, xCoords, yCoords);
                    }
                }

                case "Shape" -> {
                    /* THIS IS NOT FUNCTIONING... UNSURE OF HOW TO FIX
                    if (vCount > 1 && (Math.abs(mouseEvent.getX() - xCoords[0]) >= 5 || Math.abs(mouseEvent.getY() - yCoords[0]) >= 5)) {
                        Draw.drawLine(this, xCoords[vCount - 1], yCoords[vCount - 1], mouseEvent.getX(), mouseEvent.getY());
                    }
                    if (vCount >= 128 || ((Math.abs(mouseEvent.getX() - xCoords[0]) < 5 && Math.abs(mouseEvent.getY() - yCoords[0]) < 5) && vCount > 1)) {
                        Draw.drawShape(this, vCount, xCoords, yCoords);
                    }
                     */
                }

                case "Rectangle" -> {
                    if (vCount > 0) {
                        Draw.drawRectangle(this, xCoords[0], yCoords[0], mouseEvent.getX(), mouseEvent.getY());
                    }
                }

                case "RoundRect" -> {
                    if (vCount > 0) {
                        Draw.drawRoundRect(this, xCoords[0], yCoords[0], mouseEvent.getX(), mouseEvent.getY());
                    }
                }

                case "Square" -> {
                    if (vCount == 1) {
                        Draw.drawSquare(this, xCoords[0], yCoords[0], mouseEvent.getX(), mouseEvent.getY());
                    }
                }

                case "Ellipse" -> {
                    if (vCount > 0) {
                        Draw.drawEllipse(this, xCoords[0], yCoords[0], mouseEvent.getX(), mouseEvent.getY());
                    }
                }

                case "Circle" -> {
                    if (vCount == 1) {
                        Draw.drawCircle(this, xCoords[0], yCoords[0], mouseEvent.getX(), mouseEvent.getY());
                    }
                }

                case "Line" -> {
                    if (vCount == 1) {
                        Draw.drawLine(this, xCoords[0], yCoords[0], mouseEvent.getX(), mouseEvent.getY());
                    }
                }

                case "Text" -> {
                    GraphicsContext graphicsContext = Draw.prepGC(this);
                    graphicsContext.strokeText(PaintApp.textField.getText(), mouseEvent.getX(), mouseEvent.getY());
                }

                case "Stamp" -> {
                    GraphicsContext graphicsContext = Draw.prepGC(this);
                    xCoords[0] = mouseEvent.getX();
                    if (rotatedCapture == null) {
                        rotatedCapture = capture;
                    }
                    xCoords[1] = xCoords[0] + rotatedCapture.getWidth();
                    yCoords[0] = mouseEvent.getY();
                    yCoords[1] = yCoords[0] + rotatedCapture.getHeight();
                    graphicsContext.drawImage(rotatedCapture, mouseEvent.getX()-rotatedCapture.getWidth()/2, mouseEvent.getY()-rotatedCapture.getHeight()/2);
                }

                case "Select" -> {
                    if (vCount == 1) {
                        SJPCanvasSettings tempSettings = getCanvasSettings();
                        canvasSettings = new SJPCanvasSettings(1, false, true, Color.RED, "Select");
                        Draw.drawRectangle(this, xCoords[0], yCoords[0], mouseEvent.getX(), mouseEvent.getY());
                        canvasSettings = tempSettings;
                    }
                }
            }
        });


        this.setOnMouseDragged(mouseEvent -> {
            if (canvasSettings.getType().equals("Draw")) { Draw.drawDrag(this, mouseEvent.getX(), mouseEvent.getY()); }
            if (canvasSettings.getType().equals("Erase")) {Draw.eraseDrag(this, mouseEvent.getX(), mouseEvent.getY());}
        });


        this.setOnMouseReleased(mouseEvent -> {
            if (canvasSettings.getType().equals("Draw")) {
                Draw.drawRelease(this);
                updateCanvas();
            }
            if (canvasSettings.getType().equals("Erase")) {
                Draw.eraseRelease(this);
                updateCanvas();
            }
        });
    }

    //takes a canvas and its dimensions to produce an image
    /*
        dimension 1 is the minimum x coordinate of the region to be captured
        dimension 2 is the maximum x coordinate of the region
        dimension 3 is the minimum y coordinate of the region
        dimension 4 is the maximum y coordinate of the region
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

    public void updateCanvas() {
        undoImage.add(getRegion(0, 0, this.getWidth(), this.getHeight()));
        redoImage.clear();
        undoWidth.add(getWidth());
        undoHeight.add(getHeight());
        redoWidth.clear();
        redoHeight.clear();
        this.setIsSaved(false);
    }

    public void setIsSaved(Boolean isSaved) {this.isSaved = isSaved;}
    public void setvCount(int shapeVertexCounter) {this.vCount = shapeVertexCounter;}
    public void setCanvasSettings(SJPCanvasSettings canvasSettings) {
        this.canvasSettings = canvasSettings;
    }
    public void setRotatedCapture(Image rotatedCapture) {this.rotatedCapture = rotatedCapture;}

    public Boolean getIsSaved() {return isSaved;}
    public Stack<Image> getUndoImage() {return undoImage;}
    public Stack<Image> getRedoImage() {return redoImage;}

    public Stack<Double> getUndoHeight() {return undoHeight;}
    public Stack<Double> getUndoWidth() {return undoWidth;}
    public Stack<Double> getRedoHeight() {return redoHeight;}
    public Stack<Double> getRedoWidth() {return redoWidth;}

    public SJPCanvasSettings getCanvasSettings() {
        return canvasSettings;
    }
    public Image getCapture() {return capture;}
}