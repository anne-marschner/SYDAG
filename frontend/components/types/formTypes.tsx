export type FormItems = {
    // Step 1
    csvFile: File | null;
    hasHeaders: boolean | null;
    separator: string | null;
    quote: string | null;
    escape: string | null;

    // Step 2
    jsonFile: File | null;
    manualInput: boolean | null;
    mode: "UploadJson" | "Manual" | null;

    // Step 3
    splitType: "Horizontal" | "Vertical" | "VerticalHorizontal" | null;
    rowOverlapPercentage: number | null;
    columnOverlapPercentage: number | null;
    rowDistribution: number | null;
    columnDistribution: number | null;
    overlapType: "Mixed Overlap" | "Block Overlap" | null;

    // Step 4
    datasetAStructureType: "BCNF" | "Merge Columns" | "No Change" | null;
    datasetABCNFSliderValue: number | null;
    datasetAMergeColumnsSliderValue: number | null;

    datasetBStructureType: "BCNF" | "Merge Columns" | "No Change" | null;
    datasetBBCNFSliderValue: number | null;
    datasetBMergeColumnsSliderValue: number | null;

    datasetCStructureType: "BCNF" | "Merge Columns" | "No Change" | null;
    datasetCBCNFSliderValue: number | null;
    datasetCMergeColumnsSliderValue: number | null;

    datasetDStructureType: "BCNF" | "Merge Columns" | "No Change" | null;
    datasetDBCNFSliderValue: number | null;
    datasetDMergeColumnsSliderValue: number | null;

    // Step 5
    datasetASchemaNoise: boolean | null;
    datasetASchemaNoiseValue: number | null;
    datasetASchemaKeyNoise: boolean | null;
    datasetASchemaDeleteSchema: boolean | null;
    datasetASchemaMultiselect: string[] | null; 

    datasetBSchemaNoise: boolean | null;
    datasetBSchemaNoiseValue: number | null;
    datasetBSchemaKeyNoise: boolean | null;
    datasetBSchemaDeleteSchema: boolean | null;
    datasetBSchemaMultiselect: string[] | null; 

    datasetCSchemaNoise: boolean | null;
    datasetCSchemaNoiseValue: number | null;
    datasetCSchemaKeyNoise: boolean | null;
    datasetCSchemaDeleteSchema: boolean | null;
    datasetCSchemaMultiselect: string[] | null; 

    datasetDSchemaNoise: boolean | null;
    datasetDSchemaNoiseValue: number | null;
    datasetDSchemaKeyNoise: boolean | null;
    datasetDSchemaDeleteSchema: boolean | null;
    datasetDSchemaMultiselect: string[] | null; 

    // Step 6
    datasetADataNoise: boolean | null;
    datasetADataNoiseValue: number | null;
    datasetADataKeyNoise: boolean | null;
    datasetADataNoiseInside: number | null;
    datasetADataMultiselect: string[] | null;

    datasetBDataNoise: boolean | null;
    datasetBDataNoiseValue: number | null;
    datasetBDataKeyNoise: boolean | null;
    datasetBDataNoiseInside: number | null;
    datasetBDataMultiselect: string[] | null;

    datasetCDataNoise: boolean | null;
    datasetCDataNoiseValue: number | null;
    datasetCDataKeyNoise: boolean | null;
    datasetCDataNoiseInside: number | null;
    datasetCDataMultiselect: string[] | null;

    datasetDDataNoise: boolean | null;
    datasetDDataNoiseValue: number | null;
    datasetDDataKeyNoise: boolean | null;
    datasetDDataNoiseInside: number | null;
    datasetDDataMultiselect: string[] | null;

    // Step 7
    datasetAShuffleOption: "Shuffle Columns" | "Shuffle Rows" | "No Change" | null;
    datasetBShuffleOption: "Shuffle Columns" | "Shuffle Rows" | "No Change" | null;
    datasetCShuffleOption: "Shuffle Columns" | "Shuffle Rows" | "No Change" | null;
    datasetDShuffleOption: "Shuffle Columns" | "Shuffle Rows" | "No Change" | null;
};
