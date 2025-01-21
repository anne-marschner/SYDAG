# SYDAG
## Table of Contents
1. [General Information](#general-information)
2. [Technologies](#technologies)
3. [Installation](#installation)
4. [User Instructions](#user-instructions)
5. [FAQ](#faq)

## General Information
SYDAG is a **Sy**ntehtic **Da**taset **G**eneartor for data integration scenarios.
Users can apply it to create integration scenarios that are customized to their specific needs. The generator works with relational datamodels. The user must supply SYADG with a relation stored in the form of a CSV file. SYDAG can process this relation to create an integration scenario of multiple datasets and relations.
The user can choose between varies configuration options to achieve a specific result. 
We provide a graphical user interface for an easy usage of SYDAG. The output includes the new datasets, a Mapping File, and a key file for each created dataset. The Mappinf File specifies which attributes in the new relations correspond to the original attributes. The key files containin the key and foreign key relationships.

## Technologies
A list of technologies used within the project:
* [Normalize](https://hpi.de/naumann/projects/data-profiling-and-analytics/metanome-data-profiling/algorithms.html): Version 1.2 
* [HyUCC](https://hpi.de/naumann/projects/data-profiling-and-analytics/metanome-data-profiling/algorithms.html): Version 1.2
* [Java](https://www.oracle.com/java/technologies/downloads/#java21): Version 21
* [Spring Boot](https://spring.io/projects/spring-boot): Version 3.3.4
* [TypeScript](https://www.npmjs.com/package/typescript): Version 5.7.3
* [React](https://react.dev/): Version 19.0.0
* [NextJS](https://nextjs.org/): Version 15.1.5


## Installation
To install SYDAG the user must clone the GitHub repository. It is necessary to install React and Next.js in the frontend folder. Open a terminal in the IDE of your choice (e.g. VSCode) and navigate to 'SYDAG\frontend'. There you need to apply the the following commands:
* `npm install --save-exact react@^19.0.0 react-dom@^19.0.0`
* `npm install --save-exact @types/react@^19.0.0 @types/react-dom@^19.0.0`
* `npx @next/codemod@canary upgrade latest`
* `npm install` 

The project uses Docker for fast and easy access. Therefore, users must execute the [Docker Compose file](https://github.com/anne-marschner/SYDAG/blob/main/docker-compose.yml) to build the application on their device. Then SYDAG can be started through the Docker environment.

## User Instructions
The configuration of SYDAG includes several parameters that the user must adjust.
In the following we give an overview over all of the parameters and their effects on the generation. For our explanation we follow the grouping of the parameters in the interface. 

### Input File
* **csvFile**: An input relation stored as a CSV file. 
* **hasHeaders**: Whether the relation has headers. 
* **separator**:  The characters used as separator in the csv file. 
* **quote**: The characters used for quotes in the csv file. 
* **escape**: The characters used as escape sequence in the csv file.

### Configuration Type
* **jsonFile**: A json file that includes all configuration settings. 
* **mode**: The option of setting the configuration (manual setting or upload of a json file).

### Split
* **splitType**: The type of split to be applied to the input relation (horizontal, vertical or both). 
* **rowOverlapPercentage**: The percentage of rows that should overlap between the split relations. 
* **columnOverlapPercentage**: The percentage of columns that should overlap between the split relations. 
* **rowDistribution**:  The distribution of the non-overlapping rows between the new relations. This percentage describes how many non-overlapping rows will be part of one of the new relations (0-100). 
* **columnDistribution**: The distribution of the non-overlapping columns between the new relations. This percentage describes how many non-overlapping columns will be part of one of the new relations (0-100). 
* **overlapType**: the way the overlapping rows should be picked (as block overlap or random overlap).

### Structure
For each new Dataset X: 
* **datasetXStructureType**: the structure change that will be applied to the new datsets (Join of Columns, Normalization to BCNF or no change).
* **datasetXBCNFSliderValue**: the percentage that determines how many of the possible decomposition steps should be executed (0-100). 
* **datasetXJoinColumnsSliderValue**: the percentage of overlapping columns to join (0-100).

### Schema Noise
For each new Dataset X:
* **datasetXSchemaNoise**: whether the schema should include errors or not (true or false). 
* **datasetXSchemaNoiseValue**: the percentage that determines how many of the attributes overlapping with other relations the generator will change. (0-100) 
* **datasetXSchemaKeyNoise**: whether key columns should also be affected by the noise (true or false). 
* **datasetXSchemaDeleteSchema**: whether the schema should be deleted (true or false). 
* **datasetXSchemaMultiselect**: the methods that are chosen for the noise. ("generateRandomString", "abbreviateFirstLetters", "abbreviateRandomLength", "addRandomPrefix", "shuffleLetters", "replaceWithSynonyms", "replaceWithTranslation").

### Data Noise
For each new Dataset X:
* **dataset1DataNoise**: whether the data should include errors or not (true or false). 
* **dataset1DataNoiseValue**:  the percentage of rows/ columns to receive noise. (0-100) 
* **dataset1DataNoiseInside**: percentage that indicates how many of the entries within a for noise selected column or row should receive errors. (0-100) 
* **datasetXDataMultiselect**:  the methods that are chosen for the noise. ("replaceWithSynonyms", "addRandomPrefix", "replaceWithTranslation", "shuffleWords", "generateMissingValue", "generatePhoneticError", "generateOCRError", "abbreviateDataEntry", "changeFormat", "generateTypingError", "generateRandomString", "changeValue", "changeValueToOutlier").

### Shuffle
For each new Dataset X:
* **datasetXShuffleOption**: the shuffle option for the new datasets (row shuffle, column shuffle or no change).

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
You need to install React and Next.js. Please check if this installation was successful.  
If you have problems with building the Docker containers, please check if you are executing VS Code and Docker as an administrator.

#### Why do my generated datasets look different from what I expected?
You can check several points:
1. Did you specify the correct separator? If you specify the wrong character, it is possible that the tool will read the entries of a whole row as one single entry.
2. Did you specify error methods? If you chose to enable noise, you must specify what methods SYDAG is allowed to use. If you do not choose methods, SYDAG cannot add errors.
3. Did you try adding errors to numeric values? If so, you need to chose at least one method that is applied on numeric methods: "changeValue" or "changeValueToOutlier". If you do not chose one of them, SYDAG cannot add numeric errors.
4. Did you use normalization? If you choose to apply normalization and preserve the key constraints, it is possible that fewer errors than you expected appear in your relations. This happens because, in that case, SYDAG does not add errors to the key and foreign key columns. You can either choose a higher percentage of data noise, which will cause more of the non-key columns to receive noise, or choose a smaller percentage of normalization. That will cause fewer foreign-key columns, and therefore more columns remain to receive noise.

#### When using the JSON file, what value should I assign to the parameters I am not using?
If you do not apply both split types, SYDAG will produce only two new datasets. Therefore, the parameters for the third and fourth datasets will not be used.
Additionally, Split, Structure and Noise can include parameters that are not needed, for example, when the noise is not enabled or the structure is not changed.
In the JSON file, set the unused booleans to false, the arrays of chosen methods to empty '[]' and the rest of the parameters to null. They will not be used for the generation anyway.
