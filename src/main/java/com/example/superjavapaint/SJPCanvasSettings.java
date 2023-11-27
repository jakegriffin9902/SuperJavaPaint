package com.example.superjavapaint;

import javafx.scene.paint.Color;

/**
 * This class constructs objects that bundle together information about canvas settings, which are set in the toolbar.
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

    /** Constructs a new SJPCanvasSettings with all variables defined.
     *
     * @param lineWidth the width to be applied to the GraphicsContext stroke
     * @param isFilled a Boolean tracking whether shapes should be filled or empty
     * @param isDashed a Boolean tracking whether lines will be dashed or solid
     * @param color a Color to be assigned to the stroke
     * @param type a string specifying the initial shape type stored in the SJPCanvasSettings
     */
    public SJPCanvasSettings(int lineWidth, boolean isFilled, boolean isDashed, Color color, String type) {
        this.lineWidth = Math.abs(lineWidth);
        this.isFilled = isFilled;
        this.isDashed = isDashed;
        this.color = color;
        this.type = type;
    }

    public int getLineWidth() {
        return lineWidth;
    }
    public boolean isFilled() {
        return isFilled;
    }
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
