package map;

import net.sf.json.JSONObject;

public class Bullet {
	private int type;
	private int team;
	private int x;
	private int y;


	private String direction;
	
	public Bullet(JSONObject object) {
		this.type = object.getInt("type");
		this.team = object.getInt("team");
		this.x = object.getInt("x");
		this.y = object.getInt("y");
		this.direction = object.getString("direction");
	}
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getTeam() {
		return team;
	}

	public void setTeam(int team) {
		this.team = team;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

}
