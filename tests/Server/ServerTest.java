package Server;

import Map.Area;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ServerTest {

    Server server;
    @Before
    public void init()
    {
        server = new Server();
    }
}
