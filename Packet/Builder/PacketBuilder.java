package Packet.Builder;

import Packet.Packet;

public abstract class PacketBuilder {
    int id;
    int x;
    int y;
    int HP;
    public PacketBuilder setId(int id)
    {
        this.id = id;
        return this;
    };
    public PacketBuilder setX(int x)
    {
        this.x = x;
        return this;
    };
    public PacketBuilder setY(int y)
    {
        this.y = y;
        return this;
    };
    public PacketBuilder setHP(int HP)
    {
        this.HP = HP;
        return this;
    };
    public abstract Packet getPacket();
}
