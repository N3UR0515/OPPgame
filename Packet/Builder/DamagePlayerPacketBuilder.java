package Packet.Builder;

import Packet.Packet;

public class DamagePlayerPacketBuilder extends PacketBuilder{
    @Override
    public Packet getPacket() {
        return new Packet().setId(id).setHP(HP).setAttack(true);
    }
}
