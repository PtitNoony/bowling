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
package com.github.ptitnoony.apps.bowling;

/**
 *
 * @author Arnaud HAMON-KEROMEN
 */
public class SimpleTurn implements Turn {

    private final BallThrow throw1;
    private final BallThrow throw2;
    private final int nbPins;
    private final boolean isStrike;
    private final boolean isSpare;
    private final int nbThows;

    /**
     *
     * @param ball1 first ball throw
     * @param ball2 second ball throw
     */
    public SimpleTurn(BallThrow ball1, BallThrow ball2) {
        throw1 = ball1;
        throw2 = ball2;
        if (throw2 == null) {
            assert throw1.getValue() == BallThrow.NB_PINS;
            isStrike = true;
            isSpare = false;
            nbPins = BallThrow.NB_PINS;
            nbThows = 1;
        } else {
            isStrike = false;
            nbPins = throw1.getValue() + throw2.getValue();
            isSpare = nbPins == BallThrow.NB_PINS;
            assert nbPins <= BallThrow.NB_PINS;
            nbThows = 2;
        }
    }

    /**
     * Should only be used for strikes
     *
     * @param ball1 ball throw
     */
    public SimpleTurn(BallThrow ball1) {
        this(ball1, null);
    }

    @Override
    public int getNbThrows() {
        return nbThows;
    }

    @Override
    public boolean isStrike() {
        return isStrike;
    }

    @Override
    public boolean isSpare() {
        return isSpare;
    }

    @Override
    public int getNbPins() {
        return nbPins;
    }

    @Override
    public int getNbPinForThrow(int throwNumber) {
        switch (throwNumber) {
            case 0:
                return throw1.getValue();
            case 1:
                if (throw2 != null) {
                    return throw2.getValue();
                } else {
                    return 0;
                }
            default:
                throw new IllegalArgumentException("impossible to throw the ball " + throwNumber + " times");
        }
    }

    @Override
    public boolean isSplit() {
        return throw1.isSplit();
    }

}
