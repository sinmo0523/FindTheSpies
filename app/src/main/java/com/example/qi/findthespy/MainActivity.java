package com.example.qi.findthespy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.qi.findthespy.Activity.Create_room;
import com.example.qi.findthespy.Activity.Join_room;
import com.parse.Parse;

public class MainActivity extends AppCompatActivity {

    private Button mNewGameButton;
    private Button mJoinGameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // [Optional] Power your app with Local Datastore. For more info, go to
        // https://parse.com/docs/android/guide#local-datastore
        Parse.enableLocalDatastore(this);
        Parse.initialize(this);

        mNewGameButton = (Button)findViewById(R.id.newGame_Button);
        mNewGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Create_room.class);
                startActivity(i);
            }
        });
        mJoinGameButton = (Button)findViewById(R.id.joinGame_Button);
        mJoinGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Join_room.class);
                startActivity(i);
            }
        });

    }
}
