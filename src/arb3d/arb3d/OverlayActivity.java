package arb3d.arb3d;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.SurfaceHolder.Callback;
import android.view.View.OnTouchListener;
import android.widget.TextView;

public class OverlayActivity extends Activity {
 
	public static TextView statusText = null;
	private Camera camera = null;  
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE); 
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.overlay); 
        statusText = (TextView)findViewById(R.id.debug);
        final ArbRenderer renderer = new ArbRenderer(this);
        
        
        GLSurfaceView view3D = (GLSurfaceView)findViewById(R.id.view3D);
        view3D.setEGLConfigChooser(8,8,8,8, 16, 0);
        view3D.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        view3D.setRenderer(renderer);
 		view3D.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				renderer.onTouch(v, event); 
				return false; 
			} 
		});
 		
   		SurfaceView cameraView = (SurfaceView) findViewById(R.id.viewCam);
   		SurfaceHolder holder = cameraView.getHolder();
   		holder.addCallback(new Callback() { 
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				camera.stopPreview();
		    	camera.release();
			}
			@Override 
			public void surfaceCreated(SurfaceHolder holder) {
				camera = Camera.open();
				try {
					camera.setPreviewDisplay(holder); 
					camera.startPreview();
		    	} catch(Exception e) {
		    		Log.d("CAMERA", e.getMessage()); 
		    	}
			}			
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
				;
			}
		}); 
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);       
    }
}