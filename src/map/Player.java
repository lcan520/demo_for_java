package map;

import net.sf.json.JSONObject;

public class Player {
	private int id;
	private int team;
	private int x;
	private int y;
	private int superBullet;
	public Player(){}
	public Player(JSONObject object) {
		this.id = object.getInt("id");
		this.team = object.getInt("team");
		this.x = object.getInt("x");
		this.y = object.getInt("y");
		this.superBullet = object.getInt("super_bullet");
	}
	
	public int getId()
	{
		return this.id;
	}
	
	public int getTeam()
	{
		return this.team;
	}

	public void setId(int id) {
		this.id = id;
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

	public int getSuperBullet() {
		return superBullet;
	}

	public void setSuperBullet(int superBullet) {
		this.superBullet = superBullet;
	}
}
