package com.example.qi.findthespy.Information;

/**
 * Created by sinmo on 2015/12/31.
 */
public class RoomInfoHandler {
    private String userName = new String();
    private String roomPin = new String();
    private int spyNum = 0;
    private int normalNum = 0;

    public void setUserName(String uName){
        this.userName = uName;
    }

    public void setRoomPin(String rPin){
        this.roomPin = rPin;
    }

    public void setSpyNum(int sNum){
        this.spyNum = sNum;
    }

    public void setNormalNum(int nNum){
        this.normalNum = nNum;
    }

    public String getUserName(){
        return this.userName;
    }

    public String getRoomPin(){
        return this.roomPin;
    }

    public int getSpyNum(){
        return this.spyNum;
    }

    public int getNormalNum(){
        return this.normalNum;
    }

}

