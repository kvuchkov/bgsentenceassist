package org.bglow.assistant;

import junit.framework.TestCase;

import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;

/**
 * Created by kiko on 3/10/2017.
 */
public class PunishmentGroupingTest extends TestCase {

    public void testGenerateTwoInAGroup() throws Exception {
        Sentence[] sentences = {
                new Sentence("asdf-1234", LocalDate.of(2014, 6, 10), LocalDate.of(2014, 6, 29), new Punishment(Period.ofMonths(12), Punishment.Type.Probation)),
                new Sentence("xcvb-4452", LocalDate.of(2014, 6, 30), LocalDate.of(2014, 6, 30), new Punishment(Period.ofMonths(12), Punishment.Type.Probation))};

        PunishmentGrouping pg = new PunishmentGrouping(Arrays.asList(sentences));
        pg.generate();

        pg.groupings.forEach(grouping -> grouping.forEach((group, sentence) -> System.out.printf("%s (%d)", sentence.getId(), group)));

//        System.out.println(pg.groupings.size());
    }
}