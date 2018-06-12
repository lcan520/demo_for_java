package demo;

import map.*;

import java.util.*;

public class GetBulltesDistence {
    public GetBulltesDistence(){}
    public boolean calculateDistence(Player player, List<Bullet> bullets){
        boolean isDangerous = false;
        int distence = 0;
            for(int j= 0;j<bullets.size();j++){
                distence = Math.abs(player.getX()-bullets.get(j).getX())+Math.abs(player.getY()-bullets.get(j).getY());
                if(distence>2) {
                    isDangerous = true;
                    break;
                }
            }

        return isDangerous;
    }
}

