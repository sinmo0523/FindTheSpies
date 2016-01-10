package com.example.qi.findthespy.ParseConnection;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by sinmo on 2016/1/9.
 */
public class VoteHandler {
    public void voteUser(String roomId, final String voteName,int round){
        final int vRound = round;
        final String rId = roomId;
        ParseQuery queryRoom = new ParseQuery("Rooms");
        queryRoom.getInBackground(rId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject room, ParseException e) {
                if (room != null) {
                    ParseQuery queryUser = new ParseQuery("Users");
                    queryUser.whereEqualTo("inRoom", room);
                    queryUser.whereEqualTo("uName", voteName);
                    queryUser.getFirstInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject user, ParseException e) {
                            if (user != null) {
                                final ParseObject votedUser = user;
                                ParseQuery queryVote = new ParseQuery("Vote");
                                queryVote.whereEqualTo("inRoom", rId);
                                queryVote.whereEqualTo("vRound",vRound);
                                queryVote.whereEqualTo("votedUser", user.getObjectId());
                                queryVote.getFirstInBackground(new GetCallback<ParseObject>() {
                                    @Override
                                    public void done(ParseObject vote, ParseException e) {
                                        if (vote != null) {
                                            vote.put("getVoteNum", vote.getInt("getVoteNum") + 1);
                                            vote.saveInBackground();
                                        } else {
                                            ParseObject mVote = new ParseObject("Vote");
                                            mVote.put("votedUser", votedUser.getObjectId());
                                            mVote.put("inRoom", rId);
                                            mVote.put("getVoteNum", 1);
                                            mVote.put("vRound",vRound);
                                            mVote.saveInBackground();
                                        }
                                    }
                                });
                            }
                        }

                    });
                } else {
                }
            }

        });
    }

    public void deleteVoteList(String roomId){
        ParseQuery queryVoteDelete = new ParseQuery("Vote");
        queryVoteDelete.whereEqualTo("inRoom", roomId);
        queryVoteDelete.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> voteList, ParseException e) {
                if (voteList.size() > 0) {
                    for (int i = 0; i < voteList.size(); i++) {
                        voteList.get(i).deleteInBackground();
                    }
                }
            }

        });
    }
}