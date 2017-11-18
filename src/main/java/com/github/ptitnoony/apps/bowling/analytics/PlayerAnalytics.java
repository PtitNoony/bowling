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
package com.github.ptitnoony.apps.bowling.analytics;

import com.github.ptitnoony.gameutils.Player;
import com.github.ptitnoony.apps.bowling.Round;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import javafx.scene.paint.Color;

/**
 *
 * @author Arnaud HAMON-KEROMEN
 */
public interface PlayerAnalytics {

    void recalculate();

    Player getPlayer();

    Map<LocalDate, List<Round>> getAllRounds();

    Color getPlayerColor();

    void setPlayerColor(Color color);

    int getMinScore();

    double getAverageScore();

    int getMaxScore();

    double getAverageSpares();

    double getAverageStrikes();

}
