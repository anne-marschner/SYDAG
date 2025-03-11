package org.anne_marschner_project.core.structure;

import de.metanome.algorithm_integration.AlgorithmExecutionException;
import org.anne_marschner_project.core.data.Attribute;
import org.anne_marschner_project.core.data.Relation;
import org.anne_marschner_project.core.data.Type;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class NormalizationTest {

    @Test
    void testTransformToBCNF_NormalizePercentage() throws IOException, AlgorithmExecutionException {

        // Create relation
        Map<Integer, Attribute> schema = new HashMap<>();
        Map<Integer, List<String>> data = new HashMap<>();
        schema.put(1, new Attribute("Column1", Type.STRING));
        schema.put(2, new Attribute("Column2", Type.STRING));
        schema.put(4, new Attribute("Column3", Type.STRING));
        schema.put(5, new Attribute("Column4", Type.STRING));
        schema.put(8, new Attribute("Column5", Type.STRING));
        data.put(1, Arrays.asList("Thomas", "Sarah", "Peter", "Jasmine", "Mike", "Thomas"));
        data.put(2, Arrays.asList("Miller", "Miller", "Smith", "Cone", "Cone", "Moore"));
        data.put(4, Arrays.asList("14482", "14482", "60329", "01069", "14482", "60329"));
        data.put(5, Arrays.asList("Potsdam", "Potsdam", "Frankfurt", "Dresden", "Potsdam", "Frankfurt"));
        data.put(8, Arrays.asList("Jakobs", "Jakobs", "Feldmann", "Orosz", "Jakobs", "Feldmann"));
        List<Integer> keyIndices = new ArrayList<>(Arrays.asList(1,2));
        Integer numOverlappingColumns = 2;
        Relation relation = new Relation(schema, data, keyIndices, numOverlappingColumns);

        // Execute normalization
        Normalization normalization = new Normalization();
        List<Relation> allRelations = normalization.transformToBCNF(relation, ',', '"', 0);

        // Check if one dataset is returned
        assertEquals(1, allRelations.size(), "There should be exactly one relation after normalization.");

        // Get expected data and real data
        Map<Integer, List<String>> expectedData = Map.of(
                1, List.of("Thomas", "Sarah", "Peter", "Jasmine", "Mike", "Thomas"),
                2, List.of("Miller", "Miller", "Smith", "Cone", "Cone", "Moore"),
                4, List.of("14482", "14482", "60329", "01069", "14482", "60329"),
                5, List.of("Potsdam", "Potsdam", "Frankfurt", "Dresden", "Potsdam", "Frankfurt"),
                8, List.of("Jakobs", "Jakobs", "Feldmann", "Orosz", "Jakobs", "Feldmann")
        );
        Relation relation1 = allRelations.get(0);

        // Check number of overlapping rows and columns
        assertEquals(2, relation1.getNumOfOverlappingRows(), "Number of overlapping rows is incorrect.");
        assertNull(relation1.getOverlappingColumnsIndices(), "Overlapping columns should be null.");

        // Compare data
        for (Integer key : expectedData.keySet()) {
            assertEquals(expectedData.get(key), relation1.getData().get(key),
                    "Mismatch in data for column: " + key);
        }
    }

    @Test
    void testTransformToBCNF_HorizontalSplit() throws IOException, AlgorithmExecutionException {

        // Create relation
        Map<Integer, Attribute> schema = new HashMap<>();
        Map<Integer, List<String>> data = new HashMap<>();
        schema.put(1, new Attribute("Column1", Type.STRING));
        schema.put(2, new Attribute("Column2", Type.STRING));
        schema.put(4, new Attribute("Column3", Type.STRING));
        schema.put(5, new Attribute("Column4", Type.STRING));
        schema.put(8, new Attribute("Column5", Type.STRING));
        data.put(1, Arrays.asList("Thomas", "Sarah", "Peter", "Jasmine", "Mike", "Thomas"));
        data.put(2, Arrays.asList("Miller", "Miller", "Smith", "Cone", "Cone", "Moore"));
        data.put(4, Arrays.asList("14482", "14482", "60329", "01069", "14482", "60329"));
        data.put(5, Arrays.asList("Potsdam", "Potsdam", "Frankfurt", "Dresden", "Potsdam", "Frankfurt"));
        data.put(8, Arrays.asList("Jakobs", "Jakobs", "Feldmann", "Orosz", "Jakobs", "Feldmann"));
        List<Integer> keyIndices = new ArrayList<>(Arrays.asList(1,2));
        Integer numOverlappingColumns = 2;
        Relation relation = new Relation(schema, data, keyIndices, numOverlappingColumns);

        // Execute normalization
        Normalization normalization = new Normalization();
        List<Relation> allRelations = normalization.transformToBCNF(relation, ',', '"', 50);

        // Check if two datasets are returned
        assertEquals(2, allRelations.size(), "There should be exactly two relations after normalization.");

        // Check first relation
        Relation relation1 = allRelations.get(0);
        Map<Integer, List<String>> expectedData1 = Map.of(
                1, List.of("Thomas", "Sarah", "Peter", "Jasmine", "Mike", "Thomas"),
                2, List.of("Miller", "Miller", "Smith", "Cone", "Cone", "Moore"),
                4, List.of("14482", "14482", "60329", "01069", "14482", "60329")
        );
        assertEquals(2, relation1.getNumOfOverlappingRows(), "Number of overlapping rows in first relation is incorrect.");
        assertNull(relation1.getOverlappingColumnsIndices(), "Overlapping columns in first relation should be null.");
        for (Integer key : expectedData1.keySet()) {
            assertEquals(expectedData1.get(key), relation1.getData().get(key),
                    "Mismatch in first relation data for column: " + key);
        }

        // Check second relation
        Relation relation2 = allRelations.get(1);
        Map<Integer, List<String>> expectedData2 = Map.of(
                4, List.of("14482", "60329", "01069"),
                5, List.of("Potsdam", "Frankfurt", "Dresden"),
                8, List.of("Jakobs", "Feldmann", "Orosz")
        );
        assertEquals(1, relation2.getNumOfOverlappingRows(), "Number of overlapping rows in second relation is incorrect.");
        assertNull(relation2.getOverlappingColumnsIndices(), "Overlapping columns in second relation should be null.");
        for (Integer key : expectedData2.keySet()) {
            assertEquals(expectedData2.get(key), relation2.getData().get(key),
                    "Mismatch in second relation data for column: " + key);
        }
    }

    @Test
    void testTransformToBCNF_VerticalSplit() throws IOException, AlgorithmExecutionException {

        // Create relation
        Map<Integer, Attribute> schema = new HashMap<>();
        Map<Integer, List<String>> data = new HashMap<>();
        schema.put(1, new Attribute("Column1", Type.STRING));
        schema.put(2, new Attribute("Column2", Type.STRING));
        schema.put(4, new Attribute("Column3", Type.STRING));
        schema.put(5, new Attribute("Column4", Type.STRING));
        schema.put(8, new Attribute("Column5", Type.STRING));
        data.put(1, Arrays.asList("Thomas", "\"Sarah, Lea\"", "Peter", "Jasmine", "Mike", "Thomas"));
        data.put(2, Arrays.asList("Miller", "Miller", "Smith", "Cone", "Cone", "Moore"));
        data.put(4, Arrays.asList("14482", "14482", "60329", "01069", "14482", "60329"));
        data.put(5, Arrays.asList("Potsdam", "Potsdam", "Frankfurt", "Dresden", "Potsdam", "Frankfurt"));
        data.put(8, Arrays.asList("Jakobs", "Jakobs", "Feldmann", "Orosz", "Jakobs", "Feldmann"));
        List<Integer> keyIndices = new ArrayList<>(Arrays.asList(1,2));
        List<Integer> overlappingColumnIndices = new ArrayList<>(Arrays.asList(1,2,5));
        Relation relation = new Relation(schema, data, keyIndices, overlappingColumnIndices);

        // Execute Normalization
        Normalization normalization = new Normalization();
        List<Relation> allRelations = normalization.transformToBCNF(relation, ',', '"',100);

        // Check if two datasets are returned
        assertEquals(2, allRelations.size(), "There should be exactly two relations after normalization.");

        // Check first relation
        Relation relation1 = allRelations.get(0);
        Map<Integer, List<String>> expectedData1 = Map.of(
                1, List.of("Thomas", "\"Sarah, Lea\"", "Peter", "Jasmine", "Mike", "Thomas"),
                2, List.of("Miller", "Miller", "Smith", "Cone", "Cone", "Moore"),
                4, List.of("14482", "14482", "60329", "01069", "14482", "60329")
        );
        assertEquals(List.of(1, 2), relation1.getOverlappingColumnsIndices(),
                "Overlapping columns in first relation are incorrect.");
        assertNull(relation1.getNumOfOverlappingRows(),
                "Number of overlapping rows in first relation should be null.");
        for (Integer key : expectedData1.keySet()) {
            assertEquals(expectedData1.get(key), relation1.getData().get(key),
                    "Mismatch in first relation data for column: " + key);
        }

        // Check second relation
        Relation relation2 = allRelations.get(1);
        Map<Integer, List<String>> expectedData2 = Map.of(
                4, List.of("14482", "60329", "01069"),
                5, List.of("Potsdam", "Frankfurt", "Dresden"),
                8, List.of("Jakobs", "Feldmann", "Orosz")
        );
        assertEquals(List.of(5), relation2.getOverlappingColumnsIndices(),
                "Overlapping columns in second relation are incorrect.");
        assertNull(relation2.getNumOfOverlappingRows(),
                "Number of overlapping rows in second relation should be null.");
        for (Integer key : expectedData2.keySet()) {
            assertEquals(expectedData2.get(key), relation2.getData().get(key),
                    "Mismatch in second relation data for column: " + key);
        }
    }
}