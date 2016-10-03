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
package fr.noony.games.bowling.analytics;

import fr.noony.games.bowling.Player;
import fr.noony.games.bowling.Round;
import fr.noony.games.bowling.Session;
import fr.noony.games.bowling.Sessions;
import fr.noony.games.bowling.Turn;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Arnaud HAMON-KEROMEN
 */
public class PlayerAnalytics {

    private final Player player;

    private final List<Session> sessions;
    private final List<Round> rounds;
    private final List<Integer> scores;

    private int nbSessions = 0;
    private int nbRounds = 0;
    private int minScore = -1;
    private int maxScore = 0;
    private int totalPoints = 0;
    private double averageScore = 0.0;

    public PlayerAnalytics(Player aplayer) {
        player = aplayer;
        sessions = new LinkedList<>();
        rounds = new LinkedList<>();
        scores = new LinkedList<>();
        recalculate();
    }

    public final void recalculate() {
        sessions.clear();
        rounds.clear();
        scores.clear();
        totalPoints = 0;
        minScore = -1;
        maxScore = 0;

        // retreive the sessions
        sessions.addAll(Sessions.getSessions().stream().filter(s -> s.getPlayers().contains(player)).collect(Collectors.toList()));
        nbSessions = sessions.size();

        //retreiving rounds
        sessions.forEach(s -> s.getConfrontations().forEach(c -> rounds.add(c.getPlayerRound(player))));

    }

}
