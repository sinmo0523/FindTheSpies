package com.example.qi.findthespy.ParseConnection;

import com.example.qi.findthespy.OtherFunction;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

/**
 * Created by sinmo on 2016/1/1.
 */
public class WordHandler {

    public void setWordRand(final String roomId){
        OtherFunction other = new OtherFunction();
        final String wId = String.valueOf(other.createRandomNum(30));

        final ParseQuery query = new ParseQuery("Words");
        query.whereEqualTo("wId", wId);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(final ParseObject object, ParseException e) {
                if (object != null) {
                    ParseQuery queryRoom = new ParseQuery("Rooms");
                    queryRoom.whereEqualTo("objectId", roomId);
                    queryRoom.getInBackground(roomId,new GetCallback < ParseObject > () {
                        @Override
                        public void done (ParseObject room, ParseException e){
                            if (room != null) {
                                room.put("Word", ParseObject.createWithoutData("Words", object.getObjectId()));
                                room.saveInBackground();
                            }
                        }
                    });

                }
            }
        });
    }
}
