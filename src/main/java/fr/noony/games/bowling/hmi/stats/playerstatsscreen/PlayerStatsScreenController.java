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

import fr.noony.games.bowling.Round;
import fr.noony.games.bowling.analytics.PlayerAnalytics;
import fr.noony.games.bowling.hmi.ScreenController;
import fr.noony.games.bowling.hmi.edition.scoreview.HistoryDrawing;
import fr.noony.games.bowling.utils.UIUtils;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import static javafx.application.Platform.runLater;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author ahamon
 */
public class PlayerStatsScreenController implements ScreenController {

    @FXML
    private Label playerLabel;
    @FXML
    private Label avgScoreLabel;
    @FXML
    private Label maxScoreLabel;
    @FXML
    private Label minScoreLabel;
    @FXML
    private Label avgStrikesLabel;
    @FXML
    private Label avgSparesLabel;
    @FXML
    private LineChart<String, Integer> scoresChart;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private AnchorPane playerGamesPane;

    private PlayerAnalytics pA;
    private XYChart.Series series;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        series = new XYChart.Series();
        scoresChart.getData().add(series);
        scoresChart.setAnimated(false);
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(300);
        yAxis.setTickUnit(25);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
    }

    @FXML
    protected void handleBackAction(ActionEvent event) {
        System.err.println("TODO");
    }

    protected void refresh() {
        runLater(this::refreshUI);
    }

    protected void setPlayerAnalytics(PlayerAnalytics playerAnalytics) {
        pA = playerAnalytics;
        runLater(this::refreshUI);
    }

    private void refreshUI() {
        playerLabel.setText(pA.getPlayer().toString());
        avgScoreLabel.setText(UIUtils.formatNumber(pA.getAverageScore()));
        minScoreLabel.setText(Integer.toString(pA.getMinScore()));
        maxScoreLabel.setText(Integer.toString(pA.getMaxScore()));
        avgStrikesLabel.setText(UIUtils.formatNumber(pA.getAverageStrikes()));
        avgSparesLabel.setText(UIUtils.formatNumber(pA.getAverageSpares()));
        //
        series.setName(pA.getPlayer().getNickName() + " games");
        series.getData().clear();
        pA.getAllRounds().keySet().stream().sorted((d1, d2) -> d1.compareTo(d2)).forEach(date -> {
            List<Round> rounds = pA.getAllRounds().get(date);
            for (int i = 0; i < rounds.size(); i++) {
                series.getData().add(new XYChart.Data<>(date.toString() + " (" + i + ")", rounds.get(i).getFinalScore()));
            }
        });
        //
        HistoryDrawing historyDrawing = new HistoryDrawing(pA);
        playerGamesPane.getChildren().add(historyDrawing);
        playerGamesPane.setPrefSize(historyDrawing.getPrefWidth() + 16, historyDrawing.getPrefHeight() + 16);
        AnchorPane.setTopAnchor(historyDrawing, 8.0);
        AnchorPane.setLeftAnchor(historyDrawing, 8.0);
    }
}
