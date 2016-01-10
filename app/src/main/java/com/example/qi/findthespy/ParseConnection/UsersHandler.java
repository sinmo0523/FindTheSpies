package com.example.qi.findthespy.ParseConnection;

import android.util.Log;

import com.example.qi.findthespy.Information.UserInfoHandler;
import com.example.qi.findthespy.OtherFunction;
import com.parse.CountCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import java.util.Random;

/**
 * Created by sinmo on 2015/12/29.
 */
public class UsersHandler {

    public void setRole(String roomId, String userId) {

        final String rId = roomId;
        ParseQuery<ParseObject> queryUser = new ParseQuery("Users");
        queryUser.getInBackground(userId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                final ParseObject mUser = object;
                ParseQuery<ParseObject> queryRoom = new ParseQuery("Rooms");
                queryRoom.getInBackground(rId, new GetCallback<ParseObject>() {
                    @Override
                    public void done(final ParseObject room, ParseException e) {
                        if (room != null) {
                            final int total = room.getInt("sNum")+ room.getInt("nNum");
                            final ParseQuery queryCount = new ParseQuery("Users");
                            queryCount.whereEqualTo("inRoom", room);
                            //queryCount.whereEqualTo("uRole","Spy");
                            queryCount.whereExists("uRole");
                            queryCount.countInBackground(new CountCallback() {
                                @Override
                                public void done(int count, ParseException e) {
                                    if (count == 0) { //no one been setted a role
                                        mUser.put("uRole", setRandRole());
                                        mUser.put("uState","Alive");
                                        mUser.saveInBackground();
                                        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1");
                                    }
                                    else{ //some one been setted a role
                                        ParseQuery querySpyNum = new ParseQuery("Users");
                                        querySpyNum.whereEqualTo("inRoom", room);
                                        querySpyNum.whereEqualTo("uRole","Spy");
                                        querySpyNum.countInBackground(new CountCallback() {
                                            @Override
                                            public void done(int count, ParseException e) {
                                                if(count == room.getInt("sNum")){//spy is full
                                                    mUser.put("uRole", "Normal");
                                                    mUser.put("uState","Alive");
                                                    mUser.saveInBackground();
                                                }
                                                else{
                                                    ParseQuery queryNormalNum = new ParseQuery("Users");
                                                    queryNormalNum.whereEqualTo("inRoom", room);
                                                    queryNormalNum.whereEqualTo("uRole","Normal");
                                                    queryNormalNum.countInBackground(new CountCallback() {
                                                        @Override
                                                        public void done(int count, ParseException e) {
                                                            if (count == room.getInt("nNum")){
                                                                mUser.put("uRole", "Spy");
                                                                mUser.put("uState","Alive");
                                                                mUser.saveInBackground();
                                                            }
                                                            else{
                                                                mUser.put("uRole", setRandRole());
                                                                mUser.put("uState","Alive");
                                                                mUser.saveInBackground();
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        });

                                    }

                                }
                            });
                        }
                    }
                });
            }
        });
    }
    public String setRandRole() {
        OtherFunction other = new OtherFunction();
        int randNum = other.createRandomNum(100);
        if (randNum % 2 == 0) {
            return "Spy";
        } else {
            return "Normal";
        }
    }

}

