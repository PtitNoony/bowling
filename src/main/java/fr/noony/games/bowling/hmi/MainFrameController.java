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

import fr.noony.games.bowling.utils.XMLLoader;
import fr.noony.games.bowling.Player;
import fr.noony.games.bowling.Session;
import fr.noony.games.bowling.EditablePlayerRound;
import fr.noony.games.bowling.hmi.edition.confrontationcreatorscreen.ConfrontationCreatorScreen;
import fr.noony.games.bowling.hmi.edition.homescreen.HomeScreen;
import fr.noony.games.bowling.hmi.edition.scoreview.SessionScoreViewerScreen;
import fr.noony.games.bowling.hmi.edition.sessioncreatorscreen.SessionCreatorScreen;
import fr.noony.games.bowling.utils.XMLSaver;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 *
 * @author Arnaud HAMON-KEROMEN
 */
public class MainFrameController implements Initializable {

    private enum ApplicationMode {
        EDITION, STATISTICS
    }

    private enum EditionState {
        HOME, PLAYER_CREATOR, CONFRONTATION_CREATOR, SESSION_CREATOR, SESSION_VIEWER
    }

    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(MainFrameController.this);

    private final FileChooser fileChooser = new FileChooser();

    @FXML
    private AnchorPane mainContentPane;

    // Mode selection
    @FXML
    private ToggleButton editionModeTB;
    @FXML
    private ToggleButton statisticsModeTB;

    private ApplicationMode applicationMode;

    private EditionState previousEditionState;
    private EditionState currentEditionState;

    private HomeScreen homeScreen;
    private SessionCreatorScreen sessionCreatorScreen;
    private ConfrontationCreatorScreen confrontationCreatorScreen;
    private SessionScoreViewerScreen scoreViewerScreen;

    private Session currentSession;

    private Screen currentScreen;

    /**
     * Initializes the controller class.
     *
     * @param url location
     * @param rb resource bundle
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //TODO: place it where it belongs and with a progress update
        //PlayerXmlUtils.parsePlayersFile(new File(Player.class.getResource("players.xml").getPath()));
        //PlayerFactory.getCreatedPlayers().forEach(player -> System.err.println(player));
//        XMLLoader.loadFile(new File(Player.class.getResource("TestBowling.xml").getPath()));
        ToggleGroup toggleGroup = new ToggleGroup();

        //TODO: properly
        editionModeTB.setToggleGroup(toggleGroup);
        statisticsModeTB.setToggleGroup(toggleGroup);
        editionModeTB.setSelected(true);
        setApplicationMode(ApplicationMode.EDITION);

        setHome();
        //
    }

    @FXML
    protected void onLoadAction(ActionEvent event) {
        File sourceFile = fileChooser.showOpenDialog(mainContentPane.getScene().getWindow());
        if (sourceFile != null) {
            XMLLoader.loadFile(sourceFile);
            currentScreen.refresh();
        }
    }

    @FXML
    protected void onSaveAction(ActionEvent event) {
        File destFile = fileChooser.showSaveDialog(mainContentPane.getScene().getWindow());
        if (destFile != null) {
            XMLSaver.save(destFile);
        }
    }

    private void setApplicationMode(ApplicationMode newMode) {
        applicationMode = newMode;
    }

    private void setHome() {
        previousEditionState = EditionState.HOME;
        currentEditionState = EditionState.HOME;
        loadHomeScreen();
    }

    private void loadHomeScreen() {
        if (homeScreen == null) {
            homeScreen = new HomeScreen();
            homeScreen.addPropertyChangeListener(this::handleHomeEvents);
        }
        setAnchorPaneConstant(homeScreen.getMainNode());
        currentScreen = homeScreen;
        homeScreen.refresh();
    }

    private void loadSessionScreenCreator() {
        if (sessionCreatorScreen == null) {
            sessionCreatorScreen = new SessionCreatorScreen();
            sessionCreatorScreen.addPropertyChangeListener(this::handleSessionCreatorEvents);
        }
        setAnchorPaneConstant(sessionCreatorScreen.getMainNode());
        currentScreen = sessionCreatorScreen;
    }

    private void loadSessionViewerScreen(Session session) {
        if (scoreViewerScreen == null) {
            scoreViewerScreen = new SessionScoreViewerScreen();
            scoreViewerScreen.addPropertyChangeListener(this::handleScoreViewEvents);
        }
        setAnchorPaneConstant(scoreViewerScreen.getMainNode());
        scoreViewerScreen.displaySession(session);
        currentScreen = scoreViewerScreen;
    }

    private void loadConfrontationCreator(Session session) {
        if (confrontationCreatorScreen == null) {
            confrontationCreatorScreen = new ConfrontationCreatorScreen();
            confrontationCreatorScreen.addPropertyChangeListener(this::handleConfrontationCreatorEvents);
        }
        confrontationCreatorScreen.clear();
        confrontationCreatorScreen.setSessionInProgress(session);
        setAnchorPaneConstant(confrontationCreatorScreen.getMainNode());
        currentScreen = confrontationCreatorScreen;
    }

    //
    //// methods to handle screen changes
    //
    private void handleHomeEvents(PropertyChangeEvent event) {
        if (currentEditionState != EditionState.HOME) {
            throw new IllegalStateException("Should not receive events from HOME while in state " + currentEditionState);
        }
        previousEditionState = EditionState.HOME;
        switch (event.getPropertyName()) {
            case ScreenEvents.OPEN_SESSION_CREATOR:
                currentEditionState = EditionState.SESSION_CREATOR;
                loadSessionScreenCreator();
                break;
            case ScreenEvents.OPEN_SESSION_VIEWER:
                Session s = (Session) event.getNewValue();
                currentEditionState = EditionState.SESSION_VIEWER;
                loadSessionViewerScreen(s);
                break;
            default:
                throw new UnsupportedOperationException("Unsupported event on HOME page:: " + event.getPropertyName());
        }

    }

    private void handleScoreViewEvents(PropertyChangeEvent event) {
        if (currentEditionState != EditionState.SESSION_VIEWER) {
            throw new IllegalStateException("Should not receive events from score viewer while in state " + currentEditionState);
        }
        previousEditionState = EditionState.SESSION_VIEWER;
        switch (event.getPropertyName()) {
            case ScreenEvents.BACK_HOME:
                currentEditionState = EditionState.HOME;
                loadHomeScreen();
                break;
            default:
                throw new UnsupportedOperationException("Unsupported event on HOME page:: " + event.getPropertyName());
        }
    }

    private void handleSessionCreatorEvents(PropertyChangeEvent event) {
        if (currentEditionState != EditionState.SESSION_CREATOR) {
            throw new IllegalStateException("Should not receive events from session creator while in state " + currentEditionState);
        }
        previousEditionState = EditionState.SESSION_CREATOR;
        switch (event.getPropertyName()) {
            case ScreenEvents.CANCEL:
                currentEditionState = EditionState.HOME;
                loadHomeScreen();
                break;
            case ScreenEvents.OPEN_CONFRONTATION_CREATOR:
                currentEditionState = EditionState.CONFRONTATION_CREATOR;
                currentSession = (Session) event.getNewValue();
                loadConfrontationCreator(currentSession);
                break;
            case ScreenEvents.MAIN_SCREEN:
                currentEditionState = EditionState.HOME;
                loadHomeScreen();
                break;
            default:
                throw new UnsupportedOperationException("Unsupported event on HOME page:: " + event.getPropertyName());
        }
    }

    private void handleConfrontationCreatorEvents(PropertyChangeEvent event) {
        if (currentEditionState != EditionState.CONFRONTATION_CREATOR) {
            throw new IllegalStateException("Should not receive events from confrontation while in state " + currentEditionState);
        }
        previousEditionState = EditionState.CONFRONTATION_CREATOR;
        switch (event.getPropertyName()) {
            case ScreenEvents.NEW_CONFRONTATION:
                currentEditionState = EditionState.SESSION_CREATOR;
                List<EditablePlayerRound> confrontationRounds = (List<EditablePlayerRound>) event.getNewValue();
                sessionCreatorScreen.setSessionInProgress(currentSession);
                sessionCreatorScreen.createConfrontation(confrontationRounds);
                setAnchorPaneConstant(sessionCreatorScreen.getMainNode());
                break;
            case ScreenEvents.CANCEL:
                currentEditionState = EditionState.SESSION_CREATOR;
                sessionCreatorScreen.setSessionInProgress(currentSession);
                setAnchorPaneConstant(sessionCreatorScreen.getMainNode());
                break;
            default:
                throw new UnsupportedOperationException("Unsupported event on HOME page:: " + event.getPropertyName());
        }
    }

    private void setAnchorPaneConstant(Node node) {
        mainContentPane.getChildren().setAll(node);
        AnchorPane.setBottomAnchor(node, 0.0);
        AnchorPane.setLeftAnchor(node, 0.0);
        AnchorPane.setRightAnchor(node, 0.0);
        AnchorPane.setTopAnchor(node, 0.0);
    }

}
