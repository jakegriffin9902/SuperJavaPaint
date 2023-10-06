package com.example.superjavapaint;

import javafx.scene.paint.Color;

public class SJPCanvasSettings {
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

    public SJPCanvasSettings(int lineWidth) {
        this.lineWidth = Math.abs(lineWidth);
        isFilled = isDashed = false;
        color = Color.BLACK;
        type = "Draw";
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
