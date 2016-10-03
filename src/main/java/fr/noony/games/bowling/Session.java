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
public class Session {

    private final List<Confrontation> confrontations;
    // todo is it ok?
    private final List<Player> players;

    //todo use it as an interface?
    private LocalDate sessionDate;
    private String location;

    public Session(LocalDate date, String aLocation) {
        sessionDate = date;
        location = aLocation;
        confrontations = new LinkedList<>();
        players = new LinkedList<>();
    }

    public Session(LocalDate date) {
        this(date, "");
    }

    //todo
    public Session() {
        confrontations = new LinkedList<>();
        players = new LinkedList<>();
    }

    public void setDate(LocalDate date) {
        sessionDate = date;
    }

    public void setLocation(String sessionLocation) {
        location = sessionLocation;
    }

    public LocalDate getSessionDate() {
        return sessionDate;
    }

    public String getLocation() {
        return location;
    }

    public void addConfrontation(Confrontation c) {
        confrontations.add(c);
        c.getRounds().forEach(round -> {
            if (!players.contains(round.getPlayer())) {
                players.add(round.getPlayer());
            }
        });
    }

    public List<Confrontation> getConfrontations() {
        return confrontations;
    }

    public void addPlayer(Player player) {
        if (!players.contains(player)) {
            players.add(player);
        }
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public int getTotalScore(Player p) {
        int score = 0;
        for (int i = 0; i < confrontations.size(); i++) {
            score += confrontations.get(i).getPlayerScore(p);
        }
        return score;
    }

    @Override
    public String toString() {
        return "Session: " + players.size() + " players on " + sessionDate + " @ " + location;
    }

}
