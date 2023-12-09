package Logs;

import Packet.Packet;

import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PacketSenderProxy {
    private ObjectOutputStream realOut;
    private PrintWriter logger;

    public PacketSenderProxy(ObjectOutputStream realOut) {
        this.realOut = realOut;

        try {
            String timestamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());
            logger = new PrintWriter(new FileWriter("PacketLog_" + timestamp + ".txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void log(String methodName, Packet packet) {
        String logEntry = methodName + " - " + new Date().toString();
        //System.out.println(logEntry);
        //System.out.println("X:" + packet.getX() + "  Y:" + packet.getY() + "  HP:" + packet.getHP() + "  ID:" + packet.getId());// Output to console for demonstration purposes
        logger.println(logEntry);
        logger.println("X:" + packet.getX() + "  Y:" + packet.getY() + "  HP:" + packet.getHP() + "  ID:" + packet.getId());
        logger.flush();
    }

    public void sendPacket(Packet packet) {
        log("sendPacket", packet);
        try {
            realOut.writeObject(packet);
            realOut.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeLogger() {
        logger.close();
    }
}
