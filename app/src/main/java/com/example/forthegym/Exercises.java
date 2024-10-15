package com.example.forthegym;

import java.io.Serializable;
import java.util.ArrayList;

public class Exercises implements Serializable {

    private String name;
    private ArrayList<DLR> training;

    public Exercises(String name){
        this.name = name;
        training = new ArrayList<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public ArrayList<DLR> getTraining() {
        return training;
    }

    public void addTraining(DLR dlr){
        training.add(dlr);
    }

    public String getLastFive(){
        String result = "";

        if(training.size() == 0){

            result = "No past trainings at the moment";

        } else if(training.size()>=5) {
            for (int i = 0; i < 5; ++i) {
                result += training.get(training.size() - 1 - i).toString();
            }
        }else{
            for(int i=0;i< training.size();i++){
                result += training.get(training.size() - 1 - i).toString();
            }
        }
        return result;
    }

    public void deleteLast(){

        if(!training.isEmpty()) {
            training.remove(training.size() - 1);
        }
    }

    public String getAll(){
        String result = "";

        for(int i=0;i<training.size();++i){
            result += training.get(training.size()-1-i).toString();
        }

        return result;
    }
}
