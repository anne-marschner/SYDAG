package org.anne_marschner_project.core.noise;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NoiseTest {

    @Test
    void testGenerateRandomString() {
        SchemaNoise schemaNoise = new SchemaNoise();
        String randomString = schemaNoise.generateRandomString();
        assertNotNull(randomString, "Random string should not be null");
        assertTrue(!randomString.isEmpty() && randomString.length() <= 10, "Random string length should be between 1 and 10");
        assertTrue(randomString.matches("[A-Za-z0-9]+"), "Random string should only contain alphanumeric characters");
    }

    @Test
    void testRemoveVowels() {
        SchemaNoise schemaNoise = new SchemaNoise();
        String input = "Column Name";
        String result = schemaNoise.removeVowels(input);
        assertEquals("Clmn Nm", result, "Vowels should be removed from the string");
    }

    @Test
    void testAbbreviateFirstLetters() {
        SchemaNoise schemaNoise = new SchemaNoise();
        String input = "this is a column_name";
        String result = schemaNoise.abbreviateFirstLetters(input);
        assertEquals("T.I.A.C.N.", result, "First letters of each word should be concatenated and separated by dots");
    }

    @Test
    void testAbbreviateRandomLength() {
        SchemaNoise schemaNoise = new SchemaNoise();
        String input = "this is a column_name";
        String result = schemaNoise.abbreviateRandomLength(input);
        assertTrue(result.length() <= input.length(), "Abbreviation should not exceed the original string length");
    }

    @Test
    void testAddRandomPrefix() {
        SchemaNoise schemaNoise = new SchemaNoise();
        String input = "ColumnName";
        String result = schemaNoise.addRandomPrefix(input);
        assertTrue(result.matches("[A-Za-z0-9]{1,4}_ColumnName"), "The prefix should be 1 to 4 alphanumeric characters followed by '_ColumnName'");
    }

    @Test
    void testShuffleLetters() {
        SchemaNoise schemaNoise = new SchemaNoise();
        String input = "Column Name";
        String result = schemaNoise.shuffleLetters(input);
        assertNotEquals(input, result, "Shuffled string should not be the same as the original string");
        assertEquals(result.length(), input.length(), "Shuffled string should have the same length as the original");
        assertArrayEquals(result.chars().sorted().toArray(), input.chars().sorted().toArray(), "Shuffled string should contain the same characters as the original");
    }

    @Test
    void testShuffleWords() {
        SchemaNoise schemaNoise = new SchemaNoise();
        String input = "this is a column_name";
        String result = schemaNoise.shuffleWords(input);
        assertNotEquals(input, result, "Words should be shuffled and not in the same order as the original");
        assertEquals(input.length(), result.length(), "Shuffled result should have the same length as the original string");
        // Check if all words are included
        String[] originalWords = input.split("[\\s-_]+");
        String[] shuffledWords = result.split("[\\s-_]+");
        Arrays.sort(originalWords);
        Arrays.sort(shuffledWords);
        assertArrayEquals(originalWords, shuffledWords, "Shuffled result should contain the same words as the original");
    }

    @Test
    void testGetSynonymFromAPI() {
        SchemaNoise schemaNoise = new SchemaNoise();
        String columName = "number";
        String response = schemaNoise.getSynonymFromAPI(columName);
        assertEquals("identification number", response);
    }

    @Test
    void testReplaceWithSynonyms() throws Exception {
        SchemaNoise schemaNoise = new SchemaNoise();
        String columName = "salary person";
        String response = schemaNoise.replaceWithSynonyms(columName);
        assertEquals("earnings somebody", response);
    }

    @Test
    void testReplaceWithTranslation() throws Exception {
        SchemaNoise schemaNoise = new SchemaNoise();
        String columName = "phone number";
        String response = schemaNoise.replaceWithTranslation(columName, "en", "de");
        assertEquals("Telefonnummer", response);
    }

    @Test
    void testGenerateMissingValue() {
        DataNoise dataNoise = new DataNoise();
        String entry = "dance";
        String result = dataNoise.generateMissingValue(entry);
        assertEquals("", result);
    }

    @Test
    void testGeneratePhoneticError() {
        DataNoise dataNoise = new DataNoise();
        String entry = "Fun";
        String result = dataNoise.generatePhoneticError(entry);
        assertEquals("Vun", result);
    }

    @Test
    void testGenerateOCRError() {
        DataNoise dataNoise = new DataNoise();
        String entry = "Sand";
        String result = dataNoise.generateOCRError(entry);
        assertEquals("5and", result);
    }

    @Test
    void testAbbreviateDataEntry() {
        DataNoise dataNoise = new DataNoise();
        String entry = "Number person";
        String result = dataNoise.abbreviateDataEntry(entry);
        assertNotEquals("Number person", result);
        assertNotEquals(entry.length(), result.length());
    }

    @Test
    void testChangeFormat_forDate() {
        DataNoise dataNoise = new DataNoise();
        String entry = "12-08-24";
        String result = dataNoise.changeFormat(entry);
        assert result.equals("12 08 24") || result.equals("12.08.24") || result.equals("12_08_24") : "The format should have been changed";
    }

    @Test
    void testChangeFormat_forName() {
        DataNoise dataNoise = new DataNoise();
        String entry = "A.N. Maler";
        String result = dataNoise.changeFormat(entry);
        assert result.equals("A-N- Maler") || result.equals("A_N_ Maler") || result.equals("A N  Maler") : "The format should have been changed";
    }

    @Test
    void testChangeFormat_forDifferentSymbols() {
        DataNoise dataNoise = new DataNoise();
        String entry = "A.N. Maler-Vogt";
        String result = dataNoise.changeFormat(entry);
        assert result.equals("A-N- Maler-Vogt") || result.equals("A_N_ Maler-Vogt") || result.equals("A N  Maler-Vogt") : "The format should have been changed";
    }

    @Test
    void testChangeValue() {
        DataNoise dataNoise = new DataNoise();
        String entry = "20";
        List<String> column = new ArrayList<>(Arrays.asList("10.0", "20.0", "30.0", "40.0", "50.0"));
        List<Double> numericColumn = dataNoise.createListOfDouble(column);

        // Calculate mean and standard deviation of the list
        double mean = dataNoise.calculateMean(numericColumn);
        double standardDeviation = dataNoise.calculateStandardDeviation(numericColumn, mean);

        // Execute method
        String result = dataNoise.changeValue(entry, mean, standardDeviation);

        // Test that new value is different from entry
        assertNotEquals(entry, result, "New value should be different.");

        // Test if result is numeric
        assertDoesNotThrow(() -> Double.parseDouble(result), "Result should be String of numeric value.");

        // Test if value is in realistic range
        double resultValue = Double.parseDouble(result);
        double min = 0.0;
        double max = 100.0;
        assertTrue(resultValue >= min && resultValue <= max, "The value should be in the correct range.");
    }


    @Test
    void testChangeValueToOutlier() {
        DataNoise dataNoise = new DataNoise();
        String entry = "20";
        List<String> column = new ArrayList<>(Arrays.asList("10.0", "20.0", "30.0", "40.0", "50.0"));
        List<Double> numericColumn = dataNoise.createListOfDouble(column);

        // Calculate mean and standard deviation of the list
        double mean = dataNoise.calculateMean(numericColumn);
        double standardDeviation = dataNoise.calculateStandardDeviation(numericColumn, mean);

        // Execute method
        String result = dataNoise.changeValueToOutlier(entry, mean, standardDeviation);

        // Test that new Value is different from entry
        assertNotEquals(entry, result, "New value should be different.");

        // Test if result is numeric values
        assertDoesNotThrow(() -> Double.parseDouble(result), "Result should be String of numeric value.");

        // Test if value is significantly outside realistic range
        double resultValue = Double.parseDouble(result);
        double threshold = 2 * 10.0;  // Differs more than 2 standard deviations
        assertTrue(Math.abs(resultValue - mean) > threshold, "The value should differ significantly from mean.");
    }


    @Test
    void testGenerateTypingError() {
        DataNoise dataNoise = new DataNoise();
        String entry = "A. MÜLLER-Leiler";
        String result = dataNoise.generateTypingError(entry);
        assertEquals(result.length(), entry.length());

        // Test the number of different characters in entry and result
        int differences = 0;
        for (int i = 0; i < entry.length(); i++) {
            if (entry.charAt(i) != result.charAt(i)) {
                differences++;
            }
        }
        assertTrue(differences <= 3, "Only 3 differences are allowed");
        assertTrue(differences > 0, "At least one difference should exist");
    }


    @Test
    public void testMapColumn_TwoValues() {
        DataNoise dataNoise = new DataNoise();
        List<String> input = List.of("Ja", "Nein", "Ja", "Nein");
        List<String> expected = List.of("0", "1", "0", "1");
        assertEquals(expected, dataNoise.mapColumn(input));
    }

    @Test
    public void testMapColumn_ThreeValues() {
        DataNoise dataNoise = new DataNoise();
        List<String> input = List.of("Ja", "Nein", "Vielleicht", "Ja", "Vielleicht");
        List<String> expected = List.of("0", "1", "2", "0", "2");
        assertEquals(expected, dataNoise.mapColumn(input));
    }

    @Test
    public void testMapColumn_FourValues() {
        DataNoise dataNoise = new DataNoise();
        List<String> input = List.of("A", "B", "C", "D");
        List<String> expected = List.of("0", "1", "2", "3");
        assertEquals(expected, dataNoise.mapColumn(input));
    }
}