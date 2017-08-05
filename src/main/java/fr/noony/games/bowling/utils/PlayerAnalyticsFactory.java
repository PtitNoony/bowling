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
package fr.noony.games.bowling.utils;

import com.github.ptitnoony.gameutils.Player;
import fr.noony.games.bowling.Round;
import fr.noony.games.bowling.Session;
import fr.noony.games.bowling.analytics.PlayerAnalytics;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.scene.paint.Color;

/**
 *
 * @author Arnaud HAMON-KEROMEN
 */
public class PlayerAnalyticsFactory {

    private static final Map<Player, PlayerAnalytics> ANALYTICS = new HashMap<>();

    private PlayerAnalyticsFactory() {
        // utility constructor
    }

    public static PlayerAnalytics getPlayerAnalytics(Player player) {
        if (ANALYTICS.containsKey(player)) {
            return ANALYTICS.get(player);
        }
        final PlayerAnalytics playerAnalytics = new PlayerAnalyticsImpl(player);
        ANALYTICS.put(player, playerAnalytics);
        return playerAnalytics;
    }

    public static List<PlayerAnalytics> getCreatedPlayerAnalytics() {
        // doc that time consuming method
        return ANALYTICS.entrySet().stream().map(entry -> entry.getValue()).collect(Collectors.toList());
    }

    private static class PlayerAnalyticsImpl implements PlayerAnalytics {

        private final Player player;

        // TODO clean 
        private final List<Session> sessions;
        private final List<Round> rounds;
        private final List<Integer> scores;
        //
        // hum
        private Map<LocalDate, List<Round>> allRounds;

        private int nbSessions = 0;
        private int nbRounds = 0;
        private int minScore = -1;
        private int maxScore = 0;
        private int totalPoints = 0;
        private double averageScore = 0.0;
        //
        private int nbStrikes = 0;
        private int nbSpare = 0;
        private double averageSpares = 0.0;
        private double averageStrikes = 0.0;
        //
        private Color playerColor;

        private PlayerAnalyticsImpl(Player aplayer) {
            player = aplayer;
            sessions = new LinkedList<>();
            rounds = new LinkedList<>();
            scores = new LinkedList<>();
            playerColor = ColorFactory.getColor();
            recalculate();
        }

        @Override
        public final void recalculate() {
            sessions.clear();
            rounds.clear();
            scores.clear();
            totalPoints = 0;
            minScore = -1;
            maxScore = 0;

            // retreive the sessions
            sessions.addAll(SessionFactory.getCreatedSessions().stream().filter(s -> s.getPlayers().contains(player)).collect(Collectors.toList()));
            nbSessions = sessions.size();

            // retreiving rounds
            sessions.forEach(s -> s.getConfrontations().forEach(c -> rounds.add(c.getPlayerRound(player))));

            allRounds = new HashMap<>();
            sessions.forEach(s -> {
                LocalDate date = s.getSessionDate();
                s.getConfrontations().forEach(confrontation -> {
                    if (!allRounds.containsKey(date)) {
                        allRounds.put(date, new LinkedList<>());
                    }
                    allRounds.get(date).add(confrontation.getPlayerRound(player));
                });
            });

            // analyze each round
            int roundScore;
            for (Round r : rounds) {
                roundScore = r.getFinalScore();
                maxScore = Math.max(maxScore, roundScore);
                // hum..., could use Integer max instead as initial value
                if (minScore < 0) {
                    minScore = roundScore;
                } else {
                    minScore = Math.min(minScore, roundScore);
                }

                nbSpare += r.getNbSpare();
                nbStrikes += r.getNbStrikes();
                totalPoints += roundScore;
            }
            //
            nbRounds = rounds.size();
            //
            averageScore = (double) totalPoints / nbRounds;
            averageSpares = (double) nbSpare / nbRounds;
            averageStrikes = (double) nbStrikes / nbRounds;
        }

        @Override
        public Player getPlayer() {
            return player;
        }

        @Override
        public Map<LocalDate, List<Round>> getAllRounds() {
            return Collections.unmodifiableMap(allRounds);
        }

        @Override
        public Color getPlayerColor() {
            return playerColor;
        }

        @Override
        public void setPlayerColor(Color color) {
            playerColor = color;
        }

        @Override
        public int getMinScore() {
            return minScore;
        }

        @Override
        public double getAverageScore() {
            return averageScore;
        }

        @Override
        public int getMaxScore() {
            return maxScore;
        }

        @Override
        public double getAverageSpares() {
            return averageSpares;
        }

        @Override
        public double getAverageStrikes() {
            return averageStrikes;
        }

        @Override
        public String toString() {
            return "[" + player.getNickName() + "] avg:" + averageScore;
        }

    }

}
