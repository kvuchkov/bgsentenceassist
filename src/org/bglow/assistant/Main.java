package org.bglow.assistant;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        HBox root = new HBox();
        root.setSpacing(5);
        root.setPadding(new Insets(10, 10, 10, 10));
        root.setAlignment(Pos.CENTER);

        Button punishmentButton = new Button("Крупиране на наказания");
        punishmentButton.setOnAction(event -> {
            try {
                primaryStage.setScene(getPunishmentScene());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        root.getChildren().add(punishmentButton);

        Scene startingScene = new Scene(root);

        primaryStage.setTitle("Правен асистент");
        primaryStage.setScene(getPunishmentScene());
        primaryStage.show();
    }

    private Scene getPunishmentScene() throws IOException {
        return new Scene(FXMLLoader.load(getClass().getResource("Punishment.fxml")));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
