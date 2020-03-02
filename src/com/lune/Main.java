package com.lune;

import java.util.*;

public class Main {
    static int inputNum(String msg, Scanner sc) {   /*数値が入力されなければ繰り返すメソッド*/
        while(true) {
            System.out.print(msg);
            try {return Integer.parseInt(sc.next());}
            catch(NumberFormatException e) {}
        }
    }

    public static Room mkRoom(Room org){
        Scanner sc = new Scanner(System.in);
        System.out.println("教室を作成します.\n作成方法を選んでください.(0: 戻る, 1: ランダム, 2: 手動作成)");
        String input = sc.next();
        char command = input.charAt(0);

        while(command != '0' && command != '1' && command != '2'){
            System.out.println("作成方法を選んでください.(0: 戻る, 1: ランダム, 2: 手動作成)");
            input = sc.next();
            command = input.charAt(0);
        }
        Room room = org;
        String name;
        int numSeat, numClm, brdrFront = 0;
        boolean useFront = false;
        switch(command){
            case '1':
                System.out.print("ランダムで作成します.\n教室名を入力: ");
                name = sc.next();
                room = new Room(name);
                break;
            case '2':
                System.out.print("手動で作成します.\n教室名を入力: ");
                name = sc.next();
                numSeat = inputNum("座席数を入力: ", sc);
                numClm = numSeat + 1;
                while(numClm > numSeat)
                    numClm = inputNum("列数を入力: ", sc);
                System.out.print("前方希望を有効にしますか？( y / n ): ");
                if(sc.next().charAt(0) == 'y')useFront = true;
                if(useFront)
                    brdrFront = inputNum("何行目までを前方としますか？: ", sc);

                room = new Room(name, numSeat, numClm, useFront, brdrFront);
                break;
            default:
                System.out.println("戻ります.");
        }
        System.out.println("以下のように教室を作成しました.");
        room.showRoom();
        return room;
    }

    public static void registerStudent(Room room){
        Scanner sc = new Scanner(System.in);
        System.out.println("新規に学生情報を登録します.\n登録方法を選んでください.(0: 戻る, 1: ランダム, 2: 手動)");
        String input = sc.next();
        char command = input.charAt(0);

        switch(command){
            case '1':
                System.out.println("ランダムに学生を登録します.\n");
                room.mkClass(true);
                break;
            case '2':
                System.out.println("手動で登録します.\n");
                room.mkClass(false);
                break;
            default:
                System.out.println("戻ります.");
                return;
        }
        if(room.getNumStudent() > 0){
            System.out.println("以下のように学生を登録しました.\n");
            room.showStudents(0);
        }
    }

    public static void changeSeat(Room room){
        Scanner sc = new Scanner(System.in);
        System.out.println("席替えを行います.\n席替え方法を選んでください.(0: 戻る, 1: ランダム, 2: 登録順, 3: 満足度考慮A, 3: 満足度考慮B");
        String input = sc.next();
        char command = input.charAt(0);

        switch(command){
            case '1':
                room.setSeat(0);
                System.out.println("ランダムに席替えを行いました.\n");
                break;
            case '2':
                room.setSeat(1);
                System.out.println("番号順に座席を割り振りました.");
                break;
            case '3':
                room.setSeat(2);
                System.out.println("8近傍の希望者の数を指標に満足度を考慮し席替えしました.\n");
                break;
            case '4':
                room.setSeat(3);
                System.out.println("希望者との距離の平均の逆数を指標に満足度を考慮し席替えしました.");
                break;
            default:
                System.out.println("戻ります.");
                return;
        }
        room.showSeat();
    }

    public static void replaceSeat(Room room){
        Scanner sc = new Scanner(System.in);
        System.out.println("指定した座席の入替えをします.\n入れ替える座席の番号を選んでください.(変更しない場合は同じ座席を選択)");
        room.replaceSeat();
        System.out.println("座席の入替えを行いました.");
        room.showSeat();
    }

    public static void calculateSatisfaction(Room room){
        Scanner sc = new Scanner(System.in);
        System.out.println("満足度を表示します.\n満足度の計算方法を選んでください.(0: 戻る, 1: 8近傍の希望者の数, 2: 希望者との距離の平均の逆数");
        String input = sc.next();
        char command = input.charAt(0);

        switch(command){
            case '1':
                room.calculateSatisfaction(0);
                System.out.println("8近傍の希望者の数で満足度を計算します.\n");
                break;
            case '2':
                room.calculateSatisfaction(1);
                System.out.println("希望者との距離の平均の逆数で満足度を計算します.");
                break;
            default:
                System.out.println("戻ります.\n");
        }
        System.out.println("満足度を表示する学生の番号を選んでください.(教室全体の満足度の場合は0)");
        input = sc.next();
        room.showSatisfaction(Integer.parseInt(input));
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Room room = null;
        String input;
        char command;

        while (true) {
            System.out.println("\n行う操作を選んでください.\n" +
                    "(0: 終了, 1: 教室作成, 2: 学生登録, 3: 座席表示, 4: 席替え, 5: 座席入替え, 6: 満足度確認)");
            if(room != null){   //教室が作成済みなら名前と登録済み学生数を表示
                System.out.print("教室名: " + room.getName());
                if(room.getUseFront())System.out.print("(前方希望有)");
                else System.out.print("(前方希望無)");
                if(room.getNumStudent() != 0)System.out.print("(学生" + room.getNumStudent() + "人登録済み)");
                else System.out.println("(学生未登録)");
                System.out.print("\n");
            }
            input = sc.next();
            command = input.charAt(0);
            switch(command){
                case '0':
                    System.out.println("終了します.");
                    System.exit(0);
                    break;
                case '1':
                    room = mkRoom(room);
                    break;
                case '2':
                    if(room == null)
                        System.out.println("教室が作成されていません");
                    else {
                        registerStudent(room);
                        room.setSeat(1);
                    }
                    break;
                case '3':
                    if(room == null)
                        System.out.println("教室が作成されていません");
                    else
                        room.showSeat();

                    break;
                case '4':
                    if(room == null)
                        System.out.println("教室が作成されていません");
                    else
                        changeSeat(room);
                    break;
                case '5':
                    if(room == null)
                        System.out.println("教室が作成されていません");
                    else
                        replaceSeat(room);
                    break;
                case '6':
                    if(room == null)
                        System.out.println("教室が作成されていません");
                    else
                        calculateSatisfaction(room);
                    break;
                default:

            }
        }
//        room = new Room();
//        room.setSeat(1);
//        room.showSeat();
//        room.replaceSeat(0, 1);
//        room.showSeat();
//        room.calculateSatisfaction(0);
//        room.showSatisfaction(0);
    }
}
