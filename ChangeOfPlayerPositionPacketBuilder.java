public class ChangeOfPlayerPositionPacketBuilder implements PacketBuilder{
    private int id;
    private int x;
    private int y;
    @Override
    public PacketBuilder setId(int id) {
        this.id = id;
        return this;
    }

    @Override
    public PacketBuilder setX(int x) {
        this.x = x;
        return this;
    }

    @Override
    public PacketBuilder setY(int y) {
        this.y = y;
        return this;
    }

    @Override
    public PacketBuilder setHP(int HP) {
        return this;
    }

    @Override
    public Packet getPacket() {
        return new Packet().setId(id).setX(x).setY(y).setAttack(false).setEnemy(false);
    }
}
