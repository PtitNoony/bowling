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
package fr.noony.games.bowling.hmi.edition.confrontationcreatorscreen;

import fr.noony.games.bowling.hmi.FxDrawing;
import static javafx.application.Platform.runLater;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.TextAlignment;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * @author Arnaud HAMON-KEROMEN
 */
public class PopUpEditorItem extends FxDrawing {

    public static final String ITEM_CLICKED = "popupEditorItemClicked";

    private static final Color OVER_FILL_COLOR = Color.LIMEGREEN;
    private static final Color DEFAULT_FILL_COLOR = Color.GREY;

    private static final double DEFAULT_LABEL_SIZE = 8.0;

    private final PropertyChangeSupport propertyChangeSupport;

    private Shape backGround;
    private Shape foreGround;
    private final Label label;
    private double startAngle;
    private double angle;
    private final int myValue;

    public PopUpEditorItem(int value, double startAng, double length) {
        super();
        propertyChangeSupport = new PropertyChangeSupport(PopUpEditorItem.this);
        startAngle = startAng;
        angle = length;
        myValue = value;
        label = new Label(Integer.toString(myValue));
        init();
    }

    public final void setAngles(double start, double length) {
        startAngle = start;
        angle = length;
        removeNode(backGround);
        removeNode(foreGround);
        createShapes();
    }

    private void createShapes() {
        backGround = createArc();
        backGround.setStroke(Color.BLACK);
        backGround.setFill(Color.GREY);
        updateLabelPosition();
        backGround.toBack();
        foreGround = createArc();
        foreGround.setOpacity(0.0);
        setUpInteractivity();
        displayAsIdle();
    }

    private void init() {
        label.setTextFill(Color.BLACK);
        label.setTextAlignment(TextAlignment.CENTER);
        createShapes();
        addNode(backGround);
        addNode(label);
        addNode(foreGround);
    }

    private Shape createArc() {
        Arc tmp = new Arc(0.0, 0.0, ThrowPopupEditor.DEFAULT_EXT_RADIUS, ThrowPopupEditor.DEFAULT_EXT_RADIUS, startAngle, angle);
        tmp.setType(ArcType.ROUND);
        Circle innerCircle = new Circle(ThrowPopupEditor.DEFAULT_INT_RADIUS);
        return Shape.subtract(tmp, innerCircle);
    }

    private void setUpInteractivity() {
        foreGround.setOnMouseClicked(this::handleMouseClicked);
        foreGround.setOnMouseEntered(this::handleMouseEntered);
        foreGround.setOnMouseExited(this::handleMouseExited);
    }

    private void updateLabelPosition() {
        double x = Math.cos(Math.toRadians(startAngle + angle / 2.0)) * ((ThrowPopupEditor.DEFAULT_EXT_RADIUS - ThrowPopupEditor.DEFAULT_INT_RADIUS) / 2.0 + ThrowPopupEditor.DEFAULT_INT_RADIUS);
        double y = -Math.sin(Math.toRadians(startAngle + angle / 2.0)) * ((ThrowPopupEditor.DEFAULT_EXT_RADIUS - ThrowPopupEditor.DEFAULT_INT_RADIUS) / 2.0 + ThrowPopupEditor.DEFAULT_INT_RADIUS);
        x -= DEFAULT_LABEL_SIZE;
        y -= DEFAULT_LABEL_SIZE;
        label.setTranslateX(x);
        label.setTranslateY(y);
        label.setPrefSize(2 * DEFAULT_LABEL_SIZE, 2 * DEFAULT_LABEL_SIZE);
    }

    @Override
    public void setSize(double sX, double sY) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    protected void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    private void handleMouseClicked(MouseEvent event) {
        //todo
        propertyChangeSupport.firePropertyChange(ITEM_CLICKED, null, myValue);
    }

    private void handleMouseEntered(MouseEvent event) {
        //todo
        displayAsOver();
    }

    private void handleMouseExited(MouseEvent event) {
        //todo
        displayAsIdle();
    }

    private void displayAsOver() {
        runLater(() -> backGround.setFill(OVER_FILL_COLOR));
    }

    private void displayAsIdle() {
        runLater(() -> backGround.setFill(DEFAULT_FILL_COLOR));
    }

}
