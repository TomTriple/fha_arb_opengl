package arb3d.models;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import arb3d.util.LocationListenerImpl;
import arb3d.util.LocationReceivable;

public class Handheld {

	protected float rotX = 0;
	protected float rotY = 0;
	protected float rotZ = 0; 
	protected static Context context = null;
	protected static Handheld handheld = null;
	protected Location userLocation = null;
	
	private Handheld() {
		initOrientationSensor();
		initLocationSensor();
	} 
	
	
	private void initOrientationSensor() {
		SensorManager sm = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
		SensorEventListener sel = new SensorEventListener() { 
			@Override
			public void onSensorChanged(SensorEvent event) {
				rotX = event.values[SensorManager.DATA_X]; 
			}
			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
				;
			} 
		};
		sm.registerListener(sel, sm.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_GAME);		
	}
	
	private void initLocationSensor() {
        LocationListener locationListener = new LocationListenerImpl(new LocationReceivable() {
			public void receiveNewLocation(Location l) {
				Handheld.getInstance().setUserLocation(l); 
			} 
		});
        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);  
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 3, locationListener);		
	}
	
	
	public static void registerContext(Context _context) {
		context = _context;
	}
	
	public static Handheld getInstance() {
		if(handheld == null) {
			handheld = new Handheld();  
		}
		return handheld;
	}
	
	public float getRotX() {
		return rotX;
	}

	public void setRotX(float rotX) {
		this.rotX = rotX;
	}

	public float getRotY() {
		return rotY;
	}

	public void setRotY(float rotY) {
		this.rotY = rotY;
	}

	public float getRotZ() {
		return rotZ;
	}

	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
	}

	public Location getUserLocation() {
		return userLocation;
	}

	public void setUserLocation(Location userLocation) {
		this.userLocation = userLocation;
	}
	
	
}
