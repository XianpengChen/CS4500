import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;

import org.junit.Test;

import java.io.IOException;
import java.util.List;

import spreadsheet.Cell;
import spreadsheet.FormulaCell;

import static org.junit.Assert.assertTrue;

public class TestJsonToSpreadsheet {
  private JsonToSpreadsheet jsonToSpreadsheet;
  private static ObjectMapper mapper = new ObjectMapper();

  @Test
  public void testCreateMultiplication() {
    this.jsonToSpreadsheet = new JsonToSpreadsheet();
    ArrayNode node = JsonNodeFactory.instance.arrayNode();
    node.add(1);
    node.add("*");
    node.add(50);
    FormulaCell multiplication = this.jsonToSpreadsheet.createMultiplication(node);
    assertTrue(multiplication.evaluate() == 50);
  }

  @Test
  public void testCreateAddition() {
    this.jsonToSpreadsheet = new JsonToSpreadsheet();
    ArrayNode node = JsonNodeFactory.instance.arrayNode();
    node.add(1);
    node.add("+");
    node.add(50);
    FormulaCell addition = this.jsonToSpreadsheet.createAddition(node);
    assertTrue(addition.evaluate() == 51);
  }

  @Test
  public void testCreateReference() {
    this.jsonToSpreadsheet = new JsonToSpreadsheet();
    ArrayNode node = JsonNodeFactory.instance.arrayNode();
    node.add(">");
    node.add(12);
    node.add(50);
    FormulaCell reference = this.jsonToSpreadsheet.createReference(node);
    assertTrue(reference.getFormula().evaluate() == 0);
  }

  @Test
  public void testCreateNumber() {
    this.jsonToSpreadsheet = new JsonToSpreadsheet();
    ArrayNode node = JsonNodeFactory.instance.arrayNode();
    node.add(">");
    node.add(12);
    node.add(22);
    FormulaCell reference = this.jsonToSpreadsheet.createReference(node);
    assertTrue(reference.getFormula().evaluate() == 0);
  }

  @Test
  public void testJfToCellNumber() throws IOException {
    this.jsonToSpreadsheet = new JsonToSpreadsheet();
    JsonNode node = mapper.readTree("1");
    Cell cell = this.jsonToSpreadsheet.jfToCell(node);
    assertTrue(cell.evaluate() == 1);
  }

  @Test
  public void testJfToCellReference() throws IOException {
    this.jsonToSpreadsheet = new JsonToSpreadsheet();
    JsonNode node = mapper.readTree("[\">\",4,5]");
    Cell cell = this.jsonToSpreadsheet.jfToCell(node);
    assertTrue(cell.evaluate() == 0);
  }

  @Test
  public void testJfToCellAddition() throws IOException {
    this.jsonToSpreadsheet = new JsonToSpreadsheet();
    JsonNode node = mapper.readTree("[2,\"+\",5]");
    Cell cell = this.jsonToSpreadsheet.jfToCell(node);
    assertTrue(cell.evaluate() == 7);
  }

  @Test
  public void testJfToCellMult() throws IOException {
    this.jsonToSpreadsheet = new JsonToSpreadsheet();
    JsonNode node = mapper.readTree("[2,\"*\",5]");
    Cell cell = this.jsonToSpreadsheet.jfToCell(node);
    assertTrue(cell.evaluate() == 10);
  }

  @Test
  public void testJfToCellException() throws IOException {
    this.jsonToSpreadsheet = new JsonToSpreadsheet();
    JsonNode node = mapper.readTree("[2,3]");
    try {
      this.jsonToSpreadsheet.jfToCell(node);
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().equals("Invalid JF structure"));
    }
  }

  @Test
  public void testConvertJfListToCells() throws IOException {
    this.jsonToSpreadsheet = new JsonToSpreadsheet();
    JsonNode node = mapper.readTree("[[2],[[4,\"+\",6]]]");
    List<List<Cell>> cellList = this.jsonToSpreadsheet.convertJfListToCells(node);
    assertTrue(cellList.get(0).get(0).evaluate() == 2);
    assertTrue(cellList.get(1).get(0).evaluate() == 10);
  }
}
