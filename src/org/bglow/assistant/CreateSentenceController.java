package org.bglow.assistant;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.Period;

/**
 * Created by kiko on 3/9/2017.
 */
public class CreateSentenceController {

    private CreateSentenceListener createListener;

    @FXML
    private ComboBox punishmentTypeCombo;

    @FXML
    private TextField idField;

    @FXML
    private DatePicker actDateField;

    @FXML
    private DatePicker effectDateField;

    @FXML
    private TextField periodField;

    @FXML
    private void initialize() {
        ObservableList<String> options = FXCollections.observableArrayList("Пробация", "ЛОС");
        punishmentTypeCombo.setItems(options);
        punishmentTypeCombo.setValue("Пробация");
    }

    @FXML
    private void onCreateSentence() {
        if(!validate())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Невалидни данни");
            alert.setContentText("Моля, попълнете всички полета.");
            alert.show();
            return;
        }

        Period period = Period.ofMonths(Integer.parseInt(periodField.getText()));
        Punishment.Type type;
        switch ((String) punishmentTypeCombo.getValue()) {
            case "Пробация":
                type = Punishment.Type.Probation;
                break;
            case "ЛОС":
                type = Punishment.Type.Jail;
                break;
            default:
                type = Punishment.Type.Probation;
                break;
        }
        Punishment punishment = new Punishment(period, type);

        Sentence sentence = new Sentence(idField.getText(), actDateField.getValue(), effectDateField.getValue(), punishment);
        if(createListener != null)
            createListener.onCreate(sentence);
    }

    public void setCreateListener(CreateSentenceListener createListener) {
        this.createListener = createListener;
    }

    private boolean validate() {
        return idField.getText().length() > 2
                && actDateField.getValue() != null
                && effectDateField.getValue() != null
                && periodField.getText().length() > 0;
    }
}
