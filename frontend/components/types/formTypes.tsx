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
    // Dataset 1
    dataset1StructureType: "BCNF" | "Join Columns" | "No Change" | null;
    dataset1BCNFSliderValue: number | null;
    dataset1JoinColumnsSliderValue: number | null;

    // Dataset 2
    dataset2StructureType: "BCNF" | "Join Columns" | "No Change" | null;
    dataset2BCNFSliderValue: number | null;
    dataset2JoinColumnsSliderValue: number | null;

    // Dataset 3
    dataset3StructureType: "BCNF" | "Join Columns" | "No Change" | null;
    dataset3BCNFSliderValue: number | null;
    dataset3JoinColumnsSliderValue: number | null;

    // Dataset 4
    dataset4StructureType: "BCNF" | "Join Columns" | "No Change" | null;
    dataset4BCNFSliderValue: number | null;
    dataset4JoinColumnsSliderValue: number | null;

    // Step 5
    dataset1SchemaNoise: boolean | null;
    dataset1SchemaNoiseValue: number | null;
    dataset1SchemaKeyNoise: boolean | null;
    dataset1SchemaDeleteSchema: boolean | null;
    dataset1SchemaMultiselect: string[] | null; 

    dataset2SchemaNoise: boolean | null;
    dataset2SchemaNoiseValue: number | null;
    dataset2SchemaKeyNoise: boolean | null;
    dataset2SchemaDeleteSchema: boolean | null;
    dataset2SchemaMultiselect: string[] | null; 

    dataset3SchemaNoise: boolean | null;
    dataset3SchemaNoiseValue: number | null;
    dataset3SchemaKeyNoise: boolean | null;
    dataset3SchemaDeleteSchema: boolean | null;
    dataset3SchemaMultiselect: string[] | null; 

    dataset4SchemaNoise: boolean | null;
    dataset4SchemaNoiseValue: number | null;
    dataset4SchemaKeyNoise: boolean | null;
    dataset4SchemaDeleteSchema: boolean | null;
    dataset4SchemaMultiselect: string[] | null; 

    // Step 6
    // Data Noise for Dataset 1
    dataset1DataNoise: boolean | null;
    dataset1DataNoiseValue: number | null;
    dataset1DataKeyNoise: boolean | null;
    dataset1DataNoiseInside: number | null;
    dataset1DataMultiselect: string[] | null;

    // Data Noise for Dataset 2
    dataset2DataNoise: boolean | null;
    dataset2DataNoiseValue: number | null;
    dataset2DataKeyNoise: boolean | null;
    dataset2DataNoiseInside: number | null;
    dataset2DataMultiselect: string[] | null;

    // Data Noise for Dataset 3
    dataset3DataNoise: boolean | null;
    dataset3DataNoiseValue: number | null;
    dataset3DataKeyNoise: boolean | null;
    dataset3DataNoiseInside: number | null;
    dataset3DataMultiselect: string[] | null;

    // Data Noise for Dataset 4
    dataset4DataNoise: boolean | null;
    dataset4DataNoiseValue: number | null;
    dataset4DataKeyNoise: boolean | null;
    dataset4DataNoiseInside: number | null;
    dataset4DataMultiselect: string[] | null;

    // Step 7
    dataset1ShuffleOption: "Shuffle Columns" | "Shuffle Rows" | "No Change" | null;
    dataset2ShuffleOption: "Shuffle Columns" | "Shuffle Rows" | "No Change" | null;
    dataset3ShuffleOption: "Shuffle Columns" | "Shuffle Rows" | "No Change" | null;
    dataset4ShuffleOption: "Shuffle Columns" | "Shuffle Rows" | "No Change" | null;
};
