/*
 * Copyright (C) 2017 Arnaud HAMON-KEROMEN
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
package fr.noony.games.bowling.hmi.stats.comparestatscreen;

import fr.noony.games.bowling.Player;
import fr.noony.games.bowling.Round;
import fr.noony.games.bowling.utils.ColorFactory;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 *
 * @author ahamon
 */
public class PlayerScoresDrawing {

    private final Player player;
    // hum
    private final Map<LocalDate, List<Round>> allRounds;

    private final List<ScoreTag> scores;

    private final Group mainNode;

    private Color color = ColorFactory.getColor();

    private double x = 0;
    private double y = 0;

    public PlayerScoresDrawing(Player aPlayer, Map<LocalDate, List<Round>> playedRounds) {
        player = aPlayer;
        allRounds = playedRounds;
        //
        mainNode = new Group();
        //
        scores = new LinkedList<>();
        allRounds.forEach((date, rounds) -> 
            rounds.stream().map(r -> new ScoreTag(date, r.getFinalScore(), color, rounds.indexOf(r))).forEachOrdered(scores::add)
        );
        mainNode.getChildren().addAll(scores.stream().map(s -> s.tag).collect(Collectors.toSet()));
    }

    public Node getNode() {
        return mainNode;
    }

    public void setX(double newX) {
        x = newX;
//        mainNode.setTranslateX(x);
    }

    public void setY(double newY) {
        y = newY;
        mainNode.setTranslateY(y);
    }

    public void updateRatios(long firstDay, double dayRatio, double pointRatio, Map<LocalDate, Double> xDates) {
        // first date in constructor?
        scores.forEach(tag -> {
            // TODO: cache in class
            long day = tag.date.toEpochDay();
            double tagX = xDates.get(tag.date) + tag.dayXOffset;
            double tagY = -pointRatio * tag.score;
            tag.setPosition(tagX, tagY);
        });
    }

    private static class ScoreTag {

        private final LocalDate date;
        private final int score;
        private final Circle tag;
        private final Color color;
        private final int index;
        private final double dayXOffset;

        public ScoreTag(LocalDate date, int score, Color c, int index) {
            this.date = date;
            this.score = score;
            this.index = index;
            tag = new Circle(5);
            color = c;
            tag.setFill(color);
            if (index == 0) {
                dayXOffset = 4 + 5;
            } else {
                dayXOffset = 4 + (index) * 20;
            }
        }

        private void setPosition(double x, double y) {
            tag.setCenterX(x);
            tag.setCenterY(y);
        }

    }
}
