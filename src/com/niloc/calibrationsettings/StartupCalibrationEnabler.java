package com.niloc.calibrationsettings;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StartupCalibrationEnabler extends BroadcastReceiver {
	@Override
	public void onReceive(final Context context, final Intent bootIntent) {
		Intent calibrateIntent = new Intent(context, bootCalibrate.class);
		calibrateIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(calibrateIntent);
	}
}
