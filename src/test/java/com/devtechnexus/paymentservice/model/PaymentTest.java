import static org.junit.Assert.assertEquals;
import org.junit.Test;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.Month;

public class PaymentTest {

    @Test
    public void testParseTimestamp() {
        // Create a sample timestamp
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.of(2023, Month.OCTOBER, 16, 12, 0));

        // Create a Payment object
        Payment payment = new Payment();
        payment.setDatetime(timestamp);

        // Call the parseTimestamp method and validate the result
        String expected = "2023-10-16T12:00 UTC"; // Adjust the expected output based on your formatting
        String actual = payment.parseTimestamp();

        assertEquals(expected, actual);
    }
}