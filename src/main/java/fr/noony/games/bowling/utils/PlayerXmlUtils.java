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
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author Arnaud HAMON-KEROMEN
 */
public class PlayerXmlUtils {

    public static final String PLAYER_ELEMENT = "PLAYER";

    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String NICK_NAME = "nickName";
    public static final String ID = "id";

    private static final Logger LOG = Logger.getGlobal();

    private PlayerXmlUtils() {
        // private utility class
    }

    public static Player parsePlayer(Element playerElement) {
        String firstName = playerElement.getAttribute(FIRST_NAME);
        String lastName = playerElement.getAttribute(LAST_NAME);
        String nickName = playerElement.getAttribute(NICK_NAME);
        if (playerElement.hasAttribute(ID)) {
            int id = Integer.parseInt(playerElement.getAttribute(ID));
            return PlayerFactory.createPlayer(id, firstName, lastName, nickName);
        }
        return PlayerFactory.createPlayer(firstName, lastName, nickName);
    }

    public static void createPlayerXmlElement(Document doc, Element parentElement, Player player) {
        Element playerElement = doc.createElement(PLAYER_ELEMENT);
        playerElement.setAttribute(ID, Integer.toString(player.getID()));
        playerElement.setAttribute(FIRST_NAME, player.getFirstName());
        playerElement.setAttribute(LAST_NAME, player.getLastName());
        parentElement.appendChild(playerElement);
    }

    public static List<Player> parsePlayersFile(File file) {
        DocumentBuilderFactory builderFactory;
        builderFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder docBuilder = builderFactory.newDocumentBuilder();
            InputSource source = new InputSource(file.getAbsolutePath());
            Document document = docBuilder.parse(source);
            Element e = document.getDocumentElement();
            NodeList nodes = e.getElementsByTagName(PLAYER_ELEMENT);
            List<Player> result = new LinkedList<>();
            for (int i = 0; i < nodes.getLength(); i++) {
                result.add(parsePlayer((Element) nodes.item(i)));
            }
            return Collections.unmodifiableList(result);
        } catch (ParserConfigurationException e) {
            LOG.log(Level.SEVERE, "Error while creating document builder {0}", e);
        } catch (SAXException | IOException ex) {
            LOG.log(Level.SEVERE, "Exception parsing players xml file:: {0}", ex);
        }
        return Collections.emptyList();
    }

}
