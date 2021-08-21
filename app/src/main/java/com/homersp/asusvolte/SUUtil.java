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

    public static Builder build(String prog) {
        return new Builder(prog);
    }

    private static boolean exec(String prog, List<String> lines) {
        try {
            Process p = Runtime.getRuntime().exec(prog);
            DataOutputStream dos = new DataOutputStream(p.getOutputStream());
            for (String s: lines) {
                dos.writeBytes(s);
                dos.writeBytes("\n");
                dos.flush();
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
        private String mProg;
        private List<String> mLines = new ArrayList<>();

        public Builder() {
            this("su");
        }

        public Builder(String prog) {
            mProg = prog;
        }

        public Builder add(String s) {
            mLines.add(s);
            return this;
        }

        public boolean exec() {
            return SUUtil.exec(mProg, mLines);
        }
    }
}
