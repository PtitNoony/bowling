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
package fr.noony.games.bowling.hmi;

import static javafx.application.Platform.runLater;
import javafx.scene.Group;
import javafx.scene.Node;

/**
 *
 * @author Arnaud HAMON-KEROMEN
 */
public abstract class FxDrawing {

    /**
     * Default width used for turn drawings
     */
    public static final double DEFAULT_CELL_WIDTH = 60;
    /**
     * Default height used for turn drawings
     */
    public static final double DEFAULT_CELL_HEIGHT = 60;
    /**
     * Default width used for turn drawings
     */
    public static final double DEFAULT_EDITOR_CELL_WIDTH = 80;
    /**
     * Default height used for turn drawings
     */
    public static final double DEFAULT_EDITOR_CELL_HEIGHT = 80;
    /**
     * Default text for label with unset value
     */
    public static final String DEFAULT_TEXT = "-";

    public static final double CELL_NAME_RATIO = 3.0;

    public static final double PLAYER_NAME_WIDTH = DEFAULT_CELL_WIDTH * CELL_NAME_RATIO;

    private final Group mainNode;

    public FxDrawing() {
        mainNode = new Group();
    }

    public final void addNode(Node node) {
        runLater(() -> mainNode.getChildren().add(node));
    }

    public final void removeNode(Node node) {
        runLater(() -> mainNode.getChildren().remove(node));
    }

    public final Node getNode() {
        return mainNode;
    }

    public final void setTranslateX(double x) {
        runLater(() -> mainNode.setTranslateX(x));
    }

    public final void setTranslateY(double y) {
        runLater(() -> mainNode.setTranslateY(y));
    }

    public final void setTranslate(double x, double y) {
        runLater(() -> {
            mainNode.setTranslateX(x);
            mainNode.setTranslateY(y);
        });
    }

    /**
     *
     * @param visibility drawing visibility
     */
    public final void setVisible(boolean visibility) {
        runLater(() -> mainNode.setVisible(visibility));
        mainNode.requestFocus();
    }

    /**
     * MUST be called from within the JavaFX thread
     *
     * @param sX new width
     * @param sY new height
     */
    public abstract void setSize(double sX, double sY);

}
