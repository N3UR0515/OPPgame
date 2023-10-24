package Packet.Builder;

import Packet.Packet;

public class PlayerAttackPacketBuilder extends PacketBuilder{
    @Override
    public Packet getPacket() {
        return new Packet().setX(x).setY(y).setAttack(true);
    }
}
