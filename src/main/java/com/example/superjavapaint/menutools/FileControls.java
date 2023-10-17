package com.example.superjavapaint.menutools;

import com.example.superjavapaint.SJPCanvas;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;

import static com.example.superjavapaint.PaintApp.currentFile;
import static com.example.superjavapaint.PaintApp.mainCanvas;

/**
 * Contains static methods called by the "File" MenuItems in the SJPMenuBar, as well as similar methods that run in the background.
 * These methods handle the opening and saving of files, as well as resetting the canvas
 * Resetting an unsaved canvas will open a warning window to prompt the user to save, again calling the save methods.
 */

public class FileControls {

    public static File open(SJPCanvas canvas) {
        File file = new FileChooser().showOpenDialog(null);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        if (file != null) {
            Image image = new Image(file.toURI().toString());
            canvas.setWidth(image.getWidth());
            canvas.setHeight(image.getHeight());
            graphicsContext.drawImage(image, 0, 0);
        }
        canvas.setIsSaved(true);
        canvas.updateCanvas();
        return file;
    }

    //overwrites the last opened image with a snapshot of the canvas
    //takes as arguments the canvas to be captured and the file destination
    public static void save(SJPCanvas canvas, File file) {
        Image image = canvas.getRegion(0, 0, canvas.getWidth(), canvas.getHeight());
        BufferedImage bufferedImage = new BufferedImage((int) image.getWidth(), (int) image.getHeight(), BufferedImage.TYPE_INT_RGB);

        try {
            String name = file.getName();
            //collects the final characters of the filename to determine the type
            String extension = name.substring(1+name.lastIndexOf(".")).toLowerCase();
            ImageIO.write(SwingFXUtils.fromFXImage(image, bufferedImage), extension, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        canvas.setIsSaved(true);
    }

    //Uses FileChooser to select a save directory for a canvas snapshot, and saves as one of 3 file types
    //Takes as an argument the canvas to be saved
    public static void saveAs(SJPCanvas canvas) {
        Image image = canvas.getRegion(0, 0, canvas.getWidth(), canvas.getHeight());
        BufferedImage bufferedImage = new BufferedImage((int) image.getWidth(), (int) image.getHeight(), BufferedImage.TYPE_INT_RGB);

        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter jpeg = new FileChooser.ExtensionFilter("JPEG", "*.jpg");
        FileChooser.ExtensionFilter png = new FileChooser.ExtensionFilter("PNG", "*.png");
        FileChooser.ExtensionFilter bmp = new FileChooser.ExtensionFilter("BMP", "*.bmp");
        fileChooser.getExtensionFilters().addAll(jpeg, png, bmp);

        File file = fileChooser.showSaveDialog(null);
        String type = fileChooser.getSelectedExtensionFilter().getDescription();

        //saves a new file of the type selected by the FileChooser
        try {
            if (type.equals("JPEG")) {
                ImageIO.write(SwingFXUtils.fromFXImage(image, bufferedImage), "jpg", file);
            }
            else if (type.equals("PNG")) {
                ImageIO.write(SwingFXUtils.fromFXImage(image, bufferedImage), "png", file);
            }
            else { //bmp
                ImageIO.write(SwingFXUtils.fromFXImage(image, bufferedImage), "bmp", file);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        canvas.setIsSaved(true);
    }

    public static void newCanvas(SJPCanvas canvas, double width, double height) {
        if (!canvas.getIsSaved()) {
            //Creates a new window prompting the user to save or discard their current canvas
            //Should this instead just open a new tab, and keep the old canvas intact?
            Stage stage1 = new Stage();
            stage1.setWidth(200);
            GridPane gridPane = new GridPane();
            gridPane.setHgap(10);
            gridPane.setVgap(10);
            gridPane.setPadding(new Insets(10));
            Scene scene1 = new Scene(gridPane);
            Button saveAs = new Button("Save As...");
            Button exit = new Button("Discard");
            if (currentFile[0] != null) {
                Button save = new Button("Save");
                gridPane.add(save, 1, 1);
                gridPane.add(saveAs, 2, 1);
                gridPane.add(exit, 3, 1);
                save.setOnAction(actionEvent -> {
                    FileControls.save(mainCanvas, currentFile[0]);
                    stage1.close();
                });
            } else {
                gridPane.add(saveAs, 1, 1);
                gridPane.add(exit, 2, 1);
            }

            stage1.setTitle("Reset Canvas?");
            stage1.setScene(scene1);
            stage1.show();

            saveAs.setOnAction(actionEvent -> {
                FileControls.saveAs(mainCanvas);
                stage1.close();
                canvas.setHeight(height);
                canvas.setWidth(width);
                GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
                graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                canvas.setIsSaved(true);
            });
            exit.setOnAction(actionEvent -> {
                stage1.close();
                canvas.setHeight(height);
                canvas.setWidth(width);
                GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
                graphicsContext.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                graphicsContext.drawImage(canvas.getRegion(0, 0, canvas.getWidth(), canvas.getHeight()), 0, 0);
                canvas.setIsSaved(true);
            });
        }
    }

    //takes the current canvas and resizes based on user inputs, then clears the entire canvas
    //Creates a stage to grab width and height, which closes once the "new Canvas" is made
    //This SHOULD, but does not, clear the stored file
    public static void newCanvasPrompter(SJPCanvas canvas) throws IOException {
        Stage stage = new Stage();
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));
        Scene scene = new Scene(gridPane);
        Label widthLabel = new Label("Width");
        Label heightLabel = new Label("Height");
        TextField width = new TextField();
        TextField height = new TextField();
        Button create = new Button("Create New Canvas");

        gridPane.add(widthLabel, 1, 1);
        gridPane.add(heightLabel, 1, 2);
        gridPane.add(width, 2, 1);
        gridPane.add(height, 2, 2);
        gridPane.add(create, 2, 3);

        stage.setTitle("New Canvas");
        stage.setScene(scene);
        stage.show();

        create.setOnAction(actionEvent -> {
            newCanvas(canvas, Double.parseDouble(width.getText()), Double.parseDouble(height.getText()));
            stage.close();
        });
    }

    public static void autoSaveImage(SJPCanvas canvas) throws IOException {
        if(!canvas.getIsSaved()) {
            Image autoSaveBackup = canvas.getRegion(0, 0, canvas.getWidth(), canvas.getHeight()); //snapshot of the current canvas
            File backupFile = new File("resourceFiles/autoSaves/" + LocalDate.now() + " " + Instant.now() + ".png");
            backupFile.createNewFile();
            ImageIO.write(SwingFXUtils.fromFXImage(autoSaveBackup, null), "png", new FileOutputStream(backupFile));
        }
    }
}
