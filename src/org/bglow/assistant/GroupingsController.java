package org.bglow.assistant;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.web.WebView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by kiko on 3/13/2017.
 */
public class GroupingsController {

    @FXML
    private WebView webView;

    private PunishmentGrouping logic;

    public void setSentences(List<Sentence> data) {
        logic = new PunishmentGrouping(data);
    }

    public void calculate() {
        try {
            renderGroups(logic.generate());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onPrint(Event e) {
        Node node = (Node) e.getSource();
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null && job.showPrintDialog(node.getScene().getWindow())) {
            webView.getEngine().print(job);
            job.endJob();
        }
    }

    private void renderGroups(List<PunishmentGrouping.Grouping> groupings) throws IOException {
        StringBuilder html = new StringBuilder();
        html.append("<html>");
        html.append("<head>");
        html.append("</head>");
        html.append("<style>");
        InputStream is = getClass().getResource("results.css").openStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line = reader.readLine();
        while (line != null) {
            html.append(line);
            line = reader.readLine();
        }

        html.append("</style>");
        html.append("<body>");
        final int[] num = new int[1];

        groupings.forEach(grouping -> {
            html.append("<table>");
            html.append("<caption>Вариант " + ++num[0] + " </caption>");
            int groupNumber = 1;
            for (ArrayList<Sentence> group : grouping.groups) {
                Sentence maxSentence = group.get(0);
                for(int i=0; i<group.size(); i++) {
                    Sentence sentence = group.get(i);
                    if(maxSentence.getPunishment().compareTo(sentence.getPunishment()) < 0)
                        maxSentence = sentence;
                }

                html.append("<tbody>");
                html.append("<tr>");
                html.append(MessageFormat.format("<th colspan=\"3\">Група {0}</th>", groupNumber++));
                html.append(MessageFormat.format("<th>{0}</th>", maxSentence.getPunishment()));

                html.append("</tr>");
                for(int i=0; i<group.size(); i++) {
                    Sentence sentence = group.get(i);
                    html.append("<tr>");
                    html.append(MessageFormat.format("<td>{0}</td>", sentence.getId()));
                    html.append(MessageFormat.format("<td>{0}</td>", sentence.getActDate()));
                    html.append(MessageFormat.format("<td>{0}</td>", sentence.getEffectDate()));
                    html.append(MessageFormat.format("<td class=\"punishment {1}\">{0}</td>", sentence.getPunishment(), sentence == maxSentence ? "active" : ""));
                    html.append("</tr>");
                }
                html.append("</tbody>");
            }
            html.append("</body>");
            html.append("</html>");

            webView.getEngine().loadContent(html.toString());

        });
    }
}
