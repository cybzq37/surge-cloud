import com.fasterxml.jackson.core.JsonProcessingException;
import com.surge.device.task.ShipTraceTask;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ShipTraceTest {

    @Autowired
    private ShipTraceTask shipTraceTask;

    @Test
    public void testTask() throws JsonProcessingException {

        shipTraceTask.task();
    }

}
