package ceres.ld42.font;

import java.util.Hashtable;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Font {
	private static String chars = "" 
			+ "abcdefghij"
			+ "klmnopqrst" 
			+ "uvwxyz    "
			+ "ABCDEFGHIJ"
			+ "KLMNOPQRST"
			+ "UVWXYZ    "
			+ "1234567890"
			+ "!.:,\\\"-+=?"
			+ "()        "
			;
	public static final int WIDTH = 8, HEIGHT = 12;
	private static final int LINE_LENGTH = 10;
	private Hashtable<Color, Texture> textures;
	
	public Font() {
		this.textures = new Hashtable<Color, Texture>();
		this.textures.put(Color.BLACK, new Texture("fonts/simple_font.png"));
		this.textures.put(Color.WHITE, setColour(Color.WHITE));
		this.textures.put(Color.GOLD, setColour(Color.GOLD));
		this.textures.put(Color.CYAN, setColour(Color.CYAN));
		this.textures.put(Color.RED, setColour(Color.RED));
		this.textures.put(Color.OLIVE, setColour(Color.OLIVE));
		this.textures.put(Color.LIGHT_GRAY, setColour(Color.LIGHT_GRAY));
		this.textures.put(Color.MAROON, setColour(Color.MAROON));
	}
	
	private Texture setColour(Color c) {
		TextureData textureData = textures.get(Color.BLACK).getTextureData();
		textureData.prepare();
		
		Pixmap pixmap = textureData.consumePixmap();
		for(int y = 0; y < pixmap.getHeight(); y++) {
			for(int x = 0; x < pixmap.getWidth(); x++) {
				Color color = new Color();
				Color.rgba8888ToColor(color, pixmap.getPixel(x, y));
				
				Color newColor = color;
				if(color.equals(Color.BLACK)) newColor = c;
				
				pixmap.setColor(newColor);
				pixmap.fillRectangle(x, y, 1, 1);
			}
		}
		
		Texture t = new Texture(pixmap);
		textureData.disposePixmap();
		pixmap.dispose();
		
		return t;
	}
	
	public void write(SpriteBatch batch, String msg, Color c, float x, float y, float scale) {
		if(!textures.containsKey(c)) return;
		
		for(int i = 0; i < msg.length(); i++) {
			int ind = chars.indexOf(msg.charAt(i));
			int row = ind / LINE_LENGTH;
			int column = ind % LINE_LENGTH;
			
			TextureRegion tr = new TextureRegion(textures.get(c), column * WIDTH, row * HEIGHT, WIDTH, HEIGHT);
			batch.draw(tr, (i * WIDTH * scale) + x, y - (4 * scale), WIDTH * scale, HEIGHT * scale);
		}
	}
}
