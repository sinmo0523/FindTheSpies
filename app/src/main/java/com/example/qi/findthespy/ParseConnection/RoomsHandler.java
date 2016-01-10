package com.example.qi.findthespy.ParseConnection;

import android.app.DownloadManager;

import com.example.qi.findthespy.OtherFunction;
import com.example.qi.findthespy.R;
import com.parse.CountCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Random;

/**
 * Created by sinmo on 2015/12/27.
 */
public class RoomsHandler {
    public String oId;
    public ParseObject mWord;
    public int checkPinFlag = 0;
    public int nNum;
    public int sNum;
    public String cName;
    public String pin = new String();

    public String inputRoom(int spyNum, int normalNum, String creatorName) {
        nNum = normalNum;
        sNum = spyNum;
        cName = creatorName;

        do {
            pin = createRoomPIN(creatorName);
            ParseQuery query = new ParseQuery("Rooms");
            query.whereEqualTo("rPIN", pin); //remove this line to count ALL
            query.countInBackground(new CountCallback() {
                @Override
                public void done(int count, ParseException e) {
                    if (count == 0){
                        ParseObject mUser = new ParseObject("Users"); //TODO check name unique
                        ParseObject mRoom = new ParseObject("Rooms");

                        mRoom.put("sNum", sNum);
                        mRoom.put("nNum", nNum);
                        mRoom.put("rPIN", pin);

                        mUser.put("uName", cName);
                        mUser.put("inRoom", mRoom);

                        //UsersHandler user = new UsersHandler();
                        //mUser.put("uRole", user.setRandRole());
                        mUser.saveInBackground();
                    }
                    else
                        checkPinFlag = 2;

                }
            });
        }
        while (checkPinFlag == 2);
        return pin;
    }

    public String createRoomPIN(String creatorName) {
        String PIN = new String();
        OtherFunction other = new OtherFunction();
        int randNum = other.createRandomNum(9999);
        if (randNum < 1000) {
            randNum = 9999 - randNum;
        }
        if(creatorName.substring(0,1) == " ")
        {
            PIN = "*" + creatorName.substring(creatorName.length() - 1)
                    + String.valueOf(randNum);
        }
        else {
            PIN = creatorName.substring(0, 1) + creatorName.substring(creatorName.length() - 1)
                    + String.valueOf(randNum);
        }
        return PIN;
    }
}