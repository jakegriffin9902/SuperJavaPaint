package com.example.superjavapaint;

import javafx.scene.paint.Color;

/** This class bundles together information about canvas settings set in the toolbar.
 * It contains only the object constructors and getters and setters as methods.
 */
public class SJPCanvasSettings{
    private int lineWidth;
    private boolean isFilled, isDashed;
    private Color color;
    private String type;

    public SJPCanvasSettings() {
        lineWidth = 1;
        isFilled = isDashed = false;
        color = Color.BLACK;
        type = "Draw";
    }

    /**
     * Constructs a new SJPCanvasSettings with a defined lineWidth.
     * @param lineWidth the initial width that will be applied to the stroke in GraphicsContext.
     */
    public SJPCanvasSettings(int lineWidth) {
        this.lineWidth = Math.abs(lineWidth);
        isFilled = isDashed = false;
        color = Color.BLACK;
        type = "Draw";
    }

    public SJPCanvasSettings(int lineWidth, boolean isFilled, boolean isDashed, Color color, String type) {
        this.lineWidth = Math.abs(lineWidth);
        this.isFilled = isFilled;
        this.isDashed = isDashed;
        this.color = color;
        this.type = type;
    }

    /**
     * @return the current integer value stored in lineWidth.
     */
    public int getLineWidth() {
        return lineWidth;
    }

    /**
     * @return the boolean value stored in isFilled.
     */
    public boolean isFilled() {
        return isFilled;
    }

    /**
     * @return the boolean value stored in isDashed.
     */
    public boolean isDashed() {
        return isDashed;
    }

    public Color getColor() {
        return color;
    }

    public String getType() {
        return type;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = Math.abs(lineWidth);
    }

    public void setFilled(boolean filled) {
        isFilled = filled;
    }

    public void setDashed(boolean dashed) {
        isDashed = dashed;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setType(String type) {
        this.type = type;
    }
}
