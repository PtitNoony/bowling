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

import com.github.ptitnoony.apps.bowling.hmi.FxDrawing;
import com.github.ptitnoony.apps.bowling.hmi.ITurnDrawing;
import com.github.ptitnoony.apps.bowling.hmi.ThrowLabel;
import com.github.ptitnoony.apps.bowling.BallType;
import com.github.ptitnoony.apps.bowling.Round;
import com.github.ptitnoony.apps.bowling.EditableTurn;

/**
 *
 * @author Arnaud HAMON-KEROMEN
 */
public class TurnDrawing extends FxDrawing implements ITurnDrawing {

    private final ThrowLabel score1Label;
    private final ThrowLabel score2Label;
    private final ThrowLabel scoreFLabel;
    private final Round round;
    private final EditableTurn turn;
    private double width = DEFAULT_CELL_WIDTH;
    private double height = DEFAULT_CELL_HEIGHT;

    private final int index;

    //TODO change API
    public TurnDrawing(Round r, EditableTurn t, int turnIndex) {
        super();
        score1Label = new ThrowLabel(DEFAULT_TEXT);
        score2Label = new ThrowLabel(DEFAULT_TEXT);
        scoreFLabel = new ThrowLabel(DEFAULT_TEXT);
        round = r;
        turn = t;
        index = turnIndex;
        init();
    }

    private void init() {
        addNode(score1Label.getNode());
        addNode(score2Label.getNode());
        addNode(scoreFLabel.getNode());
        //
        //TODO
        score1Label.setText("" + turn.getNbPinForThrow(1));
        if (turn.isStrike()) {
            score2Label.setText(STRIKE_DRAWING);
        } else if (turn.isSpare()) {
            score2Label.setText(SPARE_DRAWING);

        } else {
            score2Label.setText("" + turn.getNbPinForThrow(2));
        }
        scoreFLabel.setText("" + round.getScores()[index]);
        if (turn.isSplit()) {
            score1Label.setThrowType(BallType.SPLIT);
        } else {
            score1Label.setThrowType(BallType.SIMPLE);
        }
        if (turn.isSpare()) {
            score2Label.setThrowType(BallType.SPARE);
        } else if (turn.isStrike()) {
            score2Label.setThrowType(BallType.STRIKE);
        } else {
            score2Label.setThrowType(BallType.SIMPLE);
        }

        //
        setSize(DEFAULT_CELL_WIDTH, DEFAULT_CELL_HEIGHT);
    }

    @Override
    public void setSize(double sX, double sY) {
        width = sX;
        height = sY;
        updateSize();
    }

    private void updateSize() {
        //size for strike
        if (turn != null && turn.isStrike()) {
            score2Label.setTranslateX(0);
            score2Label.setPrefSize(width, height / 2.0);
        } else {
            score2Label.setTranslateX(width / 2.0);
            score2Label.setPrefSize(width / 2.0, height / 2.0);
        }
        //position
        scoreFLabel.setTranslateY(height / 2.0);
        //sizes
        score1Label.setPrefSize(width / 2.0, height / 2.0);
        scoreFLabel.setPrefSize(width, height / 2.0);
    }

}
