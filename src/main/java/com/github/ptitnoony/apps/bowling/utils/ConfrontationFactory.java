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
package com.github.ptitnoony.apps.bowling.utils;

import com.github.ptitnoony.gameutils.Player;
import com.github.ptitnoony.apps.bowling.Confrontation;
import com.github.ptitnoony.apps.bowling.Round;
import com.github.ptitnoony.apps.bowling.Session;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author Arnaud HAMON-KEROMEN
 */
public class ConfrontationFactory {

    private static final Map<Integer, Confrontation> CONFRONTATIONS = new HashMap<>();

    private static final int ID_INCR = 3;

    private static int nextUniqueID = 1;

    private ConfrontationFactory() {
        // utility constructor
    }

    public static Confrontation createSession(LocalDate date) {
        while (CONFRONTATIONS.containsKey(nextUniqueID)) {
            nextUniqueID++;
        }
        final Confrontation confrontation = new ConfrontationImpl(date, nextUniqueID);
        CONFRONTATIONS.put(nextUniqueID, confrontation);
        incrementUniqueID();
        return confrontation;
    }

    public static List<Confrontation> getCreatedConfrontations() {
        // doc that time consuming method
        return CONFRONTATIONS.entrySet().stream().map(entry -> entry.getValue()).collect(Collectors.toList());
    }

    private static void incrementUniqueID() {
        nextUniqueID++;
        while (CONFRONTATIONS.containsKey(nextUniqueID)) {
            nextUniqueID += ID_INCR;
        }
    }

    private static class ConfrontationImpl implements Confrontation {

        private final LocalDate confrontationDate;
        private final List<Round> rounds;
        private final List<Player> players;
        private final int uniqueID;

        private Session session;

        private ConfrontationImpl(LocalDate date, int id) {
            confrontationDate = date;
            rounds = new LinkedList<>();
            players = new LinkedList<>();
            uniqueID = id;
        }

        @Override
        public void addRound(Round round) {
            round.setConfrontation(this);
            rounds.add(round);
            players.add(round.getPlayer());
        }

        @Override
        public List<Round> getRounds() {
            return Collections.unmodifiableList(rounds);
        }

        @Override
        public LocalDate getConfrontationDate() {
            return confrontationDate;
        }

        @Override
        public int getPlayerScore(Player p) {
            return getPlayerRound(p).getFinalScore();
        }

        @Override
        public Round getPlayerRound(Player p) {
            //TODO change data structure
            for (Round round : rounds) {
                if (round.getPlayer().equals(p)) {
                    return round;
                }
            }
            return null;
        }

        @Override
        public List<Player> getPlayers() {
            return Collections.unmodifiableList(players);
        }

        @Override
        public Session getSession() {
            return session;
        }

        @Override
        public void setSession(Session aSession) {
            session = aSession;
        }

        @Override
        public int getID() {
            return uniqueID;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            } else if (obj instanceof Confrontation) {
                return ((Confrontation) obj).getID() == uniqueID;
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 47 * hash + this.uniqueID;
            return hash;
        }

    }

}
