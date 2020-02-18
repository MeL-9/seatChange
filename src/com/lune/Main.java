package com.lune;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Room room = new Room("A教室");
        room.mkClass(true);
        room.setSeat(0);
        room.showSeat();
        room.replaceSeat(0, 1);
        room.showSeat();
    }
}
