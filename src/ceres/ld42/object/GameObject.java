package ceres.ld42.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import ceres.ld42.LD42Game;

public class GameObject {
	private Texture[] textures;
	private boolean isAnimated;
	private int currentFrameInd;
	private float frameLength;
	private float frameTimer;
	
	protected Vector2 position;
	protected Vector2 size;
	
	protected boolean isVisible;
	
	public GameObject() {
		this.isVisible = false;
		this.position = new Vector2();
		this.size = new Vector2();
	}
	
	public GameObject setTexture(Texture[] t, float scale) {
		this.textures = t;
		this.isVisible = true;
		this.size = new Vector2(t[0].getWidth() * scale, t[0].getHeight() * scale);
		
		//Is animated
		if(textures.length > 1) {
			isAnimated = true;
			currentFrameInd = 0;
			frameLength = 0.5f;
			frameTimer = 0.0f;
		} else {
			isAnimated = false;
			currentFrameInd = 0;
		}
		
		return this;
	}
	
	public GameObject setTexture(Texture t, float scale) {
		if(t == null) return this;
		return this.setTexture(new Texture[] { t }, scale);
	}
	
	public GameObject setPosition(Vector2 p) {
		this.position = p;
		return this;
	}
	
	public GameObject setWidth(float w) {
		this.size.x = w;
		return this;
	}
	
	public GameObject setHeight(float h) {
		this.size.y = h;
		return this;
	}
	
	public GameObject setCentered(boolean x, boolean y) {
		if(x) this.position.x = (LD42Game.WIDTH - this.size.x) / 2f;
		if(y) this.position.y = (LD42Game.HEIGHT - this.size.y) / 2f;
		
		return this;
	}
	
	public void update(float dt) {
		//Animation logic
		if(this.isAnimated) {
			frameTimer += dt;
			if(frameTimer >= frameLength) {
				frameTimer -= frameLength;
				if(++currentFrameInd > textures.length - 1) currentFrameInd = 0;
			}
		}
	}
	
	public void render(SpriteBatch batch) {
		if(!isVisible) return;
		
		batch.draw(
				new TextureRegion(textures[currentFrameInd]),
				position.x, position.y,
				0f, size.y,
				size.x, size.y,
				1f, 1f, 0f);
	}
	
	public void setVisibility(boolean v) {
		this.isVisible = v;
	}
	
	public boolean isTouched() {
		float x = LD42Game.getMouse().x;
		float y = LD42Game.getMouse().y;
		
		return (x > position.x && x < position.x + size.x && y > position.y && y < position.y + size.y);
	}
	
	public boolean isTouched(OrthographicCamera camera) {
		Vector3 pos = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
		
		float x = pos.x;
		float y = pos.y;
		
		return (x > position.x && x < position.x + size.x && y > position.y && y < position.y + size.y);
	}
	
	public Vector2 getSize() {
		return this.size;
	}
	
	public Vector2 getPosition() {
		return this.position;
	}
}
