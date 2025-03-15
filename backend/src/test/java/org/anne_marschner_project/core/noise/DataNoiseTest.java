package org.anne_marschner_project.core.noise;

import org.anne_marschner_project.core.data.Attribute;
import org.anne_marschner_project.core.data.Relation;
import org.anne_marschner_project.core.data.Type;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class DataNoiseTest {

    @Test
    public void testChooseNoise_WithSTRING() throws Exception {
        List<String> selectedStringMethods = new ArrayList<>(Arrays.asList("abbreviateDataEntry", "addRandomPrefix"));
        List<String> selectedNumericMethods = new ArrayList<>(Arrays.asList("changeValue", "changeValueToOutlier"));
        DataNoise dataNoise = new DataNoise(selectedStringMethods, selectedNumericMethods);

        String entry = "word";
        Attribute attribute = new Attribute("Name", Type.STRING);
        List<String> columnValues = new ArrayList<>(Arrays.asList("20", "30", "40", "18"));
        String replacement = dataNoise.chooseNoise(entry, attribute, 3, columnValues);
        assertNotEquals(replacement, entry);
    }

    @Test
    public void testChooseNoise_WithDOUBLES() throws Exception {
        List<String> selectedStringMethods = new ArrayList<>(Arrays.asList("abbreviateDataEntry", "addRandomPrefix"));
        List<String> selectedNumericMethods = new ArrayList<>(Arrays.asList("changeValue", "changeValueToOutlier"));
        DataNoise dataNoise = new DataNoise(selectedStringMethods, selectedNumericMethods);

        String entry1 = "20";
        Attribute attribute1 = new Attribute("Number", Type.DOUBLE);
        String entry2 = "20";
        Attribute attribute2 = new Attribute("Age", Type.DOUBLE);
        List<String> columnValues = new ArrayList<>(Arrays.asList("20", "30", "40", "18"));
        String replacement1 = dataNoise.chooseNoise(entry1, attribute1, 3, columnValues);
        String replacement2 = dataNoise.chooseNoise(entry2, attribute2, 3, columnValues);
        assertNotEquals(replacement1, entry1);
        assertNotEquals(replacement2, entry2);
    }

    @Test
    public void testChooseStringNoise_MethodsChosen() throws Exception {
        List<String> selectedStringMethods = new ArrayList<>(Arrays.asList("abbreviateDataEntry", "addRandomPrefix"));
        List<String> selectedNumericMethods = new ArrayList<>(Arrays.asList("changeValue", "changeValueToOutlier"));
        DataNoise dataNoise = new DataNoise(selectedStringMethods, selectedNumericMethods);

        String entry = "word";
        List<String> applicableMethods = new ArrayList<>(Arrays.asList("abbreviateDataEntry", "generateMissingValue",
                "addRandomPrefix", "replaceWithTranslation", "replaceWithSynonyms"));
        String result = dataNoise.chooseStringNoise(entry, applicableMethods);
        assertNotEquals(result, entry);
    }

    @Test
    public void testChooseStringNoise_NoMethodsChosen() throws Exception {
        List<String> selectedStringMethods = new ArrayList<>();
        List<String> selectedNumericMethods = new ArrayList<>(Arrays.asList("changeValue", "changeValueToOutlier"));
        DataNoise dataNoise = new DataNoise(selectedStringMethods, selectedNumericMethods);

        String entry = "word";
        List<String> applicableMethods = new ArrayList<>(Arrays.asList("abbreviateDataEntry", "generateMissingValue", "addRandomPrefix"));
        String result = dataNoise.chooseStringNoise(entry, applicableMethods);
        assertNotEquals(result, entry);
    }

    @Test
    public void testChooseNumericNoise_MethodsChosen() {
        List<String> selectedStringMethods = new ArrayList<>();
        List<String> selectedNumericMethods = new ArrayList<>(Arrays.asList("changeValue", "changeValueToOutlier"));
        DataNoise dataNoise = new DataNoise(selectedStringMethods, selectedNumericMethods);
        String entry = "20";
        String result = dataNoise.chooseNumericNoise(entry, 10, 3);
        assertNotEquals(result, entry);
    }

    @Test
    public void testChooseNumericNoise_NoMethodsChosen() {
        List<String> selectedStringMethods = new ArrayList<>();
        List<String> selectedNumericMethods = new ArrayList<>();
        DataNoise dataNoise = new DataNoise(selectedStringMethods, selectedNumericMethods);
        String entry = "20";
        String result = dataNoise.chooseNumericNoise(entry, 10, 3);
        assertNotEquals(result, entry);
    }

    @Test
    public void testPickUniqueRandomIndices() {

        int N = 10;
        int k = 5;

        // Call method
        DataNoise dataNoise = new DataNoise();
        Set<Integer> result = dataNoise.pickUniqueRandomIndices(N, k);

        // Check if k indices were selected
        assertEquals(k, result.size(), "Selected indices should be k");

        for (Integer index : result) {
            assertTrue(index >= 0 && index < N, "Every index has to be between 0 and N-1");
        }
    }

    @Test
    void testFindApplicableMethods() {

        // Set DataNoise Object
        DataNoise dataNoise = new DataNoise();

        // Entry with additional methods: "generatePhoneticError", "generateTypingError"
        List<String> result1 = dataNoise.findApplicableMethods("word");
        List<String> expected1 = Arrays.asList("abbreviateDataEntry", "generateMissingValue", "addRandomPrefix",
                "replaceWithTranslation", "replaceWithSynonyms", "generatePhoneticError", "generateTypingError");
        assertEquals(expected1, result1);

        // Entry with additional methods: "shuffleWords", "generatePhoneticError", "generateOCRError", "changeFormat","generateTypingError"
        List<String> result2 = dataNoise.findApplicableMethods("multiple WORDS");
        List<String> expected2 = Arrays.asList("abbreviateDataEntry", "generateMissingValue", "addRandomPrefix",
                "replaceWithTranslation", "replaceWithSynonyms", "shuffleWords", "generatePhoneticError", "generateOCRError",
                "changeFormat","generateTypingError");
        assertEquals(expected2, result2);

        // Entry with additional methods: "shuffleWords", "generateOCRError","changeFormat", "generateTypingError"
        List<String> result3 = dataNoise.findApplicableMethods("28-04-2424");
        List<String> expected3 = Arrays.asList("abbreviateDataEntry", "generateMissingValue", "addRandomPrefix",
                "replaceWithTranslation", "replaceWithSynonyms", "shuffleWords", "generateOCRError"
                ,"changeFormat", "generateTypingError");
        assertEquals(expected3, result3);

        // Empty entry
        List<String> result4 = dataNoise.findApplicableMethods("");
        List<String> expected4 = Arrays.asList("generateMissingValue", "generateRandomString");
        assertEquals(expected4, result4);

        // Entry with additional methods: "generateOCRError" ,"generatePhoneticError", "generateTypingError"
        List<String> result5 = dataNoise.findApplicableMethods("A0");
        List<String> expected5 = Arrays.asList("abbreviateDataEntry", "generateMissingValue", "addRandomPrefix",
                "replaceWithTranslation", "replaceWithSynonyms", "generatePhoneticError", "generateOCRError", "generateTypingError");
        assertEquals(expected5, result5);
    }

    @Test
    void testCalculateNumOfNumericPerturbation_NoAdjustmentNeeded() {
        // Case: no adjustments needed
        int numToPerturb = 8;
        int numOverlappingColumns = 10;
        int numOfNumericColumns = 4;
        int numOfStringColumns = 6;
        List<String> allSelectedStringMethods = new ArrayList<>(Arrays.asList("Method1", "Method2", "Method3"));
        List<String> allSelectedNumericMethods = new ArrayList<>(Arrays.asList("Method1", "Method2"));
        DataNoise dataNoise = new DataNoise(allSelectedStringMethods, allSelectedNumericMethods);
        int result = dataNoise.calculateNumOfNumericPerturbation(numToPerturb ,numOverlappingColumns, numOfNumericColumns, numOfStringColumns);
        assertEquals(4, result);
    }

    @Test
    void testCalculateNumOfNumericPerturbation_AdjustNumeric() {
        // Case: more numeric Methods chosen than numeric columns in proposed ratio
        int numToPerturb = 6;
        int numOverlappingColumns = 20;
        int numOfNumericColumns = 3;
        int numOfStringColumns = 18;
        List<String> allSelectedStringMethods = new ArrayList<>(List.of("Method1"));
        List<String> allSelectedNumericMethods = new ArrayList<>(Arrays.asList("Method1", "Method2"));
        DataNoise dataNoise = new DataNoise(allSelectedStringMethods, allSelectedNumericMethods);
        int result = dataNoise.calculateNumOfNumericPerturbation(numToPerturb ,numOverlappingColumns, numOfNumericColumns, numOfStringColumns);
        assertEquals(2, result);
    }

    @Test
    void testCalculateNumOfNumericPerturbation_AdjustString() {
        // Case: more String Methods chosen than string columns in proposed ratio
        int numToPerturb = 6;
        int numOverlappingColumns = 40;
        int numOfNumericColumns = 25;
        int numOfStringColumns = 5;
        List<String> allSelectedStringMethods = new ArrayList<>(Arrays.asList("Method1", "Method2", "Method3", "Method4"));
        List<String> allSelectedNumericMethods = new ArrayList<>(List.of("Method1"));
        DataNoise dataNoise = new DataNoise(allSelectedStringMethods, allSelectedNumericMethods);
        int result = dataNoise.calculateNumOfNumericPerturbation(numToPerturb ,numOverlappingColumns, numOfNumericColumns, numOfStringColumns);
        assertEquals(2, result);
    }

    @Test
    void testCalculateNumOfNumericPerturbation_AdjustBoth() {
        // Case: more Methods chosen than columns exist and both are adjusted
        int numToPerturb = 4;
        int numOverlappingColumns = 40;
        int numOfNumericColumns = 5;
        int numOfStringColumns = 35;
        List<String> allSelectedStringMethods = new ArrayList<>(Arrays.asList("Method1", "Method2", "Method3", "Method4"));
        List<String> allSelectedNumericMethods = new ArrayList<>(List.of("Method1", "Method2"));
        DataNoise dataNoise = new DataNoise(allSelectedStringMethods, allSelectedNumericMethods);
        int result = dataNoise.calculateNumOfNumericPerturbation(numToPerturb ,numOverlappingColumns, numOfNumericColumns, numOfStringColumns);
        assertEquals(2, result);
    }

    @Test
    void testCalculateNumOfNumericPerturbation_TooManyMethods() {
        // Case: more Methods chosen than columns exist
        int numToPerturb = 4;
        int numOverlappingColumns = 40;
        int numOfNumericColumns = 35;
        int numOfStringColumns = 5;
        List<String> allSelectedStringMethods = new ArrayList<>(Arrays.asList("Method1", "Method2", "Method3", "Method4"));
        List<String> allSelectedNumericMethods = new ArrayList<>(List.of("Method1"));
        DataNoise dataNoise = new DataNoise(allSelectedStringMethods, allSelectedNumericMethods);
        int result = dataNoise.calculateNumOfNumericPerturbation(numToPerturb ,numOverlappingColumns, numOfNumericColumns, numOfStringColumns);
        assertEquals(1, result);
    }

    @Test
    void testCalculateNumOfNumericPerturbation_TwoMethodsChosen() {
        // Case: only two rows will be perturbed (one of each)
        int numToPerturb = 2;
        int numOverlappingColumns = 40;
        int numOfNumericColumns = 35;
        int numOfStringColumns = 5;
        List<String> allSelectedStringMethods = new ArrayList<>(Arrays.asList("Method1", "Method2", "Method3", "Method4"));
        List<String> allSelectedNumericMethods = new ArrayList<>(Arrays.asList("Method1", "Method2"));
        DataNoise dataNoise = new DataNoise(allSelectedStringMethods, allSelectedNumericMethods);
        int result = dataNoise.calculateNumOfNumericPerturbation(numToPerturb ,numOverlappingColumns, numOfNumericColumns, numOfStringColumns);
        assertEquals(1, result);
    }

    @Test
    void testCalculateNumOfNumericPerturbation_OnlyStringMethods() {
        // Case: only string methods chosen
        int numToPerturb = 10;
        int numOverlappingColumns = 40;
        int numOfNumericColumns = 34;
        int numOfStringColumns = 6;
        List<String> allSelectedStringMethods = new ArrayList<>(Arrays.asList("Method1", "Method2", "Method3", "Method4"));
        List<String> allSelectedNumericMethods = new ArrayList<>();
        DataNoise dataNoise = new DataNoise(allSelectedStringMethods, allSelectedNumericMethods);
        int result = dataNoise.calculateNumOfNumericPerturbation(numToPerturb ,numOverlappingColumns, numOfNumericColumns, numOfStringColumns);
        assertEquals(4, result);
    }

    @Test
    void testCalculateNumOfNumericPerturbation_OnlyNumericMethods() {
        // Case: only numeric methods chosen
        int numToPerturb = 10;
        int numOverlappingColumns = 20;
        int numOfNumericColumns = 7;
        int numOfStringColumns = 13;
        List<String> allSelectedStringMethods = new ArrayList<>();
        List<String> allSelectedNumericMethods = new ArrayList<>(Arrays.asList("Method1", "Method2"));
        DataNoise dataNoise = new DataNoise(allSelectedStringMethods, allSelectedNumericMethods);
        int result = dataNoise.calculateNumOfNumericPerturbation(numToPerturb ,numOverlappingColumns, numOfNumericColumns, numOfStringColumns);
        assertEquals(7, result);
    }

    @Test
    void testCalculateNumOfNumericPerturbation_NoNumericColumns() {
        // Case: no numeric columns exist
        int numToPerturb = 10;
        int numOverlappingColumns = 10;
        int numOfNumericColumns = 0;
        int numOfStringColumns = 10;
        List<String> allSelectedStringMethods = new ArrayList<>(Arrays.asList("Method1", "Method2", "Method3", "Method4"));
        List<String> allSelectedNumericMethods = new ArrayList<>(Arrays.asList("Method1"));
        DataNoise dataNoise = new DataNoise(allSelectedStringMethods, allSelectedNumericMethods);
        int result = dataNoise.calculateNumOfNumericPerturbation(numToPerturb ,numOverlappingColumns, numOfNumericColumns, numOfStringColumns);
        assertEquals(0, result);
    }

    @Test
    void testCalculateNumOfNumericPerturbation_NoStringColumns() {
        // Case: no string columns exist
        int numToPerturb = 3;
        int numOverlappingColumns = 10;
        int numOfNumericColumns = 10;
        int numOfStringColumns = 0;
        List<String> allSelectedStringMethods = new ArrayList<>(Arrays.asList("Method1", "Method2", "Method3", "Method4"));
        List<String> allSelectedNumericMethods = new ArrayList<>(Arrays.asList("Method1"));
        DataNoise dataNoise = new DataNoise(allSelectedStringMethods, allSelectedNumericMethods);
        int result = dataNoise.calculateNumOfNumericPerturbation(numToPerturb ,numOverlappingColumns, numOfNumericColumns, numOfStringColumns);
        assertEquals(3, result);
    }

    @Test
    void testPerturbData_VerticalSplit() throws Exception {

        // Set DataNoise Object
        List<String> selectedStringMethods = new ArrayList<>(Arrays.asList("abbreviateDataEntry", "addRandomPrefix", "shuffleWords"));
        List<String> selectedNumericMethods = new ArrayList<>(Arrays.asList("changeValue", "changeValueToOutlier"));
        DataNoise dataNoise = new DataNoise(selectedStringMethods, selectedNumericMethods);

        // Create relation
        Map<Integer, Attribute> schema = new HashMap<>();
        schema.put(0, new Attribute("String column", Type.STRING));
        schema.put(1, new Attribute("Number column", Type.DOUBLE));
        schema.put(2, new Attribute("String column", Type.STRING));
        schema.put(3, new Attribute("Number column", Type.DOUBLE));
        schema.put(4, new Attribute("Number column", Type.DOUBLE));
        schema.put(5, new Attribute("Number column", Type.DOUBLE));
        Map<Integer, List<String>> data = new HashMap<>();
        data.put(0, Arrays.asList("la lo", "lu le", "le li", "li lo", "lo lu", "mo mu", "wo wu", "to tu"));
        data.put(1, Arrays.asList("10.5", "20", "20.0", "15.2", "30", "10.8", "16.9", "20"));
        data.put(2, Arrays.asList("", "", "", "", "a", "b", "c", "d"));
        data.put(3, Arrays.asList("10", "20", "20", "15", "30", "10", "16", "20"));
        data.put(4, Arrays.asList("1.0", "2.0", "hi", "3.5", "3.0", "2.5", "3.4", "bye"));
        data.put(5, Arrays.asList("1", "2", "hi", "3.5", "3", "2.5", "3.4", "bye"));
        List<Integer> overlappingColumnIndices = new ArrayList<>(Arrays.asList(0,1,2,3,4));
        List<Integer> keyIndices = new ArrayList<>(Arrays.asList(1,2));
        Relation sourceRelation = new Relation(schema, data, keyIndices, overlappingColumnIndices);
        int noisePercentage = 90;
        int noiseInsidePercentage = 15;

        // Apply method
        Relation perturbedRelation = dataNoise.perturbData(sourceRelation, noisePercentage, noiseInsidePercentage,true);

        // Column 1: 1 or 2 values should have been changed
        List<String> resultColumn0 = perturbedRelation.getData().get(0);
        List<String> originalColumn0 = new ArrayList<>(Arrays.asList("la lo", "lu le", "le li", "li lo", "lo lu", "mo mu", "wo wu", "to tu"));
        int changesinColumn0 = 0;
        for (int i = 0; i < resultColumn0.size(); i++) {
            if (!resultColumn0.get(i).equals(originalColumn0.get(i))) {
                changesinColumn0++;
            }
        }
        assertTrue(changesinColumn0 <= 2, "Too many values changed");
        assertTrue(changesinColumn0 >= 1, "Not enough values changed");

        // Column 2: 1 or 2 values should have been changed
        List<String> resultColumn1 = perturbedRelation.getData().get(1);
        List<String> originalColumn1 = new ArrayList<>(Arrays.asList("10.5", "20", "20.0", "15.2", "30", "10.8", "16.9", "20"));
        int changesinColumn1 = 0;
        for (int i = 0; i < resultColumn1.size(); i++) {
            if (!resultColumn1.get(i).equals(originalColumn1.get(i))) {
                changesinColumn1++;
            }
        }
        assertTrue(changesinColumn1 <= 2, "Too many values changed");
        assertTrue(changesinColumn1 >= 1, "Not enough values changed");

        // Column 3: 1 or 2 values should have been changed
        List<String> resultColumn2 = perturbedRelation.getData().get(2);
        List<String> originalColumn2 = new ArrayList<>(Arrays.asList("", "", "", "", "a", "b", "c", "d"));
        int changesinColumn2 = 0;
        for (int i = 0; i < resultColumn2.size(); i++) {
            if (!resultColumn2.get(i).equals(originalColumn2.get(i))) {
                changesinColumn2++;
            }
        }
        assertTrue(changesinColumn2 <= 2, "Too many values changed");
        assertTrue(changesinColumn2 >= 1, "Not enough values changed");

        // Column 4: 1 or 2 values should have been changed
        List<String> resultColumn3 = perturbedRelation.getData().get(3);
        List<String> originalColumn3 = new ArrayList<>(Arrays.asList("10", "20", "20", "15", "30", "10", "16", "20"));
        int changesinColumn3 = 0;
        for (int i = 0; i < resultColumn3.size(); i++) {
            if (!resultColumn3.get(i).equals(originalColumn3.get(i))) {
                changesinColumn3++;
            }
        }
        assertTrue(changesinColumn3 <= 2, "Too many values changed");
        assertTrue(changesinColumn3 >= 1, "Not enough values changed");

        //  Column 5: 1 or 2 values should have been changed
        List<String> resultColumn4 = perturbedRelation.getData().get(4);
        List<String> originalColumn4 = new ArrayList<>(Arrays.asList("1.0", "2.0", "hi", "3.5", "3.0", "2.5", "3.4", "bye"));
        int changesinColumn4 = 0;
        for (int i = 0; i < resultColumn4.size(); i++) {
            if (!resultColumn4.get(i).equals(originalColumn4.get(i))) {
                changesinColumn4++;
            }
        }
        assertTrue(changesinColumn4 <= 2, "Too many values changed");
        assertTrue(changesinColumn4 >= 1, "Not enough values changed");

        // Column 6 should stay the same
        List<String> column5 = perturbedRelation.getData().get(5);
        assertEquals(Arrays.asList("1", "2", "hi", "3.5", "3", "2.5", "3.4", "bye"), column5, "Column should not be changed");
    }

    @Test
    void testPerturbData_HorizontalSplit() throws Exception {

        // Set DataNoise Object
        List<String> selectedStringMethods = new ArrayList<>(Arrays.asList("abbreviateDataEntry", "generateMissingValue", "addRandomPrefix"));
        List<String> selectedNumericMethods = new ArrayList<>(Arrays.asList("changeValue", "changeValueToOutlier"));
        DataNoise dataNoise = new DataNoise(selectedStringMethods, selectedNumericMethods);

        // Create relation
        Map<Integer, Attribute> schema = new HashMap<>();
        schema.put(0, new Attribute("String column", Type.STRING));
        schema.put(1, new Attribute("Number column", Type.DOUBLE));
        schema.put(2, new Attribute("String column", Type.STRING));
        schema.put(3, new Attribute("Number column", Type.DOUBLE));
        schema.put(4, new Attribute("Number column", Type.DOUBLE));
        schema.put(5, new Attribute("Number column", Type.DOUBLE));
        Map<Integer, List<String>> data = new HashMap<>();
        data.put(0, Arrays.asList("la lo", "lu le", "le li", "li lo", "lo lu", "mo mu", "wo wu", "to tu"));
        data.put(1, Arrays.asList("10.5", "20", "20.0", "15.2", "30", "10.8", "16.9", "20"));
        data.put(2, Arrays.asList("", "", "", "", "a", "b", "c", "d"));
        data.put(3, Arrays.asList("10", "20", "20", "15", "30", "10", "16", "20"));
        data.put(4, Arrays.asList("1.0", "2.0", "hi", "3.5", "3.0", "2.5", "3.4", "bye"));
        data.put(5, Arrays.asList("1", "2", "hi", "3.5", "3", "2.5", "3.4", "bye"));
        int numberOfColumns = schema.size();
        int numOfOverlappingRows = 4;
        List<Integer> keyIndices = new ArrayList<>(Arrays.asList(1,2));
        Relation sourceRelation = new Relation(schema, data, keyIndices, numOfOverlappingRows);
        int noisePercentage = 90;
        int noiseInsidePercentage = 15;

        // Apply method
        Relation perturbedRelation = dataNoise.perturbData(sourceRelation, noisePercentage, noiseInsidePercentage, true);

        // Extract result rows
        List<String> originalRow0 = new ArrayList<>(Arrays.asList("la lo", "10.5", "", "10", "1.0", "1"));
        List<String> originalRow1 = new ArrayList<>(Arrays.asList("lu le", "20", "", "20", "2.0", "2"));
        List<String> originalRow2 = new ArrayList<>(Arrays.asList("le li", "20.0", "", "20", "hi", "hi"));
        List<String> originalRow3 = new ArrayList<>(Arrays.asList("li lo", "15.2", "", "15", "3.5", "3.5"));
        List<List<String>> resultRows = new ArrayList<>();
        for (int i = 0; i < numOfOverlappingRows; i++) {
            resultRows.add(new ArrayList<>());
        }
        for (int i = 0; i < numberOfColumns; i++) {
            for (int j = 0; j < numOfOverlappingRows; j++) {
                resultRows.get(j).add(perturbedRelation.getData().get(i).get(j));
            }
        }

        // Calculate changes
        int changesinRow0 = 0;
        int changesinRow1 = 0;
        int changesinRow2 = 0;
        int changesinRow3 = 0;
        for (int i = 0; i < resultRows.get(0).size(); i++) {
            if (!resultRows.get(0).get(i).equals(originalRow0.get(i))) {
                changesinRow0++;
            }
        }
        for (int i = 0; i < resultRows.get(1).size(); i++) {
            if (!resultRows.get(1).get(i).equals(originalRow1.get(i))) {
                changesinRow1++;
            }
        }
        for (int i = 0; i < resultRows.get(2).size(); i++) {
            if (!resultRows.get(2).get(i).equals(originalRow2.get(i))) {
                changesinRow2++;
            }
        }
        for (int i = 0; i < resultRows.get(3).size(); i++) {
            if (!resultRows.get(3).get(i).equals(originalRow3.get(i))) {
                changesinRow3++;
            }
        }

        // 1 or 2 values should have been changed per row
        assertTrue(changesinRow0 <= 2, "Too many values changed");
        assertTrue(changesinRow0 >= 1, "Not enough values changed");
        assertTrue(changesinRow1 <= 2, "Too many values changed");
        assertTrue(changesinRow1 >= 1, "Not enough values changed");
        assertTrue(changesinRow2 <= 2, "Too many values changed");
        assertTrue(changesinRow2 >= 1, "Not enough values changed");
        assertTrue(changesinRow3 <= 2, "Too many values changed");
        assertTrue(changesinRow3 >= 1, "Not enough values changed");
    }


    @Test
    void testPerturbRowData() throws Exception {

        // Set DataNoise Object
        List<String> selectedStringMethods = new ArrayList<>(Arrays.asList("abbreviateDataEntry", "generateMissingValue", "addRandomPrefix"));
        List<String> selectedNumericMethods = new ArrayList<>(Arrays.asList("changeValue", "changeValueToOutlier"));
        DataNoise dataNoise = new DataNoise(selectedStringMethods, selectedNumericMethods);

        // Create relation
        Map<Integer, Attribute> schema = new HashMap<>();
        schema.put(0, new Attribute("String column", Type.STRING));
        schema.put(1, new Attribute("Number column", Type.DOUBLE));
        schema.put(2, new Attribute("String column", Type.STRING));
        schema.put(3, new Attribute("Number column", Type.DOUBLE));
        schema.put(4, new Attribute("Number column", Type.DOUBLE));
        schema.put(5, new Attribute("Number column", Type.DOUBLE));
        Map<Integer, List<String>> data = new HashMap<>();
        data.put(0, Arrays.asList("la lo", "lu le", "le li", "li lo", "lo lu", "mo mu", "wo wu", "to tu"));
        data.put(1, Arrays.asList("10.5", "20", "20.0", "15.2", "30", "10.8", "16.9", "20"));
        data.put(2, Arrays.asList("", "", "", "", "a", "b", "c", "d"));
        data.put(3, Arrays.asList("10", "20", "20", "15", "30", "10", "16", "20"));
        data.put(4, Arrays.asList("1.0", "2.0", "hi", "3.5", "3.0", "2.5", "3.4", "bye"));
        data.put(5, Arrays.asList("1", "2", "hi", "3.5", "3", "2.5", "3.4", "bye"));
        int numberOfColumns = schema.size();
        int numOfOverlappingRows = 4;
        List<Integer> keyIndices = new ArrayList<>(Arrays.asList(1,2));
        Relation sourceRelation = new Relation(schema, data, keyIndices, numOfOverlappingRows);
        int noisePercentage = 90;
        int noiseInsidePercentage = 15;

        // Apply method
        Relation perturbedRelation = dataNoise.perturbRowData(sourceRelation, noisePercentage, noiseInsidePercentage,true, false);

        // Extract result rows
        List<String> originalRow0 = new ArrayList<>(Arrays.asList("la lo", "10.5", "", "10", "1.0", "1"));
        List<String> originalRow1 = new ArrayList<>(Arrays.asList("lu le", "20", "", "20", "2.0", "2"));
        List<String> originalRow2 = new ArrayList<>(Arrays.asList("le li", "20.0", "", "20", "hi", "hi"));
        List<String> originalRow3 = new ArrayList<>(Arrays.asList("li lo", "15.2", "", "15", "3.5", "3.5"));
        List<List<String>> resultRows = new ArrayList<>();
        for (int i = 0; i < numOfOverlappingRows; i++) {
            resultRows.add(new ArrayList<>());
        }
        for (int i = 0; i < numberOfColumns; i++) {
            for (int j = 0; j < numOfOverlappingRows; j++) {
                resultRows.get(j).add(perturbedRelation.getData().get(i).get(j));
            }
        }

        // Calculate changes
        int changesinRow0 = 0;
        int changesinRow1 = 0;
        int changesinRow2 = 0;
        int changesinRow3 = 0;
        for (int i = 0; i < resultRows.get(0).size(); i++) {
            if (!resultRows.get(0).get(i).equals(originalRow0.get(i))) {
                changesinRow0++;
            }
        }
        for (int i = 0; i < resultRows.get(1).size(); i++) {
            if (!resultRows.get(1).get(i).equals(originalRow1.get(i))) {
                changesinRow1++;
            }
        }
        for (int i = 0; i < resultRows.get(2).size(); i++) {
            if (!resultRows.get(2).get(i).equals(originalRow2.get(i))) {
                changesinRow2++;
            }
        }
        for (int i = 0; i < resultRows.get(3).size(); i++) {
            if (!resultRows.get(3).get(i).equals(originalRow3.get(i))) {
                changesinRow3++;
            }
        }

        // 1 or 2 values should have been changed per row
        assertTrue(changesinRow0 <= 2, "Too many values changed");
        assertTrue(changesinRow0 >= 1, "Not enough values changed");
        assertTrue(changesinRow1 <= 2, "Too many values changed");
        assertTrue(changesinRow1 >= 1, "Not enough values changed");
        assertTrue(changesinRow2 <= 2, "Too many values changed");
        assertTrue(changesinRow2 >= 1, "Not enough values changed");
        assertTrue(changesinRow3 <= 2, "Too many values changed");
        assertTrue(changesinRow3 >= 1, "Not enough values changed");
    }

    @Test
    void testPerturbColumnData() throws Exception {

        // Set DataNoise Object
        List<String> selectedStringMethods = new ArrayList<>(Arrays.asList("abbreviateDataEntry", "addRandomPrefix", "shuffleWords"));
        List<String> selectedNumericMethods = new ArrayList<>(Arrays.asList("changeValue", "changeValueToOutlier"));
        DataNoise dataNoise = new DataNoise(selectedStringMethods, selectedNumericMethods);

        // Create relation
        Map<Integer, Attribute> schema = new HashMap<>();
        schema.put(0, new Attribute("String column", Type.STRING));
        schema.put(1, new Attribute("Number column", Type.DOUBLE));
        schema.put(2, new Attribute("String column", Type.STRING));
        schema.put(3, new Attribute("Number column", Type.DOUBLE));
        schema.put(4, new Attribute("Number column", Type.DOUBLE));
        schema.put(5, new Attribute("Number column", Type.DOUBLE));
        Map<Integer, List<String>> data = new HashMap<>();
        data.put(0, Arrays.asList("la lo", "lu le", "le li", "li lo", "lo lu", "mo mu", "wo wu", "to tu"));
        data.put(1, Arrays.asList("10.5", "20", "20.0", "15.2", "30", "10.8", "16.9", "20"));
        data.put(2, Arrays.asList("", "", "", "", "a", "b", "c", "d"));
        data.put(3, Arrays.asList("10", "20", "20", "15", "30", "10", "16", "20"));
        data.put(4, Arrays.asList("1.0", "2.0", "hi", "3.5", "3.0", "2.5", "3.4", "bye"));
        data.put(5, Arrays.asList("1", "2", "hi", "3.5", "3", "2.5", "3.4", "bye"));
        List<Integer> overlappingColumnIndices = new ArrayList<>(Arrays.asList(0,1,2,3,4));
        List<Integer> keyIndices = new ArrayList<>(Arrays.asList(1,2));
        Relation sourceRelation = new Relation(schema, data, keyIndices, overlappingColumnIndices);
        int noisePercentage = 90;
        int noiseInsidePercentage = 15;

        // Apply method
        Relation perturbedRelation = dataNoise.perturbColumnData(sourceRelation, noisePercentage, noiseInsidePercentage,true);

        // Column 1: 1 or 2 values should have been changed
        List<String> resultColumn0 = perturbedRelation.getData().get(0);
        List<String> originalColumn0 = new ArrayList<>(Arrays.asList("la lo", "lu le", "le li", "li lo", "lo lu", "mo mu", "wo wu", "to tu"));
        int changesinColumn0 = 0;
        for (int i = 0; i < resultColumn0.size(); i++) {
            if (!resultColumn0.get(i).equals(originalColumn0.get(i))) {
                changesinColumn0++;
            }
        }
        assertTrue(changesinColumn0 <= 2, "Too many values changed");
        assertTrue(changesinColumn0 >= 1, "Not enough values changed");

        // Column 2: 1 or 2 values should have been changed
        List<String> resultColumn1 = perturbedRelation.getData().get(1);
        List<String> originalColumn1 = new ArrayList<>(Arrays.asList("10.5", "20", "20.0", "15.2", "30", "10.8", "16.9", "20"));
        int changesinColumn1 = 0;
        for (int i = 0; i < resultColumn1.size(); i++) {
            if (!resultColumn1.get(i).equals(originalColumn1.get(i))) {
                changesinColumn1++;
            }
        }
        assertTrue(changesinColumn1 <= 2, "Too many values changed");
        assertTrue(changesinColumn1 >= 1, "Not enough values changed");

        // Column 3: 1 or 2 values should have been changed
        List<String> resultColumn2 = perturbedRelation.getData().get(2);
        List<String> originalColumn2 = new ArrayList<>(Arrays.asList("", "", "", "", "a", "b", "c", "d"));
        int changesinColumn2 = 0;
        for (int i = 0; i < resultColumn2.size(); i++) {
            if (!resultColumn2.get(i).equals(originalColumn2.get(i))) {
                changesinColumn2++;
            }
        }
        assertTrue(changesinColumn2 <= 2, "Too many values changed");
        assertTrue(changesinColumn2 >= 1, "Not enough values changed");

        // Column 4: 1 or 2 values should have been changed
        List<String> resultColumn3 = perturbedRelation.getData().get(3);
        List<String> originalColumn3 = new ArrayList<>(Arrays.asList("10", "20", "20", "15", "30", "10", "16", "20"));
        int changesinColumn3 = 0;
        for (int i = 0; i < resultColumn3.size(); i++) {
            if (!resultColumn3.get(i).equals(originalColumn3.get(i))) {
                changesinColumn3++;
            }
        }
        assertTrue(changesinColumn3 <= 2, "Too many values changed");
        assertTrue(changesinColumn3 >= 1, "Not enough values changed");

        // Column 5: 1 or 2 values should have been changed
        List<String> resultColumn4 = perturbedRelation.getData().get(4);
        List<String> originalColumn4 = new ArrayList<>(Arrays.asList("1.0", "2.0", "hi", "3.5", "3.0", "2.5", "3.4", "bye"));
        int changesinColumn4 = 0;
        for (int i = 0; i < resultColumn4.size(); i++) {
            if (!resultColumn4.get(i).equals(originalColumn4.get(i))) {
                changesinColumn4++;
            }
        }
        assertTrue(changesinColumn4 <= 2, "Too many values changed");
        assertTrue(changesinColumn4 >= 1, "Not enough values changed");

        // Column 6 should stay the same
        List<String> column5 = perturbedRelation.getData().get(5);
        assertEquals(Arrays.asList("1", "2", "hi", "3.5", "3", "2.5", "3.4", "bye"), column5, "Column should not be changed");
    }

    @Test
    void testPerturbStringColumnData() throws Exception {

        // Set DataNoise Object
        List<String> selectedStringMethods = new ArrayList<>(Arrays.asList("abbreviateDataEntry", "addRandomPrefix", "shuffleWords"));
        List<String> selectedNumericMethods = new ArrayList<>(Arrays.asList("changeValue", "changeValueToOutlier"));
        DataNoise dataNoise = new DataNoise(selectedStringMethods, selectedNumericMethods);

        // Create relation
        Map<Integer, List<String>> data = new HashMap<>();
        data.put(0, Arrays.asList("la lo", "lu le", "le li", "li lo", "lo lu", "mo mu", "wo wu", "to tu"));
        data.put(1, Arrays.asList("10.5", "20", "20.0", "15.2", "30", "10.8", "16.9", "20"));
        data.put(2, Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h"));
        data.put(3, Arrays.asList("10", "20", "20", "15", "30", "10", "16", "20"));

        // Set values
        List<Integer> stringIndices = Arrays.asList(0, 2);
        int numStringPerturbation = 2;
        int noiseInsidePercentage = 35;

        // Apply method
        Map<Integer, List<String>> result = dataNoise.perturbStringColumnData(data, numStringPerturbation, stringIndices, noiseInsidePercentage);

        // Column 1: 2 or 3 values should have been changed
        List<String> resultColumn0 = result.get(0);
        List<String> originalColumn0 = new ArrayList<>(Arrays.asList("la lo", "lu le", "le li", "li lo", "lo lu", "mo mu", "wo wu", "to tu"));
        int changesinColumn0 = 0;
        for (int i = 0; i < resultColumn0.size(); i++) {
            if (!resultColumn0.get(i).equals(originalColumn0.get(i))) {
                changesinColumn0++;
            }
        }
        assertTrue(changesinColumn0 <= 3, "Too many values changed");
        assertTrue(changesinColumn0 >= 2, "Not enough values changed");

        // Column 3: 2 or 3 values should have been changed
        List<String> resultColumn2 = result.get(2);
        List<String> originalColumn2 = new ArrayList<>(Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h"));
        int changesinColumn2 = 0;
        for (int i = 0; i < resultColumn2.size(); i++) {
            if (!resultColumn2.get(i).equals(originalColumn2.get(i))) {
                changesinColumn2++;
            }
        }
        assertTrue(changesinColumn2 <= 3, "Too many values changed");
        assertTrue(changesinColumn2 >= 2, "Not enough values changed");

        // Column 2 and 4 should stay the same
        List<String> column1 = result.get(1);
        assertEquals(Arrays.asList("10.5", "20", "20.0", "15.2", "30", "10.8", "16.9", "20"), column1, "Column should not be changed");
        List<String> column3 = result.get(3);
        assertEquals(Arrays.asList("10", "20", "20", "15", "30", "10", "16", "20"), column3, "Column should not be changed");
    }


    @Test
    void testPerturbNumericColumnData() {

        // Set DataNoise Object
        List<String> selectedStringMethods = new ArrayList<>();
        List<String> selectedNumericMethods = new ArrayList<>(Arrays.asList("changeValue", "changeValueToOutlier"));
        DataNoise dataNoise = new DataNoise(selectedStringMethods, selectedNumericMethods);

        // Create Relation
        Map<Integer, List<String>> data = new HashMap<>();
        data.put(0, Arrays.asList("1.0", "2.0", "hi", "3.5", "3.0", "2.5", "3.4", "bye"));
        data.put(1, Arrays.asList("10.5", "20", "20.0", "15.2", "30", "10.8", "16.9", "20"));
        data.put(2, Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h"));

        // Set values
        List<Integer> numericIndices = Arrays.asList(0, 1);
        int numNumericPerturbation = 2;
        int noiseInsidePercentage = 35;

        // Apply method
        Map<Integer, List<String>> result = dataNoise.perturbNumericColumnData(data, numNumericPerturbation, numericIndices, noiseInsidePercentage);

        // Column 1: 2 or 3 values should have been changed
        List<String> resultColumn0 = result.get(0);
        List<String> originalColumn0 = new ArrayList<>(Arrays.asList("1.0", "2.0", "hi", "3.5", "3.0", "2.5", "3.4", "bye"));
        int changesinColumn0 = 0;
        for (int i = 0; i < resultColumn0.size(); i++) {
            if (!resultColumn0.get(i).equals(originalColumn0.get(i))) {
                changesinColumn0++;
            }
        }
        assertTrue(changesinColumn0 <= 3, "Too many values changed");
        assertTrue(changesinColumn0 >= 2, "Not enough values changed");

        // Column 2: 2 or 3 values should have been changed
        List<String> resultColumn1 = result.get(1);
        List<String> originalColumn1 = new ArrayList<>(Arrays.asList("10.5", "20", "20.0", "15.2", "30", "10.8", "16.9", "20"));
        int changesinColumn1 = 0;
        for (int i = 0; i < resultColumn1.size(); i++) {
            if (!resultColumn1.get(i).equals(originalColumn1.get(i))) {
                changesinColumn1++;
            }
        }
        assertTrue(changesinColumn1 <= 3, "Too many values changed");
        assertTrue(changesinColumn1 >= 2, "Not enough values changed");

        // Column 3 should stay the same
        List<String> column2 = result.get(2);
        assertEquals(Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h"), column2, "Column should not be changed");
    }
}