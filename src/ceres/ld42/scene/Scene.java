package ceres.ld42.scene;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;

import ceres.ld42.LD42Game;
import ceres.ld42.object.GameObject;

public abstract class Scene {
	private SceneManager sceneManager;
	private ArrayList<GameObject> bgObjects, objects, fgObjects, guiObjects;
	
	public Scene(SceneManager sm) {
		this.sceneManager = sm;
		
		this.bgObjects = new ArrayList<GameObject>();
		this.objects = new ArrayList<GameObject>();
		this.fgObjects = new ArrayList<GameObject>();
		this.guiObjects = new ArrayList<GameObject>();
	}
	
	public void update(float dt) {
		updateObjects(bgObjects, dt);
		updateObjects(objects, dt);
		updateObjects(fgObjects, dt);
		updateObjects(guiObjects, dt);
	}
	
	public void handleInput(float dt) {
		
	}
	
	public void render(SpriteBatch batch) {
		//Set camera 
		sceneManager.getCamera().update();
		batch.setProjectionMatrix(sceneManager.getCamera().combined);
		
		batch.begin();
		renderObjects(bgObjects, batch);
		renderObjects(objects, batch);
		renderObjects(fgObjects, batch);
		batch.end();
		
		//Set camera for gui
		Matrix4 uiMatrix = sceneManager.getCamera().combined.cpy();
		uiMatrix.setToOrtho2D(0, 0, LD42Game.WIDTH, LD42Game.HEIGHT);
		batch.setProjectionMatrix(uiMatrix);
		
		batch.begin();
		renderObjects(guiObjects, batch);
		batch.end();
	}
	
	public SceneManager getSceneManager() {
		return sceneManager;
	}
	
	public ArrayList<GameObject> getBgObjects() {
		return this.bgObjects;
	}
	
	public ArrayList<GameObject> getObjects() {
		return this.objects;
	}
	
	public ArrayList<GameObject> getFgObjects() {
		return this.fgObjects;
	}
	
	public ArrayList<GameObject> getGuiObjects() {
		return this.guiObjects;
	}
	
	/** Helper method for update */
	private void updateObjects(ArrayList<GameObject> objs, float dt) {
		for(GameObject obj : objs) {
			obj.update(dt);
		}
	}
	
	/** Helper method for render */
	private void renderObjects(ArrayList<GameObject> objs, SpriteBatch batch) {
		for(GameObject obj : objs) {
			obj.render(batch);
		}
	}
}
