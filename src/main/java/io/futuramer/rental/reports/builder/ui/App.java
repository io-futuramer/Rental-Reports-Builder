package io.futuramer.rental.reports.builder.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Objects;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("rentalReportsBuilder.fxml")));
        Font.loadFont(Objects.requireNonNull(getClass().getClassLoader().getResource("Lobster.ttf")).toExternalForm(), 10);
        Image icon = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("report.png")));
        primaryStage.getIcons().add(icon);
        primaryStage.setTitle("Rental Reports builder");
        primaryStage.setScene(new Scene(root, 700, 321));
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}

