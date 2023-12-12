package Iterator;

import Map.Tile.Tile;

import java.util.LinkedList;
import java.util.Queue;

public class BFSIterator implements IObjectIterator<Tile>{

    private final Queue<Tile> openQueue = new LinkedList<>();

    public BFSIterator() {
    }

    @Override
    public boolean hasNext(Tile object) {
        boolean t = openQueue.peek() != null;
        return t;
    }

    @Override
    public Object getNext(Tile object) {
        return openQueue.poll();
    }

    @Override
    public Object find(Tile data) {
        return (Boolean)openQueue.contains(data);
    }

    public void add(Tile tile){
        openQueue.add(tile);
    }
}
