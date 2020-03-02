package com.lune;

import java.util.*;

public class Room {
    String name;
    int numStudent = 0, numSeat, numClm, brdrFront;
    List<Student> students, seat;
    boolean useFront = false;
    Scanner sc = new Scanner(System.in);

    Room(String name, int numSeat, int numClm, boolean useFront, int brdrFront){
        setName(name);
        setNumSeat(numSeat);
        setNumClm(numClm);
        setUseFront(useFront);
        if(useFront)setBrdrFront(brdrFront);
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
        setUseFront(true);
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
    public void setUseFront(boolean useFront){this.useFront = useFront;}
    public void setBrdrFront(int brdrFront){this.brdrFront = brdrFront;}
    public String getName(){return this.name;}
    public int getNumStudent(){return this.numStudent;}
    public boolean getUseFront(){return this.useFront;}

    public void showRoom(){ /*教室情報表示メソッド*/
        System.out.println("教室名: " + this.name);
        System.out.println("座席数: " + this.numSeat);
        System.out.println("列数　: " + this.numClm);
        System.out.println("前方希望の有無: " + this.useFront);
        if(this.useFront)System.out.println("前方とみなす行: " + this.brdrFront + "行まで");
    }

    public void mkClass(boolean rnd){  //学生情報登録メソッド
        students.clear();
        if(rnd){
            Random rand = new Random();
            int numStudent = /*rand.nextInt(40) + 1*/numSeat;  //1-40の範囲で  //numStudent = numSeatにすると空席がなくなる
            setNumStudent(numStudent);

            for(int i=0; i<this.numStudent; i++){
                Student student = new Student(i + 1);
                students.add(student);
            }
        }else{
            String msg = "登録する学生数を入力(座席数は" + numSeat + "席): ";
            this.numStudent = 0;
            while(this.numStudent == 0 || this.numStudent > numSeat)
                this.numStudent = Main.inputNum(msg, sc);

//            System.out.print("前方希望機能を利用しますか？ y / n: ");
//            String tmpStr = sc.next();
//            if(tmpStr.charAt(0) == 'y')useFront = true;
//            else useFront = false;
            for(int i=0; i<numStudent; i++){
                int number = i + 1;
                boolean front = false;
                List<Integer> like = new ArrayList<Integer>();

                System.out.println((i + 1) + "番の学生の情報を入力.");
                if(useFront){
                    System.out.print(number + "番は前方を希望しますか？ y / n: ");
                    String tmpStr = sc.next();
                    if(tmpStr.charAt(0) == 'y')front = true;
                }
                System.out.println(number + "番が近くを希望する学生の番号を入力(" + Student.getNumLike() + "人以内)\nいない場合は0を入力");
                while(like.size() < Student.getNumLike()){
                    msg = (like.size() + 1) + "人目: ";
                    int tmpInt = Main.inputNum(msg, sc);
                    if(tmpInt == 0)like.add(tmpInt);
                    else if(!like.contains(tmpInt))like.add(tmpInt);
                }

                Student student = new Student(number, front, like);
                students.add(student);
                System.out.println((i + 1) + "番目の情報入力完了(残り" + (numStudent - (i + 1)) + "人)\n");
            }
        }
    }
    public void showStudents(int number){ //登録されている学生情報表示メソッド
        if(number == 0){
            System.out.println(this.name+ "の学生情報を表示します.");
            for(int i=0; i<numStudent; i++){
                Student student = students.get(i);
                System.out.println("\n学籍番号: " + student.getNumber());
                System.out.println("前方希望の有無: " + student.getFront());
                for(int j=0; j<Student.getNumLike(); j++)
                    System.out.println("希望者" + j + "人目: "+ student.getLike().get(j));
            }
        }else{
            Student student = students.get(number - 1);
            System.out.println("学籍番号: " + student.getNumber());
            System.out.println("前方希望の有無: " + student.getFront());
            for(int j=0; j<Student.getNumLike(); j++)
                System.out.println("希望者" + j + "人目: "+ student.getLike().get(j));
        }
    }

    public void setSeat(int mode){  //座席指定メソッド
        Student dummy = new Student(-1, false, new ArrayList<Integer>(Student.getNumLike()));
        seat = new ArrayList<Student>(numSeat);
        for(int i=0; i<numSeat; i++)
            seat.add(dummy);

        if(mode == 0){   /*mode == 0: ランダム*/
            Random rand = new Random();
            Boolean[] flg = new Boolean[numSeat];
            int count = 0;
            for(int i=0; count<numSeat; i++){
                int rnd = rand.nextInt(numSeat);
                if(flg[rnd] == null){
                    flg[rnd] = true;
                    if(i < numStudent)seat.set(rnd, students.get(i));
                    else seat.set(rnd, dummy);
                    count++;
                }else i--;
            }
        }
        else if(mode == 1)  /*mode == 1: 順番*/
            for(int i=0; i<numSeat; i++)
                if(i < numStudent)seat.set(i, students.get(i));
                else seat.set(i, dummy);
        else if(mode == 2){ /*mode == 2: 満足度考慮A*/
            /*満足度考慮席替え*/
        }else if(mode == 3){ /*mode == 3: 満足度考慮B*/
            /*満足度考慮席替え*/
        }
    }
    public void showSeat(){ //座席表表示メソッド
        System.out.println(this.name + "の座席表を表示します.");
        System.out.println("\n前方");
        System.out.print("01: ");
        for(int i=0; i<numSeat; i++){
            if(i > 0 && i % numClm == 0)System.out.print("\n" + String.format("%02d", i + 1) + ": ");
            if(seat.get(i).getNumber() == -1)System.out.print("----- ");
            else System.out.print(String.format("%05d", seat.get(i).getNumber()) + " ");
        }
        System.out.println("\n後方");
    }
    public void replaceSeat(){  //指定座席入れ替えメソッド
        int from, to;
        from = to = -1;
        while(from < 0 || from > numSeat)
            from = Main.inputNum("1つ目: ", sc) - 1;
        while(to < 0 || to > numSeat)
            to = Main.inputNum("2つ目: ", sc) - 1;
        Student tmp = seat.get(to);
        seat.set(to, seat.get(from));
        seat.set(from, tmp);
    }
    private int checkNeighbor(Student curStudent, int pos){
        if(curStudent.getLike().contains(seat.get(pos).getNumber())){
//            System.out.println(curStudent.getNumber() + ":" + seat.get(pos).getNumber()); //希望と適合したとみなされた時の番号表示
            return 1;
        }
        return 0;
    }
    public void calculateSatisfaction(int mode){    /*満足度計算メソッド*/
        if(mode == 0){  //mode == 0 8近傍のlikeの数 満足度A
            for(int i=0; i<seat.size(); i++){   //各座席を順番に見ていく
                Student curStudent = seat.get(i);
                int likeCount = 0;
                if(curStudent.getNumber() == -1){    //空席(ダミー)を見ている場合
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
                for(int j=0; j<Student.getNumLike(); j++)   //希望が0(いない)の分カウント
                    if(curStudent.getLike().get(j) == 0)likeCount++;

                seat.get(i).setSatisfaction(likeCount);
          }
        }else if(mode == 1){    //mode == 1:　likeまでの平均距離に逆数 満足度B
            
        }
    }
    public void showSatisfaction(int number){   /*満足度表示メソッド*/
        number--;
        if(number == -1){
            int sumSatisfaction = 0, minSatisfaction = Student.getNumLike() + 1;
            for (Student student : students){
                if(student.getNumber() != -1)sumSatisfaction += student.getSatisfaction();
                if(student.getSatisfaction() < minSatisfaction)minSatisfaction = student.getSatisfaction();
            }
            System.out.println("この教室の満足度の平均は" + (double)sumSatisfaction / students.size());
            System.out.println("この教室の満足度の最低値は" + minSatisfaction);
        }else if(number >= 0 && number < numSeat){
            System.out.println((number + 1) + "番の席の"
                    + students.get(number).getNumber() + "番の学生の満足度は" + students.get(number).getSatisfaction());
            if(students.get(number).getFront())System.out.println(students.get(number).getNumber() + "番の学生は前方希望.");
            System.out.print(students.get(number).getNumber() + "番の学生のlikeは:");
            for(int tmp :students.get(number).getLike())System.out.print(tmp + " ");
            System.out.print("\n");
        }
        else System.out.println("範囲外の番号");
    }
}
