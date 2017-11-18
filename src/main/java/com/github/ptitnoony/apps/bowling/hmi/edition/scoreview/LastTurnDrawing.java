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
import com.github.ptitnoony.apps.bowling.LastTurn;
import com.github.ptitnoony.apps.bowling.Round;

/**
 *
 * @author Arnaud HAMON-KEROMEN
 */
public class LastTurnDrawing extends FxDrawing implements ITurnDrawing {

    private final ThrowLabel score1Label;
    private final ThrowLabel score2Label;
    private final ThrowLabel score3Label;
    private final ThrowLabel scoreFLabel;
    private final Round round;
    private final LastTurn turn;

    public LastTurnDrawing(Round r, LastTurn t) {
        super();
        score1Label = new ThrowLabel(DEFAULT_TEXT);
        score2Label = new ThrowLabel(DEFAULT_TEXT);
        score3Label = new ThrowLabel(DEFAULT_TEXT);
        scoreFLabel = new ThrowLabel(DEFAULT_TEXT);
        round =r;
        turn=t;
        init();
    }

    private void init() {
        addNode(score1Label.getNode());
        addNode(score2Label.getNode());
        addNode(score3Label.getNode());
        addNode(scoreFLabel.getNode());
        setSize(DEFAULT_CELL_WIDTH, DEFAULT_CELL_HEIGHT);
        
        if (round != null && turn != null) {
            //handling first throw
            if (turn.isStrike()) {
                score1Label.setText(STRIKE_DRAWING);
                score1Label.setThrowType(BallType.STRIKE);
            } else if (turn.isSplit()) {
                score1Label.setText("" + turn.getNbPinForThrow(1));
                score1Label.setThrowType(BallType.SPLIT);
            } else {
                score1Label.setText("" + turn.getNbPinForThrow(1));
                score1Label.setThrowType(BallType.SIMPLE);
            }
            //handling second throw
            if (turn.isSpare()) {
                score2Label.setText(SPARE_DRAWING);
                score2Label.setThrowType(BallType.SPARE);
            } else if (turn.isSecondBallStrike()||turn.getNbPinForThrow(2)==10) {
                score2Label.setText(STRIKE_DRAWING);
                score2Label.setThrowType(BallType.STRIKE);
            } else if (turn.isSecondBallSplit()) {
                score2Label.setText("" + turn.getNbPinForThrow(2));
                score2Label.setThrowType(BallType.SPLIT);
            } else {
                score2Label.setText("" + turn.getNbPinForThrow(2));
                score2Label.setThrowType(BallType.SIMPLE);
            }
            //handling fhird throw
            if (turn.getNbPinForThrow(2) != 0) {
                if (turn.isThirdBallStrike()||turn.getNbPinForThrow(3)==10) {
                    score3Label.setText(STRIKE_DRAWING);
                    score3Label.setThrowType(BallType.STRIKE);
                } else if (turn.isThirdBallSpare()) {
                    score3Label.setText(SPARE_DRAWING);
                    score3Label.setThrowType(BallType.SPARE);
                } else if (turn.isThirdBallSplit()) {
                    score3Label.setText("" + turn.getNbPinForThrow(3));
                    score3Label.setThrowType(BallType.SPLIT);
                } else {
                    score3Label.setText("" + turn.getNbPinForThrow(3));
                    score3Label.setThrowType(BallType.SIMPLE);
                }
            } else {
                score3Label.setText(DEFAULT_TEXT);
            }
            scoreFLabel.setText("" + round.getScores()[9]);
        }
    }

    @Override
    public void setSize(double sX, double sY) {
        //positions
        score2Label.setTranslateX(sX / 3.0);
        score3Label.setTranslateX(2.0 * sX / 3.0);
        scoreFLabel.setTranslateY(sY / 2.0);
        //sizes
        score1Label.setPrefSize(sX / 3.0, sY / 2.0);
        score2Label.setPrefSize(sX / 3.0, sY / 2.0);
        score3Label.setPrefSize(sX / 3.0, sY / 2.0);
        scoreFLabel.setPrefSize(sX, sY / 2.0);
    }


}
