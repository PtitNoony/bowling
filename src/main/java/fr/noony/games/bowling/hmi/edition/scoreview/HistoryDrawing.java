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
package fr.noony.games.bowling.hmi.edition.scoreview;

import fr.noony.games.bowling.hmi.ThrowLabel;
import fr.noony.games.bowling.analytics.PlayerAnalytics;
import static fr.noony.games.bowling.hmi.FxDrawing.CELL_NAME_RATIO;
import static fr.noony.games.bowling.hmi.FxDrawing.DEFAULT_CELL_HEIGHT;
import static fr.noony.games.bowling.hmi.FxDrawing.DEFAULT_CELL_WIDTH;
import java.util.LinkedList;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Arnaud HAMON-KEROMEN
 */
public class HistoryDrawing extends Pane {

    private final PlayerAnalytics playerAnalytics;
    private final Rectangle background;
    private final ThrowLabel nameLabel;
    private final ThrowLabel scoreLabel;
    private final ThrowLabel[] turnNumberLabels;
    private final List<PlayerGameDrawing> playerGameDrawings;

    public HistoryDrawing(PlayerAnalytics aPlayerAnalytics) {
        super();
        playerAnalytics = aPlayerAnalytics;
        background = new Rectangle();
        nameLabel = new ThrowLabel("Date");
        turnNumberLabels = new ThrowLabel[10];
        scoreLabel = new ThrowLabel("TOTAL");
        playerGameDrawings = new LinkedList<>();
        init();
    }

    private void init() {
        //TODO: use constants
        background.setFill(Color.GREY);
        background.setStroke(Color.BLACK);
        background.setStrokeWidth(6.0);
        //header
        addNode(background);
        addNode(nameLabel.getNode());
        nameLabel.setFill(Color.LIGHTGREY);
        for (int i = 0; i < turnNumberLabels.length; i++) {
            ThrowLabel turnNumberLabel = new ThrowLabel("" + (i + 1));
            turnNumberLabel.setFill(Color.LIGHTGREY);
            turnNumberLabels[i] = turnNumberLabel;
            addNode(turnNumberLabel.getNode());
        }
        addNode(scoreLabel.getNode());
        scoreLabel.setFill(Color.LIGHTGREY);
        //Players' rounds
        playerAnalytics.getAllRounds().entrySet().stream()
                .sorted((e1, e2) -> e1.getKey().compareTo(e2.getKey()))
                .map(e -> e.getValue())
                .forEach(
                        rounds -> rounds.forEach(
                                round -> {
                                    PlayerGameDrawing playerGameDrawing = new PlayerGameDrawing(round);
                                    playerGameDrawing.setDisplayMode(PlayerGameDrawing.DisplayMode.DATE);
                                    playerGameDrawings.add(playerGameDrawing);
                                    addNode(playerGameDrawing.getNode());
                                })
                );
        setPrefSize(DEFAULT_CELL_WIDTH * (10 + CELL_NAME_RATIO), DEFAULT_CELL_HEIGHT * (0.5 + playerGameDrawings.size()));
    }

    @Override
    protected void setWidth(double value) {
        super.setWidth(value);
        redraw();
    }

    @Override
    protected void setHeight(double value) {
        super.setHeight(value);
        redraw();
    }

    public void redraw() {
        //private constants
        int nbPlayers = playerGameDrawings.size();
        double newCellHeight = getHeight() / (nbPlayers + 0.5);
        double newCellWidth = getWidth() / (11 + CELL_NAME_RATIO);
        //background
        background.setWidth(getWidth());
        background.setHeight(getHeight());
        //first fine
        nameLabel.setPrefSize(newCellWidth * CELL_NAME_RATIO, newCellHeight / 2.0);
        for (int i = 0; i < turnNumberLabels.length; i++) {
            turnNumberLabels[i].setTranslateX((CELL_NAME_RATIO + i) * newCellWidth);
            turnNumberLabels[i].setPrefSize(newCellWidth, newCellHeight / 2.0);
        }
        scoreLabel.setPrefSize(newCellWidth, newCellHeight / 2.0);
        scoreLabel.setTranslateX((CELL_NAME_RATIO + 10) * newCellWidth);
        //player drawings
        playerGameDrawings.forEach(playerGameDrawing -> {
            playerGameDrawing.setSize(getWidth(), newCellHeight);
            playerGameDrawing.setTranslateY(newCellHeight / 2.0 + playerGameDrawings.indexOf(playerGameDrawing) * newCellHeight);
        });

    }

    private void addNode(Node node) {
        getChildren().add(node);
    }

}
