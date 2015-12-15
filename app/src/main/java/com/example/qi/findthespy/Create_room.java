package com.example.qi.findthespy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseObject;

/**
 * Created by Zhenyu on 13/12/15.
 */
public class Create_room extends AppCompatActivity {

    private Spinner spinner = null;
    private Integer playerNumber;
    private Button createNewRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_room);

        Spinner spinner = (Spinner) findViewById(R.id.spinner1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                String[] playernumbers = getResources().getStringArray(R.array.playernumber);
               // Toast.makeText(Create_room.this, "你点击的是:" + playernumbers[pos], 2000).show();
                playerNumber = Integer.parseInt(playernumbers[pos]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        createNewRoom = (Button)findViewById(R.id.createNewRoomBnt);
        createNewRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseObject mRooms = new ParseObject("Rooms");
                switch (playerNumber) {
                    case 3: {
                        mRooms.put("sNum", 1);
                        mRooms.put("nNum", 2);
                        mRooms.saveInBackground();
                        break;

                    }
                    case 4: {
                        mRooms.put("sNum", 1);
                        mRooms.put("nNum", 3);
                        mRooms.saveInBackground();
                        break;
                    }
                    case 5: {
                        mRooms.put("sNum", 2);
                        mRooms.put("nNum", 3);
                        mRooms.saveInBackground();
                        break;
                    }
                    case 6: {
                        mRooms.put("sNum", 2);
                        mRooms.put("nNum", 4);
                        mRooms.saveInBackground();
                        break;
                    }
                    case 7: {
                        mRooms.put("sNum", 3);
                        mRooms.put("nNum", 4);
                        mRooms.saveInBackground();
                        break;
                    }
                    case 8: {
                        mRooms.put("sNum", 3);
                        mRooms.put("nNum", 5);
                        mRooms.saveInBackground();
                        break;
                    }
                    case 9: {
                        mRooms.put("sNum", 3);
                        mRooms.put("nNum", 6);
                        mRooms.saveInBackground();
                        break;
                    }
                    case 10: {
                        mRooms.put("sNum", 4);
                        mRooms.put("nNum", 6);
                        mRooms.saveInBackground();
                        break;
                    }
                    default: {
                        mRooms.put("sNum", 1);
                        mRooms.put("nNum", 2);
                        mRooms.saveInBackground();
                        break;
                    }
                }
            }

        });
    }
}
