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

        Relation relation = new Relation(schema, data);

        // Find Keys
        KeyFinder keyFinder = new KeyFinder();
        List<Integer> keyIndices = keyFinder.findKeyIndices(relation, "relation1");
        for(int key: keyIndices) {
            System.out.println(key);
        }
        assertEquals(1, keyIndices.get(0));
    }

    @Test
    void testFindKeyIndices_withHeaders() throws AlgorithmExecutionException {

        // Create relation
        Map<Integer, Attribute> schema = new HashMap<>();
        schema.put(1, new Attribute("id", Type.DOUBLE));
        schema.put(2, new Attribute("name", Type.STRING));
        schema.put(4, new Attribute("age", Type.DOUBLE));
        Map<Integer, List<String>> data = new HashMap<>();
        data.put(1, List.of("1", "2", "3"));
        data.put(2, List.of("Anne", "Anne", "Jonathan"));
        data.put(4, List.of("24", "30", "24"));
        Relation relation = new Relation(schema, data);

        // Find Keys
        KeyFinder keyFinder = new KeyFinder();
        List<Integer> keyIndices = keyFinder.findKeyIndices(relation, "relation1");
        for(int key: keyIndices) {
            System.out.println(key);
        }
        assertEquals(1, keyIndices.get(0));
    }

    @Test
    void testFindKeyIndices_noHeaders() throws AlgorithmExecutionException {

        // Create relation
        Map<Integer, Attribute> schema = new HashMap<>();
        schema.put(1, new Attribute(null, Type.DOUBLE));
        schema.put(2, new Attribute(null, Type.STRING));
        schema.put(4, new Attribute(null, Type.DOUBLE));
        Map<Integer, List<String>> data = new HashMap<>();
        data.put(1, List.of("1", "2", "3"));
        data.put(2, List.of("Anne", "Anne", "Jonathan"));
        data.put(4, List.of("24", "30", "24"));
        Relation relation = new Relation(schema, data);

        // Find Keys
        KeyFinder keyFinder = new KeyFinder();
        List<Integer> keyIndices = keyFinder.findKeyIndices(relation, "relation1");
        for(int key: keyIndices) {
            System.out.println(key);
        }
        assertEquals(1, keyIndices.get(0));
    }
}