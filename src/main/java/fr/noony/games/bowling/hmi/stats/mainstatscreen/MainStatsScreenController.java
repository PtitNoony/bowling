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
package fr.noony.games.bowling.hmi.stats.mainstatscreen;

import fr.noony.games.bowling.analytics.ComparisonMode;
import fr.noony.games.bowling.analytics.PlayerAnalytics;
import fr.noony.games.bowling.hmi.ScreenController;
import fr.noony.games.bowling.hmi.ScreenEvents;
import fr.noony.games.bowling.utils.PlayerFactory;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author hamon
 */
public class MainStatsScreenController implements ScreenController {

    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(MainStatsScreenController.this);

    @FXML
    private AnchorPane mainPane;
    @FXML
    private TableView<PlayerAnalytics> scoreTable;
    @FXML
    private Button viewPlayerButton;
    @FXML
    private Button compareButton;
    @FXML
    private RadioButton rBAtLeast;
    @FXML
    private RadioButton rBOnly;
    @FXML
    private RadioButton rBAtMost;

    //
    private TableColumn<PlayerAnalytics, String> nameColumn;
    private TableColumn<PlayerAnalytics, String> minScoreColumn;
    private TableColumn<PlayerAnalytics, String> maxScoreColumn;
    private TableColumn<PlayerAnalytics, String> averageScoreColumn;
    private TableColumn<PlayerAnalytics, String> averageSpareColumn;
    private TableColumn<PlayerAnalytics, String> averageStrikeColumn;

    private List<PlayerAnalytics> playerAnalyticses;

    private ComparisonMode comparionMode;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initControls();
        loadTable();
        refresh();
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    @FXML
    protected void handleOnViewPlayer(ActionEvent event) {
        PlayerAnalytics playerToView = scoreTable.getSelectionModel().getSelectedItem();
        propertyChangeSupport.firePropertyChange(ScreenEvents.VIEW_PLAYER_STATS, null, playerToView);
    }

    @FXML
    protected void handleOnCompare(ActionEvent event) {
        propertyChangeSupport.firePropertyChange(ScreenEvents.COMPARE_PLAYERS_STATS, comparionMode, Collections.unmodifiableList(scoreTable.getSelectionModel().getSelectedItems()));
    }

    protected void refresh() {
        // create new analytic list
        playerAnalyticses = PlayerFactory.getCreatedPlayers().stream().map(player -> new PlayerAnalytics(player)).collect(Collectors.toList());
        scoreTable.getItems().setAll(playerAnalyticses);
    }

    private void initControls() {
        viewPlayerButton.setDisable(true);
        compareButton.setDisable(true);
        //
        ToggleGroup comparisonGroupMode = new ToggleGroup();
        rBAtLeast.setToggleGroup(comparisonGroupMode);
        rBOnly.setToggleGroup(comparisonGroupMode);
        rBAtMost.setToggleGroup(comparisonGroupMode);
        rBAtLeast.setSelected(true);
        comparionMode = ComparisonMode.AT_LEAST;
        comparisonGroupMode.selectedToggleProperty().addListener((ObservableValue<? extends Toggle> ov, Toggle t, Toggle t1) -> {
            //TODO: log
            if (rBAtLeast.isSelected()) {
                comparionMode = ComparisonMode.AT_LEAST;
            } else if (rBOnly.isSelected()) {
                comparionMode = ComparisonMode.ONLY;
            } else if (rBAtMost.isSelected()) {
                comparionMode = ComparisonMode.AT_MOST;
            } else {
                throw new UnsupportedOperationException();
            }
        });

    }

    private void loadTable() {
        nameColumn = new TableColumn<>("Player");
        nameColumn.setPrefWidth(75);
        nameColumn.setCellValueFactory((TableColumn.CellDataFeatures<PlayerAnalytics, String> param) -> new ReadOnlyStringWrapper(param.getValue().getPlayer().getNickName()));
        //
        minScoreColumn = new TableColumn<>("Min Score");
        minScoreColumn.setPrefWidth(100);
        minScoreColumn.setCellValueFactory((TableColumn.CellDataFeatures<PlayerAnalytics, String> param) -> new ReadOnlyStringWrapper(Integer.toString(param.getValue().getMinScore())));
        minScoreColumn.setComparator(new ScoreComparator());
        //
        maxScoreColumn = new TableColumn<>("Max Score");
        maxScoreColumn.setPrefWidth(100);
        maxScoreColumn.setCellValueFactory((TableColumn.CellDataFeatures<PlayerAnalytics, String> param) -> new ReadOnlyStringWrapper(Integer.toString(param.getValue().getMaxScore())));
        maxScoreColumn.setComparator(new ScoreComparator());
        //
        averageScoreColumn = new TableColumn<>("Avg. Score");
        averageScoreColumn.setPrefWidth(100);
        averageScoreColumn.setCellValueFactory((TableColumn.CellDataFeatures<PlayerAnalytics, String> param) -> new ReadOnlyStringWrapper(Double.toString(param.getValue().getAverageScore())));
        averageScoreColumn.setComparator(new ScoreComparator());
        //
        averageSpareColumn = new TableColumn<>("Avg. nb Spares");
        averageSpareColumn.setPrefWidth(150);
        averageSpareColumn.setCellValueFactory((TableColumn.CellDataFeatures<PlayerAnalytics, String> param) -> new ReadOnlyStringWrapper(Double.toString(param.getValue().getAverageSpares())));
        averageSpareColumn.setComparator(new ScoreComparator());
        //
        averageStrikeColumn = new TableColumn<>("Avg. nb Strikes");
        averageStrikeColumn.setPrefWidth(150);
        averageStrikeColumn.setCellValueFactory((TableColumn.CellDataFeatures<PlayerAnalytics, String> param) -> new ReadOnlyStringWrapper(Double.toString(param.getValue().getAverageStrikes())));
        averageStrikeColumn.setComparator(new ScoreComparator());
        //
        scoreTable.getColumns().addAll(nameColumn, minScoreColumn, maxScoreColumn, averageScoreColumn, averageSpareColumn, averageStrikeColumn);
        scoreTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        //
        scoreTable.getSelectionModel().getSelectedItems().addListener((ListChangeListener.Change<? extends PlayerAnalytics> change) -> {
            //TODO: log
            handleSelectionChange();
        });
    }

    private void handleSelectionChange() {
        switch (scoreTable.getSelectionModel().getSelectedItems().size()) {
            case 0:
                //TODO: wrap im method
                viewPlayerButton.setDisable(true);
                compareButton.setDisable(true);
                break;
            case 1:
                // TODO: same
                viewPlayerButton.setDisable(false);
                compareButton.setDisable(true);
                break;
            default:
                //TODO: same
                viewPlayerButton.setDisable(true);
                compareButton.setDisable(false);

        }
    }

    private class ScoreComparator implements Comparator<String> {

        @Override
        public int compare(String o1, String o2) {
            double i1;
            double i2;
            if ("".equals(o1)) {
                i1 = 0;
            } else {
                i1 = Double.parseDouble(o1);
            }
            if ("".equals(o2)) {
                i2 = 0;
            } else {
                i2 = Double.parseDouble(o2);
            }
            return Double.compare(i1, i2);
        }
    }

}
