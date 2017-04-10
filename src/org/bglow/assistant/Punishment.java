package org.bglow.assistant;

import java.time.Period;

/**
 * Created by kiko on 3/9/2017.
 */

public class Punishment implements Comparable<Punishment> {
    @Override
    public int compareTo(Punishment o) {
        if (type == o.type) {
            return period.getMonths() < o.period.getMonths() ? -1 : 1;
        } else if (type == Type.Jail)
            return 1;
        else
            return -1;
    }

    public enum Type {
        Probation,
        Jail
    }

    private Period period;
    private Type type;

    public Punishment(Period period, Type type) {
        this.period = period;
        this.type = type;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (period.getYears() > 0) {
            sb.append(String.format("%dг. ", period.getYears()));
        }
        if (period.getMonths() > 0) {
            sb.append(String.format("%dм. ", period.getMonths()));
        }
        String typeStr = type == Punishment.Type.Probation ? "Пробация" : "ЛОС";
        sb.append(typeStr);
        return sb.toString();
    }
}
