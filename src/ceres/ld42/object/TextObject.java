package ceres.ld42.object;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ceres.ld42.font.Font;

public class TextObject extends GameObject {
	private String message;
	private Font font;
	private float scale;
	private Color colour;
	
	public TextObject setMessage(String s, float scale, Color c) {
		this.message = s;
		this.size.x = s.length() * Font.WIDTH * scale;
		this.size.y = Font.HEIGHT * scale;
		this.scale = scale;
		this.isVisible = true;
		this.colour = c;
		
		return this;
	}
	
	public TextObject setFont(Font f) {
		this.font = f;
		return this;
	}
	
	@Override
	public void render(SpriteBatch batch) {
		if(!this.isVisible) return;
		
		font.write(batch, message, colour, position.x, position.y, scale);
	}
}
