package com.homersp.asusvolte;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SUUtil {
    private static final String TAG = "AsusVoLTE." + SUUtil.class.getSimpleName();

    public static Builder build() {
        return new Builder();
    }

    private static boolean exec(List<String> lines) {
        try {
            Process p = Runtime.getRuntime().exec("/sbin/su");
            DataOutputStream dos = new DataOutputStream(p.getOutputStream());
            for (String s: lines) {
                dos.writeBytes(s);
                dos.writeBytes("\n");
            }

            dos.writeBytes("exit\n");
            dos.flush();
            dos.close();
            p.waitFor();

            return true;
        } catch (IOException | InterruptedException e) {
            Log.e(TAG, "Exception", e);
        }

        return false;
    }

    public static class Builder {
        private List<String> mLines = new ArrayList<>();

        public Builder add(String s) {
            mLines.add(s);
            return this;
        }

        public boolean exec() {
            return SUUtil.exec(mLines);
        }
    }
}
