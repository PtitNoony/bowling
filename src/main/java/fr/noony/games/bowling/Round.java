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

import com.github.ptitnoony.gameutils.Player;

/**
 *
 * @author Arnaud HAMON-KEROMEN
 */
public interface Round {

    /**
     *
     * @return the scores of each turn
     */
    int[] getScores();

    /**
     * Notice: if the game is not finished yet, the score will not be accurate
     * if strikes or spare need next turns to calculate the score
     *
     * @return the score of the game
     */
    public int getFinalScore();

    /**
     *
     * @return the player
     */
    Player getPlayer();

    Turn[] getTurns();
    
    int getNbStrikes();
    
    int getNbSpare();
    
    void setConfrontation(Confrontation confrontation);
    
    Confrontation getConfrontation();

}
