package com.example.superjavapaint;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import java.util.Stack;

/**
 * Bundles together all stacks required for a canvas's undo and redo functionality. Also contains the undo and redo
 * methods themselves that are called on UndoRedo objects when the buttons are selected from the "Edit" menu.
 */
public class UndoRedo {

    private Stack<Image> undoImage, redoImage;
    private Stack<Double> undoWidth, undoHeight, redoWidth, redoHeight;

    public UndoRedo(SJPCanvas canvas) {
        undoImage = new Stack<>();
        redoImage = new Stack<>();
        undoWidth = new Stack<>();
        undoHeight = new Stack<>();
        redoWidth = new Stack<>();
        redoHeight = new Stack<>();

        undoImage.add(canvas.getRegion(0, 0, canvas.getWidth(), canvas.getHeight()));
        undoWidth.add(canvas.getWidth());
        undoHeight.add(canvas.getHeight());
    }

    /**
     * Pushes information gathered by popping the undo stacks into the redo stacks. Then peeks at the stacks to set canvas information.
     * @param canvas the canvas on which to draw the image atop the undo stack.
     */
    public void undo(SJPCanvas canvas) {
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        if (undoImage.size() > 1) {
            redoWidth.push(undoWidth.pop());
            canvas.setWidth(undoWidth.peek());

            redoHeight.push(undoHeight.pop());
            canvas.setHeight(undoHeight.peek());

            redoImage.push(undoImage.pop());
            graphicsContext.drawImage(undoImage.peek(), 0, 0);
        }
        if (undoImage.size() > 99) {
            trimUndo();
            graphicsContext.drawImage(canvas.getUndoRedo().getUndoImage().peek(), 0, 0);
        }
        canvas.setVCount(0);
    }

    /**
     * Peeks at all redo stacks and pushes the information into the corresponding undo stacks, and then sets the canvas
     * information by popping the redo stacks.
     * @param canvas the canvas on which to draw the image at the top of the redo stack.
     */
    public void redo(SJPCanvas canvas) {
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        if (!redoImage.isEmpty()) {
            undoWidth.push(redoWidth.peek());
            canvas.setWidth(redoWidth.pop());

            undoHeight.push(redoHeight.peek());
            canvas.setHeight(redoHeight.pop());

            undoImage.push(redoImage.peek());
            graphicsContext.drawImage(redoImage.pop(), 0, 0);
        }
        canvas.setVCount(0);
    }

    /**
     * Adds a canvas's dimensions and appearance to the undo stacks, and clears all information from the redo stacks.
     * This function is called whenever a new shape or item is added to the canvas.
     * @param canvas the canvas from which to gather information for the undo stacks
     */
    public void updateStacks(SJPCanvas canvas) {
        getUndoImage().push(canvas.getRegion(0, 0, canvas.getWidth(), canvas.getHeight()));
        getRedoImage().clear();
        getUndoWidth().push(canvas.getWidth());
        getUndoHeight().push(canvas.getHeight());
        getRedoWidth().clear();
        getRedoHeight().clear();
        canvas.setIsSaved(false);
    }

    /**
     * Removes the first item placed in each undo stack. This is used to prevent the stack from becoming too large.
     */
    public void trimUndo() {
        undoImage.remove(undoImage.firstElement());
        undoWidth.remove(undoWidth.firstElement());
        redoWidth.remove(undoHeight.firstElement());
    }

    public Stack<Image> getUndoImage() {return undoImage;}
    public Stack<Image> getRedoImage() {return redoImage;}
    public Stack<Double> getUndoWidth() {return undoWidth;}
    public Stack<Double> getUndoHeight() {return undoHeight;}
    public Stack<Double> getRedoWidth() {return redoWidth;}
    public Stack<Double> getRedoHeight() {return redoHeight;}
}
