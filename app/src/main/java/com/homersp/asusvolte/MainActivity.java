package com.homersp.asusvolte;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.enable_volte).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableVoLTE();
            }
        });

        findViewById(R.id.mode_diag).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableDiag();
            }
        });

        findViewById(R.id.mode_pdc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enablePDC();
            }
        });

        findViewById(R.id.restart_modem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetConnection();
            }
        });

        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDiag();
            }
        });

        findViewById(R.id.btn_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopDiag();
            }
        });
    }

    @Override
    public void onPause()
    {
        super.onPause();

        saveConfig();
    }

    @Override
    public void onResume()
    {
        super.onResume();

        SharedPreferences prefs = getSharedPreferences("config", Context.MODE_PRIVATE);
        ((EditText) findViewById(R.id.text_ip)).setText(prefs.getString("address", getString(R.string.def_address)));
        ((EditText) findViewById(R.id.text_port)).setText(prefs.getString("port", getString(R.string.def_port)));
    }

    private void enableVoLTE()
    {
        boolean ret = SUUtil.build()
                .add("setprop persist.vendor.dbg.ims_volte_enable 1")
                .add("setprop persist.vendor.dbg.volte_avail_ovr 1")
                .add("setprop persist.vendor.dbg.vt_avail_ovr 1")
                .add("setprop persist.vendor.dbg.wfc_avail_ovr 1")
                .exec();

        if (!ret) {
            Toast.makeText(MainActivity.this, "Failed enabling VoLTE", Toast.LENGTH_SHORT).show();
        }
    }

    private void enableDiag()
    {
        boolean ret = SUUtil.build()
                .add("setprop sys.usb.diag 2")
                .exec();

        if (!ret) {
            Toast.makeText(MainActivity.this, "Failed enabling DIAG, are you rooted?", Toast.LENGTH_SHORT).show();
        }
    }

    private void enablePDC()
    {
        boolean ret = SUUtil.build()
                .add("setprop persist.sys.usb.config diag,serial_cdev,rmnet,adb")
                .add("setprop sys.usb.config diag,serial_cdev,rmnet,adb")
                .add("rmnetfunc=$(getprop vendor.usb.rmnet.func.name)")
                .add("rmnetinst=$(getprop vendor.usb.rmnet.inst.name)")
                .add("controller=$(getprop sys.usb.controller)")
                .add("config=$(getprop sys.usb.config)")
                .add("echo \"Default composition\" > /config/usb_gadget/g1/configs/b.1/strings/0x409/configuration")
                .add("rm /config/usb_gadget/g1/configs/b.1/f1")
                .add("rm /config/usb_gadget/g1/configs/b.1/f2")
                .add("rm /config/usb_gadget/g1/configs/b.1/f3")
                .add("rm /config/usb_gadget/g1/configs/b.1/f4")
                .add("rm /config/usb_gadget/g1/configs/b.1/f5")
                .add("rm /config/usb_gadget/g1/configs/b.1/f6")
                .add("rm /config/usb_gadget/g1/configs/b.1/f7")
                .add("rm /config/usb_gadget/g1/configs/b.1/f8")
                .add("rm /config/usb_gadget/g1/configs/b.1/f9")
                .add("echo \"0x05C6\" > /config/usb_gadget/g1/idVendor")
                .add("echo \"0x9091\" > /config/usb_gadget/g1/idProduct")
                .add("ln -s /config/usb_gadget/g1/functions/diag.diag /config/usb_gadget/g1/configs/b.1/f1")
                .add("ln -s /config/usb_gadget/g1/functions/cser.dun.0 /config/usb_gadget/g1/configs/b.1/f2")
                .add("ln -s /config/usb_gadget/g1/functions/${rmnetfunc}.${rmnetinst} /config/usb_gadget/g1/configs/b.1/f3")
                .add("ln -s /config/usb_gadget/g1/functions/ffs.adb /config/usb_gadget/g1/configs/b.1/f4")
                .add("echo \"${controller}\" > /config/usb_gadget/g1/UDC")
                .add("setprop sys.usb.state ${config}")
                .exec();

        if (!ret) {
            Toast.makeText(MainActivity.this, "Failed enabling PDC, are you rooted?", Toast.LENGTH_SHORT).show();
        }
    }

    private void resetConnection()
    {
        boolean ret =SUUtil.build()
                .add("setprop ctl.restart vendor.qcrild")
                .exec();

        if (!ret) {
            Toast.makeText(MainActivity.this, "Failed resetting connection, are you rooted?", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveConfig()
    {
        String address = ((EditText) findViewById(R.id.text_ip)).getText().toString();
        String port = ((EditText) findViewById(R.id.text_port)).getText().toString();

        SharedPreferences prefs = getSharedPreferences("config", Context.MODE_PRIVATE);
        prefs.edit()
                .putString("address", address)
                .putString("port", port)
                .apply();
    }

    private void startDiag()
    {
        saveConfig();

        new StartDiagTask(this).execute();
    }

    private void stopDiag()
    {
        new StopDiagTask().execute();
    }

    private static class StartDiagTask extends AsyncTask<Void, Void, Void> {
        private String mAddress;
        private String mPort;
        private Process mProcess = null;

        StartDiagTask(Context context) {
            SharedPreferences prefs = context.getSharedPreferences("config", Context.MODE_PRIVATE);
            mAddress = prefs.getString("address", context.getString(R.string.def_address));
            mPort = prefs.getString("port", context.getString(R.string.def_port));
        }

        @Override
        protected Void doInBackground(Void... voids) {
            SUUtil.build()
                    .add("diag_socket_log -k")
                    .add("diag_socket_log -a \"" + mAddress + "\" -p \"" + mPort + "\"")
                    .exec();

            return null;
        }
    }

    private static class StopDiagTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            SUUtil.build()
                    .add("diag_socket_log -k")
                    .exec();

            return null;
        }
    }
}
