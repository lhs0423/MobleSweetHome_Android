package com.example.MobleSweetHome.Menu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import com.example.MobleSweetHome.R;

public class VentActivity extends AppCompatActivity {

    // RaspberryPi 환풍기 미구현

    Button returnmenu;
    Switch ventilatorswitch;
    ImageView ventilator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vent);

        setting();
    }

    public void setting() {
        returnmenu =   findViewById(R.id.ventilator_menu);
        ventilatorswitch =  findViewById(R.id.switch_ventilator);
        ventilator =  findViewById(R.id.iv_ventilator);
        ventilatorswitch.setOnCheckedChangeListener(changevetil);
        returnmenu.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

    CompoundButton.OnCheckedChangeListener changevetil = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            if(isChecked){
                ventilator.setImageResource(R.drawable.open);
            }
            else {
                ventilator.setImageResource(R.drawable.close);
            }
        }
    };
}