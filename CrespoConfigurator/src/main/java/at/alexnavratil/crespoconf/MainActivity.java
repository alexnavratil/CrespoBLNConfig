package at.alexnavratil.crespoconf;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final BlnConfiguration bln = new BlnConfiguration();
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = sharedPref.edit();

        if (bln.checkCompatibility()) {
            final CheckBox checkBLN = (CheckBox) findViewById(R.id.checkBLN);
            try {
                boolean bln_enabled = bln.isBlnEnabled();
                checkBLN.setChecked(bln_enabled);
                if(sharedPref.getBoolean("bln_enabled", true) != bln_enabled){
                    editor.putBoolean("bln_enabled", bln_enabled);
                    editor.commit();
                }
            } catch (FileNotFoundException e) {
                Toast.makeText(getApplicationContext(), "Back Light Notification (BLN) settings file not found!", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "An error occured while reading BLN settings file!", Toast.LENGTH_LONG).show();
            }

            checkBLN.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    try {
                        bln.setBln(isChecked);
                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), "An error occured while writing to BLN settings file!", Toast.LENGTH_LONG).show();
                    }
                    try {
                        boolean bln_enabled = bln.isBlnEnabled();
                        checkBLN.setChecked(bln_enabled);
                        editor.putBoolean("bln_enabled", bln_enabled);
                        editor.commit();
                    } catch (FileNotFoundException e) {
                        Toast.makeText(getApplicationContext(), "Back Light Notification (BLN) settings file not found!", Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(), "An error occured while reading BLN settings file!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Your device is not supported! This app is only for crespo users.", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
