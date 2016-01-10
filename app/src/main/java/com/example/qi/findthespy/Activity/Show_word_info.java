package com.example.qi.findthespy.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.qi.findthespy.Information.RoomInfoHandler;
import com.example.qi.findthespy.Information.UserInfoHandler;
import com.example.qi.findthespy.ParseConnection.UsersHandler;
import com.example.qi.findthespy.ParseConnection.WordHandler;
import com.example.qi.findthespy.R;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by sinmo on 2015/12/30.
 */
public class Show_word_info extends AppCompatActivity {
    private TextView wordTxt;
    private TextView userIdTxt;
    private Thread thread;
    private Button voteButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_word_info);

        Intent intent = getIntent();
        String info = intent.getStringExtra("data");

        final RoomInfoHandler roomInfo = new RoomInfoHandler();
        final UserInfoHandler userInfo = new UserInfoHandler();
        userInfo.setRoomId(info.substring(0, 10));
        userInfo.setUserName(info.substring(12));

        roomInfo.setSpyNum(Integer.valueOf(info.substring(10, 11)));
        roomInfo.setNormalNum(Integer.valueOf(info.substring(11, 12)));

        wordTxt = (TextView)findViewById(R.id.wordTxt);
        voteButton = (Button)findViewById(R.id.goVote);
        userIdTxt = (TextView)findViewById(R.id.userIdShow);

        voteButton.setClickable(false);

        thread=  new Thread(){
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        wait(8000);
                    }
                } catch (InterruptedException ex) {
                }
                final ParseQuery queryRm = new ParseQuery("Rooms");
                queryRm.getInBackground(userInfo.getRoomId(), new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject rm, ParseException e) {
                        if(rm != null) {
                            final ParseObject room = rm;
                            final ParseObject word = rm.getParseObject("Word");
                            word.fetchIfNeededInBackground(new GetCallback<ParseObject>() {
                                public void done(ParseObject object, ParseException e) {
                                    if (object != null) {
                                        final String spyWord = object.getString("sWord");
                                        final String normalWord = object.getString("nWord");

                                        System.out.println(spyWord + "@@@@@@@@@@@@@@@@@@@@@@@@@@@" + normalWord);
                                        ParseQuery queryRole = new ParseQuery("Users");
                                        queryRole.whereEqualTo("inRoom", room);
                                        queryRole.whereEqualTo("uName", userInfo.getUserName());
                                        queryRole.whereExists("uRole");
                                        queryRole.getFirstInBackground(new GetCallback<ParseObject>() {
                                            @Override
                                            public void done(ParseObject user, ParseException ea) {
                                                if (user != null) {
                                                    System.out.println(user.getString("uRole") + "##############################");
                                                    userIdTxt.setText(user.getObjectId());
                                                    if (user.getString("uRole").equals("Spy")) {
                                                        wordTxt.setText(spyWord);
                                                        wordTxt.setTextColor(0xFFDE3970);
                                                    } else if (user.getString("uRole").equals("Normal")) {
                                                        wordTxt.setText(normalWord);
                                                        wordTxt.setTextColor(0xFFDE3970);
                                                        voteButton.setClickable(true);
                                                    } else {
                                                        wordTxt.setText("Error-role");
                                                    }
                                                } else {
                                                    wordTxt.setText("Error-user");
                                                }
                                            }

                                        });
                                    } else {
                                        wordTxt.setText("Error");
                                    }
                                }
                            });
                        } else{
                            wordTxt.setText("Error!");
                        }

                    }

                });
            }
        };
        thread.start();


        voteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String info = userInfo.getRoomId() + roomInfo.getSpyNum() + roomInfo.getNormalNum()
                        + userIdTxt.getText().toString() + wordTxt.getText().toString();
                Intent intent = new Intent(Show_word_info.this, Vote.class);
                intent.putExtra("data", info);
                startActivity(intent);

            }
        });
    }

}
