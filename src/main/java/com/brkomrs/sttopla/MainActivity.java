package com.brkomrs.sttopla;

import androidx.appcompat.app.AppCompatActivity;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.Button;
import android.widget.CheckBox;
import android.content.Intent;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.EditText;
import java.io.PrintStream;
import java.util.Scanner;
import java.io.File;

public class MainActivity extends AppCompatActivity {
    private String id = "";
    private boolean autolog = false;
    public File configfile = null;
    private ImageButton refbut;
    private ProgressBar bar;
    private TextView conn_label;
    private CheckBox remember_chk,autolog_chk;
    private Button submit_btn;
    private EditText id_inp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        submit_btn = findViewById(R.id.submit_id);
        remember_chk = findViewById(R.id.remember_chk);
        autolog_chk = findViewById(R.id.autologin_chk);

        submit_btn.setEnabled(false);
        //construction of id.txt file, to save
        File path = MainActivity.this.getFilesDir();
        configfile = new File(path, getString(R.string.configfilename_str));
        if (!configfile.exists()) {
            try{
                configfile.createNewFile();
            }catch (Exception e){
                Toast.makeText(MainActivity.this, "Dosya oluşturulamadı. Kayıt Başarısız.", Toast.LENGTH_SHORT).show();
            }

        }
        //getting saved id from file
        id_inp = findViewById(R.id.id_input);
        String fromFile[] = readNLineFromFile(configfile, 2);
        if(!fromFile[0].equalsIgnoreCase("not")){
            remember_chk.setChecked(true);
            id_inp.setText(fromFile[0]);
            if(fromFile[1].equalsIgnoreCase("true")){
                autolog_chk.setChecked(true);
            }else{
                autolog_chk.setChecked(false);
            }
        }else{
            remember_chk.setChecked(false);
        }

        //refresh button
        refbut = findViewById(R.id.refresh_but);
        //at the beginning of app
        //it automatically checks for connection, and moves forward
        moveForward(haveConnection());
        //if first try is failed, then refresh button tries to move forward
        refbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                haveConnection();
            }
        });

    }

    private boolean haveConnection() {

        boolean wifi = false;
        boolean mobile = false;
        ConnectivityManager cn = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] ni_arr = null;
        try {
            ni_arr = cn.getAllNetworkInfo();
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Erişim Sağlanamadı.", Toast.LENGTH_SHORT).show();
        }
        for (NetworkInfo inf : ni_arr) {
            if (inf.getTypeName().equalsIgnoreCase("WIFI")) {
                if (inf.isConnected()) wifi = true;
            } else if (inf.getTypeName().equalsIgnoreCase("Mobile")) {
                if (inf.isConnected()) mobile = true;
            }
        }
        conn_label = findViewById(R.id.connection_label);
        bar = findViewById(R.id.progressBar);

        return wifi || mobile;
    }

    private void moveForward(boolean conn) {
        bar.setVisibility(View.INVISIBLE);
        conn_label.setVisibility(View.INVISIBLE);
             if (!conn) {
                Toast.makeText(MainActivity.this, "Erişim Sağlanamadı.Yenileyin.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MainActivity.this, "Bağlandı", Toast.LENGTH_SHORT).show();
                submit_btn.setEnabled(true);
                submit_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (remember_chk.isChecked()) {
                            id = id_inp.getText().toString();
                            writeToFile(id);
                        }else{
                            writeToFile("not");
                        }
                        autolog = autolog_chk.isChecked();
                        writeToFile(autolog? "true" : "false");
                        //to the next page
                        startActivity(new Intent(MainActivity.this, Missionselect.class));
                    }
                });
            }

    }

    private void writeToFile(String data) {
        PrintStream stream;
        try {
            stream = new PrintStream(configfile);
            stream.println(data);
            stream.close();
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Dosya Bozuk", Toast.LENGTH_LONG).show();
        }
    }



    /**
     * Reads N distinct line from given file
     * @param f  File to read lines
     * @param n  Line Number that we read from file
     * @return  array with n elements that has strings
     */
    public String[] readNLineFromFile(File f, int n) {
        String toRet[] = new String[n];
        Scanner scanfile = null;
        if (!configfile.exists() || !configfile.canRead()) return toRet;
        try{
            scanfile = new Scanner(f);
        }catch (Exception e){
            Toast.makeText(MainActivity.this, "Dosya Bozuk", Toast.LENGTH_LONG).show();
        }
        for(int i = 0; i < n ; i++){
            boolean hasNext = false;
            try{
                hasNext = scanfile.hasNextLine();

            }catch (Exception e){
                Toast.makeText(MainActivity.this, "Dosya Bozuk", Toast.LENGTH_LONG).show();
            }
            if(hasNext){
                toRet[i] = scanfile.nextLine();
            }
        }
        try{
            scanfile.close();
        }catch (Exception e){
            Toast.makeText(MainActivity.this, "Dosya Bozuk", Toast.LENGTH_LONG).show();
        }

        return toRet;
    }

}

