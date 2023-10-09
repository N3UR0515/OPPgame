public class DamagePlayerPacketBuilder implements PacketBuilder{
    int id;
    int HP;
    @Override
    public PacketBuilder setId(int id) {
        this.id = id;
        return this;
    }

    @Override
    public PacketBuilder setX(int x) {
        return this;
    }

    @Override
    public PacketBuilder setY(int y) {
        return this;
    }
    @Override
    public PacketBuilder setHP(int HP)
    {
        this.HP = HP;
        return this;
    }


    @Override
    public Packet getPacket() {
        return new Packet().setId(id).setHP(HP).setAttack(true);
    }
}
