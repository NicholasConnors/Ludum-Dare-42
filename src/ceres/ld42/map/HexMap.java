package ceres.ld42.map;

import ceres.ld42.scene.PlayScene;

public class HexMap {
	private int size;
	private Hex[][] hexmap;
	
	public HexMap(int size, PlayScene scene) {
		this.size = size;
		this.hexmap = new Hex[size][size];
		
		//Map generation
		for(int x = 0; x < size; x++) {
			for(int y = 0; y < size; y++) {
				if(x + y < Math.floor(size *0.5f) || x + y >= Math.floor(size * 1.5f)) continue;
				
				HexType type = Math.random() > 0.5 ? HexType.DUST : HexType.BASALT;
				if(Math.random() < 0.1) type = HexType.CRATER;
				if(y == 0 || y == size - 1) type = HexType.ICE;
				if((y == 1 || y == size - 2) && Math.random() < 0.3) type = HexType.ICE;
				
				hexmap[x][y] = new Hex(x, y, this, type, scene);
			}
		}
	}
	
	public int getSize() {
		return this.size;
	}
	
	public Hex[][] getHexMap() {
		return this.hexmap;
	}
}
