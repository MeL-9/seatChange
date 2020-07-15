package com.lune;

import java.io.Serializable;
import java.util.*;

public class Room implements Serializable {
    String name;
    int numStudent = 0, numSeat, numClm, brdrFront;
    ArrayList<Student> students, seat;
    boolean useFront = false;
    Scanner sc = new Scanner(System.in);

    /*コンストラクタ*/
    Room(String name, int numSeat, int numClm, boolean useFront, int brdrFront){
        setName(name);
        setNumSeat(numSeat);
        setNumClm(numClm);
        setUseFront(useFront);
        if(useFront)setBrdrFront(brdrFront);
        students = new ArrayList<Student>();
    }
    /*教室名以外はランダム*/
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
    /*テスト用*/
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
        ArrayList<Integer> like = new ArrayList<Integer>();
        like.add(1);
        like.add(2);
        like.add(3);
        for(int i=0; i<numStudent; i++){
            student = new Student(i + 1, false, like);
            students.add(student);
        }
    }
    /*各種セッタ、ゲッタ*/
    public void setName(String name){this.name = name;}
    public void setNumStudent(int numStudent){this.numStudent = numStudent;}
    public void setNumSeat(int numSeat){this.numSeat = numSeat;}
    public void setNumClm(int numClm) {this.numClm = numClm;}
    public void setUseFront(boolean useFront){this.useFront = useFront;}
    public void setBrdrFront(int brdrFront){this.brdrFront = brdrFront;}
    public String getName(){return this.name;}
    public int getNumStudent(){return this.numStudent;}
    public boolean getUseFront(){return this.useFront;}

    /*教室情報表示メソッド*/
    public void showRoom(){
        System.out.println("教室名: " + this.name);
        System.out.println("座席数: " + this.numSeat);
        System.out.println("列数　: " + this.numClm);
        System.out.println("前方希望の有無: " + this.useFront);
        if(this.useFront)System.out.println("前方とみなす行: " + this.brdrFront + "行まで");
    }

    /*学生登録メソッド*/
    public void mkClass(boolean rnd){
        students.clear();
        if(rnd){    //rndがtrueならランダムで学生を生成
            Random rand = new Random();
            int numStudent = /*rand.nextInt(40) + 1*/numSeat;  //1-40の範囲で  //numStudent = numSeatにすると空席がなくなる
            setNumStudent(numStudent);

            for(int i=0; i<this.numStudent; i++){
                Student student = new Student(i + 1);   //numberのみの指定でランダムに学生を生成する
                students.add(student);
            }
        }else{  //手動で学生情報を入力
            String msg = "登録する学生数を入力(座席数は" + numSeat + "席): ";
            this.numStudent = 0;
            while(this.numStudent <= 0 || this.numStudent > numSeat)
                this.numStudent = Main.inputNum(msg, sc);

            for(int i=0; i<numStudent; i++){
                int number = i + 1;
                boolean front = false;
                ArrayList<Integer> like = new ArrayList<Integer>();

                System.out.println((i + 1) + "番の学生の情報を入力.");
                if(useFront){
                    System.out.print(number + "番は前方を希望しますか？ y / n: ");
                    String tmpStr = sc.next();
                    if(tmpStr.charAt(0) == 'y')front = true;
                }
                System.out.println(number + "番が近くを希望する学生の番号を入力(" + Student.getNumLike() + "人以内)\nいない場合は0を入力");
                while(like.size() < Student.getNumLike()) {
                    msg = (like.size() + 1) + "人目: ";
                    int tmpInt = Main.inputNum(msg, sc);
                    if (tmpInt == 0) {
                        like.add(tmpInt);
                        continue;
                    } else if (like.isEmpty()) {  //自分の番号または被りを防止
                        if (tmpInt == number) continue;
                    } else if (tmpInt == number || like.contains(tmpInt)) continue;
                    like.add(tmpInt);
                }

                Student student = new Student(number, front, like);
                students.add(student);
                System.out.println((i + 1) + "番目の情報入力完了(残り" + (numStudent - (i + 1)) + "人)\n");
            }
        }
    }

    /*登録学生表示メソッド*/
    public void showStudents(int number){
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

    /*座席設定メソッド*/
    public void setSeat(int mode){
        //空席用のダミー
        Student dummy = new Student(-1, false, new ArrayList<Integer>(Student.getNumLike()));
        seat = new ArrayList<Student>(numSeat);
        for(int i=0; i<numSeat; i++)    //座席数分のサイズを確保するために一旦ダミーで満たす
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
            ArrayList<Student> tmpSeat = new ArrayList<Student>(seat);

            for (int i = 0; i < 10000; i++) {
                
            }
        }
    }

    /*座席表表示メソッド*/
    public void showSeat(){
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

    /*指定座席入れ替えメソッド*/
    public void replaceSeat(){
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

    /*posの座席がlikeに含まれる番号を持つか調べるメソッド*/
    private int checkNeighbor(Student curStudent, int pos){
        if(curStudent.getLike().contains(seat.get(pos).getNumber())){
//            System.out.println(curStudent.getNumber() + ":" + seat.get(pos).getNumber()); //希望と適合したとみなされた時の番号表示
            return 1;
        }
        return 0;
    }
    /*満足度計算メソッド*/
    public void calculateSatisfaction(int mode){
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
        }/*else if(mode == 1){    //mode == 1:　likeまでの平均距離の逆数 満足度B    //実装途中
            int curx, cury, x, y, indexLike;
            for(int i=0; i<seat.size(); i++){   //各座席を順番に見ていく
                Student curStudent = seat.get(i);
                int sumDistance = 0;
                x = y = -1;

                if(curStudent.getNumber() == -1){    //空席(ダミー)を見ている場合
                    seat.get(i).setSatisfaction(0);
                    continue;
                }
                if(useFront && i / numClm >= brdrFront && curStudent.getFront()){  //前方希望での場合
                    seat.get(i).setSatisfaction(0);
                    continue;
                }

                curx = i % this.numClm;
                cury = i / numClm;
                for (int j=0; j<seat.size(); j++) {
                    if(curStudent.getLike().contains(seat.get(j).getNumber())){
                        
                    }
                }

                for (Integer like : curStudent.getLike()) {
                    for(int j=0; j<seat.size(); j++) {
                        if (seat.get(j).getNumber() == like) {
                            indexLike = j;
                            x = indexLike % this.numClm;
                            y = indexLike / this.numClm;
                            break;
                        }
                    }
                }

                for(int j=0; j<Student.getNumLike(); j++)   //希望が0(いない)の分カウント
                    if(curStudent.getLike().get(j) == 0)sumDistance++;

                seat.get(i).setSatisfaction((double)sumDistance / Student.getNumLike());
            }
        }*/
    }

    /*満足度表示メソッド*/
    public void showSatisfaction(int number){
        number--;
        if(number == -1){
            double sumSatisfaction = 0, minSatisfaction = Student.getNumLike() + 1;
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
