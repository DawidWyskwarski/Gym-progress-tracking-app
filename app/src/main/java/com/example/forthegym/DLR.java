package com.example.forthegym;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

public class DLR implements Serializable {

    private LocalDate date;
    private float load;
    private int reps;

    public DLR(LocalDate date ,float load, int reps){
        this.date = date;
        this.load = load;
        this.reps = reps;
    }

    public LocalDate getDate() {
        return date;
    }
    public float getLoad() {
        return load;
    }
    public int getReps() {
        return reps;
    }

    @Override
    public String toString() {
        return  "date: "  + getDate() +" load: " + getLoad() + " kg reps: " + getReps() + "\n";
    }
}
