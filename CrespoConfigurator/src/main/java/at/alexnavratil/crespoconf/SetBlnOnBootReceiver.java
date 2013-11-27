package at.alexnavratil.crespoconf;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by alexnavratil on 27.11.13.
 */
public class SetBlnOnBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        boolean bln_enabled = sharedPref.getBoolean("bln_enabled", true);
        final BlnConfiguration bln = new BlnConfiguration();

        if (bln.checkCompatibility()) {
            try {
                bln.setBln(bln_enabled);
            } catch (FileNotFoundException e) {
            } catch (IOException e) {
            }
        }
    }
}
