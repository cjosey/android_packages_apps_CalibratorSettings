package com.niloc.calibrationsettings;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

//SurfaceFlingerAccess
import android.os.ServiceManager;


//Root Access
import java.lang.Runtime;
import java.io.IOException;
import java.lang.InterruptedException;
import java.io.DataOutputStream;

public class bootCalibrate extends Activity{
    private final static String CALIBRATORPREFS = "CalibratorPref";
    private final static String BOOTPRESET = "bootPreset";

	Database db = new Database(this);
	   @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.main);
		SharedPreferences prefs = getSharedPreferences(CALIBRATORPREFS, Context.MODE_PRIVATE);
        int preset = Integer.parseInt(prefs.getString(BOOTPRESET, "1"));

		db.open();
		int red = db.getRed(preset);
		int green = db.getGreen(preset);
		int blue = db.getBlue(preset);
		db.close();

		writeRenderColor(7, red, green, blue);
        finish();
    }

    private void writeRenderColor(int id, int red, int green, int blue) {
       try {
	        IBinder flinger = ServiceManager.getService("SurfaceFlinger");
		    if (flinger != null) {
    	        Parcel data = Parcel.obtain();
    	        data.writeInterfaceToken("android.ui.ISurfaceComposer");
    	        data.writeInt(id);
    	        flinger.transact(1014, data, null, 0);
    	        data.recycle();
	
    	     	data = Parcel.obtain();
    	        data.writeInterfaceToken("android.ui.ISurfaceComposer");
			    data.writeInt(red);
			    flinger.transact(1015, data, null, 0);
			    data.recycle();
	
    	    	data = Parcel.obtain();
    	        data.writeInterfaceToken("android.ui.ISurfaceComposer");
			    data.writeInt(green);
			    flinger.transact(1016, data, null, 0);
			    data.recycle();
	
			    data = Parcel.obtain();
    	        data.writeInterfaceToken("android.ui.ISurfaceComposer");
			    data.writeInt(blue);
			    flinger.transact(1017, data, null, 0);
			    data.recycle();
			}
	    } catch (RemoteException ex) {
	    } 
    }
}
