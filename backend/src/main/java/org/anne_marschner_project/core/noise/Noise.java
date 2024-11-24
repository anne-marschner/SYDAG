package org.anne_marschner_project.core.noise;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The Noise class provides various methods to generate and apply noise to data.
 * This class is designed to create simulated data entry errors or variations.
 */
public class Noise {

    // Final Strings for Noise Generation
    private final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private final String SYNONYME_API_URL = "https://api.datamuse.com/words?rel_syn=";
    private final String TRANSLATION_API_URL = "https://api.mymemory.translated.net/get?q=";
    public final String[] replaceableSymbols = {"-", "_", ".", " "};
    public final Map<Character, Character> phoneticSimilarities = new HashMap<>() {{
        put('a', 'e'); put('e', 'a'); put('i', 'y'); put('y', 'i'); put('c', 'k'); put('k', 'c'); put('s', 'z'); put('z', 's');
        put('g', 'j'); put('j', 'g'); put('t', 'd'); put('d', 't'); put('p', 'b'); put('b', 'p'); put('f', 'v'); put('v', 'f');}};

    public final Map<Character, Character> ocrSimilarities = new HashMap<>() {{
        put('0', 'O'); put('O', '0'); put('1', 'I'); put('I', '1'); put('5', 'S'); put('S', '5');
        put('8', 'B'); put('B', '8'); put('2', 'Z'); put('Z', '2'); put('V', 'U'); put('U', 'V');
    }};

    public final Map<Character, List<Character>> keyboardSimilarities = new HashMap<>() {{
        put('a', Arrays.asList('q', 'w', 'z', 's', 'x'));
        put('b', Arrays.asList('f', 'g', 'h', 'v', 'n'));
        put('c', Arrays.asList('s', 'x', 'd', 'f', 'v'));
        put('d', Arrays.asList('w', 'e', 'r', 's', 'x', 'c', 'f', 'v'));
        put('e', Arrays.asList('w', 'r', 's', 'd', 'f', '2', '3', '4'));
        put('f', Arrays.asList('e', 'r', 't', 'd', 'c', 'b', 'g', 'v'));
        put('g', Arrays.asList('r', 't', 'y', 'f', 'b', 'h', 'v', 'n'));
        put('h', Arrays.asList('t', 'y', 'u', 'b', 'm', 'j', 'g', 'n'));
        put('i', Arrays.asList('u', 'o', 'j', 'k', 'l', '7', '8', '9'));
        put('j', Arrays.asList('y', 'u', 'i', 'm', 'h', 'k', 'n'));
        put('k', Arrays.asList('u', 'i', 'o', 'm', 'j', 'l'));
        put('l', Arrays.asList('i', 'o', 'p', 'k'));
        put('m', Arrays.asList('j', 'h', 'k', 'n'));
        put('n', Arrays.asList('b', 'm', 'j', 'g', 'h'));
        put('o', Arrays.asList('i', 'p', 'k', 'l', '8', '9', '0'));
        put('p', Arrays.asList('o', 'l', '9', '0'));
        put('q', Arrays.asList('w', 'a', 's', '1', '2'));
        put('r', Arrays.asList('e', 't', 'd', 'f', 'g', '3', '4', '5'));
        put('s', Arrays.asList('q', 'w', 'e', 'a', 'z', 'x', 'd', 'c'));
        put('t', Arrays.asList('r', 'y', 'f', 'g', 'h', '4', '5', '6'));
        put('u', Arrays.asList('y', 'i', 'j', 'h', 'k', '6', '7', '8'));
        put('v', Arrays.asList('d', 'c', 'f', 'b', 'g'));
        put('w', Arrays.asList('q', 'e', 'a', 's', 'd', '1', '2', '3'));
        put('x', Arrays.asList('a', 'z', 's', 'd', 'c'));
        put('y', Arrays.asList('t', 'u', 'j', 'g', 'h', '5', '6', '7'));
        put('z', Arrays.asList('a', 's', 'x'));
        put('1', Arrays.asList('q', 'w', '2'));
        put('2', Arrays.asList('q', 'w', 'e', '1', '3'));
        put('3', Arrays.asList('w', 'e', 'r', '2', '4'));
        put('4', Arrays.asList('e', 'r', 't', '3', '5'));
        put('5', Arrays.asList('r', 't', 'y', '4', '6'));
        put('6', Arrays.asList('t', 'y', 'u', '5', '7'));
        put('7', Arrays.asList('y', 'u', 'i', '6', '8'));
        put('8', Arrays.asList('u', 'i', 'o', '7', '9'));
        put('9', Arrays.asList('i', 'o', 'p', '8', '0'));
        put('0', Arrays.asList('o', 'p', '9'));
    }};


    /**
     * Generates a random alphanumeric string of random length between 1 and 10.
     *
     * @return a randomly generated string
     */
    public String generateRandomString() {

        Random random = new Random();

        // Chose random length between 1 and 10
        int length = random.nextInt(10) + 1;

        // StringBuilder for random String
        StringBuilder randomString = new StringBuilder(length);

        // Fill String with random letters or numbers
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            randomString.append(characters.charAt(index));
        }

        return randomString.toString();
    }


    /**
     * Removes all vowels from the provided text.
     *
     * @param entry the input string from which vowels are removed
     * @return the resulting string without vowels
     */
    public String removeVowels(String entry) {
        return entry.replaceAll("[aeiouAEIOU]", "");
    }


    /**
     * Abbreviates a string by taking the first letter of each word, separated by periods.
     *
     * @param entry the input string to abbreviate
     * @return the abbreviation of the first letters of each word, in uppercase
     */
    public String abbreviateFirstLetters(String entry) {

        // Separate String into words with whitespace or "_" or "-" as separator
        String[] words = entry.split("[\\s-_]+");

        StringBuilder abbreviation = new StringBuilder();

        // Iterate words and take the first letter of each word
        for (String word : words) {
            if (!word.isEmpty()) {
                abbreviation.append(word.charAt(0)).append(".");
            }
        }

        return abbreviation.toString().toUpperCase();
    }


    /**
     * Abbreviates each word in the string to a random length between 1 and 2/3 of the word's length.
     *
     * @param entry the input string to abbreviate
     * @return a string of concatenated word abbreviations
     */
    public String abbreviateRandomLength(String entry) {

        // Separate String into words with whitespace or "_" or "-" as separator
        String[] words = entry.split("[\\s-_]+");

        StringBuilder abbreviation = new StringBuilder();
        Random random = new Random();

        // Append a part with random length of each word to the StringBuilder
        for (String word : words) {

            // Bound is the index of the last letter that should be included in abbreviation
            int bound;

            // Calculate how many letters of the word should be kept
            if (word.length() > 1) {
                // Calculate random bound if word has more than one letter (2/3 is the limit)
                bound = random.nextInt((int) Math.ceil(word.length() / 1.5));
            } else {
                // Bound is clear is word only has one letter
                bound = 0;
            }

            // Add the letters to abbreviation (up to index "bound")
            if (bound > 0) {
                abbreviation.append(Character.toUpperCase(word.charAt(0))).append(word, 1, bound + 1);
            } else {
                abbreviation.append(Character.toUpperCase(word.charAt(bound)));
            }
        }

        return abbreviation.toString();
    }


    /**
     * Adds a random alphanumeric prefix of length 1 to 4 to the input string.
     *
     * @param entry the input string to which a prefix is added
     * @return the input string prefixed with a random alphanumeric string
     */
    public String addRandomPrefix(String entry) {

        Random random = new Random();

        // Chose random length between 1 and 10
        int length = random.nextInt(4) + 1;

        StringBuilder prefix = new StringBuilder(length);

        // Set new prefix
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            prefix.append(characters.charAt(index));
        }

        return  prefix + "_" + entry;
    }


    /**
     * Shuffles the letters in a string while avoiding the original order.
     *
     * @param entry the input string to shuffle
     * @return a new string with shuffled letters
     */
    public String shuffleLetters(String entry) {

        // If word has only one character it cannot be shuffled
        if (entry.length() <= 1) {
            return entry;
        }

        // Convert String to a List<Character> using Stream
        List<Character> charList = entry.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.toList());

        // Create a StringBuilder and append shuffled characters
        StringBuilder shuffled;

        do {
            // Shuffle the list
            Collections.shuffle(charList);

            // Create a StringBuilder and append shuffled characters
            shuffled = new StringBuilder(entry.length());
            for (Character c : charList) {
                shuffled.append(c);
            }
        } while (shuffled.toString().equals(entry)); // Repeat if order is equal to original string

        // Return the shuffled string
        return shuffled.toString();
    }


    /**
     * Shuffles the words in the string, separated by whitespace, hyphens, or underscores.
     *
     * @param entry the input string to shuffle
     * @return a new string with shuffled words
     */
    public String shuffleWords(String entry) {

        // Separate String into words with whitespace or "_" or as separator
        String[] words = entry.split("[\\s-_]+");

        // Handle case where there's only one word or the input is empty
        if (words.length <= 1) {
            return entry;
        }

        // Convert Array to List and shuffle the words
        List<String> wordsList = Arrays.asList(words);
        List<String> shuffledList;

        do {
            // Copy the original list and shuffle it
            shuffledList = new ArrayList<>(wordsList);
            Collections.shuffle(shuffledList);
        } while (shuffledList.equals(wordsList)); // Repeat if order is the same

        // Create StringBuilder to save new order of words
        StringBuilder randomized = new StringBuilder();

        // Go through all words and add them to StringBuilder
        for (int i = 0; i < shuffledList.size(); i++) {
            randomized.append(shuffledList.get(i));
            // Only add "_" if it is not the last word
            if (i < shuffledList.size() - 1) {
                randomized.append("_");
            }
        }

        return randomized.toString();
    }


    /**
     * Replaces each word in the input string with a synonym retrieved from the Datamuse API.
     *
     * @param entry the input string to replace with synonyms
     * @return the string with words replaced by their synonyms
     * @throws Exception if there is an issue accessing the Datamuse API
     */
    public String replaceWithSynonyms(String entry) throws Exception {

        // Remove all special characters and put words into array
        String cleanedEntry = entry.replaceAll("[^a-zA-Z\\s-_]", "");
        String[] words = cleanedEntry.split("[\\s-_]+");

        // Create a StringBuilder to append words
        StringBuilder synonyms = new StringBuilder();

        // Go through all words and add them to StringBuilder
        for (int i = 0; i < words.length; i++) {
            synonyms.append(getSynonymFromAPI(words[i]));
            // Only add " " if it is not the last word
            if (i < words.length - 1) {
                synonyms.append(" ");
            }
        }

        return synonyms.toString();
    }


    /**
     * Retrieves a synonym for a word using the Datamuse API.
     *
     * @param word the word for which to find a synonym
     * @return a synonym of the word, or an empty string if none is found
     * @throws Exception if there is an issue accessing the API
     */
    public String getSynonymFromAPI(String word) {

        // Get Synonyms for word from Datamuse API
        String urlString = SYNONYME_API_URL + word;

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Check if the response code is OK (200)
            int responseCode = conn.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                return "";  // Return "entry" if API does not respond with 200
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            // Add all synonyms
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Process JSON-Answer with Jackson
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonArray = objectMapper.readTree(response.toString());

            // Check if the array is not empty
            if (jsonArray.isArray() && jsonArray.size() > 0) {
                // Get the last synonym in the array
                JsonNode lastSynonym = jsonArray.get(jsonArray.size() - 1); // Get last element
                return lastSynonym.get("word").asText(); // Return the word
            } else {
                return ""; // If no synonyms were found return empty string
            }
        } catch (Exception e) {
            // Catch any exception and return "" as fallback
            return "";
        }
    }


    /**
     * Translates a string from a source language to a target language using the MyMemory API.
     *
     * @param text the text to translate
     * @param sourceLang the source language code (e.g., "en" for English)
     * @param targetLang the target language code (e.g., "de" for German)
     * @return the translated text
     * @throws Exception if there is an issue accessing the MyMemory API
     */
    public String replaceWithTranslation(String text, String sourceLang, String targetLang) throws Exception {

        // Set URL for Translation
        String urlString = TRANSLATION_API_URL + URLEncoder.encode(text, "UTF-8")
                + "&langpair=" + sourceLang + "|" + targetLang;

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            // Append translation to StringBuilder
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Process JSON-Answer
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonResponse = objectMapper.readTree(response.toString());

            return jsonResponse.get("responseData").get("translatedText").asText();
        } catch (Exception e) {
            return text;
        }
    }


    /**
     * Generates a missing value representation for the input entry.
     * If the entry is empty, returns a dash ("-"); otherwise, returns an empty string.
     *
     * @param entry The input entry to check.
     * @return A dash if entry is empty, otherwise an empty string.
     */
    public String generateMissingValue(String entry) {
        if (entry.isEmpty()) {
            return "-";
        } else {
            return "";
        }
    }


    /**
     * Introduces a phonetic error in the entry by replacing a randomly chosen character
     * with a phonetically similar character based on a predefined map.
     * If no replacement is possible, the original entry is returned.
     *
     * @param entry The input string to modify.
     * @return The modified string with a phonetic error, or the original entry if no errors can be introduced.
     */
    public String generatePhoneticError(String entry) {

        // Create List of all possible positions for phonetic Error
        List<Integer> possibleErrorPositions = new ArrayList<>();
        for (int i = 0; i < entry.length(); i++) {
            char originalLetter = entry.charAt(i);
            char lowerCaseLetter = Character.toLowerCase(originalLetter);
            if (phoneticSimilarities.containsKey(lowerCaseLetter)) {
                possibleErrorPositions.add(i);
            }
        }

        // Check if a phonetic Error can be included
        if (possibleErrorPositions.isEmpty()) {
            return entry;
        }

        // Choose a random position for the Error
        Random random = new Random();
        int errorPosition = possibleErrorPositions.get(random.nextInt(possibleErrorPositions.size()));

        // Add Error to String
        StringBuilder replacement = new StringBuilder();
        for (int i = 0; i < entry.length(); i++) {
            char originalLetter = entry.charAt(i);
            char lowerCaseLetter = Character.toLowerCase(originalLetter);

            if (i == errorPosition) {
                char replacementLetter = phoneticSimilarities.get(lowerCaseLetter);
                replacement.append(Character.isUpperCase(originalLetter) ? Character.toUpperCase(replacementLetter) : replacementLetter);
            } else {
                replacement.append(originalLetter);
            }
        }

        return replacement.toString();
    }


    /**
     * Introduces an OCR error in the input by randomly replacing a character with a visually similar one
     * based on a predefined OCR similarity map. If no replacement is possible, the original entry is returned.
     *
     * @param input The input string to modify.
     * @return The modified string with an OCR error, or the original input if no errors can be introduced.
     */
    public String generateOCRError(String input) {

        // Create list of the possible positions of OCR Error
        List<Integer> possibleErrorPositions = new ArrayList<>();
        for (int i = 0; i < input.length(); i++) {
            char letter = input.charAt(i);
            if (ocrSimilarities.containsKey(letter)) {
                possibleErrorPositions.add(i);
            }
        }

        // Check if an OCR Error can be included
        if (possibleErrorPositions.isEmpty()) {
            return input;
        }

        // Choose a random position for the OCR Error
        Random random = new Random();
        int errorPosition = possibleErrorPositions.get(random.nextInt(possibleErrorPositions.size()));

        // Add the OCR Error
        StringBuilder replacement = new StringBuilder(input);
        char originalChar = input.charAt(errorPosition);
        char errorChar = ocrSimilarities.get(originalChar);
        replacement.setCharAt(errorPosition, errorChar);

        return replacement.toString();
    }


    /**
     * Abbreviates a given data entry by retaining a portion of each word and appending a period.
     * The abbreviation retains a random length of each word up to two-thirds of the word length.
     *
     * @param entry The input string to abbreviate.
     * @return The abbreviated string representation.
     */
    public String abbreviateDataEntry(String entry) {

        // Separate String into words with whitespace or "_" or "-" as separator
        String[] words = entry.split("[\\s-_]+");

        StringBuilder abbreviation = new StringBuilder();
        Random random = new Random();

        // Append a part with random length of each word to the StringBuilder
        for (String word : words) {

            // Bound is the index of the last letter that should be included in abbreviation
            int bound;

            // Calculate how many letters of the word should be kept
            if (word.length() > 1) {
                // Calculate random bound if word has more than one letter (2/3 is the limit)
                bound = random.nextInt((int) Math.ceil(word.length() / 1.5));
            } else {
                // Bound is clear is word only has one letter
                bound = 0;
            }

            // Add the letters to abbreviation (up to index "bound")
            if (bound > 0) {
                abbreviation.append(Character.toUpperCase(word.charAt(0))).append(word, 1, bound + 1).append(".");
            } else {
                abbreviation.append(Character.toUpperCase(word.charAt(bound))).append(".");
            }
        }

        return abbreviation.toString();
    }


    /**
     * Alters the format of the input entry by replacing certain symbols with random, predefined alternatives.
     * For example, replaces hyphens with underscores or other symbols based on a predefined set.
     *
     * @param entry The input string to modify.
     * @return The modified string with altered symbols, or the original entry if no replacements are made.
     */
    public String changeFormat(String entry) {

        StringBuilder result = new StringBuilder(entry);
        Random random = new Random();
        String replacementSymbol = null;
        String oldSymbol = null;

        // Search for all Symbols that can be replaced
        for (int i = 0; i < result.length(); i++) {
            String current = String.valueOf(result.charAt(i));

            if (replacementSymbol == null) {
                // Check if current symbol can be replaced
                for (String symbol : replaceableSymbols) {
                    if (current.equals(symbol)) {
                        // Choose random Character that is different from the one that should be replaced
                        do {
                            replacementSymbol = replaceableSymbols[random.nextInt(replaceableSymbols.length)];
                        } while (replacementSymbol.equals(current));
                        oldSymbol = current;
                    }
                }
            }

            if (current.equals(oldSymbol)) {
                result.setCharAt(i, replacementSymbol.charAt(0));
            }
        }

        return result.toString();
    }


    /**
     * Introduces typing errors by replacing letters in the entry with characters close to them on the keyboard.
     * Symbols not found in the keyboard similarity map are preserved in their original positions.
     *
     * @param entry The input string to modify.
     * @return The modified string with typing errors, or the original entry if no modifications are possible.
     */
    public String generateTypingError(String entry) {

        // List for symbols that are not part of the keyboardSimilarities Map
        Map<Integer, Character> otherSymbols = new HashMap<>();
        StringBuilder filteredWord = new StringBuilder();
        int length = entry.length();

        // Remove symbols that are not part of keyboardSimilarities Map and safe their position
        for (int i = 0; i < length; i++) {
            char symbol = entry.charAt(i);
            if (!keyboardSimilarities.containsKey(Character.toLowerCase(symbol))) {
                otherSymbols.put(i, symbol); // Save position and symbol
            } else {
                filteredWord.append(symbol); // Add letter
            }
        }

        // Calculate length of filtered word
        length = filteredWord.length();

        // If no modification can be made return original entry
        if (length == 0) {
            return entry;
        }

        // Get random number of typing errors that will be included
        int typoCount = decideNumberOfTypos(length);

        // Include the given number of Typing Errors into filteredWord
        char[] wordWithTypos = addTypos(filteredWord, typoCount, length);

        // Build modified word from filtered word and excluded symbols
        return buildFinalString(wordWithTypos, otherSymbols);
    }


    /**
     * Determines the number of typos to introduce based on the length of the input entry.
     *
     * @param length The length of the entry.
     * @return A random number between 1 and a maximum based on entry length.
     */
    private int decideNumberOfTypos(int length) {

        Random random = new Random();

        // Set number of maximal Typos (1 for length <=5; 2 for length <=10; 3 for all length >10)
        int maxTypos = length <= 5 ? 1 : (length <= 10 ? 2 : 3);

        // Return random number between 1 and maxTypos
        return random.nextInt(maxTypos) + 1;
    }


    /**
     * Introduces a given number of typing errors in the filtered word by replacing letters with keyboard-adjacent ones.
     *
     * @param filteredWord The modified entry with only letters eligible for typos.
     * @param typoCount The number of typing errors to introduce.
     * @param length The length of the filtered word.
     * @return A char array of the entry with typing errors.
     */
    private char[] addTypos(StringBuilder filteredWord, int typoCount, int length) {

        // Create Map to save indices of changed letters to make sure that no letter is not changed multiple times
        Set<Integer> modifiedIndices = new HashSet<>();
        char[] wordArray = filteredWord.toString().toCharArray();
        Random random = new Random();

        // Include Errors
        for (int i = 0; i < typoCount; i++) {

            // Choose position that has not been changed yet
            int index;
            do {
                index = random.nextInt(length);
            } while (modifiedIndices.contains(index));

            // Get original letter at index and the list of possible replacements
            char original = wordArray[index];
            List<Character> nearby = keyboardSimilarities.get(Character.toLowerCase(original));

            // Choose random neighbor as replacement
            char replacement = nearby.get(random.nextInt(nearby.size()));
            wordArray[index] = Character.isUpperCase(original) ? Character.toUpperCase(replacement) : replacement;

            // Mark index as modified
            modifiedIndices.add(index);
        }
        return wordArray;
    }


    /**
     * Reconstructs the original entry with typing errors by combining the modified entry and special symbols.
     *
     * @param wordArray The character array of the modified entry with typing errors.
     * @param otherSymbols A map of positions and characters for non-keyboard symbols.
     * @return The final entry string with typos and preserved symbols.
     */
    private String buildFinalString (char[] wordArray, Map<Integer, Character> otherSymbols) {

        StringBuilder finalString = new StringBuilder();

        int originalIndex = 0;

        // Gehe durch alle Indizes des Strings und füge die Zeichen ein
        for (int i = 0; i < wordArray.length + otherSymbols.size(); i++) {
            if (otherSymbols.containsKey(i)) {
                // Falls ein spezielles Symbol an diesem Index ist, füge es hinzu
                finalString.append(otherSymbols.get(i));
            } else {
                // Ansonsten füge das nächste Zeichen des originalen Strings hinzu
                if (originalIndex < wordArray.length) {
                    finalString.append(wordArray[originalIndex]);
                    originalIndex++;
                }
            }
        }

        return finalString.toString();
    }


    /**
     * Generates a new numeric value based on a Gaussian distribution using the specified mean and standard deviation.
     * Ensures the new value is different from the original entry, with up to five attempts.
     *
     * @param entry The original numeric value as a string.
     * @param mean The mean of the distribution.
     * @param standardDeviation The standard deviation of the distribution.
     * @return A new value as a string or "0" if no unique value can be generated within five attempts.
     */
    public String changeValue(String entry, double mean, double standardDeviation) {

        // Create value from Gaussian-distribution
        Random random = new Random();
        double newValue;
        double entryValue = Double.parseDouble(entry);
        int attempts = 0;

        do {
            // Create a random Gaussian value
            double gaussian = random.nextGaussian();  // Random value from standard normal distribution (mean 0, std dev 1)

            // Scale and shift the gaussian value to match the mean and standard deviation of the data
            newValue = mean + gaussian * standardDeviation;

            // Check if the original entry contains decimal points
            if (!entry.contains(".")) {
                // Round to nearest integer if no decimal points
                newValue = Math.round(newValue);
            } else {
                // Otherwise round to 2 decimal places
                newValue = roundDouble(newValue, 2);
            }

            // If 5 attempts have been made, return "0"
            attempts++;
            if (attempts > 5) {
                return "0";
            }

        } while (newValue == entryValue);

        // If newValue is a whole number, return it as an integer (no decimal point)
        if (newValue % 1 == 0) {
            return String.valueOf((int) newValue);
        } else {
            return String.valueOf(newValue);
        }
    }


    /**
     * Generates an outlier value significantly different from the mean by applying a random factor between 3 and 5.
     * Attempts to ensure the outlier value is unique compared to the original entry, with up to five attempts.
     *
     * @param entry The original numeric value as a string.
     * @param mean The mean of the distribution.
     * @param standardDeviation The standard deviation of the distribution.
     * @return An outlier value as a string or "0" if no unique value can be generated within five attempts.
     */
    public String changeValueToOutlier(String entry, double mean, double standardDeviation) {

        // Create outlier, which strongly differs from mean
        Random random = new Random();
        double outlierValue;
        double entryValue = Double.parseDouble(entry);
        int attempts = 0;

        do {
            // Create random factor between 3 and 5 for calculation
            double factor = 3 + random.nextDouble() * 2;
            outlierValue = mean + factor * standardDeviation;

            // Check if the original entry contains decimal points
            if (!entry.contains(".")) {
                // Round to nearest integer if no decimal points
                outlierValue = Math.round(outlierValue);
            } else {
                // Otherwise round to 2 decimal places
                outlierValue = roundDouble(outlierValue, 2);
            }

            // If 5 attempts have been made, return "0"
            attempts++;
            if (attempts > 5) {
                return "0";
            }

        } while (outlierValue == entryValue);

        // If newValue is a whole number, return it as an integer (no decimal point)
        if (outlierValue % 1 == 0) {
            return String.valueOf((int) outlierValue);
        } else {
            return String.valueOf(outlierValue);
        }
    }


    /**
     * Converts a list of objects to a list of doubles. Non-convertible values are filtered out.
     *
     * @param column The list of objects representing a numeric column.
     * @return A list of doubles with valid numeric values.
     */
    public List<Double> createListOfDouble(List<String> column) {
        return column.stream()
                .map(value -> {
                    try {
                        return Double.valueOf(value); // Try to convert to Double
                    } catch (NumberFormatException e) {
                        return null; // If not possible return null
                    }
                })
                .filter(Objects::nonNull) // Filter all null-values
                .toList();
    }


    /**
     * Calculates the mean of a list of numeric values.
     *
     * @param numericColumn The list of numeric values.
     * @return The mean of the values, or 0.0 if the list is empty.
     */
    public double calculateMean(List<Double> numericColumn) {
        return numericColumn.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }


    /**
     * Calculates the standard deviation of a list of numeric values based on the provided mean.
     *
     * @param numericColumn The list of numeric values.
     * @param mean The mean of the values.
     * @return The standard deviation of the values.
     */
    public double calculateStandardDeviation(List<Double> numericColumn, double mean) {
        double variance = numericColumn.stream().mapToDouble(d -> Math.pow(d - mean, 2)).sum() / numericColumn.size();
        return Math.sqrt(variance);
    }


    /**
     * Rounds a double to a specified number of decimal places.
     *
     * @param value The value to round.
     * @param places The number of decimal places to round to.
     * @return The rounded value.
     */
    public double roundDouble(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }


    /**
     * Maps the unique values of an input column to a sequence of numeric strings ("0", "1", ..., "n").
     *
     * @param inputColumn the input list of strings to be mapped
     * @return a new list where each entry from the input list is replaced by its corresponding
     *         numeric mapping
     */
    public List<String> mapColumn(List<String> inputColumn) {

        Set<String> uniqueValues = new LinkedHashSet<>(inputColumn);

        // Create Mapping
        Map<String, String> dynamicMapping = new LinkedHashMap<>();
        int index = 0;
        for (String value : uniqueValues) {
            dynamicMapping.put(value, String.valueOf(index++));
        }

        // Change each entry
        return inputColumn.stream()
                .map(dynamicMapping::get)
                .collect(Collectors.toList());
    }
}
