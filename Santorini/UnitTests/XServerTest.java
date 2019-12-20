import org.junit.Test;
import static org.junit.Assert.*;
public class XServerTest {
    private XServer server = new XServer();

    @Test
    public void applyConfigTest() {
        String config = "{ \"min players\" : 4,\n" +
                "  \"port\"        : 55556,\n" +
                "  \"waiting for\" : 6,\n" +
                "  \"repeat\" : 0}";
        String expectedConfig = "min players: 4\n" +
                "port: 55556\n" +
                "waiting for: 6\n" +
                "repeat: 0";
        server.applyConfig(config);
        assertEquals(server.printConfig(), expectedConfig);
    }
}
