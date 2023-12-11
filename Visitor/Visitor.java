package Visitor;

import AbstractFactory.Monster.Walker;
import Character.Enemies.Enemy;
import Map.Tile.Tile;
import Map.Map;
import Character.Player;
import Map.Tile.TileFactory;

import java.util.ArrayList;
import java.util.Random;

public abstract class Visitor {
    public Tile[] visit(Map map, Tile from, Tile to){
        return null;
    }

    public Tile[] visit(Enemy enemy, Player destination){
        return null;
    }

    public Tile[] visit(Walker enemy){return null;}

    public ArrayList<Tile> visit(Map map, Tile tile){
        return null;
    }

    public ArrayList<Tile> visit(Enemy enemy, Tile tile){
        return null;
    }

    public void visit(Map map, Tile[] prec, Tile currentTile, boolean isWide, Random rng, TileFactory factory){
    }

    public void visit(Enemy enemy, Tile[] prec, Tile currentTile){}
}
