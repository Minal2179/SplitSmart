package com.example.minalshettigar.splashscreen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class Profile_edit_options extends AppCompatActivity {

    TextView Name, Email, Conatct;
    Button Edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit_options);

        Edit = (Button) findViewById(R.id.Edit);
        Name = (TextView) findViewById(R.id.Name);
        Email = (TextView) findViewById(R.id.Email);
        Conatct = (TextView) findViewById(R.id.Contact);

        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
