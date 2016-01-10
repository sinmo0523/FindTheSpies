package com.example.qi.findthespy.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.qi.findthespy.Information.UserInfoHandler;
import com.example.qi.findthespy.MainActivity;
import com.example.qi.findthespy.ParseConnection.VoteHandler;
import com.example.qi.findthespy.R;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by sinmo on 2016/1/9.
 */
public class Game_result extends AppCompatActivity {
    TextView spyWord;
    TextView normalWord;
    TextView gameResult;
    Button newGame;
    Button quitGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_result);

        Intent intent = getIntent();
        String info = intent.getStringExtra("data");

        final UserInfoHandler userInfo = new UserInfoHandler();
        userInfo.setRoomId(info.substring(0, 10));
        userInfo.setUserId(info.substring(10, 20));
        final String winner = info.substring(20);

        spyWord = (TextView) findViewById(R.id.spyWordTxt);
        normalWord = (TextView) findViewById(R.id.normalWordTxt);
        gameResult = (TextView) findViewById(R.id.gameResultTxt);
        newGame = (Button) findViewById(R.id.newGameBtn);
        setWord(userInfo.getRoomId());
        setGameResult(userInfo.getUserId(), winner);

        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VoteHandler vote = new VoteHandler();
                vote.deleteVoteList(userInfo.getRoomId());
                ParseQuery queryRoom = new ParseQuery("Rooms");
                queryRoom.getInBackground(userInfo.getRoomId(), new GetCallback<ParseObject>() {
                    @Override
                    public void done(final ParseObject room, ParseException e) {
                        if (room != null) {
                            room.remove("Word");
                            room.saveInBackground();
                            ParseQuery queryUserList = new ParseQuery("Users");
                            queryUserList.whereEqualTo("inRoom", room);
                            queryUserList.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> playerList, ParseException e) {
                                    if (playerList.size() > 0) {
                                        System.out.println("");
                                        String uName = new String();
                                        for (int i = 0; i < playerList.size(); i++) {
                                            if(playerList.get(i).getObjectId().equals(userInfo.getUserId()))
                                            {
                                                uName = playerList.get(i).getString("uName");
                                            }
                                            playerList.get(i).remove("uRole");
                                            playerList.get(i).remove("uState");
                                            playerList.get(i).saveInBackground();

                                        }
                                        String info = room.getString("rPIN") + room.getInt("sNum") + room.getInt("nNum") + uName;
                                        Intent intent = new Intent(Game_result.this, Show_room_info.class);
                                        intent.putExtra("data", info);
                                        startActivity(intent);
                                    }
                                    else {
                                        new AlertDialog.Builder(Game_result.this).setTitle("Error")
                                                .setMessage(R.string.warning_someoneQuit)
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Intent intent = new Intent(Game_result.this, MainActivity.class);
                                                        startActivity(intent);
                                                    }
                                                })
                                                .show();
                                    }
                                }
                            });
                        }
                        else{
                            new AlertDialog.Builder(Game_result.this).setTitle("Error")
                                    .setMessage(R.string.warning_someoneQuit)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(Game_result.this, MainActivity.class);
                                            startActivity(intent);
                                        }
                                    })
                                    .show();
                        }
                    }

                });

            }

        });

    }


        public void setGameResult(String userId,final String winner){
        ParseQuery queryRole = new ParseQuery("Users");
        queryRole.getInBackground(userId, new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject user, ParseException e) {
                        if (user.getString("uRole").equals(winner)) {
                            gameResult.setText("You Win!!!");
                            getWindow().setBackgroundDrawableResource(R.mipmap.winner_background);
                        } else {
                            gameResult.setText("You Lose...");
                            getWindow().setBackgroundDrawableResource(R.mipmap.loser_background);
                        }
                    }
                });
            }

        public void setWord(String roomId){
        final ParseQuery queryRm = new ParseQuery("Rooms");
        queryRm.getInBackground(roomId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject rm, ParseException e) {
                if (rm != null) {
                    final ParseObject room = rm;
                    final ParseObject word = rm.getParseObject("Word");
                    word.fetchIfNeededInBackground(new GetCallback<ParseObject>() {
                        public void done(ParseObject object, ParseException e) {
                            if (object != null) {
                                spyWord.setText(spyWord.getText() + object.getString("sWord"));
                                normalWord.setText(normalWord.getText() + object.getString("nWord"));
                            }
                        }
                    });
                }
            }
        });
    }


}

