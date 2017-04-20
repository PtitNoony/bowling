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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 *
 * @author Arnaud HAMON-KEROMEN
 */
public class LastEditableTurnImpl implements LastEditableTurn {

    // TODO proper heritage
    private final PropertyChangeSupport propertyChangeSupport;

    private int ball1 = 0;
    private int ball2 = 0;
    private int ball3 = 0;
    private int nbThows = 0;
    private boolean is1Split = false;
    private boolean is2Split = false;
    private boolean is3Split = false;

    public LastEditableTurnImpl() {
        propertyChangeSupport = new PropertyChangeSupport(LastEditableTurnImpl.this);
    }

    @Override
    public int getNbThrows() {
        return nbThows;
    }

    @Override
    public boolean isStrike() {
        return ball1 == 10;
    }

    @Override
    public boolean isSpare() {
        return ball1 + ball2 == 10 && ball1 < 10;
    }

    @Override
    public int getNbPins() {
        return ball1 + ball2 + ball3;
    }

    @Override
    public boolean isSecondBallSpare() {
        return isSpare();
    }

    @Override
    public int getNbPinForThrow(int throwNumber) {
        switch (throwNumber) {
            case 1:
                return ball1;
            case 2:
                return ball2;
            case 3:
                return ball3;
            default:
                throw new IllegalArgumentException(" wrong throw number:: " + throwNumber);
        }
    }

    @Override
    public void setIsSplit(boolean isSplit) {
        is1Split = isSplit;
    }

    @Override
    public boolean isSplit() {
        return is1Split;
    }

    @Override
    public void setThrowValue(int throwNumber, int value, boolean isSplit) {
        //TODO test properly all combinations
        switch (throwNumber) {
            case 1:
                is1Split = isSplit;
                setBall1(value);
                break;
            case 2:
                is2Split = isSplit;
                setBall2(value);
                break;
            case 3:
                is3Split = isSplit;
                setBall3(value);
                break;
            default:
                throw new IllegalArgumentException("wrong throw number:: " + throwNumber);
        }
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    @Override
    public void setBall1(int b1) {
        ball1 = b1;
        if (ball1 == 10) {
            nbThows = 3;
        }
    }

    @Override
    public void setBall2(int b2) {
        //TODO test and clean
        if (ball1 != 10 && b2 + ball1 <= 10) {
            ball2 = b2;
            nbThows = 2;
        } else {
            ball2 = b2;
            nbThows = 3;
        }
    }

    @Override
    public void setBall3(int b3) {
        if (ball1 == 10 || ball1 + ball2 == 10) {
            ball3 = b3;
            nbThows = 3;
        }
    }

    @Override
    public void setSplit(boolean isSpilt) {
        is1Split = isSpilt;
    }

    @Override
    public boolean isSecondBallStrike() {
        return ball2 == 10;
    }

    @Override
    public boolean isSecondBallSplit() {
        return is2Split;
    }

    @Override
    public boolean isThirdBallStrike() {
        return ball3 == 10;
    }

    @Override
    public boolean isThirdBallSplit() {
        return is3Split;
    }

    @Override
    public boolean isThirdBallSpare() {
        if(isSecondBallSpare()){
            return false;
        }
        return ball3 != 10 && ball2 != 10 && ball2 + ball3 == 10;
    }

    @Override
    public void setBall1isSplit(boolean split) {
        is1Split = split;
    }

    @Override
    public void setBall2isSplit(boolean split) {
        is2Split = split;
    }

    @Override
    public void setBall3isSplit(boolean split) {
        is3Split = split;
    }

}
