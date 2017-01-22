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
package fr.noony.games.bowling.hmi.stats.playerstatsscreen;

import fr.noony.games.bowling.analytics.PlayerAnalytics;
import fr.noony.games.bowling.hmi.Screen;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

/**
 *
 * @author hamon
 */
public class PlayerStatsScreen implements Screen {

    private static final Logger LOGGER = Logger.getGlobal();

    private final PropertyChangeSupport propertyChangeSupport;

    private PlayerStatsScreenController controller;
    private Node screenNode;

    public PlayerStatsScreen() {
        propertyChangeSupport = new PropertyChangeSupport(PlayerStatsScreen.this);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("PlayerStatsScreen.fxml"));
            screenNode = loader.load();
            controller = loader.getController();
            controller.addPropertyChangeListener(this::handleHomeScreenActions);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Exception while loading Player Stats Screen fxml file:: {0}", ex);
        }
    }

    private void handleHomeScreenActions(PropertyChangeEvent event) {
        // TODO well
        propertyChangeSupport.firePropertyChange(event.getPropertyName(), event.getOldValue(), event.getNewValue());
    }

    public void setPlayerAnalytics(PlayerAnalytics playerAnalytics) {
        controller.setPlayerAnalytics(playerAnalytics);
    }
    
}
