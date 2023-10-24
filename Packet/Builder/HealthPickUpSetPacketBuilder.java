package Packet.Builder;

import Packet.Packet;

public class HealthPickUpSetPacketBuilder extends PacketBuilder {
    @Override
    public Packet getPacket() {
        return new Packet().setSetHealth(true).setX(x).setY(y);
    }
}
