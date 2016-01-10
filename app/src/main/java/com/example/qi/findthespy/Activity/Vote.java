package com.example.qi.findthespy.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.qi.findthespy.Information.RoomInfoHandler;
import com.example.qi.findthespy.Information.UserInfoHandler;
import com.example.qi.findthespy.ParseConnection.VoteHandler;
import com.example.qi.findthespy.R;
import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sinmo on 2016/1/6.
 */
public class Vote extends AppCompatActivity {
    ListView playerList;
    Button refreshBtn;
    Button vFinishBtn;
    TextView roundTxt;
    List<String> playerItems = new ArrayList<String>();
    ArrayAdapter<String> playerListAdapter;
    int round = 1;
    int voted = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vote_activity);

        Intent intent = getIntent();
        String info = intent.getStringExtra("data");

        System.out.println(info + "11111111111111111111111111111111111");

        final UserInfoHandler userInfo = new UserInfoHandler();
        final RoomInfoHandler roomInfo = new RoomInfoHandler();
        userInfo.setRoomId(info.substring(0, 10));
        roomInfo.setSpyNum(Integer.valueOf(info.substring(10, 11)));
        roomInfo.setNormalNum(Integer.valueOf(info.substring(11, 12)));
        userInfo.setUserId(info.substring(12, 22));

        playerList = (ListView)findViewById(R.id.voteList);
        vFinishBtn = (Button)findViewById(R.id.voteFinishBtn);
        refreshBtn = (Button)findViewById(R.id.voteListRefreshBtn);
        roundTxt = (TextView)findViewById(R.id.roundTxt);

        roundTxt.setText("Round " + round);
        playerListAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, playerItems);
        round = 1;
        getList(userInfo.getUserId(), userInfo.getRoomId(), roomInfo.getNormalNum(), roomInfo.getSpyNum());

        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerItems.clear();
                getList(userInfo.getUserId(), userInfo.getRoomId(), roomInfo.getNormalNum(), roomInfo.getSpyNum());
            }
        });

        vFinishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VotedFinish(roomInfo.getNormalNum() + roomInfo.getSpyNum(), userInfo.getRoomId(), round, userInfo.getUserId());
                checkWin(userInfo.getRoomId(), userInfo.getUserId(),roomInfo.getSpyNum(),roomInfo.getNormalNum());
                playerItems.clear();
                getList(userInfo.getUserId(), userInfo.getRoomId(), roomInfo.getNormalNum(), roomInfo.getSpyNum());
            }
    });

        playerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(voted + "^^^^^^^^^^^!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                if (voted == 0) {
                    final String selectedItem = playerListAdapter.getItem(position);
                    if (selectedItem.length() > 7) {
                        if (selectedItem.substring(selectedItem.length() - 6, selectedItem.length() - 1).equals("Voted")) {
                            new AlertDialog.Builder(Vote.this).setTitle("Warning")
                                    .setMessage(R.string.warning_voteMemberVoted)
                                    .setPositiveButton("OK", null)
                                    .show();
                        }
                    } else {
                        new AlertDialog.Builder(Vote.this).setTitle("Warning")
                                .setMessage("Are you sure" + selectedItem + " is a spy?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        VoteHandler vote = new VoteHandler();
                                        vote.voteUser(userInfo.getRoomId(), selectedItem, round);
                                    }
                                })
                                .setNegativeButton("No", null)
                                .show();
                        voted = 1;

                    }
                }
                else if (voted == 1){
                    new AlertDialog.Builder(Vote.this).setTitle("Warning")
                            .setMessage(R.string.warning_vote)
                            .setPositiveButton("OK", null)
                            .show();
                }

                else if (voted == 2){
                    new AlertDialog.Builder(Vote.this).setTitle("Warning")
                            .setMessage(R.string.warning_voteMemberLeft)
                            .setPositiveButton("OK", null).show();
                }
                else if (voted == 3){
                    new AlertDialog.Builder(Vote.this).setTitle("Warning")
                            .setMessage(R.string.warning_votedMeBlocked)
                            .setPositiveButton("OK", null).show();
                }
           }
        });

    }

    public void getList(String userId, String roomId, int normalNum, int spyNum){
        final String uId = userId;
        final String rId = roomId;
        final int nNum = normalNum;
        final int sNum = spyNum;
        round = 1;
        final ParseQuery queryRoom = new ParseQuery("Rooms");
        queryRoom.getInBackground(rId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject room, ParseException e) {
                if (room != null) {
                    ParseQuery queryPlayerList = new ParseQuery("Users");
                    queryPlayerList.whereMatchesQuery("inRoom", queryRoom);
                    queryPlayerList.whereNotEqualTo("objectId", uId);
                    queryPlayerList.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> players, ParseException e) {
                            if (players.size() != 0) {
                                for (int i = 0; i < players.size(); i++) {
                                    if (players.get(i).getString("uState").equals("Voted")) {
                                        playerItems.add(players.get(i).getString("uName") + "(Voted)");
                                        round++;
                                    } else
                                        playerItems.add(players.get(i).getString("uName"));
                                    roundTxt.setText("Round " + round);
                                }
                                playerList.setAdapter(playerListAdapter);
                            }
                            if (players.size() != (nNum + sNum - 1)) {
                                new AlertDialog.Builder(Vote.this).setTitle("Warning")
                                        .setMessage(R.string.warning_voteMemberLeft)
                                        .setPositiveButton("OK", null).show();
                                voted = 2;
                            }
                        }

                    });
                } else {
                }
            }
        });
    }

    public void VotedFinish(final int totalNum, String roomId, int round,final String userId){
        ParseQuery queryCount = new ParseQuery("Vote");
        queryCount.whereEqualTo("inRoom", roomId);
        queryCount.whereEqualTo("vRound", round);
        queryCount.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> voteList, ParseException e) {
                int votedTime = 0;
                int mostVoted = 0;
                String votedUserId = new String();
                for (int i = 0; i < voteList.size(); i++) {
                    votedTime += voteList.get(i).getInt("getVoteNum");
                    if (voteList.get(i).getInt("getVoteNum") > mostVoted) {
                        mostVoted = voteList.get(i).getInt("getVoteNum");
                        votedUserId = voteList.get(i).getString("votedUser");
                    }
                }
                if (votedTime == totalNum) {
                    ParseQuery queryVotedUser = new ParseQuery("Users");
                    queryVotedUser.getInBackground(votedUserId, new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject user, ParseException e) {
                            if (user != null) {
                                user.put("uState", "Voted");
                                user.saveInBackground();
                                new AlertDialog.Builder(Vote.this).setTitle("Warning")
                                        .setMessage(user.getString("uName") + " has been voted in this round!")
                                        .show();
                            }

                            if (user.getObjectId().equals(userId)) {
                                new AlertDialog.Builder(Vote.this).setTitle("Warning")
                                        .setMessage(R.string.warning_votedMe)
                                        .show();
                                voted = 3;
                            }
                        }
                    });
                } else {
                    new AlertDialog.Builder(Vote.this).setTitle("Warning")
                            .setMessage(R.string.warning_voteNotFinish)
                            .show();
                }
            }
        });
    }

    public void checkWin(final String roomId, final String userId, final int spyNum, final int normalNum){
        ParseQuery queryRoom = new ParseQuery("Rooms");
        queryRoom.getInBackground(roomId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject room, ParseException e) {
                if (room != null) {
                    ParseQuery queryCountSpy = new ParseQuery("Users");
                    queryCountSpy.whereEqualTo("uState", "Voted");
                    queryCountSpy.whereEqualTo("uRole", "Spy");
                    queryCountSpy.whereEqualTo("inRoom", room);
                    queryCountSpy.countInBackground(new CountCallback() {
                        @Override
                        public void done(int count, ParseException e) {
                            if (count == spyNum) {
                                String info = roomId + userId + "Normal";
                                Intent intent = new Intent(Vote.this, Game_result.class);
                                intent.putExtra("data", info);
                                startActivity(intent);
                            }
                        }
                    });
                    ParseQuery queryCountNormal = new ParseQuery("Users");
                    queryCountNormal.whereEqualTo("uState", "Voted");
                    queryCountNormal.whereEqualTo("uRole", "Normal");
                    queryCountNormal.whereEqualTo("inRoom",room);
                    queryCountNormal.countInBackground(new CountCallback() {
                        @Override
                        public void done(int count, ParseException e) {

                            if(count == normalNum - 1){
                                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                                String info = roomId + userId + "Spy";
                                Intent intent = new Intent(Vote.this, Game_result.class);
                                intent.putExtra("data", info);
                                startActivity(intent);
                            }
                        }
                    });

                } else {
                }
            }

        });

    }
}