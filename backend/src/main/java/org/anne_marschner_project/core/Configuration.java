package org.anne_marschner_project.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.databind.JsonNode;


public class Configuration {

    public String directoryPath;
    public String inputName;
    public String outputName;

    public boolean horizontalSplit;
    public int rowOverlap;
    public boolean verticalSplit;
    public int columnOverlap;

    public boolean dataNoise;
    public int dataNoisePercentage;
    public boolean schemaNoise;
    public int schemaNoisePercentage;

    public boolean normalizeResultOne;
    public boolean normalizeResultTwo;
    public boolean breakPK;

    public Configuration() {}

    // Constructor
    public Configuration(String jsonFilePath) { // hier anpassen von wo die Config eingelesen wird
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Read JSON-File to initialize fields
            Configuration config = objectMapper.readValue(new File(jsonFilePath), Configuration.class);
            this.directoryPath = config.directoryPath;
            this.inputName = config.inputName;
            this.outputName = config.outputName;
            this.horizontalSplit = config.horizontalSplit;
            this.rowOverlap = config.rowOverlap;
            this.verticalSplit = config.verticalSplit;
            this.columnOverlap = config.columnOverlap;
            this.dataNoise = config.dataNoise;
            this.dataNoisePercentage = config.dataNoisePercentage;
            this.schemaNoise = config.schemaNoise;
            this.schemaNoisePercentage = config.schemaNoisePercentage;
            this.normalizeResultOne = config.normalizeResultOne;
            this.normalizeResultTwo = config.normalizeResultTwo;
            this.breakPK = config.breakPK;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> initializeSchemaMethodsFromJson(String jsonFilePath) throws Exception {
        // Lade die JSON-Datei
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode methodsNode = objectMapper.readTree(new File(jsonFilePath));
        List<String> allSelectedMethods = new ArrayList<>();

        // Füge alle ausgewählten Methoden der Liste hinzu
        if (methodsNode.get("generateRandomString").asBoolean()) {
            allSelectedMethods.add("generateRandomString");
        }
        if (methodsNode.get("removeVowels").asBoolean()) {
            allSelectedMethods.add("removeVowels");
        }
        if (methodsNode.get("abbreviateFirstLetters").asBoolean()) {
            allSelectedMethods.add("abbreviateFirstLetters");
        }
        if (methodsNode.get("abbreviateRandomLength").asBoolean()) {
            allSelectedMethods.add("abbreviateRandomLength");
        }
        if (methodsNode.get("addRandomPrefix").asBoolean()) {
            allSelectedMethods.add("addRandomPrefix");
        }
        if (methodsNode.get("shuffleLetters").asBoolean()) {
            allSelectedMethods.add("shuffleLetters");
        }
        if (methodsNode.get("shuffleWords").asBoolean()) {
            allSelectedMethods.add("shuffleWords");
        }
        if (methodsNode.get("replaceWithSynonyms").asBoolean()) {
            allSelectedMethods.add("replaceWithSynonyms");
        }
        if (methodsNode.get("replaceWithTranslation").asBoolean()) {
            allSelectedMethods.add("replaceWithTranslation");
        }

        // Kopiere die Liste für die Rotationslogik
        return  allSelectedMethods;
    }

    public static List<String> initializeDataMethodsFromJson(String jsonFilePath) throws Exception {
        // Lade die JSON-Datei
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode methodsNode = objectMapper.readTree(new File(jsonFilePath));
        List<String> allSelectedMethods = new ArrayList<>();

        // Füge alle ausgewählten Methoden der Liste hinzu
        if (methodsNode.get("replaceWithSynonyms").asBoolean()) {
            allSelectedMethods.add("replaceWithSynonyms");
        }
        if (methodsNode.get("addRandomPrefix").asBoolean()) {
            allSelectedMethods.add("addRandomPrefix");
        }
        if (methodsNode.get("replaceWithTranslation").asBoolean()) {
            allSelectedMethods.add("replaceWithTranslation");
        }
        if (methodsNode.get("shuffleWords").asBoolean()) {
            allSelectedMethods.add("shuffleWords");
        }
        if (methodsNode.get("generateMissingValue").asBoolean()) {
            allSelectedMethods.add("generateMissingValue");
        }
        if (methodsNode.get("generatePhoneticError").asBoolean()) {
            allSelectedMethods.add("generatePhoneticError");
        }
        if (methodsNode.get("generateOCRError").asBoolean()) {
            allSelectedMethods.add("generateOCRError");
        }
        if (methodsNode.get("abbreviateDataEntry").asBoolean()) {
            allSelectedMethods.add("abbreviateDataEntry");
        }
        if (methodsNode.get("changeFormat").asBoolean()) {
            allSelectedMethods.add("changeFormat");
        }
        if (methodsNode.get("generateTypingError").asBoolean()) {
            allSelectedMethods.add("generateTypingError");
        }
        if (methodsNode.get("changeValue").asBoolean()) {
            allSelectedMethods.add("changeValue");
        }
        if (methodsNode.get("changeValueToOutlier").asBoolean()) {
            allSelectedMethods.add("changeValueToOutlier");
        }

        // Kopiere die Liste für die Rotationslogik
        return  allSelectedMethods;
    }

    private static void writeToFile(FileWriter writer, String line) {
        try {
            writer.write(String.format("%s%n", line));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    /*

    public List<List<Integer>> normalize(String filepath, char separator, String outputFilepath) {

        // List to save the column indices of each new Relation
        List<List<Integer>> columnIndices = new ArrayList<>();

        CustomNormiConfig conf = new CustomNormiConfig(filepath, separator, outputFilepath);

        try {
            Normi normi = new Normi();

            RelationalInputGenerator relationalInputGenerator = null;
            ResultCache resultReceiver = new ResultCache("MetanomeMock", null);

            relationalInputGenerator = new DefaultFileInputGenerator(new ConfigurationSettingFileInput(
                    conf.inputFilepath, true,
                    conf.inputFileSeparator, conf.inputFileQuotechar, conf.inputFileEscape, conf.inputFileStrictQuotes,
                    conf.inputFileIgnoreLeadingWhiteSpace, conf.inputFileSkipLines, conf.inputFileHasHeader, conf.inputFileSkipDifferingLines, conf.inputFileNullString));

            normi.setRelationalInputConfigurationValue(Normi.Identifier.INPUT_GENERATOR.name(), relationalInputGenerator);
            normi.setResultReceiver(resultReceiver);

            // A human in the loop works only outside of Metanome. Hence, this is not a Metanome parameter
            normi.setIsHumanInTheLoop(conf.isHumanInTheLoop);

            normi.execute();

            if (conf.writeResults) {
                final String outputPath = conf.outputPath;
                List<Result> results = resultReceiver.fetchNewResults();
                columnIndices = getColumnIndices(results);
                Stream<Result> resultsStream = results.stream();

                final File resultFile = new File(outputPath);
                FileUtils.createFile(outputPath, true);
                final FileWriter writer = new FileWriter(resultFile, false);
                resultsStream.map(result -> result.toString()).forEach(fd -> writeToFile(writer, fd));

                writer.close();
                resultsStream.close();
            }
        }
        catch (AlgorithmExecutionException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return columnIndices;
    }

    public List<List<Integer>> getColumnIndices(List<Result> results) {

        // Patterns that need to be found to extract indices
        Pattern bracketPattern = Pattern.compile("\\[(.*?)\\]");
        Pattern columnPattern = Pattern.compile("column(\\d+)");

        // List to save the column indices of each new Relation
        List<List<Integer>> columnIndices = new ArrayList<>();

        for (Result result : results) {
            System.out.println(result.toString());
            // add new List for relation
            List<Integer> indicesOfRelation = new ArrayList<>();

            // extract indices from result-string
            Matcher bracketMatcher = bracketPattern.matcher(result.toString());

            if (bracketMatcher.find()) {
                // Get content of inner bracket
                String insideBrackets = bracketMatcher.group(1);
                Matcher columnMatcher = columnPattern.matcher(insideBrackets);

                // Get all indices and save as integer (convert to startindex 0)
                while (columnMatcher.find()) {
                    int number = Integer.parseInt(columnMatcher.group(1)) - 1;
                    indicesOfRelation.add(number);
                }
            }
            columnIndices.add(indicesOfRelation);
        }
        return columnIndices;
    }

    public static void main(String[] args) throws IOException {
        Configuration conf = new Configuration();
        String filepath = "C:/Users/Marschner/Documents/Studium/Bachelorarbeit/Datensätze/meine/tempself-made.csv";
        char separator = ',';
        String outputFilepath = "C:/Users/Marschner/Documents/Studium/Bachelorarbeit/Datensätze/meine/self-made.txt";;
        List<List<Integer>> columnIndices = conf.normalize(filepath, separator, outputFilepath);
        System.out.println(columnIndices);
    }

     */
}
