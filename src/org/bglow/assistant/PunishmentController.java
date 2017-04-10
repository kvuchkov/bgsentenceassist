package org.bglow.assistant;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by kiko on 3/9/2017.
 */
public class PunishmentController implements CreateSentenceListener {
    @FXML
    private VBox content;

    CreateSentenceController createSentenceController;
    SentencesController sentencesController;

    @FXML
    private void initialize() {
        try {
            FXMLLoader createSentenceLoader = new FXMLLoader(getClass().getResource("CreateSentence.fxml"));
            content.getChildren().add(createSentenceLoader.load());
            createSentenceController = createSentenceLoader.<CreateSentenceController>getController();
            createSentenceController.setCreateListener(this);

            FXMLLoader sentencesLoader = new FXMLLoader(getClass().getResource("Sentences.fxml"));
            content.getChildren().add(sentencesLoader.load());
            sentencesController = sentencesLoader.<SentencesController>getController();

            Button calculateButton = new Button("Групирай");
            calculateButton.setOnAction(event -> this.onCalculate(event));

            content.getChildren().add(calculateButton);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onCalculate(Event e){
        FXMLLoader groupingsLoader;
        groupingsLoader = new FXMLLoader(getClass().getResource("Groupings.fxml"));
        Parent root = null;
        try {
            root = groupingsLoader.load();
        } catch (IOException e1) {
            e1.printStackTrace();
            return;
        }
        Scene s = new Scene(root);
        Node n = (Node) e.getSource();
        Stage stage = (Stage) n.getScene().getWindow();
        stage.setScene(s);

        GroupingsController controller = groupingsLoader.getController();
        controller.setSentences(sentencesController.data);
        controller.calculate();
    }

    @Override
    public void onCreate(Sentence sentence) {
        try {
            sentencesController.addSentence(sentence);
        }catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Грешка");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}
