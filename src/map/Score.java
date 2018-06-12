package map;

import net.sf.json.JSONObject;

public class Score {
	private int id;
	private int point;
	private int life;
	
	public Score(JSONObject object) {
		this.id = object.getInt("id");
		this.point = object.getInt("point");
		this.life = object.getInt("remain_life");
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}
}
