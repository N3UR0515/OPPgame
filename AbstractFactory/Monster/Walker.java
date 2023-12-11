package AbstractFactory.Monster;

import Character.Enemies.Enemy;
import FlyWeight.FlyWeightFactory;
import Visitor.BFSVisitor;
import Map.Map;
import Map.Tile.HiderTile;
import Map.Tile.Tile;
import Visitor.Visitor;
import Visitor.BacktrackVisitor;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import Character.Character;
import Character.Player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Walker extends Enemy {
    public Walker(int HP, Map map, int rel_x, int rel_y){
        super(HP, map, rel_x, rel_y);
    }
    @Override
    public void createMonsterImage() {

        try {
            monsterImage = FlyWeightFactory.getMonsterImage("FlyWeight/Images/walker.png");
        } catch (SlickException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateCharacter(Character character) {
        getRealLoc();
        triangle.setCenterX(x);
        triangle.setCenterY(y);

       seekTile();
    }

    @Override
    public void drawCharacter(Graphics g, int x, int y, int HP) {
        if (map.getTileByLoc(rel_x, rel_y).getClass() != HiderTile.class) {
            this.getRealLoc();
            monsterImage.draw(g,this.x, this.y, this.HP);
        }
    }

    public void seekTile()
    {
        Visitor bfsVisitor = new BFSVisitor();
        Visitor backVisitor = new BacktrackVisitor();
        Tile[] prec = this.accept(bfsVisitor);
        this.accept(backVisitor, prec, prec[prec.length-1]);
    }

    private Tile[] accept(Visitor visitor){
        return visitor.visit(this);
    }

    private void accept(Visitor backVisitor, Tile[] prec, Tile tile) {
        backVisitor.visit(this, prec, tile);
    }
}
