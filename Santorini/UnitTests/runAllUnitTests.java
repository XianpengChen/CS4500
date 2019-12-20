
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Result;

public class runAllUnitTests {
    public static void main(String[] args) {
    JUnitCore jUnitCore = new JUnitCore();
    System.out.println("Please be patient, this runs all unit tests, which will take 2 or 3 minutes");
    Request request1 = Request.aClass(RefereeTest.class);
    Result result1 = jUnitCore.run(request1);
    System.out.printf("Referee Tests ran: %s, Failed: %s%n",
            result1.getRunCount(), result1.getFailureCount());

    Request request2 = Request.aClass(PlayerTest.class);
    Result result2 = jUnitCore.run(request2);
    System.out.printf("Player Tests ran: %s, Failed: %s%n",
            result2.getRunCount(), result2.getFailureCount());

    Request request3 = Request.aClass(PlayerTest.class);
    Result result3 = jUnitCore.run(request3);
    System.out.printf("StayAliveStrategy Tests ran: %s, Failed: %s%n",
            result3.getRunCount(), result3.getFailureCount());

    Request request4 = Request.aClass(ObserverTest.class);
    Result result4 = jUnitCore.run(request4);
    System.out.printf("observer Tests ran: %s, Failed: %s%n",
            result4.getRunCount(), result4.getFailureCount());

    Request request5 = Request.aClass(TManagerTest.class);
    Result result5 = jUnitCore.run(request5);
    System.out.printf("TManager Tests ran: %s, Failed: %s%n",
            result5.getRunCount(), result5.getFailureCount());

    Request request6 = Request.aClass(JSONUtilsTest.class);
    Result result6 = jUnitCore.run(request6);
    System.out.printf("JSONUtils Tests ran: %s, Failed: %s%n",
            result6.getRunCount(), result6.getFailureCount());

    Request request7 = Request.aClass(XClientsTest.class);
    Result result7 = jUnitCore.run(request7);
    System.out.printf("XClients Tests ran: %s, Failed: %s%n",
            result7.getRunCount(), result7.getFailureCount());

    Request request8 = Request.aClass(XServerTest.class);
    Result result8 = jUnitCore.run(request8);
    System.out.printf("XServer Tests ran: %s, Failed: %s%n",
            result8.getRunCount(), result8.getFailureCount());
    }
}
