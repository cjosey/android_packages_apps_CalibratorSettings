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
import android.widget.SeekBar;
import android.widget.ImageView;
import android.widget.TextView;

import android.graphics.BitmapFactory;

import android.graphics.Bitmap;

import android.widget.SeekBar.OnSeekBarChangeListener;

import android.os.ServiceManager;

public class CalibrationMain extends Activity {
	
	private Button enableButton;
	
	private SeekBar redInput;
	private SeekBar blueInput;
	private SeekBar  greenInput;

	private TextView redOut;
	private TextView greenOut;
	private TextView blueOut;

	private ImageView image;

	int red;
	int green;
	int blue;
	
	Database db = new Database(this);
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        this.enableButton = (Button)this.findViewById(R.id.enable);
        
        this.redInput = (SeekBar)this.findViewById(R.id.seekRed);
        this.greenInput = (SeekBar)this.findViewById(R.id.seekGreen);
        this.blueInput = (SeekBar)this.findViewById(R.id.seekBlue); 

		this.redOut = (TextView)this.findViewById(R.id.redNum);
		this.greenOut = (TextView)this.findViewById(R.id.greenNum);
		this.blueOut = (TextView)this.findViewById(R.id.blueNum);
   
		this.image = (ImageView)this.findViewById(R.id.calibImage);

        Bitmap mBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.color);

		image.setImageBitmap(mBitmap);
        
        db.open();
        red = db.getRed(1);
        green = db.getGreen(1);
        blue = db.getBlue(1);
        db.close();
	
        redInput.setProgress(red);
        greenInput.setProgress(green);
        blueInput.setProgress(blue);

		redOut.setText(String.valueOf(red));
		greenOut.setText(String.valueOf(green));
		blueOut.setText(String.valueOf(blue));
        
        this.enableButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				writeRenderColor(7, red, green, blue);
				db.open();
                db.replaceRGBPreset("Default", red, green, blue, 1);
				db.close();
			}
          });
		this.redInput.setOnSeekBarChangeListener(RedChange);
		this.greenInput.setOnSeekBarChangeListener(GreenChange);
		this.blueInput.setOnSeekBarChangeListener(BlueChange);
    }
 
	private OnSeekBarChangeListener RedChange = new OnSeekBarChangeListener(){

    @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            int index = redInput.getProgress();
            red = index;
			redOut.setText(String.valueOf(index));
			writeRenderColor(7, red, green, blue);
        }
           
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    };

	private OnSeekBarChangeListener GreenChange = new OnSeekBarChangeListener(){

    @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            int index = greenInput.getProgress();
            green = index;
			greenOut.setText(String.valueOf(index));
			writeRenderColor(7, red, green, blue);
        }
           
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    };

	private OnSeekBarChangeListener BlueChange = new OnSeekBarChangeListener(){

    @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            int index = blueInput.getProgress();
            blue = index;
			blueOut.setText(String.valueOf(index));
			writeRenderColor(7, red, green, blue);
        }
           
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    };

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
