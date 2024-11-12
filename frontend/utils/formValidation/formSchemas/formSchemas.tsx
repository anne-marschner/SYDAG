import { z } from 'zod';

// Step 1 Schema
export const Step1Schema = z.object({
    csvFile: z
        .instanceof(File, { message: "Please upload a CSV file." })
        .refine((file) => file.size > 0, { message: "File cannot be empty." }),
    hasHeaders: z.boolean({
        required_error: "Please indicate if the CSV has headers.",
    }),
    separator: z.enum([";", ":", ","], {
        errorMap: () => ({ message: "Separator must be one of the following: ';', ':', or ','" }),
    }),
});


// Step 2 Schema
export const Step2Schema = z.object({
    mode: z.enum(['UploadJson', 'Manual']),
    jsonFile: z
        .instanceof(File)
        .refine((file) => file.type === 'application/json', { message: 'File must be a JSON.' })
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
    splitType: z.enum(['Horizontal', 'Vertical']),
    rowOverlapPercentage: z.number().min(0).max(100).optional(), // Optional since it's used for Horizontal
    columnOverlapPercentage: z.number().min(0).max(100).optional(), // Optional since it's used for Vertical
}).refine((data) => {
    // Conditionally validate rowOverLab only if splitType is "Horizontal"
    if (data.splitType === 'Horizontal' && (data.rowOverlapPercentage === undefined || data.rowOverlapPercentage === null)) {
        return false;
    }
    // Conditionally validate columnOverLab only if splitType is "Vertical"
    if (data.splitType === 'Vertical' && (data.columnOverlapPercentage === undefined || data.columnOverlapPercentage === null)) {
        return false;
    }
    return true;
}, {
    message: "rowOverLab is required for Horizontal split, and columnOverLab is required for Vertical split",
    path: ["rowOverLab", "columnOverLab"], // This will help point to the specific path when validation fails
});

// Step 4 Schema
export const Step4Schema = z.object({
    dataset1StructureType: z.enum(['BCNF', 'Join Columns', 'No Change']),
    dataset2StructureType: z.enum(['BCNF', 'Join Columns', 'No Change'])
}).refine((data) => {
    // Ensure that at least one of the datasets has a structure type defined other than "No Change"
    return data.dataset1StructureType !== 'No Change' || data.dataset2StructureType !== 'No Change';
}, {
    message: "At least one of 'dataset1StructureType' or 'dataset2StructureType' must be set to a value other than 'No Change'",
    path: ["dataset1StructureType", "dataset2StructureType"] // Specifies error path
});


// Step 5 Schema
export const Step5Schema = z.object({
    dataset1SchemaNoise: z.boolean(),
    dataset1SchemaNoiseValue: z.number().min(0).max(100),
    dataset1SchemaKeyNoise: z.boolean(),
    
    dataset2SchemaNoise: z.boolean(),
    dataset2SchemaNoiseValue: z.number().min(0).max(100),
    dataset2SchemaKeyNoise: z.boolean(),
});


// Step 6 Data
export const Step6Schema = z.object({
    dataset1DataNoise: z.boolean(),
    dataset1DataNoiseValue: z.number().min(0).max(100),
    dataset1DataKeyNoise: z.boolean(),
    dataset2DataNoise: z.boolean(),
    dataset2DataNoiseValue: z.number().min(0).max(100),
    dataset2DataKeyNoise: z.boolean(),
});


// Step 7 Schema
export const Step7Schema = z.object({
    dataset1ShuffleOption: z.enum(['Shuffle Columns', 'Shuffle Rows', 'No Change']),
    dataset2ShuffleOption: z.enum(['Shuffle Columns', 'Shuffle Rows', 'No Change']),
});


// Updated Zod schema to match the provided JSON structure
export const FormItemsJSONSchema = z.object({

    // Step 1
    hasHeaders: z.boolean().nullable(),
    separator: z.enum([";", ":", ","], {
        errorMap: () => ({ message: "Separator must be one of the following: ';', ':', or ','" }),
    }).nullable(),

    // Step 3 (assuming Step 2 does not have changes)
    splitType: z.enum(['Horizontal', 'Vertical']).nullable(),
    rowOverlapPercentage: z.number().min(0).max(100).nullable(), // Renamed to match JSON key
    columnOverlapPercentage: z.number().min(0).max(100).nullable(), // Renamed to match JSON key

    // Step 4
    dataset1StructureType: z.enum(['BCNF', 'Join Columns', 'No Change']).nullable(),
    dataset2StructureType: z.enum(['BCNF', 'Join Columns', 'No Change']).nullable(),

    // Step 5
    dataset1SchemaNoise: z.boolean().nullable(),
    dataset1SchemaNoiseValue: z.number().min(0).max(100).nullable(),
    dataset1SchemaKeyNoise: z.boolean().nullable(),
    dataset2SchemaNoise: z.boolean().nullable(),
    dataset2SchemaNoiseValue: z.number().min(0).max(100).nullable(),
    dataset2SchemaKeyNoise: z.boolean().nullable(),

    // Step 6
    dataset1DataNoise: z.boolean().nullable(),
    dataset1DataNoiseValue: z.number().min(0).max(100).nullable(),
    dataset1DataKeyNoise: z.boolean().nullable(),
    dataset2DataNoise: z.boolean().nullable(),
    dataset2DataNoiseValue: z.number().min(0).max(100).nullable(),
    dataset2DataKeyNoise: z.boolean().nullable(),

    // Step 7
    dataset1ShuffleOption: z.enum(['Shuffle Columns', 'Shuffle Rows', 'No Change']).nullable(),
    dataset2ShuffleOption: z.enum(['Shuffle Columns', 'Shuffle Rows', 'No Change']).nullable(),

    // Additional Methods
    selectedSchemaMethods1: z.array(z.string()),
    selectedSchemaMethods2: z.array(z.string()),
    selectedStringMethods1: z.array(z.string()),
    selectedStringMethods2: z.array(z.string()),
    selectedNumericMethods1: z.array(z.string()),
    selectedNumericMethods2: z.array(z.string())
});

