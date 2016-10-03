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
public class EditableTurnImpl implements EditableTurn {

    private final PropertyChangeSupport propertyChangeSupport;

    private int ball1 = 0;
    private int ball2 = 0;
    private int nbThrows = 0;
    private boolean isSplit = false;

    public EditableTurnImpl() {
        propertyChangeSupport = new PropertyChangeSupport(EditableTurnImpl.this);
    }

    @Override
    public int getNbThrows() {
        return nbThrows;
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
        return ball1 + ball2;
    }

    @Override
    public int getNbPinForThrow(int throwNumber) {
        switch (throwNumber) {
            case 1:
                return ball1;
            case 2:
                return ball2;
            default:
                throw new IllegalArgumentException(" wrong throw number:: " + throwNumber);
        }
    }

    @Override
    public void setIsSplit(boolean isSplit) {
        this.isSplit = isSplit;
    }

    @Override
    public boolean isSplit() {
        return isSplit;
    }

    @Override
    public void setThrowValue(int throwNumber, int value, boolean isSplit) {
        if (throwNumber == 1) {
            setBall1(value);
        } else if (throwNumber == 2) {
            //TODO test properly
            setBall2(value);
        }
        //TODO test properly
        this.isSplit = isSplit;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void setBall1(int b1) {
        ball1 = b1;
        if (ball1 == 10) {
            ball2 = 0;
            nbThrows = 1;
        }
    }

    public void setBall2(int b2) {
        if (ball1 != 10 && b2 + ball1 <= 10) {
            ball2 = b2;
            nbThrows = 2;
        }
    }

    public void setSplit(boolean spilt) {
        isSplit = spilt;
    }

    @Override
    public String toString() {
        return "turn () [" + ball1 + "," + ball2 + "] strike=" + isStrike();
    }

}
