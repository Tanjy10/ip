package tanjy.parser;

import tanjy.exception.TanjyException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {

    @Test
    public void parseDateTime_dateOnly_returnsStartOfDay() throws TanjyException {
        Parser parser = new Parser();

        LocalDateTime dt = parser.parseDateTime("2019-10-15");

        assertEquals(LocalDateTime.of(2019, 10, 15, 0, 0), dt);
    }

    @Test
    public void parseDateTime_dateTime_parsesCorrectly() throws TanjyException {
        Parser parser = new Parser();

        LocalDateTime dt = parser.parseDateTime("2019-10-15 1800");

        assertEquals(LocalDateTime.of(2019, 10, 15, 18, 0), dt);
    }

    @Test
    public void parseDateTime_trimsWhitespace_parsesCorrectly() throws TanjyException {
        Parser parser = new Parser();

        LocalDateTime dt = parser.parseDateTime("   2019-10-15 1800   ");

        assertEquals(LocalDateTime.of(2019, 10, 15, 18, 0), dt);
    }

    @Test
    public void parseDateTime_invalidFormat_throwsException() {
        Parser parser = new Parser();

        TanjyException e = assertThrows(TanjyException.class, () -> parser.parseDateTime("15/10/2019"));

        assertTrue(e.getMessage().toLowerCase().contains("invalid"));
    }
}
