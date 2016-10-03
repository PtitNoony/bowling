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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Arnaud HAMON-KEROMEN
 */
public final class Sessions {

    private static final List<Session> SESSIONS = new LinkedList<>();

    private Sessions() {
        // private utility constructor
    }
    
    public static void addSession(Session session){
        SESSIONS.add(session);
    }
    
    public static List<Session> getSessions(){
        return Collections.unmodifiableList(SESSIONS);
    }

}
