package org.anne_marschner_project.core.structure;

import org.anne_marschner_project.core.data.Attribute;
import org.anne_marschner_project.core.data.Relation;
import org.anne_marschner_project.core.data.Type;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MergeTest {

    @Test
    void testMergeColumns_withHeaders() {

        // Create relation
        Map<Integer, Attribute> schema = new HashMap<>();
        Map<Integer, List<String>> data = new HashMap<>();
        schema.put(2, new Attribute("Column1", Type.STRING));
        schema.put(5, new Attribute("Column2", Type.DOUBLE));
        schema.put(6, new Attribute("Column3", Type.STRING));
        data.put(2, Arrays.asList("A", "B", "C"));
        data.put(5, Arrays.asList("1", "2", "3"));
        data.put(6, Arrays.asList("X", "Y", "Z"));
        Relation relation = new Relation(schema, data);

        // Execute merge
        Merge merge = new Merge();
        Relation result = merge.mergeColumns(relation, 50, ',');

        // Check schema size
        assertEquals(2, result.getSchema().size(), "Schema should have 2 Column");

        // Check merged attribute name
        Attribute mergedAttribute = result.getSchema().get(2);
        assertEquals("Column1;Column3", mergedAttribute.getColumnName(), "Attribute name is not correct");

        // Check merged data
        List<String> mergedData = result.getData().get(2);
        assertEquals(Arrays.asList("A;X", "B;Y", "C;Z"), mergedData, "Data names not correct");

        // Check if third row was removed
        assertFalse(result.getData().containsKey(6), "The third column should have been removed");
    }

    @Test
    void testMergeColumns_withoutHeaders() {

        // Create relation
        Map<Integer, Attribute> schema = new HashMap<>();
        Map<Integer, List<String>> data = new HashMap<>();
        schema.put(2, new Attribute(null, Type.STRING));
        schema.put(5, new Attribute(null, Type.DOUBLE));
        data.put(2, Arrays.asList("A", "B", "C"));
        data.put(5, Arrays.asList("1", "2", "3"));
        Relation relation = new Relation(schema, data);

        // Execute Merge
        Merge merge = new Merge();
        Relation result = merge.mergeColumns(relation, 50, ',');

        // Check schema size
        assertEquals(1, result.getSchema().size(), "Schema should have 1 Column");

        // Check merged attribute name
        Attribute mergedAttribute = result.getSchema().get(2);
        assertNull(mergedAttribute.getColumnName(), "Attribute name is not correct");

        // Check merged data
        List<String> mergedData = result.getData().get(2);
        assertEquals(Arrays.asList("A;1", "B;2", "C;3"), mergedData, "Data names not correct");

        // Check if second row was removed
        assertFalse(result.getData().containsKey(5), "The second column should have been removed");
    }
}