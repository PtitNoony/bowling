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
package com.github.ptitnoony.apps.bowling.hmi.edition.scoreview;

import com.github.ptitnoony.apps.bowling.Session;
import com.github.ptitnoony.apps.bowling.hmi.Screen;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static javafx.application.Platform.runLater;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

/**
 *
 * @author Arnaud HAMON-KEROMEN
 */
public final class SessionScoreViewerScreen implements Screen {

    private static final Logger LOGGER = Logger.getGlobal();

    private final PropertyChangeSupport propertyChangeSupport;

    private ScoreViewScreenController controller;
    private Node screenNode;

    public SessionScoreViewerScreen() {
        propertyChangeSupport = new PropertyChangeSupport(SessionScoreViewerScreen.this);
        loadFXML();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    @Override
    public Node getNavigationNode() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void refresh() {
        // nothing to do
    }

    @Override
    public Node getMainNode() {
        return screenNode;
    }

    public void displaySession(Session session) {
        runLater(() -> controller.displaySession(session));
    }

    private void loadFXML() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ScoreViewScreen.fxml"));
            screenNode = loader.load();
            controller = loader.getController();
            controller.addPropertyChangeListener(this::handleHomeScreenActions);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Exception while loading session score viewer fxml file:: {0}", ex);
        }
    }

    private void handleHomeScreenActions(PropertyChangeEvent event) {
        // TODO well
        propertyChangeSupport.firePropertyChange(event.getPropertyName(), null, null);
    }

}
