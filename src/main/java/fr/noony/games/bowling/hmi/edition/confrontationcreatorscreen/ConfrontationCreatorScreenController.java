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

import static fr.noony.games.bowling.hmi.FxDrawing.CELL_NAME_RATIO;
import static fr.noony.games.bowling.hmi.FxDrawing.DEFAULT_EDITOR_CELL_HEIGHT;
import static fr.noony.games.bowling.hmi.FxDrawing.DEFAULT_EDITOR_CELL_WIDTH;
import fr.noony.games.bowling.hmi.ThrowLabel;
import fr.noony.games.bowling.Session;
import fr.noony.games.bowling.EditablePlayerRound;
import fr.noony.games.bowling.hmi.ScreenEvents;
import fr.noony.gameutils.Player;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * FXML Controller class
 *
 * @author Arnaud HAMON-KEROMEN
 */
public class ConfrontationCreatorScreenController implements Initializable {

    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(ConfrontationCreatorScreenController.this);

    @FXML
    private AnchorPane confrontationPane;

    private Session sessionInProgress;

    //private final Confrontation confrontation;
    private Group playersGroup;
    private Rectangle background;
    private ThrowLabel nameLabel;
    private ThrowLabel scoreLabel;
    private ThrowLabel[] turnNumberLabels;
    private ThrowPopupEditor popup;
    //
    private double width = 0.0;
    private double height = 0.0;

    private final List<PlayerConfrontationEditor> playerConfrontationEditors = new LinkedList<>();
    //

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        background = new Rectangle();
        nameLabel = new ThrowLabel("PLAYER");
        turnNumberLabels = new ThrowLabel[10];
        scoreLabel = new ThrowLabel("TOTAL");
        playersGroup = new Group();
        popup = new ThrowPopupEditor();
        init();
    }

    @FXML
    protected void onCancelAction(ActionEvent event){
        //TODO: log
        propertyChangeSupport.firePropertyChange(ScreenEvents.CANCEL, null, null);
    }
    
    @FXML
    protected void onCreateAction(ActionEvent event) {
        //TODO: log
        final List<EditablePlayerRound> editablePlayerRounds = new LinkedList<>();
        playerConfrontationEditors.forEach(editor -> editablePlayerRounds.add(editor.getEditablePlayerRound()));
        propertyChangeSupport.firePropertyChange(ScreenEvents.NEW_CONFRONTATION, null, Collections.unmodifiableList(editablePlayerRounds));
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }
    
    
    protected void clear() {
        playerConfrontationEditors.clear();
        playersGroup.getChildren().clear();
        setSize(DEFAULT_EDITOR_CELL_WIDTH * (10 + CELL_NAME_RATIO), DEFAULT_EDITOR_CELL_HEIGHT * (1.5 + playerConfrontationEditors.size()));
    }

    protected void setSessionInProgress(Session session) {
        sessionInProgress = session;
        populateSession();
    }

    private void init() {
        //TODO: use constants
        background.setFill(Color.GREY);
        background.setStroke(Color.BLACK);
        background.setStrokeWidth(6.0);
        //header
        addNode(background);
        addNode(nameLabel.getNode());
        nameLabel.setFill(Color.LIGHTGREY);
        for (int i = 0; i < turnNumberLabels.length; i++) {
            ThrowLabel turnNumberLabel = new ThrowLabel("" + (i + 1));
            turnNumberLabel.setFill(Color.LIGHTGREY);
            turnNumberLabels[i] = turnNumberLabel;
            addNode(turnNumberLabel.getNode());
        }
        addNode(scoreLabel.getNode());
        scoreLabel.setFill(Color.LIGHTGREY);
        //
        addNode(playersGroup);
        //pop up in last position to be over all other drawings
        popup.setVisible(false);
        addNode(popup.getNode());
        setSize(DEFAULT_EDITOR_CELL_WIDTH * (10 + CELL_NAME_RATIO), DEFAULT_EDITOR_CELL_HEIGHT * (0.5 + playerConfrontationEditors.size()));
    }

    //todo
    private void setSize(double sX, double sY) {
        width = sX;
        height = sY;
        updateLayout();
    }

    private void updateLayout() {
        int nbPlayers = playerConfrontationEditors.size();
        double newCellHeight = height / (nbPlayers + 0.5 );
        double newCellWidth = width / (12 + CELL_NAME_RATIO);
        //background
        background.setWidth(width);
        background.setHeight(height);
        //first fine
        nameLabel.setPrefSize(newCellWidth * CELL_NAME_RATIO, newCellHeight / 2.0);
        for (int i = 0; i < 9; i++) {
            turnNumberLabels[i].setTranslateX((CELL_NAME_RATIO + i) * newCellWidth);
            turnNumberLabels[i].setPrefSize(newCellWidth, newCellHeight / 2.0);
        }
            turnNumberLabels[9].setTranslateX((CELL_NAME_RATIO + 9) * newCellWidth);
            turnNumberLabels[9].setPrefSize(newCellWidth*2.0, newCellHeight / 2.0);
        scoreLabel.setPrefSize(newCellWidth, newCellHeight / 2.0);
        scoreLabel.setTranslateX((CELL_NAME_RATIO + 11) * newCellWidth);
        //player drawings
        playerConfrontationEditors.stream().forEach(playerGameDrawing -> {
            playerGameDrawing.setSize(width, newCellHeight);
            playerGameDrawing.setTranslateY(newCellHeight / 2.0 + playerConfrontationEditors.indexOf(playerGameDrawing) * newCellHeight);
        });
    }

    private void addNode(Node node) {
        confrontationPane.getChildren().add(node);
    }

    private void populateSession() {
        // todo: clean before
        sessionInProgress.getPlayers().forEach(this::addPlayer);

    }

    private void addPlayer(Player player) {
//        LOG.log(Level.INFO, "add player action", event);
        double oldHeight = height / (1.5 + playerConfrontationEditors.size());
        PlayerConfrontationEditor playerEditor = new PlayerConfrontationEditor(player, this::showPopUp);
        playerConfrontationEditors.add(playerEditor);
        playersGroup.getChildren().add(playerEditor.getNode());
        double newHeight = oldHeight * (1.5 + playerConfrontationEditors.size());
        setSize(width, newHeight);
        updateLayout();
    }

    private void showPopUp(PropertyChangeEvent evt) {
        if (ThrowLabelEditable.LABEL_CLICKED.equals(evt.getPropertyName())) {
            ThrowLabelEditable label = (ThrowLabelEditable) evt.getNewValue();
            //TODO: check if not touch events !!!
            MouseEvent event = (MouseEvent) evt.getOldValue();
            Point2D localtoScene = confrontationPane.localToScene(0.0, 0.0);
            double x = event.getSceneX() - localtoScene.getX();
            double y = event.getSceneY() - localtoScene.getY();
            //TODO true center
            popup.setCenter(x, y);
            popup.setThrowModel(label);
            popup.setVisible(true);
        }
    }


}
