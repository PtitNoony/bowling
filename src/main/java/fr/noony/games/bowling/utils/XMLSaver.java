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

import fr.noony.games.bowling.*;
import static fr.noony.games.bowling.utils.PlayerXmlUtils.NICK_NAME;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Arnaud HAMON-KEROMEN
 */
public class XMLSaver {

    public static final String BOWLINGSTAT_TAG = "bowlingStat";

    public static final String PLAYER_GROUP = "PLAYERS";
    public static final String PLAYER = "player";
    public static final String ID = "id";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";

    public static final String SESSION_GROUP = "SESSIONS";
    public static final String SESSION = "session";
    public static final String SESSION_LOCATION = "sessionLocation";
    public static final String SESSION_DATE = "sessionDate";
    public static final String CONFRONTATION = "confrontation";
    public static final String CONFRONTATION_DATE = "confrontationDate";
    public static final String GAME = "game";
    public static final String GAME_DATE = "gameDate";
    public static final String TURN = "turn";
    public static final String LAST_TURN = "lastTurn";
    public static final String SPARE = "spare";
    public static final String SPARE_3 = "spare_3";
    public static final String STRIKE = "strike";
    public static final String STRIKE_1 = "strike1";
    public static final String STRIKE_2 = "strike2";
    public static final String STRIKE_3 = "strike3";
    public static final String SPLIT = "split";
    public static final String SPLIT_1 = "split1";
    public static final String SPLIT_2 = "split2";
    public static final String SPLIT_3 = "split3";
    public static final String THROW = "throw";
    public static final String SCORE = "score";

    private static final Logger LOG = Logger.getGlobal();

    private XMLSaver() {
        //empty constructor
    }

    public static boolean save(File destFile) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement(BOWLINGSTAT_TAG);
            doc.appendChild(rootElement);

            // save players
            Element playerGroupElement = doc.createElement(PLAYER_GROUP);
            rootElement.appendChild(playerGroupElement);
            PlayerFactory.getCreatedPlayers().forEach(player -> playerGroupElement.appendChild(getPlayerXMLElement(doc, player)));

            // save sessions
            Element sessionGroupElement = doc.createElement(SESSION_GROUP);
            rootElement.appendChild(sessionGroupElement);
            SessionFactory.getCreatedSessions().forEach(session -> sessionGroupElement.appendChild(getSessionXMLElement(doc, session)));
            //
            rootElement.normalize();
            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(destFile);
            transformer.transform(source, result);
        } catch (ParserConfigurationException | TransformerException ex) {
            LOG.log(Level.SEVERE, " Exception while exporting atc geography :: {0}", ex);
            return false;
        }
        return true;
    }

    private static Node getPlayerXMLElement(Document doc, Player player) {
        Element playerElement = doc.createElement(PLAYER);
        playerElement.setAttribute(ID, Integer.toString(player.getID()));
        playerElement.setAttribute(FIRST_NAME, player.getFirstName());
        playerElement.setAttribute(LAST_NAME, player.getLastName());
        playerElement.setAttribute(NICK_NAME, player.getNickName());
        return playerElement;
    }

    private static Node getSessionXMLElement(Document document, Session session) {
        Element sessionElement = document.createElement(SESSION);
        sessionElement.setAttribute(SESSION_DATE, session.getSessionDate().toString());
        sessionElement.setAttribute(SESSION_LOCATION, session.getLocation());
        //create confrontation elements
        session.getConfrontations().forEach(c -> sessionElement.appendChild(getConfrontationXMLElement(document, c)));
        return sessionElement;
    }

    private static Node getConfrontationXMLElement(Document document, Confrontation confrontation) {
        Element confrontationElement = document.createElement(CONFRONTATION);
        confrontationElement.setAttribute(CONFRONTATION_DATE, confrontation.getConfrontationDate().toString());
        //create game elements
        confrontation.getRounds().forEach(r -> confrontationElement.appendChild(getGameXMLElement(document, r)));
        return confrontationElement;
    }

    private static Node getGameXMLElement(Document document, Round r) {
        Element gameElement = document.createElement(GAME);
        gameElement.setAttribute(PLAYER, Integer.toString(r.getPlayer().getID()));
        gameElement.setAttribute(SCORE, Integer.toString(r.getFinalScore()));
        //create confrontation elements
        for (int i = 0; i < 9; i++) {
            gameElement.appendChild(getTurnXMLElement(document, r.getTurns()[i]));
        }
        gameElement.appendChild(getLastTurnXMLElement(document, (LastTurn) r.getTurns()[9]));
        return gameElement;
    }

    private static Node getTurnXMLElement(Document document, Turn turn) {
        Element turnElement = document.createElement(TURN);
        // not every one will have only '0's
        turnElement.setAttribute(THROW + "_" + 1, Integer.toString(turn.getNbPinForThrow(1)));
        turnElement.setAttribute(THROW + "_" + 2, Integer.toString(turn.getNbPinForThrow(2)));
        turnElement.setAttribute(SPLIT, "" + turn.isSplit());
        return turnElement;
    }

    private static Node getLastTurnXMLElement(Document document, LastTurn lastTurn) {
        Element lastTurnElement = document.createElement(LAST_TURN);
        // not every one will have only '0's
        lastTurnElement.setAttribute(THROW + "_" + 1, Integer.toString(lastTurn.getNbPinForThrow(1)));
        lastTurnElement.setAttribute(THROW + "_" + 2, Integer.toString(lastTurn.getNbPinForThrow(2)));
        lastTurnElement.setAttribute(THROW + "_" + 3, Integer.toString(lastTurn.getNbPinForThrow(3)));
        lastTurnElement.setAttribute(SPLIT, Boolean.toString(lastTurn.isSplit()));
        lastTurnElement.setAttribute(SPLIT_2, Boolean.toString(lastTurn.isSecondBallSplit()));
        lastTurnElement.setAttribute(SPLIT_3, Boolean.toString(lastTurn.isThirdBallSplit()));
        return lastTurnElement;
    }

}
