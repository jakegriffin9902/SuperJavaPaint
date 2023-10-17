package com.example.superjavapaint.menutools;

import com.example.superjavapaint.SJPCanvas;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

/**
 * Creates static methods for undo and redo, which are called by SJPMenu items, and also the method to allow for live draw
 */

public class EditControls {
    public static void undo(SJPCanvas canvas) {
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        if (canvas.getUndoImage().size() > 1) {
            canvas.getRedoWidth().push(canvas.getUndoWidth().pop());
            canvas.setWidth(canvas.getUndoWidth().peek());

            canvas.getRedoHeight().push(canvas.getUndoHeight().pop());
            canvas.setHeight(canvas.getUndoHeight().peek());

            canvas.getRedoImage().push(canvas.getUndoImage().pop());
            graphicsContext.drawImage(canvas.getUndoImage().peek(), 0, 0);
        }
        if (canvas.getUndoImage().size() > 128) {
            canvas.getUndoImage().remove(canvas.getUndoImage().lastElement());
            canvas.getRedoImage().push(canvas.getUndoImage().pop());
            canvas.getUndoWidth().remove(canvas.getUndoHeight().lastElement());
            canvas.getRedoWidth().remove(canvas.getRedoHeight().lastElement());
            canvas.getUndoHeight().remove(canvas.getUndoHeight().lastElement());
            canvas.getRedoHeight().remove(canvas.getRedoHeight().lastElement());

            graphicsContext.drawImage(canvas.getUndoImage().peek(), 0, 0);
        }
        canvas.setvCount(0);
    }

    public static void redo(SJPCanvas canvas) {
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        if (!canvas.getRedoImage().isEmpty()) {
            canvas.getUndoWidth().push(canvas.getRedoWidth().pop());
            canvas.setWidth(canvas.getRedoWidth().peek());

            canvas.getUndoHeight().push(canvas.getRedoHeight().pop());
            canvas.setHeight(canvas.getRedoHeight().peek());

            canvas.getUndoImage().push(canvas.getRedoImage().peek());
            graphicsContext.drawImage(canvas.getRedoImage().pop(), 0, 0);
        }
    }

    public static void updateLiveDraw(SJPCanvas canvas) {
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.drawImage(canvas.getUndoImage().peek(), 0, 0);
    }

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
        canvas.updateCanvas();
    }
}
