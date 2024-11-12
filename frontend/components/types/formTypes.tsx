export type FormItems = {
    // Step 1
    csvFile: File | null;
    hasHeaders: boolean | null;
    separator: string | null;

    // Step 2
    jsonFile: File | null;
    manualInput: boolean | null;
    mode: "UploadJson" | "Manual" | null;

    // Step 3
    splitType: "Horizontal" | "Vertical" | null;
    rowOverlapPercentage: number | null;
    columnOverlapPercentage: number | null;

    // Step 4
    dataset1StructureType: "BCNF" | "Join Columns"| "No Change" | null;
    dataset2StructureType: "BCNF" | "Join Columns"| "No Change" | null;

    // Step 5
    dataset1SchemaNoise: boolean | null;
    dataset1SchemaNoiseValue: number | null;
    dataset2SchemaNoise: boolean | null;
    dataset2SchemaNoiseValue: number | null;
    dataset1SchemaKeyNoise: boolean | null;
    dataset2SchemaKeyNoise: boolean | null;

    // Step 6
    dataset1DataNoise: boolean | null;
    dataset1DataNoiseValue: number | null;
    dataset2DataNoise: boolean | null;
    dataset2DataNoiseValue: number | null;
    dataset1DataKeyNoise: boolean | null;
    dataset2DataKeyNoise: boolean | null;

    // Step 7
    dataset1ShuffleOption: "Shuffle Columns" | "Shuffle Rows" | "No Change" | null;
    dataset2ShuffleOption: "Shuffle Columns" | "Shuffle Rows" | "No Change" | null;
};
