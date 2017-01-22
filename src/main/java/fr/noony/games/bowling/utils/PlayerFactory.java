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

import fr.noony.games.bowling.Player;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *
 * @author Arnaud HAMON-KEROMEN
 */
public class PlayerFactory {

    private static final Map<Integer, Player> PLAYERS = new HashMap<>();

    private static final int ID_INCR = 7;

    private static int nextUniqueID = 1;

    private PlayerFactory() {
        // private utility constructor
    }

    public static List<Player> getCreatedPlayers() {
        // doc that time consuming method
        return PLAYERS.entrySet().stream().map(entry -> entry.getValue()).collect(Collectors.toList());
    }

    public static Player createPlayer(String firstName, String lastName, String nickName) {
        while(PLAYERS.containsKey(nextUniqueID)){
            nextUniqueID++;
        }
        final Player player = new PlayerImpl(nextUniqueID, firstName, lastName, nickName);
        PLAYERS.put(nextUniqueID, player);
        incrementUniqueID();
        return player;
    }

    public static Player createPlayer(int id, String firstName, String lastName, String nickName) {
        if (PLAYERS.containsKey(id)) {
            //TODO: message
            throw new IllegalStateException("Error");

        }
        final Player player = new PlayerImpl(id, firstName, lastName, nickName);
        PLAYERS.put(id, player);
        return player;
    }

    public static Player getPlayerFromID(int playerID) {
        return PLAYERS.get(playerID);
    }
    
    public static boolean areValidPlayerAttibutes(String firstName, String lastName, String nickName){
        //TODO: test existence
        return !firstName.trim().isEmpty() && ! lastName.trim().isEmpty() && ! nickName.trim().isEmpty();
    }

    private static void incrementUniqueID() {
        nextUniqueID++;
        while (PLAYERS.containsKey(nextUniqueID)) {
            nextUniqueID += ID_INCR;
        }
    }

    private static class PlayerImpl implements Player {

        private final int pUniqueID;
        private final String pFirstName;
        private final String pLastName;
        private final String pNickName;

        public PlayerImpl(int uniqueID, String firstName, String lastName, String nickName) {
            pUniqueID = uniqueID;
            pFirstName = firstName;
            pLastName = lastName;
            pNickName = nickName;
        }

        @Override
        public int getID() {
            return pUniqueID;
        }

        @Override
        public String getFirstName() {
            return pFirstName;
        }

        @Override
        public String getLastName() {
            return pLastName;
        }

        @Override
        public String getNickName() {
            return pNickName;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 97 * hash + this.pUniqueID;
            hash = 97 * hash + Objects.hashCode(this.pFirstName);
            hash = 97 * hash + Objects.hashCode(this.pLastName);
            hash = 97 * hash + Objects.hashCode(this.pNickName);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final PlayerImpl other = (PlayerImpl) obj;
            if (this.pUniqueID != other.pUniqueID) {
                return false;
            }
            if (!Objects.equals(this.pFirstName, other.pFirstName)) {
                return false;
            }
            if (!Objects.equals(this.pLastName, other.pLastName)) {
                return false;
            }
            return Objects.equals(this.pNickName, other.pNickName);
        }

        @Override
        public String toString() {
            return pFirstName + " " + pLastName + " ("+pNickName+")";
        }

    }
}
