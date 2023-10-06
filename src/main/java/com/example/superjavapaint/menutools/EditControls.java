package com.example.superjavapaint.menutools;

import com.example.superjavapaint.SJPCanvas;
import javafx.scene.canvas.GraphicsContext;

/**
 * Creates static methods for undo and redo, which are called by SJPMenu items, and also the method to allow for live draw
 */

public class EditControls {
    public static void undo(SJPCanvas canvas) {
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        if (canvas.getUndo().size() > 1) {
            canvas.getRedo().push(canvas.getUndo().pop());
            graphicsContext.drawImage(canvas.getUndo().peek(), 0, 0);
        }
        if (canvas.getUndo().size() > 128) {
            canvas.getUndo().remove(canvas.getUndo().lastElement());
            canvas.getRedo().push(canvas.getUndo().pop());
            graphicsContext.drawImage(canvas.getUndo().peek(), 0, 0);
        }
        canvas.setvCount(0);
    }

    public static void redo(SJPCanvas canvas) {
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        if (!canvas.getRedo().isEmpty()) {
            canvas.getUndo().push(canvas.getRedo().peek());
            graphicsContext.drawImage(canvas.getRedo().pop(), 0, 0);
        }
    }

    public static void updateLiveDraw(SJPCanvas canvas) {
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.drawImage(canvas.getUndo().peek(), 0, 0);
    }
}
