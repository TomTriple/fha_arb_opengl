package arb3d.models;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import android.graphics.Bitmap;

public class TextureData {

	protected static Bitmap defaultTexSmall = null;
	protected static Bitmap defaultTexBig = null;

	
	public static ByteBuffer bitmapToBuffer(Bitmap newbmp) {
		ByteBuffer bb = ByteBuffer.allocateDirect(newbmp.getHeight() * newbmp.getWidth() * 4); 
		bb.order(ByteOrder.BIG_ENDIAN); 
		IntBuffer intbuf = bb.asIntBuffer();
		for(int y = newbmp.getHeight() - 1; y > -1; y--) {
			for(int x = 0; x < newbmp.getWidth(); x++) {
				int pixel = newbmp.getPixel(x, newbmp.getHeight() - y - 1); 
				int red = ((pixel >> 16) & 0xff);
				int green = ((pixel >> 8) & 0xff); 
				int blue = ((pixel) & 0xff); 
				int alpha = ((pixel >> 24) & 0xff);
				intbuf.put(red << 24 | green << 16 | blue << 8 | (alpha == 255 ? 215 : alpha));  
			}
		}
		bb.rewind();
		return bb;
	}
	
	
	public static Bitmap getMutableCopy(Bitmap src) {
		return src.copy(src.getConfig(), true); 
	}
	
	public static Bitmap getDefaultTexSmall() {
		return defaultTexSmall;
	}
	public static void setDefaultTexSmall(Bitmap _defaultTexSmall) {
		defaultTexSmall = _defaultTexSmall;
	}
	public static Bitmap getDefaultTexBig() {
		return defaultTexBig;
	}
	public static void setDefaultTexBig(Bitmap _defaultTexBig) {
		defaultTexBig = _defaultTexBig;
	}
	
}
