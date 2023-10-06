package com.example.superjavapaint.menutools;

import com.example.superjavapaint.PaintApp;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import java.io.File;

/**
 * Designed solely for creating a SJPToolbar, an extension of the ToolBar object that, by default, contains all necessary tools
 * SJPToolbar contains a type selector, line width selector, fill and dash options, text edit, a color selector and grabber, and transform tools
 */
public class SJPToolbar extends ToolBar {

    //TOGGLE BUTTONS
    private ColorPicker colorPicker;

    //takes the canvas as an argument to pass to editor methods
    public SJPToolbar() {
        super();

        //Sets up the line width option panel, which uses the passed canvas to set the lineWidth
        ToggleButton thin = new ToggleButton("1");
            File thinLineFile = new File("resourceFiles/icons/solid.png");
            thin.setGraphic(new ImageView(new Image(String.valueOf(thinLineFile.toURI()))));
        ToggleButton medium = new ToggleButton("2");
            File mediumLineFile = new File("resourceFiles/icons/medium.png");
            medium.setGraphic(new ImageView(new Image(String.valueOf(mediumLineFile.toURI()))));
        ToggleButton wide = new ToggleButton("3");
            File wideLineFile = new File("resourceFiles/icons/wide.png");
            wide.setGraphic(new ImageView(new Image(String.valueOf(wideLineFile.toURI()))));

        ToggleGroup lineWidthGroup = new ToggleGroup();
        lineWidthGroup.getToggles().addAll(thin, medium, wide);
        lineWidthGroup.selectToggle(thin);

        VBox widths = new VBox(thin, medium, wide);
        widths.setSpacing(1);
            //sets the actions for all buttons
            thin.setOnAction(actionEvent -> PaintApp.mainCanvas.getCanvasSettings().setLineWidth(1));
            medium.setOnAction(actionEvent -> PaintApp.mainCanvas.getCanvasSettings().setLineWidth(4));
            wide.setOnAction(actionEvent -> PaintApp.mainCanvas.getCanvasSettings().setLineWidth(8));

        //Sets up the grid for the type selector, which will control the canvas String type
        //Eyedropper tool is created here so that it can be added to the color group, but all other actions occur within the color HBox
        ToggleButton freeDraw = new ToggleButton("Draw");
            freeDraw.setPrefWidth(70);
        ToggleButton lineDraw = new ToggleButton("Line");
            lineDraw.setPrefWidth(70);
        ToggleButton eraser = new ToggleButton("Erase");
            eraser.setPrefWidth(70);
        ToggleButton square = new ToggleButton("Square");
            square.setPrefWidth(95);
        ToggleButton rectangle = new ToggleButton("Rectangle");
            rectangle.setPrefWidth(95);
        ToggleButton roundRect = new ToggleButton("Round Rectangle");
            roundRect.setPrefWidth(170);
        ToggleButton circle = new ToggleButton("Circle");
            circle.setPrefWidth(75);
        ToggleButton oval = new ToggleButton("Oval");
            oval.setPrefWidth(75);
        ToggleButton triangle = new ToggleButton("Triangle");
            triangle.setPrefWidth(85);
        ToggleButton shape = new ToggleButton("Shape");
            shape.setPrefWidth(85);
        ToggleButton eyedropper = new ToggleButton();

        ToggleGroup typeGroup = new ToggleGroup();
        typeGroup.getToggles().addAll(freeDraw, lineDraw, eraser, square, rectangle, circle, oval, triangle, roundRect, shape, eyedropper);
        typeGroup.selectToggle(freeDraw);

        HBox typesR1 = new HBox(freeDraw, square, circle, triangle);
        HBox typesR2 = new HBox(lineDraw, rectangle, oval, shape);
        HBox typesR3 = new HBox(eraser, roundRect);
        VBox types = new VBox(typesR1, typesR2, typesR3);

            //adds images to the buttons
            File freeDrawFile = new File("resourceFiles/icons/draw.png");
                freeDraw.setGraphic(new ImageView(new Image(String.valueOf(freeDrawFile.toURI()))));
            File lineDrawFile = new File("resourceFiles/icons/line.png");
                lineDraw.setGraphic(new ImageView(new Image(String.valueOf(lineDrawFile.toURI()))));
            File squareDrawFile = new File("resourceFiles/icons/square.png");
                square.setGraphic(new ImageView(new Image(String.valueOf(squareDrawFile.toURI()))));
            File rectangleDrawFile = new File("resourceFiles/icons/rectangle.png");
                rectangle.setGraphic(new ImageView(new Image(String.valueOf(rectangleDrawFile.toURI()))));
            File circleDrawFile = new File("resourceFiles/icons/circle.png");
                circle.setGraphic(new ImageView(new Image(String.valueOf(circleDrawFile.toURI()))));
            File ovalDrawFile = new File("resourceFiles/icons/oval.png");
                oval.setGraphic(new ImageView(new Image(String.valueOf(ovalDrawFile.toURI()))));
            File triangleDrawFile = new File("resourceFiles/icons/triangle.png");
                triangle.setGraphic(new ImageView(new Image(String.valueOf(triangleDrawFile.toURI()))));
            File roundRectFile = new File("resourceFiles/icons/roundRect.png");
                roundRect.setGraphic(new ImageView(new Image(String.valueOf(roundRectFile.toURI()))));
            File shapeDrawFile = new File("resourceFiles/icons/shape.png");
                shape.setGraphic(new ImageView(new Image(String.valueOf(shapeDrawFile.toURI()))));
            File eraserFile = new File("resourceFiles/icons/eraser.png");
                eraser.setGraphic(new ImageView(new Image(String.valueOf(eraserFile.toURI()))));

            //adds the actions to each button to change the type stored in canvas
            freeDraw.setOnAction(actionEvent -> {
                PaintApp.mainCanvas.getCanvasSettings().setType("Draw");
                EditControls.updateLiveDraw(PaintApp.mainCanvas);
            });
            eraser.setOnAction(actionEvent -> PaintApp.mainCanvas.getCanvasSettings().setType("Erase"));
            lineDraw.setOnAction(actionEvent -> {
                PaintApp.mainCanvas.getCanvasSettings().setType("Line");
                PaintApp.mainCanvas.setvCount(0);
            });
            square.setOnAction(actionEvent -> {
                PaintApp.mainCanvas.getCanvasSettings().setType("Square");
                PaintApp.mainCanvas.setvCount(0);
            });
            rectangle.setOnAction(actionEvent -> {
                PaintApp.mainCanvas.getCanvasSettings().setType("Rectangle");
                PaintApp.mainCanvas.setvCount(0);
            });
            circle.setOnAction(actionEvent -> {
                PaintApp.mainCanvas.getCanvasSettings().setType("Circle");
                PaintApp.mainCanvas.setvCount(0);
            });
            oval.setOnAction(actionEvent -> {
                PaintApp.mainCanvas.getCanvasSettings().setType("Ellipse");
                PaintApp.mainCanvas.setvCount(0);
            });
            triangle.setOnAction(actionEvent -> {
                PaintApp.mainCanvas.getCanvasSettings().setType("Triangle");
                PaintApp.mainCanvas.setvCount(0);
            });
            shape.setOnAction(actionEvent -> {
                PaintApp.mainCanvas.getCanvasSettings().setType("Shape");
                PaintApp.mainCanvas.setvCount(0);
            });
            roundRect.setOnAction(actionEvent -> {
                PaintApp.mainCanvas.getCanvasSettings().setType("RoundRect");
                PaintApp.mainCanvas.setvCount(0);
            });


        //Sets up the colorPicker and Eyedropper in their own HBox
        colorPicker = new ColorPicker(Color.BLACK);
        colorPicker.setOnAction(actionEvent -> PaintApp.mainCanvas.getCanvasSettings().setColor(colorPicker.getValue()));
        colorPicker.setPrefWidth(107);
        colorPicker.setPrefHeight(27);
        eyedropper.setOnAction(actionEvent -> {
            PaintApp.mainCanvas.getCanvasSettings().setType("Eyedropper");
        });
        File eyedropperFile = new File("resourceFiles/icons/eyedropper.png");
        eyedropper.setGraphic(new ImageView(new Image(String.valueOf(eyedropperFile.toURI()))));

        HBox colorTools = new HBox(colorPicker, eyedropper);

        //Sets up the lineType Buttons
        VBox lineType = new VBox();
            ToggleButton solid = new ToggleButton("Solid");
            ToggleButton dashed = new ToggleButton("Dash");
            ToggleGroup lineTypeGroup = new ToggleGroup();
            lineTypeGroup.getToggles().addAll(solid, dashed);
            lineTypeGroup.selectToggle(solid);
            File solidLineFile = new File("resourceFiles/icons/solid.png");
            solid.setGraphic(new ImageView(new Image(String.valueOf(solidLineFile.toURI()))));
            File dashedLineFile = new File("resourceFiles/icons/dashed.png");
            dashed.setGraphic(new ImageView(new Image(String.valueOf(dashedLineFile.toURI()))));
            lineType.getChildren().addAll(solid, dashed);

            solid.setOnAction(actionEvent -> PaintApp.mainCanvas.getCanvasSettings().setDashed(false));
            dashed.setOnAction(actionEvent -> PaintApp.mainCanvas.getCanvasSettings().setDashed(true));

        VBox fillType = new VBox();
            ToggleButton filled = new ToggleButton("Filled  ");
            ToggleButton empty = new ToggleButton("Empty");
            ToggleGroup fillGroup = new ToggleGroup();
            fillGroup.getToggles().addAll(filled, empty);
            fillGroup.selectToggle(empty);
            File filledFile = new File("resourceFiles/icons/filled.png");
            filled.setGraphic(new ImageView(new Image(String.valueOf(filledFile.toURI()))));
            File emptyFile = new File("resourceFiles/icons/empty.png");
            empty.setGraphic(new ImageView(new Image(String.valueOf(emptyFile.toURI()))));
            fillType.getChildren().addAll(filled, empty);

        filled.setOnAction(actionEvent -> PaintApp.mainCanvas.getCanvasSettings().setFilled(true));
        empty.setOnAction(actionEvent -> PaintApp.mainCanvas.getCanvasSettings().setFilled(false));

        HBox dashFill = new HBox(lineType, fillType);
        VBox multi = new VBox(colorTools, dashFill);

        //Builds the text input box and text ToggleButton
        TextField textField = PaintApp.textField;
        textField.setPrefWidth(100);
        ToggleButton textButton = new ToggleButton("Text");
        textButton.setPrefWidth(100);
        textButton.setOnAction(actionEvent ->  PaintApp.mainCanvas.getCanvasSettings().setType("Text"));
        typeGroup.getToggles().add(textButton);
        VBox text = new VBox(textField, textButton);


        //Sets up the Select and Stamp buttons in the TRANSFORM panel
        ToggleButton select = new ToggleButton("Select");
            select.setOnAction(actionEvent -> {
                PaintApp.mainCanvas.getCanvasSettings().setType("Select");
                PaintApp.mainCanvas.setvCount(0);
            });
            select.setPrefWidth(70);
        ToggleButton stamp = new ToggleButton("Stamp");
            stamp.setOnAction(actionEvent -> {
                PaintApp.mainCanvas.getCanvasSettings().setType("Stamp");
            });
            stamp.setPrefWidth(70);
        VBox transform = new VBox(select, stamp);

        typeGroup.getToggles().addAll(select, stamp);

        super.getItems().addAll(widths, types, text, transform, multi);
    }

    public Color getColorPicker() {
        return colorPicker.getValue();
    }

    public void setColorPicker(Color color) {
        colorPicker.setValue(color);
    }
}
