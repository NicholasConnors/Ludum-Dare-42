package ceres.ld42.map;

public enum BuildingType {
	HAB(ResourceType.HOUSING),
	SOLAR(ResourceType.ENERGY),
	FACTORY(ResourceType.PRODUCTION),
	FARM(ResourceType.FOOD),
	MOXIE(ResourceType.O2);
	
	private ResourceType res;
	BuildingType(ResourceType res) {
		this.res = res;
	}
	
	public ResourceType getResource() {
		return this.res;
	}
}
