package org.anne_marschner_project.core.noise;

import org.anne_marschner_project.core.data.Attribute;
import org.anne_marschner_project.core.data.Relation;
import org.anne_marschner_project.core.data.Type;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class SchemaNoiseTest {


    @Test
    public void testPerturbSchema_HorizontalSplit_NoKeyNoise() throws Exception {

        // Create relation
        Map<Integer, Attribute> schema = new HashMap<>();
        schema.put(0, new Attribute("Identification Number", Type.DOUBLE));
        schema.put(1, new Attribute("Name", Type.STRING));
        schema.put(2, new Attribute("Age of Person", Type.DOUBLE));
        schema.put(3, new Attribute("Height", Type.DOUBLE));
        schema.put(4, new Attribute("hobby", Type.STRING));
        Map<Integer, List<String>> data = new HashMap<>();
        data.put(0, Arrays.asList("1", "2", "3", "4", "5"));
        data.put(1, Arrays.asList("Anne", "Jonathan", "Mareike", "Luca", "Milena"));
        data.put(2, Arrays.asList("24", "22", "23", "27", "22"));
        data.put(3, Arrays.asList("1.57", "1.8", "1.6", "1.71", "1.58"));
        data.put(4, Arrays.asList("dancing", "football", "knitting", "running", "fitness"));
        int numOfOverlappingRows = 4;
        List<Integer> keyIndices = new ArrayList<>(Arrays.asList(1,2));
        Relation sourceRelation = new Relation(schema, data, keyIndices, numOfOverlappingRows);

        // Set List for chosen methods and SchemaNoise Object
        List<String> selectedMethods = new ArrayList<>(Arrays.asList("replaceWithTranslation", "removeVowels", "shuffleWords"));
        SchemaNoise schemaNoise = new SchemaNoise(selectedMethods);

        // Call method to perturb schema
        Relation perturbedRelation = schemaNoise.perturbSchema(sourceRelation, 100, false, false);

        // Test if all 3 non-key columns out of 5 ColumnNames have been changed
        assertNotEquals("Identification Number", schema.get(0).getColumnName());
        assertEquals("Name", schema.get(1).getColumnName());
        assertEquals("Age of Person", schema.get(2).getColumnName());
        assertNotEquals("Height", schema.get(3).getColumnName());
        assertNotEquals("hobby", schema.get(4).getColumnName());
    }

    @Test
    public void testPerturbSchema_fromHorizontalSplit() throws Exception {

        // Create Relation
        Map<Integer, Attribute> schema = new HashMap<>();
        schema.put(0, new Attribute("Identification Number", Type.DOUBLE));
        schema.put(1, new Attribute("Name", Type.STRING));
        schema.put(2, new Attribute("Age of Person", Type.DOUBLE));
        schema.put(3, new Attribute("Height", Type.DOUBLE));
        schema.put(4, new Attribute("Hobby", Type.STRING));
        Map<Integer, List<String>> data = new HashMap<>();
        data.put(0, Arrays.asList("1", "2", "3", "4", "5"));
        data.put(1, Arrays.asList("Anne", "Jonathan", "Mareike", "Luca", "Milena"));
        data.put(2, Arrays.asList("24", "22", "23", "27", "22"));
        data.put(3, Arrays.asList("1.57", "1.8", "1.6", "1.71", "1.58"));
        data.put(4, Arrays.asList("dancing", "football", "knitting", "running", "fitness"));
        int numOfOverlappingRows = 4;
        List<Integer> keyIndices = new ArrayList<>(Arrays.asList(1,2));
        Relation sourceRelation = new Relation(schema, data, keyIndices, numOfOverlappingRows);

        // Set List for chosen methods and SchemaNoise Object
        List<String> selectedMethods = new ArrayList<>(Arrays.asList("replaceWithTranslation", "removeVowels", "shuffleWords"));
        SchemaNoise schemaNoise = new SchemaNoise(selectedMethods);

        // Call method to perturb schema
        Relation perturbedRelation = schemaNoise.perturbSchema(sourceRelation, 80, true, false);

        // Test if 4 out of 5 ColumnNames have been changed
        List<String> sourceColumnNames = new ArrayList<>(Arrays.asList("Identification Number", "Name", "Age of Person", "Height", "Hobby"));
        int changedColumnsCount = 0;
        for (int i = 0; i < 5; i++) {
            String originalColumn = sourceColumnNames.get(i);
            String perturbedColumn = perturbedRelation.getSchema().get(i).getColumnName();
            if (!originalColumn.equals(perturbedColumn)) {
                changedColumnsCount++;
                System.out.println("Column " + i + " changed from: " + originalColumn + " to: " + perturbedColumn);
            }
        }

        // Test if exactly 3 column names have been changed
        assertEquals(4, changedColumnsCount);
    }

    @Test
    public void testPerturbSchema_fromVerticalSplit() throws Exception {

        // Create Relation
        Map<Integer, Attribute> schema = new HashMap<>();
        schema.put(0, new Attribute("Identification Number", Type.DOUBLE));
        schema.put(1, new Attribute("Name", Type.STRING));
        schema.put(2, new Attribute("Age of Person", Type.DOUBLE));
        schema.put(3, new Attribute("Height", Type.DOUBLE));
        schema.put(4, new Attribute("Hobby", Type.STRING));
        Map<Integer, List<String>> data = new HashMap<>();
        data.put(0, Arrays.asList("1", "2", "3", "4", "5"));
        data.put(1, Arrays.asList("Anne", "Jonathan", "Mareike", "Luca", "Milena"));
        data.put(2, Arrays.asList("24", "22", "23", "27", "22"));
        data.put(3, Arrays.asList("1.57", "1.8", "1.6", "1.71", "1.58"));
        data.put(4, Arrays.asList("dancing", "football", "knitting", "running", "fitness"));
        List<Integer> overlappingColumnsIndices = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        List<Integer> keyIndices = new ArrayList<>(Arrays.asList(1,2));
        Relation sourceRelation = new Relation(schema, data, keyIndices, overlappingColumnsIndices);

        // Set List for chosen methods and SchemaNoise Object
        List<String> selectedMethods = new ArrayList<>(Arrays.asList("replaceWithTranslation", "removeVowels", "shuffleWords"));
        SchemaNoise schemaNoise = new SchemaNoise(selectedMethods);

        // Call method to perturb schema
        Relation perturbedRelation = schemaNoise.perturbSchema(sourceRelation, 75, true, false);

        // Test if 3 out of 5 ColumnNames have been changed
        List<String> sourceColumnNames = new ArrayList<>(Arrays.asList("Identification Number", "Name", "Age of Person", "Height", "Hobby"));
        int changedColumnsCount = 0;
        for (int i = 0; i < 5; i++) {
            String originalColumn = sourceColumnNames.get(i);
            String perturbedColumn = perturbedRelation.getSchema().get(i).getColumnName();

            if (!originalColumn.equals(perturbedColumn)) {
                changedColumnsCount++;
                System.out.println("Column " + i + " changed from: " + originalColumn + " to: " + perturbedColumn);
            }
        }

        // Test if exactly 3 column names have been changed
        assertEquals(3, changedColumnsCount);

        // Test if Index that does not overlap stayed the same
        assertEquals("Identification Number", perturbedRelation.getSchema().get(0).getColumnName());
    }

    @Test
    public void testChooseNoise_CorrectOrderOfSpecialMethods() throws Exception {

        // Set SchemaNoise Object with selected methods
        List<String> selectedMethods = new ArrayList<>(Arrays.asList("abbreviateFirstLetters", "removeVowels", "shuffleWords"));
        SchemaNoise schemaNoise = new SchemaNoise(selectedMethods);

        // Set List with column names
        List<String> columnNames = new ArrayList<>(Arrays.asList("name_that can be_shuffled", "ColumnName", "name_that_can_be abbreviated"));

        // Create replacements with chooseNoise
        List<String> replacements = new ArrayList<>();
        for (String columnName: columnNames) {
            replacements.add(schemaNoise.chooseNoise(columnName));
        }

        // Test if shuffleWords was applied first
        String originalColumn1 = "name_that can be_shuffled";
        String replacement1 = replacements.get(0);
        assertNotEquals(originalColumn1, replacement1, "The words should have been shuffled");
        assertTrue(replacement1.contains("name") && replacement1.contains("shuffled"));

        // Test if removeVowels was applied second
        String replacement2 = replacements.get(1);
        assertEquals("ClmnNm", replacement2, "The vowels should have been removed");

        // Test if other method was applied last
        String replacement3 = replacements.get(2);
        assertEquals("N.T.C.B.A.", replacement3, "The words should have been abbreviated");
    }

    @Test
    public void testChooseNoise_RefillEmptyList() throws Exception {

        // Set SchemaNoise Object with selected methods
        List<String> selectedMethods = new ArrayList<>(List.of("abbreviateFirstLetters"));
        SchemaNoise schemaNoise = new SchemaNoise(selectedMethods);

        // Set List with column Names
        List<String> columnNames = new ArrayList<>(Arrays.asList("Hobby", "Name", "Salary of Person"));

        // Create replacements with chooseNoise
        List<String> replacements = new ArrayList<>();
        for (String columnName: columnNames) {
            replacements.add(schemaNoise.chooseNoise(columnName));
        }

        // Test if all three words have been changed with the same method
        String replacement1 = replacements.get(0);
        assertEquals("H.", replacement1, "The words should have been abbreviated");

        // Test if removeVowels was applied second
        String replacement2 = replacements.get(1);
        assertEquals("N.", replacement2, "The words should have been abbreviated");

        // Test if other method was applied last
        String replacement3 = replacements.get(2);
        assertEquals("S.O.P.", replacement3, "The words should have been abbreviated");
    }

    @Test
    public void testChooseNoise_OneSpecialMethod() throws Exception {

        // Set SchemaNoise Object with selected methods
        List<String> selectedMethods = new ArrayList<>(List.of("removeVowels"));
        SchemaNoise schemaNoise = new SchemaNoise(selectedMethods);

        // Set List with column Names
        List<String> columnNames = new ArrayList<>(Arrays.asList("Hobby", "Nm", "Salary of Person"));

        // Create replacements with chooseNoise
        List<String> replacements = new ArrayList<>();
        for (String columnName: columnNames) {
            replacements.add(schemaNoise.chooseNoise(columnName));
        }

        // Test if all words with vowels have been changed with the same method
        String replacement1 = replacements.get(0);
        assertEquals("Hbby", replacement1, "The vowels should have been removed");

        // Test if removeVowels was applied second
        String replacement2 = replacements.get(1);
        assertNotEquals("Nm", replacement2, "The word should have been changed");

        // Test if other method was applied last
        String replacement3 = replacements.get(2);
        assertEquals("Slry f Prsn", replacement3, "The vowels should have been removed");
    }

    @Test
    public void testChooseNoise_SpecialMethodsCannotBeApplied() throws Exception {

        // Set SchemaNoise Object with selected methods
        List<String> selectedMethods = new ArrayList<>(Arrays.asList("abbreviateFirstLetters", "removeVowels", "shuffleWords", "abbreviateRandomLength"));
        SchemaNoise schemaNoise = new SchemaNoise(selectedMethods);

        // Set List with column Names
        List<String> columnNames = new ArrayList<>(Arrays.asList("H", "N", "2"));

        // Create replacements with chooseNoise
        List<String> replacements = new ArrayList<>();
        for (String columnName: columnNames) {
            replacements.add(schemaNoise.chooseNoise(columnName));
        }

        // Test if all three words have been changed
        String replacement1 = replacements.get(0);
        assertEquals("H.", replacement1, "The words should have been abbreviated");
        String replacement2 = replacements.get(1);
        assertEquals("N.", replacement2, "The words should have been abbreviated");
        String replacement3 = replacements.get(2);
        assertEquals("2.", replacement3, "The words should have been abbreviated");
    }


    @Test
    public void testChooseNoise_ShuffleWordsCannotBeApplied() throws Exception {

        // Set SchemaNoise Object with selected methods
        List<String> selectedMethods = new ArrayList<>(Arrays.asList("abbreviateFirstLetters", "removeVowels", "shuffleWords"));
        SchemaNoise schemaNoise = new SchemaNoise(selectedMethods);

        // Set list with column Names
        List<String> columnNames = new ArrayList<>(Arrays.asList("Hobby", "Name", "234"));

        // Create replacements with chooseNoise
        List<String> replacements = new ArrayList<>();
        for (String columnName: columnNames) {
            replacements.add(schemaNoise.chooseNoise(columnName));
        }

        // Test if all words have been changed correct
        String replacement1 = replacements.get(0);
        assert replacement1.equals("H.") || replacement1.equals("Hbby") : "The word should have been abbreviated or vowels removed";
        String replacement2 = replacements.get(1);
        assert replacement2.equals("N.") || replacement2.equals("Nm") : "The word should have been abbreviated or vowels removed";
        String replacement3 = replacements.get(2);
        assertEquals("2.", replacement3, "The words should have been abbreviated");
    }

    @Test
    public void testChooseNoise_RemoveVowelsCannotBeApplied() throws Exception {

        // Set SchemaNoise Object with selected methods
        List<String> selectedMethods = new ArrayList<>(Arrays.asList("abbreviateFirstLetters", "removeVowels", "shuffleWords"));
        SchemaNoise schemaNoise = new SchemaNoise(selectedMethods);

        // Set List with column Names
        List<String> columnNames = new ArrayList<>(Arrays.asList("Hbby", "Nm prs", "kl js", "HP TS"));

        // Create replacements with chooseNoise
        List<String> replacements = new ArrayList<>();
        for (String columnName: columnNames) {
            replacements.add(schemaNoise.chooseNoise(columnName));
        }

        // Test if all words have been changed correct
        String replacement1 = replacements.get(0);
        assertEquals("H.", replacement1, "The words should have been abbreviated");
        String replacement2 = replacements.get(1);
        assertEquals("prs_Nm", replacement2, "The words should have been shuffled");
        String replacement3 = replacements.get(2);
        assertEquals("K.J.", replacement3, "The words should have been abbreviated");
        String replacement4 = replacements.get(3);
        assertEquals("TS_HP", replacement4, "The words should have been shuffled");
    }

    @Test
    public void testChooseNoise_RefillOptionsAfterShuffleMethod() throws Exception {

        // Set SchemaNoise Object with selected methods
        List<String> selectedMethods = new ArrayList<>(Arrays.asList("shuffleWords", "replaceWithTranslation"));
        SchemaNoise schemaNoise = new SchemaNoise(selectedMethods);

        // Set List with column Names
        List<String> columnNames = new ArrayList<>(Arrays.asList("person name", "telephone number", "languages", "the address"));

        // Create replacements with chooseNoise
        List<String> replacements = new ArrayList<>();
        for (String columnName: columnNames) {
            replacements.add(schemaNoise.chooseNoise(columnName));
        }

        // Test if all words have been changed correct
        String replacement1 = replacements.get(0);
        assertEquals("name_person", replacement1, "The words should have been shuffled");
        String replacement2 = replacements.get(1);
        assertEquals("Telefonnummer", replacement2, "The words should have been translated");
        String replacement3 = replacements.get(2);
        assertEquals("Sprachen", replacement3, "The words should have been shuffled");
        String replacement4 = replacements.get(3);
        assertEquals("address_the", replacement4, "The words should have been translated");
    }

    @Test
    public void testChooseNoise_OnlySpecialMethods() throws Exception {

        // Set SchemaNoise Object with selected methods
        List<String> selectedMethods = new ArrayList<>(Arrays.asList("removeVowels", "shuffleWords"));
        SchemaNoise schemaNoise = new SchemaNoise(selectedMethods);

        // Set List with column Names
        List<String> columnNames = new ArrayList<>(Arrays.asList("Person name", "salary person", "Hobbys", "phone number"));

        // Create replacements with chooseNoise
        List<String> replacements = new ArrayList<>();
        for (String columnName: columnNames) {
            replacements.add(schemaNoise.chooseNoise(columnName));
        }

        // Test if all words have been changed correct
        String replacement1 = replacements.get(0);
        assertEquals("name_Person", replacement1, "The words should have been abbreviated");
        String replacement2 = replacements.get(1);
        assertEquals("slry prsn", replacement2, "The words should have been translated");
        String replacement3 = replacements.get(2);
        assertEquals("Hbbys", replacement3, "The words should have been abbreviated");
        String replacement4 = replacements.get(3);
        assertEquals("number_phone", replacement4, "The words should have been abbreviated");
    }

    @Test
    public void testChooseNoise_UseFallback() throws Exception {

        // Set SchemaNoise Object with selected methods
        List<String> selectedMethods = new ArrayList<>(Arrays.asList("replaceWithSynonyms", "shuffleWords"));
        SchemaNoise schemaNoise = new SchemaNoise(selectedMethods);

        // Set List with column Names
        List<String> columnNames = new ArrayList<>(Arrays.asList("dsopdksp", "shuffle me"));

        // Create replacements with chooseNoise
        List<String> replacements = new ArrayList<>();
        for (String columnName: columnNames) {
            replacements.add(schemaNoise.chooseNoise(columnName));
        }

        // Test if all words have been changed correct
        String replacement1 = replacements.get(0);
        assertNotEquals("dsopdksp", replacement1, "The word was not shuffled or replaced with synonyme");
        String replacement2 = replacements.get(1);
        assertEquals("me_shuffle", replacement2, "The word should have been shuffled");
    }

    @Test
    public void testChooseNoise_reuseShuffleMethodFromBottom() throws Exception {

        // Set SchemaNoise Object with selected methods
        List<String> selectedMethods = new ArrayList<>(Arrays.asList("replaceWithSynonyms", "removeVowels", "shuffleWords"));
        SchemaNoise schemaNoise = new SchemaNoise(selectedMethods);

        // Set List with column Names
        List<String> columnNames = new ArrayList<>(Arrays.asList("Person name", "salary person", "jsde lk", "number"));

        // Create replacements with chooseNoise
        List<String> replacements = new ArrayList<>();
        for (String columnName: columnNames) {
            replacements.add(schemaNoise.chooseNoise(columnName));
        }

        // Test if all words have been changed correct
        String replacement1 = replacements.get(0);
        assertEquals("name_Person", replacement1, "The words should have been shuffled");
        String replacement2 = replacements.get(1);
        assertEquals("slry prsn", replacement2, "The words vowels should have been removed");
        String replacement3 = replacements.get(2);
        assertEquals("lk_jsde", replacement3, "The words should have been shuffled");
        String replacement4 = replacements.get(3);
        assertEquals("identification number", replacement4, "The words should have been replaced with synonyme");
    }

    @Test
    public void testChooseNoise_reuseVowelMethodFromBottom() throws Exception {

        // Set SchemaNoise Object with selected methods
        List<String> selectedMethods = new ArrayList<>(Arrays.asList("replaceWithSynonyms", "removeVowels"));
        SchemaNoise schemaNoise = new SchemaNoise(selectedMethods);

        // Set List with column Names
        List<String> columnNames = new ArrayList<>(Arrays.asList("Person", "sjeed", "number", "kooo"));

        // Create replacements with chooseNoise
        List<String> replacements = new ArrayList<>();
        for (String columnName: columnNames) {
            replacements.add(schemaNoise.chooseNoise(columnName));
        }

        // Test if all words have been changed correct
        String replacement1 = replacements.get(0);
        assertEquals("Prsn", replacement1, "The words should have been shuffled");
        String replacement2 = replacements.get(1);
        assertEquals("sjd", replacement2, "The words vowels should have been removed");
        String replacement3 = replacements.get(2);
        assertEquals("identification number", replacement3, "The words should have been shuffled");
        String replacement4 = replacements.get(3);
        assertEquals("k", replacement4, "The words should have been replaced with synonyme");
    }
}