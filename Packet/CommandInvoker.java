package Packet;

import java.util.*;

import Packet.Command.PacketCommand;
import Character.Character;
import Memento.Memento;
import Memento.PacketCommandMemento;
public class CommandInvoker {
    private PacketCommand packetCommand;
    private Queue<PacketCommand> packetCommandQueue;
    private Stack<Memento> commandHistory;
    private Stack<Memento> redoCommands;

    public CommandInvoker()
    {
        packetCommandQueue = new ArrayDeque<>();
        commandHistory = new Stack<>();
        redoCommands = new Stack<>();
    }

    public void setCommand(PacketCommand command)
    {
        if(command != null)
            packetCommandQueue.add(command);
    }

    public void undo() {
        if (!commandHistory.empty()) {
            System.out.println("Memento undo");
            System.out.println(commandHistory.size());
            Memento memento = commandHistory.pop();
            redoCommands.push(memento);
            memento.restore();
        }
    }

    public void redo() {
        if (!redoCommands.empty()) {
            System.out.println("Memento redo");
            Memento memento = redoCommands.pop();
            System.out.println(commandHistory.size());
            commandHistory.push(memento);
            memento.restore();
        }
    }

    public void invoke() {
        if (!packetCommandQueue.isEmpty()) {
            packetCommand = packetCommandQueue.remove();
            if (packetCommand != null) {
                packetCommand.execute();
                commandHistory.push(new PacketCommandMemento(packetCommand));
            }
        }
    }
}
