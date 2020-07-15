package com.lune;

import java.io.Serializable;
import java.util.*;

public class Student implements Serializable {
    static int numLike = 3;
    int number;
    double satisfaction;
    boolean front;
    ArrayList<Integer> like;

    /*コンストラクタ*/
    Student(int number, boolean front, ArrayList<Integer> like){
        setNumber(number);
        setFront(front);
        setLike(like);
    }
    Student(int number){    //番号のみの指定でランダムに学生を生成
        Random rand = new Random();
        ArrayList<Integer> like = new ArrayList<Integer>();

        setNumber(number);
        if(rand.nextInt(100) < 20)setFront(true);
        else setFront(false);
        while(like.size() < numLike){
            int tmp = rand.nextInt(41);
            if(tmp == 0){
                like.add(tmp);
                continue;
            }else if (like.isEmpty()) {
                if (tmp == this.number) continue;
            } else if (tmp == this.number || like.contains(tmp)) continue;
            like.add(tmp);
        }
        setLike(like);
    }

    /*各種セッタ、ゲッタ*/
    public void setNumber(int number) {this.number = number;}
    public void setFront(boolean front) {this.front = front;}
    public void setLike(ArrayList<Integer> like) {this.like = like;}
    public void setSatisfaction(double satisfaction) {this.satisfaction = satisfaction;}
    public int getNumber() {return number;}
    public boolean getFront(){return front;}
    public List<Integer> getLike() {return like;}
    public double getSatisfaction() {return satisfaction;}
    public static int getNumLike() {return numLike;}
}
