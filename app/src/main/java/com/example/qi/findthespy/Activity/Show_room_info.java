package com.example.qi.findthespy.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.qi.findthespy.Information.RoomInfoHandler;
import com.example.qi.findthespy.Information.UserInfoHandler;
import com.example.qi.findthespy.MainActivity;
import com.example.qi.findthespy.ParseConnection.UsersHandler;
import com.example.qi.findthespy.ParseConnection.WordHandler;
import com.example.qi.findthespy.R;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by sinmo on 2015/12/29.
 */
public class Show_room_info extends AppCompatActivity {
    private TextView pinInfo;
    private TextView spyNum;
    private TextView normalNum;
    private Button startBtn;
    public RoomInfoHandler room = new RoomInfoHandler();
    public UserInfoHandler userInfo = new UserInfoHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_room_info);

        Intent intent = getIntent();
        String rPin = intent.getStringExtra("data");

        room.setRoomPin(rPin.substring(0, 6));
        room.setSpyNum(Integer.valueOf(rPin.substring(6, 7)));
        room.setNormalNum(Integer.valueOf(rPin.substring(7, 8)));
        room.setUserName(rPin.substring(8));

        userInfo.setUserName(rPin.substring(8));

        pinInfo = (TextView)findViewById(R.id.roomPinInfo);
        spyNum = (TextView) findViewById(R.id.roomInfoSpyEdit);
        normalNum = (TextView) findViewById(R.id.roomInfoNormalEdit);
        startBtn = (Button)findViewById(R.id.roomInfoBnt);

        pinInfo.setText(room.getRoomPin());
        spyNum.setText(spyNum.getText() + String.valueOf(room.getSpyNum()));
        normalNum.setText(normalNum.getText() + String.valueOf(room.getNormalNum()));

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery query = new ParseQuery("Rooms");
                query.whereEqualTo("rPIN", room.getRoomPin());
                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(final ParseObject object, ParseException e) {
                        if (object != null) {
                            userInfo.setRoomId(object.getObjectId());
                            ParseQuery queryExistWord = new ParseQuery("Rooms");
                            queryExistWord.whereDoesNotExist("Word");
                            queryExistWord.whereEqualTo("objectId", userInfo.getRoomId());
                            queryExistWord.getFirstInBackground(new GetCallback<ParseObject>() {
                                @Override
                                public void done(ParseObject room, ParseException e) {
                                    if (room != null) {
                                        WordHandler mWord = new WordHandler();
                                        mWord.setWordRand(userInfo.getRoomId());
                                    }
                                }
                            });
                            setRole(object.getObjectId());
                            String info = object.getObjectId() + room.getSpyNum() + room.getNormalNum() + userInfo.getUserName();
                            Intent intent = new Intent(Show_room_info.this, Show_word_info.class);
                            intent.putExtra("data", info);
                            startActivity(intent);
                        }
                        else {
                        }

                    }
                });
            }
        });
    }

    public void setRole(String rId){
        final String roomId = rId;
        final ParseQuery queryRoom = new ParseQuery("Rooms");
        queryRoom.getInBackground(userInfo.getRoomId(), new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject rm, ParseException e) {
                final ParseObject room = rm;
                if (room != null) {
                    ParseQuery<ParseObject> querySetRole = new ParseQuery("Users");
                    querySetRole.whereEqualTo("inRoom", room);
                    querySetRole.whereEqualTo("uName", userInfo.getUserName());
                    querySetRole.whereDoesNotExist("uRole");
                    querySetRole.getFirstInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject user, ParseException e) {
                            if (user != null) {
                                UsersHandler usersHandler = new UsersHandler();
                                usersHandler.setRole(roomId, user.getObjectId());
                                userInfo.setUserId(user.getObjectId());
                                System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

                            } else {
                                Intent intent = new Intent(Show_room_info.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
                }
            }
        });

    }
}