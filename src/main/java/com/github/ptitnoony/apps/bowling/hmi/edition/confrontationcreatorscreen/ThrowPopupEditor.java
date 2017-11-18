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
package com.github.ptitnoony.apps.bowling.hmi.edition.confrontationcreatorscreen;

import com.github.ptitnoony.apps.bowling.hmi.FxDrawing;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

/**
 *
 * @author Arnaud HAMON-KEROMEN
 */
public class ThrowPopupEditor extends FxDrawing {

    public static final String THROW_VALUE_SELECTED = "throwValueSelected";

    static final double DEFAULT_INTERACTIVE_RADIUS = 100;
    static final double DEFAULT_EXT_RADIUS = 60;
    static final double DEFAULT_INT_RADIUS = 15;

    private final PropertyChangeSupport propertyChangeSupport;

    private final Shape background;
    private final Circle innerCircle;
    private final Shape interactiveCircle;

    private final PopUpEditorItem[] popUpEditorItems;

    private ThrowLabelEditable labelEditable = null;

    public ThrowPopupEditor() {
        super();
        propertyChangeSupport = new PropertyChangeSupport(ThrowPopupEditor.this);
        background = createBackground();
        innerCircle = new Circle(DEFAULT_INT_RADIUS);
        interactiveCircle = createInteractiveArea();
        popUpEditorItems = new PopUpEditorItem[11];
        init();
    }

    //todo protected, constuctor??
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    private Shape createBackground() {
        Circle extC = new Circle(DEFAULT_EXT_RADIUS);
        Circle intC = new Circle(DEFAULT_INT_RADIUS);
        return Shape.subtract(extC, intC);
    }

    private Shape createInteractiveArea() {
        Circle extC = new Circle(DEFAULT_INTERACTIVE_RADIUS);
        Circle intC = new Circle(DEFAULT_INT_RADIUS);
        return Shape.subtract(extC, intC);
    }

    private void init() {
        innerCircle.setOnMouseReleased(this::handleInnerCircleReleased);
        innerCircle.setOpacity(0.1);
        addNode(innerCircle);
        //
        interactiveCircle.setFill(Color.WHITESMOKE);
        interactiveCircle.setOpacity(0.0);
        interactiveCircle.setOnMouseMoved(this::handleMouseMouvmentsOver);
        interactiveCircle.setOnMouseExited(this::handleMouseExited);
        addNode(interactiveCircle);
        //
        background.setStroke(Color.BLACK);
        background.setFill(Color.GREY);
        background.setEffect(new DropShadow());
        addNode(background);
        createPopUpItems();
    }

    private void createPopUpItems() {
        for (int i = 0; i < 11; i++) {
            PopUpEditorItem item = new PopUpEditorItem(i, 90 - (i + 1) * 360 / 11, 360 / 11);
            item.addPropertyChangeListener(this::handleItemEvent);
            addNode(item.getNode());
            popUpEditorItems[i] = item;
        }
    }

    public final void setCenter(double x, double y) {
        setTranslate(x, y);
    }

    @Override
    public void setSize(double sX, double sY) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public final void setThrowModel(ThrowLabelEditable label) {
        labelEditable = label;
        labelEditable.setInEdition();
    }

    private void handleMouseMouvmentsOver(MouseEvent event) {
        double distance = Math.sqrt(event.getX() * event.getX() + event.getY() * event.getY());
        if (distance > DEFAULT_EXT_RADIUS) {
            double opacity = Math.max(0.0, 1 - (distance - DEFAULT_EXT_RADIUS) / (DEFAULT_INTERACTIVE_RADIUS - DEFAULT_EXT_RADIUS));
            getNode().setOpacity(opacity);
        } else {
            getNode().setOpacity(1.0);
        }
    }

    private void handleMouseExited(MouseEvent event) {
        if (!innerCircle.contains(event.getX(), event.getY()) && !background.contains(event.getX(), event.getY())) {
            deactivatePopUp();
        }
    }

    private void deactivatePopUp() {
        setVisible(false);
        getNode().setOpacity(1.0);
        labelEditable.setIdle();
        labelEditable = null;
    }

    private void handleInnerCircleReleased(MouseEvent event) {
        //todo remove?
        Logger.getLogger(ThrowPopupEditor.class.getName()).log(Level.FINE, "Pop up inner circle clicked {0}", event);
        deactivatePopUp();
    }

    private void handleItemEvent(PropertyChangeEvent event) {
        labelEditable.updateValue((int) event.getNewValue());
        deactivatePopUp();
    }

}
