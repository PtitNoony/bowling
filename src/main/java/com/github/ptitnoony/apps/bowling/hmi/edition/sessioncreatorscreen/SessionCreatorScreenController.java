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
package com.github.ptitnoony.apps.bowling.hmi.edition.sessioncreatorscreen;

import com.github.ptitnoony.gameutils.Player;
import com.github.ptitnoony.gameutils.PlayerFactory;
import com.github.ptitnoony.apps.bowling.Confrontation;
import com.github.ptitnoony.apps.bowling.Session;
import com.github.ptitnoony.apps.bowling.EditablePlayerRound;
import com.github.ptitnoony.apps.bowling.hmi.ScreenController;
import com.github.ptitnoony.apps.bowling.hmi.ScreenEvents;
import com.github.ptitnoony.apps.bowling.hmi.edition.playercreation.PlayerDialogFactory;
import com.github.ptitnoony.apps.bowling.utils.ConfrontationFactory;
import com.github.ptitnoony.apps.bowling.utils.SessionFactory;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.controlsfx.control.ListSelectionView;

/**
 * FXML Controller class
 *
 * @author Arnaud HAMON-KEROMEN
 */
public class SessionCreatorScreenController implements ScreenController {

    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(SessionCreatorScreenController.this);

    @FXML
    private DatePicker sessionDatePicker;
    @FXML
    private ListSelectionView<Player> playerSelectionListView;
    @FXML
    private TextField locationField;
    //
    @FXML
    private TableView<Player> tableView;
    @FXML
    private TableColumn<Player, String> playerColumn;
    //TODO manage integers
    @FXML
    private TableColumn<Player, String> totalScoreColumn;

    private Session session;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        playerSelectionListView.getSourceItems().setAll(PlayerFactory.getCreatedPlayers());
        sessionDatePicker.setValue(LocalDate.now());
        sessionDatePicker.valueProperty().addListener((ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) -> {
            //todo log
            session.setDate(newValue);
        });
        session = SessionFactory.createSession(LocalDate.now());

        playerColumn.setPrefWidth(100);
        playerColumn.setCellValueFactory((TableColumn.CellDataFeatures<Player, String> param) -> new ReadOnlyStringWrapper(param.getValue().getNickName()));

        totalScoreColumn.setPrefWidth(100);
        totalScoreColumn.setCellValueFactory((TableColumn.CellDataFeatures<Player, String> param) -> new ReadOnlyStringWrapper("" + session.getTotalScore(param.getValue())));

        // TODO update session date on change
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    @FXML
    protected void onCancelAction(ActionEvent event) {
        // TODO: log event
        propertyChangeSupport.firePropertyChange(ScreenEvents.CANCEL, null, null);
    }

    @FXML
    protected void onCreatePlayer(ActionEvent event) {
        //TODO: log event
        Optional<Player> newPlayer = PlayerDialogFactory.requestPlayerDialog();
        //TODO: log created player
        if (newPlayer.isPresent()) {
            playerSelectionListView.getTargetItems().add(newPlayer.get());
        }
    }

    @FXML
    protected void onGameCreationAction(ActionEvent event) {
        playerSelectionListView.getTargetItems().forEach(session::addPlayer);
        propertyChangeSupport.firePropertyChange(ScreenEvents.OPEN_CONFRONTATION_CREATOR, null, session);
    }

    @FXML
    public void onCreateSessionAction(ActionEvent event) {
        //todo log
        session.setLocation(locationField.getText() != null ? locationField.getText() : "");
        // no need since added upon creation
//        Sessions.addSession(session);
        tableView.getItems().clear();
        propertyChangeSupport.firePropertyChange(ScreenEvents.MAIN_SCREEN, null, null);
    }

    protected void createConfrontation(List<EditablePlayerRound> confrontationRounds) {
        final Confrontation confrontation = ConfrontationFactory.createSession(sessionDatePicker.getValue());
        confrontationRounds.forEach(round -> confrontation.addRound(round));
        session.addConfrontation(confrontation);

        //TEMP
        tableView.setItems(playerSelectionListView.getTargetItems());

        TableColumn<Player, String> confrontationColumn = new TableColumn<>("Round " + (session.getConfrontations().indexOf(confrontation) + 1));
        confrontationColumn.setPrefWidth(100);
        confrontationColumn.setCellValueFactory((TableColumn.CellDataFeatures<Player, String> param) -> new ReadOnlyStringWrapper("" + confrontation.getPlayerScore(param.getValue())));

        tableView.getColumns().add(tableView.getColumns().size() - 1, confrontationColumn);
    }

}
