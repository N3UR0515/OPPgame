package Main;

public class PlayerPosition {
    private int clientId;
    private int x;
    private int y;
    public boolean changed;

    public PlayerPosition(int clientId, int x, int y, boolean chng)
    {
        this.clientId = clientId;
        this.x = x;
        this.y = y;
        this.changed = chng;
    }

    public int getClientId()
    {
        return clientId;
    }
    public int getX()
    {
        return x;
    }
    public int getY()
    {
        return y;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public void setY(int y)
    {
        this.y = y;
    }
}
