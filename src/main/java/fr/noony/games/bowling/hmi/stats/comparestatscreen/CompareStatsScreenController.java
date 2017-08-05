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
package fr.noony.games.bowling.hmi.stats.comparestatscreen;

import com.github.ptitnoony.gameutils.Player;
import fr.noony.games.bowling.Confrontation;
import fr.noony.games.bowling.Session;
import fr.noony.games.bowling.analytics.ComparisonMode;
import fr.noony.games.bowling.analytics.PlayerAnalytics;
import fr.noony.games.bowling.hmi.ScreenController;
import fr.noony.games.bowling.utils.PlayerAnalyticsFactory;
import fr.noony.games.bowling.utils.SessionFactory;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author ahamon
 */
public class CompareStatsScreenController implements ScreenController {

    @FXML
    private AnchorPane gamesChartPane;

    private Canvas chartCanvas;
    private GraphicsContext chartGC;

    private double canvasWidth = 0;
    private double canvasHeight = 0;

    private ComparisonChart chart;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initLayout();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
    }

    protected void refresh() {

    }

    protected void setPlayersAnalytics(ComparisonMode mode, List<PlayerAnalytics> playerAnalyticses) {

        List<Player> players = Collections.unmodifiableList(playerAnalyticses.stream().map(pA -> pA.getPlayer()).collect(Collectors.toList()));

        Map<LocalDate, List<Confrontation>> confrontations = new HashMap<>();

        SessionFactory.getCreatedSessions().stream()
                .sorted(this::compareSession)
                .forEach(session -> {
                    session.getConfrontations().stream()
                            .filter(c -> confrontationFilter(c, mode, players))
                            .forEach(c -> {
                                if (!confrontations.containsKey(c.getConfrontationDate())) {
                                    confrontations.put(c.getConfrontationDate(), new LinkedList<>());
                                }
                                confrontations.get(c.getConfrontationDate()).add(c);
                            });
                });

        // get the players analytics for each player in these confrontations
        List<PlayerAnalytics> listAnalytics = new LinkedList<>();
        confrontations.forEach((date, cList) -> cList
                .forEach(confrontation -> confrontation.getPlayers()
                .forEach(player -> {
                    PlayerAnalytics pA = PlayerAnalyticsFactory.getPlayerAnalytics(player);
                    if (!listAnalytics.contains(pA)) {
                        listAnalytics.add(pA);
                    }
                })));

        chart.setData(listAnalytics, confrontations);
    }

    // TODO put somewhere
    private int compareSession(Session s1, Session s2) {
        return Long.compare(s1.getSessionDate().toEpochDay(), s2.getSessionDate().toEpochDay());
    }

    private boolean confrontationFilter(Confrontation confrontation, ComparisonMode mode, List<Player> players) {
        List<Player> cPlayers = confrontation.getPlayers();
        switch (mode) {
            case AT_LEAST:
                return cPlayers.containsAll(players);
            case ONLY:
                return cPlayers.containsAll(players) && cPlayers.size() == players.size();
            case AT_MOST:
                return !Collections.disjoint(cPlayers, players);
            default:
                throw new UnsupportedOperationException("unsupported confrontation mode:: " + mode);
        }
    }

    private void initLayout() {
        //
        gamesChartPane.widthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            //TODO: log
            canvasWidth = newValue.doubleValue();
            redrawChart();
        });
        gamesChartPane.heightProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            //TODO: log
            canvasHeight = newValue.doubleValue();
            redrawChart();
        });
        canvasWidth = gamesChartPane.getWidth();
        canvasHeight = gamesChartPane.getHeight();

        chart = new ComparisonChart();
        gamesChartPane.getChildren().add(chart.getNode());
        // hum...
        chart.setWidth(canvasWidth);
        chart.setHeight(canvasHeight);
    }

    private void redrawChart() {
        chart.setWidth(canvasWidth);
        chart.setHeight(canvasHeight);
    }

}
