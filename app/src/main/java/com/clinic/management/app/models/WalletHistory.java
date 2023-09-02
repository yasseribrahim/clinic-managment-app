package com.clinic.management.app.models;

import java.util.Date;
import java.util.Objects;

public class WalletHistory {
    private String id;

    private String userId;
    private double amount;
    private Date date;
    private String notes;

    public WalletHistory(String userId) {
        this.userId = userId;
    }

    public WalletHistory(String userId, double amount, Date date, String notes) {
        this.userId = userId;
        this.amount = amount;
        this.date = date;
        this.notes = notes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WalletHistory that = (WalletHistory) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
