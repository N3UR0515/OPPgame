package Packet;

import java.util.*;

import Packet.Command.PacketCommand;
import Character.Character;

public class CommandInvoker {
    private PacketCommand packetCommand;
    private Queue<PacketCommand> packetCommandQueue;
    private Stack<PacketCommand> commandHistory;
    private Stack<PacketCommand> redoCommands;
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

    public void undo()
    {
        packetCommand.undo();
        if(!commandHistory.empty())
        {
            packetCommand = commandHistory.pop();
            if(packetCommand != null) {
                System.out.println(packetCommand);
                packetCommand.execute();
                redoCommands.push(packetCommand);
        }

        }
    }
    public void redo()
    {
        if(!redoCommands.empty())
        {
            packetCommand = redoCommands.pop();
            packetCommand.execute();
            commandHistory.push(packetCommand);
        }
    }

    public void invoke()
    {
        if(!packetCommandQueue.isEmpty())
        {
            while(!redoCommands.empty())
            {
                redoCommands.peek().execute();
                commandHistory.push(redoCommands.pop());
            }

            packetCommand = packetCommandQueue.remove();

            if(packetCommand != null)
            {
                packetCommand.execute();
                commandHistory.push(packetCommand);
            }

        }
    }

    public HashMap<Integer, Character> getResults()
    {
        if(packetCommand != null)
            return packetCommand.getResults();
        return null;
    }

    public PacketCommand getCommand()
    {
        return packetCommand;
    }
}
