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

import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Arnaud HAMON-KEROMEN
 */
public class Confrontation {

    private final LocalDate confrontationDate;
    private final List<Round> rounds;
    private final List<Player> players;

    public Confrontation(LocalDate date) {
        confrontationDate = date;
        rounds = new LinkedList<>();
        players = new LinkedList<>();
    }

    public void addRound(Round round) {
        rounds.add(round);
        players.add(round.getPlayer());
    }

    public List<Round> getRounds() {
        return Collections.unmodifiableList(rounds);
    }

    public LocalDate getConfrontationDate() {
        return confrontationDate;
    }

    public int getPlayerScore(Player p) {
        return getPlayerRound(p).getFinalScore();
    }

    public Round getPlayerRound(Player p) {
        //TODO change data structure
        for (Round round : rounds) {
            if (round.getPlayer().equals(p)) {
                return round;
            }
        }
        return null;
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

}
