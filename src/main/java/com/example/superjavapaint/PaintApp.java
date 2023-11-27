package com.example.superjavapaint;

import com.example.superjavapaint.menutools.FileControls;
import com.example.superjavapaint.menutools.SJPMenu;
import com.example.superjavapaint.menutools.SJPToolbar;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class PaintApp extends Application {

    public static SJPCanvas mainCanvas;
    public static TextField mainTextField, stampAngle;
    public static SJPToolbar mainToolbar;
    public static SJPMenu mainMenuBar;
    public static File[] currentFile;
    public static LinkedList<SJPTab> sjpTabs;
    public static TabPane tabPane;
    public static TimerTask autoSave;
    public static Timer autoSaveTimer;
    public static int autoSaveMinutes;

    public void start(Stage stage) {

        currentFile = new File[]{null};
        mainTextField = new TextField("Enter Text");
        stampAngle = new TextField("0");
        tabPane = new TabPane();
        sjpTabs = new LinkedList<>();
        autoSaveMinutes = 5;

        //Using this linked list allows me to store and use SJPTabs (the tabPane itself can't access the canvas data)
        sjpTabs.addLast(new SJPTab());

        autoSaveTimer = new Timer();
        autoSave = new TimerTask(){
            @Override
            public void run(){
                Platform.runLater(() -> {
                    for (int i = 0; i < sjpTabs.size(); i++) {
                        try {
                            FileControls.autoSaveImage(sjpTabs.get(i).getCanvas());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        };
        //AutoSave is currently scheduled to occur every 5 minutes, and only on the main canvas.
        //Ideally, functionality would be extended to all canvases.
        autoSaveTimer.schedule(autoSave, 0, 60000L * autoSaveMinutes);

        mainCanvas = sjpTabs.getLast().getCanvas();

        sjpTabs.get(0).setOnSelectionChanged(t -> {
            for (int i = 0; i < PaintApp.sjpTabs.size(); i++) {
                if (PaintApp.sjpTabs.get(i).isSelected()) {
                    SJPCanvasSettings tempSettings = mainCanvas.getCanvasSettings();
                    mainCanvas = PaintApp.sjpTabs.get(i).getCanvas();
                    mainCanvas.setCanvasSettings(tempSettings);
                }
            }
        });

        tabPane.getTabs().add(sjpTabs.get(0));
        mainMenuBar = new SJPMenu(currentFile);

        BorderPane borderPane = new BorderPane();
        Scene scene = new Scene(borderPane, 805, 725);

        //Setup of the TOP, which consists of the SJPMenu and ToolBar
        mainToolbar = new SJPToolbar();
        VBox top = new VBox(mainMenuBar, mainToolbar);
        borderPane.setTop(top);

        //Setup of the CENTER, which contains a tabPane to hold SJPTabs
        borderPane.setCenter(tabPane);

        stage.setTitle("Super Java Paint");
        stage.setScene(scene);
        stage.show();

        //SMART SAVE
        /*
        Checks whether the canvas has been edited since it was last saved
        If so, it opens a warning window to either save or save as, or to exit
        save only appears as an option if a file to be overwritten exists already
         */
        stage.setOnCloseRequest(windowEvent -> {
            if (mainCanvas.getIsSaved()) {
                stage.close();
                System.exit(0);
            }
            else {
                Stage stage1 = new Stage();
                GridPane gridPane = new GridPane();
                gridPane.setHgap(10);
                gridPane.setVgap(10);
                gridPane.setPadding(new Insets(10));
                Scene scene1 = new Scene(gridPane);
                Button saveAs = new Button("Save As...");
                Button exit = new Button("Exit");
                if (currentFile[0] != null) {
                    Button save = new Button("Save");
                    gridPane.add(save, 1, 1);
                    gridPane.add(saveAs, 2, 1);
                    gridPane.add(exit, 3, 1);
                    save.setOnAction(actionEvent -> {
                        FileControls.save(mainCanvas);
                        stage1.close();
                        stage.close();
                    });
                } else {
                    gridPane.add(saveAs, 1, 1);
                    gridPane.add(exit, 2, 1);
                }

                stage1.setTitle("Exit SJP?");
                stage1.setScene(scene1);
                stage1.show();

                saveAs.setOnAction(actionEvent -> {
                    FileControls.saveAs(mainCanvas);
                    stage1.close();
                    stage.close();
                });
                exit.setOnAction(actionEvent -> {
                    stage1.close();
                    stage.close();
                    System.exit(0);
                });
                windowEvent.consume();
            }
        });
    }
    public static void main(String[] args) {
        launch();
    }
}