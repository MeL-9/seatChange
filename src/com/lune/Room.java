package com.lune;

import java.util.*;

public class Room {
    String name;
    int numStudent = 0, numSeat, numClm, brdrFront;
    List<Student> students, seat;
    boolean useFront = false;
    Scanner sc = new Scanner(System.in);

    Room(String name, int numSeat, int numClm, int brdrFront){
        setName(name);
        setNumSeat(numSeat);
        setNumClm(numClm);
        setBrdrFront(brdrFront);
        students = new ArrayList<Student>();
    }
    Room(String name){
        Random rand = new Random();

        int numSeat = rand.nextInt(11) + 30; //30-40の範囲で
        while(numSeat < numStudent)numSeat = rand.nextInt(11) + 30;
        int numClm = rand.nextInt(5) + 5; //5-9の範囲で
        int brdrFront = rand.nextInt(3) + 1; //1-3の範囲で

        setName(name);
        setNumSeat(numSeat);
        setNumClm(numClm);
        setBrdrFront(brdrFront);
        students = new ArrayList<Student>();
    }
    Room(){
        setName("TestRoom");
        setNumStudent(9);
        setNumSeat(9);
        setNumClm(3);
        setBrdrFront(1);
        students = new ArrayList<Student>();
        mkTest();
    }
    private void mkTest(){
        Student student;
        List<Integer> like = new ArrayList<Integer>();
        like.add(1);
        like.add(2);
        like.add(3);
        for(int i=0; i<numStudent; i++){
            student = new Student(i + 1, false, like);
            students.add(student);
        }
    }
    public void setName(String name){this.name = name;}
    public void setNumStudent(int numStudent){this.numStudent = numStudent;}
    public void setNumSeat(int numSeat){this.numSeat = numSeat;}
    public void setNumClm(int numClm) {this.numClm = numClm;}
    public void setBrdrFront(int brdrFront){this.brdrFront = brdrFront;}

    public void mkClass(boolean rnd){  //学生情報登録メソッド
        if(rnd){
            Random rand = new Random();
            int numStudent = /*rand.nextInt(40) + 1*/numSeat;  //1-40の範囲で  //numStudent = numSeatにすると空席がなくなる
            setNumStudent(numStudent);

            for(int i=0; i<this.numStudent; i++){
                Student student = new Student(i + 1);
                students.add(student);
            }
        }else{
            while(this.numStudent >= numSeat){
                System.out.print("登録する学生数を入力(座席数は" + numSeat + "席): ");
                this.numStudent = sc.nextInt();
                System.out.print("前方希望機能を利用しますか？ y / n: ");
                String tmpStr = sc.next();
                if(tmpStr.charAt(0) == 'y')useFront = true;
            }
            for(int i=0; i<numStudent; i++){
                int number;
                boolean front = false;
                List<Integer> like = new ArrayList<Integer>();

                System.out.print((i + 1) + "番目の学生の学籍番号を入力: ");
                number = sc.nextInt();
                if(useFront){
                    System.out.print(number + "番は前方を希望しますか？ y / n: ");
                    String tmpStr = sc.next();
                    if(tmpStr.charAt(0) == 'y')front = true;
                }
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
        else if(mode == 2){ /*mode == 2: 満足度考慮*/
            /*満足度考慮席替え*/
        }
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
    private int checkNeighbor(Student curStudent, int pos){
        if(curStudent.getLike().contains(seat.get(pos).getNumber())){
            System.out.println(curStudent.getNumber() + ":" + seat.get(pos).getNumber());
            return 1;
        }
        return 0;
    }
    public void satisfactionCalc(int mode){
        if(mode == 0){  //mode == 0 8近傍のlikeの数
            for(int i=0; i<seat.size(); i++){   //各座席を順番に見ていく
                Student curStudent = seat.get(i);
                int likeCount = 0;
                if(curStudent.getNumber() == 0){    //空席(ダミー)を見ている場合
                    seat.get(i).setSatisfaction(0);
                    continue;
                }
                if(useFront && i / numClm >= brdrFront && curStudent.getFront()){  //前方希望での場合
                    seat.get(i).setSatisfaction(0);
                    continue;
                }

                if((i % numClm) != 0 && (i - numClm - 1) >= 0)  //左斜め前存在チェック
                    if(seat.get(i - numClm - 1).getNumber() != 0){
                        likeCount += checkNeighbor(curStudent, i - numClm - 1);
                    }
                if((i - numClm) >= 0)   //前
                    if(seat.get(i - numClm).getNumber() != 0){
                        likeCount += checkNeighbor(curStudent, i - numClm);
                    }
                if((i % numClm) != (numClm - 1) && (i - numClm + 1) >= 0)   //右斜め前
                    if(seat.get(i - numClm + 1).getNumber() != 0){
                        likeCount += checkNeighbor(curStudent, i - numClm + 1);
                    }
                if((i % numClm) != 0)   //左
                    if(seat.get(i - 1).getNumber() != 0){
                        likeCount += checkNeighbor(curStudent, i - 1);
                    }
                if((i % numClm) != (numClm - 1) && (i + 1) < numSeat)   //右
                    if(seat.get(i + 1).getNumber() != 0){
                        likeCount += checkNeighbor(curStudent, i + 1);
                    }
                if((i % numClm) != 0 && (i + numClm - 1) < numSeat)   //左斜め後ろ
                    if(seat.get(i + numClm - 1).getNumber() != 0){
                        likeCount += checkNeighbor(curStudent, i + numClm - 1);
                    }
                if((i + numClm) < numSeat)   //後ろ
                    if(seat.get(i + numClm).getNumber() != 0){
                        likeCount += checkNeighbor(curStudent, i + numClm);
                    }
                if((i % numClm) != (numClm - 1) && (i + numClm + 1) < numSeat)   //右斜め後ろ
                    if(seat.get(i + numClm + 1).getNumber() != 0){
                        likeCount += checkNeighbor(curStudent, i + numClm + 1);
                    }
                seat.get(i).setSatisfaction(likeCount);
          }
        }else if(mode == 1){    //mode == 1:　likeまでの平均距離に逆数

        }
    }
    public void showSatisfaction(int seatNumber){
        seatNumber--;
        if(seatNumber + 1 == 0){
            int sumSatisfaction = 0, minSatisfaction = Student.getNumLike() + 1;
            for (Student student : seat){
                sumSatisfaction += student.getSatisfaction();
                if(student.getSatisfaction() < minSatisfaction)minSatisfaction = student.getSatisfaction();
            }
            System.out.println("この教室の満足度の平均は" + (double)sumSatisfaction / seat.size());
            System.out.println("この教室の満足度の最低値は" + minSatisfaction);
        }else if(seatNumber > 0 && seatNumber < numSeat){
            System.out.println((seatNumber + 1) + "番の席の"
                    + seat.get(seatNumber).getNumber() + "番の学生の満足度は" + seat.get(seatNumber).getSatisfaction());
            System.out.print(seat.get(seatNumber).getNumber() + "番の学生のlikeは:");
            for(int tmp :seat.get(seatNumber).getLike())System.out.print(tmp + ",");
        }
        else System.out.println("不正な値");
    }
}
