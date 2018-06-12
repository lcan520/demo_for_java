package demo;

import map.*;

import java.util.*;

public class GetMinPath {
    private List<String> directions = new ArrayList<String>();
    private Player selfplayer = null;
    private List<Player> enemyplayers = new ArrayList<Player>();
    private Player enemyplayers_point = new Player();
    private List<Star> stars = new ArrayList<Star>();
    private List<Coin> coins = new ArrayList<Coin>();
    private List<Bullet> bullets = new ArrayList<Bullet>();
    private Object[][] map;

    public GetMinPath(Player selfplayer, List<Player> enemyplayers, List<Coin> Coins, List<Star> stars, List<Bullet> bullets, Object[][] map) {
        this.selfplayer = selfplayer;
        this.enemyplayers = enemyplayers;
        this.stars = stars;
        this.coins = Coins;
        this.bullets = bullets;
        this.map = map;
    }
    public GetMinPath(){}

    /*
     *
     *
     * @author lizhilong
     * @date 2018/5/29 1:02
     * @param [players, map, coin]
     * @return java.util.List<java.lang.String>
     * @description
     * 双线程遍历最短路径
     */
    public List<String> getMinPath(int start_x, int start_y, int to_x, int to_y, Object[][] map) {
        List<List<String>> all_directionts = new ArrayList<List<String>>();
        List<String> directions = new ArrayList<String>();
            DepthSearchPath depht_path = new DepthSearchPath();
            depht_path.Move(to_x , to_y , start_x, start_y, this.map);
            directions = depht_path.getDirection();
            all_directionts.add(directions);
            SpanSearchPath span_path = new SpanSearchPath();
            span_path.Move(to_x , to_y , start_x, start_y, this.map);
            directions = span_path.getDirection();
            all_directionts.add(directions);
            SearchPath left_path = new LeftSearchPath();
            span_path.Move(to_x , to_y , start_x, start_y, this.map);
            directions = span_path.getDirection();
            all_directionts.add(directions);
            SearchPath up_path = new UpSearchPath();
            span_path.Move(to_x , to_y , start_x, start_y, this.map);
            directions = span_path.getDirection();
            all_directionts.add(directions);
        if (all_directionts.isEmpty()) {
            System.out.println("所有路径不可达！");
            return new ArrayList<>();
        }
        Collections.sort(all_directionts, new Comparator<List<String>>() {
            @Override
            public int compare(List<String> o1, List<String> o2) {
                return o1.size() - o2.size();
            }
        });
        for (String s : all_directionts.get(0))
            System.out.println("最短路径方向" + s);
        return all_directionts.get(0);
    }

    /*
     *
     *
     * @author lizhilong
     * @date 2018/5/28 20:52
     * @param [players, map, coin]
     * @return java.util.List<java.lang.String>
     * @description
     * 计算金钱的最短路径
     */
    public List<String> getCoinMinPath(boolean only_one) {
        List<List<String>> all_directionts = new ArrayList<List<String>>();
        List<String> directions = new ArrayList<String>();
        for (int j = 0; j < this.coins.size(); j++) {
            DepthSearchPath depht_path = new DepthSearchPath();
            depht_path.Move(this.selfplayer.getX(), this.selfplayer.getY(), this.coins.get(j).getX(), this.coins.get(j).getY(), this.map);
            directions = depht_path.getDirection();
            if (directions.size() == 0) continue;
            all_directionts.add(directions);
        }
        for (int j = 0; j < this.coins.size(); j++) {
            SpanSearchPath span_path = new SpanSearchPath();
            span_path.Move(this.selfplayer.getX(), this.selfplayer.getY(), this.coins.get(j).getX(), this.coins.get(j).getY(), this.map);
            directions = span_path.getDirection();
            if (directions.size() == 0) continue;
            all_directionts.add(directions);
        }
        for (int j = 0; j < this.coins.size(); j++) {
            SearchPath span_path = new LeftSearchPath();
            span_path.Move(this.selfplayer.getX(), this.selfplayer.getY(), this.coins.get(j).getX(), this.coins.get(j).getY(), this.map);
            directions = span_path.getDirection();
            if (directions.size() == 0) continue;
            all_directionts.add(directions);
        }
        for (int j = 0; j < this.coins.size(); j++) {
            SearchPath span_path = new UpSearchPath();
            span_path.Move(this.selfplayer.getX(), this.selfplayer.getY(), this.coins.get(j).getX(), this.coins.get(j).getY(), this.map);
            directions = span_path.getDirection();
            if (directions.size() == 0) continue;
            all_directionts.add(directions);
        }
        if (all_directionts.isEmpty()) {
            System.out.println("所有路径不可达！");
            return new ArrayList<>();
        }
        Collections.sort(all_directionts, new Comparator<List<String>>() {
            @Override
            public int compare(List<String> o1, List<String> o2) {
                return o1.size() - o2.size();
            }
        });
        for (String s : all_directionts.get(0))
            System.out.println("最短路径方向" + s);
        return all_directionts.get(0);
    }


    /*
     *
     *
     * @author lizhilong
     * @date 2018/5/28 20:52
     * @param [players, map, stars]
     * @return java.util.List<java.lang.String>
     * @description
     * 计算星星的最短路径
     */
    public List<String> getStarMinPath() {
        List<List<String>> all_directionts = new ArrayList<List<String>>();
        List<String> directions = new ArrayList<String>();
        for (int j = 0; j < this.stars.size(); j++) {
            DepthSearchPath depht_path = new DepthSearchPath();
            depht_path.Move(this.selfplayer.getX(), this.selfplayer.getY(), this.stars.get(j).getX(), this.stars.get(j).getY(), this.map);
            directions = depht_path.getDirection();
            if (directions.size() == 0) continue;
            all_directionts.add(directions);
        }
        for (int j = 0; j < this.stars.size(); j++) {
            SpanSearchPath span_path = new SpanSearchPath();
            span_path.Move(this.selfplayer.getX(), this.selfplayer.getY(), this.stars.get(j).getX(), this.stars.get(j).getY(), this.map);
            directions = span_path.getDirection();
            if (directions.size() == 0) continue;
            all_directionts.add(directions);
        }
        for (int j = 0; j < this.stars.size(); j++) {
            SearchPath span_path = new LeftSearchPath();
            span_path.Move(this.selfplayer.getX(), this.selfplayer.getY(), this.stars.get(j).getX(), this.stars.get(j).getY(), this.map);
            directions = span_path.getDirection();
            if (directions.size() == 0) continue;
            all_directionts.add(directions);
        }
        for (int j = 0; j < this.stars.size(); j++) {
            SearchPath span_path = new UpSearchPath();
            span_path.Move(this.selfplayer.getX(), this.selfplayer.getY(), this.stars.get(j).getX(), this.stars.get(j).getY(), this.map);
            directions = span_path.getDirection();
            if (directions.size() == 0) continue;
            all_directionts.add(directions);
        }
        if (all_directionts.isEmpty()) {
            System.out.println("所有路径不可达！");
            return new ArrayList<>();
        }
        Collections.sort(all_directionts, new Comparator<List<String>>() {
            @Override
            public int compare(List<String> o1, List<String> o2) {
                return o1.size() - o2.size();
            }
        });
        for (String s : all_directionts.get(0))
            System.out.println("最短路径方向" + s);
        return all_directionts.get(0);

    }

    /*
     *
     *
     * @author lizhilong
     * @date 2018/5/28 20:51
     * @param [selfplayers, map, enemyplayers]
     * @return java.util.List<java.lang.String>
     * @description
     * 计算敌我坦克最短路劲
     */
    public List<String> getEnemyMinPath() {
        Map<List<String>,Player> all_directionts_map = new IdentityHashMap<List<String>,Player>();
        List<String> directions = new ArrayList<String>();
        for (int j = 0; j < this.enemyplayers.size(); j++) {
            if (enemyplayers.get(j).getSuperBullet() != 1||selfplayer.getSuperBullet()==1) {
                SpanSearchPath span_path = new SpanSearchPath();
                span_path.Move(this.selfplayer.getX(), this.selfplayer.getY(), this.enemyplayers.get(j).getX(), this.enemyplayers.get(j).getY(), this.map);
                directions = span_path.getDirection();
                if (directions.size() == 0) continue;
                all_directionts_map.put(directions,this.enemyplayers.get(j));
            }
        }
        for (int j = 0; j < this.enemyplayers.size(); j++) {
            if (enemyplayers.get(j).getSuperBullet() != 1||selfplayer.getSuperBullet()==1) {
                DepthSearchPath depth_path = new DepthSearchPath();
                depth_path.Move(this.selfplayer.getX(), this.selfplayer.getY(), this.enemyplayers.get(j).getX(), this.enemyplayers.get(j).getY(), this.map);
                directions = depth_path.getDirection();
                if (directions.size() == 0) continue;
                all_directionts_map.put(directions,this.enemyplayers.get(j));
            }
        }
        for (int j = 0; j < this.enemyplayers.size(); j++) {
            if (enemyplayers.get(j).getSuperBullet() != 1||selfplayer.getSuperBullet()==1) {
                LeftSearchPath left_path = new LeftSearchPath();
                left_path.Move(this.selfplayer.getX(), this.selfplayer.getY(), this.enemyplayers.get(j).getX(), this.enemyplayers.get(j).getY(), this.map);
                directions = left_path.getDirection();
                if (directions.size() == 0) continue;
                all_directionts_map.put(directions,this.enemyplayers.get(j));
            }
        }
        for (int j = 0; j < this.enemyplayers.size(); j++) {
            if (enemyplayers.get(j).getSuperBullet() != 1||selfplayer.getSuperBullet()==1) {
                UpSearchPath up_path = new UpSearchPath();
                up_path.Move(this.selfplayer.getX(), this.selfplayer.getY(), this.enemyplayers.get(j).getX(), this.enemyplayers.get(j).getY(), this.map);
                directions = up_path.getDirection();
                if (directions.size() == 0) continue;
                all_directionts_map.put(directions,this.enemyplayers.get(j));
            }
        }
        if (all_directionts_map.isEmpty()) {
            System.out.println("所有路径不可达！");
            return new ArrayList<>();
        }

        Player  objValue= null;
        List<String>  objKey= null;
        List<Map.Entry<List<String>,Player>> sortHashMapByKey = new ArrayList<Map.Entry<List<String>,Player>>();
        for (Map.Entry<List<String>,Player> entry : all_directionts_map.entrySet()) {
            sortHashMapByKey.add(entry);
        }
        Collections.sort(sortHashMapByKey, new Comparator<Map.Entry<List<String>, Player>>() {
            @Override
            public int compare(Map.Entry<List<String>, Player> o1, Map.Entry<List<String>, Player> o2) {
                return o1.getKey().size() - o2.getKey().size();
            }
        });
        objValue = sortHashMapByKey.get(0).getValue();
        objKey = sortHashMapByKey.get(0).getKey();
        for (String s : objKey)
            System.out.println("最短路径方向" + s);
        enemyplayers_point = objValue;
        return objKey;

    }

    public Player getEnemyplayers_point() {
        return enemyplayers_point;
    }
}
