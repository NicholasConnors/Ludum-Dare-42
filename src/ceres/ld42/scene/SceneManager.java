package ceres.ld42.scene;

import java.util.Stack;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;

import ceres.ld42.font.Font;

public class SceneManager {
	private Stack<Scene> scenes;
	private AssetManager assetManager;
	private OrthographicCamera camera;
	private Font font;
	
	public SceneManager(AssetManager am, OrthographicCamera c) {
		this.scenes = new Stack<Scene>();
		this.assetManager = am;
		this.camera = c;
		this.font = new Font();
	}

	public Scene peek() {
		return this.scenes.peek();
	}
	
	public void pop() {
		this.scenes.pop();
	}
	
	public void push(Scene scene) {
		this.scenes.push(scene);
	}
	
	public AssetManager getAssetManager() {
		return this.assetManager;
	}
	
	public OrthographicCamera getCamera() {
		return this.camera;
	}
	
	public Font getFont() {
		return this.font;
	}
}
