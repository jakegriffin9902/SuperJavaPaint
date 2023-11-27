package com.example.superjavapaint;

import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;

/**
 *  This class creates SJPTab objects, which contain a ScrollPane holding a SJPCanvas. These objects are placed in the
 *  center of the Borderpane housed in the main application window.
 */
public class SJPTab extends Tab {
    private SJPCanvas canvas;
    private ScrollPane scrollPane;

    public SJPTab() {
        super();

        canvas = new SJPCanvas();
        scrollPane = new ScrollPane(canvas);

        super.setContent(scrollPane);
    }

    public SJPCanvas getCanvas() {return canvas;}
}
