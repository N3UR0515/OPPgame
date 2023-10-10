public class ChangeOfEnemyPositionPacketBuilder extends PacketBuilder {
    @Override
    public Packet getPacket() {
        return new Packet().setId(id).setX(x).setY(y).setAttack(false).setEnemy(true);
    }
}
