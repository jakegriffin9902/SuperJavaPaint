package com.example.superjavapaint;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;

public class SJPTab extends Tab {
    private SJPCanvas canvas;
    private ScrollPane scrollPane;

    public SJPTab() {
        super();

        canvas = new SJPCanvas();
        scrollPane = new ScrollPane(canvas);

        super.setContent(scrollPane);
    }

    public SJPCanvas getCanvas() {
        return canvas;
    }
}
