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
package fr.noony.games.bowling.hmi.edition.scoreview;

import fr.noony.games.bowling.Session;
import fr.noony.games.bowling.hmi.ScreenEvents;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Arnaud HAMON-KEROMEN
 */
public class ScoreViewScreenController implements Initializable {

    private static final Logger LOG = Logger.getGlobal();

    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(ScoreViewScreenController.this);

    @FXML
    private VBox drawingBox;
    @FXML
    private Label sessionLabel;

    private final List<ConfrontationDrawing> confrontationDrawings = new LinkedList<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    protected void onBackAction(ActionEvent event) {
        LOG.log(Level.FINE, "handle onBackAction from screen score view on event {0}", event);
        propertyChangeSupport.firePropertyChange(ScreenEvents.BACK_HOME, null, null);
    }

    protected void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    protected void displaySession(Session session) {
        sessionLabel.setText(session.toString());

        drawingBox.getChildren().removeAll(confrontationDrawings);
        confrontationDrawings.clear();
        session.getConfrontations().forEach(c -> {
            ConfrontationDrawing cD = new ConfrontationDrawing(c);
            confrontationDrawings.add(cD);
            drawingBox.getChildren().add(cD);
            cD.setPrefWidth(1000);
            cD.setPrefHeight(300);
        });

    }

}
