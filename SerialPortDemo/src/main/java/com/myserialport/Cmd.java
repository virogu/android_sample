package com.myserialport;
/*
    FE 00 01 00 00 EA 60 4B
    FE   00 01     00 00 EA 60      4B
    头部  协议头     ACC延时时间 ms    FE后六位之和
 */

public interface Cmd {
    String OPEN_DOOR = "010100000000FFFF";
    String ACC_DELAY_60 = "FE00010000EA604B";
    String ACC_DELAY_0 = "FE00010000000001";
}
