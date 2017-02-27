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
package fr.noony.games.bowling.hmi.edition.homescreen;

import fr.noony.games.bowling.Player;
import fr.noony.games.bowling.Session;
import fr.noony.games.bowling.hmi.ScreenController;
import fr.noony.games.bowling.hmi.ScreenEvents;
import fr.noony.games.bowling.hmi.edition.playercreation.PlayerDialogFactory;
import fr.noony.games.bowling.utils.PlayerFactory;
import fr.noony.games.bowling.utils.SessionFactory;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

/**
 * FXML Controller class
 *
 * @author Arnaud HAMON-KEROMEN
 */
public class HomeScreenController implements ScreenController {

    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(HomeScreenController.this);

    @FXML
    private ListView<Player> playerListView;
    @FXML
    private ListView<Session> sessionListView;
    @FXML
    private Button viewSessionB;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        viewSessionB.setDisable(true);
        sessionListView.getSelectionModel().selectedItemProperty().addListener((obs, old, newV) -> {
            //TODO log
            viewSessionB.setDisable(newV == null);
        });
        refresh();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    @FXML
    protected void onCreatePlayerAction(ActionEvent event) {
        //TODO: log event
        PlayerDialogFactory.requestPlayerDialog();
        //TODO: log created player
        refresh();
    }

    @FXML
    protected void openNewSession(ActionEvent event) {
        propertyChangeSupport.firePropertyChange(ScreenEvents.OPEN_SESSION_CREATOR, null, null);
    }

    @FXML
    protected void onViewAction(ActionEvent event) {
        final Session selectedSession = sessionListView.getSelectionModel().getSelectedItem();
        propertyChangeSupport.firePropertyChange(ScreenEvents.OPEN_SESSION_VIEWER, null, selectedSession);
    }

    protected void refresh() {
        sessionListView.getItems().setAll(SessionFactory.getCreatedSessions());
        playerListView.getItems().setAll(PlayerFactory.getCreatedPlayers());
    }

}
