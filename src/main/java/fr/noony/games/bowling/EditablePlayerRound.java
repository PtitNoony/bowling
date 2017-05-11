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
package fr.noony.games.bowling;

import fr.noony.gameutils.Player;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 *
 * @author Arnaud HAMON-KEROMEN
 */
public class EditablePlayerRound implements Round {

    public static final String ROUND_UPDATED = "roundUpdated";

    private final PropertyChangeSupport propertyChangeSupport;

    private final Player player;
    private final EditableTurn[] turns;

    private final int[] scores;

    private Confrontation confrontation;

    private int currentScore = 0;

    public EditablePlayerRound(Player aPlayer) {
        propertyChangeSupport = new PropertyChangeSupport(EditablePlayerRound.this);
        player = aPlayer;
        //
        scores = new int[10];
        for (int i = 0; i < 10; i++) {
            scores[i] = 0;
        }
        //
        turns = new EditableTurn[10];
        for (int i = 0; i < 9; i++) {
            final EditableTurn eTurn = new EditableTurnImpl();
            turns[i] = eTurn;
        }
        final EditableTurn lastETurn = new LastEditableTurnImpl();
        turns[9] = lastETurn;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public int getFinalScore() {
        return currentScore;
    }

    @Override
    public int[] getScores() {
        return scores.clone();
    }

    @Override
    public Turn[] getTurns() {
        //hum
        return turns.clone();
    }

    @Override
    public int getNbSpare() {
        //TODO cache the value
        int result = 0;
        for (int i = 0; i < 9; i++) {
            if (turns[i].isSpare()) {
                result++;
            }
        }
        LastTurn lastTurn = (LastTurn) turns[9];
        if (lastTurn.isSecondBallSpare()) {
            result++;
        }
        if (lastTurn.isThirdBallSpare()) {
            result++;
        }
        return result;
    }

    @Override
    public int getNbStrikes() {
        //TODO cache the value
        int result = 0;
        for (int i = 0; i < 10; i++) {
            if (turns[i].isStrike()) {
                result++;
            }
        }
        LastTurn lastTurn = (LastTurn) turns[9];
        if (lastTurn.isSecondBallStrike()) {
            result++;
        }
        if (lastTurn.isThirdBallStrike()) {
            result++;
        }
        return result;
    }

    @Override
    public void setConfrontation(Confrontation aConfrontation) {
        confrontation = aConfrontation;
    }

    @Override
    public Confrontation getConfrontation() {
        return confrontation;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void setThrowValue(int turnNumber, int throwNumber, int value) {
        turns[turnNumber - 1].setThrowValue(throwNumber, value);
        updateScore();
        fireRoundUpdated(turnNumber);
    }

    public void setTurnIsSplit(int turnNumber, boolean isSplit) {
        turns[turnNumber - 1].setIsSplit(isSplit);
        fireRoundUpdated(turnNumber);
    }

    public void setTurnIsSplit(int turnNumber, int throwNumber, boolean isSplit) {
        if (turnNumber == 10) {
            switch (throwNumber) {
                case 1:
                    ((LastEditableTurn) turns[9]).setBall1isSplit(isSplit);
                    break;
                case 2:
                    ((LastEditableTurn) turns[9]).setBall2isSplit(isSplit);
                    break;
                case 3:
                    ((LastEditableTurn) turns[9]).setBall3isSplit(isSplit);
                    break;
                default:;
                    throw new IllegalArgumentException("wrong throw number " + throwNumber);
            }
            fireRoundUpdated(turnNumber);
        } else {
            System.err.println("setTurnIsSplit with number " + turnNumber + " -> " + isSplit);
            setTurnIsSplit(turnNumber, isSplit);
        }
    }

    public int getThrowValue(int turnNumber, int throwNumber) {
        return turns[turnNumber - 1].getNbPinForThrow(throwNumber);
    }

    public boolean isThrowSplit(int turnNumber) {
        return turns[turnNumber - 1].isSplit();
    }

    public boolean isThrowSplit(int turnNumber, int throwNumber) {
        if (turnNumber == 10) {
            switch (throwNumber) {
                case 1:
                    return ((LastEditableTurn) turns[9]).isSplit();
                case 2:
                    return ((LastEditableTurn) turns[9]).isSecondBallSplit();
                case 3:
                    return ((LastEditableTurn) turns[9]).isThirdBallSplit();
                default:;
                    throw new IllegalArgumentException("wrong throw number " + throwNumber);
            }
        } else {
            return turns[turnNumber - 1].isSplit();
        }
    }

    public boolean isThrowStrike(int turnNumber) {
        return turns[turnNumber - 1].isStrike();
    }

    public boolean isThrowStrike(int turnNumber, int throwNumber) {
        if (turnNumber == 10) {
            switch (throwNumber) {
                case 1:
                    return ((LastEditableTurn) turns[9]).isStrike();
                case 2:
                    return ((LastEditableTurn) turns[9]).isSecondBallStrike();
                case 3:
                    return ((LastEditableTurn) turns[9]).isThirdBallStrike();
                default:;
                    throw new IllegalArgumentException("wrong throw number " + throwNumber);
            }
        } else {
            return turns[turnNumber - 1].isStrike();
        }
    }

    public boolean isThrowSpare(int turnNumber) {
        return turns[turnNumber - 1].isSpare();
    }

    public boolean isThrowSpare(int turnNumber, int throwNumber) {
        if (turnNumber == 10) {
            switch (throwNumber) {
                case 1:
                    return false;
                case 2:
                    return ((LastEditableTurn) turns[9]).isSpare();
                case 3:
                    return ((LastEditableTurn) turns[9]).isThirdBallSpare();
                default:;
                    throw new IllegalArgumentException("wrong throw number " + throwNumber);
            }
        } else {
            return turns[turnNumber - 1].isSpare();
        }
    }

    public int getScoreAtTurn(int turn) {
        return scores[turn - 1];
    }

    private void fireRoundUpdated(int turnNumber) {
        propertyChangeSupport.firePropertyChange(ROUND_UPDATED, null, turnNumber);
    }

    private void updateScore() {
        currentScore = 0;
        for (int i = 0; i < 10; i++) {
            if (turns[i].isSpare()) {
                calculateForSpare(i);
            } else if (turns[i].isStrike()) {
                calculateForStrike(i);
            } else {
                // simple turn
                currentScore = currentScore + turns[i].getNbPins();
                scores[i] = currentScore;
            }
        }
    }

    private void calculateForSpare(int i) {
        // if last turn
        if (i == 9) {
            currentScore = currentScore + turns[i].getNbPins();
            scores[i] = currentScore;
        } else {
            currentScore = currentScore + 10 + turns[i + 1].getNbPinForThrow(1);
            scores[i] = currentScore;
        }
    }

    // not optimized yet
    public void calculateForStrike(int i) {
        // if last turn
        switch (i) {
            case 9:
                currentScore = currentScore + turns[i].getNbPins();
                scores[i] = currentScore;
                break;
            case 8:
                // if turn before
                if (turns[9].isStrike()) {
                    currentScore += 20 + turns[9].getNbPinForThrow(2);
                    scores[i] = currentScore;
                } else {
                    currentScore = currentScore + 10 + turns[9].getNbPinForThrow(1) + turns[9].getNbPinForThrow(2);
                    scores[i] = currentScore;
                }
                break;
            default:
                // default case
                if (turns[i + 1].isStrike()) {
                    currentScore = currentScore + 20 + turns[i + 2].getNbPinForThrow(1);
                    scores[i] = currentScore;
                } else {

                    currentScore = currentScore + 10 + turns[i + 1].getNbPins();
                    scores[i] = currentScore;
                }
                break;
        }
    }

}
