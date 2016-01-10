package com.example.qi.findthespy.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.qi.findthespy.ParseConnection.RoomsHandler;
import com.example.qi.findthespy.R;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Random;

/**
 * Created by Zhenyu on 13/12/15.
 */
public class Create_room extends AppCompatActivity {

    private Spinner spinner = null;
    private Integer playerNumber;
    private Button createNewRoom;
    EditText playerName;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_room);

        Spinner spinner = (Spinner) findViewById(R.id.spinner1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override

            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                String[] playernumbers = getResources().getStringArray(R.array.playerNumber);
                // Toast.makeText(Create_room.this, "你点击的是:" + playernumbers[pos], 2000).show();
                playerNumber = Integer.parseInt(playernumbers[pos]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        createNewRoom = (Button)findViewById(R.id.createNewRoomBnt);
        playerName = (EditText) findViewById(R.id.inputNameEdit);


        createNewRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playerName == null || playerName.length() <= 0) {
                    new AlertDialog.Builder(Create_room.this).setTitle("Error")
                            .setMessage(R.string.warning_nullPlayerName).setPositiveButton("OK", null)
                            .show();
                }
                else {
                    RoomsHandler mRoom = new RoomsHandler();
                    String pin = new String();
                    switch (playerNumber) {
                        case 3: {
                            pin = mRoom.inputRoom(1, 2, playerName.getText().toString()) + "12";
                            break;
                        }
                        case 4: {
                            pin = mRoom.inputRoom(1, 3, playerName.getText().toString()) + "13";
                            break;
                        }
                        case 5: {
                            pin = mRoom.inputRoom(2, 3, playerName.getText().toString()) + "23";
                            break;
                        }
                        case 6: {
                            pin = mRoom.inputRoom(2, 4, playerName.getText().toString()) + "24";
                            break;
                        }
                        case 7: {
                            pin = mRoom.inputRoom(3, 4, playerName.getText().toString()) + "34";
                            break;
                        }
                        case 8: {
                            pin = mRoom.inputRoom(3, 5, playerName.getText().toString()) + "35";
                            break;
                        }
                        case 9: {
                            pin = mRoom.inputRoom(3, 6, playerName.getText().toString()) + "36";
                            break;
                        }
                        case 10: {
                            pin = mRoom.inputRoom(3, 7, playerName.getText().toString()) + "37";
                            break;
                        }
                        default: {
                            pin = mRoom.inputRoom(1, 3, playerName.getText().toString()) + "13";
                            break;
                        }
                    }
                    pin += playerName.getText().toString();
                    Intent intent = new Intent(Create_room.this, Show_room_info.class);
                    intent.putExtra("data", pin);
                    startActivity(intent);

                }
            }
        });

    }
}
