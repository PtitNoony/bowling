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
public class SessionFactory {

    private static final Map<Integer, Session> SESSIONS = new HashMap<>();

    private static final int ID_INCR = 3;

    private static int nextUniqueID = 1;

    private SessionFactory() {
        // utility constructor
    }

    public static Session createSession(LocalDate date, String aLocation) {
        while (SESSIONS.containsKey(nextUniqueID)) {
            nextUniqueID++;
        }
        final Session session = new SessionImpl(date, aLocation, nextUniqueID);
        SESSIONS.put(nextUniqueID, session);
        incrementUniqueID();
        return session;
    }

    public static Session createSession(LocalDate date) {
        return createSession(date, "");
    }

    public static List<Session> getCreatedSessions() {
        // doc that time consuming method
        return SESSIONS.entrySet().stream().map(entry -> entry.getValue()).collect(Collectors.toList());
    }

    private static void incrementUniqueID() {
        nextUniqueID++;
        while (SESSIONS.containsKey(nextUniqueID)) {
            nextUniqueID += ID_INCR;
        }
    }

    private static class SessionImpl implements Session {

        private final List<Confrontation> confrontations;
        // todo is it ok?
        private final List<Player> players;
        private final int sessionID;

        //todo use it as an interface?
        private LocalDate sessionDate;
        private String location;

        private SessionImpl(LocalDate date, String aLocation, int id) {
            sessionDate = date;
            location = aLocation;
            confrontations = new LinkedList<>();
            players = new LinkedList<>();
            sessionID = id;
        }

        private SessionImpl(LocalDate date, int id) {
            this(date, "", id);
        }

        //todo
        private SessionImpl(int id) {
            confrontations = new LinkedList<>();
            players = new LinkedList<>();
            sessionID = id;
        }

        @Override
        public void setDate(LocalDate date) {
            sessionDate = date;
        }

        @Override
        public void setLocation(String sessionLocation) {
            location = sessionLocation;
        }

        @Override
        public LocalDate getSessionDate() {
            return sessionDate;
        }

        @Override
        public String getLocation() {
            return location;
        }

        @Override
        public void addConfrontation(Confrontation c) {
            c.setSession(this);
            confrontations.add(c);
            c.getRounds().forEach(round -> {
                if (!players.contains(round.getPlayer())) {
                    players.add(round.getPlayer());
                }
            });
        }

        @Override
        public List<Confrontation> getConfrontations() {
            return confrontations;
        }

        @Override
        public void addPlayer(Player player) {
            if (!players.contains(player)) {
                players.add(player);
            }
        }

        @Override
        public List<Player> getPlayers() {
            return Collections.unmodifiableList(players);
        }

        @Override
        public int getTotalScore(Player p) {
            int score = 0;
            for (int i = 0; i < confrontations.size(); i++) {
                score += confrontations.get(i).getPlayerScore(p);
            }
            return score;
        }

        @Override
        public int getSessionID() {
            return sessionID;
        }

        @Override
        public String toString() {
            return "Session: " + players.size() + " players on " + sessionDate + " @ " + location;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            } else if (obj instanceof Session) {
                return ((Session) obj).getSessionID() == sessionID;
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 37 * hash + this.sessionID;
            return hash;
        }

    }

}
