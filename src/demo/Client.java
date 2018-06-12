package demo;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import map.*;

import java.io.*;
import java.util.*;

import cmd.Action;
import cmd.RoundAction;
import org.omg.CORBA.Context;
import org.omg.CORBA.Environment;
import sun.rmi.runtime.Log;

public class Client {
    private int team_id = 0;
    private String team_name = "";
    public static Team self = null;
    public static Team enemy = null;
    public static int  self_size = 0;
    private int roundId = 0;
    private final String UP = "up";
    private final String DOWN = "down";
    private final String LEFT = "left";
    private final String RIGHT = "right";
    private final int NOM_BULLET = 0;
    private final int SUP_BULLET = 1;
    private int width = 1;
    private int height = 1;
    private List<Player> selfplayers = new ArrayList<Player>();
    private List<Player> lastselfplayers = new ArrayList<Player>();
    private List<Player> enemyplayers = new ArrayList<Player>();
    private List<Player> enemytarget = new ArrayList<Player>();
    private List<Star> Stars = new ArrayList<Star>();
    private List<Coin> Coins = new ArrayList<Coin>();
    private List<Bullet> Bullets = new ArrayList<Bullet>();
    private Object[][] map;
    public Client(int team_id, String team_name) {
        this.team_id = team_id;
        this.team_name = team_name;
    }

    public void legStart(JSONObject data) {
        System.out.println("leg start");

        JSONObject map = data.getJSONObject("map");

        this.width = map.getInt("width");
        this.height = map.getInt("height");
        this.map = new Object[height][width];
        for (int i = 0; i < height; ++i)
            for (int j = 0; j < width; ++j)
                this.map[i][j] = new String("");
        System.out.printf("map width:%d, map height %d\n", width, height);

        JSONArray teams = data.getJSONArray("teams");
        for (int i = 0; i < 2; i++) {
            JSONObject team = teams.getJSONObject(i);
            int team_id = team.getInt("id");
            if (this.team_id == team_id) {
                System.out.println("self team");
                this.self = new Team(team);
            } else {
                System.out.println("enemy team");
                this.enemy = new Team(team);
            }
        }
    }

    public void legEnd(JSONObject data) {
        System.out.println("leg end");

        JSONArray results = data.getJSONArray("teams");
        for (int i = 0; i < results.size(); i++) {
            Result result = new Result(results.getJSONObject(i));
        }
    }

    public void round(JSONObject data) {
        Object[][] map = new Object[this.width][this.height];
        this.roundId = data.getInt("round_id");
        System.out.printf("round %d\n", this.roundId);
        this.Bullets.clear();
        JSONArray bullets = data.getJSONArray("bullets");
        for (int i = 0; i < bullets.size(); i++) {
            JSONObject object = bullets.getJSONObject(i);
            Bullet bullet = new Bullet(object);
            map[bullet.getX()][bullet.getY()] = bullet;
            if (bullet.getTeam() != self.getId())
                Bullets.add(bullet);
        }
        JSONArray brickWalls = data.getJSONArray("brick_walls");
        for (int i = 0; i < brickWalls.size(); i++) {
            JSONObject object = brickWalls.getJSONObject(i);
            BrickWall wall = new BrickWall(object);
            map[wall.getX()][wall.getY()] = wall;
        }
        JSONArray ironWalls = data.getJSONArray("iron_walls");
        for (int i = 0; i < ironWalls.size(); i++) {
            JSONObject object = ironWalls.getJSONObject(i);
            IronWall wall = new IronWall(object);
            map[wall.getX()][wall.getY()] = wall;
        }
        JSONArray rivers = data.getJSONArray("river");
        for (int i = 0; i < rivers.size(); i++) {
            JSONObject object = rivers.getJSONObject(i);
            River river = new River(object);
            map[river.getX()][river.getY()] = river;
        }
        this.Coins.clear();
        JSONArray coins = data.getJSONArray("coins");
        for (int i = 0; i < coins.size(); i++) {
            JSONObject object = coins.getJSONObject(i);
            Coin coin = new Coin(object);
            map[coin.getX()][coin.getY()] = coin;
            Coins.add(coin);
        }
        this.Stars.clear();
        JSONArray stars = data.getJSONArray("stars");
        for (int i = 0; i < stars.size(); i++) {
            JSONObject object = stars.getJSONObject(i);
            Star star = new Star(object);
            map[star.getX()][star.getY()] = star;
            Stars.add(star);
        }
        this.selfplayers.clear();
        this.enemyplayers.clear();
        JSONArray players = data.getJSONArray("players");
        for (int i = 0; i < players.size(); i++) {
            JSONObject object = players.getJSONObject(i);
            Player player = new Player(object);
            if (player.getTeam() == this.self.getId()) {
                this.selfplayers.add(player);
                map[player.getX()][player.getY()] = player;
            } else {
                this.enemyplayers.add(player);
                map[player.getX()][player.getY()] = player;
            }
        }

        JSONArray scores = data.getJSONArray("teams");
        for (int i = 0; i < scores.size(); i++) {
            JSONObject object = scores.getJSONObject(i);
            Score Score = new Score(object);
        }
        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                this.map[i][j] = map[i][j];
            }
        }

    }

    public void targetEnemy() {
        this.enemytarget = this.enemyplayers;
        if (enemytarget.size() == 4) {
            enemytarget.remove(3);
            enemytarget.remove(2);
        }

    }

    public RoundAction act() {
        List<Action> actions = new ArrayList<Action>();
        List<Player> players = this.selfplayers;
        self_size = this.selfplayers.size();
        for (int i = 0; i < selfplayers.size(); i++) {
            AiTank aiTank = new AiTank();
            int TankID = selfplayers.get(i).getId();
            String fire_driection = null;
            int super_bullet = 0;
            if(roundId==4&&i==0){
                int g= 0;
            }
            String driection = aiTank.getDirection(this.selfplayers.get(i), this.enemyplayers, this.Coins, this.Stars, this.Bullets, this.map, this.roundId);
            //aiTank.dodgeEnemy(this.selfplayers.get(i), this.enemyplayers, this.map);
            //aiTank.dodgeEnemyNext(this.selfplayers.get(i), this.Bullets, this.map);
            if(!lastselfplayers.isEmpty()) {
                if(!aiTank.isOnly_one()){
                    aiTank.isNoMove(selfplayers.get(i), lastselfplayers.get(i), Bullets, map);
                    lastselfplayers.set(i, selfplayers.get(i));
                }
                else {
                    int distence = 10;
                    distence = Math.abs(selfplayers.get(i).getX() - enemyplayers.get(0).getX()) + Math.abs(selfplayers.get(i).getY() - enemyplayers.get(0).getY());
                    if (distence != 1) {
                        aiTank.isNoMove(selfplayers.get(i), lastselfplayers.get(i), Bullets, map);
                        lastselfplayers.set(i, selfplayers.get(i));
                    }
                }
            }

            //aiTank.dodgeEnemyNext(this.selfplayers.get(i), this.Bullets, this.map);
            aiTank.dodgeBullet(this.selfplayers.get(i), this.Bullets, this.map);
            aiTank.dodgeEnemy(this.selfplayers.get(i), this.enemyplayers, this.map);
            aiTank.dodgeEnemyNext(this.selfplayers.get(i), this.Bullets, this.map);
            driection = aiTank.getDirection();
            fire_driection = aiTank.getFire_direction();
            aiTank.isSuperBullet(this.selfplayers.get(i),map);
            super_bullet = aiTank.getIsSuperBullet();
            if (fire_driection == null) fire_driection = driection;
            if (driection != null && fire_driection != null) {
                actions.add(new Action(players.get(0).getTeam(), players.get(i).getId(), super_bullet, driection, fire_driection));
            }
            System.out.println("tankID:" + selfplayers.get(i).getId() +
                    "tankpostie:" + selfplayers.get(i).getX() + "  " + selfplayers.get(i).getY() +
                    "roingid:" + roundId + " " + "driection:" + driection + "  " +
                    "fire_driection:" + fire_driection+" "+"super:"+super_bullet);
        }
        if(lastselfplayers.isEmpty()){
            lastselfplayers = new ArrayList<>(selfplayers);
        }
        RoundAction roundAction = new RoundAction(this.roundId, actions);
        return roundAction;
    }
}
