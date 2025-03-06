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

        Normalization normalization = new Normalization();
        List<Relation> allRelations = normalization.transformToBCNF(relation, ',', '"',0);

        // Check if two datasets are returned
        assertEquals(1, allRelations.size(), "There should be one relations");

        // Check first relation
        Relation relation1 = allRelations.get(0);
        List<List<Object>> expectedData1 = Arrays.asList(
                Arrays.asList("Thomas", "Sarah", "Peter", "Jasmine", "Mike", "Thomas"),
                Arrays.asList("Miller", "Miller", "Smith", "Cone", "Cone", "Moore"),
                Arrays.asList("14482", "14482", "60329", "01069", "14482", "60329"),
                Arrays.asList("Potsdam", "Potsdam", "Frankfurt", "Dresden", "Potsdam", "Frankfurt"),
                Arrays.asList("Jakobs", "Jakobs", "Feldmann", "Orosz", "Jakobs", "Feldmann")
        );

        assertEquals(2, relation1.getNumOfOverlappingRows());
        assertNull(relation1.getOverlappingColumnsIndices());
        assertEquals(expectedData1.get(0), relation1.getData().get(1), "The data of the first relation is wrong.");
        assertEquals(expectedData1.get(1), relation1.getData().get(2), "The data of the first relation is wrong.");
        assertEquals(expectedData1.get(2), relation1.getData().get(4), "The data of the first relation is wrong.");
        assertEquals(expectedData1.get(3), relation1.getData().get(5), "The data of the second relation is wrong.");
        assertEquals(expectedData1.get(4), relation1.getData().get(8), "The data of the second relation is wrong.");
        System.out.println(relation1.getData().get(1));
        System.out.println(relation1.getData().get(2));
        System.out.println(relation1.getData().get(4));
        System.out.println(relation1.getData().get(5));
        System.out.println(relation1.getData().get(8));
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

        Normalization normalization = new Normalization();
        List<Relation> allRelations = normalization.transformToBCNF(relation, ',', '"',50);

        // Check if two datasets are returned
        assertEquals(2, allRelations.size(), "There should be two relations");

        // Check first relation
        Relation relation1 = allRelations.get(0);
        List<List<Object>> expectedData1 = Arrays.asList(
                Arrays.asList("Thomas", "Sarah", "Peter", "Jasmine", "Mike", "Thomas"),
                Arrays.asList("Miller", "Miller", "Smith", "Cone", "Cone", "Moore"),
                Arrays.asList("14482", "14482", "60329", "01069", "14482", "60329")
        );

        assertEquals(2, relation1.getNumOfOverlappingRows());
        assertNull(relation1.getOverlappingColumnsIndices());
        assertEquals(expectedData1.get(0), relation1.getData().get(1), "The data of the first relation is wrong.");
        assertEquals(expectedData1.get(1), relation1.getData().get(2), "The data of the first relation is wrong.");
        assertEquals(expectedData1.get(2), relation1.getData().get(4), "The data of the first relation is wrong.");
        System.out.println(relation1.getData().get(1));
        System.out.println(relation1.getData().get(2));
        System.out.println(relation1.getData().get(4));

        // Check second relation
        Relation relation2 = allRelations.get(1);
        List<List<Object>> expectedData2 = Arrays.asList(
                Arrays.asList("14482", "60329", "01069"),
                Arrays.asList("Potsdam", "Frankfurt", "Dresden"),
                Arrays.asList("Jakobs", "Feldmann", "Orosz")
        );

        assertEquals(1, relation2.getNumOfOverlappingRows());
        assertNull(relation1.getOverlappingColumnsIndices());
        assertEquals(expectedData2.get(0), relation2.getData().get(4), "The data of the second relation is wrong.");
        assertEquals(expectedData2.get(1), relation2.getData().get(5), "The data of the second relation is wrong.");
        assertEquals(expectedData2.get(2), relation2.getData().get(8), "The data of the second relation is wrong.");
        System.out.println(relation2.getData().get(4));
        System.out.println(relation2.getData().get(5));
        System.out.println(relation2.getData().get(8));
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

        Normalization normalization = new Normalization();
        List<Relation> allRelations = normalization.transformToBCNF(relation, ',', '"',100);

        // Check if two datasets are returned
        assertEquals(2, allRelations.size(), "There should be two relations");

        // Check first relation
        Relation relation1 = allRelations.get(0);
        List<List<Object>> expectedData1 = Arrays.asList(
                Arrays.asList("Thomas", "\"Sarah, Lea\"", "Peter", "Jasmine", "Mike", "Thomas"),
                Arrays.asList("Miller", "Miller", "Smith", "Cone", "Cone", "Moore"),
                Arrays.asList("14482", "14482", "60329", "01069", "14482", "60329")
        );

        assertEquals(new ArrayList<>(Arrays.asList(1,2)), relation1.getOverlappingColumnsIndices());
        assertNull(relation1.getNumOfOverlappingRows());
        assertEquals(expectedData1.get(0), relation1.getData().get(1), "The data of the first relation is wrong.");
        assertEquals(expectedData1.get(1), relation1.getData().get(2), "The data of the first relation is wrong.");
        assertEquals(expectedData1.get(2), relation1.getData().get(4), "The data of the first relation is wrong.");
        System.out.println(relation1.getData().get(1));
        System.out.println(relation1.getData().get(2));
        System.out.println(relation1.getData().get(4));

        // Check second relation
        Relation relation2 = allRelations.get(1);
        List<List<Object>> expectedData2 = Arrays.asList(
                Arrays.asList("14482", "60329", "01069"),
                Arrays.asList("Potsdam", "Frankfurt", "Dresden"),
                Arrays.asList("Jakobs", "Feldmann", "Orosz")
        );

        assertEquals(new ArrayList<>(List.of(5)), relation2.getOverlappingColumnsIndices());
        assertNull(relation2.getNumOfOverlappingRows());
        assertEquals(expectedData2.get(0), relation2.getData().get(4), "The data of the second relation is wrong.");
        assertEquals(expectedData2.get(1), relation2.getData().get(5), "The data of the second relation is wrong.");
        assertEquals(expectedData2.get(2), relation2.getData().get(8), "The data of the second relation is wrong.");
        System.out.println(relation2.getData().get(4));
        System.out.println(relation2.getData().get(5));
        System.out.println(relation2.getData().get(8));
    }
}