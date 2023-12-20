package Memento;

import Packet.Command.PacketCommand;

public class PacketCommandMemento implements Memento{
    private final PacketCommand command;

    public PacketCommandMemento(PacketCommand command) {
        this.command = command;
    }

    @Override
    public void restore() {
        command.execute();
    }
}
