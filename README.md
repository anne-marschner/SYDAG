# SYDAG
***
## Table of Contents
1. [General Info](#general-info)
2. [Technologies](#technologies)
3. [Installation](#installation)
3. [User Instructions](#user-instructions)

### General Info
***
SYDAG is a **Sy**ntehtic **Da**taset **G**eneartor for data integration scenarios.
Users can apply it to create integration scenarios that are customized to their specific needs. The generator works with relational datamodels. The user must supply SYADG with a relation stored in the form of a CSV file. SYDAG can process this relation to create an integration scenario of multiple datasets and relations.
The user can choose between varies configuration options to achieve a specific result. 
We provide a graphical user interface for an easy usage of SYDAG. The output includes the new datasets, a Mapping File, and a key file for each created dataset. The Mappinf File specifies which attributes in the new relations correspond to the original attributes. The key files containin the key and foreign key relationships.

## Technologies
***
A list of technologies used within the project:
* [Normalize](Algorithms): Version 1.2 
* [HyUCC](Algorithms): Version 1.2
* [Java](Java Downloads | Oracle): Version 21
* [Spring Boot](Spring Boot): Version 3.3.4
* [TypeScript](typescript - npm): Version 4.9.5
* [React](React): Version 18.2.0
* [NextJS](Next.js by Vercel - The React Framework): Version 14.2.14


## Installation
To install SYDAG the user must clone the GitHub repository. The project uses docker for fast and easy access. Therefore users must execute the [docker compose file](SYDAG/docker-compose.yml at main · marschna/SYDAG) to build the application on their device. Then SYDAG can be started through the docker environment.

## User Instructions
*** 
The configuration of SYDAG includes several parameters that the user must adjust.
In the follwing we give an overview over all of the parameters and their effects on the generation. For our explanation we follow the grouping of the parameters in the interface. 

### Input File
**csvFile**: 
**hasHeaders**: 
**separator**:
**quote**:
**escape**:

### Configuration Type
**jsonFile**:
**manualInput**:
**mode**:

### Split
**splitType**:
**rowOverlapPercentage**:
**columnOverlapPercentage**:
**rowDistribution**:
**columnDistribution**:
**overlapType**:

### Structure
For each new Dataset X:
**datasetXStructureType**:
**datasetXBCNFSliderValue**:
**datasetXJoinColumnsSliderValue**:

### Schema Noise
For each new Dataset X:
**datasetXSchemaNoise**:
**datasetXSchemaNoiseValue**:
**datasetXSchemaKeyNoise**:
**datasetXSchemaDeleteSchema**:
**datasetXSchemaMultiselect**:

### Data Noise:
For each new Dataset X:
**dataset1DataNoise**:
**dataset1DataNoiseValue**:
**dataset1DataNoiseInside**:
**datasetXDataMultiselect**:

### Shuffle
For each new Dataset X:
**datasetXShuffleOption**:![image](https://github.com/user-attachments/assets/cb6bef71-6453-4f24-ad82-7f504d49081e)
