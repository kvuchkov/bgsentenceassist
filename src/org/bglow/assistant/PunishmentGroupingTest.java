package org.bglow.assistant;

import junit.framework.TestCase;

import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.List;

/**
 * Created by kiko on 3/10/2017.
 */
public class PunishmentGroupingTest extends TestCase {

    public void testGenerateTwoInAGroup() throws Exception {
        Sentence[] sentences = {
                new Sentence("asdf-1234", LocalDate.of(2014, 6, 10), LocalDate.of(2014, 6, 29), new Punishment(Period.ofMonths(12), Punishment.Type.Probation)),
                new Sentence("xcvb-4452", LocalDate.of(2014, 6, 25), LocalDate.of(2014, 6, 30), new Punishment(Period.ofMonths(12), Punishment.Type.Probation))};

        PunishmentGrouping pg = new PunishmentGrouping(Arrays.asList(sentences));
        List<PunishmentGrouping.Grouping> solutions = pg.generate();
        assertEquals(1, solutions.size());
        assertEquals(1, solutions.get(0).groups.size());
    }

    public void testGenerateTwoSolutionsWithTwoGroups() throws Exception {
        Sentence[] sentences = {
                new Sentence("asdf-1234", LocalDate.of(2014, 6, 10), LocalDate.of(2014, 6, 20), new Punishment(Period.ofMonths(12), Punishment.Type.Probation)),
                new Sentence("asdf-2234", LocalDate.of(2014, 6, 5), LocalDate.of(2014, 6, 25), new Punishment(Period.ofMonths(12), Punishment.Type.Probation)),
                new Sentence("xcvb-4452", LocalDate.of(2014, 6, 23), LocalDate.of(2014, 6, 30), new Punishment(Period.ofMonths(12), Punishment.Type.Probation))};

        PunishmentGrouping pg = new PunishmentGrouping(Arrays.asList(sentences));
        List<PunishmentGrouping.Grouping> solutions = pg.generate();
        assertEquals(2, solutions.size());
        assertEquals(2, solutions.get(0).groups.size());
        assertEquals(2, solutions.get(1).groups.size());
    }
}