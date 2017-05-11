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
import fr.noony.games.bowling.hmi.ThrowLabel;
import fr.noony.games.bowling.EditablePlayerRound;
import fr.noony.gameutils.Player;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 *
 * @author Arnaud HAMON-KEROMEN
 */
public class PlayerConfrontationEditor extends FxDrawing {

    private final Rectangle background;
    private final Label playerNameField;
    private final TurnEditor[] turnEditors;
    private final LastTurnEditor lastTurnEditor;
    private final ThrowLabel scoreLabel;
    //
    private final Player editedPlayer;
    //
    private final EditablePlayerRound playerRound;

    public PlayerConfrontationEditor(Player aPlayer, PropertyChangeListener listener) {
        super();
        //
        editedPlayer = aPlayer;
        //
        playerRound = new EditablePlayerRound(editedPlayer);
        playerRound.addPropertyChangeListener(this::handleRoundUpdate);
        //
        background = new Rectangle();
        playerNameField = new Label(editedPlayer.getNickName());
        //TODO use static field to uniformize 
        playerNameField.setFont(Font.font(null, FontWeight.BOLD, 16));
        playerNameField.setAlignment(Pos.CENTER);
        //TODO on key typed
        turnEditors = new TurnEditor[9];
        lastTurnEditor = new LastTurnEditor(listener, playerRound, 10);
        scoreLabel = new ThrowLabel("0");
        scoreLabel.setFont(Font.font(null, FontWeight.BOLD, 16));
        init(listener);
    }

    private void init(PropertyChangeListener listener) {
        background.setFill(Color.GHOSTWHITE);
        background.setStroke(Color.BLACK);
        background.setStrokeWidth(3.0);
        addNode(background);
        addNode(playerNameField);
        for (int i = 0; i < turnEditors.length; i++) {
            TurnEditor turnEditor = new TurnEditor(listener, playerRound, i + 1);
            turnEditors[i] = turnEditor;
            addNode(turnEditor.getNode());
            turnEditor.setTranslateX(PLAYER_NAME_WIDTH + i * DEFAULT_EDITOR_CELL_WIDTH);
        }
        addNode(lastTurnEditor.getNode());
        lastTurnEditor.setTranslateX(PLAYER_NAME_WIDTH + 9 * DEFAULT_EDITOR_CELL_WIDTH);
        addNode(scoreLabel.getNode());
        scoreLabel.setTranslateX(PLAYER_NAME_WIDTH + 11 * DEFAULT_EDITOR_CELL_WIDTH);
        scoreLabel.setPrefSize(DEFAULT_EDITOR_CELL_WIDTH, DEFAULT_EDITOR_CELL_HEIGHT);
        //TODO: use set size instead
    }

    @Override
    public void setSize(double sX, double sY) {
        background.setWidth(sX);
        background.setHeight(sY);
        double newCellWidth = sX / (12 + CELL_NAME_RATIO);
        playerNameField.setPrefSize(newCellWidth * CELL_NAME_RATIO, sY);
        for (int i = 0; i < turnEditors.length; i++) {
            turnEditors[i].setSize(newCellWidth, sY);
            turnEditors[i].setTranslateX(newCellWidth * (i + CELL_NAME_RATIO));
        }
        lastTurnEditor.setSize(newCellWidth*2.0, sY);
        lastTurnEditor.setTranslateX(newCellWidth * (9 + CELL_NAME_RATIO));
        scoreLabel.setTranslateX((11 + CELL_NAME_RATIO) * newCellWidth);
        scoreLabel.setPrefSize(newCellWidth, sY);
    }

    public EditablePlayerRound getEditablePlayerRound() {
        return playerRound;
    }

    private void handleRoundUpdate(PropertyChangeEvent event) {
        //todo log
        for (int i = 1; i <= 9; i++) {
            turnEditors[i - 1].update();
        }
        lastTurnEditor.update();
        scoreLabel.setText(Integer.toString(playerRound.getFinalScore()));
    }

}
