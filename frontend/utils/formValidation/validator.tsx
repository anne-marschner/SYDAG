import {z} from 'zod';
import {
    Step1Schema,
    Step2Schema,
    Step3Schema,
    Step4Schema,
    Step5Schema,
    Step6Schema,
    Step7Schema,
} from '@/utils/formValidation/formSchemas/formSchemas';

// Mapping of step index to the corresponding schema
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
 */
export const validateStep = async (currentStepIndex: number, formData: any) => {
    const currentSchema = stepSchemas[currentStepIndex];

    if (currentSchema) {
        try {
            await currentSchema.parseAsync(formData); 
            return {};
        } catch (error) {
            if (error instanceof z.ZodError) {
                const fieldErrors = error.flatten().fieldErrors;
                const processedFieldErrors: Record<string, string[]> = {};

                for (const key in fieldErrors) {
                    processedFieldErrors[key] = fieldErrors[key] ?? [];
                }

                return processedFieldErrors;
            } else {
                console.error('Validation Error:', error);
            }
        }
    }

    return {};
};
