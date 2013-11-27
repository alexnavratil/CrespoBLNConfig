package at.alexnavratil.crespoconf;

import android.os.Build;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by alexnavratil on 27.11.13.
 */
public class BlnConfiguration {
    public static final String BLN_SETTINGS_FILE = "/sys/class/misc/notification/enabled";
    public static final String COMPAT_DEVICE = "crespo";

    public boolean checkCompatibility() {
        File f = new File(BLN_SETTINGS_FILE);
        if (Build.DEVICE.equals(COMPAT_DEVICE) && f.exists() && f.isFile()) {
            return true;
        }
        return false;
    }

    public boolean isBlnEnabled() throws FileNotFoundException, IOException {
        BufferedReader br = null;

        br = new BufferedReader(new FileReader(BLN_SETTINGS_FILE));
        String line;
        while ((line = br.readLine()) != null) {
            try {
                int val = Integer.parseInt(line);
                return val == 0 ? false : true;
            } catch (NumberFormatException e) {
            }
        }
        return true;
    }

    public void setBln(boolean enabled) throws IOException {
        BufferedWriter bw = null;

        Runtime.getRuntime().exec("su");
        bw = new BufferedWriter(new FileWriter(BLN_SETTINGS_FILE, false));
        bw.write(enabled ? "1" : "0");
        bw.close();
    }
}
