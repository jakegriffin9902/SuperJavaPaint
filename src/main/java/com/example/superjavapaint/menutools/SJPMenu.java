package com.example.superjavapaint.menutools;

import com.example.superjavapaint.PaintApp;
import com.example.superjavapaint.SJPCanvasSettings;
import com.example.superjavapaint.SJPTab;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCodeCombination;

import java.io.File;
import java.io.IOException;

public class SJPMenu extends MenuBar {

    public SJPMenu(File[] storedFile) {
        super();

        //Sets up the FILE section of the MenuBar
        Menu file = new Menu("File");
            MenuItem newCanvas = new MenuItem("New Canvas");
                newCanvas.setOnAction(actionEvent -> {
                    FileControls.newCanvas(PaintApp.mainCanvas, 800, 600);
                    storedFile[0] = null;
                });
            MenuItem newCustom = new MenuItem("New Custom Canvas");
                newCustom.setOnAction(actionEvent -> {
                    try {
                        FileControls.newCanvasPrompter(PaintApp.mainCanvas);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    storedFile[0] = null;
                });
            MenuItem openFile = new MenuItem("Open Image");
            MenuItem saveFile = new MenuItem("Save");
            MenuItem saveAs = new MenuItem("Save As...");

        //Sets the actions for each of the menuItems.
        openFile.setOnAction(actionEvent -> storedFile[0] = FileControls.open(PaintApp.mainCanvas));
        saveFile.setOnAction(actionEvent -> FileControls.save(PaintApp.mainCanvas, storedFile[0]));
        saveAs.setOnAction(actionEvent -> FileControls.saveAs(PaintApp.mainCanvas));

        file.getItems().addAll(newCanvas, newCustom, openFile, saveFile, saveAs);

        //sets all accelerators for the file portion of the MenuBar
        newCanvas.setAccelerator(KeyCodeCombination.keyCombination("Ctrl+N"));
        newCustom.setAccelerator(KeyCodeCombination.keyCombination("Ctrl+Shift+N"));
        openFile.setAccelerator(KeyCodeCombination.keyCombination("Ctrl+O"));
        saveFile.setAccelerator(KeyCodeCombination.keyCombination("Ctrl+S"));
        saveAs.setAccelerator(KeyCodeCombination.keyCombination("Ctrl+Shift+S"));

        Menu edit = new Menu("Edit");
            MenuItem undo = new MenuItem("Undo");
                undo.setOnAction(actionEvent -> EditControls.undo(PaintApp.mainCanvas));
                undo.setAccelerator(KeyCodeCombination.keyCombination("Ctrl+Z"));
            MenuItem redo = new MenuItem("Redo");
                redo.setOnAction(actionEvent -> EditControls.redo(PaintApp.mainCanvas));
                redo.setAccelerator(KeyCodeCombination.keyCombination("Ctrl+Shift+Z"));
            MenuItem rotate = new MenuItem("Rotate Canvas 90º");
                rotate.setOnAction(actionEvent -> EditControls.rotate(PaintApp.mainCanvas));
                rotate.setAccelerator(KeyCodeCombination.keyCombination("Ctrl+R"));
            MenuItem vFlip = new MenuItem("Flip Canvas Vertically");
                vFlip.setOnAction(actionEvent -> {
                    Image capture = PaintApp.mainCanvas.getRegion(0, 0, PaintApp.mainCanvas.getWidth(), PaintApp.mainCanvas.getHeight());
                    PaintApp.mainCanvas.getGraphicsContext2D().drawImage(capture, 0, 0, capture.getWidth(), capture.getHeight(), capture.getWidth(),0,-capture.getWidth(),capture.getHeight());
                    PaintApp.mainCanvas.updateCanvas();
                });
                vFlip.setAccelerator(KeyCodeCombination.keyCombination("Ctrl+F"));
        MenuItem hFlip = new MenuItem("Flip Canvas Vertically");
        hFlip.setOnAction(actionEvent -> {
            Image capture = PaintApp.mainCanvas.getRegion(0, 0, PaintApp.mainCanvas.getWidth(), PaintApp.mainCanvas.getHeight());
            PaintApp.mainCanvas.getGraphicsContext2D().drawImage(capture, 0, 0, capture.getWidth(), capture.getHeight(), 0,capture.getHeight(),capture.getWidth(),-capture.getHeight());
            PaintApp.mainCanvas.updateCanvas();
        });
        hFlip.setAccelerator(KeyCodeCombination.keyCombination("Ctrl+G"));
        edit.getItems().addAll(undo, redo, rotate, vFlip, hFlip);



        //Sets up the WINDOW section of the MenuBar
        Menu window = new Menu("Window");
            MenuItem newTab = new Menu("New Tab");
                newTab.setOnAction(actionEvent -> {
                    PaintApp.sjpTabs.addLast(new SJPTab());
                    PaintApp.tabPane.getTabs().add(PaintApp.sjpTabs.getLast());

                    PaintApp.sjpTabs.getLast().setOnSelectionChanged(t -> {
                        for (int i = 0; i < PaintApp.sjpTabs.size(); i++) {
                            if (PaintApp.sjpTabs.get(i).isSelected()) {
                                SJPCanvasSettings tempSettings = PaintApp.mainCanvas.getCanvasSettings();
                                PaintApp.mainCanvas = PaintApp.sjpTabs.get(i).getCanvas();
                                //These lines ensure the new canvas is synced with the Toolbar
                                PaintApp.mainCanvas.setCanvasSettings(tempSettings);
                            }
                        }
                    });
                });
        window.getItems().add(newTab);

        Menu view = new Menu("View");
            MenuItem showTimer = new MenuItem("Show Autosave Timer");
                showTimer.setOnAction(actionEvent -> {

                });
            MenuItem hideTimer = new MenuItem("Hide Autosave Timer");
                hideTimer.setOnAction(actionEvent -> {

                });
        view.getItems().addAll(showTimer, hideTimer);

        //Sets up the HELP section of the MenuBar
        Menu help = new Menu("Help");
            MenuItem releaseNotes = new MenuItem("Release Notes");
            MenuItem about = new MenuItem("About");
        help.getItems().addAll(releaseNotes, about);

        super.getMenus().addAll(file, edit, window, view, help);
    }
}
