package ceres.ld42.map;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import ceres.ld42.object.GameObject;
import ceres.ld42.object.TextObject;
import ceres.ld42.scene.PlayScene;

public class Hex {
	private static final int WIDTH = 30;
	private static final int HEIGHT = 23;
	private static final int scale = 2;
	
	private HexMap map;
	private GameObject hexObject, buildingObject, resourceObject, resourceTextObject;
	private ResourceType resource;
	private int rank = 1;
	private PlayScene scene;
	public boolean hasBuilding = false;
	
	private int q, r;
	
	private Vector2 position;
	private HexType type;
	public Hex(int q, int r, HexMap map, HexType t, PlayScene scene) {
		this.q = q;
		this.r = r;
		
		this.map = map;
		
		int x = (int) (WIDTH * ((q + r / 2.f) - map.getSize() / 2f));
		int y = (int) (HEIGHT * (r - map.getSize() / 2f));
		
		this.position = new Vector2(scale * x, scale * y);
		
		this.type = t;
		this.scene = scene;
		
		//Add GameObjects
		hexObject = new GameObject()
				.setTexture(scene.hexTypeToTexture(type), scale)
				.setPosition(this.position);
		scene.getBgObjects().add(hexObject);
		
		buildingObject = new GameObject()
				.setPosition(this.position);
		scene.getBgObjects().add(buildingObject);
		
		resourceObject = new GameObject()
				.setTexture(scene.resTypeToTexture(resource), 1f)
				.setPosition(new Vector2(this.position.x + 16, this.position.y + 12));
		scene.getObjects().add(resourceObject);
		
		resourceTextObject = new TextObject()
				.setFont(scene.getSceneManager().getFont())
				.setPosition(new Vector2(this.position.x + 32, this.position.y + 16));
		scene.getObjects().add(resourceTextObject);
		
		//Add base resource
		if(type == HexType.DUST && Math.random() < 0.66) {
			for(int i = 0; i < (int) (Math.random() * 3 + 1); i++)
				this.setResource(ResourceType.ENERGY, false);
		}
		if(type == HexType.BASALT && Math.random() < 0.66) this.setResource(ResourceType.PRODUCTION, false);
		if(type == HexType.CRATER) {
			resource = ResourceType.IMPASSABLE;
			this.resourceObject.setTexture(scene.resTypeToTexture(this.resource), 1f);
		}
	}
	
	public Vector2 getPosition() {
		return this.position;
	}
	
	public HexType getType() {
		return this.type;
	}
	
	public void setResource(ResourceType type, boolean override) {
		if(this.resource == type) {
			this.rank++;
		} else if((!this.hasBuilding && this.resource != ResourceType.IMPASSABLE) || override) {		
			this.resource = type;
			this.rank = 1;
			this.resourceObject.setTexture(scene.resTypeToTexture(this.resource), 1f);
		}
		
		if(this.rank > 1) ((TextObject) this.resourceTextObject).setMessage("x" + this.rank, 1f, Color.GOLD);
		else this.resourceTextObject.setVisibility(false);
	}
	
	public boolean addBuilding(BuildingType type) {
		if(hasBuilding || this.resource == ResourceType.IMPASSABLE) return false;
		
		this.setResource(type.getResource(), false);
		this.buildingObject.setTexture(scene.buildingTypeToTexture(type), scale);
		this.hasBuilding = true;
		
		//ADJ BONUSES
		for(Hex h : this.getAdjacent()) {
			if(this.resource == ResourceType.HOUSING) 
				h.setResource(ResourceType.HOUSING, false);
			else if(this.resource == ResourceType.FOOD) 
				h.setResource(ResourceType.O2, false);
			else if(this.resource == ResourceType.ENERGY)
				h.setResource(ResourceType.PRODUCTION, false);
		}
		
		return true;
	}
	
	public boolean isTouched(OrthographicCamera camera) {
		Vector3 pos = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
		
		float x = pos.x;
		float y = pos.y;
		float x2 = position.x + 16f * scale;
		float y2 = position.y + 16f * scale;
		
		double dist2 = Math.pow(x - x2, 2f) + Math.pow(y - y2, 2f);

		float r = (HEIGHT * scale) / 2f;
		return (dist2 < r * r);
	}
	
	public ArrayList<Hex> getAdjacent() {
		ArrayList<Hex> hexes = new ArrayList<Hex>();
		Hex[][] hexmap = map.getHexMap();
		for(int i = -1; i <= 1; i++) {
			for(int j = -1; j <= 1; j++) {
				if(i == j) continue;
				int x = this.q - i;
				int y = this.r - j;
				if(x < 0 || y < 0 || x >= hexmap.length || y >= hexmap[0].length)
					continue;
				if(hexmap[x][y] == null)
					continue;
				hexes.add(hexmap[x][y]);
			}
		}
		return hexes;
	}
	
	public GameObject getBuildingObject() {
		return this.buildingObject;
	}
	
	public ResourceType getResourceType() {
		return this.resource;
	}
	
	public int getRank() {
		return this.rank;
	}
}
