package Visitor;

import AbstractFactory.Monster.Walker;
import Character.Enemies.Enemy;
import Iterator.BFSIterator;
import Map.Map;
import Map.Tile.Tile;
import Character.Player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class BFSVisitor extends Visitor {
    @Override
    public Tile[] visit(Map map, Tile from, Tile to) {
        ArrayList<Tile> closedList = new ArrayList<>();
        BFSIterator iterator = new BFSIterator();
        Tile[] prec = new Tile[map.getTileCount() + 1];

        // Starting tile followed by destination tile
        iterator.add(from);
        prec[from.getTrel_x() * map.getRows() + from.getTrel_y()] = from;

        //Declaring another visitor for neighboring tiles
        Visitor visitor = new NeighboringTilesVisitor();

        while (iterator.hasNext(null)) {
            Tile currentTile = (Tile) iterator.getNext(null);

            if (isCloseToTile(to, currentTile)) {
                prec[prec.length - 1] = currentTile;
                return prec;
            }

            closedList.add(currentTile);

            // Get neighboring tiles
            ArrayList<Tile> neighbors = this.accept(visitor, map, currentTile);

            for (Tile neighbor : neighbors) {
                if (!closedList.contains(neighbor) && !(Boolean) iterator.find(neighbor)) {
                    iterator.add(neighbor);
                    prec[neighbor.getTrel_x() * map.getRows() + neighbor.getTrel_y()] = currentTile;
                }
            }
        }
        return null;
    }

    @Override
    public Tile[] visit(Enemy enemy, Player destination) {
        ArrayList<Tile> closedList = new ArrayList<>();
        BFSIterator iterator = new BFSIterator();
        Tile[] prec = new Tile[enemy.getMap().getTileCount()+1];

        // Starting tile
        Tile startTile = enemy.getMap().getTileByLoc(enemy.getRel_x(), enemy.getRel_y());
        iterator.add(startTile);
        prec[startTile.getTrel_x()*enemy.getMap().getRows()+ startTile.getTrel_y()] = startTile;

        //Declaring another visitor for neighboring tiles
        Visitor visitor = new NeighboringTilesVisitor();

        while (iterator.hasNext(null)) {
            Tile currentTile = (Tile) iterator.getNext(null);

            if (isCloseToTile(enemy.getMap().getTileByLoc(destination.getRel_x(),  destination.getRel_y()), currentTile)) {
                prec[prec.length - 1] = currentTile;
                return prec;
            }

            closedList.add(currentTile);

            // Get neighboring tiles
            ArrayList<Tile> neighbors = this.accept(visitor, enemy, currentTile);

            for (Tile neighbor : neighbors) {
                if (!closedList.contains(neighbor) && !(Boolean)iterator.find(neighbor)) {
                    iterator.add(neighbor);
                    prec[neighbor.getTrel_x() * enemy.getMap().getRows() + neighbor.getTrel_y()] =  currentTile;
                }
            }
        }
        return null;
    }

    @Override
    public Tile[] visit(Walker enemy) {
        ArrayList<Tile> closedList = new ArrayList<>();
        BFSIterator iterator = new BFSIterator();
        Tile[] prec = new Tile[enemy.getMap().getTileCount() + 1];

        // Starting tile
        Tile startTile = enemy.getMap().getTileByLoc(enemy.getRel_x(), enemy.getRel_y());
        iterator.add(startTile);
        prec[startTile.getTrel_x()*enemy.getMap().getRows()+ startTile.getTrel_y()] = startTile;

        Visitor visitor = new NeighboringTilesVisitor();

        while (iterator.hasNext(null)) {
            Tile currentTile = (Tile) iterator.getNext(null);

            if (currentTile.getPickUp() != null) {
                prec[prec.length - 1] = currentTile;
                return prec;
            }

            closedList.add(currentTile);

            // Get neighboring tiles
            ArrayList<Tile> neighbors = this.accept(visitor, enemy, currentTile);

            for (Tile neighbor : neighbors) {
                if (!closedList.contains(neighbor) && !(Boolean)iterator.find(neighbor)) {
                    iterator.add(neighbor);
                    prec[neighbor.getTrel_x() * enemy.getMap().getRows() + neighbor.getTrel_y()] =  currentTile;
                }
            }
        }
        return null;
    }

    private boolean isCloseToTile(Tile destination, Tile currentTile) {
        int destX = destination.getTrel_x();
        int destY = destination.getTrel_y();

        if (currentTile.id) {
            //{-1, -1}, {0, -1}, {1, -1}, {-1, 0}, {0, 1}, {1, 0}
            if ((destX == currentTile.getTrel_x() - 1 && destY == currentTile.getTrel_y() - 1) ||
                    (destX == currentTile.getTrel_x() && destY == currentTile.getTrel_y() - 1) ||
                    (destX == currentTile.getTrel_x() + 1 && destY == currentTile.getTrel_y() - 1) ||
                    (destX == currentTile.getTrel_x() - 1 && destY == currentTile.getY()) ||
                    (destX == currentTile.getTrel_x() && destY == currentTile.getY() + 1) ||
                    (destX == currentTile.getTrel_x() + 1 && destY == currentTile.getY())) {
                return true;
            }
        } else {
            //{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, 1}, {1, 1}
            if ((destX == currentTile.getTrel_x() - 1 && destY == currentTile.getTrel_y()) ||
                    (destX == currentTile.getTrel_x() + 1 && destY == currentTile.getTrel_y()) ||
                    (destX == currentTile.getTrel_x() && destY == currentTile.getTrel_y() - 1) ||
                    (destX == currentTile.getTrel_x() && destY == currentTile.getTrel_y() + 1) ||
                    (destX == currentTile.getTrel_x() - 1 && destY == currentTile.getTrel_y() + 1) ||
                    (destX == currentTile.getTrel_x() + 1 && destY == currentTile.getTrel_y() + 1)) {
                return true;
            }
        }
        return false;
    }

    private ArrayList<Tile> accept(Visitor visitor, Map map, Tile tile){
        return visitor.visit(map, tile);
    }

    private ArrayList<Tile> accept(Visitor visitor, Enemy enemy, Tile tile){
        return visitor.visit(enemy, tile);
    }
}
