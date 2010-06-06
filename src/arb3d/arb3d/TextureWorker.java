package arb3d.arb3d;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import arb3d.models.POI;

public class TextureWorker extends Thread {
	
	public TextureWorker() {
	}
	
	
	public void run() {
		while(true) {
			Log.d("test", "Start...");
			for(int i = 0; i < POI.size(); i++) {
				POI poi = POI.get(i); 
				poi.paintSmallTexture(); 
				poi.paintBigTexture(); 
			}
			Log.d("test", "Schlafe...");
			try {
				Thread.sleep(6000);
			} catch(Exception e) {
				;
			}
		}
	}

	
}
