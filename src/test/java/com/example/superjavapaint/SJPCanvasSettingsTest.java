package com.example.superjavapaint;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SJPCanvasSettingsTest {

    @Test
    public void constructorGeneratesPositiveWidth() {
        SJPCanvasSettings testSettings = new SJPCanvasSettings(-2);
        assertEquals(2, testSettings.getLineWidth());
    }

    @Test
    public void typeInitializedToDraw() {
        SJPCanvasSettings testSettings = new SJPCanvasSettings();
        assertEquals("Draw", testSettings.getType());
    }

    @Test
    public void setLineWidthCorrectsNegativeInput() {
        SJPCanvasSettings testSettings = new SJPCanvasSettings();
        testSettings.setLineWidth(-5);
        assertEquals(5, testSettings.getLineWidth());
    }

}