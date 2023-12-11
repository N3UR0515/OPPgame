package Visitor;

import Character.Enemies.Enemy;
import Map.Map;
import Map.Tile.Tile;
import Map.Tile.TileFactory;

import java.util.ArrayList;
import java.util.Random;

public class BacktrackVisitor extends Visitor {
    @Override
    public void visit(Map map, Tile[] prec, Tile currentTile, boolean isWide, Random rng, TileFactory factory) {
        Visitor visitor = new NeighboringTilesVisitor();
        boolean isFiery = isWide && rng.nextInt(6) == 5;
        while (prec[currentTile.getTrel_x() * map.getRows() + currentTile.getTrel_y()] != currentTile) {
            if (isWide) {
                ArrayList<Tile> neighbors = this.accept(visitor, map, currentTile);
                for (Tile n : neighbors){
                    if (map.isUnavailable(n)) {
                        if (isFiery && rng.nextInt(16) == 4) {
                            map.setTile(n.getTrel_x() * map.getRows() + n.getTrel_y(), factory.createTile(2, n.id, n.getTrel_y(), n.getTrel_x(), ""));
                        } else {
                            map.setTile(n.getTrel_x() * map.getRows() + n.getTrel_y(),factory.createTile(0, n.id, n.getTrel_y(), n.getTrel_x(), ""));
                        }
                    }
                }
            }
            if (map.isUnavailable(currentTile)) {
                if (isFiery && rng.nextInt(16) == 4){
                    map.setTile(currentTile.getTrel_x() * map.getRows() + currentTile.getTrel_y(),factory.createTile(2, currentTile.id, currentTile.getTrel_y(), currentTile.getTrel_x(), ""));
                } else {
                    map.setTile(currentTile.getTrel_x() * map.getRows() + currentTile.getTrel_y(), factory.createTile(0, currentTile.id, currentTile.getTrel_y(), currentTile.getTrel_x(), ""));
                }
            }
            currentTile = prec[currentTile.getTrel_x() * map.getRows() + currentTile.getTrel_y()];
        }
    }

    @Override
    public void visit(Enemy enemy, Tile[] prec, Tile currentTile) {
        while (prec[currentTile.getTrel_x() * enemy.getMap().getRows() + currentTile.getTrel_y()] != currentTile) {
            if (checkDistance(enemy, currentTile)) {
                enemy.setRel_x(currentTile.getTrel_x());
                enemy.setRel_y(currentTile.getTrel_y());
                break;
            }
            currentTile = prec[currentTile.getTrel_x() * enemy.getMap().getRows() + currentTile.getTrel_y()];
        }
    }

    private boolean checkDistance(Enemy enemy, Tile tile){
        int prel_x = enemy.getRel_x();
        int prel_y = enemy.getRel_y();

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

    private ArrayList<Tile> accept(Visitor visitor, Map map, Tile tile){
        return visitor.visit(map, tile);
    }
}
