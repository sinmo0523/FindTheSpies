package com.example.qi.findthespy.Information;

/**
 * Created by sinmo on 2015/12/31.
 */
public class UserInfoHandler {
    private String userName = new String();
    private String roomId = new String();
    private String word = new String();
    private String userId = new String();
    private String userRole = new String();


    public void setUserName(String uName){
        this.userName = uName;
    }

    public void setRoomId(String rId){
        this.roomId = rId;
    }

    public void setWord(String sWord){
        this.word = sWord;
    }

    public void setUserId(String uId){
        this.userId = uId;
    }

    public void setUserRole(String uRole) {
        this.userRole = uRole;
    }

    public String getUserName(){
        return userName;
    }

    public String getUserId(){
        return userId;
    }

    public String getRoomId(){
        return roomId;
    }

    public String getWord(){
        return word;
    }

    public String getUserRole(){
        return  userRole;
    }
}
