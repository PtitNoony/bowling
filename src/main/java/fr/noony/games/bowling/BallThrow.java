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

/**
 *
 * @author Arnaud HAMON-KEROMEN
 */
public class BallThrow {

    /**
     * Number of pins in a bowling game
     */
    public static final int NB_PINS = 10;

    private final boolean isStrike;
    private final boolean isSplit;
    private final int value;

    /**
     *
     * @param nbPins number of pins removed with the throw
     * @param split if the throw is a split
     */
    public BallThrow(int nbPins, boolean split) {
        value = nbPins;
        isSplit = split;
        isStrike = value == NB_PINS;
    }

    /**
     * Use this constructor only if no split has been made
     *
     * @param nbPins number of pins removed with the throw
     */
    public BallThrow(int nbPins) {
        this(nbPins, false);
    }

    /**
     *
     * @return number of pins removed with the throw
     */
    public int getValue() {
        return value;
    }

    /**
     *
     * @return if the throw is a split
     */
    public boolean isSplit() {
        return isSplit;
    }

    /**
     *
     * @return if the throw is a strike
     */
    public boolean isStrike() {
        return isStrike;
    }

}
