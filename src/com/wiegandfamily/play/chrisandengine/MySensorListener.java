package com.wiegandfamily.play.chrisandengine;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class MySensorListener implements SensorEventListener {
	private float[] dimensions;
	public float[] getDimensions() {
		return dimensions;
	}	

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		dimensions = event.values;
		
	}
}
