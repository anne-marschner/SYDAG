import React, { useState, useCallback } from 'react';
import { useMultistepForm } from '@/hooks/useMultistepForm';
import { FormItems } from '@/components/types/formTypes';
import { validateStep } from '@/utils/formValidation/validator';

export function useFormHandler(initialValues: FormItems, totalSteps: number) {
    const [formData, setFormData] = useState<FormItems>(initialValues);
    const [errors, setErrors] = useState<Record<string, string[]>>({});
    const [showSuccessMsg, setShowSuccessMsg] = useState(false);
    const [isGenerating, setIsGenerating] = useState(false);
    const [downloadUrl, setDownloadUrl] = useState<string>('');
    const [downloadBlob, setDownloadBlob] = useState<Blob | null>(null); 

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

    /**
     * Function to update form data.
     */
    const updateForm = useCallback((fieldToUpdate: Partial<FormItems>) => {
        setFormData((prevFormData) => ({ ...prevFormData, ...fieldToUpdate }));
    }, []);

    /**
     * HandleOnSubmit function.
     */
    const handleOnSubmit = useCallback(
        async (e: React.FormEvent<HTMLFormElement>) => {
            e.preventDefault();

            // Validate the current step
            const stepErrors = await validateStep(currentStepIndex, formData);
            setErrors(stepErrors);

            // If there are any errors, stop here
            if (Object.keys(stepErrors).length > 0) {
                return;
            }

            // Jump to the last step if UploadJson is chosen
            if (currentStepIndex === 1) {
                if (formData.mode === 'UploadJson' && formData.jsonFile) {
                    goTo(totalSteps - 1);
                    return;
                }
            }

            // If we are on the last step, submit the data
            if (isLastStep) {
                try {
                    setIsGenerating(true);

                    const formDataToSend = new FormData();

                    // Always append the CSV file
                    if (formData.csvFile) {
                        formDataToSend.append('csvFile', formData.csvFile);
                    } else {
                        setErrors((prevErrors) => ({
                            ...prevErrors,
                            csvFile: ['CSV file is required.'],
                        }));
                        setIsGenerating(false);
                        return;
                    }

                    let jsonParameters: Partial<FormItems>;

                    // Merge JSON file data with the rest of the formData
                    if (formData.mode === 'UploadJson' && formData.jsonFile) {
                        const jsonFileContent = await formData.jsonFile.text();
                        const jsonFileData = JSON.parse(jsonFileContent);

                        // Omit actual files from the JSON
                        const { csvFile, jsonFile, ...rest } = formData;

                        // Merge the uploaded JSON data with existing form fields
                        jsonParameters = {
                            ...rest,
                            ...jsonFileData,
                        };
                    } else {
                        // If no JSON file uploaded, remove file objects from final JSON
                        const { csvFile, jsonFile, ...rest } = formData;
                        jsonParameters = rest;
                    }

                    // Append all parameters as JSON in the multipart request
                    formDataToSend.append(
                        'parameters',
                        new Blob([JSON.stringify(jsonParameters)], { type: 'application/json' })
                    );

                    formDataToSend.forEach((value, key) => {
                        console.log(`Key: ${key}`);
                        if (value instanceof Blob) {
                            const reader = new FileReader();
                            reader.onload = function () {
                                console.log(`Value: ${reader.result}`);
                            };
                            reader.readAsText(value);
                        } else {
                            console.log(`Value: ${value}`);
                        }
                    });

                    // Send the POST request to Spring Boot API endpoint
                    const response = await fetch('http://localhost:8080/api/runSYDAG', {
                        method: 'POST',
                        body: formDataToSend,
                    });

                    if (response.ok) {
                        // Extract the ZIP file from the response
                        const blob = await response.blob();
                        setDownloadBlob(blob);

                        // Create an URL for the blob
                        const url = window.URL.createObjectURL(blob);
                        setDownloadUrl(url);

                        setShowSuccessMsg(true);
                    } else {
                        // Handle error response
                        const errorData = await response.json();
                        setErrors((prevErrors) => ({
                            ...prevErrors,
                            server: [errorData.message || 'An error occurred on the server.'],
                        }));
                    }
                } catch (error) {
                    setErrors((prevErrors) => ({
                        ...prevErrors,
                        network: ['A network error occurred.'],
                    }));
                } finally {
                    setIsGenerating(false);
                }
            } else {
                // Go to the next step
                nextStep();
            }
        },
        [
            currentStepIndex,
            formData,
            isLastStep,
            nextStep,
            goTo,
            totalSteps,
            updateForm,
        ]
    );

    return {
        formData,
        errors,
        showSuccessMsg,
        isGenerating,
        downloadUrl,
        downloadBlob,
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
