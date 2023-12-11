package Character.Enemies;

import AbstractFactory.EnemyFactory;
import Character.Character;
import Character.Player;
import Character.Camera;
import Visitor.BFSVisitor;

import FlyWeight.MonsterImage;
import Map.Tile.*;
import Map.Map;
import Visitor.Visitor;
import Visitor.BacktrackVisitor;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public abstract class Enemy extends Character {
    protected MonsterImage monsterImage;
    public Enemy(int HP, Map map, int rel_x, int rel_y) {
        super(HP, map, rel_x, rel_y);
    }
    public Enemy(int HP, Map map, int rel_x, int rel_y, Camera camera) {
        super(HP, map, rel_x, rel_y, camera);
    }

    protected Enemy parent;
    public void setParent(Enemy enemy)
    {
        this.parent = enemy;
    }

    @Override
    public boolean updateCharacter(GameContainer container) {
        return false;
    }

    @Override
    public abstract void updateCharacter(Character character);

    public void drawCharacter(Graphics g) {
        // Checks if enemy is NOT on a hiding tile. If true - draws enemy
        if (map.getTileByLoc(rel_x, rel_y).getClass() != HiderTile.class) {
            this.getRealLoc();
            monsterImage.draw(g,this.x, this.y, this.HP);
        }
    }
    public abstract void createMonsterImage();

    protected boolean checkDistance(Character player, Tile tile) {
        int prel_x = player.getRel_x();
        int prel_y = player.getRel_y();

        if(tile.id)
        {
            //{-1, -1}, {0, -1}, {1, -1}, {-1, 0}, {0, 1}, {1, 0}
            return (prel_x == tile.getTrel_x() - 1 && prel_y == tile.getTrel_y() - 1) ||
                    (prel_x == tile.getTrel_x() && prel_y == tile.getTrel_y() - 1) ||
                    (prel_x == tile.getTrel_x() + 1 && prel_y == tile.getTrel_y() - 1) ||
                    (prel_x == tile.getTrel_x() - 1 && prel_y == tile.getTrel_y()) ||
                    (prel_x == tile.getTrel_x() && prel_y == tile.getTrel_y() + 1) ||
                    (prel_x == tile.getTrel_x() + 1 && prel_y == tile.getTrel_y());

        }
        else
        {
            //{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, 1}, {1, 1}
            return (prel_x == tile.getTrel_x() - 1 && prel_y == tile.getTrel_y()) ||
                    (prel_x == tile.getTrel_x() + 1 && prel_y == tile.getTrel_y()) ||
                    (prel_x == tile.getTrel_x() && prel_y == tile.getTrel_y() - 1) ||
                    (prel_x == tile.getTrel_x() && prel_y == tile.getTrel_y() + 1) ||
                    (prel_x == tile.getTrel_x() - 1 && prel_y == tile.getTrel_y() + 1) ||
                    (prel_x == tile.getTrel_x() + 1 && prel_y == tile.getTrel_y() + 1);
        }
        //return prel_x==tile.getTrel_x()&&prel_y==tile.getTrel_y();
    }

    protected boolean checkDistance(Tile tile) {
        int prel_x = this.rel_x;
        int prel_y = this.rel_y;

        if(tile.id)
        {
            //{-1, -1}, {0, -1}, {1, -1}, {-1, 0}, {0, 1}, {1, 0}
            return (prel_x == tile.getTrel_x() - 1 && prel_y == tile.getTrel_y() - 1) ||
                    (prel_x == tile.getTrel_x() && prel_y == tile.getTrel_y() - 1) ||
                    (prel_x == tile.getTrel_x() + 1 && prel_y == tile.getTrel_y() - 1) ||
                    (prel_x == tile.getTrel_x() - 1 && prel_y == tile.getTrel_y()) ||
                    (prel_x == tile.getTrel_x() && prel_y == tile.getTrel_y() + 1) ||
                    (prel_x == tile.getTrel_x() + 1 && prel_y == tile.getTrel_y());
        }
        else
        {
            //{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, 1}, {1, 1}
            return (prel_x == tile.getTrel_x() - 1 && prel_y == tile.getTrel_y()) ||
                    (prel_x == tile.getTrel_x() + 1 && prel_y == tile.getTrel_y()) ||
                    (prel_x == tile.getTrel_x() && prel_y == tile.getTrel_y() - 1) ||
                    (prel_x == tile.getTrel_x() && prel_y == tile.getTrel_y() + 1) ||
                    (prel_x == tile.getTrel_x() - 1 && prel_y == tile.getTrel_y() + 1) ||
                    (prel_x == tile.getTrel_x() + 1 && prel_y == tile.getTrel_y() + 1);
        }
    }

    protected void seekPlayer(Character player) {
        Visitor bfsVisitor = new BFSVisitor();
        Visitor backVisitor = new BacktrackVisitor();
        Tile[] prec = this.accept(bfsVisitor, player);
        this.accept(backVisitor, prec, prec[prec.length-1]);
    }

//    protected void moveEnemyToPlayer(Tile[] prec, Tile currentTile) {
//        while (prec[currentTile.getTrel_x() * map.getRows() + currentTile.getTrel_y()] != currentTile) {
//            if (checkDistance(currentTile)) {
//                this.rel_x = currentTile.getTrel_x();
//                this.rel_y = currentTile.getTrel_y();
//                break;
//            }
//            currentTile = prec[currentTile.getTrel_x() * map.getRows() + currentTile.getTrel_y()];
//        }
//    }

//    protected ArrayList<Tile> getNeighbors(Tile tile) {
//        ArrayList<Tile> neighbors = new ArrayList<>();
//
//        int x = tile.getTrel_x();
//        int y = tile.getTrel_y();
//
//        int[][] directionsEven = {
//                {-1, -1}, {0, -1}, {1, -1}, {-1, 0}, {0, 1}, {1, 0}
//        };
//        int[][] directionsOdd = {
//                {-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, 1}, {1, 1}
//        };
//        int[][] directions ={};
//
//        if(tile.id)
//        {
//            directions = directionsEven;
//        }
//        else
//        {
//            directions = directionsOdd;
//        }
//        for (int[] dir : directions) {
//            int dx = dir[0];
//            int dy = dir[1];
//
//            if (x + dx >= 0 && x + dx < map.getCols() && y + dy >= 0 && y + dy < map.getRows()) {
//                Tile neighbor = map.getTileByLoc(x + dx, y + dy);
//                if (neighbor.getClass() != UnavailableTile.class && neighbor.getOnTile() == null){
//                    neighbors.add(neighbor);
//                }
//            }
//        }
//
//        return neighbors;
//    }

    private Tile[] accept(Visitor visitor, Character player){
        return visitor.visit(this, (Player) player);
    }
    private void accept(Visitor backVisitor, Tile[] prec, Tile tile) {
        backVisitor.visit(this, prec, tile);
    }
}
