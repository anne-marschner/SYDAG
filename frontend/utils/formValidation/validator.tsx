// utils/validator.ts
import {z} from 'zod';
import {
    Step1Schema,
    Step2Schema,
    Step3Schema,
    Step4Schema,
    Step5Schema,
    Step6Schema,
    Step7Schema,
} from '@/utils/formValidation/formSchemas/formSchemas'; // Import schemas

// Mapping step index to the corresponding Zod schema
const stepSchemas: { [key: number]: z.ZodSchema } = {
    0: Step1Schema,
    1: Step2Schema,
    2: Step3Schema,
    3: Step4Schema,
    4: Step5Schema,
    5: Step6Schema,
    6: Step7Schema,
};

/**
 * Validates form data for a given step based on the step index.
 * @param currentStepIndex - The current step index in the multi-step form
 * @param formData - The form data object being validated
 * @returns A promise that resolves with an error object or an empty object if the data is valid.
 */
export const validateStep = async (currentStepIndex: number, formData: any) => {
    const currentSchema = stepSchemas[currentStepIndex];

    if (currentSchema) {
        try {
            await currentSchema.parseAsync(formData); // Validate data
            return {}; // No errors if validation passes
        } catch (error) {
            if (error instanceof z.ZodError) {
                const fieldErrors = error.flatten().fieldErrors;
                const processedFieldErrors: Record<string, string[]> = {};

                // Ensure all values are string arrays
                for (const key in fieldErrors) {
                    processedFieldErrors[key] = fieldErrors[key] ?? [];
                }

                return processedFieldErrors; // Return the formatted errors
            } else {
                console.error('Validation Error:', error);
            }
        }
    }

    return {}; // No schema for the current step, or validation passed
};
