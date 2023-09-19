public class PlayerPosition {
    private int clientId;
    private int x;
    private int y;

    public PlayerPosition(int clientId, int x, int y)
    {
        this.clientId = clientId;
        this.x = x;
        this.y = y;
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
}
