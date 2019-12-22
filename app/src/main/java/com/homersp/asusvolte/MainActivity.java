package com.homersp.asusvolte;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.mode_pdc).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean ret = SUUtil.build()
                        .add("setprop persist.vendor.dbg.ims_volte_enable 1")
                        .add("setprop persist.vendor.dbg.volte_avail_ovr 1")
                        .add("setprop persist.vendor.dbg.vt_avail_ovr 1")
                        .add("setprop persist.vendor.dbg.wfc_avail_ovr 1")
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
        });

        findViewById(R.id.restart_modem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean ret =SUUtil.build()
                        .add("setprop ctl.restart vendor.qcrild")
                        .exec();

                if (!ret) {
                    Toast.makeText(MainActivity.this, "Failed resetting connection, are you rooted?", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
