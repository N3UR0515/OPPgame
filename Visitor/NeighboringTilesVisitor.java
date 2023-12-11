package Visitor;

import Character.Enemies.Enemy;
import Map.Map;
import Map.Tile.Tile;
import Map.Tile.UnavailableTile;

import java.util.ArrayList;

public class NeighboringTilesVisitor extends Visitor{

    public NeighboringTilesVisitor() {}

    @Override
    public ArrayList<Tile> visit(Map map, Tile tile) {
        ArrayList<Tile> neighbors = new ArrayList<>();

        int x = tile.getTrel_x();
        int y = tile.getTrel_y();

        int[][] directions = this.getDirections(tile);

        for (int[] dir : directions) {
            int dx = dir[0];
            int dy = dir[1];

            if (x + dx >= 0 && x + dx < map.getCols() && y + dy >= 0 && y + dy < map.getRows()) {
                Tile neighbor = map.getTileByLoc(x + dx, y + dy);
                neighbors.add(neighbor);
            }
        }

        return neighbors;
    }

    @Override
    public ArrayList<Tile> visit(Enemy enemy, Tile tile) {
        ArrayList<Tile> neighbors = new ArrayList<>();

        int x = tile.getTrel_x();
        int y = tile.getTrel_y();

        int[][] directions = this.getDirections(tile);

        for (int[] dir : directions) {
            int dx = dir[0];
            int dy = dir[1];

            if (x + dx >= 0 && x + dx < enemy.getMap().getCols() && y + dy >= 0 && y + dy < enemy.getMap().getRows()) {
                Tile neighbor = enemy.getMap().getTileByLoc(x + dx, y + dy);
                if (neighbor.getClass() != UnavailableTile.class && neighbor.getOnTile() == null){
                    neighbors.add(neighbor);
                }
            }
        }

        return neighbors;
    }

    private int[][] getDirections(Tile tile){

        int[][] directionsEven = {
                {-1, -1}, {0, -1}, {1, -1}, {-1, 0}, {0, 1}, {1, 0}
        };
        int[][] directionsOdd = {
                {-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, 1}, {1, 1}
        };

        if(tile.id)
        {
            return directionsEven;
        }
        else
        {
            return directionsOdd;
        }
    }
}
