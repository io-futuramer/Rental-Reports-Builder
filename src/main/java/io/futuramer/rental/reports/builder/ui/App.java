package io.futuramer.rental.reports.builder.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("rentalReportsBuilder.fxml"));
        Font.loadFont(App.class.getResource("Lobster.ttf").toExternalForm(), 10);
        Image icon = new Image(App.class.getResourceAsStream("report.png"));
        primaryStage.getIcons().add(icon);
        primaryStage.setTitle("Rental Reports builder");
        primaryStage.setScene(new Scene(root, 700, 321));
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}

