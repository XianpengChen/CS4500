import org.junit.Test;
import static org.junit.Assert.*;
public class XClientsTest {
    private XClients clients = new XClients();


    @Test
    public void configApplyingTest() {
        String config = "{\"players\" : [[\"good\", \"foobar\", \"../../Player/src/player/Player\"],\n" +
                "  [\"good\", \"barfoo\", \"../../Player/src/player/Player\"],\n" +
                "  [\"breaker\", \"barfus\", \"../../Player/src/player/BreakerPlayer\"],\n" +
                "  [\"breaker\", \"cat\", \"../../Player/src/player/BreakerPlayer\"]],\n" +
                "  \"observers\" : [],\n" +
                "  \"ip\": \"127.0.0.1\",\n" +
                "  \"port\": 55556}";
        String expectedConfig = "players: \n" +
                "foobar good\n" +
                "barfoo good\n" +
                "barfus breaker\n" +
                "cat breaker\n" +
                "IP: 127.0.0.1\n" +
                "PORT: 55556";
        clients.applyConfig(config);
        assertEquals(clients.printConfig(), expectedConfig);
    }
}
