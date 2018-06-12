package demo;

import map.Point;

public class SpanSearchPath extends SearchPath {
    @Override
    public void nextMove(int x, int y, int to_x, int to_y, Object[][] map) {
        int addx = 1;
        if (to_x < x) addx = -1;
        int addy = 1;
        if (to_y < y) addy = -1;

        if (!isWall(x + addx, y, map)) {
            //如果在同一x坐标上
            if (to_y == y) Move(x + addx, y, to_x, to_y, map);
            if (to_x == x) Move(x, y + addy, to_x, to_y, map);
            //x向目标一步无障碍（假设目标在起始点右下方）
            Move(x + addx, y, to_x, to_y, map);  //往前走
            if (isWall(x + addx, y + addy, map)) { //右下方有障碍，往下走
                Move(x, y + addy, to_x, to_y, map);
            }
            if (isWall(x + addx, y - addy, map)) {   //右上方有障碍，往上走
                Move(x, y - addy, to_x, to_y, map);
            }
            if (isWall(x - addx, y + addy, map)) {  //左下方有障碍，往下走，往后走
                Move(x, y + addy, to_x, to_y, map);
                Move(x - addx, y, to_x, to_y, map);
            }
            if (isWall(x - addx, y - addy, map)) {     //左上方有障碍，往上走,往后走
                Move(x, y - addy, to_x, to_y, map);
                Move(x - addx, y, to_x, to_y, map);
            }

        } else {  //若前方有障碍
            if (!isWall(x, y + addy, map)) { //优先向下走
                Move(x, y + addy, to_x, to_y, map);
            }

            if (!isWall(x, y - addy, map)) { //向上走
                Move(x, y - addy, to_x, to_y, map);
            }
        }
    }
}
