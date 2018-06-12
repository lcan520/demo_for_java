package demo;

import map.*;

import java.util.ArrayList;
import java.util.List;

public abstract class SearchPath {
    public  Boolean end = false;
    protected List<Point>  points = new ArrayList<Point>();
    protected List<String> direction = new ArrayList<String>();
    public SearchPath() {
    }
    public boolean isEnd(int star_x, int star_y, int to_x, int to_y) {
        if ((star_x == to_x) && (star_y == to_y)) return true;
        return false;
    }

    public void Move(int star_x, int star_y, int to_x, int to_y, Object[][] map){
        if(points.size()>80) return;
        if (end) return;
        if (star_x > 14 || star_x < 0 || to_x > 14 || to_x < 0) return;
        if (isWall(star_x, star_y, map)) return;
        if (hasMove(star_x, star_y)) return;
        if (isEnd(star_x, star_y, to_x, to_y)) {
            if(points.isEmpty()) {
                points.add(new Point(star_x, star_y));
                nextMove(star_x, star_y, to_x, to_y, map);
            }
            points.add(new Point(star_x, star_y));
            end = true;
            return;
        }
        //System.out.println("输出x:" + star_x + "  " + "输出y：" + star_y);
        points.add(new Point(star_x, star_y));
        nextMove(star_x, star_y, to_x, to_y, map);
    }

    public boolean isBrickWall(int x, int y, Object[][] map) {
        if (x < 0 || x > 14 || y < 0 || y > 14) return true;
        return map[x][y] instanceof BrickWall ;
    }
    public boolean isIronWall(int x, int y, Object[][] map) {
        if (x < 0 || x > 14 || y < 0 || y > 14) return true;
        return map[x][y] instanceof IronWall ;
    }
    public boolean isWall(int x, int y, Object[][] map) {
        if (x < 0 || x > 14 || y < 0 || y > 14) return true;
        return map[x][y] instanceof IronWall || map[x][y] instanceof River;
    }
    public String isBullet(int x, int y, Object[][] map) {
        String bullet_driection ="";
        if (x < 0 || x > 14 || y < 0 || y > 14) return bullet_driection;
        if(map[x][y] instanceof Bullet){
            Bullet bullet = (Bullet) map[x][y];
            if(bullet.getTeam()==Client.enemy.getId()) {
                bullet_driection = bullet.getDirection();
            }
        }
        return bullet_driection;
    }
    public boolean isEnemy(int x, int y, Object[][] map) {
        boolean isEnemy =false;
        if (x < 0 || x > 14 || y < 0 || y > 14) return false;
        if(map[x][y] instanceof Player){
            Player player = (Player) map[x][y];
            if(player.getTeam()==Client.enemy.getId()) isEnemy=true;
        }
        return isEnemy;
    }
    public boolean isSelf(int x, int y, Object[][] map) {
        boolean isSelf =false;
        if (x < 0 || x > 14 || y < 0 || y > 14) return true;
        if(map[x][y] instanceof Player){
            Player player = (Player) map[x][y];
            if(player.getTeam()==Client.self.getId()) isSelf=true;
        }

        return map[x][y] instanceof Player;
    }
    public boolean isSelfPlayer(int x, int y, Object[][] map) {
        boolean isSelf =false;
        if (x < 0 || x > 14 || y < 0 || y > 14) return true;
        if(map[x][y] instanceof Player){
            Player player = (Player) map[x][y];
            if(player.getTeam()==Client.self.getId()) isSelf=true;
        }

        return isSelf;
    }
    public boolean hasMove(int x, int y) {
        for (Point point : points) {
            if (point.getX() == x && point.getY() == y) return true;
        }
        return false;
    }


    public abstract void nextMove(int x, int y, int to_x, int to_y, Object[][] map);

    public List<String> getDirection(List<Point> points) {
        List<String> directions = new ArrayList<String>();
        for (int i = 0; i < points.size(); i++) {
            int x = points.get(i).getX() - points.get(i + 1).getX();
            int y = points.get(i).getY() - points.get(i + 1).getY();
            ;
            if (x == 0) {
                if (y == 0) {
                    return null;
                } else if (y == 1) directions.add("up");
                else directions.add("down");
            } else if (x == 1) directions.add("right");
            else directions.add("left");
        }

        return directions;
    }

    public List<String> getDirection() {
        if(end) {
            for (int i = 0; i < points.size()-1 ; i++) {
                int x = points.get(i).getX() - points.get(i + 1).getX();
                int y = points.get(i).getY() - points.get(i + 1).getY();
                ;
                if (x == 0) {
                    if (y == 0) {
                        return new ArrayList<>();
                    } else if (y == 1) direction.add("up");
                    else direction.add("down");
                } else if (x == 1) direction.add("left");
                else direction.add("right");
            }
        }else {
            System.out.println("当前路径不可达！");
            direction.clear();
        }
        return direction;
    }

    public List<Point> getPoints() {
        for (int i = 0; i < this.points.size() - 1; i++) {
            boolean exist = false;
            for (int j = this.points.size() - 1; j > i; j--) {
                if (this.points.get(j).equals(this.points.get(i))) {
                    System.out.println("删除J" + this.points.get(j).getX() + "  " + this.points.get(j).getY());
                    this.points.remove(j);
                    exist = true;
                }
            }
            if (exist) {
                System.out.println("删除I" + this.points.get(i).getX() + "  " + this.points.get(i).getY());
                this.points.remove(i);
                i--;
            }
        }
        return points;
    }

    public List<Point> getPointsByDistance() {
        if(end) {
            for (int i = this.points.size() - 1; i > 0; i--) {
                if (this.points.get(i).distance(this.points.get(i - 1)) > 1) {
                    System.out.println("删除i" + this.points.get(i - 1).getX() + "  " + this.points.get(i - 1).getY());
                    this.points.remove(i - 1);
                    i = i--;
                }
            }
        }
        return points;
    }

//    public static void main(String[] args) {
//
//        SearchPath spanSearchPath = new SearchPath();
//       SearchPath.getPointsByDistance();
//    }

}
