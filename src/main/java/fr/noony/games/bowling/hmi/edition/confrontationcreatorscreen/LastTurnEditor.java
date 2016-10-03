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

import fr.noony.games.bowling.BallType;
import fr.noony.games.bowling.hmi.FxDrawing;
import fr.noony.games.bowling.hmi.ITurnDrawing;
import fr.noony.games.bowling.hmi.ThrowLabel;
import fr.noony.games.bowling.EditablePlayerRound;
import java.beans.PropertyChangeListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.text.Font;

/**
 *
 * @author Arnaud HAMON-KEROMEN
 */
public class LastTurnEditor extends FxDrawing implements ITurnDrawing {

    private static final Logger LOG = Logger.getLogger(TurnEditor.class.getName());

    private final EditablePlayerRound playerRound;

    private final ThrowLabelEditable score1Label;
    private final ThrowLabelEditable score2Label;
    private final ThrowLabelEditable score3Label;
    private final ThrowLabel scoreFLabel;
    private final CheckBox isSplitBox1;
    private final CheckBox isSplitBox2;
    private final CheckBox isSplitBox3;
    private final int turnNumber;

    public LastTurnEditor(PropertyChangeListener listener, EditablePlayerRound editablePlayerRound, int turnNb) {
        super();
        playerRound = editablePlayerRound;
        score1Label = new ThrowLabelEditable(DEFAULT_TEXT);
        score2Label = new ThrowLabelEditable(DEFAULT_TEXT);
        score3Label = new ThrowLabelEditable(DEFAULT_TEXT);
        scoreFLabel = new ThrowLabel(DEFAULT_TEXT);
        isSplitBox1 = new CheckBox("");
        isSplitBox2 = new CheckBox("");
        isSplitBox3 = new CheckBox("");
        turnNumber = turnNb;
        init(listener);
    }

    private void init(PropertyChangeListener listener) {
        isSplitBox1.setFont(new Font(10));
        isSplitBox2.setFont(new Font(10));
        isSplitBox3.setFont(new Font(10));
        isSplitBox1.setAlignment(Pos.CENTER);
        isSplitBox2.setAlignment(Pos.CENTER);
        isSplitBox3.setAlignment(Pos.CENTER);
        isSplitBox1.setPadding(new Insets(4, 4, 4, 4));
        isSplitBox2.setPadding(new Insets(4, 4, 4, 4));
        isSplitBox3.setPadding(new Insets(4, 4, 4, 4));
        //
        addNode(score1Label.getNode());
        addNode(score2Label.getNode());
        addNode(score3Label.getNode());
        addNode(isSplitBox1);
        addNode(isSplitBox2);
        addNode(isSplitBox3);
        addNode(scoreFLabel.getNode());
        //
        score1Label.addPropertyChangeListener(listener);
        score2Label.addPropertyChangeListener(listener);
        score3Label.addPropertyChangeListener(listener);
        //
        score1Label.addPropertyChangeListener(event -> {
            if (ThrowLabelEditable.VALUE_CHANGED.equals(event.getPropertyName())) {
                playerRound.setThrowValue(turnNumber, 1, (int) event.getNewValue());
            }
        });
        score2Label.addPropertyChangeListener(event -> {
            if (ThrowLabelEditable.VALUE_CHANGED.equals(event.getPropertyName())) {
                playerRound.setThrowValue(turnNumber, 2, (int) event.getNewValue());
            }
        });
        score3Label.addPropertyChangeListener(event -> {
            if (ThrowLabelEditable.VALUE_CHANGED.equals(event.getPropertyName())) {
                playerRound.setThrowValue(turnNumber, 3, (int) event.getNewValue());
            }
        });
        //
        isSplitBox1.setSelected(false);
        isSplitBox1.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            LOG.log(Level.FINE, "new split for {0} on throw 1 on observable={1} oldValue={2} newValue={3}", new Object[]{this, observable, oldValue, newValue});
            playerRound.setTurnIsSplit(turnNumber, 1, newValue);
        });

        isSplitBox2.setSelected(false);
        isSplitBox2.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            LOG.log(Level.FINE, "new split for {0} on throw 2 on observable={1} oldValue={2} newValue={3}", new Object[]{this, observable, oldValue, newValue});
            playerRound.setTurnIsSplit(turnNumber, 2, newValue);
        });

        isSplitBox3.setSelected(false);
        isSplitBox3.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            LOG.log(Level.FINE, "new split for {0} on throw 3 on observable={1} oldValue={2} newValue={3}", new Object[]{this, observable, oldValue, newValue});
            playerRound.setTurnIsSplit(turnNumber, 3, newValue);
        });
        //
        setSize(DEFAULT_CELL_WIDTH, DEFAULT_CELL_HEIGHT);
    }

    @Override
    public void setSize(double sX, double sY) {
        //positions
        score2Label.setTranslateX(sX / 3.0);
        isSplitBox2.setTranslateX(sX / 3.0);
        score3Label.setTranslateX(2.0 * sX / 3.0);
        isSplitBox3.setTranslateX(2.0 * sX / 3.0);
        isSplitBox1.setTranslateY(sX / 3.0);
        isSplitBox2.setTranslateY(sX / 3.0);
        isSplitBox3.setTranslateY(sX / 3.0);
        scoreFLabel.setTranslateY(2.0 * sY / 3.0);
        //sizes
        score1Label.setPrefSize(sX / 3.0, sY / 3.0);
        score2Label.setPrefSize(sX / 3.0, sY / 3.0);
        score3Label.setPrefSize(sX / 3.0, sY / 3.0);
        isSplitBox1.setPrefSize(sX / 3.0, sY / 3.0);
        isSplitBox2.setPrefSize(sX / 3.0, sY / 3.0);
        isSplitBox3.setPrefSize(sX / 3.0, sY / 3.0);
        scoreFLabel.setPrefSize(sX, sY / 3.0);
    }

    public void update() {
        score1Label.setText(Integer.toString(playerRound.getThrowValue(turnNumber, 1)));
        score2Label.setText(Integer.toString(playerRound.getThrowValue(turnNumber, 2)));
        score3Label.setText(Integer.toString(playerRound.getThrowValue(turnNumber, 3)));
        if (playerRound.isThrowSplit(turnNumber, 1)) {
            score1Label.setThrowType(BallType.SPLIT);
        }
        if (playerRound.isThrowSplit(turnNumber, 2)) {
            score2Label.setThrowType(BallType.SPLIT);
        }
        if (playerRound.isThrowSplit(turnNumber, 3)) {
            score3Label.setThrowType(BallType.SPLIT);
        }
        scoreFLabel.setText(Integer.toString(playerRound.getScoreAtTurn(turnNumber)));
    }

}
