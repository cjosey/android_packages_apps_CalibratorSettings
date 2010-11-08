package com.niloc.calibrationsettings;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

import android.text.TextUtils;
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
	
    private final static String CALIBRATORPREFS = "CalibratorPref";
    private final static String BOOTPRESET = "bootPreset";
    private final static String CUSTOMPREFS = "customPrefs";
    
	private Button enableButton;
	private Button preset1;
	private Button preset2;
	private Button preset3;
	private Button custom1;
	private Button custom2;
	
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

	int settingNum;
	
	Database db = new Database(this);
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        this.enableButton = (Button)this.findViewById(R.id.enable);
        this.preset1 = (Button)this.findViewById(R.id.preset1);
        this.preset2 = (Button)this.findViewById(R.id.preset2);
        this.preset3 = (Button)this.findViewById(R.id.preset3);
        this.custom1 = (Button)this.findViewById(R.id.custom1);
		this.custom2 = (Button)this.findViewById(R.id.custom2);
        
        this.redInput = (SeekBar)this.findViewById(R.id.seekRed);
        this.greenInput = (SeekBar)this.findViewById(R.id.seekGreen);
        this.blueInput = (SeekBar)this.findViewById(R.id.seekBlue); 

		this.redOut = (TextView)this.findViewById(R.id.redNum);
		this.greenOut = (TextView)this.findViewById(R.id.greenNum);
		this.blueOut = (TextView)this.findViewById(R.id.blueNum);

		SharedPreferences prefs = getSharedPreferences(CALIBRATORPREFS, Context.MODE_PRIVATE);
        int preset = prefs.getInt(BOOTPRESET, 4);

		db.open();
		red = db.getRed(preset);
        green = db.getGreen(preset);
		blue = db.getBlue(preset);
		db.close();
		
		settingNum = preset;
	
        redInput.setProgress(red);
        greenInput.setProgress(green);
        blueInput.setProgress(blue);

		redOut.setText(String.valueOf(red));
		greenOut.setText(String.valueOf(green));
		blueOut.setText(String.valueOf(blue));

		this.preset1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				settingNum = 1;

				db.open();
				int redc = db.getRed(settingNum);
				int greenc = db.getGreen(settingNum);
				int bluec = db.getBlue(settingNum);
				db.close();

				SharedPreferences settings = getSharedPreferences(CALIBRATORPREFS, Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = settings.edit();
				editor.putInt(BOOTPRESET, settingNum);
				editor.commit();

				writeRenderColor(7, redc, greenc, bluec);
			}
          });

        this.preset2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				settingNum = 2;

				db.open();
				int redc = db.getRed(settingNum);
				int greenc = db.getGreen(settingNum);
				int bluec = db.getBlue(settingNum);
				db.close();

				SharedPreferences settings = getSharedPreferences(CALIBRATORPREFS, Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = settings.edit();
				editor.putInt(BOOTPRESET, settingNum);
				editor.commit();


				writeRenderColor(7, redc, greenc, bluec);
			}
          });
        this.preset3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				settingNum = 3;

				db.open();
				int redc = db.getRed(settingNum);
				int greenc = db.getGreen(settingNum);
				int bluec = db.getBlue(settingNum);
				db.close();

				SharedPreferences settings = getSharedPreferences(CALIBRATORPREFS, Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = settings.edit();
				editor.putInt(BOOTPRESET, settingNum);
				editor.commit();

				writeRenderColor(7, redc, greenc, bluec);
			}
          });
        this.custom1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				settingNum = 4;

				db.open();
				int redc = db.getRed(settingNum);
				int greenc = db.getGreen(settingNum);
				int bluec = db.getBlue(settingNum);
				db.close();

				SharedPreferences settings = getSharedPreferences(CALIBRATORPREFS, Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = settings.edit();
				editor.putInt(BOOTPRESET, settingNum);
				editor.commit();

				writeRenderColor(7, redc, greenc, bluec);
			}
          });

		this.custom2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				settingNum = 5;

				db.open();
				int redc = db.getRed(settingNum);
				int greenc = db.getGreen(settingNum);
				int bluec = db.getBlue(settingNum);
				db.close();

				SharedPreferences settings = getSharedPreferences(CALIBRATORPREFS, Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = settings.edit();
				editor.putInt(BOOTPRESET, settingNum);
				editor.commit();

				writeRenderColor(7, redc, greenc, bluec);
			}
          });

		
        this.enableButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				writeRenderColor(7, red, green, blue);
				
				// SAVE SHARED PREFS				
				int customPrefs = settingNum;
				saveCustomPreferencesData(String.valueOf(customPrefs));
				
				if(settingNum == 4 || settingNum == 5) 
				{
					db.open();
                	db.replaceRGBPreset("Custom", red, green, blue, settingNum);
					db.close();
				}
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

    public void saveCustomPreferencesData(String customPrefs) {
        SharedPreferences prefs = getSharedPreferences(CALIBRATORPREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(CUSTOMPREFS, customPrefs);
        editor.commit();
    }

    private void writeRenderColor(int id, int red, int green, int blue) {

		redOut.setText(String.valueOf(red));
		greenOut.setText(String.valueOf(green));
		blueOut.setText(String.valueOf(blue));
	    redInput.setProgress(red);
	    greenInput.setProgress(green);
	    blueInput.setProgress(blue);

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
