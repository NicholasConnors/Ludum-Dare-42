package ceres.ld42;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import ceres.ld42.scene.InstructionScene;
import ceres.ld42.scene.SceneManager;

public class LD42Game extends ApplicationAdapter {
	public static final int WIDTH = 1280;
	public static final int HEIGHT = 720;
	public static final String TITLE = "LD42 - Last Days on Mars";
	
	private SpriteBatch batch;
	private SceneManager sceneManager;
	private OrthographicCamera camera;
	private AssetManager assetManager;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera(WIDTH, HEIGHT);
		assetManager = new AssetManager();
		
		sceneManager = new SceneManager(assetManager, camera);
		
		//Set camera position
		//camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0f);
		camera.zoom = 0.5f;
		
		//Background colour (black)
		Gdx.gl.glClearColor(0.f, 0.f, 0.f, 1);
		
		//Load resources
		load();
	}

	@Override
	public void render () {
		//Game loop
		float dt = Gdx.graphics.getDeltaTime();
		
		sceneManager.peek().handleInput(dt);
		sceneManager.peek().update(dt);

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		sceneManager.peek().render(batch);
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		assetManager.dispose();
	}
	
	public static Vector2 getMouse() {
		float x = WIDTH * Gdx.input.getX() / Gdx.graphics.getWidth();
		float y = HEIGHT - (HEIGHT * Gdx.input.getY() / Gdx.graphics.getHeight());
		return new Vector2(x, y);
	}
	
	private void load() {
		//Load font
		assetManager.load("fonts/simple_font.png", Texture.class);
		
		//Hexes
		loadTexture("hex/hex.png");
		loadTexture("hex/dust.png");
		loadTexture("hex/basalt.png");
		loadTexture("hex/ice.png");
		loadTexture("hex/crater.png");
		
		//Resources
		loadTexture("resources/energy.png");
		loadTexture("resources/food.png");
		loadTexture("resources/oxygen.png");
		loadTexture("resources/production.png");
		loadTexture("resources/housing.png");
		loadTexture("resources/impassable.png");
		
		//Gui
		loadTexture("gui/banner_1.png");
		loadTexture("gui/banner_2.png");
		loadTexture("gui/banner_3.png");
		loadTexture("gui/next.png");
		
		//Buildings
		loadTexture("buildings/hab.png");
		loadTexture("buildings/solar.png");
		loadTexture("buildings/moxie.png");
		loadTexture("buildings/factory.png");
		loadTexture("buildings/farm.png");
		loadTexture("buildings/dead_hab.png");
		
		//Sound
		loadAudio("bad_pip.wav");
		loadAudio("good_pip.wav");
		loadAudio("game_over.wav");
		loadAudio("tick.wav");
		
		assetManager.finishLoading();
		sceneManager.push(new InstructionScene(sceneManager));
	}
	
	//** Helper method for load() */
	private void loadTexture(String s) {
		assetManager.load("textures/" + s, Texture.class);
	}
	
	private void loadAudio(String s) {
		assetManager.load("audio/" + s, Sound.class);
	}
}
