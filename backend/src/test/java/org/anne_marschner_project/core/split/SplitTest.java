package org.anne_marschner_project.core.split;

import org.anne_marschner_project.core.data.Attribute;
import org.anne_marschner_project.core.data.Relation;
import org.anne_marschner_project.core.data.Type;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class SplitTest {

    @Test
    void testSplitHorizontally_withOverlap() {

        // Create relation
        Map<Integer, Attribute> schema = new HashMap<>();
        schema.put(0, new Attribute("ID", Type.DOUBLE));
        schema.put(1, new Attribute("Name", Type.STRING));
        schema.put(2, new Attribute("Age", Type.DOUBLE));
        Map<Integer, List<String>> data = new HashMap<>();
        data.put(0, Arrays.asList("1", "2", "3", "4", "5"));
        data.put(1, Arrays.asList("Anne", "Jonathan", "Mareike", "Luca", "Milena"));
        data.put(2, Arrays.asList("24", "22", "23", "27", "22"));
        Relation sourceRelation = new Relation(schema, data);

        // Execute split
        Split split = new Split();
        int overlapPercentage = 50;
        List<Relation> result = split.splitHorizontally(sourceRelation, overlapPercentage, 50);
        Relation leftRelation = result.get(0);
        Relation rightRelation = result.get(1);

        // Check the number of rows in both datasets
        int expectedOverlapRows = (int) Math.ceil(5 * (overlapPercentage / 100.0));
        assertEquals(expectedOverlapRows + 1, leftRelation.getData().get(0).size());
        assertEquals(expectedOverlapRows + 1, rightRelation.getData().get(0).size());

        // Check if number of overlapping rows is correct
        assertEquals(leftRelation.getNumOfOverlappingRows(), rightRelation.getNumOfOverlappingRows());
        assertEquals(leftRelation.getNumOfOverlappingRows(), expectedOverlapRows);

        // Check if overlapping columns are null
        assertNull(leftRelation.getOverlappingColumnsIndices());
        assertNull(rightRelation.getOverlappingColumnsIndices());
    }


    @Test
    void testSplitHorizontally_withZeroOverlap() {

        // Create relation
        Map<Integer, Attribute> schema = new HashMap<>();
        schema.put(0, new Attribute("ID", Type.DOUBLE));
        schema.put(1, new Attribute("Name", Type.STRING));
        schema.put(2, new Attribute("Age", Type.DOUBLE));
        Map<Integer, List<String>> data = new HashMap<>();
        data.put(0, Arrays.asList("1", "2", "3", "4", "5"));
        data.put(1, Arrays.asList("Anne", "Jonathan", "Mareike", "Luca", "Milena"));
        data.put(2, Arrays.asList("24", "22", "23", "27", "22"));
        Relation sourceRelation = new Relation(schema, data);

        // Execute split
        Split split = new Split();
        int overlapPercentage = 0;
        List<Relation> result = split.splitHorizontally(sourceRelation, overlapPercentage, 50);
        Relation leftRelation = result.get(0);
        Relation rightRelation = result.get(1);

        // Check the number of rows in both datasets
        assertEquals(3, leftRelation.getData().get(0).size());
        assertEquals(2, rightRelation.getData().get(0).size());

        // Check if number of overlapping rows is correct
        assertEquals(leftRelation.getNumOfOverlappingRows(), rightRelation.getNumOfOverlappingRows());
        assertEquals(leftRelation.getNumOfOverlappingRows(), 0);

        // Check if overlapping columns are null
        assertNull(leftRelation.getOverlappingColumnsIndices());
        assertNull(rightRelation.getOverlappingColumnsIndices());

        // Check if the values are correct
        assertEquals("[1, 2, 3]", leftRelation.getData().get(0).toString());
        assertEquals("[Anne, Jonathan, Mareike]", leftRelation.getData().get(1).toString());
        assertEquals("[24, 22, 23]", leftRelation.getData().get(2).toString());
        assertEquals("[4, 5]", rightRelation.getData().get(0).toString());
        assertEquals("[Luca, Milena]", rightRelation.getData().get(1).toString());
        assertEquals("[27, 22]", rightRelation.getData().get(2).toString());
    }


    @Test
    void testSplitHorizontally_withFullOverlap() {

        // Create relation
        Map<Integer, Attribute> schema = new HashMap<>();
        schema.put(0, new Attribute("ID", Type.DOUBLE));
        schema.put(1, new Attribute("Name", Type.STRING));
        schema.put(2, new Attribute("Age", Type.DOUBLE));
        Map<Integer, List<String>> data = new HashMap<>();
        data.put(0, Arrays.asList("1", "2", "3", "4", "5"));
        data.put(1, Arrays.asList("Anne", "Jonathan", "Mareike", "Luca", "Milena"));
        data.put(2, Arrays.asList("24", "22", "23", "27", "22"));
        Relation sourceRelation = new Relation(schema, data);

        // Execute split
        Split split = new Split();
        int overlapPercentage = 100;
        List<Relation> result = split.splitHorizontally(sourceRelation, overlapPercentage, 50);
        Relation leftRelation = result.get(0);
        Relation rightRelation = result.get(1);

        // Check the number of rows in both datasets
        int expectedOverlapRows = (int) Math.ceil(5 * (overlapPercentage / 100.0));
        assertEquals(expectedOverlapRows, leftRelation.getData().get(0).size());
        assertEquals(expectedOverlapRows, rightRelation.getData().get(0).size());

        // Check if number of overlapping rows is correct
        assertEquals(leftRelation.getNumOfOverlappingRows(), rightRelation.getNumOfOverlappingRows());
        assertEquals(leftRelation.getNumOfOverlappingRows(), expectedOverlapRows);

        // Check if overlapping columns is null
        assertNull(leftRelation.getOverlappingColumnsIndices());
        assertNull(rightRelation.getOverlappingColumnsIndices());

        // Check if the values are correct
        assertEquals("[1, 2, 3, 4, 5]", leftRelation.getData().get(0).toString());
        assertEquals("[Anne, Jonathan, Mareike, Luca, Milena]", leftRelation.getData().get(1).toString());
        assertEquals("[24, 22, 23, 27, 22]", leftRelation.getData().get(2).toString());
        assertEquals("[1, 2, 3, 4, 5]", rightRelation.getData().get(0).toString());
        assertEquals("[Anne, Jonathan, Mareike, Luca, Milena]", rightRelation.getData().get(1).toString());
        assertEquals("[24, 22, 23, 27, 22]", rightRelation.getData().get(2).toString());
    }


    @Test
    void testSplitVertically_withKeyColumnsAndOverlap() {

        // Create relation
        Map<Integer, Attribute> schema = new HashMap<>();
        schema.put(0, new Attribute("ID", Type.DOUBLE));
        schema.put(1, new Attribute("Name", Type.STRING));
        schema.put(2, new Attribute("Age", Type.DOUBLE));
        schema.put(3, new Attribute("Height", Type.DOUBLE));
        schema.put(4, new Attribute("Hobby", Type.STRING));
        Map<Integer, List<String>> data = new HashMap<>();
        data.put(0, Arrays.asList("1", "2", "3", "4", "5"));
        data.put(1, Arrays.asList("Anne", "Jonathan", "Mareike", "Luca", "Milena"));
        data.put(2, Arrays.asList("24", "22", "23", "27", "22"));
        data.put(3, Arrays.asList("1.57", "1.8", "1.6", "1.71", "1.58"));
        data.put(4, Arrays.asList("dancing", "football", "knitting", "running", "fitness"));
        Relation sourceRelation = new Relation(schema, data);
        List<Integer> keyIndices = Arrays.asList(0, 1);
        sourceRelation.setKeyIndices(keyIndices); // ID and Name are keys

        // Execute split
        Split split = new Split();
        int columnOverlap = 50;
        List<Relation> result = split.splitVertically(sourceRelation, columnOverlap, 50);
        Relation leftRelation = result.get(0);
        Relation rightRelation = result.get(1);

        // Validate that key columns are in both datasets
        assertTrue(leftRelation.getSchema().containsKey(0)); // ID
        assertTrue(leftRelation.getSchema().containsKey(1)); // Name
        assertTrue(rightRelation.getSchema().containsKey(0)); // ID
        assertTrue(rightRelation.getSchema().containsKey(1)); // Name

        // Check that column overlap is correct
        int totalNonKeyColumns = 3;
        int expectedOverlapColumns = (int) Math.round(0.5 * totalNonKeyColumns);
        Set<Integer> leftColumnSet = new HashSet<>(leftRelation.getSchema().keySet());
        Set<Integer> rightColumnSet = new HashSet<>(rightRelation.getSchema().keySet());
        leftColumnSet.retainAll(rightColumnSet); // Find the intersection (overlap)
        assertEquals(expectedOverlapColumns + keyIndices.size(), leftColumnSet.size()); // Expect keys + overlap

        // Check that total column count is correct
        assertEquals(5, leftRelation.getSchema().size());
        assertEquals(4, rightRelation.getSchema().size());

        // Check if overlapping column indices are the same and correct size
        assertEquals(leftRelation.getOverlappingColumnsIndices(), rightRelation.getOverlappingColumnsIndices());
        assertEquals(leftRelation.getOverlappingColumnsIndices().size(), expectedOverlapColumns + keyIndices.size());

        // Check if row overlap is null
        assertNull(leftRelation.getNumOfOverlappingRows());
        assertNull(rightRelation.getNumOfOverlappingRows());
    }


    @Test
    void testSplitVertically_withZeroOverlap() {

        // Create relation
        Map<Integer, Attribute> schema = new HashMap<>();
        schema.put(0, new Attribute("ID", Type.DOUBLE));
        schema.put(1, new Attribute("Name", Type.STRING));
        schema.put(2, new Attribute("Age", Type.DOUBLE));
        schema.put(3, new Attribute("Height", Type.DOUBLE));
        schema.put(4, new Attribute("Hobby", Type.STRING));
        Map<Integer, List<String>> data = new HashMap<>();
        data.put(0, Arrays.asList("1", "2", "3", "4", "5"));
        data.put(1, Arrays.asList("Anne", "Jonathan", "Mareike", "Luca", "Milena"));
        data.put(2, Arrays.asList("24", "22", "23", "27", "22"));
        data.put(3, Arrays.asList("1.57", "1.8", "1.6", "1.71", "1.58"));
        data.put(4, Arrays.asList("dancing", "football", "knitting", "running", "fitness"));
        Relation sourceRelation = new Relation(schema, data);
        List<Integer> keyIndices = Arrays.asList(0, 1); // ID and Name are keys
        sourceRelation.setKeyIndices(keyIndices);

        // Execute split
        Split split = new Split();
        int columnOverlap = 0;
        List<Relation> result = split.splitVertically(sourceRelation, columnOverlap, 50);
        Relation leftRelation = result.get(0);
        Relation rightRelation = result.get(1);

        // Validate that key columns are in both datasets
        assertTrue(leftRelation.getSchema().containsKey(0)); // ID
        assertTrue(leftRelation.getSchema().containsKey(1)); // Name
        assertTrue(rightRelation.getSchema().containsKey(0)); // ID
        assertTrue(rightRelation.getSchema().containsKey(1)); // Name

        // Check that no additional columns overlap
        Set<Integer> leftColumnSet = new HashSet<>(leftRelation.getSchema().keySet());
        Set<Integer> rightColumnSet = new HashSet<>(rightRelation.getSchema().keySet());
        leftColumnSet.retainAll(rightColumnSet); // Find the intersection (overlap)
        assertEquals(2, leftColumnSet.size()); // Only keys should overlap

        // Check that total column count is correct
        assertEquals(4, leftRelation.getSchema().size());
        assertEquals(3, rightRelation.getSchema().size());

        // Check if overlapping column indices are the same and correct size
        assertEquals(leftRelation.getOverlappingColumnsIndices(), rightRelation.getOverlappingColumnsIndices());
        assertEquals(leftRelation.getOverlappingColumnsIndices().size(), keyIndices.size());

        // Check if row overlap is null
        assertNull(leftRelation.getNumOfOverlappingRows());
        assertNull(rightRelation.getNumOfOverlappingRows());
    }

    @Test
    void testSplitVertically_withFullOverlap() {

        // Create relation
        Map<Integer, Attribute> schema = new HashMap<>();
        schema.put(0, new Attribute("ID", Type.DOUBLE));
        schema.put(1, new Attribute("Name", Type.STRING));
        schema.put(2, new Attribute("Age", Type.DOUBLE));
        schema.put(3, new Attribute("Salary", Type.DOUBLE));
        schema.put(4, new Attribute("Department", Type.STRING));
        Map<Integer, List<String>> data = new HashMap<>();
        data.put(0, Arrays.asList("1", "2", "3", "4", "5"));
        data.put(1, Arrays.asList("Anne", "Jonathan", "Mareike", "Luca", "Milena"));
        data.put(2, Arrays.asList("24", "22", "23", "27", "22"));
        data.put(3, Arrays.asList("1.57", "1.8", "1.6", "1.71", "1.58"));
        data.put(4, Arrays.asList("dancing", "football", "knitting", "running", "fitness"));
        Relation sourceRelation = new Relation(schema, data);
        List<Integer> keyIndices = Arrays.asList(0, 1); // ID and Name are keys
        sourceRelation.setKeyIndices(keyIndices);

        // Execute split
        Split split = new Split();
        int columnOverlap = 100;
        List<Relation> result = split.splitVertically(sourceRelation, columnOverlap, 50);
        Relation leftRelation = result.get(0);
        Relation rightRelation = result.get(1);

        // Validate that all columns are in both datasets (since columnOverlap is 100%)
        assertEquals(sourceRelation.getSchema().size(), leftRelation.getSchema().size());
        assertEquals(sourceRelation.getSchema().size(), rightRelation.getSchema().size());

        // Validate that the data is correctly distributed
        assertEquals(sourceRelation.getData().size(), leftRelation.getData().size());
        assertEquals(sourceRelation.getData().size(), rightRelation.getData().size());

        // Check if overlapping column indices are the same and correct size
        assertEquals(leftRelation.getOverlappingColumnsIndices(), rightRelation.getOverlappingColumnsIndices());
        assertEquals(leftRelation.getOverlappingColumnsIndices().size(), sourceRelation.getSchema().size());

        // Check if row overlap is null
        assertNull(leftRelation.getNumOfOverlappingRows());
        assertNull(rightRelation.getNumOfOverlappingRows());
    }

    @Test
    void testSplitVertically_withDistribution() {

        // Create relation
        Map<Integer, Attribute> schema = new HashMap<>();
        schema.put(0, new Attribute("ID", Type.DOUBLE));
        schema.put(1, new Attribute("Name", Type.STRING));
        schema.put(2, new Attribute("Age", Type.DOUBLE));
        schema.put(3, new Attribute("Height", Type.DOUBLE));
        schema.put(4, new Attribute("Hobby", Type.STRING));
        Map<Integer, List<String>> data = new HashMap<>();
        data.put(0, Arrays.asList("1", "2", "3", "4", "5"));
        data.put(1, Arrays.asList("Anne", "Jonathan", "Mareike", "Luca", "Milena"));
        data.put(2, Arrays.asList("24", "22", "23", "27", "22"));
        data.put(3, Arrays.asList("1.57", "1.8", "1.6", "1.71", "1.58"));
        data.put(4, Arrays.asList("dancing", "football", "knitting", "running", "fitness"));
        Relation sourceRelation = new Relation(schema, data);
        List<Integer> keyIndices = Arrays.asList(0);
        sourceRelation.setKeyIndices(keyIndices); // ID and Name are keys

        // Execute split
        Split split = new Split();
        int columnOverlap = 30;
        List<Relation> result = split.splitVertically(sourceRelation, columnOverlap, 100);
        Relation leftRelation = result.get(0);
        Relation rightRelation = result.get(1);

        // Validate that key columns are in both datasets
        assertTrue(leftRelation.getSchema().containsKey(0)); // ID
        assertTrue(rightRelation.getSchema().containsKey(0)); // ID

        // Check that column overlap is correct
        int totalNonKeyColumns = 4;
        int expectedOverlapColumns = (int) Math.round(0.3 * totalNonKeyColumns);
        Set<Integer> leftColumnSet = new HashSet<>(leftRelation.getSchema().keySet());
        Set<Integer> rightColumnSet = new HashSet<>(rightRelation.getSchema().keySet());
        leftColumnSet.retainAll(rightColumnSet); // Find the intersection (overlap)
        assertEquals(expectedOverlapColumns + keyIndices.size(), leftColumnSet.size()); // Expect keys + overlap

        // Check that total column count is correct
        assertEquals(5, leftRelation.getSchema().size());
        assertEquals(2, rightRelation.getSchema().size());

        // Check if overlapping column indices are the same and correct size
        assertEquals(leftRelation.getOverlappingColumnsIndices(), rightRelation.getOverlappingColumnsIndices());
        assertEquals(leftRelation.getOverlappingColumnsIndices().size(), expectedOverlapColumns + keyIndices.size());

        // Check if row overlap is null
        assertNull(leftRelation.getNumOfOverlappingRows());
        assertNull(rightRelation.getNumOfOverlappingRows());
    }

    @Test
    void testSplitHorizontally_withDistribution() {

        // Create relation
        Map<Integer, Attribute> schema = new HashMap<>();
        schema.put(0, new Attribute("ID", Type.DOUBLE));
        schema.put(1, new Attribute("Name", Type.STRING));
        schema.put(2, new Attribute("Age", Type.DOUBLE));
        Map<Integer, List<String>> data = new HashMap<>();
        data.put(0, Arrays.asList("1", "2", "3", "4", "5"));
        data.put(1, Arrays.asList("Anne", "Jonathan", "Mareike", "Luca", "Milena"));
        data.put(2, Arrays.asList("24", "22", "23", "27", "22"));
        Relation sourceRelation = new Relation(schema, data);

        // Execute split
        Split split = new Split();
        int overlapPercentage = 20;
        List<Relation> result = split.splitHorizontally(sourceRelation, overlapPercentage, 30);
        Relation leftRelation = result.get(0);
        Relation rightRelation = result.get(1);

        // Check the number of rows in both datasets
        int expectedOverlapRows = (int) Math.round(5 * (overlapPercentage / 100.0));
        assertEquals(expectedOverlapRows + 1, leftRelation.getData().get(0).size());
        assertEquals(expectedOverlapRows + 3, rightRelation.getData().get(0).size());

        // Check if number of overlapping rows is correct
        assertEquals(leftRelation.getNumOfOverlappingRows(), rightRelation.getNumOfOverlappingRows());
        assertEquals(leftRelation.getNumOfOverlappingRows(), expectedOverlapRows);

        // Check if overlapping columns is null
        assertNull(leftRelation.getOverlappingColumnsIndices());
        assertNull(rightRelation.getOverlappingColumnsIndices());
    }

    @Test
    void testSplitHorizontallyWithMixedOverlap() {

        // Create relation
        Map<Integer, Attribute> schema = new HashMap<>();
        schema.put(0, new Attribute("ID", Type.DOUBLE));
        schema.put(1, new Attribute("Name", Type.STRING));
        schema.put(2, new Attribute("Age", Type.DOUBLE));
        Map<Integer, List<String>> data = new HashMap<>();
        data.put(0, Arrays.asList("1", "2", "3", "4", "5"));
        data.put(1, Arrays.asList("Anne", "Jonathan", "Mareike", "Luca", "Milena"));
        data.put(2, Arrays.asList("24", "22", "23", "27", "22"));
        Relation sourceRelation = new Relation(schema, data);

        // Execute split
        Split split = new Split();
        int overlapPercentage = 50;
        List<Relation> result = split.splitHorizontallyWithMixedOverlap(sourceRelation, overlapPercentage, 10);
        Relation leftRelation = result.get(0);
        Relation rightRelation = result.get(1);

        // Check the number of rows in both datasets
        int expectedOverlapRows = (int) Math.round(5 * (overlapPercentage / 100.0));
        assertEquals(expectedOverlapRows + 0, leftRelation.getData().get(0).size());
        assertEquals(expectedOverlapRows + 2, rightRelation.getData().get(0).size());

        // Check if number of overlapping rows is correct
        assertEquals(leftRelation.getNumOfOverlappingRows(), rightRelation.getNumOfOverlappingRows());
        assertEquals(leftRelation.getNumOfOverlappingRows(), expectedOverlapRows);

        // Check if overlapping columns is null
        assertNull(leftRelation.getOverlappingColumnsIndices());
        assertNull(rightRelation.getOverlappingColumnsIndices());
    }
}