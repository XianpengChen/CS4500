import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.NullNode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestJSONReader {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private static ObjectMapper mapper = new ObjectMapper();

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void testWithInt() {
        String number = "4";
        JSONReader reader = new JSONReader(new Scanner(new StringReader(number)));
        assertTrue(reader.isNumber(number));
    }

    @Test
    public void testWithString() {
        String string = "a";
        JSONReader reader = new JSONReader(new Scanner(new StringReader(string)));
        assertFalse(reader.isNumber(string));
    }

    @Test
    public void testWithEmptyString() {
        String string = "";
        JSONReader reader = new JSONReader(new Scanner(new StringReader(string)));
        assertFalse(reader.isNumber(string));
    }

    @Test
    public void testWithNull() {
        String string = null;
        JSONReader reader = new JSONReader(new Scanner(new StringReader("")));
        assertFalse(reader.isNumber(string));
    }

    @Test
    public void testIsJsonObjectStr() {
        JSONReader reader = new JSONReader(new Scanner(new StringReader("")));
        assertTrue(reader.isJsonObject("\"hello\""));
    }

    @Test
    public void testIsJsonObjectNull() {
        JSONReader reader = new JSONReader(new Scanner(new StringReader("")));
        assertTrue(reader.isJsonObject("null"));
    }

    @Test
    public void testIsJsonObjectTrue() {
        JSONReader reader = new JSONReader(new Scanner(new StringReader("")));
        assertTrue(reader.isJsonObject("true"));
    }

    @Test
    public void testIsJsonObjectFalse() {
        JSONReader reader = new JSONReader(new Scanner(new StringReader("")));
        assertTrue(reader.isJsonObject("false"));
    }

    @Test
    public void testIsJsonObjectNum() {
        JSONReader reader = new JSONReader(new Scanner(new StringReader("")));
        assertTrue(reader.isJsonObject("12"));
    }

    @Test
    public void testIsJsonObjectDecimal() {
        JSONReader reader = new JSONReader(new Scanner(new StringReader("")));
        assertTrue(reader.isJsonObject("12.5"));
    }

    @Test
    public void testIsJsonObjectArray() {
        JSONReader reader = new JSONReader(new Scanner(new StringReader("")));
        assertTrue(reader.isJsonObject("[1,2]"));
    }

    @Test
    public void testIsJsonObject() {
        JSONReader reader = new JSONReader(new Scanner(new StringReader("")));
        assertTrue(reader.isJsonObject("{\"a\":12}"));
    }

    @Test
    public void testProcessInput() throws IOException {
        JSONReader reader = new JSONReader(new Scanner(new StringReader("{\"a\": 4}\n3\n{\"b\":5}\n4\n{\"id\": \" ae4f2c\"}")));
        JsonNode output = mapper.readTree("{\"a\":4}\n3\n{\"b\":5}\n4\n{\"id\":\" ae4f2c\"}\n");
        assertEquals(output, reader.readInput());
    }

    @Test
    public void testProcessInputBlank() throws IOException {
        JSONReader reader = new JSONReader(new Scanner(new StringReader("")));
        assertEquals(NullNode.getInstance(), reader.readInput());
    }

    @Test
    public void testProcessInputMultiline() throws IOException {
        JSONReader reader = new JSONReader(new Scanner(new StringReader("{\n\"a\": \n4\n}")));
        JsonNode output = mapper.readTree("{\"a\":4}\n");
        assertEquals(output, reader.readInput());
    }

    @Test
    public void testProcessInputNumber() throws IOException {
        JSONReader reader = new JSONReader(new Scanner(new StringReader("44")));
        JsonNode output = mapper.readTree("44\n");
        assertEquals(output, reader.readInput());
    }

    @Test
    public void testProcessInputCharacter() throws IOException {
        JSONReader reader = new JSONReader(new Scanner(new StringReader("\"true\"")));
        JsonNode output = mapper.readTree("\"true\"\n");
        assertEquals(output, reader.readInput());
    }

    @Test
    public void testProcessInputMultiNumber() throws IOException {
        JSONReader reader = new JSONReader(new Scanner(new StringReader("{\"a\":4}\n44\n55")));
        JsonNode output = mapper.readTree("{\"a\":4}\n44\n55\n");
        assertEquals(output, reader.readInput());
    }
}