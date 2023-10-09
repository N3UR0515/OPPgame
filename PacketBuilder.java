public interface PacketBuilder {
    public PacketBuilder setId(int id);
    public PacketBuilder setX(int x);
    public PacketBuilder setY(int y);
    public PacketBuilder setHP(int HP);
    public Packet getPacket();
}
