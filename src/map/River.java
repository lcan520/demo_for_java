package map;

import net.sf.json.JSONObject;

public class River {
	private int x;
	private int y;
	
	public River(JSONObject object) {
		this.x = object.getInt("x");
		this.y = object.getInt("y");
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
}
