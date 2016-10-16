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
import fr.noony.games.bowling.hmi.ITurnDrawing;
import fr.noony.games.bowling.hmi.ThrowLabel;
import fr.noony.games.bowling.BallType;
import fr.noony.games.bowling.EditablePlayerRound;
import java.beans.PropertyChangeListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.text.Font;

/**
 *
 * @author Arnaud HAMON-KEROMEN
 */
public class TurnEditor extends FxDrawing implements ITurnDrawing {

    private static final Logger LOG = Logger.getLogger(TurnEditor.class.getName());

    private enum TURN_STATE {
        INIT, BOTH_EDITABLE
    }

    private final EditablePlayerRound playerRound;
    private final int turnNumber;

    private final ThrowLabelEditable score1Label;
    private final ThrowLabelEditable score2Label;
    private final ThrowLabel scoreFLabel;
    private final CheckBox isSplitBox;
    private double width = DEFAULT_EDITOR_CELL_WIDTH;
    private double height = DEFAULT_EDITOR_CELL_HEIGHT;
    //

    public TurnEditor(PropertyChangeListener listener, EditablePlayerRound editablePlayerRound, int theTurnNumber) {
        super();
        turnNumber = theTurnNumber;
        playerRound = editablePlayerRound;
        score1Label = new ThrowLabelEditable(DEFAULT_TEXT);
        score2Label = new ThrowLabelEditable(DEFAULT_TEXT);
        scoreFLabel = new ThrowLabel(DEFAULT_TEXT);
        isSplitBox = new CheckBox("Split");
        isSplitBox.setFont(new Font(10));
        isSplitBox.setStyle("-fx-border-style: solid inside;");
        isSplitBox.setPadding(new Insets(4, 4, 4, 4));
        isSplitBox.setAlignment(Pos.CENTER);
        init(listener);
    }

    private void init(PropertyChangeListener listener) {
        isSplitBox.setSelected(false);
        isSplitBox.setOnAction(event -> {
            LOG.log(Level.SEVERE, "new split for {0} on observable={1} oldValue={2} newValue={3}", new Object[]{this, event});
            playerRound.setTurnIsSplit(turnNumber, isSplitBox.isSelected());
        });
        //todo: hum?? for what
        score1Label.addPropertyChangeListener(listener);
        score2Label.addPropertyChangeListener(listener);
        //todo: to it cleaner
        score1Label.addPropertyChangeListener(event -> {
            if (ThrowLabelEditable.VALUE_CHANGED.equals(event.getPropertyName())) {
                int value = (int) event.getNewValue();
                playerRound.setThrowValue(turnNumber, 1, (int) event.getNewValue());
            }
        });
        score2Label.addPropertyChangeListener(event -> {
            if (ThrowLabelEditable.VALUE_CHANGED.equals(event.getPropertyName())) {
                playerRound.setThrowValue(turnNumber, 2, (int) event.getNewValue());
            }
        });
        //
        addNode(score1Label.getNode());
        addNode(score2Label.getNode());
        addNode(scoreFLabel.getNode());
        addNode(isSplitBox);
        setSize(DEFAULT_EDITOR_CELL_WIDTH, DEFAULT_EDITOR_CELL_HEIGHT);
    }

    @Override
    public void setSize(double sX, double sY) {
        width = sX;
        height = sY;
        updateSize();
    }

    public void update() {
        score1Label.setText(Integer.toString(playerRound.getThrowValue(turnNumber, 1)));
        score2Label.setText(Integer.toString(playerRound.getThrowValue(turnNumber, 2)));
        scoreFLabel.setText(Integer.toString(playerRound.getScoreAtTurn(turnNumber)));

        if (playerRound.isThrowSplit(turnNumber)) {
            score1Label.setThrowType(BallType.SPLIT);
        } else if (playerRound.isThrowStrike(turnNumber)) {
            score1Label.setThrowType(BallType.STRIKE);
        } else {
            score1Label.setThrowType(BallType.SIMPLE);
        }
        if (playerRound.isThrowSpare(turnNumber)) {
            score2Label.setThrowType(BallType.SPARE);
        }
        //
        isSplitBox.setSelected(playerRound.isThrowSplit(turnNumber));
    }

    private void updateSize() {
        //size for strike
        if (playerRound.isThrowStrike(turnNumber)) {
            score2Label.setTranslateX(0);
            score2Label.setPrefSize(width, 2 * height / 3.0);
        } else {
            score2Label.setTranslateX(width / 2.0);
            score2Label.setPrefSize(width / 2.0, 2 * height / 3.0);
        }
        //position
        isSplitBox.setTranslateY(height / 3.0);
        scoreFLabel.setTranslateY(2 * height / 3.0);
        //sizes
        score1Label.setPrefSize(width / 2.0, height / 3.0);
        score2Label.setPrefSize(width / 2.0, height / 3.0);
        isSplitBox.setPrefSize(width, height / 3.0);
        scoreFLabel.setPrefSize(width, height / 3.0);
    }

}
