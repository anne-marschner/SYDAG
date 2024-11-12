// hooks/useFormHandler.ts
import React, { useState } from 'react';
import { useMultistepForm } from '@/hooks/useMultistepForm';
import { FormItems } from '@/components/types/formTypes';
import { validateStep } from '@/utils/formValidation/validator';

// utils/formDataHelper.ts

export function useFormHandler(initialValues: FormItems, totalSteps: number) {
    const [formData, setFormData] = useState<FormItems>(initialValues);
    const [errors, setErrors] = useState<Record<string, string[]>>({});
    const [showSuccessMsg, setShowSuccessMsg] = useState(false);

    const {
        previousStep,
        nextStep,
        currentStepIndex,
        isFirstStep,
        isLastStep,
        goTo,
        startForm,
        isIntroScreen,
    } = useMultistepForm(totalSteps, true);

    function updateForm(fieldToUpdate: Partial<FormItems>) {
        setFormData((prevFormData) => ({ ...prevFormData, ...fieldToUpdate }));
    }

    const handleOnSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();

        const stepErrors = await validateStep(currentStepIndex, formData);
        setErrors(stepErrors);

        if (Object.keys(stepErrors).length > 0) {
            return;
        }

        // Handle step navigation logic
        if (currentStepIndex === 1) {
            if (formData.mode === "UploadJson") {
                if (formData.jsonFile) {
                    goTo(totalSteps - 1);
                    return;
                }
            }
        }

        if (isLastStep) {
            try {
                // Create a FormData object for multipart/form-data submission
                const formDataToSend = new FormData();

                // Append the CSV file to the FormData
                if (formData.csvFile) {
                    formDataToSend.append('csvFile', formData.csvFile);
                } else {
                    setErrors((prevErrors) => ({
                        ...prevErrors,
                        csvFile: ['CSV file is required.'],
                    }));
                    return;
                }

                // Prepare the JSON parameters
                let jsonParameters: Partial<FormItems>;

                if (formData.mode === 'UploadJson' && formData.jsonFile) {
                    // Read the JSON file and use its content
                    const jsonFileContent = await formData.jsonFile.text();
                    jsonParameters = JSON.parse(jsonFileContent);
                } else {
                    // Use the formData as is (excluding files)
                    const { csvFile, jsonFile, ...rest } = formData;
                    jsonParameters = rest;
                }

                // Append the JSON parameters as a Blob with application/json content type
                formDataToSend.append(
                    'parameters',
                    new Blob([JSON.stringify(jsonParameters)], { type: 'application/json' })
                );

                // Send the POST request to Spring Boot API endpoint
                const response = await fetch('http://localhost:8080/api/runSYDAG', {
                    method: 'POST',
                    body: formDataToSend,
                });

                if (response.ok) {
                    setShowSuccessMsg(true);
                } else {
                    const errorData = await response.json();
                    setErrors((prevErrors) => ({
                        ...prevErrors,
                        server: [errorData.message || 'An error occurred on the server.'],
                    }));
                }
            } catch (error) {
                setErrors((prevErrors) => ({
                    ...prevErrors,
                    network: ['A network error occurred. Please try again.'],
                }));
            }
        } else {
            nextStep();
        }
    };

    // The return statement should be outside handleOnSubmit
    return {
        formData,
        errors,
        showSuccessMsg,
        currentStepIndex,
        isFirstStep,
        isLastStep,
        nextStep,
        previousStep,
        goTo,
        startForm,
        isIntroScreen,
        updateForm,
        handleOnSubmit,
    };
}
