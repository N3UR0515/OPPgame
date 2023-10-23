package Packet;

import java.util.HashMap;
import java.util.Stack;
import Packet.Command.PacketCommand;
import Character.Character;

public class CommandInvoker {
    private PacketCommand packetCommand;
    private Stack<PacketCommand> commandHistory;
    public CommandInvoker()
    {
        commandHistory = new Stack<>();
    }

    public void setCommand(PacketCommand command)
    {
        if(packetCommand != null)
            commandHistory.push(this.packetCommand);
        this.packetCommand = command;
    }

    public void undo()
    {
        if(!commandHistory.empty())
            packetCommand = commandHistory.pop();
    }

    public void invoke()
    {
        if(packetCommand != null)
            packetCommand.execute();
    }

    public HashMap<Integer, Character> getResults()
    {
        return packetCommand.getResults();
    }

    public PacketCommand getCommand()
    {
        return packetCommand;
    }
}
