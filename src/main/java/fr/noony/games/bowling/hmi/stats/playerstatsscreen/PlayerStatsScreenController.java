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
package fr.noony.games.bowling.hmi.stats.playerstatsscreen;

import fr.noony.games.bowling.analytics.PlayerAnalytics;
import fr.noony.games.bowling.hmi.ScreenController;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author ahamon
 */
public class PlayerStatsScreenController implements ScreenController {

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
    }

    protected void refresh() {

    }

    protected void setPlayerAnalytics(PlayerAnalytics playerAnalytics) {
        System.err.println("HERE !! "+playerAnalytics.getPlayer().getNickName());
    }
}
