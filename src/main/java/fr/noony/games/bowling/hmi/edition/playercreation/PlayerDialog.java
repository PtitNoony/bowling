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
package fr.noony.games.bowling.hmi.edition.playercreation;

import com.github.ptitnoony.gameutils.Player;
import com.github.ptitnoony.gameutils.PlayerFactory;
import static javafx.application.Platform.runLater;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 *
 * @author Arnaud HAMON-KEROMEN
 */
public class PlayerDialog extends Dialog<Player> {

    private Node createButton;
    private TextField firstName;
    private TextField lastName;
    private TextField nickName;

    public PlayerDialog() {
        super();
        init();
    }

    private void init() {
        setTitle("Player creation Dialog");
        setHeaderText("Create a new player");

        // Set the button types.
        ButtonType loginButtonType = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create the labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(8);
        grid.setPadding(new Insets(20, 150, 10, 10));

        firstName = new TextField();
        firstName.setPromptText("First Name");
        lastName = new TextField();
        lastName.setPromptText("Last Name");
        nickName = new TextField();
        nickName.setPromptText("Nickname");

        grid.add(new Label("First Name:"), 0, 0);
        grid.add(firstName, 1, 0);
        grid.add(new Label("Last Name:"), 0, 1);
        grid.add(lastName, 1, 1);
        grid.add(new Label("Nickname:"), 0, 2);
        grid.add(nickName, 1, 2);

        // Enable/Disable create button 
        createButton = getDialogPane().lookupButton(loginButtonType);
        createButton.setDisable(true);

        // listen to text input changed to update create button
        firstName.textProperty().addListener((observable, oldValue, newValue) -> {
            //TODO: log
            updateCreateButton();
        });
        lastName.textProperty().addListener((observable, oldValue, newValue) -> {
            //TODO: log
            updateCreateButton();
        });
        nickName.textProperty().addListener((observable, oldValue, newValue) -> {
            //TODO: log
            updateCreateButton();
        });

        getDialogPane().setContent(grid);

        // Request focus on the username field by default.
        runLater(() -> firstName.requestFocus());

        // Convert the result to a player when the create button is clicked.
        setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return PlayerFactory.createPlayer(firstName.getText().trim(), lastName.getText().trim(), nickName.getText().trim());
            }
            return null;
        });
    }

    private void updateCreateButton() {
        createButton.setDisable(!PlayerFactory.areValidPlayerAttibutes(firstName.getText().trim(), lastName.getText().trim(), nickName.getText().trim()));
    }

}
