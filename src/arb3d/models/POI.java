package arb3d.models;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import arb3d.util.IEach;


public class POI { 
	
	private float[] colors = new float[] {1, 1, 1};
	private float posx = 0;
	private float posy = (float)Math.random() * 3 - 1.5f; 
	private float posz = -12 + (float)Math.random() * 4;
	private int textureIndex = 0;
	private boolean newTexAvailable = false;
	
	private boolean smallBuffered = false;
	private boolean bigBuffered = false;
	private String amenityBuffer = "";
	private String descriptionBuffer = "";
	private int distanceBuffer = -1;
	
	private Bitmap bitmapSmall = null; 
	private Bitmap bitmapBig = null; 

	private ByteBuffer textureBufferSmall = null;
	private ByteBuffer textureBufferBig = null; 
	
	
	// Our vertices.
	private float vertices[] = {
	      -2.0f,  0.5f, 0.0f,  // 0, Top Left
	      -2.0f, -0.5f, 0.0f,  // 1, Bottom Left
	       2.0f, -0.5f, 0.0f,  // 2, Bottom Right
	       2.0f,  0.5f, 0.0f,  // 3, Top Right 
	};  
	
	private byte textCoords[] = { 
			//1,0,1,1,0,1,0,0
			0,0,0,1,1,1,1,0
	};

	// The order we like to connect them.
	private short[] indices = { 0, 1, 2, 0, 2, 3 };
	
	// Our vertex buffer.
	private FloatBuffer vertexBuffer;

	// Our index buffer.
	private ShortBuffer indexBuffer;
	
	private ByteBuffer textureBuffer;
	
	
	private String name = "";
	private double longitude = 0;
	private double latitude = 0; 
	private Location location = null;
	private float distance = 0.0f;
	private Map<String, String> tags = new HashMap<String, String>(); 
	
	private static Map<String,String> keyMap = new HashMap<String, String>();
	
	private static List<POI> all = new ArrayList<POI>(); 
	
	static {
		keyMap.put("fee", "Gebühr");
		keyMap.put("wheelchair", "Rollstuhl");
		keyMap.put("male", "Männlich");
		keyMap.put("female", "Weiblich"); 
		keyMap.put("parking", "Parktyp");
		keyMap.put("amenity", "Einrichtung");
		keyMap.put("operator", "Betreiber");
		keyMap.put("opening_hours", "Öffnungszeit");
		keyMap.put("cuisine", "Küche");
	}	
	
	
	public void repos() {
		float vertices[] = {
			      -2.0f,  2f, 0.0f,  // 0, Top Left
			      -2.0f, -2f, 0.0f,  // 1, Bottom Left
			       2.0f, -2f, 0.0f,  // 2, Bottom Right
			       2.0f,  2f, 0.0f,  // 3, Top Right 
		}; 
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0); 
	}
	
	
	public POI() {
		// a float is 4 bytes, therefore we multiply the number if 
		// vertices with 4.
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
		
		// short is 2 bytes, therefore we multiply the number if 
		// vertices with 2.
		ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 2);
		ibb.order(ByteOrder.nativeOrder());
		indexBuffer = ibb.asShortBuffer();
		indexBuffer.put(indices);
		indexBuffer.position(0);
		
		textureBuffer = ByteBuffer.allocateDirect(textCoords.length); 
		textureBuffer.put(textCoords); 
		textureBuffer.rewind(); 
	}
	 
	/** 
	 * This function draws our square on screen.
	 * @param gl 
	 */
	public void draw(GL10 gl) {
		// Counter-clockwise winding.
		gl.glFrontFace(GL10.GL_CCW);
		// Enable face culling. 
		gl.glEnable(GL10.GL_CULL_FACE);
		// What faces to remove with the face culling.
		gl.glCullFace(GL10.GL_BACK);
		
		// Enabled the vertices buffer for writing and to be used during 
		// rendering.
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		// Specifies the location and data format of an array of vertex
		// coordinates to use when rendering.
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		
		gl.glTexCoordPointer(2, GL10.GL_BYTE, 0, textureBuffer); 
		
		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length, 
				GL10.GL_UNSIGNED_SHORT, indexBuffer);
		
		// Disable the vertices buffer.
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		// Disable face culling.
		gl.glDisable(GL10.GL_CULL_FACE);
	}
	

	public float[] getColors() {
		return colors;
	}

	public void setColors(float[] colors) {
		this.colors = colors;
	}

	public float getPosx() {
		return posx;
	}

	public void setPosx(float posx) {
		this.posx = posx;
	}

	public float getPosy() {
		return posy;
	}

	public void setPosy(float posy) {
		this.posy = posy;
	}

	public float getPosz() {
		return posz;
	}

	public void setPosz(float posz) {
		this.posz = posz;
	}
	
	
	public void setPos(float x, float y, float z) {
		posx = x;
		posy = y;
		posz = z;
	}

	public Bitmap getBitmapSmall() {
		return bitmapSmall;
	}


	public void setBitmapSmall(Bitmap bitmapSmall) {
		this.bitmapSmall = bitmapSmall;
	}


	public Bitmap getBitmapBig() {
		return bitmapBig;
	}


	public void setBitmapBig(Bitmap bitmapBig) {
		this.bitmapBig = bitmapBig;
	}
	
	public ByteBuffer getTextureBufferSmall() {
		return textureBufferSmall;
	}


	public void setTextureBufferSmall(ByteBuffer textureBufferSmall) {
		this.textureBufferSmall = textureBufferSmall;
	}


	public ByteBuffer getTextureBufferBig() {
		return textureBufferBig;
	}


	public void setTextureBufferBig(ByteBuffer textureBufferBig) {
		this.textureBufferBig = textureBufferBig;
	}
	
	
	public static List<POI> findAll() {
		return all; 
	}
	
	
	public static void add(POI poi) {
		all.add(poi); 
	}
	
	
	public static int size() {
		return all.size(); 
	}
	
	public static void clear() {
		all.clear();  
	}

	public static POI get(int index) {
		return all.get(index); 
	}
	
	
	public String toString() {
		return name + " - " + latitude + " - " + longitude; 
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public void addTag(String key, String value) {
		tags.put(key, value);  
	}
	public Map<String, String> getTags() {
		return tags;  
	}
	public void setDistance(float distance) {
		this.distance = distance;
	}
	public float getDistance() {
		return distance; 
	}
	public Location getLocation()  {
		Location l = new Location("");
		l.setLatitude(getLatitude());
		l.setLongitude(getLongitude()); 
		return l; 
	}
	
	public String getTagStreet() {
		return getTags().get("addr:street");
	}
	public String getTagHousenumber() {
		return getTags().get("addr:housenumber");
	}
	public String getTagPostcode() {
		return getTags().get("addr:postcode"); 
	}
	public String getTagURL() {
		return getTags().get("url:official");
	}
	public String getTagDescription() {
		return getTags().get("description"); 
	}
	public String getTagAmenity() {
		return getTags().get("amenity"); 
	}	
	
	
	public String getDescription() {
		if(!descriptionBuffer.equals(""))
			return descriptionBuffer;
		StringBuffer buf = new StringBuffer();
		int lines = 5; 
		if(getTagStreet() != null) {
			buf.append(getTagStreet());
			getTags().remove("addr:street");
			lines--;
		}
		if(getTagHousenumber() != null) {
			buf.append(" " + getTagHousenumber());
			getTags().remove("addr:housenumber");
			lines--;
		}
		if(buf.toString().equals("") == false) {
			buf.append("\n");
			lines--;
		}
		if(getTagAmenity() != null) {
			amenityBuffer = getTagAmenity().substring(0,1).toUpperCase()+getTagAmenity().substring(1); 
			buf.append("Einrichtung: " + amenityBuffer + "\n");  
			getTags().remove("amenity"); 
			lines--;
		}		
		if(getTagURL() != null && !getTagURL().equals("null") && !getTagURL().equals("")) { 
			buf.append("Web: " + getTags().get("url:official") + "\n");
			getTags().remove("url:official");
			lines--;
		}
		if(getTagDescription() != null && !getTagDescription().equals("null") && !getTagDescription().equals("")) {
			buf.append(getTags().get("description") + "\n"); 
			getTags().remove("description");
			lines--;
		}
			
		
		Set<String> keys = getTags().keySet();
		Iterator<String> keyIterator = keys.iterator();
		while(keyIterator.hasNext()) {
			if(lines == 0)
				break; 
			String key = keyIterator.next();
			buf.append(keyMap.get(key) + ": " + getTags().get(key) + "\n"); 
			lines--;
		}
		
		descriptionBuffer = buf.toString();
		return descriptionBuffer;
	}
	
	
	public static void eachPoi(IEach each) {
    	int i = 1; 
    	for(POI p : all) { 
    		each.each(p, i); 
    		i++; 
    	}		
	}

	
	public String getTitle() {
		String title = name;
		if(title.equals(""))
			title = amenityBuffer;
		if(title == null)
			return "---";
		if(title.length() >= 15) {
			return title.substring(0, 13) + "...";
		}
		return title;
	}	
	
	
	public int getTextureIndex() {
		return textureIndex;
	}


	public void setTextureIndex(int textureIndex) {
		this.textureIndex = textureIndex;
	}

	public void setNewTexAvailable(boolean avail) {
		newTexAvailable = avail; 
	}
	
	public boolean newTexAvailable() {
		return newTexAvailable; 
	} 
	
	public void paintSmallTexture() {
		int distance = (int)Handheld.getInstance().getUserLocation().distanceTo(getLocation());
		if(distance == distanceBuffer && smallBuffered)
			return;		
		smallBuffered = true;
		distanceBuffer = distance;
		Bitmap small = TextureData.getMutableCopy(TextureData.getDefaultTexSmall());
		Canvas c = new Canvas(small);
		Paint p = new Paint();
		p.setColor(Color.WHITE);
		p.setTextSize(9.5f);
		// p.setSubpixelText(true); 
		p.setAntiAlias(true); 
		c.drawText(getTitle().toUpperCase(), 30, 15, p);
		//c.drawText((int)Handheld.getInstance().getUserLocation().distanceTo(getLocation()) + " m", 30, 23, p);
		
		c.drawText("dist: " + distanceBuffer + " m", 30, 23, p);
		
		setTextureBufferSmall(TextureData.bitmapToBuffer(small));
		setBitmapSmall(small);
	}
	
	
	public void paintBigTexture() {
		int distance = (int)Handheld.getInstance().getUserLocation().distanceTo(getLocation());
		if(distance == distanceBuffer && bigBuffered)
			return;
		bigBuffered = true;
		distanceBuffer = distance;
		Bitmap big = TextureData.getMutableCopy(TextureData.getDefaultTexBig());
		Canvas c = new Canvas(big);
		Paint p = new Paint();
		p.setColor(Color.WHITE); 
		p.setTextSize(9.5f);
		// p.setSubpixelText(true);  
		p.setAntiAlias(true); 
		c.drawText(getTitle().toUpperCase(), 30, 15, p);
		c.drawText(distanceBuffer + " m", 30, 23, p); 
		String[] str = getDescription().split("\n");
		for(int i = 0; i < str.length; i++) {
			int len = str[i].length();
			if(len >= 25) {
				c.drawText(str[i].substring(0, 25) + "...", 5, 65 + i * 12, p);				
			} else {
				c.drawText(str[i], 5, 65 + i * 12, p);				
			}
		}
		setTextureBufferBig(TextureData.bitmapToBuffer(big));
		setBitmapBig(big);		
	}	
}
