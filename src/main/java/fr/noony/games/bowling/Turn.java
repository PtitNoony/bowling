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
public interface Turn {

    /**
     *
     * @return number of times the ball has been thrown during the turn
     */
    int getNbThrows();

    /**
     *
     * @return is the first throw made a strike
     */
    boolean isStrike();

    /**
     *
     * @return if both first throws combined made a spare
     */
    boolean isSpare();

    /**
     *
     * @return the amount at pins removed during the turn
     */
    int getNbPins();

    /**
     *
     * @param throwNumber the throw considered
     * @return the amount at pins removed during the corresponding throw
     */
    int getNbPinForThrow(int throwNumber);

    /**
     *
     * @return if a split occurred in the turn
     */
    boolean isSplit();

}
