package ceres.ld42.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import ceres.ld42.LD42Game;
import ceres.ld42.object.GameObject;
import ceres.ld42.object.TextObject;

public class MainMenuScene extends Scene {
	private Texture logoTexture = this.getSceneManager().getAssetManager().get("textures/hex/dust.png", Texture.class);
	private Texture logoTexture2 = this.getSceneManager().getAssetManager().get("textures/buildings/hab.png", Texture.class);
	private Sound pip = this.getSceneManager().getAssetManager().get("audio/good_pip.wav", Sound.class);
	private GameObject button;
	
	private float timer;
	
	public MainMenuScene(SceneManager sm) {
		super(sm);

		this.getGuiObjects().add(new GameObject().setTexture(logoTexture, 14f).setCentered(true, true));
		this.getGuiObjects().add(new GameObject().setTexture(logoTexture2, 14f).setCentered(true, true));
		
		button = new TextObject()
				.setFont(this.getSceneManager().getFont())
				.setMessage("PLAY", 6f, Color.GOLD)
				.setPosition(new Vector2(0, 64))
				.setCentered(true, false);
		this.getGuiObjects().add(button);
		
		this.getGuiObjects().add(new TextObject()
				.setFont(this.getSceneManager().getFont())
				.setMessage("Last Days on Mars", 6f, Color.WHITE)
				.setPosition(new Vector2(0, LD42Game.HEIGHT - 64))
				.setCentered(true, false));
		this.getGuiObjects().add(new TextObject()
				.setFont(this.getSceneManager().getFont())
				.setMessage("(Running out of Space)", 2f, Color.WHITE)
				.setPosition(new Vector2(0, LD42Game.HEIGHT - 96))
				.setCentered(true, false));
		
		this.getGuiObjects().add(new TextObject()
				.setFont(this.getSceneManager().getFont())
				.setMessage("Made in Java with LibGDX. Sounds made with BFXR. Made for Ludum Dare 42", 2f, Color.WHITE)
				.setPosition(new Vector2(0, 8))
				.setCentered(true, false)
				);
	}
	
	@Override
	public void update(float dt) {
		timer += dt;
	}
	
	@Override
	public void handleInput(float dt) {
		if(timer > 0.5f && Gdx.input.isTouched() && button.isTouched()) {
			this.getSceneManager().push(new PlayScene(this.getSceneManager()));
			pip.play();
		}
	}
}
