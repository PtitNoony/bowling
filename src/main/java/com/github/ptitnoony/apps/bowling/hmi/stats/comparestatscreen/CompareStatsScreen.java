/*
 * Copyright (C) 2017 Arnaud HAMON-KEROMEN
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
package com.github.ptitnoony.apps.bowling.hmi.stats.comparestatscreen;

import com.github.ptitnoony.apps.bowling.analytics.ComparisonMode;
import com.github.ptitnoony.apps.bowling.analytics.PlayerAnalytics;
import com.github.ptitnoony.apps.bowling.hmi.Screen;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

/**
 *
 * @author hamon
 */
public class CompareStatsScreen implements Screen {

    private static final Logger LOGGER = Logger.getGlobal();

    private final PropertyChangeSupport propertyChangeSupport;

    private CompareStatsScreenController controller;
    private Node screenNode;

    public CompareStatsScreen() {
        propertyChangeSupport = new PropertyChangeSupport(CompareStatsScreen.this);
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
    public Node getMainNode() {
        return screenNode;
    }

    @Override
    public void refresh() {
        controller.refresh();
    }

    private void loadFXML() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CompareStatsScreen.fxml"));
            screenNode = loader.load();
            controller = loader.getController();
            controller.addPropertyChangeListener(this::handleHomeScreenActions);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Exception while loading Compare Stats Screen fxml file:: {0}", ex);
        }
    }

    private void handleHomeScreenActions(PropertyChangeEvent event) {
        // TODO well
        propertyChangeSupport.firePropertyChange(event.getPropertyName(), event.getOldValue(), event.getNewValue());
    }

    public void setPlayersAnalytics(ComparisonMode mode, List<PlayerAnalytics> playerAnalyticses) {
        controller.setPlayersAnalytics(mode, playerAnalyticses);
    }

}
