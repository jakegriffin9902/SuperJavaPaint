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

/**
 * Contains static methods called by the "File" MenuItems in the SJPMenuBar, as well as similar methods that run in the background.
 * These methods handle the opening and saving of files, as well as resetting the canvas.
 * Resetting an unsaved canvas will open a warning window to prompt the user to save, again calling the save methods.
 */
public class FileControls {

    /**
     * Opens a new file using the FileChooser, draws the image on a canvas, stores the file in the canvas, and assigns
     * the file to the currentFile in PaintApp.
     * @param canvas the canvas on which to place the opened file.
     * @return the opened file, to be stored in PaintApp's currentFile
     */
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
        canvas.getUndoRedo().updateStacks(canvas);
        canvas.setStoredFile(file);
        return file;
    }

    /**
     * Takes a snapshot of a canvas and saves it to the file located in the canvas's storedFile.
     * @param canvas the canvas from which the saved image will be taken.
     */
    public static void save(SJPCanvas canvas) {
        if (canvas.getStoredFile() != null) {
            Image image = canvas.getRegion(0, 0, canvas.getWidth(), canvas.getHeight());
            BufferedImage bufferedImage = new BufferedImage((int) image.getWidth(), (int) image.getHeight(), BufferedImage.TYPE_INT_RGB);
            try {
                String name = canvas.getStoredFile().getName();

                // Collects the final characters of the filename to determine the type
                String extension = name.substring(1+name.lastIndexOf(".")).toLowerCase();
                ImageIO.write(SwingFXUtils.fromFXImage(image, bufferedImage), extension, canvas.getStoredFile());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            canvas.setIsSaved(true);
        }
    }

    /**
     * Uses a FileChooser to create a save directory for a canvas snapshot, and assigns the file to the canvas's
     * storedFile and to the PaintApp's currentFile.
     * @param canvas the canvas from which the saved image will be taken.
     * @return a file to be stored in the PaintApp's currentFile
     */
    public static File saveAs(SJPCanvas canvas) {
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
        canvas.setStoredFile(file);
        canvas.setIsSaved(true);
        return file;
    }

    /**
     * "Replaces" the passed canvas with a "new" one by resizing the old canvas and clearing its contents.
     * @param canvas the canvas to be replaced with a new one
     * @param width a double used to set the width of the new canvas
     * @param height a double used to set the height of the new canvas
     */
    public static void saveAndResetCanvas(SJPCanvas canvas, double width, double height) {
        if (!canvas.getIsSaved()) {
            //Creates a new window prompting the user to save or discard their current canvas
            Stage saveWarning = new Stage();
            saveWarning.setWidth(300);
            GridPane gridPane = new GridPane();
            gridPane.setHgap(10);
            gridPane.setVgap(10);
            gridPane.setPadding(new Insets(10));
            Scene scene1 = new Scene(gridPane);

            Button saveAsButton = new Button("Save As...");
            saveAsButton.setOnAction(actionEvent -> {
                saveWarning.close();
                saveAs(canvas);
                resetCanvas(canvas, width, height);
            });

            Button discardButton = new Button("Discard");
            discardButton.setOnAction(actionEvent -> {
                saveWarning.close();
                resetCanvas(canvas, width, height);
            });

            if (currentFile[0] != null) {
                Button saveButton = new Button("Save");
                gridPane.add(saveButton, 1, 1);
                gridPane.add(saveAsButton, 2, 1);
                gridPane.add(discardButton, 3, 1);
                saveButton.setOnAction(actionEvent -> {
                    saveWarning.close();
                    save(canvas);
                    resetCanvas(canvas, width, height);
                });
            } else {
                gridPane.add(saveAsButton, 1, 1);
                gridPane.add(discardButton, 2, 1);
            }

            saveWarning.setTitle("Reset Canvas?");
            saveWarning.setScene(scene1);
            saveWarning.show();

        }
        else {resetCanvas(canvas, width, height);}
    }

    /**
     * Creates a window that allows the user to input a width and height. Then passes those values to newCanvas.
     * @param canvas the canvas to be replaced
     * @throws IOException
     */
    public static void newCanvasPrompter(SJPCanvas canvas) throws IOException {
        Stage stage = new Stage();
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10));

        // Creates all elements of the popup window
        Scene scene = new Scene(gridPane);
        Label widthLabel = new Label("Width");
        Label heightLabel = new Label("Height");
        TextField width = new TextField();
        TextField height = new TextField();
        Button create = new Button("Create New Canvas");

        // Adds all elements to the window
        gridPane.add(widthLabel, 1, 1);
        gridPane.add(heightLabel, 1, 2);
        gridPane.add(width, 2, 1);
        gridPane.add(height, 2, 2);
        gridPane.add(create, 2, 3);

        stage.setTitle("New Canvas");
        stage.setScene(scene);
        stage.show();

        create.setOnAction(actionEvent -> {
            saveAndResetCanvas(canvas, Double.parseDouble(width.getText()), Double.parseDouble(height.getText()));
            stage.close();
        });
    }

    /**
     * Takes a snapshot of the passed canvas and stores it as a new file in the designated autoSave folder.
     * This does NOT change the save state of the canvas, but it does use the save state to determine whether the
     * autosave is necessary.
     * @param canvas the canvas to be saved.
     * @throws IOException
     */
    public static void autoSaveImage(SJPCanvas canvas) throws IOException {
        if(!canvas.getIsSaved()) {
            Image autoSaveBackup = canvas.getRegion(0, 0, canvas.getWidth(), canvas.getHeight()); //snapshot of the current canvas
            File backupFile = new File("resourceFiles/autoSaves/" + LocalDate.now() + " " + Instant.now() + ".png");
            backupFile.createNewFile();
            ImageIO.write(SwingFXUtils.fromFXImage(autoSaveBackup, null), "png", new FileOutputStream(backupFile));
        }
    }

    /**
     * Clears a canvas and resizes it based on passed width and height doubles.
     * @param canvas the canvas to be reset
     * @param width the width to make the new canvas
     * @param height the height to make the new canvas
     */
    public static void resetCanvas(SJPCanvas canvas, double width, double height) {
        canvas.setHeight(height);
        canvas.setWidth(width);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.clearRect(0, 0, width, height);
        canvas.getUndoRedo().updateStacks(canvas);
        canvas.updateLiveDraw();
        canvas.setIsSaved(true);
    }
}