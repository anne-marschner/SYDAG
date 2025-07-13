import {z} from 'zod';

// Step 1 Schema
export const Step1Schema = z.object({
    csvFile: z
        .instanceof(File, {message: "Please upload a CSV file."})
        .refine((file) => file.size > 0, {message: "File cannot be empty."}),
    hasHeaders: z.boolean({
        required_error: "Please indicate if the CSV has headers.",
    }),
    separator: z.enum([";", ":", ","], {
        errorMap: () => ({message: "Separator must be one of the following: ';', ':', or ','"}),
    }),
    quote: z.string({
        required_error: "Please specify the quote character.",
    }).length(1, {message: "Quote must be a single character."}),
    escape: z.string({
        required_error: "Please specify the escape character.",
    }).length(1, {message: "Escape must be a single character."}),
});


// Step 2 Schema
export const Step2Schema = z.object({
    mode: z.enum(['UploadJson', 'Manual']),
    jsonFile: z
        .instanceof(File)
        .refine((file) => file.type === 'application/json', {message: 'File must be a JSON.'})
        .nullable(),
    manualInput: z.boolean(),
}).superRefine((data, ctx) => {
    // If the mode is 'UploadJson', the jsonFile must be provided
    if (data.mode === 'UploadJson' && !data.jsonFile) {
        ctx.addIssue({
            code: z.ZodIssueCode.custom,
            path: ['jsonFile'],
            message: 'JSON file is required when UploadJson mode is selected.',
        });
    }

    // If the mode is 'Manual', ensure the jsonFile is not required
    if (data.mode === 'Manual' && data.jsonFile) {
        ctx.addIssue({
            code: z.ZodIssueCode.custom,
            path: ['jsonFile'],
            message: 'JSON file should not be provided in Manual mode.',
        });
    }
});


// Step 3 Schema
export const Step3Schema = z.object({
    splitType: z.enum(['Horizontal', 'Vertical', 'VerticalHorizontal'], {
        required_error: "Split Type is required.",
    }),
    rowOverlapPercentage: z.number().min(0).max(100).default(0),
    columnOverlapPercentage: z.number().min(0).max(100).default(0),
    rowDistribution: z.number().min(0).max(100).default(0),
    columnDistribution: z.number().min(0).max(100).default(0),
    overlapType: z.enum(['Mixed Overlap', 'Block Overlap'], {
        required_error: "Overlap Type is required.",
    }).nullable(),
})
    .superRefine((data, ctx) => {
        // Conditionally require overlapType only for Horizontal and VerticalHorizontal splits
        if (
            (data.splitType === 'Horizontal' || data.splitType === 'VerticalHorizontal') &&
            !data.overlapType
        ) {
            ctx.addIssue({
                code: z.ZodIssueCode.custom,
                message: "Overlap Type is required for the selected split type.",
                path: ["overlapType"],
            });
        }

        // Depending on splitType, validate relevant fields
        switch (data.splitType) {
            case 'Horizontal':
                // Sliders for Row Overlap and Row Distribution must have values
                if (data.rowOverlapPercentage === null || data.rowOverlapPercentage === undefined) {
                    ctx.addIssue({
                        code: z.ZodIssueCode.custom,
                        message: "Row Overlap Percentage is required for Horizontal split.",
                        path: ["rowOverlapPercentage"],
                    });
                }
                if (data.rowDistribution === null || data.rowDistribution === undefined) {
                    ctx.addIssue({
                        code: z.ZodIssueCode.custom,
                        message: "Row Distribution is required for Horizontal split.",
                        path: ["rowDistribution"],
                    });
                }
                break;

            case 'Vertical':
                // Sliders for Column Overlap and Column Distribution must have values
                if (data.columnOverlapPercentage === null || data.columnOverlapPercentage === undefined) {
                    ctx.addIssue({
                        code: z.ZodIssueCode.custom,
                        message: "Column Overlap Percentage is required for Vertical split.",
                        path: ["columnOverlapPercentage"],
                    });
                }
                if (data.columnDistribution === null || data.columnDistribution === undefined) {
                    ctx.addIssue({
                        code: z.ZodIssueCode.custom,
                        message: "Column Distribution is required for Vertical split.",
                        path: ["columnDistribution"],
                    });
                }
                break;

            case 'VerticalHorizontal':
                // Sliders for both Row and Column Overlaps and Distributions must have values
                if (data.rowOverlapPercentage === null || data.rowOverlapPercentage === undefined) {
                    ctx.addIssue({
                        code: z.ZodIssueCode.custom,
                        message: "Row Overlap Percentage is required for VerticalHorizontal split.",
                        path: ["rowOverlapPercentage"],
                    });
                }
                if (data.rowDistribution === null || data.rowDistribution === undefined) {
                    ctx.addIssue({
                        code: z.ZodIssueCode.custom,
                        message: "Row Distribution is required for VerticalHorizontal split.",
                        path: ["rowDistribution"],
                    });
                }
                if (data.columnOverlapPercentage === null || data.columnOverlapPercentage === undefined) {
                    ctx.addIssue({
                        code: z.ZodIssueCode.custom,
                        message: "Column Overlap Percentage is required for VerticalHorizontal split.",
                        path: ["columnOverlapPercentage"],
                    });
                }
                if (data.columnDistribution === null || data.columnDistribution === undefined) {
                    ctx.addIssue({
                        code: z.ZodIssueCode.custom,
                        message: "Column Distribution is required for VerticalHorizontal split.",
                        path: ["columnDistribution"],
                    });
                }
                break;

            default:
                ctx.addIssue({
                    code: z.ZodIssueCode.custom,
                    message: "Invalid Split Type selected.",
                    path: ["splitType"],
                });
        }
    });



// Step 4 Schema
export const Step4Schema = z
    .object({
        splitType: z.enum(["Horizontal", "Vertical", "VerticalHorizontal"]).nullable(),

        // Structure Type selections for all datasets
        datasetAStructureType: z
            .enum(["BCNF", "Merge Columns", "No Change"])
            .nullable(),
        datasetBStructureType: z
            .enum(["BCNF", "Merge Columns", "No Change"])
            .nullable(),
        datasetCStructureType: z
            .enum(["BCNF", "Merge Columns", "No Change"])
            .nullable().optional(),
        datasetDStructureType: z
            .enum(["BCNF", "Merge Columns", "No Change"])
            .nullable().optional(),

        // Slider values for Dataset A
        datasetABCNFSliderValue: z
            .number()
            .min(0, {message: "BCNF Slider Value must be at least 0%"})
            .max(100, {message: "BCNF Slider Value cannot exceed 100%"})
            .nullable(),
        datasetAMergeColumnsSliderValue: z
            .number()
            .min(0, {message: "Merge Columns Slider Value must be at least 0%"})
            .max(100, {message: "Merge Columns Slider Value cannot exceed 100%"})
            .nullable(),

        // Slider values for Dataset B
        datasetBBCNFSliderValue: z
            .number()
            .min(0, {message: "BCNF Slider Value must be at least 0%"})
            .max(100, {message: "BCNF Slider Value cannot exceed 100%"})
            .nullable(),
        datasetBMergeColumnsSliderValue: z
            .number()
            .min(0, {message: "Merge Columns Slider Value must be at least 0%"})
            .max(100, {message: "Merge Columns Slider Value cannot exceed 100%"})
            .nullable(),

        // Slider values for Dataset C
        datasetCBCNFSliderValue: z
            .number()
            .min(0, {message: "BCNF Slider Value must be at least 0%"})
            .max(100, {message: "BCNF Slider Value cannot exceed 100%"})
            .nullable().optional(),
        datasetCMergeColumnsSliderValue: z
            .number()
            .min(0, {message: "Merge Columns Slider Value must be at least 0%"})
            .max(100, {message: "Merge Columns Slider Value cannot exceed 100%"})
            .nullable().optional(),

        // Slider values for Dataset D
        datasetDBCNFSliderValue: z
            .number()
            .min(0, {message: "BCNF Slider Value must be at least 0%"})
            .max(100, {message: "BCNF Slider Value cannot exceed 100%"})
            .nullable().optional(),
        datasetDMergeColumnsSliderValue: z
            .number()
            .min(0, {message: "Merge Columns Slider Value must be at least 0%"})
            .max(100, {message: "Merge Columns Slider Value cannot exceed 100%"})
            .nullable().optional(),
    })
    .superRefine((data, ctx) => {
        // Conditional validation for Datasets A and B
        const datasets = [1, 2, 3, 4];
        datasets.forEach((dataset) => {
            const structureType = data[`dataset${dataset}StructureType` as keyof typeof data];
            const bcfnSlider = data[`dataset${dataset}BCNFSliderValue` as keyof typeof data];
            const joinColumnsSlider = data[`dataset${dataset}MergeColumnsSliderValue` as keyof typeof data];

            if (structureType === "BCNF") {
                if (bcfnSlider === null || bcfnSlider === undefined) {
                    ctx.addIssue({
                        code: z.ZodIssueCode.custom,
                        message: `BCNF Slider Value for Dataset ${dataset} is required when BCNF is selected`,
                        path: [`dataset${dataset}BCNFSliderValue` as keyof typeof data],
                    });
                }
            }

            if (structureType === "Merge Columns") {
                if (joinColumnsSlider === null || joinColumnsSlider === undefined) {
                    ctx.addIssue({
                        code: z.ZodIssueCode.custom,
                        message: `Merge Columns Slider Value for Dataset ${dataset} is required when Merge Columns is selected`,
                        path: [`dataset${dataset}MergeColumnsSliderValue` as keyof typeof data],
                    });
                }
            }
        });

        // Additional conditional validations based on splitType
        if (data.splitType !== "VerticalHorizontal") {
            // If splitType is not VerticalHorizontal, ensure datasets C and D are not set
            if (data.datasetCStructureType !== null) {
                ctx.addIssue({
                    code: z.ZodIssueCode.custom,
                    message: "Dataset C structure type should not be set unless splitType is VerticalHorizontal",
                    path: ["datasetCStructureType"],
                });
            }
            if (data.datasetDStructureType !== null) {
                ctx.addIssue({
                    code: z.ZodIssueCode.custom,
                    message: "Dataset D structure type should not be set unless splitType is VerticalHorizontal",
                    path: ["datasetDStructureType"],
                });
            }
        }
    });


// Step 5 Schema
export const Step5Schema = z
    .object({
            datasetASchemaNoise: z.boolean().nullable(),
            datasetASchemaNoiseValue: z.number().min(0).max(100).nullable(),
            datasetASchemaKeyNoise: z.boolean().nullable(),
            datasetASchemaDeleteSchema: z.boolean().nullable(),
            datasetASchemaMultiselect: z.array(z.string()).nullable(),

        }
    );



// Step 6 Data Schema
export const Step6Schema = z
    .object({

    });


// Step 7 Schema
export const Step7Schema = z.object({
    datasetAShuffleOption: z.enum(['Shuffle Columns', 'Shuffle Rows', 'No Change']).nullable(),
    datasetBShuffleOption: z.enum(['Shuffle Columns', 'Shuffle Rows', 'No Change']).nullable(),
    datasetCShuffleOption: z.enum(['Shuffle Columns', 'Shuffle Rows', 'No Change']).nullable(),
    datasetDShuffleOption: z.enum(['Shuffle Columns', 'Shuffle Rows', 'No Change']).nullable(),
});

export const FormItemsJSONSchema = z
    .object({

        // Step 3 (Split)
        splitType: z.enum(['Horizontal', 'Vertical', 'VerticalHorizontal']),
        rowOverlapPercentage: z.number().min(0).max(100).nullable(),
        columnOverlapPercentage: z.number().min(0).max(100).nullable(),
        rowDistribution: z.number().min(0).max(100).nullable(),
        columnDistribution: z.number().min(0).max(100).nullable(),
        overlapType: z.enum(['Mixed Overlap', 'Block Overlap']).nullable(),

        // Step 4 (Datasets Structure)
        datasetAStructureType: z.enum(['BCNF', 'Merge Columns', 'No Change']).nullable(),
        datasetABCNFSliderValue: z.number().min(0).max(100).nullable(),
        datasetAMergeColumnsSliderValue: z.number().min(0).max(100).nullable(),

        datasetBStructureType: z.enum(['BCNF', 'Merge Columns', 'No Change']).nullable(),
        datasetBBCNFSliderValue: z.number().min(0).max(100).nullable(),
        datasetBMergeColumnsSliderValue: z.number().min(0).max(100).nullable(),

        datasetCStructureType: z.enum(['BCNF', 'Merge Columns', 'No Change']).nullable(),
        datasetCBCNFSliderValue: z.number().min(0).max(100).nullable(),
        datasetCMergeColumnsSliderValue: z.number().min(0).max(100).nullable(),

        datasetDStructureType: z.enum(['BCNF', 'Merge Columns', 'No Change']).nullable(),
        datasetDBCNFSliderValue: z.number().min(0).max(100).nullable(),
        datasetDMergeColumnsSliderValue: z.number().min(0).max(100).nullable(),

        // Step 5 (Schema Noise)
        datasetASchemaNoise: z.boolean().nullable(),
        datasetASchemaNoiseValue: z.number().min(0).max(100).nullable(),
        datasetASchemaKeyNoise: z.boolean().nullable(),
        datasetASchemaDeleteSchema: z.boolean().nullable(),
        datasetASchemaMultiselect: z.array(z.string()).nullable(),

        datasetBSchemaNoise: z.boolean().nullable(),
        datasetBSchemaNoiseValue: z.number().min(0).max(100).nullable(),
        datasetBSchemaKeyNoise: z.boolean().nullable(),
        datasetBSchemaDeleteSchema: z.boolean().nullable(),
        datasetBSchemaMultiselect: z.array(z.string()).nullable(),

        datasetCSchemaNoise: z.boolean().nullable(),
        datasetCSchemaNoiseValue: z.number().min(0).max(100).nullable(),
        datasetCSchemaKeyNoise: z.boolean().nullable(),
        datasetCSchemaDeleteSchema: z.boolean().nullable(),
        datasetCSchemaMultiselect: z.array(z.string()).nullable(),

        datasetDSchemaNoise: z.boolean().nullable(),
        datasetDSchemaNoiseValue: z.number().min(0).max(100).nullable(),
        datasetDSchemaKeyNoise: z.boolean().nullable(),
        datasetDSchemaDeleteSchema: z.boolean().nullable(),
        datasetDSchemaMultiselect: z.array(z.string()).nullable(),

        // Step 6 (Data Noise)
        datasetADataNoise: z.boolean().nullable(),
        datasetADataNoiseValue: z.number().min(0).max(100).nullable(),
        datasetADataKeyNoise: z.boolean().nullable(),
        datasetADataNoiseInside: z.number().min(0).max(100).nullable(),
        datasetADataMultiselect: z.array(z.string()).nullable(),

        datasetBDataNoise: z.boolean().nullable(),
        datasetBDataNoiseValue: z.number().min(0).max(100).nullable(),
        datasetBDataKeyNoise: z.boolean().nullable(),
        datasetBDataNoiseInside: z.number().min(0).max(100).nullable(),
        datasetBDataMultiselect: z.array(z.string()).nullable(),

        datasetCDataNoise: z.boolean().nullable(),
        datasetCDataNoiseValue: z.number().min(0).max(100).nullable(),
        datasetCDataKeyNoise: z.boolean().nullable(),
        datasetCDataNoiseInside: z.number().min(0).max(100).nullable(),
        datasetCDataMultiselect: z.array(z.string()).nullable(),

        datasetDDataNoise: z.boolean().nullable(),
        datasetDDataNoiseValue: z.number().min(0).max(100).nullable(),
        datasetDDataKeyNoise: z.boolean().nullable(),
        datasetDDataNoiseInside: z.number().min(0).max(100).nullable(),
        datasetDDataMultiselect: z.array(z.string()).nullable(),

        // Step 7 (Shuffle Options)
        datasetAShuffleOption: z.enum(['Shuffle Columns', 'Shuffle Rows', 'No Change']).nullable(),
        datasetBShuffleOption: z.enum(['Shuffle Columns', 'Shuffle Rows', 'No Change']).nullable(),
        datasetCShuffleOption: z.enum(['Shuffle Columns', 'Shuffle Rows', 'No Change']).nullable(),
        datasetDShuffleOption: z.enum(['Shuffle Columns', 'Shuffle Rows', 'No Change']).nullable()
    })
    .superRefine((data, ctx) => {
        if (data.splitType === "VerticalHorizontal") {
            const requiredFields = [
                "datasetCStructureType",
                "datasetDStructureType",
                "datasetCSchemaNoise",
                "datasetCSchemaKeyNoise",
                "datasetCSchemaDeleteSchema",
                "datasetCSchemaMultiselect",
                "datasetDSchemaNoise",
                "datasetDSchemaKeyNoise",
                "datasetDSchemaDeleteSchema",
                "datasetDSchemaMultiselect",
                "datasetCDataNoise",
                "datasetCDataKeyNoise",
                "datasetCDataMultiselect",
                "datasetDDataNoise",
                "datasetDDataKeyNoise",
                "datasetDDataMultiselect",
                "datasetCShuffleOption",
                "datasetDShuffleOption"
            ];

            requiredFields.forEach((field) => {
                if (data[field as keyof typeof data] === null) {
                    ctx.addIssue({
                        code: z.ZodIssueCode.custom,
                        message: `${field} is required when splitType is VerticalHorizontal`,
                        path: [field]
                    });
                }
            });
        } else {
            const optionalFields = [
                "datasetCBCNFSliderValue",
                "datasetCMergeColumnsSliderValue",
                "datasetDBCNFSliderValue",
                "datasetDMergeColumnsSliderValue",
                "datasetCSchemaNoiseValue",
                "datasetDSchemaNoiseValue",
                "datasetCDataNoiseValue",
                "datasetCDataNoiseInside",
                "datasetDDataNoiseValue",
                "datasetDDataNoiseInside"
            ];

            optionalFields.forEach((field) => {
                if (data[field as keyof typeof data] !== null) {
                    ctx.addIssue({
                        code: z.ZodIssueCode.custom,
                        message: `${field} should be null when splitType is Horizontal or Vertical`,
                        path: [field]
                    });
                }
            });
        }
    });





