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
        .nullable(), // jsonFile can be null if mode is "Manual"
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
        // Split Type (Consistent with Step 3)
        splitType: z.enum(["Horizontal", "Vertical", "VerticalHorizontal"]).nullable(),

        // Structure Type selections for all datasets
        dataset1StructureType: z
            .enum(["BCNF", "Join Columns", "No Change"])
            .nullable(),
        dataset2StructureType: z
            .enum(["BCNF", "Join Columns", "No Change"])
            .nullable(),
        dataset3StructureType: z
            .enum(["BCNF", "Join Columns", "No Change"])
            .nullable().optional(),
        dataset4StructureType: z
            .enum(["BCNF", "Join Columns", "No Change"])
            .nullable().optional(),

        // Slider values for Dataset 1
        dataset1BCNFSliderValue: z
            .number()
            .min(0, {message: "BCNF Slider Value must be at least 0%"})
            .max(100, {message: "BCNF Slider Value cannot exceed 100%"})
            .nullable(),
        dataset1JoinColumnsSliderValue: z
            .number()
            .min(0, {message: "Join Columns Slider Value must be at least 0%"})
            .max(100, {message: "Join Columns Slider Value cannot exceed 100%"})
            .nullable(),

        // Slider values for Dataset 2
        dataset2BCNFSliderValue: z
            .number()
            .min(0, {message: "BCNF Slider Value must be at least 0%"})
            .max(100, {message: "BCNF Slider Value cannot exceed 100%"})
            .nullable(),
        dataset2JoinColumnsSliderValue: z
            .number()
            .min(0, {message: "Join Columns Slider Value must be at least 0%"})
            .max(100, {message: "Join Columns Slider Value cannot exceed 100%"})
            .nullable(),

        // Slider values for Dataset 3
        dataset3BCNFSliderValue: z
            .number()
            .min(0, {message: "BCNF Slider Value must be at least 0%"})
            .max(100, {message: "BCNF Slider Value cannot exceed 100%"})
            .nullable().optional(),
        dataset3JoinColumnsSliderValue: z
            .number()
            .min(0, {message: "Join Columns Slider Value must be at least 0%"})
            .max(100, {message: "Join Columns Slider Value cannot exceed 100%"})
            .nullable().optional(),

        // Slider values for Dataset 4
        dataset4BCNFSliderValue: z
            .number()
            .min(0, {message: "BCNF Slider Value must be at least 0%"})
            .max(100, {message: "BCNF Slider Value cannot exceed 100%"})
            .nullable().optional(),
        dataset4JoinColumnsSliderValue: z
            .number()
            .min(0, {message: "Join Columns Slider Value must be at least 0%"})
            .max(100, {message: "Join Columns Slider Value cannot exceed 100%"})
            .nullable().optional(),
    })
    .superRefine((data, ctx) => {
        // Conditional validation for Datasets 1 and 2
        const datasets = [1, 2, 3, 4];
        datasets.forEach((dataset) => {
            const structureType = data[`dataset${dataset}StructureType` as keyof typeof data];
            const bcfnSlider = data[`dataset${dataset}BCNFSliderValue` as keyof typeof data];
            const joinColumnsSlider = data[`dataset${dataset}JoinColumnsSliderValue` as keyof typeof data];

            if (structureType === "BCNF") {
                if (bcfnSlider === null || bcfnSlider === undefined) {
                    ctx.addIssue({
                        code: z.ZodIssueCode.custom,
                        message: `BCNF Slider Value for Dataset ${dataset} is required when BCNF is selected`,
                        path: [`dataset${dataset}BCNFSliderValue` as keyof typeof data],
                    });
                }
            }

            if (structureType === "Join Columns") {
                if (joinColumnsSlider === null || joinColumnsSlider === undefined) {
                    ctx.addIssue({
                        code: z.ZodIssueCode.custom,
                        message: `Join Columns Slider Value for Dataset ${dataset} is required when Join Columns is selected`,
                        path: [`dataset${dataset}JoinColumnsSliderValue` as keyof typeof data],
                    });
                }
            }
        });

        // Additional conditional validations based on splitType
        if (data.splitType !== "VerticalHorizontal") {
            // If splitType is not VerticalHorizontal, ensure datasets 3 and 4 are not set
            if (data.dataset3StructureType !== null) {
                ctx.addIssue({
                    code: z.ZodIssueCode.custom,
                    message: "Dataset 3 structure type should not be set unless splitType is VerticalHorizontal",
                    path: ["dataset3StructureType"],
                });
            }
            if (data.dataset4StructureType !== null) {
                ctx.addIssue({
                    code: z.ZodIssueCode.custom,
                    message: "Dataset 4 structure type should not be set unless splitType is VerticalHorizontal",
                    path: ["dataset4StructureType"],
                });
            }
        }
    });


// Step 5 Schema
export const Step5Schema = z
    .object({
            dataset1SchemaNoise: z.boolean().nullable(),
            dataset1SchemaNoiseValue: z.number().min(0).max(100).nullable(),
            dataset1SchemaKeyNoise: z.boolean().nullable(),
            dataset1SchemaDeleteSchema: z.boolean().nullable(),
            dataset1SchemaMultiselect: z.array(z.string()).nullable(),

            }
        );



// Step 6 Data Schema
export const Step6Schema = z
    .object({

    });


// Step 7 Schema
export const Step7Schema = z.object({
    dataset1ShuffleOption: z.enum(['Shuffle Columns', 'Shuffle Rows', 'No Change']).nullable(),
    dataset2ShuffleOption: z.enum(['Shuffle Columns', 'Shuffle Rows', 'No Change']).nullable(),
    dataset3ShuffleOption: z.enum(['Shuffle Columns', 'Shuffle Rows', 'No Change']).nullable(),
    dataset4ShuffleOption: z.enum(['Shuffle Columns', 'Shuffle Rows', 'No Change']).nullable(),
});

export const FormItemsJSONSchema = z
    .object({

        // Step 3
        splitType: z.enum(['Horizontal', 'Vertical', 'VerticalHorizontal']),
        rowOverlapPercentage: z.number().min(0).max(100).nullable(),
        columnOverlapPercentage: z.number().min(0).max(100).nullable(),
        rowDistribution: z.number().min(0).max(100).nullable(),
        columnDistribution: z.number().min(0).max(100).nullable(),
        overlapType: z.enum(['Mixed Overlap', 'Block Overlap']).nullable(),

        // Step 4 (Datasets Structure)
        dataset1StructureType: z.enum(['BCNF', 'Join Columns', 'No Change']).nullable(),
        dataset1BCNFSliderValue: z.number().min(0).max(100).nullable(),
        dataset1JoinColumnsSliderValue: z.number().min(0).max(100).nullable(),

        dataset2StructureType: z.enum(['BCNF', 'Join Columns', 'No Change']).nullable(),
        dataset2BCNFSliderValue: z.number().min(0).max(100).nullable(),
        dataset2JoinColumnsSliderValue: z.number().min(0).max(100).nullable(),

        dataset3StructureType: z.enum(['BCNF', 'Join Columns', 'No Change']).nullable(),
        dataset3BCNFSliderValue: z.number().min(0).max(100).nullable(),
        dataset3JoinColumnsSliderValue: z.number().min(0).max(100).nullable(),

        dataset4StructureType: z.enum(['BCNF', 'Join Columns', 'No Change']).nullable(),
        dataset4BCNFSliderValue: z.number().min(0).max(100).nullable(),
        dataset4JoinColumnsSliderValue: z.number().min(0).max(100).nullable(),

        // Step 5 (Schema Noise)
        dataset1SchemaNoise: z.boolean().nullable(),
        dataset1SchemaNoiseValue: z.number().min(0).max(100).nullable(),
        dataset1SchemaKeyNoise: z.boolean().nullable(),
        dataset1SchemaDeleteSchema: z.boolean().nullable(),
        dataset1SchemaMultiselect: z.array(z.string()).nullable(),

        dataset2SchemaNoise: z.boolean().nullable(),
        dataset2SchemaNoiseValue: z.number().min(0).max(100).nullable(),
        dataset2SchemaKeyNoise: z.boolean().nullable(),
        dataset2SchemaDeleteSchema: z.boolean().nullable(),
        dataset2SchemaMultiselect: z.array(z.string()).nullable(),

        dataset3SchemaNoise: z.boolean().nullable(),
        dataset3SchemaNoiseValue: z.number().min(0).max(100).nullable(),
        dataset3SchemaKeyNoise: z.boolean().nullable(),
        dataset3SchemaDeleteSchema: z.boolean().nullable(),
        dataset3SchemaMultiselect: z.array(z.string()).nullable(),

        dataset4SchemaNoise: z.boolean().nullable(),
        dataset4SchemaNoiseValue: z.number().min(0).max(100).nullable(),
        dataset4SchemaKeyNoise: z.boolean().nullable(),
        dataset4SchemaDeleteSchema: z.boolean().nullable(),
        dataset4SchemaMultiselect: z.array(z.string()).nullable(),

        // Step 6 (Data Noise)
        dataset1DataNoise: z.boolean().nullable(),
        dataset1DataNoiseValue: z.number().min(0).max(100).nullable(),
        dataset1DataKeyNoise: z.boolean().nullable(),
        dataset1DataNoiseInside: z.number().min(0).max(100).nullable(),
        dataset1DataMultiselect: z.array(z.string()).nullable(),

        dataset2DataNoise: z.boolean().nullable(),
        dataset2DataNoiseValue: z.number().min(0).max(100).nullable(),
        dataset2DataKeyNoise: z.boolean().nullable(),
        dataset2DataNoiseInside: z.number().min(0).max(100).nullable(),
        dataset2DataMultiselect: z.array(z.string()).nullable(),

        dataset3DataNoise: z.boolean().nullable(),
        dataset3DataNoiseValue: z.number().min(0).max(100).nullable(),
        dataset3DataKeyNoise: z.boolean().nullable(),
        dataset3DataNoiseInside: z.number().min(0).max(100).nullable(),
        dataset3DataMultiselect: z.array(z.string()).nullable(),

        dataset4DataNoise: z.boolean().nullable(),
        dataset4DataNoiseValue: z.number().min(0).max(100).nullable(),
        dataset4DataKeyNoise: z.boolean().nullable(),
        dataset4DataNoiseInside: z.number().min(0).max(100).nullable(),
        dataset4DataMultiselect: z.array(z.string()).nullable(),

        // Step 7 (Shuffle Options)
        dataset1ShuffleOption: z.enum(['Shuffle Columns', 'Shuffle Rows', 'No Change']).nullable(),
        dataset2ShuffleOption: z.enum(['Shuffle Columns', 'Shuffle Rows', 'No Change']).nullable(),
        dataset3ShuffleOption: z.enum(['Shuffle Columns', 'Shuffle Rows', 'No Change']).nullable(),
        dataset4ShuffleOption: z.enum(['Shuffle Columns', 'Shuffle Rows', 'No Change']).nullable()
    })
    .superRefine((data, ctx) => {
        if (data.splitType === "VerticalHorizontal") {
            const requiredFields = [
                "dataset3StructureType",
                "dataset3BCNFSliderValue",
                "dataset3JoinColumnsSliderValue",
                "dataset4StructureType",
                "dataset4BCNFSliderValue",
                "dataset4JoinColumnsSliderValue",
                "dataset3SchemaNoise",
                "dataset3SchemaNoiseValue",
                "dataset3SchemaKeyNoise",
                "dataset3SchemaDeleteSchema",
                "dataset3SchemaMultiselect",
                "dataset4SchemaNoise",
                "dataset4SchemaNoiseValue",
                "dataset4SchemaKeyNoise",
                "dataset4SchemaDeleteSchema",
                "dataset4SchemaMultiselect",
                "dataset3DataNoise",
                "dataset3DataNoiseValue",
                "dataset3DataKeyNoise",
                "dataset3DataNoiseInside",
                "dataset3DataMultiselect",
                "dataset4DataNoise",
                "dataset4DataNoiseValue",
                "dataset4DataKeyNoise",
                "dataset4DataNoiseInside",
                "dataset4DataMultiselect",
                "dataset3ShuffleOption",
                "dataset4ShuffleOption"
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
                "dataset3StructureType",
                "dataset3BCNFSliderValue",
                "dataset3JoinColumnsSliderValue",
                "dataset4StructureType",
                "dataset4BCNFSliderValue",
                "dataset4JoinColumnsSliderValue"
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





