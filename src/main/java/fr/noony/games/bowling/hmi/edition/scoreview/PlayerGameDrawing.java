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


import fr.noony.games.bowling.hmi.FxDrawing;
import fr.noony.games.bowling.hmi.ThrowLabel;
import fr.noony.games.bowling.LastTurn;
import fr.noony.games.bowling.Round;
import fr.noony.games.bowling.EditableTurn;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 *
 * @author Arnaud HAMON-KEROMEN
 */
public class PlayerGameDrawing extends FxDrawing {

    private final Rectangle background;
    private final ThrowLabel playerNameLabel;
    private final TurnDrawing[] turnDrawings;
    private final LastTurnDrawing lastTurnDrawing;
    private final ThrowLabel scoreLabel;
    private final Round game;

    public PlayerGameDrawing(Round g) {
        super();
        game = g;
        background = new Rectangle();
        playerNameLabel = new ThrowLabel(game.getPlayer().getNickName());
        /// todo: use constant for font
        playerNameLabel.setFont(Font.font(null, FontWeight.BOLD, 16));
        turnDrawings = new TurnDrawing[9];
        lastTurnDrawing = new LastTurnDrawing(game, (LastTurn) game.getTurns()[9]);
        scoreLabel = new ThrowLabel(Integer.toString(game.getFinalScore()));
        scoreLabel.setFont(Font.font(null, FontWeight.BOLD, 16));
        init();
    }

    private void init() {
        background.setFill(Color.GHOSTWHITE);
        background.setStroke(Color.BLACK);
        background.setStrokeWidth(3.0);
        addNode(background);
        addNode(playerNameLabel.getNode());
        for (int i = 0; i < turnDrawings.length; i++) {
            TurnDrawing turnDrawing;
            //TODO
            if (game != null) {
                turnDrawing = new TurnDrawing(game, (EditableTurn) game.getTurns()[i],i);
            } else {
                turnDrawing = new TurnDrawing(null, null,i);
            }
            turnDrawings[i] = turnDrawing;
            addNode(turnDrawing.getNode());
            turnDrawing.setTranslateX(PLAYER_NAME_WIDTH + i * DEFAULT_CELL_WIDTH);
        }
        addNode(lastTurnDrawing.getNode());
        lastTurnDrawing.setTranslateX(PLAYER_NAME_WIDTH + 9 * DEFAULT_CELL_WIDTH);
        addNode(scoreLabel.getNode());
        scoreLabel.setTranslateX(PLAYER_NAME_WIDTH + 10 * DEFAULT_CELL_WIDTH);
        scoreLabel.setPrefSize(DEFAULT_CELL_WIDTH, DEFAULT_CELL_HEIGHT);
        //TODO: use set size instead
    }

    @Override
    public void setSize(double sX, double sY) {
        background.setWidth(sX);
        background.setHeight(sY);
        double newCellWidth = sX / (11 + CELL_NAME_RATIO);
        playerNameLabel.setPrefSize(newCellWidth * CELL_NAME_RATIO, sY);
        for (int i = 0; i < turnDrawings.length; i++) {
            turnDrawings[i].setSize(newCellWidth, sY);
            turnDrawings[i].setTranslateX(newCellWidth * (i + CELL_NAME_RATIO));
        }
        lastTurnDrawing.setSize(newCellWidth, sY);
        lastTurnDrawing.setTranslateX(newCellWidth * (9 + CELL_NAME_RATIO));
        scoreLabel.setTranslateX((10 + CELL_NAME_RATIO) * newCellWidth);
        scoreLabel.setPrefSize(newCellWidth, sY);
    }

}
