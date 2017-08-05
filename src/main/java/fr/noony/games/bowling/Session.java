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
package fr.noony.games.bowling;

import com.github.ptitnoony.gameutils.Player;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author Arnaud HAMON-KEROMEN
 */
public interface Session {

    void setDate(LocalDate date);

    void setLocation(String sessionLocation);

    LocalDate getSessionDate();

    String getLocation();

    void addConfrontation(Confrontation c);

    List<Confrontation> getConfrontations();

    void addPlayer(Player player);

    List<Player> getPlayers();

    int getTotalScore(Player p);

    //hum? or just in equals??
    int getSessionID();

}
