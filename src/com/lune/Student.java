package com.lune;

import java.util.*;

public class Student {
    static int numLike = 3;
    int number, satisfaction;
    boolean front;
    List<Integer> like;

    Student(int number, boolean front, List<Integer> like){
        setNumber(number);
        setFront(front);
        setLike(like);
    }
    Student(int number){
        Random rand = new Random();
        List<Integer> like = new ArrayList<Integer>();

        setNumber(number);
        if(rand.nextInt(100) < 20)setFront(true);
        else setFront(false);
        for(int i=0;i<numLike;i++){
            int tmp = rand.nextInt(40) + 1;
            while(tmp == this.number)tmp = rand.nextInt(40) + 1;
            like.add(tmp);
        }
        setLike(like);
    }

    public void setNumber(int number) {this.number = number;}
    public void setFront(boolean front) {this.front = front;}
    public void setLike(List<Integer> like) {this.like = like;}
    public void setSatisfaction(int satisfaction) {this.satisfaction = satisfaction;}

    public int getNumber() {return number;}
    public boolean getFront(){return front;}
    public List<Integer> getLike() {return like;}
    public int getSatisfaction() {return satisfaction;}
    public static int getNumLike() {return numLike;}
}
