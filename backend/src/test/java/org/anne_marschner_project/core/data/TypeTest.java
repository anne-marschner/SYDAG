package org.anne_marschner_project.core.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;


class TypeTest {

    @Test
    void testDetermineType_DOUBLE() {

        // Test a list with double values
        List<String> values = Arrays.asList("1", "23", "456.7", "Hello");
        Assertions.assertEquals(Type.DOUBLE, Type.determineType(values));
    }

    @Test
    void testDetermineType_STRING() {

        // Test a list with only string values
        List<String> values = Arrays.asList("Hello", "1.23", "45", "1", "99.99", "20-03-2024");
        Assertions.assertEquals(Type.STRING, Type.determineType(values));
    }

}