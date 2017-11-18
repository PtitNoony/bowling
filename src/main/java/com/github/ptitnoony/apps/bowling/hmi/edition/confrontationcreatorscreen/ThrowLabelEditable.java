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
import com.github.ptitnoony.apps.bowling.hmi.ThrowLabel;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Arnaud HAMON-KEROMEN
 */
public class ThrowLabelEditable extends ThrowLabel {

    public static final String LABEL_CLICKED = "labelClicked";
    public static final String VALUE_CHANGED = "labelValueChanged";

    private enum INNER_STATE {

        IDLE, DISABLED, SELECTED
    }

    private final PropertyChangeSupport propertyChangeSupport;
    //
    private final Rectangle foreGround = new Rectangle();

//    private boolean isEnabled = true;
    private INNER_STATE myState = INNER_STATE.IDLE;

    public ThrowLabelEditable(String text) {
        super(text);
        propertyChangeSupport = new PropertyChangeSupport(ThrowLabelEditable.this);
        init();
        setPrefSize(FxDrawing.DEFAULT_CELL_WIDTH, FxDrawing.DEFAULT_CELL_HEIGHT);
        setState(myState);
    }
    
    public void addPropertyChangeListener(PropertyChangeListener listener){
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    private void init() {
        foreGround.setOnMousePressed(this::handleLabelClicked);
        Platform.runLater(() -> {
            foreGround.setStroke(Color.BLACK);
            ((Group) getNode()).getChildren().add(foreGround);
        });
    }

    @Override
    public final void setPrefSize(double sX, double sY) {
        super.setPrefSize(sX, sY);
        if (foreGround != null) {
            foreGround.setWidth(sX);
            foreGround.setHeight(sY);
        }
    }


    private void handleLabelClicked(MouseEvent event) {
        if (myState.equals(INNER_STATE.IDLE)) {
            propertyChangeSupport.firePropertyChange(LABEL_CLICKED, event, this);
        }
    }

//    public void setInEdition(boolean inEdition) {
//        if (inEdition) {
//            foreGround.setOpacity(0.5);
//        } else {
//            foreGround.setOpacity(0.0);
//        }
//    }
//
//    public final void setEnabled(boolean enabled) {
//        isEnabled = enabled;
//    }
    public void setIdle() {
        setState(INNER_STATE.IDLE);
    }

    public void setDisabled() {
        setState(INNER_STATE.DISABLED);
    }

    public void setInEdition() {
        setState(INNER_STATE.SELECTED);
    }
    
    public  void updateValue(int i) {
          setText(Integer.toString(i));
          setIdle();
          propertyChangeSupport.firePropertyChange(VALUE_CHANGED, null, i);
    }

    private void setState(INNER_STATE state) {
        myState = state;
        switch (myState) {
            case DISABLED:
                displayAsDisabled();
                break;
            case IDLE:
                displayAsIdle();
                break;
            case SELECTED:
                displayAsSelected();
                break;
            default:
                throw new IllegalStateException("" + state);
        }
    }

    private void displayAsIdle() {
        Platform.runLater(() -> {
            foreGround.setOpacity(0.0);
        });
    }

    private void displayAsSelected() {
        Platform.runLater(() -> {
            foreGround.setFill(Color.ORANGERED);
            foreGround.setOpacity(0.5);
        });
    }

    private void displayAsDisabled() {
        Platform.runLater(() -> {
            foreGround.setFill(Color.DARKGREY);
            foreGround.setOpacity(0.8);
        });
    }
}
