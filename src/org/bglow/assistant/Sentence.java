package org.bglow.assistant;

import java.time.LocalDate;

/**
 * Created by kiko on 3/1/2017.
 */
public class Sentence {
    private String id;
    private LocalDate actDate;
    private LocalDate effectDate;
    private Punishment punishment;

    public Sentence(String id, LocalDate actDate, LocalDate effectDate, Punishment punishment) {
        this.id = id;
        this.actDate = actDate;
        this.effectDate = effectDate;
        this.punishment = punishment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getActDate() {
        return actDate;
    }

    public void setActDate(LocalDate actDate) {
        this.actDate = actDate;
    }

    public LocalDate getEffectDate() {
        return effectDate;
    }

    public void setEffectDate(LocalDate effectDate) {
        this.effectDate = effectDate;
    }

    public Punishment getPunishment() {
        return punishment;
    }

    public void setPunishment(Punishment punishment) {
        this.punishment = punishment;
    }
}
