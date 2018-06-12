package demo;

public class UpSearchPath extends SearchPath {
    @Override
    public void nextMove(int x, int y, int to_x, int to_y, Object[][] map) {
        int addx = 1;
        if (to_x < x) addx = -1;
        if (to_x == x) addx = -1;
        int addy = 1;
        if (to_y < y) addy = -1;
        if (to_y == y) addy = -1;

        if (!isWall(x, y + addy, map)) {
            //如果在同一x坐标上
            if (to_y == y) Move(x + addx, y, to_x, to_y, map);
            if (to_x == x) Move(x, y + addy, to_x, to_y, map);
            //x向目标一步无障碍（假设目标在起始点右下方）
            Move(x , y+addy, to_x, to_y, map);  //往下走
            if (isWall(x + addx, y + addy, map)) { //右下方有障碍，往前走
                Move(x + addx, y , to_x, to_y, map);
            }
            if (isWall(x + addx, y - addy, map)) {   //右上方有障碍，往后走
                Move(x - addy, y, to_x, to_y, map);
            }
            if (isWall(x - addx, y + addy, map)) {  //左下方有障碍，往前走，往后上
                Move(x + addx, y , to_x, to_y, map);
                Move(x , y - addy, to_x, to_y, map);
            }
            if (isWall(x - addx, y - addy, map)) {     //左上方有障碍，往后走,往上走
                Move(x - addy, y , to_x, to_y, map);
                Move(x , y - addy, to_x, to_y, map);
            }

        } else {  //若下方有障碍
            if (!isWall(x + addx, y, map)) { //优先向前走
                Move(x + addx, y , to_x, to_y, map);
            }

            if (!isWall(x - addx, y, map)) { //向后走
                Move(x - addy, y , to_x, to_y, map);
            }
        }
    }
}
