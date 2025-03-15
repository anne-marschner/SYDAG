package org.anne_marschner_project.core.structure;

/**
 * The CustomNormiConfig class represents a configuration for files for the Normalize algorithm.
 */
public class CustomNormiConfig {

    public String inputFilepath;
    public char inputFileSeparator;
    public char inputFileQuotechar = '\"';
    public char inputFileEscape = '\\';
    public int inputFileSkipLines = 0;
    public boolean inputFileStrictQuotes = false;
    public boolean inputFileIgnoreLeadingWhiteSpace = true;
    public boolean inputFileHasHeader = false;
    public boolean inputFileSkipDifferingLines = true;
    public String inputFileNullString = "";
    public boolean isHumanInTheLoop = false;


    /**
     * Constructs a CustomNormiConfig object with a specified file path and separator.
     *
     * @param filepath the file path of the input file to be processed.
     * @param separator the character used to separate columns in the input file.
     */
    public CustomNormiConfig(String filepath, char separator) {
        this.inputFilepath = filepath;
        this.inputFileSeparator = separator;
    }
}
