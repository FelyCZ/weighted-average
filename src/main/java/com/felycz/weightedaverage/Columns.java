package com.felycz.weightedaverage;


public class Columns {
    public  String name;
    public String mark;
    public String weight;
    public Columns (String name, String mark, String weight){
        this.name = name;
        this.mark = mark;
        this.weight = weight;
    }

    public String getMark() {
        return mark;
    }
    public void setMark(String argM){
        this.mark = argM;
    }
    public String getWeight(){
        return weight;
    }
    public void setWeight(String argW){
        this.weight = argW;
    }
}
