package com.example.superjavapaint.menutools;

import com.example.superjavapaint.PaintApp;
import com.example.superjavapaint.SJPCanvasSettings;
import com.example.superjavapaint.SJPTab;
import com.example.superjavapaint.Transformations;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import static com.example.superjavapaint.PaintApp.mainCanvas;

/**
 * Used to create an SJPMenu Object, which is built to contain all necessary items by default.
 */
public class SJPMenu extends MenuBar {

    public SJPMenu(File[] storedFile) {
        super();

        //Sets up the FILE section of the MenuBar
        Menu file = new Menu("File");
            MenuItem newCanvas = createMenuItem("New Canvas", "Ctrl+N");
                newCanvas.setOnAction(actionEvent -> {
                    FileControls.saveAndResetCanvas(mainCanvas,  mainCanvas.getWidth(), mainCanvas.getHeight());
                    storedFile[0] = null;
                });
            MenuItem newCustom = createMenuItem("New Custom Canvas", "Ctrl+Shift+N");
                newCustom.setOnAction(actionEvent -> {
                    try {
                        FileControls.newCanvasPrompter(mainCanvas);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    storedFile[0] = null;
                });
            MenuItem openFile = createMenuItem("Open Image", "Ctrl+O");
                openFile.setOnAction(actionEvent -> storedFile[0] = FileControls.open(mainCanvas));
            MenuItem saveFile = createMenuItem("Save", "Ctrl+S");
                saveFile.setOnAction(actionEvent -> FileControls.save(mainCanvas));
            MenuItem saveAs = createMenuItem("Save As...", "Ctrl+Shift+S");
                saveAs.setOnAction(actionEvent -> PaintApp.currentFile[0] = FileControls.saveAs(mainCanvas));

        file.getItems().addAll(newCanvas, newCustom, openFile, saveFile, saveAs);

        Menu edit = new Menu("Edit");
            MenuItem undo = createMenuItem("Undo", "Ctrl+Z");
                undo.setOnAction(actionEvent -> mainCanvas.getUndoRedo().undo(mainCanvas));
            MenuItem redo = createMenuItem("Redo", "Ctrl+Shift+Z");
                redo.setOnAction(actionEvent -> mainCanvas.getUndoRedo().redo(mainCanvas));
            MenuItem rotate = createMenuItem("Rotate Canvas 90º", "Ctrl+R");
                rotate.setOnAction(actionEvent -> Transformations.rotate(mainCanvas));
            MenuItem hFlip = createMenuItem("Flip Canvas Horizontally", "Ctrl+F");
                hFlip.setOnAction(actionEvent -> Transformations.horizontalFlip(mainCanvas));
        MenuItem vFlip = createMenuItem("Flip Canvas Vertically", "Ctrl+G");
            vFlip.setOnAction(actionEvent -> Transformations.verticalFlip(mainCanvas));
        edit.getItems().addAll(undo, redo, rotate, hFlip, vFlip);



        //Sets up the WINDOW section of the MenuBar
        Menu window = new Menu("Window");
            MenuItem newTab = createMenuItem("New Tab", "Ctrl+T");
                newTab.setOnAction(actionEvent -> {
                    PaintApp.sjpTabs.addLast(new SJPTab());
                    PaintApp.tabPane.getTabs().add(PaintApp.sjpTabs.getLast());

                    PaintApp.sjpTabs.getLast().setOnSelectionChanged(t -> {
                        for (int i = 0; i < PaintApp.sjpTabs.size(); i++) {
                            // ensures that the new canvas has the same settings as the previous, so that the menu bar appears synced.
                            if (PaintApp.sjpTabs.get(i).isSelected()) {
                                SJPCanvasSettings tempSettings = mainCanvas.getCanvasSettings();
                                mainCanvas = PaintApp.sjpTabs.get(i).getCanvas();
                                mainCanvas.setCanvasSettings(tempSettings);
                            }
                        }
                    });
                });
        window.getItems().add(newTab);

        //Sets up the HELP section of the MenuBar
        Menu help = new Menu("Help");
            MenuItem releaseNotes = new MenuItem("Release Notes");
            releaseNotes.setOnAction(actionEvent -> {
                try {
                    Desktop desktop = Desktop.getDesktop();
                    desktop.open(new File("resourceFiles/release notes.txt"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            MenuItem about = new MenuItem("About");
            about.setOnAction(actionEvent -> {
                try {
                    Desktop desktop = Desktop.getDesktop();
                    desktop.open(new File("resourceFiles/about.txt"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        help.getItems().addAll(releaseNotes, about);

        super.getMenus().addAll(file, edit, window, help);
    }

    /**
     *
     * @param label a string to be used as the label on the menu item
     * @param keyCombination a string defining the KeyCombination associated with the new item
     * @return a new MenuItem
     */
    private MenuItem createMenuItem(String label, String keyCombination) {
        MenuItem newMenuItem = new MenuItem(label);
        newMenuItem.setAccelerator(KeyCombination.keyCombination(keyCombination));
        return newMenuItem;
    }
}