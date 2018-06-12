package map;

public class Point {
    private  int x;
    private  int y;
    public Point(){}
    public Point(int x,int y){
        this.x=x;this.y=y;
    }
    public boolean equals(Point point){
        if(point.getX() == this.x && point.getY() == this.y) return true;
        return false;
    }
    public int distance(Point point){
        return  Math.abs(point.getX() - this.x) + Math.abs(point.getY() - this.y);
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
