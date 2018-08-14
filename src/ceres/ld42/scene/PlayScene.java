package ceres.ld42.scene;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import ceres.ld42.LD42Game;
import ceres.ld42.map.BuildingType;
import ceres.ld42.map.Hex;
import ceres.ld42.map.HexMap;
import ceres.ld42.map.HexType;
import ceres.ld42.map.ResourceType;
import ceres.ld42.object.GameObject;
import ceres.ld42.object.TextObject;

public class PlayScene extends Scene {
	//Hex Textures
	public Texture hexTexture = this.getSceneManager().getAssetManager().get("textures/hex/hex.png", Texture.class);
	public Texture dustTexture = this.getSceneManager().getAssetManager().get("textures/hex/dust.png", Texture.class);
	public Texture basaltTexture = this.getSceneManager().getAssetManager().get("textures/hex/basalt.png", Texture.class);
	public Texture iceTexture = this.getSceneManager().getAssetManager().get("textures/hex/ice.png", Texture.class);
	public Texture craterTexture = this.getSceneManager().getAssetManager().get("textures/hex/crater.png", Texture.class);
	
	//Building textures
	public Texture hab = this.getSceneManager().getAssetManager().get("textures/buildings/hab.png", Texture.class);
	public Texture solar = this.getSceneManager().getAssetManager().get("textures/buildings/solar.png", Texture.class);
	public Texture moxie = this.getSceneManager().getAssetManager().get("textures/buildings/moxie.png", Texture.class);
	public Texture factory = this.getSceneManager().getAssetManager().get("textures/buildings/factory.png", Texture.class);
	public Texture farm = this.getSceneManager().getAssetManager().get("textures/buildings/farm.png", Texture.class);
	
	public Texture dead_hab = this.getSceneManager().getAssetManager().get("textures/buildings/dead_hab.png", Texture.class);
	
	//Resource textures
	public Texture energyTexture = this.getSceneManager().getAssetManager().get("textures/resources/energy.png", Texture.class);
	public Texture foodTexture = this.getSceneManager().getAssetManager().get("textures/resources/food.png", Texture.class);
	public Texture oxygenTexture = this.getSceneManager().getAssetManager().get("textures/resources/oxygen.png", Texture.class);
	public Texture productionTexture = this.getSceneManager().getAssetManager().get("textures/resources/production.png", Texture.class);
	public Texture housingTexture = this.getSceneManager().getAssetManager().get("textures/resources/housing.png", Texture.class);
	public Texture impassableTexture = this.getSceneManager().getAssetManager().get("textures/resources/impassable.png", Texture.class);
	
	//GUI
	private Texture banner1Texture = this.getSceneManager().getAssetManager().get("textures/gui/banner_1.png", Texture.class);
	private Texture banner2Texture = this.getSceneManager().getAssetManager().get("textures/gui/banner_2.png", Texture.class);
	private Texture banner3Texture = this.getSceneManager().getAssetManager().get("textures/gui/banner_3.png", Texture.class);
	private Texture nextButtonTexture = this.getSceneManager().getAssetManager().get("textures/gui/next.png", Texture.class);
	
	//Audio
	private Sound pip = this.getSceneManager().getAssetManager().get("audio/good_pip.wav", Sound.class);
	private Sound bad_pip = this.getSceneManager().getAssetManager().get("audio/bad_pip.wav", Sound.class);
	private Sound gameOverSound = this.getSceneManager().getAssetManager().get("audio/game_over.wav", Sound.class);
	private Sound tick = this.getSceneManager().getAssetManager().get("audio/tick.wav", Sound.class);
	
	private int losing = -1;
	private int turnCount = 0;
	private boolean gameOver, needHousing;
	private float waitTimer = 1f;
	
	private GameObject habButton, solarButton, moxieButton, factoryButton, farmButton, 
		nextButton, losingText, exitButton, losingBanner, exitBanner;
	
	//Hexmap
	private HexMap map;
	
	private BuildingType isPlacing;
	private GameObject isPlacingObject;
	private HashMap<ResourceType, TextObject> resourceCountersObj;
	private HashMap<ResourceType, Integer> resourceCounters;
	private HashMap<ResourceType, Integer> resourceChangeCounters;
	
	public PlayScene(SceneManager sm) {
		super(sm);
		
		isPlacingObject = new GameObject();
		this.getFgObjects().add(isPlacingObject);
		
		//Map
		map = new HexMap(15, this);

		this.getGuiObjects().add(
				new GameObject()
				.setTexture(banner1Texture, 8f)
				.setWidth(LD42Game.WIDTH)
				);
		this.getGuiObjects().add(
				new GameObject()
				.setTexture(banner1Texture, 8f)
				.setWidth(LD42Game.WIDTH)
				.setPosition(new Vector2(0, LD42Game.HEIGHT - 64))
				);
		
		habButton = addButton(0, BuildingType.HAB, ResourceType.HOUSING, ResourceType.HOUSING);
		solarButton = addButton(1, BuildingType.SOLAR, ResourceType.ENERGY, ResourceType.PRODUCTION);
		factoryButton = addButton(2, BuildingType.FACTORY, ResourceType.PRODUCTION, null);
		farmButton = addButton(3, BuildingType.FARM, ResourceType.FOOD, ResourceType.O2);
		moxieButton = addButton(4, BuildingType.MOXIE, ResourceType.O2, null);
		nextButton = new GameObject()
				.setTexture(nextButtonTexture, 8f)
				.setPosition(new Vector2(LD42Game.WIDTH - (16 * 8), 0));
		this.getGuiObjects().add(nextButton);
		
		//Counters
		resourceCountersObj = new HashMap<ResourceType, TextObject>();
		resourceCounters = new HashMap<ResourceType, Integer>();
		resourceChangeCounters = new HashMap<ResourceType, Integer>();
		int i = 0;
		for(ResourceType r : ResourceType.values()) {
			if(r == ResourceType.IMPASSABLE) continue;
			this.getGuiObjects().add(
					new GameObject()
					.setTexture(resTypeToTexture(r), 4f)
					.setPosition(new Vector2(i * 256 + 16, LD42Game.HEIGHT - 50))
					);
			TextObject text = 
					(TextObject) new TextObject()
					.setFont(this.getSceneManager().getFont())
					.setPosition(new Vector2(i * 256 + 16 + 64, LD42Game.HEIGHT - 32));
			resourceCountersObj.put(r, text);
			resourceCounters.put(r, 0);
			
			resourceChangeCounters.put(r, 0);
			
			this.getGuiObjects().add(text);
					
			i++;
		}
		resourceCounters.put(ResourceType.PRODUCTION, 100);
		
		losingText = 
			new TextObject()
			.setFont(this.getSceneManager().getFont())
			.setMessage("", 2f, Color.BLACK)
			.setPosition(new Vector2(0, LD42Game.HEIGHT - 128));
		losingBanner = new GameObject()
				.setTexture(banner2Texture, 3f)
				.setPosition(new Vector2(0, LD42Game.HEIGHT - 128 - 8))
				.setWidth(LD42Game.WIDTH);
		
		this.getGuiObjects().add(losingBanner);
		this.getGuiObjects().add(losingText);
			
		exitBanner = new GameObject()
				.setTexture(banner3Texture, 6f)
				.setWidth(256)
				.setCentered(true, true);
		exitBanner.setVisibility(false);
		exitBanner.getPosition().x-=4;
		exitBanner.getPosition().y-=12;
		this.getGuiObjects().add(exitBanner);
		
		exitButton = new TextObject()
				.setFont(this.getSceneManager().getFont())
				.setMessage("EXIT", 8f, Color.GOLD)
				.setCentered(true, true);
		exitButton.setVisibility(false);
		this.getGuiObjects().add(exitButton);
		
		nextTurn();
		updateResourceChange();
		
		this.getGuiObjects().add(new TextObject()
				.setFont(this.getSceneManager().getFont())
				.setMessage("Cost: 10", 3f, Color.BLACK)
				.setPosition(new Vector2(LD42Game.WIDTH - 480 + 24 + 32, 36)));
		this.getGuiObjects().add(new GameObject()
				.setTexture(productionTexture, 4f)
				.setPosition(new Vector2(LD42Game.WIDTH - 256 + 32, 24)));
	}
	
	private void updateResourceChange() {
		for(ResourceType r : ResourceType.values()) {
			if(r == ResourceType.IMPASSABLE) continue;
			if(r == ResourceType.HOUSING) resourceChangeCounters.put(r, -1);
			else resourceChangeCounters.put(r, 0);
		}
		
		resourceCounters.put(ResourceType.HOUSING, 1-turnCount);
		for(Hex[] hs : map.getHexMap()) {
			for(Hex h : hs) {
				if(h != null && h.hasBuilding) {
					if(h.getResourceType() == ResourceType.HOUSING) {
						resourceCounters.put(ResourceType.HOUSING, resourceCounters.get(ResourceType.HOUSING) + h.getRank());
						resourceChangeCounters.put(ResourceType.FOOD, resourceChangeCounters.get(ResourceType.FOOD) - 1);
						resourceChangeCounters.put(ResourceType.O2, resourceChangeCounters.get(ResourceType.O2) - h.getRank());
					}
					else
						resourceChangeCounters.put(h.getResourceType(), resourceChangeCounters.get(h.getResourceType()) + h.getRank());
					
					if(h.getResourceType() != ResourceType.ENERGY)
						resourceChangeCounters.put(ResourceType.ENERGY, resourceChangeCounters.get(ResourceType.ENERGY) - 1);
				}
			}
		}
		
		updateCounterText();
	}
	
	private void updateCounterText() {
		for(ResourceType r : ResourceType.values()) {
			if(r == ResourceType.IMPASSABLE) continue;
			
			String msg = "" + resourceCounters.get(r) +
					(resourceChangeCounters.get(r) >= 0 ? " + " : " - ") + 
					(int) Math.abs(resourceChangeCounters.get(r));
			if(r != ResourceType.HOUSING)
				resourceCountersObj.get(r).setMessage(msg, 2f, resourceCounters.get(r) < 0 ? Color.RED : Color.BLACK);
			else
				resourceCountersObj.get(r).setMessage(msg, 2f, resourceCounters.get(r) - 1 < 0 ? Color.RED : Color.BLACK);
		}
		
		if(needHousing && resourceCounters.get(ResourceType.HOUSING) >= 1) {
			if(losing != -1) {
				((TextObject) losingText)
				.setMessage("Negative resources! You will lose in " + losing + " turns", 3f, Color.MAROON)
				.setCentered(true, false);
			} else {
				losingText.setVisibility(false);
				losingBanner.setVisibility(false);
			}
			needHousing = false;
		}
	}
	
	private void nextTurn() {
		boolean flag = false;
		for(ResourceType r : ResourceType.values()) {
			if(r == ResourceType.IMPASSABLE) continue;
			resourceCounters.put(r, resourceCounters.get(r) + resourceChangeCounters.get(r));
			if(resourceCounters.get(r) < 0) flag = true;
		}
		
		if(flag) {
			if(losing == -1) losing = 10;
			else losing--;
			
			((TextObject) losingText)
				.setMessage("Negative resources! You will lose in " + losing + " turns", 3f, Color.MAROON)
				.setCentered(true, false);
			losingBanner.setVisibility(true);
		} else {
			losing = -1;
			losingText.setVisibility(false);
			losingBanner.setVisibility(false);
		}
		
		if(resourceCounters.get(ResourceType.HOUSING) < 0)
			losing = 0;
		else if(resourceCounters.get(ResourceType.HOUSING) == 0) {
			((TextObject) losingText)
			.setMessage("Build housing now, or you will lose.", 3f, Color.MAROON)
			.setCentered(true, false);
			losingBanner.setVisibility(true);
			needHousing = true;
		}
		
		if(losing == 0) {
			gameOverSound.play();
			exitButton.setVisibility(true);
			exitBanner.setVisibility(true);
			((TextObject) losingText)
			.setMessage("You ran out of space after " + turnCount + (turnCount > 1 ? " turns!" : " turn!"), 3f, Color.MAROON)
			.setCentered(true, false);
			gameOver = true;
			
			for(Hex[] hs : map.getHexMap()) {
				for(Hex h : hs) {
					if(h != null && h.hasBuilding && h.getResourceType() == ResourceType.HOUSING) {
						h.getBuildingObject().setTexture(dead_hab, 2f);
					}
				}
			}
			
		} else {
			if(turnCount != 0) tick.play();
			turnCount++;
		}
		
		updateCounterText();
	}
	
	@Override
	public void update(float dt) {
		if(waitTimer > 0) waitTimer -= dt;
		if(gameOver) return;
		
		Vector3 pos = this.getSceneManager().getCamera().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
		isPlacingObject.setPosition(new Vector2(pos.x - 32, pos.y - 32));
	}
	
	@Override
	public void handleInput(float dt) {
		if(gameOver) {
			if(Gdx.input.justTouched()) {
				if(exitButton.isTouched())
					this.getSceneManager().pop();
				bad_pip.play();
			}
			return;
		}
		
		if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT) && this.isPlacing != null) 
			setPlacing(null);
		
		if(Gdx.input.justTouched() && waitTimer <= 0) {
			if(habButton.isTouched())
				this.setPlacing(BuildingType.HAB);
			else if(solarButton.isTouched())
				this.setPlacing(BuildingType.SOLAR);
			else if(factoryButton.isTouched())
				this.setPlacing(BuildingType.FACTORY);
			else if(farmButton.isTouched())
				this.setPlacing(BuildingType.FARM);
			else if(moxieButton.isTouched())
				this.setPlacing(BuildingType.MOXIE);			
			else if(nextButton.isTouched())
				nextTurn();
			else if(isPlacing != null) {
				for(int i = 0; i < map.getSize(); i++) {
					for(int j = 0; j < map.getSize(); j++) {
						Hex h = map.getHexMap()[i][j];
						if(h == null) continue;
						if(h.isTouched(this.getSceneManager().getCamera())) {
							if(h.hasBuilding || h.getResourceType() == ResourceType.IMPASSABLE) {
								bad_pip.play();
							} else {
								pip.play();
								h.addBuilding(isPlacing);
								resourceCounters.put(ResourceType.PRODUCTION, resourceCounters.get(ResourceType.PRODUCTION) - 10);
								if(!((Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT)) && resourceCounters.get(ResourceType.PRODUCTION) >= 10)) setPlacing(null);
								updateResourceChange();
							}
						}
					}
				}	
			}
			waitTimer = 0.1f;
		}
		
		float speed = 320;
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
			this.getSceneManager().getCamera().position.x -= speed * dt;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
			this.getSceneManager().getCamera().position.x += speed * dt;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
			this.getSceneManager().getCamera().position.y += speed * dt;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
			this.getSceneManager().getCamera().position.y -= speed * dt;
		}
		
		if((Gdx.input.isKeyJustPressed(Input.Keys.MINUS) || Gdx.input.isKeyJustPressed(Input.Keys.PERIOD)) && this.getSceneManager().getCamera().zoom < 2f) {
			this.getSceneManager().getCamera().zoom *= 2f;
		}
		if((Gdx.input.isKeyJustPressed(Input.Keys.PLUS) || Gdx.input.isKeyJustPressed(Input.Keys.COMMA)) && this.getSceneManager().getCamera().zoom > 0.25f) {
			this.getSceneManager().getCamera().zoom *= .5f;
		}
	}
	
	public Texture hexTypeToTexture(HexType type) {
		if(type == null) return null;
		switch(type) {
			case DUST: return dustTexture;
			case BASALT: return basaltTexture;
			case CRATER: return craterTexture;
			case ICE: return iceTexture;
			default: return hexTexture;
		}
	}
	
	public Texture resTypeToTexture(ResourceType type) {
		if(type == null) return null;
		switch(type) {
			case O2: return oxygenTexture;
			case FOOD: return foodTexture;
			case ENERGY: return energyTexture;
			case PRODUCTION: return productionTexture;
			case HOUSING: return housingTexture;
			case IMPASSABLE: return impassableTexture;
			default: return null;
		}
	}
	
	public Texture buildingTypeToTexture(BuildingType type) {
		if(type == null) return null;
		switch(type) {
			case HAB: return hab;
			case FARM: return farm;
			case FACTORY: return factory;
			case SOLAR: return solar;
			case MOXIE: return moxie;
			default: return null;
		}
	}
	
	private void setPlacing(BuildingType type) {
		if(type != null && this.resourceCounters.get(ResourceType.PRODUCTION) < 10) {
			bad_pip.play();
			return;
		}
		
		this.isPlacing = type;
		if(type == null) this.isPlacingObject.setVisibility(false);
		else this.isPlacingObject.setTexture(buildingTypeToTexture(type), 2f);
	}
	
	private GameObject addButton(int index, BuildingType type, ResourceType baseResource, ResourceType adjResource) {
		int x = index * 160;
		
		GameObject button = new GameObject()
				.setTexture(buildingTypeToTexture(type), 4f)
				.setPosition(new Vector2(8 + x, 48));
		this.getGuiObjects().add(button);
		
		//Building name
		this.getGuiObjects().add( 
				new TextObject()
				.setFont(this.getSceneManager().getFont())
				.setMessage(type.toString(), 2f, Color.BLACK)
				.setPosition(new Vector2(20 + x, 40))
				);
		
		//Building base yield
		this.getGuiObjects().add(
				new GameObject()
				.setTexture(resTypeToTexture(baseResource), 2f)
				.setPosition(new Vector2(8 + x, 8))
				);
		
		if(adjResource != null) {
			this.getGuiObjects().add(
				new TextObject()
				.setFont(this.getSceneManager().getFont())
				.setMessage("ADJ", 2f, Color.BLACK)
				.setPosition(new Vector2(42 + x, 12))
				);
			this.getGuiObjects().add(
				new GameObject()
				.setTexture(resTypeToTexture(adjResource), 2f)
				.setPosition(new Vector2(96 + x, 8))
				);	
		}
		
		return button;
	}
}
