package com.brkomrs.sttopla;

import android.os.Bundle;


import android.widget.ArrayAdapter;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Spinner;
import android.widget.Toast;
import com.brkomrs.sttopla.MainActivity;

import java.io.File;
import java.io.FileInputStream;
import android.content.Context;





public class Missionselect extends AppCompatActivity {
    private String[] missions = {"Görev1", "Görev2", "Görev3"};

    private File configfile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missionselect);
        ArrayAdapter<String> dataAdapterMissions;
        Spinner spin = findViewById(R.id.mission_spin);

        dataAdapterMissions = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, missions);
        dataAdapterMissions.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(dataAdapterMissions);


    }


}
