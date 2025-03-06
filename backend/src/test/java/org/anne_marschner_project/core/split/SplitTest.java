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

        // Prepare mock data
        Map<Integer, Attribute> schema = new HashMap<>();
        schema.put(0, new Attribute("ID", Type.DOUBLE));
        schema.put(1, new Attribute("Name", Type.STRING));
        schema.put(2, new Attribute("Age", Type.DOUBLE));

        Map<Integer, List<String>> data = new HashMap<>();

        data.put(0, Arrays.asList("1", "2", "3", "4", "5"));
        data.put(1, Arrays.asList("Anne", "Jonathan", "Mareike", "Luca", "Milena"));
        data.put(2, Arrays.asList("24", "22", "23", "27", "22"));
        Relation sourceRelation = new Relation(schema, data);

        // Test with 50% overlap
        int overlapPercentage = 50;

        Split split = new Split();
        List<Relation> result = split.splitHorizontally(sourceRelation, overlapPercentage, 50);
        Relation leftRelation = result.get(0);
        Relation rightRelation = result.get(1);

        // Check the number of rows in both datasets
        int expectedOverlapRows = (int) Math.ceil(5 * (overlapPercentage / 100.0)); // Expect 3 rows of overlap
        assertEquals(expectedOverlapRows + 1, leftRelation.getData().get(0).size()); // Left should have 4 rows
        assertEquals(expectedOverlapRows + 1, rightRelation.getData().get(0).size()); // Right should have 4 rows

        // Check if number of Overlapping Rows is correct
        assertEquals(leftRelation.getNumOfOverlappingRows(), rightRelation.getNumOfOverlappingRows());
        assertEquals(leftRelation.getNumOfOverlappingRows(), expectedOverlapRows);

        // Check if overlapping Columns is null
        assertNull(leftRelation.getOverlappingColumnsIndices());
        assertNull(rightRelation.getOverlappingColumnsIndices());

        System.out.println(leftRelation.getData().get(0).toString());
        System.out.println(leftRelation.getData().get(1).toString());
        System.out.println(leftRelation.getData().get(2).toString());
        System.out.println(rightRelation.getData().get(0).toString());
        System.out.println(rightRelation.getData().get(1).toString());
    }


    @Test
    void testSplitHorizontally_withZeroOverlap() {

        // Prepare mock data
        Map<Integer, Attribute> schema = new HashMap<>();
        schema.put(0, new Attribute("ID", Type.DOUBLE));
        schema.put(1, new Attribute("Name", Type.STRING));
        schema.put(2, new Attribute("Age", Type.DOUBLE));

        Map<Integer, List<String>> data = new HashMap<>();

        data.put(0, Arrays.asList("1", "2", "3", "4", "5"));
        data.put(1, Arrays.asList("Anne", "Jonathan", "Mareike", "Luca", "Milena"));
        data.put(2, Arrays.asList("24", "22", "23", "27", "22"));
        Relation sourceRelation = new Relation(schema, data);

        // Test with 0% overlap
        int overlapPercentage = 0;

        Split split = new Split();
        List<Relation> result = split.splitHorizontally(sourceRelation, overlapPercentage, 50);
        Relation leftRelation = result.get(0);
        Relation rightRelation = result.get(1);

        // Check the number of rows in both datasets
        assertEquals(3, leftRelation.getData().get(0).size()); // Left should have 3 rows
        assertEquals(2, rightRelation.getData().get(0).size()); // Right should have 2 rows

        // Check if number of Overlapping Rows is correct
        assertEquals(leftRelation.getNumOfOverlappingRows(), rightRelation.getNumOfOverlappingRows());
        assertEquals(leftRelation.getNumOfOverlappingRows(), 0);

        // Check if overlapping Columns is null
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

        // Prepare mock data
        Map<Integer, Attribute> schema = new HashMap<>();
        schema.put(0, new Attribute("ID", Type.DOUBLE));
        schema.put(1, new Attribute("Name", Type.STRING));
        schema.put(2, new Attribute("Age", Type.DOUBLE));

        Map<Integer, List<String>> data = new HashMap<>();

        data.put(0, Arrays.asList("1", "2", "3", "4", "5"));
        data.put(1, Arrays.asList("Anne", "Jonathan", "Mareike", "Luca", "Milena"));
        data.put(2, Arrays.asList("24", "22", "23", "27", "22"));
        Relation sourceRelation = new Relation(schema, data);

        // Test with 100% overlap
        int overlapPercentage = 100;

        Split split = new Split();
        List<Relation> result = split.splitHorizontally(sourceRelation, overlapPercentage, 50);
        Relation leftRelation = result.get(0);
        Relation rightRelation = result.get(1);

        // Check the number of rows in both datasets
        int expectedOverlapRows = (int) Math.ceil(5 * (overlapPercentage / 100.0)); // Expect 5 rows of overlap
        assertEquals(expectedOverlapRows, leftRelation.getData().get(0).size()); // Left should have 5 rows
        assertEquals(expectedOverlapRows, rightRelation.getData().get(0).size()); // Right should have 5 rows

        // Check if number of Overlapping Rows is correct
        assertEquals(leftRelation.getNumOfOverlappingRows(), rightRelation.getNumOfOverlappingRows());
        assertEquals(leftRelation.getNumOfOverlappingRows(), expectedOverlapRows);

        // Check if overlapping Columns is null
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

        // Prepare mock data
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


        // Test with keys and 50% overlap
        Relation sourceRelation = new Relation(schema, data);
        List<Integer> keyIndices = Arrays.asList(0, 1);
        sourceRelation.setKeyIndices(keyIndices); // ID and Name are keys
        int columnOverlap = 50;

        Split split = new Split();
        List<Relation> result = split.splitVertically(sourceRelation, columnOverlap, 50);
        Relation leftRelation = result.get(0);
        Relation rightRelation = result.get(1);

        // Validate that key columns are in both datasets
        assertTrue(leftRelation.getSchema().containsKey(0)); // ID
        assertTrue(leftRelation.getSchema().containsKey(1)); // Name
        assertTrue(rightRelation.getSchema().containsKey(0)); // ID
        assertTrue(rightRelation.getSchema().containsKey(1)); // Name

        // Check that column overlap is correct (50% of non-key columns)
        int totalNonKeyColumns = 3; // Age, Salary, Department (columns 2, 3, 4)
        int expectedOverlapColumns = (int) Math.round(0.5 * totalNonKeyColumns);

        Set<Integer> leftColumnSet = new HashSet<>(leftRelation.getSchema().keySet());
        Set<Integer> rightColumnSet = new HashSet<>(rightRelation.getSchema().keySet());

        leftColumnSet.retainAll(rightColumnSet); // Find the intersection (overlap)
        assertEquals(expectedOverlapColumns + keyIndices.size(), leftColumnSet.size()); // Expect keys + overlap

        // Check that total column count is correct
        assertEquals(5, leftRelation.getSchema().size());  // Total 5 columns
        assertEquals(4, rightRelation.getSchema().size()); // Total 4 columns

        // Check if Overlapping Column Indices are the same and correct size
        assertEquals(leftRelation.getOverlappingColumnsIndices(), rightRelation.getOverlappingColumnsIndices());
        assertEquals(leftRelation.getOverlappingColumnsIndices().size(), expectedOverlapColumns + keyIndices.size());

        // Check if row Overlap is null
        assertNull(leftRelation.getNumOfOverlappingRows());
        assertNull(rightRelation.getNumOfOverlappingRows());

        for (Map.Entry<Integer, List<String>> column : leftRelation.getData().entrySet()) {
            System.out.println(column.toString());
        }
        for (Map.Entry<Integer, List<String>> column : rightRelation.getData().entrySet()) {
            System.out.println(column.toString());
        }
    }


    @Test
    void testSplitVertically_withZeroOverlap() {

        // Prepare mock data (same schema and data as above)
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

        // Test with keys and 0% overlap
        Relation sourceRelation = new Relation(schema, data);
        List<Integer> keyIndices = Arrays.asList(0, 1); // ID and Name are keys
        sourceRelation.setKeyIndices(keyIndices);
        int columnOverlap = 0;

        Split split = new Split();
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

        // Check if Overlapping Column Indices are the same and correct size
        assertEquals(leftRelation.getOverlappingColumnsIndices(), rightRelation.getOverlappingColumnsIndices());
        assertEquals(leftRelation.getOverlappingColumnsIndices().size(), keyIndices.size());

        // Check if row Overlap is null
        assertNull(leftRelation.getNumOfOverlappingRows());
        assertNull(rightRelation.getNumOfOverlappingRows());

        for (Map.Entry<Integer, List<String>> column : leftRelation.getData().entrySet()) {
            System.out.println(column.toString());
        }
        for (Map.Entry<Integer, List<String>> column : rightRelation.getData().entrySet()) {
            System.out.println(column.toString());
        }

    }


    @Test
    void testSplitVertically_withFullOverlap() {
        // Prepare mock data (same schema and data as above)
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

        // Test with keys and 100% overlap
        Relation sourceRelation = new Relation(schema, data);
        List<Integer> keyIndices = Arrays.asList(0, 1); // ID and Name are keys
        sourceRelation.setKeyIndices(keyIndices);
        int columnOverlap = 100;

        Split split = new Split();
        List<Relation> result = split.splitVertically(sourceRelation, columnOverlap, 50);
        Relation leftRelation = result.get(0);
        Relation rightRelation = result.get(1);

        // Validate that all columns are in both datasets (since columnOverlap is 100%)
        assertEquals(sourceRelation.getSchema().size(), leftRelation.getSchema().size());
        assertEquals(sourceRelation.getSchema().size(), rightRelation.getSchema().size());

        // Validate that the data is correctly distributed
        assertEquals(sourceRelation.getData().size(), leftRelation.getData().size());
        assertEquals(sourceRelation.getData().size(), rightRelation.getData().size());

        // Check if Overlapping Column Indices are the same and correct size
        assertEquals(leftRelation.getOverlappingColumnsIndices(), rightRelation.getOverlappingColumnsIndices());
        assertEquals(leftRelation.getOverlappingColumnsIndices().size(), sourceRelation.getSchema().size());

        // Check if row Overlap is null
        assertNull(leftRelation.getNumOfOverlappingRows());
        assertNull(rightRelation.getNumOfOverlappingRows());

        for (Map.Entry<Integer, List<String>> column : leftRelation.getData().entrySet()) {
            System.out.println(column.toString());
        }
        for (Map.Entry<Integer, List<String>> column : rightRelation.getData().entrySet()) {
            System.out.println(column.toString());
        }
    }


    @Test
    void testSplitVertically_withDistribution() {

        // Prepare mock data
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


        // Test with keys and 50% overlap
        Relation sourceRelation = new Relation(schema, data);
        List<Integer> keyIndices = Arrays.asList(0);
        sourceRelation.setKeyIndices(keyIndices); // ID and Name are keys
        int columnOverlap = 30;

        Split split = new Split();
        List<Relation> result = split.splitVertically(sourceRelation, columnOverlap, 100);
        Relation leftRelation = result.get(0);
        Relation rightRelation = result.get(1);

        // Validate that key columns are in both datasets
        assertTrue(leftRelation.getSchema().containsKey(0)); // ID
        assertTrue(rightRelation.getSchema().containsKey(0)); // ID

        // Check that column overlap is correct (50% of non-key columns)
        int totalNonKeyColumns = 4; // Age, Salary, Department (columns 2, 3, 4)
        int expectedOverlapColumns = (int) Math.round(0.3 * totalNonKeyColumns);

        Set<Integer> leftColumnSet = new HashSet<>(leftRelation.getSchema().keySet());
        Set<Integer> rightColumnSet = new HashSet<>(rightRelation.getSchema().keySet());

        leftColumnSet.retainAll(rightColumnSet); // Find the intersection (overlap)
        assertEquals(expectedOverlapColumns + keyIndices.size(), leftColumnSet.size()); // Expect keys + overlap

        // Check that total column count is correct
        assertEquals(5, leftRelation.getSchema().size());
        assertEquals(2, rightRelation.getSchema().size());

        // Check if Overlapping Column Indices are the same and correct size
        assertEquals(leftRelation.getOverlappingColumnsIndices(), rightRelation.getOverlappingColumnsIndices());
        assertEquals(leftRelation.getOverlappingColumnsIndices().size(), expectedOverlapColumns + keyIndices.size());

        // Check if row Overlap is null
        assertNull(leftRelation.getNumOfOverlappingRows());
        assertNull(rightRelation.getNumOfOverlappingRows());

        for (Map.Entry<Integer, List<String>> column : leftRelation.getData().entrySet()) {
            System.out.println(column.toString());
        }
        for (Map.Entry<Integer, List<String>> column : rightRelation.getData().entrySet()) {
            System.out.println(column.toString());
        }
    }

    @Test
    void testSplitHorizontally_withDistribution() {

        // Prepare mock data
        Map<Integer, Attribute> schema = new HashMap<>();
        schema.put(0, new Attribute("ID", Type.DOUBLE));
        schema.put(1, new Attribute("Name", Type.STRING));
        schema.put(2, new Attribute("Age", Type.DOUBLE));

        Map<Integer, List<String>> data = new HashMap<>();

        data.put(0, Arrays.asList("1", "2", "3", "4", "5"));
        data.put(1, Arrays.asList("Anne", "Jonathan", "Mareike", "Luca", "Milena"));
        data.put(2, Arrays.asList("24", "22", "23", "27", "22"));
        Relation sourceRelation = new Relation(schema, data);

        // Test with 50% overlap
        int overlapPercentage = 20;

        Split split = new Split();
        List<Relation> result = split.splitHorizontally(sourceRelation, overlapPercentage, 30);
        Relation leftRelation = result.get(0);
        Relation rightRelation = result.get(1);

        // Check the number of rows in both datasets
        int expectedOverlapRows = (int) Math.round(5 * (overlapPercentage / 100.0)); // Expect 2 rows of overlap
        assertEquals(expectedOverlapRows + 1, leftRelation.getData().get(0).size()); // Left should have 4 rows
        assertEquals(expectedOverlapRows + 3, rightRelation.getData().get(0).size()); // Right should have 4 rows

        // Check if number of Overlapping Rows is correct
        assertEquals(leftRelation.getNumOfOverlappingRows(), rightRelation.getNumOfOverlappingRows());
        assertEquals(leftRelation.getNumOfOverlappingRows(), expectedOverlapRows);

        // Check if overlapping Columns is null
        assertNull(leftRelation.getOverlappingColumnsIndices());
        assertNull(rightRelation.getOverlappingColumnsIndices());

        System.out.println(leftRelation.getData().get(0).toString());
        System.out.println(leftRelation.getData().get(1).toString());
        System.out.println(leftRelation.getData().get(2).toString());
        System.out.println(rightRelation.getData().get(0).toString());
        System.out.println(rightRelation.getData().get(1).toString());
    }

    @Test
    void testSplitHorizontallyWithMixedOverlap() {

        // Prepare mock data
        Map<Integer, Attribute> schema = new HashMap<>();
        schema.put(0, new Attribute("ID", Type.DOUBLE));
        schema.put(1, new Attribute("Name", Type.STRING));
        schema.put(2, new Attribute("Age", Type.DOUBLE));

        Map<Integer, List<String>> data = new HashMap<>();

        data.put(0, Arrays.asList("1", "2", "3", "4", "5"));
        data.put(1, Arrays.asList("Anne", "Jonathan", "Mareike", "Luca", "Milena"));
        data.put(2, Arrays.asList("24", "22", "23", "27", "22"));
        Relation sourceRelation = new Relation(schema, data);

        // Test with 50% overlap
        int overlapPercentage = 50;

        Split split = new Split();
        List<Relation> result = split.splitHorizontallyWithMixedOverlap(sourceRelation, overlapPercentage, 10);
        Relation leftRelation = result.get(0);
        Relation rightRelation = result.get(1);

        // Check the number of rows in both datasets
        int expectedOverlapRows = (int) Math.round(5 * (overlapPercentage / 100.0));
        assertEquals(expectedOverlapRows + 0, leftRelation.getData().get(0).size());
        assertEquals(expectedOverlapRows + 2, rightRelation.getData().get(0).size());

        // Check if number of Overlapping Rows is correct
        assertEquals(leftRelation.getNumOfOverlappingRows(), rightRelation.getNumOfOverlappingRows());
        assertEquals(leftRelation.getNumOfOverlappingRows(), expectedOverlapRows);

        // Check if overlapping Columns is null
        assertNull(leftRelation.getOverlappingColumnsIndices());
        assertNull(rightRelation.getOverlappingColumnsIndices());

        System.out.println(leftRelation.getData().get(0).toString());
        System.out.println(leftRelation.getData().get(1).toString());
        System.out.println(leftRelation.getData().get(2).toString());
        System.out.println(rightRelation.getData().get(0).toString());
        System.out.println(rightRelation.getData().get(1).toString());
    }
}