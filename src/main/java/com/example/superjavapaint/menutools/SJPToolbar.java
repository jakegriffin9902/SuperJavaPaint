package com.example.superjavapaint.menutools;

import com.example.superjavapaint.PaintApp;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import java.io.File;

import static com.example.superjavapaint.PaintApp.*;

/**
 * Designed solely for creating a SJPToolbar, an extension of the ToolBar object that, by default, contains all necessary tools
 * SJPToolbar contains a type selector, line width selector, fill and dash options, text edit, a color selector and grabber, and transform tools
 */
public class SJPToolbar extends ToolBar {

    private final ColorPicker colorPicker;
    private double rotationAngle;

    public SJPToolbar() {
        super();

        //Sets up the line width option panel, which uses the passed canvas to set the lineWidth
        ToggleButton thinStroke = createToggleButton("1", "resourceFiles/icons/solid.png", 30);
        ToggleButton mediumStroke = createToggleButton("2", "resourceFiles/icons/medium.png", 30);
        ToggleButton wideStroke = createToggleButton("3", "resourceFiles/icons/wide.png", 30);

        VBox widths = new VBox(thinStroke, mediumStroke, wideStroke);
        widths.setSpacing(1);
            //Sets the actions for width buttons, which change the line width
            thinStroke.setOnAction(actionEvent -> PaintApp.mainCanvas.getCanvasSettings().setLineWidth(1));
            mediumStroke.setOnAction(actionEvent -> PaintApp.mainCanvas.getCanvasSettings().setLineWidth(4));
            wideStroke.setOnAction(actionEvent -> PaintApp.mainCanvas.getCanvasSettings().setLineWidth(8));

        ToggleGroup lineWidthGroup = new ToggleGroup();
        lineWidthGroup.getToggles().addAll(thinStroke, mediumStroke, wideStroke);
        lineWidthGroup.selectToggle(thinStroke);

        ToggleButton freeDraw = createToggleButton("Draw", "resourceFiles/icons/draw.png",
                "Draws on the canvas by dragging the mouse.", 70);
        ToggleButton lineDraw = createToggleButton("Line", "resourceFiles/icons/line.png",
                "Draws a line segment between two mouse click points.", 70);
        ToggleButton eraser = createToggleButton("Erase", "resourceFiles/icons/eraser.png",
                "Erases the canvas by dragging the mouse.", 70);
        ToggleButton square = createToggleButton("Square", "resourceFiles/icons/square.png",
                "Creates a square based on two mouse clicks.", 95);
        ToggleButton rectangle = createToggleButton("Rectangle", "resourceFiles/icons/rectangle.png",
                "Creates a rectangle based on two mouse clicks.", 95);
        ToggleButton roundRect = createToggleButton("Round Rectangle", "resourceFiles/icons/roundRect.png",
                "Creates a rounded rectangle based on two mouse clicks.", 170);
        ToggleButton circle = createToggleButton("Circle", "resourceFiles/icons/circle.png",
                "Creates a circle with bounds generated from two mouse clicks." , 75);
        ToggleButton oval = createToggleButton("Oval", "resourceFiles/icons/oval.png",
                "Creates an oval bounded by two mouse clicks.", 75);
        ToggleButton triangle = createToggleButton("Triangle", "resourceFiles/icons/triangle.png",
                "Creates a triangle with vertices set by mouse clicks.", 85);
        ToggleButton shape = createToggleButton("Shape", "resourceFiles/icons/shape.png",
                "Creates a polygon with vertices set by mouse clicks.", 85);
        ToggleButton eyedropper = createToggleButton("", "resourceFiles/icons/eyedropper.png",
                "Sets the color to the color present where the mouse is clicked.", 25);

        ToggleGroup typeGroup = new ToggleGroup();
        typeGroup.getToggles().addAll(freeDraw, lineDraw, eraser, square, rectangle, circle, oval, triangle, roundRect, shape, eyedropper);

        typeGroup.selectToggle(freeDraw);

        HBox typesR1 = new HBox(freeDraw, square, circle, triangle);
        HBox typesR2 = new HBox(lineDraw, rectangle, oval, shape);
        HBox typesR3 = new HBox(eraser, roundRect);
        VBox types = new VBox(typesR1, typesR2, typesR3);


            //adds the actions to each button to change the type stored in canvas
            freeDraw.setOnAction(actionEvent -> {
                mainCanvas.getCanvasSettings().setType("Draw");
                mainCanvas.updateLiveDraw();
            });
            eraser.setOnAction(actionEvent -> mainCanvas.getCanvasSettings().setType("Erase"));
            lineDraw.setOnAction(actionEvent -> {
                mainCanvas.getCanvasSettings().setType("Line");
                mainCanvas.setVCount(0);
            });
            square.setOnAction(actionEvent -> {
                mainCanvas.getCanvasSettings().setType("Square");
                mainCanvas.setVCount(0);
            });
            rectangle.setOnAction(actionEvent -> {
                mainCanvas.getCanvasSettings().setType("Rectangle");
                mainCanvas.setVCount(0);
            });
            circle.setOnAction(actionEvent -> {
                mainCanvas.getCanvasSettings().setType("Circle");
                mainCanvas.setVCount(0);
            });
            oval.setOnAction(actionEvent -> {
                mainCanvas.getCanvasSettings().setType("Oval");
                mainCanvas.setVCount(0);
            });
            triangle.setOnAction(actionEvent -> {
                mainCanvas.getCanvasSettings().setType("Triangle");
                mainCanvas.setVCount(0);
            });
            shape.setOnAction(actionEvent -> {
                mainCanvas.getCanvasSettings().setType("Shape");
                mainCanvas.setVCount(0);
            });
            roundRect.setOnAction(actionEvent -> {
                mainCanvas.getCanvasSettings().setType("Round Rectangle");
                mainCanvas.setVCount(0);
            });


        //Sets up the colorPicker and Eyedropper in their own HBox
        colorPicker = new ColorPicker(Color.BLACK);
        colorPicker.setOnAction(actionEvent -> mainCanvas.getCanvasSettings().setColor(colorPicker.getValue()));
        colorPicker.setPrefWidth(110);
        colorPicker.setPrefHeight(27);
        eyedropper.setOnAction(actionEvent -> mainCanvas.getCanvasSettings().setType("Eyedropper"));

        HBox colorTools = new HBox(colorPicker, eyedropper);

        //Sets up the lineType Buttons
        VBox lineType = new VBox();
            ToggleButton solid = createToggleButton("Solid", "resourceFiles/icons/solid.png",70);
            ToggleButton dashed = createToggleButton("Dash", "resourceFiles/icons/dashed.png", 70);
            ToggleGroup lineTypeGroup = new ToggleGroup();
            lineTypeGroup.getToggles().addAll(solid, dashed);
            lineTypeGroup.selectToggle(solid);
            lineType.getChildren().addAll(solid, dashed);

            solid.setOnAction(actionEvent -> mainCanvas.getCanvasSettings().setDashed(false));
            dashed.setOnAction(actionEvent -> mainCanvas.getCanvasSettings().setDashed(true));

        VBox fillType = new VBox();
            ToggleButton filled = createToggleButton("Filled", "resourceFiles/icons/filled.png", 75);
            ToggleButton empty = createToggleButton("Empty", "resourceFiles/icons/empty.png", 75);
            ToggleGroup fillGroup = new ToggleGroup();
            fillGroup.getToggles().addAll(filled, empty);
            fillGroup.selectToggle(empty);
            fillType.getChildren().addAll(filled, empty);

        filled.setOnAction(actionEvent -> mainCanvas.getCanvasSettings().setFilled(true));
        empty.setOnAction(actionEvent ->mainCanvas.getCanvasSettings().setFilled(false));

        HBox dashFill = new HBox(lineType, fillType);
        VBox multi = new VBox(colorTools, dashFill);

        TextField textField = mainTextField;
        textField.setPrefWidth(100);
        ToggleButton textButton = new ToggleButton("Text");
        textButton.setPrefWidth(100);
        textButton.setOnAction(actionEvent ->  mainCanvas.getCanvasSettings().setType("Text"));
        typeGroup.getToggles().add(textButton);
        VBox text = new VBox(textField, textButton);


        //Sets up the Cut, Copy, and Paste buttons in the TRANSFORM panel
        ToggleButton cut = createToggleButton("Cut", 50);
        ToggleButton copy = createToggleButton("Copy", 53);
        ToggleButton paste = createToggleButton("Paste", 103);

            cut.setOnAction(actionEvent -> {
                mainCanvas.getCanvasSettings().setType("Cut");
                mainCanvas.setVCount(0);
            });
            copy.setOnAction(actionEvent -> {
                mainCanvas.getCanvasSettings().setType("Copy");
                mainCanvas.setVCount(0);
            });
            paste.setOnAction(actionEvent -> mainCanvas.getCanvasSettings().setType("Paste"));

        Button rotate = new Button("Rotate");
            rotate.setOnAction(actionEvent -> {
                //Rotates the captured image based on the SELECTION ROTATION text box
                ImageView iv = new ImageView(mainCanvas.getCapture());
                if(stampAngle != null) {
                    rotationAngle += Double.parseDouble(stampAngle.getText());
                    iv.setRotate(rotationAngle);
                    SnapshotParameters params = new SnapshotParameters();
                    params.setFill(Color.TRANSPARENT);
                    mainCanvas.setRotatedCapture(iv.snapshot(params, null));
                }
            });
        TextField stampAngle = PaintApp.stampAngle;
        stampAngle.setPrefWidth(50);
        HBox angleBox = new HBox(rotate, stampAngle);
        HBox cutCopy = new HBox(cut, copy);
        VBox transform = new VBox(cutCopy, paste, angleBox);

        typeGroup.getToggles().addAll(cut, copy, paste);

        super.getItems().addAll(widths, types, text, transform, multi);
    }

    public Color getColorPicker() {
        return colorPicker.getValue();
    }
    public void setColorPicker(Color color) {
        colorPicker.setValue(color);
    }
    public void setRotationAngle(double rotationAngle) {this.rotationAngle = rotationAngle;}

    /**
     * Creates a ToggleButton and sets its label, icon, tooltip, and width
     * @param label The label that will appear on the button in the Paint App
     * @param iconPath The path from the content root of the image file used for the icon
     * @param toolTip The text that will appear in the tooltip associated with this ToggleButton
     * @param width An integer specifying how wide the button will be
     * @return A newly constructed ToggleButton
     */
    private ToggleButton createToggleButton(String label, String iconPath, String toolTip, int width) {
        ToggleButton newButton = new ToggleButton(label);
        File iconFile = new File(iconPath);
        newButton.setGraphic(new ImageView(new Image(String.valueOf(iconFile.toURI()))));
        Tooltip newToolTip = new Tooltip(toolTip);
        Tooltip.install(newButton, newToolTip);
        newButton.setPrefWidth(width);
        return newButton;
    }

    /**
     * Creates a ToggleButton and sets its label, icon, and width.
     * @param label The label that will appear on the button in the Paint App
     * @param iconPath The path from the content root of the image file used for the icon
     * @param width An integer specifying how wide the button will be
     * @return A newly constructed ToggleButton
     */
    private ToggleButton createToggleButton(String label, String iconPath, int width) {
        ToggleButton newButton = new ToggleButton(label);
        File iconFile = new File(iconPath);
        newButton.setGraphic(new ImageView(new Image(String.valueOf(iconFile.toURI()))));
        newButton.setPrefWidth(width);
        return newButton;
    }

    /**
     * Creates a ToggleButton and sets its label and width.
     * @param label The label that will appear on the button in the Paint App
     * @param width An integer specifying how wide the button will be
     * @return A newly constructed ToggleButton
     */
    private ToggleButton createToggleButton(String label, int width) {
        ToggleButton newButton = new ToggleButton(label);
        newButton.setPrefWidth(width);
        return newButton;
    }
}