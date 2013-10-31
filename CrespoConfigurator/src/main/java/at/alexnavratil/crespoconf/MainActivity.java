package at.alexnavratil.crespoconf;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
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

    public static final String BLN_SETTINGS_FILE = "/sys/class/misc/notification/enabled";
    public static final String COMPAT_DEVICE = "crespo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(checkCompatibility()){
            final CheckBox checkBLN = (CheckBox) findViewById(R.id.checkBLN);
            checkBLN.setChecked(isBlnEnabled());

            checkBLN.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    setBln(isChecked);
                    checkBLN.setChecked(isBlnEnabled());
                }
            });
        } else{
            Toast.makeText(getApplicationContext(), "Your device is not supported! This app is only for crespo users.", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private boolean checkCompatibility(){
        File f = new File(BLN_SETTINGS_FILE);
        if(Build.DEVICE.equals(COMPAT_DEVICE) && f.exists() && f.isFile()){
            return true;
        }
        return false;
    }

    private boolean isBlnEnabled(){
        BufferedReader br = null;
        try{
            br = new BufferedReader(new FileReader(BLN_SETTINGS_FILE));
            String line;
            while((line = br.readLine()) != null){
                try{
                    int val = Integer.parseInt(line);
                    return val==0 ? false : true;
                } catch(NumberFormatException e){}
            }
        } catch(FileNotFoundException e){
            Toast.makeText(getApplicationContext(), "Back Light Notification (BLN) settings file not found!", Toast.LENGTH_LONG).show();
        } catch (IOException e){
            Toast.makeText(getApplicationContext(), "An error occured while reading BLN settings file!", Toast.LENGTH_LONG).show();
        }
        return true;
    }

    private void setBln(boolean enabled){
        BufferedWriter bw = null;
        try{
            Runtime.getRuntime().exec("su");
            bw = new BufferedWriter(new FileWriter(BLN_SETTINGS_FILE, false));
            bw.write(enabled ? "1" : "0");
            bw.close();
        } catch(IOException e){
            Toast.makeText(getApplicationContext(), "An error occured while writing to BLN settings file!", Toast.LENGTH_LONG).show();
        }
    }

}
