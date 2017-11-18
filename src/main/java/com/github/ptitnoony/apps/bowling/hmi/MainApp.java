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
package com.github.ptitnoony.apps.bowling.hmi;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 *
 * @author Arnaud HAMON-KEROMEN
 */
public final class MainApp extends Application {

    private static final Logger LOGGER = Logger.getGlobal();

    private AnchorPane root;

    @Override
    public void start(Stage primaryStage) throws Exception {

        // todo log
        root = loadFXML();
        Scene scene = new Scene(root, 1200, 1200 * 9 / 16, Color.BLACK);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private AnchorPane loadFXML() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainFrame.fxml"));
            AnchorPane node = loader.load();
            return node;
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE,"Exception while loading main fxml file:: {0}",ex);
        }
        return new AnchorPane();
    }

}
