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

import fr.noony.games.bowling.Confrontation;
import fr.noony.games.bowling.Player;
import fr.noony.games.bowling.Round;
import fr.noony.games.bowling.analytics.PlayerAnalytics;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author ahamon
 */
public class ComparisonChart {

    private static final Color AXIS_COLOR = Color.WHITESMOKE;
    private static final Color SCALE_LINES_COLOR = Color.DARKGREY;
    private static final double AXIS_STROKE_WIDTH = 2.5;
    private static final int PADDING = 15;
    private static final int SCORE_HEIGHT = 10;
    //hum..
    private static final int TEXT_WIDTH = 40;
    private static final int TEXT_HEIGHT = 25;

    private final Group mainNode;
    // 
    private final Rectangle background;
    private final Group dataGroup;
//    private final Group 
    //
    private List<DayDrawing> dayDrawings = new LinkedList<>();
    private List<PlayerScoresDrawing> playerScoresDrawings = new LinkedList<>();
    //
    private Line xAxis;
    private Line yAxis;
    //
    private Line x100;
    private Line x200;
    private Line x300;

    private double width = 0;
    private double height = 0;
    private double pointRatio = 0;
    private double dayRatio = 0;

    public ComparisonChart() {
        mainNode = new Group();
        background = new Rectangle();
        mainNode.getChildren().add(background);
        initLines();
        dataGroup = new Group();
        mainNode.getChildren().add(dataGroup);
    }

    public Node getNode() {
        return mainNode;
    }

    public void setWidth(double newWidth) {
        width = newWidth;
        updateSize();
    }

    public void setHeight(double newHeight) {
        height = newHeight;
        updateSize();
    }

    private void updateSize() {
        // not optimized
        background.setWidth(width);
        background.setHeight(height);
        //
        xAxis.setEndX(width - PADDING);
        xAxis.setStartY(height - PADDING - TEXT_HEIGHT);
        xAxis.setEndY(height - PADDING - TEXT_HEIGHT);
        //
        yAxis.setEndY(height - PADDING);
        //
        pointRatio = (height - 2 * PADDING - TEXT_HEIGHT - SCORE_HEIGHT) / 300.0;
        //
        x100.setEndX(width - PADDING);
        x100.setStartY(height - PADDING - TEXT_HEIGHT - 100 * pointRatio);
        x100.setEndY(height - PADDING - TEXT_HEIGHT - 100 * pointRatio);
        //
        x200.setEndX(width - PADDING);
        x200.setStartY(height - PADDING - TEXT_HEIGHT - 200 * pointRatio);
        x200.setEndY(height - PADDING - TEXT_HEIGHT - 200 * pointRatio);
        //
        x300.setEndX(width - PADDING);
        x300.setStartY(height - PADDING - TEXT_HEIGHT - 300 * pointRatio);
        x300.setEndY(height - PADDING - TEXT_HEIGHT - 300 * pointRatio);
        //
        updateDataGraphics();
    }

    private void initLines() {
        xAxis = new Line();
        xAxis.setStroke(AXIS_COLOR);
        xAxis.setStrokeWidth(AXIS_STROKE_WIDTH);
        xAxis.setStartX(PADDING);
        //
        yAxis = new Line();
        yAxis.setStroke(AXIS_COLOR);
        yAxis.setStrokeWidth(AXIS_STROKE_WIDTH);
        yAxis.setStartX(PADDING + TEXT_WIDTH);
        yAxis.setEndX(PADDING + TEXT_WIDTH);
        yAxis.setStartY(PADDING);
        //
        x100 = new Line();
        x100.setStroke(SCALE_LINES_COLOR);
        x100.setStrokeDashOffset(5);
        x100.setStartX(PADDING + TEXT_WIDTH);
        //
        x200 = new Line();
        x200.setStroke(SCALE_LINES_COLOR);
        x200.setStrokeDashOffset(5);
        x200.setStartX(PADDING + TEXT_WIDTH);
        //
        x300 = new Line();
        x300.setStroke(SCALE_LINES_COLOR);
        x300.setStrokeDashOffset(5);
        x300.setStartX(PADDING + TEXT_WIDTH);
        //
        mainNode.getChildren().addAll(x100, x200, x300, xAxis, yAxis);
    }

    public void setData(List<PlayerAnalytics> listAnalytics, Map<LocalDate, List<Confrontation>> confrontations) {
        // TODO remove all
        // not optimized, nor at the right place
        dayDrawings = new LinkedList<>();
        // humm
        Map<Player, Map<LocalDate, List<Round>>> allPlayersRounds = new HashMap<>();

        listAnalytics.forEach(playerAnalytics -> {
            allPlayersRounds.put(playerAnalytics.getPlayer(), new HashMap<>());
        });

        confrontations.forEach((date, list) -> {
            DayDrawing dayDrawing = new DayDrawing(date, list);
            dataGroup.getChildren().add(dayDrawing.getNode());
            dayDrawings.add(dayDrawing);
        });

        dayDrawings.sort(this::compareDay);

        //
        playerScoresDrawings = new LinkedList<>();
        // TODO optimize and merge

        //
        listAnalytics.forEach(pA -> {
            PlayerScoresDrawing playerScoresDrawing = new PlayerScoresDrawing(pA);
            dataGroup.getChildren().add(playerScoresDrawing.getNode());
            playerScoresDrawings.add(playerScoresDrawing);
        });
        //
        updateDataGraphics();

    }

    // TODO put somewhere
    private int compareDay(DayDrawing d1, DayDrawing d2) {
        return Long.compare(d1.getDate().toEpochDay(), d2.getDate().toEpochDay());
    }

    private void updateDataGraphics() {
        DayDrawing dayDrawing;
        double daysWidth = 0;
        LocalDate minDate = LocalDate.MAX;
        LocalDate maxDate = LocalDate.MIN;
        //        
        double xOffset = PADDING + TEXT_WIDTH;
        //TODO use a method for that
        for (int i = 0; i < dayDrawings.size(); i++) {
            dayDrawing = dayDrawings.get(i);
            //TODO cache
            dayDrawing.setHeight(300 * pointRatio);
            //TODO change to only modify group node
            dayDrawing.setY(height - PADDING - TEXT_HEIGHT);
            daysWidth += dayDrawing.getWidth();
            if (dayDrawing.getDate().isBefore(minDate)) {
                minDate = dayDrawing.getDate();
            } else if (dayDrawing.getDate().isAfter(maxDate)) {
                maxDate = dayDrawing.getDate();
            }
        }
        //
        long nbDays = maxDate.toEpochDay() - minDate.toEpochDay();
        long firstDay = minDate.toEpochDay();
        //
        double availableWidth = width - 2 * PADDING - TEXT_WIDTH - daysWidth;
        dayRatio = availableWidth / nbDays;
        double addedDaysWidth = 0;
        for (int i = 0; i < dayDrawings.size(); i++) {
            dayDrawing = dayDrawings.get(i);
            long day = dayDrawing.getDate().toEpochDay();
            dayDrawing.setX(xOffset + addedDaysWidth + (day - firstDay) * dayRatio);
            addedDaysWidth += dayDrawing.getWidth();
        }
        // get the X ratio for each day
        //TODO: could be done inside previous loop
        Map<LocalDate, Double> xDates = new HashMap<>();
        for (int i = 0; i < dayDrawings.size(); i++) {
            xDates.put(dayDrawings.get(i).getDate(), dayDrawings.get(i).getX());
        }
        xDates = Collections.unmodifiableMap(xDates);

        // TODO use functional
        PlayerScoresDrawing playerScoresDrawing;
        for (int i = 0; i < playerScoresDrawings.size(); i++) {
            playerScoresDrawing = playerScoresDrawings.get(i);
            playerScoresDrawing.setX(xOffset);
            playerScoresDrawing.setY(height - PADDING - TEXT_HEIGHT);
            playerScoresDrawing.updateRatios(firstDay, dayRatio, pointRatio, xDates);
        }
    }

}
