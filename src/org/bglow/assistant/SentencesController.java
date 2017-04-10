package org.bglow.assistant;

import com.sun.javaws.exceptions.InvalidArgumentException;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

/**
 * Created by kiko on 3/9/2017.
 */
public class SentencesController {
    @FXML
    private TableView<Sentence> tableView;
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    final ObservableList<Sentence> data = FXCollections.observableArrayList(
            new Sentence("К-1233-1", LocalDate.of(2014, 6, 10), LocalDate.of(2014, 6, 27), new Punishment(Period.ofYears(1), Punishment.Type.Probation)),
            new Sentence("К-3214-2", LocalDate.of(2014, 5, 5), LocalDate.of(2014, 10, 27), new Punishment(Period.ofMonths(6), Punishment.Type.Jail)),
            new Sentence("К-4321-3", LocalDate.of(2014, 8, 30), LocalDate.of(2015, 3, 31), new Punishment(Period.ofMonths(6), Punishment.Type.Jail))
    );


    @FXML
    private void initialize() {
        TableColumn<Sentence, String> idCol = new TableColumn<>("Дело №");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Sentence, String> actDateCol = new TableColumn<>("Извършено");
        actDateCol.setCellValueFactory(features -> new ReadOnlyObjectWrapper<>(features.getValue().getActDate().format(dateFormatter)));
        TableColumn<Sentence, String> effectiveDateCol = new TableColumn<>("Влязла в сила");
        effectiveDateCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().getEffectDate().format(dateFormatter)));
        TableColumn<Sentence, String> punishCol = new TableColumn<>("Наказание");
        punishCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper(param.getValue().getPunishment().toString()));

        tableView.setItems(data);
        tableView.setEditable(true);
        tableView.getColumns().addAll(idCol, actDateCol, effectiveDateCol, punishCol);
    }

    void addSentence(Sentence sentence) {
        if (data.filtered(s -> s.getId().equalsIgnoreCase(sentence.getId())).size() > 0) {
            throw new IllegalArgumentException("Присъда със същия номер вече съществува.");
        }

        data.add(sentence);
    }
}
