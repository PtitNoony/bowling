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
package com.github.ptitnoony.apps.bowling.hmi.stats.comparestatscreen;

import com.github.ptitnoony.apps.bowling.Confrontation;
import java.time.LocalDate;
import java.util.List;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author ahamon
 */
public class DayDrawing {

    public static final double GAME_SEPARATION = 20;
    public static final double PADDING = 6;

    private final LocalDate date;
    private List<Confrontation> confrontations;
    private final int nbGames;
    private final double width;

    private final Group mainNode;
    private final Rectangle background;
    private final Line tickLine;

    private double height = 0;
    private double x = 0;

    public DayDrawing(LocalDate theDate, List<Confrontation> dayConfrontations) {
        date = theDate;
        confrontations = dayConfrontations;
        nbGames = confrontations.size();
        width = nbGames < 2 ? 2 * PADDING : 2 * PADDING + (nbGames - 1) * GAME_SEPARATION;
        // TODO static colors
        mainNode = new Group();
        background = new Rectangle();
        background.setFill(Color.SLATEGREY);
        background.setWidth(width);
        background.setOpacity(0.5);
        mainNode.getChildren().add(background);
        //
        tickLine = new Line(width / 2.0, -5, width / 2.0, 5);
        tickLine.setStroke(Color.WHITESMOKE);
        mainNode.getChildren().add(tickLine);
    }

    public Node getNode() {
        return mainNode;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setHeight(double newHeight) {
        height = newHeight;
        background.setHeight(height);
        background.setY(-height);
    }

    public void setX(double newX) {
        x = newX;
        mainNode.setTranslateX(x);
    }

    public double getX() {
        return x;
    }

    public void setY(double y) {
        mainNode.setTranslateY(y);
    }

    public double getWidth() {
        return width;
    }

}
