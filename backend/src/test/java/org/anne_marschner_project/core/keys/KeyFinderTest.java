package org.anne_marschner_project.core.keys;

import de.metanome.algorithm_integration.AlgorithmExecutionException;
import org.anne_marschner_project.core.data.Attribute;
import org.anne_marschner_project.core.data.Relation;
import org.anne_marschner_project.core.data.Type;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class KeyFinderTest {

    @Test
    void testFindKeyIndices() throws AlgorithmExecutionException {

        // Create relation
        Map<Integer, Attribute> schema = Map.of(
                1, new Attribute("Column1", Type.STRING),
                2, new Attribute("Column2", Type.STRING),
                4, new Attribute("Column3", Type.STRING),
                5, new Attribute("Column4", Type.STRING),
                8, new Attribute("Column5", Type.STRING)
        );
        Map<Integer, List<String>> data = Map.of(
                1, List.of("Thomas", "Sarah", "Peter", "Jasmine", "Mike", "Thomas"),
                2, List.of("Miller", "Miller", "Smith", "Cone", "Cone", "Moore"),
                4, List.of("14482", "14482", "60329", "01069", "14482", "60329"),
                5, List.of("Potsdam", "Potsdam", "Frankfurt", "Dresden", "Potsdam", "Frankfurt"),
                8, List.of("Jakobs", "Jakobs", "Feldmann", "Orosz", "Jakobs", "Feldmann")
        );
        Relation relation = new Relation(schema, data);

        // Find Keys
        KeyFinder keyFinder = new KeyFinder();
        List<Integer> keyIndices = keyFinder.findKeyIndices(relation, "relation1");

        // Check keys
        List<Integer> expectedKeys = List.of(1,2);
        assertEquals(expectedKeys, keyIndices);
    }

    @Test
    void testFindKeyIndices_withHeaders() throws AlgorithmExecutionException {

        // Create relation
        Map<Integer, Attribute> schema = Map.of(
                1, new Attribute("id", Type.DOUBLE),
                2, new Attribute("name", Type.STRING),
                4, new Attribute("age", Type.DOUBLE)
        );
        Map<Integer, List<String>> data = Map.of(
                1, List.of("1", "2", "3"),
                2, List.of("Anne", "Anne", "Jonathan"),
                4, List.of("24", "30", "24")
        );
        Relation relation = new Relation(schema, data);

        // Find Keys
        KeyFinder keyFinder = new KeyFinder();
        List<Integer> keyIndices = keyFinder.findKeyIndices(relation, "relation1");

        // Check keys
        List<Integer> expectedKeys = List.of(1);
        assertEquals(expectedKeys, keyIndices);
    }

    @Test
    void testFindKeyIndices_noHeaders() throws AlgorithmExecutionException {

        // Create relation
        Map<Integer, Attribute> schema = Map.of(
                1, new Attribute(null, Type.DOUBLE),
                2, new Attribute(null, Type.STRING),
                4, new Attribute(null, Type.DOUBLE)
        );
        Map<Integer, List<String>> data = Map.of(
                1, List.of("1", "2", "3"),
                2, List.of("Anne", "Anne", "Jonathan"),
                4, List.of("24", "30", "24")
        );
        Relation relation = new Relation(schema, data);

        // Find Keys
        KeyFinder keyFinder = new KeyFinder();
        List<Integer> keyIndices = keyFinder.findKeyIndices(relation, "relation1");

        // Check keys
        List<Integer> expectedKeys = List.of(1);
        assertEquals(expectedKeys, keyIndices);
    }
}