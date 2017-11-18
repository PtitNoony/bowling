/*
 * Copyright (C) 2016 Arnaud HAMON-KEROMEN
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.ptitnoony.apps.bowling.hmi;

import com.github.ptitnoony.apps.bowling.BallType;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 *
 * @author Arnaud HAMON-KEROMEN
 */
public class ThrowLabel {

    protected static final double INNER_MARGIN = 2.5;
    protected static final Font DEFAULT_FONT = Font.font("Verdana", FontWeight.NORMAL, 12);
    protected static final Font BOLD_FONT = Font.font("Verdana", FontWeight.BOLD, 12);
    protected static final Color SPLIT_COLOR = Color.ORANGERED.desaturate();
    protected static final Color SPARE_COLOR = Color.AQUA.desaturate();
    protected static final Color STRIKE_COLOR = Color.CHARTREUSE.desaturate();

    private final Group mainNode;
    private final Rectangle background;
    private final Label label;
    private final Circle circle;
    private final Rectangle square;

    public ThrowLabel(String text) {
        mainNode = new Group();
        background = new Rectangle();
        circle = new Circle();
        square = new Rectangle();
        label = new Label(text);
        init();
    }

    private void init() {
        Platform.runLater(() -> {
            background.setStroke(Color.BLACK);
            background.setStrokeWidth(0.5);
            background.setFill(Color.GHOSTWHITE);
            circle.setFill(SPLIT_COLOR);
            circle.setStroke(Color.BLACK);
            circle.setStrokeWidth(0.5);
            circle.setVisible(false);
            square.setFill(Color.DEEPPINK);
            square.setStroke(Color.BLACK);
            square.setStrokeWidth(0.5);
            square.setVisible(false);
            label.setAlignment(Pos.CENTER);
            label.setFont(DEFAULT_FONT);
            mainNode.getChildren().add(background);
            mainNode.getChildren().add(circle);
            mainNode.getChildren().add(square);
            mainNode.getChildren().add(label);
        });
        setPrefSize(FxDrawing.DEFAULT_CELL_WIDTH, FxDrawing.DEFAULT_CELL_HEIGHT);
    }

    public void setPrefSize(double sX, double sY) {
        double innerRadius = (Math.min(sX, sY) - 2.0 * INNER_MARGIN) / 2.0;
        double innerSquare = Math.min(sX, sY) - 2.5 * INNER_MARGIN;
        background.setWidth(sX);
        background.setHeight(sY);
        circle.setRadius(innerRadius);
        circle.setCenterX(sX / 2.0);
        circle.setCenterY(sY / 2.0);
        square.setWidth(innerSquare);
        square.setHeight(innerSquare);
        square.setX((sX - innerSquare) / 2.0);
        square.setY((sY - innerSquare) / 2.0);
        label.setMinSize(sX, sY);
        label.setMaxSize(sX, sY);
        label.setPrefSize(sX, sY);
    }

    public void setText(String text) {
        label.setText(text);
    }

    public Node getNode() {
        return mainNode;
    }

    public final void setTranslateX(double x) {
        Platform.runLater(() -> mainNode.setTranslateX(x));
    }

    public final void setTranslateY(double y) {
        Platform.runLater(() -> mainNode.setTranslateY(y));
    }

    public final void setThrowType(BallType type) {
        //TODO: check with label
        switch (type) {
            case SPLIT:
                displayAsSplit();
                break;
            case SPARE:
                displayAsSpare();
                break;
            case STRIKE:
                displayAsStrike();
                break;
            default:
                displayAsSimple();
                break;
        }
    }

    private void displayAsSimple() {
        Platform.runLater(() -> {
            circle.setVisible(false);
            square.setVisible(false);
            label.setFont(DEFAULT_FONT);
        });
    }

    private void displayAsSplit() {
        Platform.runLater(() -> {
            circle.setVisible(true);
            label.setFont(BOLD_FONT);
            square.setVisible(false);
        });
    }

    private void displayAsSpare() {
        Platform.runLater(() -> {
            square.setVisible(true);
            label.setFont(BOLD_FONT);
            square.setFill(SPARE_COLOR);
            circle.setVisible(false);
        });
    }

    private void displayAsStrike() {
        Platform.runLater(() -> {
            square.setVisible(true);
            label.setFont(BOLD_FONT);
            square.setFill(STRIKE_COLOR);
            circle.setVisible(false);
        });
    }

    public final void setFill(Color color) {
        Platform.runLater(() -> background.setFill(color));
    }

    public final void setFont(Font font) {
        Platform.runLater(() -> label.setFont(font));
    }

}
