package arb3d.arb3d;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import arb3d.models.Handheld;
import arb3d.models.POI;
import arb3d.models.TextureData;


public class ArbRenderer implements GLSurfaceView.Renderer {

	//private ArrayList<POI> list = new ArrayList<POI>(); 
	private Context c;
	
	private Bitmap newbmp;
	private ByteBuffer bb;
	private Bitmap bmpoff;
	private ByteBuffer bb2;
	private int[] textureName;
	private Handheld handheld; 
	private boolean touchHappened = false;
	
	
	public ArbRenderer(Context c) {
		this.c = c; 
		handheld = Handheld.getInstance();
	}
	
	
	public void onTouch(View v, MotionEvent event) { 
		touchHappened = !touchHappened; 
		for(POI p : POI.findAll()) {
			p.repos(); 
		}
	}
	

	public void onDrawFrame(GL10 gl) {

		gl.glEnable(GL10.GL_TEXTURE_2D); 
		gl.glEnable(GL10.GL_BLEND);
		gl.glEnable(GL10.GL_COLOR_MATERIAL); 
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA); 

		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		for(POI poi : POI.findAll()) {
			
			double dx = poi.getLongitude() - handheld.getUserLocation().getLongitude();
			double dy = poi.getLatitude() - handheld.getUserLocation().getLatitude();
			// returns -PI..+PI
			// android-api: dx, dy, sun-api: dy, dx !!!!!
			double angle = Math.atan2(dx, dy);
			// normalize angle to match 0°..360° 
			if(angle < 0)
				angle = Math.PI + (Math.PI + angle);
			angle = Math.toDegrees(angle); 
			double length = Math.sqrt(dx*dx + dy*dy);  
			
			
			
			if(!touchHappened) {
				gl.glBindTexture(GL10.GL_TEXTURE_2D, textureName[0]);
				gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, poi.getBitmapSmall().getWidth(), poi.getBitmapSmall().getHeight(), 0, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, poi.getTextureBufferSmall());
			} else {
				gl.glBindTexture(GL10.GL_TEXTURE_2D, textureName[0]);
				gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, poi.getBitmapBig().getWidth(), poi.getBitmapBig().getHeight(), 0, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, poi.getTextureBufferBig()); 
			}
			gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR); 
			gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
			
			
			gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_AMBIENT_AND_DIFFUSE, new float[] {1,1,1,1}, 0);
			
			
			gl.glLoadIdentity(); 
			float[] colors = poi.getColors();
			gl.glColor4f(colors[0], colors[1], colors[2], 1);
			gl.glRotatef(handheld.getRotX() + (float)angle * -1 + 135, 0, 1, 0);  
			gl.glTranslatef(poi.getPosx(), poi.getPosy(), poi.getPosz());
			poi.draw(gl);
		} 

		if(false) {
			//touchHappened = false;
			gl.glDeleteTextures(1, textureName, 0);
			gl.glGenTextures(1, textureName, 0); 
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textureName[0]);
			gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, bmpoff.getWidth(), bmpoff.getHeight(), 0, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, bb2);
			gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR); 
			gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);			
		} 
	}
	
	
	public void onSurfaceCreated(final GL10 gl, EGLConfig config) {
		gl.glDisable(GL10.GL_DITHER);
		// Set the background color to black ( rgba ). 
		gl.glClearColor(0,0,0,0);
		// Enable Smooth Shading, default not really needed.
		gl.glShadeModel(GL10.GL_SMOOTH); 
		// Depth buffer setup.
		//gl.glClearDepthf(1.0f);
		// Enables depth testing.
		gl.glEnable(GL10.GL_DEPTH_TEST); 
		gl.glEnable(GL10.GL_CULL_FACE); 
		// The type of depth testing to do.
		gl.glDepthFunc(GL10.GL_LEQUAL);
		// Really nice perspective calculations.
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
		// enable textures 
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		
		textureName = new int[POI.size() * 3]; 
		gl.glGenTextures(POI.size() * 2, textureName, 0);
		
		
		initLight(gl);
		
		
		// load default bitmap templates for textures 
		TextureData.setDefaultTexBig(BitmapFactory.decodeResource(c.getResources(), R.drawable.arb_big));
		TextureData.setDefaultTexSmall(BitmapFactory.decodeResource(c.getResources(), R.drawable.arb_small)); 
		
		for(int i = 0; i < POI.size(); i++) { 
			POI poi = POI.get(i);
			poi.paintSmallTexture();
			poi.paintBigTexture();  
		}
		
		
		new TextureWorker().start(); 
	}
	
	
	private void initLight(GL10 gl) {
		// set light
		float lightAmb[] = new float[] {1f, 1f, 1f, 1f};
		float lightPos[] = new float[] {0,1,0,0};
		gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, lightAmb, 0);
		//gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, lightPos, 0); 
		gl.glEnable(GL10.GL_LIGHTING);
		gl.glEnable(GL10.GL_LIGHT0); 		
	}
	
	
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// Sets the current view port to the new size.
		gl.glViewport(0, 0, width, height);
		// Select the projection matrix
		gl.glMatrixMode(GL10.GL_PROJECTION);
		// Reset the projection matrix
		gl.glLoadIdentity(); 
		// Calculate the aspect ratio of the window
		GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f, 100.0f);
		// Select the modelview matrix
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		// Reset the modelview matrix
		gl.glLoadIdentity();
	}	
	
}
