package ceres.ld42.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import ceres.ld42.LD42Game;
import ceres.ld42.object.GameObject;
import ceres.ld42.object.TextObject;

public class InstructionScene extends Scene{
	private GameObject[] text;
	private Sound tick = this.getSceneManager().getAssetManager().get("audio/tick.wav", Sound.class);
	private float counter;
	public InstructionScene(SceneManager sm) {
		super(sm);
		text = new GameObject[11];
		
		text[0] = newText("Earth is overpopulated. We have fled to Mars.", 1);
		
		text[1] = newText("Move with the arrow keys. Zoom with , and . or + and -.", 3);
		text[2] = newText("Shift click to place multiple buildings, right click to cancel.", 4);
		
		text[3] = newText("1 unit of population arrives each turn. Have habitats ready.", 6);
		text[4] = newText("Buildings cost production to make and energy to run.", 7);
		text[5] = newText("Habitats need food and oxygen to survive.", 8);
		
		text[6] = newText("If you are short on ANY resources for 10 consecutive turns, you lose.", 10);
		text[7] = newText("If you run out of available habitats, you lose.", 11);
		text[8] = newText("You cannot win. You can only delay the inevitable.", 12, Color.RED);
		
		text[9] = newText("How long until you run out of space?", 14);
		
		text[10] = newText("Click to continue", 16, Color.GOLD);
		
	}
	
	@Override
	public void update(float dt) {
		counter += dt;
		if(counter > 0 ) text[0].setVisibility(true);
		if(counter > 1 ) text[1].setVisibility(true);
		if(counter > 2 ) text[2].setVisibility(true);
		if(counter > 3 ) text[3].setVisibility(true);
		if(counter > 4 ) text[4].setVisibility(true);
		if(counter > 5 ) text[5].setVisibility(true);
		if(counter > 6 ) text[6].setVisibility(true);
		if(counter > 7 ) text[7].setVisibility(true);
		if(counter > 8 ) text[8].setVisibility(true);
		if(counter > 9 ) text[9].setVisibility(true);
		if(counter > 10 ) text[10].setVisibility(true);
	}
	
	@Override
	public void handleInput(float dt) {
		if(counter > 10f && Gdx.input.justTouched()) {
			this.getSceneManager().push(new MainMenuScene(this.getSceneManager()));
			tick.play();
		} else if(Gdx.input.justTouched()) {
			counter = 10f;
		}
	}
	
	private GameObject newText(String message, int index) {
		return newText(message, index, Color.WHITE);
	}
	
	private GameObject newText(String message, int index, Color c) {
		GameObject text =
				new TextObject()
				.setFont(this.getSceneManager().getFont())
				.setMessage(message, 2f, c)
				.setPosition(new Vector2(0, LD42Game.HEIGHT - 40 * index))
				.setCentered(true, false);
		this.getGuiObjects().add(text);
		text.setVisibility(false);
		return text;
	}

}
