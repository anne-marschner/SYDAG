# SYDAG
## Table of Contents
1. [General Information](#general-information)
2. [Technologies](#technologies)
3. [Installation](#installation)
4. [User Instructions](#user-instructions)
5. [FAQ](#faq)

## General Information
SYDAG is a **Sy**nthetic **Da**taset **G**enerator for data integration scenarios.
Users can apply it to create integration scenarios that are customized to their specific needs. The generator works with relational data models. The user must provide a relation stored as a CSV file. SYDAG processes this relation to create an integration scenario with multiple datasets and relations.
The user can choose from various configuration options to achieve a specific result.
We provide a graphical user interface for easy use of SYDAG. The output includes the generated datasets, a mapping file, and a key file for each dataset. The mapping file specifies which attributes in the new relations correspond to the original attributes. The key files contain information about key and foreign key relationships.

## Technologies
A list of technologies used within the project:
* [Normalize](https://hpi.de/naumann/projects/data-profiling-and-analytics/metanome-data-profiling/algorithms.html): Version 1.2 
* [HyUCC](https://hpi.de/naumann/projects/data-profiling-and-analytics/metanome-data-profiling/algorithms.html): Version 1.2
* [Datamuse](https://www.datamuse.com/api/): Version 1.1
* [MyMemory](https://mymemory.translated.net/doc/spec.php)
* [Java](https://www.oracle.com/java/technologies/downloads/#java21): Version 21
* [Spring Boot](https://spring.io/projects/spring-boot): Version 3.3.4
* [TypeScript](https://www.npmjs.com/package/typescript): Version 5.7.3
* [React](https://react.dev/): Version 19.0.0
* [Next.js](https://nextjs.org/): Version 15.1.5
  
Basis of the frontend implementation:   

* [Multi-step form](https://github.com/Marcosfitzsimons/multi-step-form?tab=readme-ov-file#author)


## Installation
To install SYDAG the user must clone the GitHub repository. It is necessary to install React and Next.js in the frontend folder. Open a terminal in the IDE of your choice (e.g. VSCode) and navigate to 'SYDAG/frontend'. There you need to run the the following commands:
* `npm install --save-exact react@^19.0.0 react-dom@^19.0.0`
* `npm install --save-exact @types/react@^19.0.0 @types/react-dom@^19.0.0`
* `npx @next/codemod@canary upgrade latest`
* `npm install` 

The project uses Docker for fast and easy deployment. Therefore, users must execute the [Docker Compose file](https://github.com/anne-marschner/SYDAG/blob/main/docker-compose.yml) to build the application on their device. SYDAG can then be started through the Docker environment.

## User Instructions
The configuration of SYDAG includes several parameters that the user must adjust.
In the following we provide an overview of all parameters and their effects on the generation. Our explanation follows the grouping of the parameters in the interface. 

### Input File
* **csvFile**: An input relation stored as a CSV-file. 
* **hasHeaders**: Whether the relation has headers. 
* **separator**:  The character used as separator in the CSV-file. 
* **quote**: The character used for quotes in the CSV-file. 
* **escape**: The character used as escape sequence in the CSV-file.

### Configuration Type
* **jsonFile**: A JSON-file that includes all configuration parameters. 
* **mode**: The mode of setting the configuration (manual setting or upload of a JSON-file).

### Split
* **splitType**: The type of split to be applied to the input relation (horizontal, vertical or both). 
* **rowOverlapPercentage**: The percentage of rows that should overlap between the split relations. 
* **columnOverlapPercentage**: The percentage of columns that should overlap between the split relations. 
* **rowDistribution**:  The distribution of the non-overlapping rows between the new relations. This percentage describes how many non-overlapping rows will be part of one of the new relations (0-100). 
* **columnDistribution**: The distribution of the non-overlapping columns between the new relations. This percentage describes how many non-overlapping columns will be part of one of the new relations (0-100). 
* **overlapType**: The way the overlapping rows should be picked (as block overlap or random overlap).

### Structure
For each new Dataset X: 
* **datasetXStructureType**: The structure change that will be applied to the new datsets (join of columns, normalization to BCNF or no change).
* **datasetXBCNFSliderValue**: The percentage that determines how many of the possible decomposition steps should be executed (0-100). 
* **datasetXJoinColumnsSliderValue**: The percentage of overlapping columns to join (0-100).

### Schema Noise
For each new Dataset X:
* **datasetXSchemaNoise**: Whether the schema should include errors or not (true or false). 
* **datasetXSchemaNoiseValue**: The percentage that determines how many of the attributes overlapping with other relations the generator will change (0-100). 
* **datasetXSchemaKeyNoise**: Whether key columns should also be affected by the noise (true or false). 
* **datasetXSchemaDeleteSchema**: Whether the schema should be deleted (true or false). 
* **datasetXSchemaMultiselect**: The error methods that are chosen ("generateRandomString", "abbreviateFirstLetters", "abbreviateRandomLength", "addRandomPrefix", "shuffleLetters", "replaceWithSynonyms", "replaceWithTranslation").

### Data Noise
For each new Dataset X:
* **dataset1DataNoise**: Whether the data should include errors or not (true or false). 
* **dataset1DataNoiseValue**:  The percentage of rows/ columns to receive noise (0-100).
* **dataset1DataNoiseInside**: The percentage that indicates how many of the entries within a for noise selected column or row should receive errors (0-100).
* **datasetXDataMultiselect**:  The eroor methods that are chosen ("replaceWithSynonyms", "addRandomPrefix", "replaceWithTranslation", "shuffleWords", "generateMissingValue", "generatePhoneticError", "generateOCRError", "abbreviateDataEntry", "changeFormat", "generateTypingError", "generateRandomString", "changeValue", "changeValueToOutlier").

### Shuffle
For each new Dataset X:
* **datasetXShuffleOption**: The shuffle option for the new datasets (row shuffle, column shuffle or no change).

### JSON Upload
In the GUI the user can either set the parameters manually or upload a JSON file that conatains the configurations. An example of such a file is given here. The possible values that the user can choose from are written in comments.
```
{
  "splitType": "VerticalHorizontal", 	// "Vertical", "Horizontal", "VerticalHorizontal" 
  "rowOverlapPercentage": 0,		// 0 to 100
  "columnOverlapPercentage": 0,		// 0 to 100
  "rowDistribution": 0,			// 0 to 100
  "columnDistribution": 0,		// 0 to 100
  "overlapType": "Mixed Overlap",	// "Mixed Overlap", "Block Overlap"

  "dataset1StructureType": "No Change",	// "No Change", "BCNF", "Join Columns"
  "dataset1BCNFSliderValue": 0,		// 0 to 100
  "dataset1JoinColumnsSliderValue": 0,	// 0 to 100

  "dataset2StructureType": "No Change",	// "No Change", "BCNF", "Join Columns"
  "dataset2BCNFSliderValue": 0,		// 0 to 100
  "dataset2JoinColumnsSliderValue": 0,	// 0 to 100

  "dataset3StructureType": "No Change",	// "No Change", "BCNF", "Join Columns"
  "dataset3BCNFSliderValue": 0,		// 0 to 100
  "dataset3JoinColumnsSliderValue": 0,	// 0 to 100

  "dataset4StructureType": "No Change", // "No Change", "BCNF", "Join Columns"
  "dataset4BCNFSliderValue": 0,		// 0 to 100
  "dataset4JoinColumnsSliderValue": 0,	// 0 to 100

  "dataset1SchemaNoise": false,		// false, true
  "dataset1SchemaNoiseValue": 0,	// 0 to 100
  "dataset1SchemaKeyNoise": false,	// false, true
  "dataset1SchemaDeleteSchema": false,	// false, true
  "dataset1SchemaMultiselect": [],	// "generateRandomString", "abbreviateFirstLetters", "abbreviateRandomLength",
                                        "addRandomPrefix", "shuffleLetters", "replaceWithSynonyms", "replaceWithTranslation"

  "dataset2SchemaNoise": false,		// false, true
  "dataset2SchemaNoiseValue": 0,	// 0 to 100
  "dataset2SchemaKeyNoise": false,	// false, true
  "dataset2SchemaDeleteSchema": false,	// false, true
  "dataset2SchemaMultiselect": [],	// "generateRandomString", "abbreviateFirstLetters", "abbreviateRandomLength",
                                        "addRandomPrefix", "shuffleLetters", "replaceWithSynonyms", "replaceWithTranslation"

  "dataset3SchemaNoise": false,		// false, true
  "dataset3SchemaNoiseValue": 0,	// 0 to 100
  "dataset3SchemaKeyNoise": false,	// false, true
  "dataset3SchemaDeleteSchema": false,	// false, true
  "dataset3SchemaMultiselect": [],	// "generateRandomString", "abbreviateFirstLetters", "abbreviateRandomLength",
                                        "addRandomPrefix", "shuffleLetters", "replaceWithSynonyms", "replaceWithTranslation"

  "dataset4SchemaNoise": false,		// false, true
  "dataset4SchemaNoiseValue": 0,	// 0 to 100
  "dataset4SchemaKeyNoise": false,	// false, true
  "dataset4SchemaDeleteSchema": false,	// false, true
  "dataset4SchemaMultiselect": [],	// "generateRandomString", "abbreviateFirstLetters", "abbreviateRandomLength",
                                        "addRandomPrefix", "shuffleLetters", "replaceWithSynonyms", "replaceWithTranslation"

  "dataset1DataNoise": false,		// false, true
  "dataset1DataNoiseValue": 0,		// 0 to 100
  "dataset1DataKeyNoise": false,	// false, true
  "dataset1DataNoiseInside": 0,		// 0 to 100
  "dataset1DataMultiselect": [],	// "replaceWithSynonyms", "addRandomPrefix", "replaceWithTranslation",
                                        "shuffleWords", "generateMissingValue", "generatePhoneticError",
                                        "generateOCRError", "abbreviateDataEntry", "changeFormat", "generateTypingError",
                                        "generateRandomString", "changeValue", "changeValueToOutlier" 

  "dataset2DataNoise": false,		// false, true
  "dataset2DataNoiseValue": 0,		// 0 to 100
  "dataset2DataKeyNoise": false,	// false, true
  "dataset2DataNoiseInside": 0,		// 0 to 100
  "dataset2DataMultiselect": [],	// "replaceWithSynonyms", "addRandomPrefix", "replaceWithTranslation",
                                        "shuffleWords", "generateMissingValue", "generatePhoneticError",
                                        "generateOCRError", "abbreviateDataEntry", "changeFormat", "generateTypingError",
                                        "generateRandomString", "changeValue", "changeValueToOutlier" 

  "dataset3DataNoise": false,		// false, true
  "dataset3DataNoiseValue": 0,		// 0 to 100
  "dataset3DataKeyNoise": false,	// false, true
  "dataset3DataNoiseInside": 0,		// 0 to 100
  "dataset3DataMultiselect": [],	// "replaceWithSynonyms", "addRandomPrefix", "replaceWithTranslation",
                                        "shuffleWords", "generateMissingValue", "generatePhoneticError",
                                        "generateOCRError", "abbreviateDataEntry", "changeFormat", "generateTypingError",
                                        "generateRandomString", "changeValue", "changeValueToOutlier" 

  "dataset4DataNoise": false,		// false, true
  "dataset4DataNoiseValue": 0,		// 0 to 100
  "dataset4DataKeyNoise": false,	// false, true
  "dataset4DataNoiseInside": 0,		// 0 to 100
  "dataset4DataMultiselect": [],	// "replaceWithSynonyms", "addRandomPrefix", "replaceWithTranslation",
                                        "shuffleWords", "generateMissingValue", "generatePhoneticError",
                                        "generateOCRError", "abbreviateDataEntry", "changeFormat", "generateTypingError",
                                        "generateRandomString", "changeValue", "changeValueToOutlier" 

  "dataset1ShuffleOption": "No Change", // "No Change", "Shuffle Rows", "Shuffle Columns"
  "dataset2ShuffleOption": "No Change", // "No Change", "Shuffle Rows", "Shuffle Columns"
  "dataset3ShuffleOption": "No Change",	// "No Change", "Shuffle Rows", "Shuffle Columns"
  "dataset4ShuffleOption": "No Change"	// "No Change", "Shuffle Rows", "Shuffle Columns"
}
```


## FAQ
#### Why is the installation not working?
You need to install React and Next.js. Please check whether this installation was successful.  
If you are having trouble building the Docker containers, make sure that you are running VS Code and Docker as an administrator.

#### Why is the generation failing?
If you forgot to set parameters, SYDAG may not be able to generate a result and will return you to the Summary page. Restart the program and make sure that you have set all the necessary parameters. Pay special attention to the selection of error methods for each individual dataset.

#### Why do my generated datasets look different from what I expected?
You can check several points:
1. Did you specify the correct separator? If you specify the wrong character, the tool may interpret an entire row as a single entry.
2. Did you specify error methods? If you enabled noise, you must define which methods SYDAG is allowed to use. If you do not select any methods, SYDAG cannot introduce errors.
3. Did you try adding errors to numeric values? If so, you need to choose at least one method that is applied on numeric values: "changeValue" or "changeValueToOutlier".If you do not select one of these, SYDAG cannot introduce numeric errors.
4. Did you use normalization? If you apply normalization while preserving key constraints, fewer errors than expected may appear in your relations. This happens because SYDAG does not introduce errors in key and foreign key columns in this case. You can either choose a higher percentage of data noise, which will cause more of the non-key columns to receive noise, or choose a smaller percentage of normalization. That will result in fewer foreign key columns, allowing more columns to receive noise.

#### When using the JSON file, what value should I assign to the parameters I am not using?
If you do not apply both split types, SYDAG will produce only two new datasets. Therefore, the parameters for the third and fourth datasets will not be used.
Additionally, Split, Structure, and Noise can include parameters that are not needed, for example, when the noise is not enabled or the structure is not changed.
In the JSON file, set the unused booleans to 'false', the arrays of chosen methods to empty '[]' and the rest of the parameters to 'null'. They will not be used for the generation anyway.
