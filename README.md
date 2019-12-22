# AsusVoLTE
Preface
Thanks to some help from this amazing community I have found the steps required to enable VoLTE and/or VoWiFi. For me both work, but others have reported differently - but the most likely outcome is that you will only have VoLTE working; you may have to do some experimentation to get both.

Prerequsities
* You must have root. There are several threads on how to do that, so look those up if you need help.

Downloads
* QPST
* Qualcomm USB drivers
* AsusVoLTE app
* MBNs (optional)

Step 1
First of all you will need to install both QPST and the Qualcomm USB drivers from the links above. It's important that you install exactly those ones, or it may not work. Once you've done that, launch PDC.exe in the bin folder of the QPST install (usually C:\Program Files\Qualcomm\QPST\bin\PDC.exe).

Step 2
Download and install the AsusVoLTE app from above and launch it. The app is very simple, and only has two buttons - press Enable PDC and you should hear/see a new device installing. Once that's done, you should see a new device under the Device dropdown in Advanced PDC Tool called something like Qualcomm ... 9091. Select it and you should see some things pop up in the box below - starting with HW_DEFAULT and SW_DEFAULT and probably also ROW_v1. If you only see those you will need to load external ones from the MBNs zip above. Simply press the Load button and select the mbn you want. Keep in mind that you will only be able to load MBNs from the zip above, ones from another device will *not* work.

Step 3
Now comes the tricky part; we have to find a profile that actually works on your carrier. For me the VivoVoWiFi_v5 profile enables both VoLTE and VoWiFi, but this may or may not work for you. Most of them at the very least enabled VoLTE for me.
To actually activate a profile you have to right-click on it, select SetSelectedConfig and Sub0, then repeat it but press Sub1 instead. After you've done that Sub0 and Sub1 for that profile should have changed to Pending, so simply highlight it again and press the Activate button at the bottom of the screen. This should change Sub0 and Sub1 to say Active and it should also be moved to the top of the list just below HW_DEFAULT and SW_DEFAULT.
To check if it works you have to either reboot, or you can do the far simpler thing and press Reset Connection in the AsusVoLTE app. This will disable the mobile connection for a few seconds but once it's back on you should hopefully see either VoLTE or VoWiFi in the status bar. If not, you will have to try another profile! It can be quite time consuming finding one that works for you, but if you get VoLTE on at least one of them there's a chance one of them will also enable VoWiFi.