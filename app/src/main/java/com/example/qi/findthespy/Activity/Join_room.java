package com.example.qi.findthespy.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.qi.findthespy.Information.UserInfoHandler;
import com.example.qi.findthespy.ParseConnection.UsersHandler;
import com.example.qi.findthespy.R;
import com.parse.CountCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.w3c.dom.Text;

/**
 * Created by sinmo on 2015/12/17.
 */
public class Join_room extends AppCompatActivity {

    private Button joinRoom;
    private EditText roomPin;
    private EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_room);

        name = (EditText) findViewById(R.id.inputJoinNameEdit);
        roomPin = (EditText) findViewById(R.id.inputJoinRoomEdit);
        joinRoom =  (Button)findViewById(R.id.joinRoomBnt);

        joinRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UsersHandler mUser = new UsersHandler();
                if (name == null || name.length() <= 0) {
                    new AlertDialog.Builder(Join_room.this).setTitle("Error")
                            .setMessage(R.string.warning_nullPlayerName).setPositiveButton("OK", null)
                            .show();
                }
                if (roomPin == null || roomPin.length() <= 0) {
                    new AlertDialog.Builder(Join_room.this).setTitle("Error")
                            .setMessage(R.string.warning_nullRoomPin).setPositiveButton("OK", null)
                            .show();
                } else {
                    joinRoom(roomPin.getText().toString(), name.getText().toString());
                }
            }
        });



    }


    public void joinRoom(String rPin, final String uName){

        final ParseQuery query = new ParseQuery("Rooms");
        query.whereEqualTo("rPIN", rPin);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(final ParseObject object, ParseException e) {
                if (object != null) {
                    final int total = object.getInt("sNum") + object.getInt("nNum");
                    ParseQuery query1 = new ParseQuery("Users");
                    query1.whereEqualTo("inRoom", object);
                    query1.countInBackground(new CountCallback() {
                        @Override
                        public void done(int count, ParseException e) {
                            if (count == 0) {
                            } else {
                                if (count < total) {
                                    ParseObject mUser = new ParseObject("Users");
                                    mUser.put("uName", uName); //TODO check name unique
                                    mUser.put("inRoom", ParseObject.createWithoutData("Rooms", object.getObjectId()));
                                    mUser.saveInBackground();
                                    mUser.getObjectId();

                                    String info = roomPin.getText().toString()+ object.getInt("sNum") + object.getInt("nNum") + name.getText().toString();
                                    Intent intent = new Intent(Join_room.this, Show_room_info.class);
                                    intent.putExtra("data", info);
                                    startActivity(intent);

                                } else {
                                    new AlertDialog.Builder(Join_room.this).setTitle("Error")
                                            .setMessage(R.string.joinRoomWrong_roomIsFull)
                                            .setPositiveButton("OK", null).show();
                                }
                            }

                        }
                    });
                } else {
                    new AlertDialog.Builder(Join_room.this).setTitle("Error")
                            .setMessage(R.string.joinRoomWrong_roomNotExist)
                            .setPositiveButton("OK", null).show();
                }
            }
        });
    }
}
