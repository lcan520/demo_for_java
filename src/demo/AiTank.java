package demo;

import map.*;

import java.util.*;

public class AiTank {
    private List<String> coin_directions = new ArrayList<String>();
    private List<String> star_directions = new ArrayList<String>();
    private List<String> enemy_directions = new ArrayList<String>();
    private List<String> has_directions = new ArrayList<String>();
    private int isSuperBullet = 0;
    private boolean isSuperBullet_one = false;
    private boolean only_one = false;
    private int coinSize = 0;
    private String direction = null;
    private String fire_direction = null;
    private Player enemyplayers_point = new Player();
    private int distence = 0;

    public String getDirection(Player selfplayer, List<Player> enemyplayers, List<Coin> coins, List<Star> stars, List<Bullet> bullets, Object[][] map, int roundId) {
        enemy_directions = attackEnemy(selfplayer, enemyplayers, coins, stars, bullets, map);
        coin_directions = getCoin(selfplayer, enemyplayers, coins, stars, bullets, map);
        star_directions = getStar(selfplayer, enemyplayers, coins, stars, bullets, map);
        if (!enemy_directions.isEmpty() && !coin_directions.isEmpty() && !star_directions.isEmpty()) {
            if (enemyplayers.size() != 1) {
                if (selfplayer.getSuperBullet() == 1) {
                    direction = enemy_directions.get(0);
                    setFire_direction(selfplayer, map);
                    return direction;
                } else if (star_directions.size() < coin_directions.size() + 4) {
                    direction = star_directions.get(0);
                    return direction;
                } else {
                    direction = coin_directions.get(0);
                    return direction;
                }
            } else {
                if (Client.self_size > 1 && enemy_directions.size() < 4 && selfplayer.getSuperBullet() != 1 && enemyplayers_point.getSuperBullet() != 1) {
                    direction = enemy_directions.get(0);
                    setFire_direction(selfplayer, map);
                    only_one = true;
                    return direction;
                } else if (selfplayer.getSuperBullet() == 1 && enemyplayers.get(0).getSuperBullet() == 1 && enemy_directions.size() < 10) {
                    direction = enemy_directions.get(0);
                    only_one = true;
                    setFire_direction(selfplayer, map);
                    return direction;
                } else if (selfplayer.getSuperBullet() != 1 && enemyplayers.get(0).getSuperBullet() == 1) {
                    enemyplayers_point = enemyplayers.get(0);
                    direction = star_directions.get(0);
                    only_one = true;
                    if (enemy_directions.size() < 3)
                        setFire_direction(selfplayer, map);
                    return direction;
                } else if (selfplayer.getSuperBullet() == 1 && enemyplayers.get(0).getSuperBullet() != 1 && enemy_directions.size() < 6) {
                    direction = enemy_directions.get(0);
                    only_one = true;
                    isSuperBullet_one = true;
                    setFire_direction(selfplayer, map);
                    return direction;
                } else {
                    if (Client.self_size == 1) {
                        enemyplayers_point = enemyplayers.get(0);
                        direction = star_directions.get(0);
                        only_one = true;
                        if (enemy_directions.size() < 3)
                            setFire_direction(selfplayer, map);
                        return direction;
                    } else {
                        enemyplayers_point = enemyplayers.get(0);
                        direction = coin_directions.get(0);
                        only_one = true;
                        if (enemy_directions.size() < 3)
                            setFire_direction(selfplayer, map);
                        return direction;
                    }
                }
            }
        } else if (!enemy_directions.isEmpty() && coin_directions.isEmpty() && star_directions.isEmpty()) {
            direction = enemy_directions.get(0);
            setFire_direction(selfplayer, map);
            return direction;
        } else if (enemy_directions.isEmpty() && !coin_directions.isEmpty() && !star_directions.isEmpty()) {
            if (star_directions.size() < coin_directions.size() + 2) {
                direction = star_directions.get(0);
                return direction;
            } else {
                direction = coin_directions.get(0);
                return direction;
            }
        } else if (!enemy_directions.isEmpty() && coin_directions.isEmpty() && !star_directions.isEmpty()) {
            if (selfplayer.getSuperBullet() == 0) {
                direction = star_directions.get(0);
                return direction;
            } else {
                direction = enemy_directions.get(0);
                setFire_direction(selfplayer, map);
                return direction;
            }
        } else if (!enemy_directions.isEmpty() && !coin_directions.isEmpty() && star_directions.isEmpty()) {
            if (enemyplayers.size() > 1) {
                if (enemy_directions.size() < 5 || coin_directions.size() < 3 || selfplayer.getSuperBullet() == 1) {
                    direction = enemy_directions.get(0);
                    setFire_direction(selfplayer, map);
                    return direction;
                } else {
                    direction = coin_directions.get(0);
                    return direction;
                }
            } else if (Client.self_size > 1 && enemy_directions.size() < 4) {
                direction = enemy_directions.get(0);
                setFire_direction(selfplayer, map);
                only_one = true;
                return direction;
            } else {
                direction = coin_directions.get(0);
                return direction;
            }
        } else if (!coin_directions.isEmpty()) {
            direction = coin_directions.get(0);
            return direction;
        } else if (!star_directions.isEmpty()) {
            direction = star_directions.get(0);
            return direction;
        } else {
            Random rand = new Random();
            int randNumber = rand.nextInt(5);
            switch (randNumber) {
                case 0:
                    direction = "up";
                    break;
                case 1:
                    direction = "down";
                    break;
                case 2:
                    direction = "left";
                    break;
                case 3:
                    direction = "right";
                    break;
                default:
                    direction = "up";
                    break;
            }
        }
        return direction;
    }


    public String getDirection() {
        return this.direction;
    }

    public String getFire_direction() {
        return fire_direction;
    }

    /**
     * @param [selfplayers, bullets, map]
     * @return java.util.List<java.lang.String>
     * @author lizhilong
     * @date 2018/5/29 18:44
     * @description:躲避子弹
     */
    public void dodgeBullet(Player selfplayer, List<Bullet> bullets, Object[][] map) {
        int addy = 0;
        int addx = 0;
        List<String> has_directions = new ArrayList<String>();
        if (direction.equals("up")) addy = -1;
        if (direction.equals("down")) addy = 1;
        if (direction.equals("left")) addx = -1;
        if (direction.equals("right")) addx = 1;
        SpanSearchPath searchPath = new SpanSearchPath();
        if (selfplayer.getX() + addx > 14 || selfplayer.getX() + addx < 0) direction = d4;
        else {
            if (searchPath.isWall(selfplayer.getX() + addx, selfplayer.getY(), map) || searchPath.isSelf(selfplayer.getX() + addx, selfplayer.getY(), map))
                direction = d4;
            if (searchPath.isSelf(selfplayer.getX() + addx, selfplayer.getY(), map) || searchPath.isSelf(selfplayer.getX() + addx, selfplayer.getY(), map))
                direction = d4;
            if (selfplayer.getY() + 2 < 15 && searchPath.isBullet(selfplayer.getX() + addx, selfplayer.getY() + 2, map).equals("up")) {
                direction = d4;
            }
            if (selfplayer.getY() + 2 < 15 && searchPath.isEnemy(selfplayer.getX() + addx, selfplayer.getY() + 2, map)) {
                direction = d4;
            }
            if (selfplayer.getY() - 2 >= 0 && searchPath.isBullet(selfplayer.getX() + addx, selfplayer.getY() - 2, map).equals("down")) {
                Bullet bullet = (Bullet) map[selfplayer.getX() + addx][selfplayer.getY() - 2];
                if (bullet.getDirection().equals("down"))
                    direction = d4;
            }
            if (selfplayer.getY() - 2 >= 0 && searchPath.isEnemy(selfplayer.getX() + addx, selfplayer.getY() - 2, map)) {
                direction = d4;
            }
            if (selfplayer.getY() + 3 < 15 && searchPath.isBullet(selfplayer.getX() + addx, selfplayer.getY() + 3, map).equals("up")) {
                direction = d4;
            }
            if (selfplayer.getY() - 3 >= 0 && searchPath.isBullet(selfplayer.getX() + addx, selfplayer.getY() - 3, map).equals("down")) {
                direction = d4;
            }
        }
    }

    public void dodgeEnemy(Player selfplayer, List<Player> players, Object[][] map) {

    }


    /**
     * @param [selfplayers, bullets, map]
     * @return java.util.List<java.lang.String>
     * @author lizhilong
     * @date 2018/5/29 18:44
     * @description:攻击敌方坦克
     */
    public List<String> attackEnemy(Player selfplayer, List<Player> enemyplayers, List<Coin> coins, List<Star> stars, List<Bullet> bullets, Object[][] map) {
        List<String> directions = new ArrayList<String>();
        GetMinPath getMinPath = new GetMinPath(selfplayer, enemyplayers, coins, stars, bullets, map);
        directions = getMinPath.getEnemyMinPath();
        this.enemyplayers_point = getMinPath.getEnemyplayers_point();
        return directions;
    }

    public List<String> getCoin(Player selfplayer, List<Player> enemyplayers, List<Coin> coins, List<Star> stars, List<Bullet> bullets, Object[][] map) {
        List<String> directions = new ArrayList<String>();
        GetMinPath getMinPath = new GetMinPath(selfplayer, enemyplayers, coins, stars, bullets, map);
        directions = getMinPath.getCoinMinPath(this.only_one);
        return directions;
    }

    public List<String> getStar(Player selfplayer, List<Player> enemyplayers, List<Coin> coins, List<Star> stars, List<Bullet> bullets, Object[][] map) {
        List<String> directions = new ArrayList<String>();
        GetMinPath getMinPath = new GetMinPath(selfplayer, enemyplayers, coins, stars, bullets, map);
        directions = getMinPath.getStarMinPath();
        return directions;
    }

    public void xBullet(List<Bullet> bullets, Player selfplayer, String d1, String d2, String d3, String d4, int index, Object[][] map) {
        if (bullets.get(index).getDirection().equals(d1)) {
            this.fire_direction = d2;
            int addy = 0;
            SpanSearchPath searchPath = new SpanSearchPath();
            direction = d3;
            if (d3.equals("up")) addy = -1;
            if (d3.equals("down")) addy = 1;
            if (selfplayer.getY() + addy < 0 || selfplayer.getY() + addy > 14) direction = d4;
            else {
                if (searchPath.isWall(selfplayer.getX(), selfplayer.getY() + addy, map))
                    direction = d4;
                if (searchPath.isSelf(selfplayer.getX(), selfplayer.getY() + addy, map))
                    direction = d4;
                if (selfplayer.getX() + 2 < 15 && searchPath.isBullet(selfplayer.getX() + 2, selfplayer.getY() + addy, map).equals("left")) {
                    direction = d4;
                }
                if (selfplayer.getX() + 2 < 15 && searchPath.isEnemy(selfplayer.getX() + 2, selfplayer.getY() + addy, map)) {
                    direction = d4;
                }
                if (selfplayer.getX() - 2 >= 0 && searchPath.isBullet(selfplayer.getX() - 2, selfplayer.getY() + addy, map).equals("rigth")) {
                    direction = d4;
                }
                if (selfplayer.getX() - 2 >= 0 && searchPath.isEnemy(selfplayer.getX() - 2, selfplayer.getY() - 1, map)) {
                    direction = d4;
                }
                if (selfplayer.getX() + 3 < 15 && searchPath.isBullet(selfplayer.getX() + 3, selfplayer.getY() + addy, map).equals("left")) {
                    direction = d4;
                }
                if (selfplayer.getX() - 3 >= 0 && searchPath.isBullet(selfplayer.getX() - 3, selfplayer.getY() + addy, map).equals("right")) {
                    direction = d4;
                }
            }
            if (direction == d4) {
                if (selfplayer.getY() - addy > 14 || selfplayer.getY() - addy < 0) direction = d1;
                else {
                    if (searchPath.isWall(selfplayer.getX(), selfplayer.getY() - addy, map))
                        direction = d1;
                    if (searchPath.isSelf(selfplayer.getX(), selfplayer.getY() - addy, map))
                        direction = d1;
                    if (selfplayer.getX() + 2 < 15 && searchPath.isBullet(selfplayer.getX() + 2, selfplayer.getY() - addy, map).equals("left")) {
                        direction = d1;
                    }
                    if (selfplayer.getX() + 2 < 15 && searchPath.isEnemy(selfplayer.getX() + 2, selfplayer.getY() - addy, map)) {
                        direction = d1;
                    }
                    if (selfplayer.getX() - 2 >= 0 && searchPath.isBullet(selfplayer.getX() - 2, selfplayer.getY() - addy, map).equals("right")) {
                        Bullet bullet = (Bullet) map[selfplayer.getX() - 2][selfplayer.getY() - addy];
                        if (bullet.getDirection().equals("right"))
                            direction = d1;
                    }
                    if (selfplayer.getX() - 2 >= 0 && searchPath.isEnemy(selfplayer.getX() - 2, selfplayer.getY() - addy, map)) {
                        direction = d1;
                    }
                    if (selfplayer.getX() + 3 < 15 && searchPath.isBullet(selfplayer.getX() + 3, selfplayer.getY() - addy, map).equals("left")) {
                        direction = d1;
                    }
                    if (selfplayer.getX() - 3 >= 0 && searchPath.isBullet(selfplayer.getX() - 3, selfplayer.getY() - addy, map).equals("right")) {
                        direction = d1;
                    }
                }
            }
        }
    }

    public void YBullet(List<Bullet> bullets, Player selfplayer, String d1, String d2, String d3, String d4, int index, Object[][] map) {
        if (bullets.get(index).getDirection().equals(d1)) {
            this.fire_direction = d2;
            SpanSearchPath searchPath = new SpanSearchPath();
            direction = d3;
            int addx = 0;
            if (d1.equals("left")) addx = -1;
            if (d1.equals("right")) addx = 1;
            if (selfplayer.getX() + addx > 14 || selfplayer.getX() + addx < 0) direction = d4;
            else {
                if (searchPath.isWall(selfplayer.getX() + addx, selfplayer.getY(), map) || searchPath.isSelf(selfplayer.getX() + addx, selfplayer.getY(), map))
                    direction = d4;
                if (searchPath.isSelf(selfplayer.getX() + addx, selfplayer.getY(), map) || searchPath.isSelf(selfplayer.getX() + addx, selfplayer.getY(), map))
                    direction = d4;
                if (selfplayer.getY() + 2 < 15 && searchPath.isBullet(selfplayer.getX() + addx, selfplayer.getY() + 2, map).equals("up")) {
                    direction = d4;
                }
                if (selfplayer.getY() + 2 < 15 && searchPath.isEnemy(selfplayer.getX() + addx, selfplayer.getY() + 2, map)) {
                    direction = d4;
                }
                if (selfplayer.getY() - 2 >= 0 && searchPath.isBullet(selfplayer.getX() + addx, selfplayer.getY() - 2, map).equals("down")) {
                    Bullet bullet = (Bullet) map[selfplayer.getX() + addx][selfplayer.getY() - 2];
                    if (bullet.getDirection().equals("down"))
                        direction = d4;
                }
                if (selfplayer.getY() - 2 >= 0 && searchPath.isEnemy(selfplayer.getX() + addx, selfplayer.getY() - 2, map)) {
                    direction = d4;
                }
                if (selfplayer.getY() + 3 < 15 && searchPath.isBullet(selfplayer.getX() + addx, selfplayer.getY() + 3, map).equals("up")) {
                    direction = d4;
                }
                if (selfplayer.getY() - 3 >= 0 && searchPath.isBullet(selfplayer.getX() + addx, selfplayer.getY() - 3, map).equals("down")) {
                    direction = d4;
                }
            }
            if ((direction == d4)) {
                if (selfplayer.getX() - addx < 0 || selfplayer.getX() - addx > 14) direction = d1;
                else {
                    if (searchPath.isWall(selfplayer.getX() - addx, selfplayer.getY(), map))
                        direction = d1;
                    if (searchPath.isSelf(selfplayer.getX() - addx, selfplayer.getY(), map))
                        direction = d1;
                    if (selfplayer.getY() + 2 < 15 && searchPath.isBullet(selfplayer.getX() - addx, selfplayer.getY() + 2, map).equals("up")) {
                        direction = d1;
                    }
                    if (selfplayer.getY() + 2 < 15 && searchPath.isEnemy(selfplayer.getX() - addx, selfplayer.getY() + 2, map)) {
                        direction = d1;
                    }
                    if (selfplayer.getY() - 2 >= 0 && searchPath.isBullet(selfplayer.getX() - addx, selfplayer.getY() - 2, map).equals("down")) {
                        direction = d1;
                    }
                    if (selfplayer.getY() - 2 >= 0 && searchPath.isEnemy(selfplayer.getX() - addx, selfplayer.getY() - 2, map)) {
                        direction = d1;
                    }
                    if (selfplayer.getY() + 3 < 15 && searchPath.isBullet(selfplayer.getX() - addx, selfplayer.getY() + 3, map).equals("up")) {

                        direction = d1;
                    }
                    if (selfplayer.getY() - 3 >= 0 && searchPath.isBullet(selfplayer.getX() - 1, selfplayer.getY() - 3, map).equals("down")) {
                        direction = d1;
                    }
                }
            }
        }
    }

    public void setFire_direction(Player selfplayer, Object[][] map) {
        int addx1 = 0;
        int addy1 = 0;
        int distence = 10;
        distence = Math.abs(selfplayer.getX() - enemyplayers_point.getX()) + Math.abs(selfplayer.getY() - enemyplayers_point.getY());
        if (direction.equals("up")) addy1 = -1;
        if (direction.equals("down")) addy1 = 1;
        if (direction.equals("right")) addx1 = 1;
        if (direction.equals("left")) addx1 = -1;
        if ((!(map[selfplayer.getX() + addx1][selfplayer.getY() + addy1] instanceof BrickWall))) {
            fire_direction = driectionXY(enemyplayers_point.getX(), enemyplayers_point.getY(), selfplayer.getX(), selfplayer.getY());
        }
    }

    public String driectionXY(int x, int y, int x0, int y0) {
        int des_x = x - x0;
        int des_y = y - y0;
        if (des_x - des_y >= 0 && des_x + des_y >= 0) return "right";
        if (des_x - des_y < 0 && des_x + des_y >= 0) return "down";
        if (des_x - des_y <= 0 && des_x + des_y < 0) return "left";
        if (des_x - des_y > 0 && des_x + des_y < 0) return "up";
        return null;
    }

    public void isSuperBullet(Player selfplayer, Object[][] map) {
        if (selfplayer.getSuperBullet() == 1 && fire_direction != null && !isSuperBullet_one) {
            int addx1 = 0;
            int addy1 = 0;
            if (fire_direction.equals("up")) addy1 = -1;
            if (fire_direction.equals("down")) addy1 = 1;
            if (fire_direction.equals("right")) addx1 = 1;
            if (fire_direction.equals("left")) addx1 = -1;
            if (selfplayer.getX() == enemyplayers_point.getX() && Math.abs(selfplayer.getY() - enemyplayers_point.getY()) < 3) {
                if (!(map[selfplayer.getX() + addx1][selfplayer.getY() + addy1] instanceof IronWall) && !(map[selfplayer.getX() + addx1][selfplayer.getY() + addy1] instanceof BrickWall)) {
                    this.isSuperBullet = 1;
                    return;
                }
            }
            if (selfplayer.getY() == enemyplayers_point.getY() && Math.abs(selfplayer.getX() - enemyplayers_point.getX()) < 3) {
                if (!(map[selfplayer.getX() + addx1][selfplayer.getY() + addy1] instanceof IronWall) && !(map[selfplayer.getX() + addx1][selfplayer.getY() + addy1] instanceof BrickWall)) {
                    this.isSuperBullet = 1;
                    return;
                }
            }

        }
        return;
    }

    public void dodgeEnemyNext(Player player, List<Bullet> bullets, Object[][] map) {
        int addx1 = 0;
        int addy1 = 0;
        SpanSearchPath searchPath = new SpanSearchPath();
        if (direction.equals("up")) {
            if (searchPath.isWall(player.getX(), player.getY() - 1, map) || searchPath.isSelf(player.getX(), player.getY() - 1, map)) {
                if (!hasMove("right")) direction = "right";
            } else {
                if (player.getX() + 2 < 15 &&
                        !(searchPath.isBrickWall(player.getX() + 1, player.getY() - 1, map) ||
                                searchPath.isIronWall(player.getX() + 1, player.getY() - 1, map) ||
                                searchPath.isSelfPlayer(player.getX() + 1, player.getY() - 1, map)))
                    if (searchPath.isEnemy(player.getX() + 2, player.getY() - 1, map) || searchPath.isBullet(player.getX() + 2, player.getY() - 1, map).equals("left")) {
                        if (!searchPath.isWall(player.getX() + 1, player.getY(), map) && !searchPath.isSelf(player.getX() + 1, player.getY(), map)) {
                            if (!hasMove("right")) direction = "right";
                        } else if (!searchPath.isWall(player.getX() - 1, player.getY(), map) && !searchPath.isSelf(player.getX() - 1, player.getY(), map)) {
                            if (!hasMove("left")) direction = "left";
                        } else direction = "down";
                    }

                if (player.getX() - 2 >= 0 && !(searchPath.isBrickWall(player.getX() - 1, player.getY() - 1, map) || searchPath.isIronWall(player.getX() - 1, player.getY() - 1, map) || searchPath.isSelfPlayer(player.getX() - 1, player.getY() - 1, map)))
                    if (searchPath.isEnemy(player.getX() - 2, player.getY() - 1, map) || searchPath.isBullet(player.getX() - 2, player.getY() - 1, map).equals("right")) {
                        if (!searchPath.isWall(player.getX() - 1, player.getY(), map) && !searchPath.isSelf(player.getX() - 1, player.getY(), map)) {
                            if (!hasMove("right")) direction = "right";
                        } else if (!searchPath.isWall(player.getX() + 1, player.getY(), map) && !searchPath.isSelf(player.getX() + 1, player.getY(), map)) {
                            if (!hasMove("left")) direction = "left";
                        } else direction = "down";
                    }
                if (player.getX() + 3 < 15 && !(searchPath.isBrickWall(player.getX() + 1, player.getY() - 1, map) || searchPath.isIronWall(player.getX() + 1, player.getY() - 1, map) || searchPath.isIronWall(player.getX() + 2, player.getY() - 1, map) || searchPath.isSelfPlayer(player.getX() + 1, player.getY() - 1, map)))
                    if (searchPath.isEnemy(player.getX() + 3, player.getY() - 1, map) || searchPath.isBullet(player.getX() + 3, player.getY() - 1, map).equals("left")) {
                        if (!searchPath.isWall(player.getX() + 1, player.getY(), map) && !searchPath.isSelf(player.getX() + 1, player.getY(), map)) {
                            if (!hasMove("right")) direction = "right";
                        } else if (!searchPath.isWall(player.getX() - 1, player.getY(), map) && !searchPath.isSelf(player.getX() - 1, player.getY(), map)) {
                            if (!hasMove("left")) direction = "left";
                        } else direction = "down";
                    }

                if (player.getX() - 3 >= 0 && !(searchPath.isBrickWall(player.getX() - 1, player.getY() - 1, map) || searchPath.isIronWall(player.getX() - 1, player.getY() - 1, map) || searchPath.isIronWall(player.getX() - 2, player.getY() - 1, map) || searchPath.isSelfPlayer(player.getX() - 1, player.getY() - 1, map)))
                    if (searchPath.isEnemy(player.getX() - 3, player.getY() - 1, map) || searchPath.isBullet(player.getX() - 3, player.getY() - 1, map).equals("right")) {
                        if (!searchPath.isWall(player.getX() - 1, player.getY(), map) && !searchPath.isSelf(player.getX() - 1, player.getY(), map)) {
                            if (!hasMove("right")) direction = "right";
                        } else if (!searchPath.isWall(player.getX() + 1, player.getY(), map) && !searchPath.isSelf(player.getX() + 1, player.getY(), map)) {
                            if (!hasMove("left")) direction = "left";
                        } else direction = "down";
                    }
                if (player.getY() - 4 >= 0 && searchPath.isBrickWall(player.getX(), player.getY() - 1, map))
                    if (searchPath.isEnemy(player.getX(), player.getY() - 4, map) || (searchPath.isBullet(player.getX(), player.getY() - 4, map).equals("down"))) {
                        if (!searchPath.isWall(player.getX() - 1, player.getY(), map) && !searchPath.isSelf(player.getX() - 1, player.getY(), map)) {
                            if (!hasMove("right")) direction = "right";
                        } else if (!searchPath.isWall(player.getX() + 1, player.getY(), map) && !searchPath.isSelf(player.getX() + 1, player.getY(), map)) {
                            if (!hasMove("left")) direction = "left";
                        } else direction = "down";
                    }
                if (player.getY() - 4 >= 0 && !(fire_direction == null || fire_direction == "down"))
                    if (searchPath.isEnemy(player.getX(), player.getY() - 4, map) || (searchPath.isBullet(player.getX(), player.getY() - 4, map).equals("down"))) {
                        if (!searchPath.isWall(player.getX() - 1, player.getY(), map) && !searchPath.isSelf(player.getX() - 1, player.getY(), map)) {
                            if (!hasMove("right")) direction = "right";
                        } else if (!searchPath.isWall(player.getX() + 1, player.getY(), map) && !searchPath.isSelf(player.getX() + 1, player.getY(), map)) {
                            if (!hasMove("left")) direction = "left";
                        } else direction = "down";
                    }
            }
        } else if (direction.equals("down")) {
            if (searchPath.isWall(player.getX(), player.getY() + 1, map) || searchPath.isSelf(player.getX(), player.getY() - 1, map)) {
                if (!hasMove("left")) direction = "left";
            } else {
                if (player.getX() + 2 < 15 && !(searchPath.isBrickWall(player.getX() + 1, player.getY() + 1, map) || searchPath.isIronWall(player.getX() + 1, player.getY() + 1, map) || searchPath.isSelfPlayer(player.getX() + 1, player.getY() + 1, map)))
                    if (searchPath.isEnemy(player.getX() + 2, player.getY() + 1, map) || searchPath.isBullet(player.getX() + 2, player.getY() + 1, map).equals("left")) {
                        if (!searchPath.isWall(player.getX() + 1, player.getY(), map) && !searchPath.isSelf(player.getX() + 1, player.getY(), map)) {
                            if (!hasMove("left")) direction = "left";
                        } else if (!searchPath.isWall(player.getX() - 1, player.getY(), map) && !searchPath.isSelf(player.getX() - 1, player.getY(), map)) {
                            if (!hasMove("right")) direction = "right";
                        } else direction = "up";
                    }
                if (player.getX() - 2 >= 0 && !(searchPath.isBrickWall(player.getX() - 1, player.getY() + 1, map) || searchPath.isIronWall(player.getX() - 1, player.getY() + 1, map) || searchPath.isSelfPlayer(player.getX() - 1, player.getY() + 1, map)))
                    if (searchPath.isEnemy(player.getX() - 2, player.getY() + 1, map) || searchPath.isBullet(player.getX() - 2, player.getY() + 1, map).equals("right")) {
                        if (!searchPath.isWall(player.getX() - 1, player.getY(), map) && !searchPath.isSelf(player.getX() - 1, player.getY(), map)) {
                            if (!hasMove("left")) direction = "left";
                        } else if (!searchPath.isWall(player.getX() + 1, player.getY(), map) && !searchPath.isSelf(player.getX() + 1, player.getY(), map)) {
                            if (!hasMove("right")) direction = "right";
                        } else direction = "up";
                    }
                if (player.getX() + 3 < 15 && !(searchPath.isBrickWall(player.getX() + 1, player.getY() + 1, map) || searchPath.isIronWall(player.getX() + 1, player.getY() + 1, map) || searchPath.isIronWall(player.getX() + 2, player.getY() + 1, map) || searchPath.isSelfPlayer(player.getX() + 1, player.getY() + 1, map)))
                    if (searchPath.isEnemy(player.getX() + 3, player.getY() + 1, map) || searchPath.isBullet(player.getX() + 3, player.getY() + 1, map).equals("left")) {
                        if (!searchPath.isWall(player.getX() + 1, player.getY(), map) && !searchPath.isSelf(player.getX() + 1, player.getY(), map)) {
                            if (!hasMove("left")) direction = "left";
                        } else if (!searchPath.isWall(player.getX() - 1, player.getY(), map) && !searchPath.isSelf(player.getX() - 1, player.getY(), map)) {
                            if (!hasMove("right")) direction = "right";
                        } else direction = "up";
                    }
                if (player.getX() - 3 >= 0 && !(searchPath.isBrickWall(player.getX() - 1, player.getY() + 1, map) || searchPath.isIronWall(player.getX() - 1, player.getY() + 1, map) || searchPath.isIronWall(player.getX() - 2, player.getY() + 1, map) || searchPath.isSelfPlayer(player.getX() - 1, player.getY() + 1, map)))
                    if (searchPath.isEnemy(player.getX() - 3, player.getY() + 1, map) || searchPath.isBullet(player.getX() - 3, player.getY() + 1, map).equals("right")) {
                        if (!searchPath.isWall(player.getX() - 1, player.getY(), map) && !searchPath.isSelf(player.getX() - 1, player.getY(), map)) {
                            if (!hasMove("left")) direction = "left";
                        } else if (!searchPath.isWall(player.getX() + 1, player.getY(), map) && !searchPath.isWall(player.getX() + 1, player.getY(), map)) {
                            if (!hasMove("right")) direction = "right";
                        } else direction = "up";
                    }
                if (player.getY() + 4 < 15 && searchPath.isBrickWall(player.getX(), player.getY() + 1, map))
                    if (searchPath.isEnemy(player.getX(), player.getY() + 4, map) || searchPath.isBullet(player.getX(), player.getY() + 4, map).equals("up")) {
                        if (!searchPath.isWall(player.getX() - 1, player.getY(), map) && !searchPath.isSelf(player.getX() + 1, player.getY(), map)) {
                            if (!hasMove("left")) direction = "left";
                        } else if (!searchPath.isWall(player.getX() + 1, player.getY(), map) && !searchPath.isSelf(player.getX() + 1, player.getY(), map)) {
                            if (!hasMove("right")) direction = "right";
                        } else direction = "up";
                    }
                if (player.getY() + 4 < 15 && !(fire_direction == null || fire_direction == "up"))
                    if (searchPath.isEnemy(player.getX(), player.getY() + 4, map) || searchPath.isBullet(player.getX(), player.getY() + 4, map).equals("up")) {
                        if (!searchPath.isWall(player.getX() - 1, player.getY(), map) && !searchPath.isSelf(player.getX() + 1, player.getY(), map)) {
                            if (!hasMove("left")) direction = "left";
                        } else if (!searchPath.isWall(player.getX() + 1, player.getY(), map) && !searchPath.isSelf(player.getX() + 1, player.getY(), map)) {
                            if (!hasMove("right")) direction = "right";
                        } else direction = "up";
                    }
            }
        } else if (direction.equals("right")) {
            if (searchPath.isWall(player.getX() + 1, player.getY(), map) || searchPath.isSelf(player.getX(), player.getY() - 1, map)) {
                if (!hasMove("down")) direction = "down";
            } else {
                if (player.getY() + 2 < 15 && !(searchPath.isBrickWall(player.getX() + 1, player.getY() + 1, map) || searchPath.isIronWall(player.getX() + 1, player.getY() + 1, map) || searchPath.isSelfPlayer(player.getX() + 1, player.getY() + 1, map)))
                    if (searchPath.isEnemy(player.getX() + 1, player.getY() + 2, map) || searchPath.isBullet(player.getX() + 1, player.getY() + 2, map).equals("up")) {
                        if (!searchPath.isWall(player.getX(), player.getY() + 1, map) && !searchPath.isSelf(player.getX(), player.getY() + 1, map)) {
                            if (!hasMove("down")) direction = "down";
                        } else if (!searchPath.isWall(player.getX(), player.getY() - 1, map) && !searchPath.isSelf(player.getX(), player.getY() - 1, map)) {
                            if (!hasMove("up")) direction = "up";
                        } else direction = "left";
                    }
                if (player.getY() - 2 >= 0 && !(searchPath.isBrickWall(player.getX() + 1, player.getY() - 1, map) || searchPath.isIronWall(player.getX() + 1, player.getY() - 1, map) || searchPath.isSelfPlayer(player.getX() + 1, player.getY() - 1, map)))
                    if (searchPath.isEnemy(player.getX() + 1, player.getY() - 2, map) || searchPath.isBullet(player.getX() + 1, player.getY() - 2, map).equals("down")) {
                        if (!searchPath.isWall(player.getX(), player.getY() + 1, map) && !searchPath.isSelf(player.getX(), player.getY() + 1, map)) {
                            if (!hasMove("down")) direction = "down";
                        } else if (!searchPath.isWall(player.getX(), player.getY() - 1, map) && !searchPath.isWall(player.getX(), player.getY() - 1, map)) {
                            if (!hasMove("up")) direction = "up";
                        } else direction = "left";
                    }
                if (player.getY() + 3 < 15 && !(searchPath.isBrickWall(player.getX() + 1, player.getY() + 1, map) || searchPath.isIronWall(player.getX() + 1, player.getY() + 1, map) || searchPath.isIronWall(player.getX() + 1, player.getY() + 2, map) || searchPath.isSelfPlayer(player.getX() + 1, player.getY() + 1, map)))
                    if (searchPath.isEnemy(player.getX() + 1, player.getY() + 3, map) || searchPath.isBullet(player.getX() + 1, player.getY() + 3, map).equals("up")) {
                        if (!searchPath.isWall(player.getX(), player.getY() + 1, map) && !searchPath.isSelf(player.getX(), player.getY() + 1, map)) {
                            if (!hasMove("down")) direction = "down";
                        } else if (!searchPath.isWall(player.getX(), player.getY() - 1, map) && !searchPath.isSelf(player.getX(), player.getY() - 1, map)) {
                            if (!hasMove("up")) direction = "up";
                        } else direction = "left";
                    }
                if (player.getY() - 3 >= 0 && !(searchPath.isBrickWall(player.getX() + 1, player.getY() - 1, map) || searchPath.isIronWall(player.getX() + 1, player.getY() - 1, map) || searchPath.isIronWall(player.getX() + 1, player.getY() - 2, map) || searchPath.isSelfPlayer(player.getX() + 1, player.getY() - 1, map)))
                    if (searchPath.isEnemy(player.getX() + 1, player.getY() - 3, map) || searchPath.isBullet(player.getX() + 1, player.getY() - 3, map).equals("down")) {
                        if (!searchPath.isWall(player.getX(), player.getY() + 1, map) && !searchPath.isSelf(player.getX(), player.getY() + 1, map)) {
                            if (!hasMove("down")) direction = "down";
                        } else if (!searchPath.isWall(player.getX(), player.getY() - 1, map) && !searchPath.isWall(player.getX(), player.getY() - 1, map)) {
                            if (!hasMove("up")) direction = "up";
                        } else direction = "left";
                    }
                if (player.getX() + 4 < 15 && !(fire_direction == null || fire_direction == "left"))
                    if (searchPath.isEnemy(player.getX() + 4, player.getY(), map) || searchPath.isBullet(player.getX() + 4, player.getY(), map).equals("left")) {
                        if (!searchPath.isWall(player.getX(), player.getY() + 1, map) && !searchPath.isSelf(player.getX(), player.getY() + 1, map)) {
                            if (!hasMove("down")) direction = "down";
                        } else if (!searchPath.isWall(player.getX(), player.getY() - 1, map) && !searchPath.isSelf(player.getX(), player.getY() - 1, map)) {
                            if (!hasMove("up")) direction = "up";
                        } else direction = "left";
                    }
                if (player.getX() + 4 < 15 && searchPath.isBrickWall(player.getX() + 1, player.getY(), map))
                    if (searchPath.isEnemy(player.getX() + 4, player.getY(), map) || searchPath.isBullet(player.getX() + 4, player.getY(), map).equals("left")) {
                        if (!searchPath.isWall(player.getX(), player.getY() + 1, map) && !searchPath.isSelf(player.getX(), player.getY() + 1, map)) {
                            if (!hasMove("down")) direction = "down";
                        } else if (!searchPath.isWall(player.getX(), player.getY() - 1, map) && !searchPath.isSelf(player.getX(), player.getY() - 1, map)) {
                            if (!hasMove("up")) direction = "up";
                        } else direction = "left";
                    }
            }
        } else if (direction.equals("left")) {
            if (searchPath.isWall(player.getX() - 1, player.getY(), map) || searchPath.isSelf(player.getX() - 1, player.getY(), map)) {
                if (!hasMove("up")) direction = "up";
            } else {
                if (player.getY() + 2 < 15 && !(searchPath.isBrickWall(player.getX() - 1, player.getY() + 1, map) || searchPath.isIronWall(player.getX() - 1, player.getY() + 1, map) || searchPath.isSelfPlayer(player.getX() - 1, player.getY() + 1, map))) {
                    if (searchPath.isEnemy(player.getX() - 1, player.getY() + 2, map) || searchPath.isBullet(player.getX() - 1, player.getY() + 2, map).equals("up")) {
                        if (!searchPath.isWall(player.getX(), player.getY() - 1, map) || !searchPath.isSelf(player.getX(), player.getY() - 1, map)) {
                            if (!hasMove("up")) direction = "up";
                        } else if (!searchPath.isWall(player.getX(), player.getY() + 1, map) || !searchPath.isSelf(player.getX(), player.getY() + 1, map)) {
                            if (!hasMove("down")) direction = "down";
                        } else direction = "right";
                    }
                }
                if (player.getY() - 2 >= 0 && !(searchPath.isBrickWall(player.getX() - 1, player.getY() - 1, map) || searchPath.isIronWall(player.getX() - 1, player.getY() - 1, map) || searchPath.isSelfPlayer(player.getX() - 1, player.getY() - 1, map)))
                    if (searchPath.isEnemy(player.getX() - 1, player.getY() - 2, map) || searchPath.isBullet(player.getX() - 1, player.getY() - 2, map).equals("down")) {
                        if (!searchPath.isWall(player.getX(), player.getY() - 1, map) || !searchPath.isSelf(player.getX(), player.getY() - 1, map)) {
                            if (!hasMove("up")) direction = "up";
                        } else if (!searchPath.isWall(player.getX(), player.getY() + 1, map) || !searchPath.isSelf(player.getX(), player.getY() + 1, map)) {
                            if (!hasMove("down")) direction = "down";
                        } else direction = "right";
                    }
                if (player.getY() + 3 < 15 && !(searchPath.isBrickWall(player.getX() - 1, player.getY() + 1, map) || searchPath.isIronWall(player.getX() - 1, player.getY() + 1, map) || searchPath.isIronWall(player.getX() - 1, player.getY() + 2, map) || searchPath.isSelfPlayer(player.getX() - 1, player.getY() + 1, map))) {
                    if (searchPath.isEnemy(player.getX() - 1, player.getY() + 3, map) || searchPath.isBullet(player.getX() - 1, player.getY() + 3, map).equals("up")) {
                        if (!searchPath.isWall(player.getX(), player.getY() - 1, map) || !searchPath.isSelf(player.getX(), player.getY() - 1, map)) {
                            if (!hasMove("up")) direction = "up";
                        } else if (!searchPath.isWall(player.getX(), player.getY() + 1, map) || !searchPath.isSelf(player.getX(), player.getY() + 1, map)) {
                            if (!hasMove("down")) direction = "down";
                        } else direction = "right";
                    }
                }
                if (player.getY() - 3 >= 0 && !(searchPath.isBrickWall(player.getX() - 1, player.getY() - 1, map) || searchPath.isIronWall(player.getX() - 1, player.getY() - 1, map) || searchPath.isIronWall(player.getX() - 1, player.getY() - 2, map) || searchPath.isSelfPlayer(player.getX() - 1, player.getY() - 1, map)))
                    if (searchPath.isEnemy(player.getX() - 1, player.getY() - 3, map) || searchPath.isBullet(player.getX() - 1, player.getY() - 3, map).equals("down")) {
                        if (!searchPath.isWall(player.getX(), player.getY() - 1, map) || !searchPath.isSelf(player.getX(), player.getY() - 1, map)) {
                            if (!hasMove("up")) direction = "up";
                        } else if (!searchPath.isWall(player.getX(), player.getY() + 1, map) || !searchPath.isSelf(player.getX(), player.getY() + 1, map)) {
                            if (!hasMove("down")) direction = "down";
                        } else direction = "right";
                    }
                if (player.getY() - 4 >= 0 && searchPath.isBrickWall(player.getX() - 1, player.getY(), map))
                    if (searchPath.isEnemy(player.getX() - 4, player.getY(), map) && searchPath.isBullet(player.getX() - 4, player.getY(), map).equals("right")) {
                        if (!searchPath.isWall(player.getX(), player.getY() - 1, map) || !searchPath.isSelf(player.getX(), player.getY() - 1, map)) {
                            if (!hasMove("up")) direction = "up";
                        } else if (!searchPath.isWall(player.getX(), player.getY() + 1, map) || !searchPath.isSelf(player.getX(), player.getY() + 1, map)) {
                            if (!hasMove("down")) direction = "down";
                        } else direction = "right";
                    }
                if (player.getY() - 4 >= 0 && !(fire_direction == null || fire_direction == "right"))
                    if (searchPath.isEnemy(player.getX() - 4, player.getY(), map) && searchPath.isBullet(player.getX() - 4, player.getY(), map).equals("right")) {
                        if (!searchPath.isWall(player.getX(), player.getY() - 1, map) || !searchPath.isSelf(player.getX(), player.getY() - 1, map)) {
                            if (!hasMove("up")) direction = "up";
                        } else if (!searchPath.isWall(player.getX(), player.getY() + 1, map) || !searchPath.isSelf(player.getX(), player.getY() + 1, map)) {
                            if (!hasMove("down")) direction = "down";
                        } else direction = "right";
                    }
            }
        }
        if (!has_directions.isEmpty()) {
            if (has_directions.get(has_directions.size() - 1).equals(direction) || has_directions.size() > 4)
                return;
        }
        has_directions.add(direction);
        dodgeEnemyNext(player, bullets, map);
    }

    public void isNoMove(Player player, Player last_player, List<Bullet> bullets, Object[][] map) {
        if (player.getX() == last_player.getX() && last_player.getY() == player.getY()) {
            int addx1 = 0;
            int addy1 = 0;
            SpanSearchPath searchPath = new SpanSearchPath();
            if (direction.equals("up")) {
                if (!searchPath.isWall(player.getX() + 1, player.getY(), map) && !searchPath.isSelf(player.getX() + 1, player.getY(), map))
                    direction = "right";
                else if (!searchPath.isWall(player.getX() - 1, player.getY(), map) && !searchPath.isSelf(player.getX() - 1, player.getY(), map))
                    direction = "left";
                else direction = "down";
            } else if (direction.equals("down")) {
                if (!searchPath.isWall(player.getX() + 1, player.getY(), map) && !searchPath.isSelf(player.getX() + 1, player.getY(), map))
                    direction = "right";
                else if (!searchPath.isWall(player.getX() - 1, player.getY(), map) && !searchPath.isSelf(player.getX() - 1, player.getY(), map))
                    direction = "left";
                else direction = "up";
            } else if (direction.equals("right")) {
                if (!searchPath.isWall(player.getX(), player.getY() - 1, map) && !searchPath.isSelf(player.getX(), player.getY() - 1, map))
                    direction = "up";
                else if (!searchPath.isWall(player.getX(), player.getY() + 1, map) && !searchPath.isSelf(player.getX(), player.getY() + 1, map))
                    direction = "down";
                else direction = "left";
            } else if (direction.equals("left")) {
                if (!searchPath.isWall(player.getX(), player.getY() - 1, map) && !searchPath.isSelf(player.getX(), player.getY() - 1, map))
                    direction = "up";
                else if (!searchPath.isWall(player.getX(), player.getY() + 1, map) && !searchPath.isSelf(player.getX(), player.getY() + 1, map))
                    direction = "down";
                else direction = "right";
            }
        }
    }

    public boolean hasMove(String direction) {
        for (String string : has_directions) {
            if (string.equals(direction)) return true;
        }
        return false;
    }
    public boolean hasMove(String direction,List<String> has_directions) {
        for (String string : has_directions) {
            if (string.equals(direction)) return true;
        }
        return false;
    }

    public int getIsSuperBullet() {
        return this.isSuperBullet;
    }

    public boolean isOnly_one() {
        return only_one;
    }
}
