package com.niloc.calibrationsettings;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

import android.view.View;
import android.view.View.OnClickListener;

import android.widget.Button;
import android.widget.EditText;

import android.os.ServiceManager;

public class CalibrationMain extends Activity {
	
	private Button enableButton;
	
	private EditText redText;
	private EditText blueText;
	private EditText greenText;
	
	Database db = new Database(this);
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        this.enableButton = (Button)this.findViewById(R.id.enable);
        
        this.redText = (EditText)this.findViewById(R.id.redIn);
        this.greenText = (EditText)this.findViewById(R.id.greenIn);
        this.blueText = (EditText)this.findViewById(R.id.blueIn);    
        
	db.open();
	String red = String.valueOf(db.getRed(1));
	String green = String.valueOf(db.getGreen(1));
	String blue = String.valueOf(db.getBlue(1));
	db.close();
	
	redText.setText(red);
	greenText.setText(green);
	blueText.setText(blue);
        
        this.enableButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int red = Integer.parseInt(redText.getText().toString());
				int green = Integer.parseInt(greenText.getText().toString());
				int blue = Integer.parseInt(blueText.getText().toString());;
				writeRenderColor(7, red, green, blue);
				db.open();
                db.replaceRGBPreset("Default", red, green, blue, 1);
			}
          });
    }
    //ADDED
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
