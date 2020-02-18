package com.lune;

import java.util.*;

public class Room {
    String name;
    int numStudent, numSeat, numClm, brdrFront;
    List<Student> students, seat;
    Scanner sc = new Scanner(System.in);

    Room(String name, int numStudent, int numSeat, int numClm, int brdrFront){
        setName(name);
        setNumStudent(numStudent);
        setNumSeat(numSeat);
        setNumClm(numClm);
        setBrdrFront(brdrFront);
        students = new ArrayList<Student>();
    }
    Room(String name){
        Random rand = new Random();

        int numStudent = rand.nextInt(40) + 1;  //1-40の範囲で  //numStudent = numSeatにすると空席がなくなる
        int numSeat = rand.nextInt(11) + 30; //30-40の範囲で
        while(numSeat < numStudent)numSeat = rand.nextInt(11) + 30;
        int numClm = rand.nextInt(5) + 5; //5-9の範囲で
        int brdrFront = rand.nextInt(3) + 1; //1-3の範囲で

        setName(name);
        setNumStudent(numStudent);
        setNumSeat(numSeat);
        setNumClm(numClm);
        setBrdrFront(brdrFront);
        students = new ArrayList<Student>();
    }
    public void setName(String name){this.name = name;}
    public void setNumStudent(int numStudent){this.numStudent = numStudent;}
    public void setNumSeat(int numSeat){this.numSeat = numSeat;}
    public void setNumClm(int numClm) {this.numClm = numClm;}
    public void setBrdrFront(int brdrFront){this.brdrFront = brdrFront;}

    public void mkClass(boolean rand){  //学生情報登録メソッド
        if(rand){
            for(int i=0; i<numStudent; i++){
                Student student = new Student(i + 1);
                students.add(student);
            }
        }else{
            this.numStudent = numSeat;
            while(this.numStudent >= numSeat){
                System.out.print("登録する学生数を入力(座席数は" + numSeat + "席): ");
                this.numStudent = sc.nextInt();
            }
            for(int i=0; i<numStudent; i++){
                int number;
                boolean front = false;
                List<Integer> like = new ArrayList<Integer>();

                System.out.print((i + 1) + "番目の学生の学籍番号を入力: ");
                number = sc.nextInt();
                System.out.print(number + "番は前方を希望しますか？ y / n: ");
                String tmpStr = sc.next();
                if(tmpStr.charAt(0) == 'y')front = true;
                System.out.println(number + "番が近くを希望する学生の番号を入力(" + Student.getNumLike() + "人以内)\n いない場合は0を入力");
                for(int j=0; j<Student.getNumLike(); j++){
                    System.out.print((j + 1) + "人目: ");
                    int tmpInt = sc.nextInt();
                    like.add(tmpInt);
                }

                Student student = new Student(number, front, like);
                students.add(student);
                System.out.println((i + 1) + "番目の情報入力完了(残り" + (numStudent - (i + 1)) + "人)");
            }
        }
    }
    public void showStudents(){ //登録されている学生情報表示メソッド
        System.out.println(this.name+ "の学生情報を表示します.");
        for(int i=0; i<numSeat; i++){
            Student student = students.get(i);
            System.out.println("学籍番号: " + student.getNumber());
            System.out.println("前方希望の有無: " + student.getFront());
            for(int j=0; j<Student.getNumLike(); j++)
                System.out.println("希望者" + j + "人目: "+ student.getLike().get(j));
            System.out.print("\n");
        }
    }

    public void setSeat(int mode){  //座席指定メソッド
        Student dummy = new Student(0, false, new ArrayList<Integer>(Student.getNumLike()));
        seat = new ArrayList<Student>(numSeat);
        for(int i=0; i<numSeat; i++)
            seat.add(dummy);

        if(mode == 0){   /*mode == 0: ランダム*/
            Random rand = new Random();
            Boolean[] flg = new Boolean[numSeat];
            int count = 0;
            for(int i=0; count<numStudent; i++){
                int tmp = rand.nextInt(numSeat);
                if(flg[tmp] == null){
                    flg[tmp] = true;
                    seat.set(tmp, students.get(i));
                    count++;
                }else i--;
            }
        }
        else if(mode == 1)  /*mode == 1: 順番*/
        for(int i=0; i<numStudent; i++)
            seat.set(i, students.get(i));
    }
    public void showSeat(){ //座席表表示メソッド
        System.out.println(this.name + "の座席表を表示します.");
        System.out.println("\n前方");
        for(int i=0; i<numSeat; i++){
            if(i > 0 && i % numClm == 0)System.out.print("\n");
            if(seat.get(i).getNumber() == 0)System.out.print("----- ");
            else System.out.print(String.format("%05d", seat.get(i).getNumber()) + " ");
        }
        System.out.println("\n後方");
    }
    public void replaceSeat(int from, int to){  //指定座席入れ替えメソッド
        Student tmp = seat.get(to);
        seat.set(to, seat.get(from));
        seat.set(from, tmp);
    }
    public void satisfactionCalc(){
        
    }
}
