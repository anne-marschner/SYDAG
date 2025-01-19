"use client";
import React, { useState } from "react";
import Image from "next/image";
import * as ToggleGroup from "@radix-ui/react-toggle-group";

import FormWrapper from "./FormWrapper";
import { FormItems } from "@/components/types/formTypes";
import JsonFileIcon from "../public/assets/json-file-svgrepo-com.svg";
import ManualIcon from "../public/assets/typing-svgrepo-com.svg";

import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { FormItemsJSONSchema } from "@/utils/formValidation/formSchemas/formSchemas";
import { z } from "zod";

/**
 * We keep the original props for `mode`, `jsonFile`, etc.,
 * plus an *optional* `formData` for merging.
 */
type StepProps = {
    mode: "UploadJson" | "Manual" | null;
    jsonFile: File | null;
    manualInput: boolean | null;
    errors: Record<string, string[]>;

    /**
     * If you want to merge in existing data (like csvFile, hasHeaders, etc.),
     * pass it here. If not provided, `formData` can be undefined.
     */
    formData?: FormItems;

    /**
     * Update function that takes a Partial<FormItems> and merges it in the parent.
     */
    updateForm: (fields: Partial<FormItems>) => void;
};

const Step2_ModeForm = ({
                            mode,
                            jsonFile,
                            manualInput,
                            errors,

                            // Optionally passed for merging older fields:
                            formData,

                            updateForm
                        }: StepProps) => {
    // Local state for toggling between "UploadJson" and "Manual"
    const [modeSelected, setModeSelected] = useState<string | null>(mode);
    const [fileError, setFileError] = useState<string | null>(null);

    /**
     * Called when the toggle group changes to "UploadJson" or "Manual".
     */
    const handleValueChange = (newModeSelected: string) => {
        if (newModeSelected) {
            setModeSelected(newModeSelected);

            const isManualMode = newModeSelected === "Manual";

            // If switching to manual, clear out file errors
            if (isManualMode) {
                setFileError(null);
            }

            // Partial update: keep your existing approach for these fields
            updateForm({
                mode: newModeSelected as "UploadJson" | "Manual",
                jsonFile: isManualMode ? null : jsonFile,
                manualInput: isManualMode,
            });
        }
    };

    /**
     * Called when the user selects a JSON file.
     */
    const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        if (e.target.files && e.target.files.length > 0) {
            const file = e.target.files[0];

            // Validate if the selected file is a JSON file
            if (file.type !== "application/json") {
                setFileError("File must be a JSON.");
                updateForm({ jsonFile: null });
                return;
            }

            // Clear any previous file error
            setFileError(null);

            // Immediately update the form with the File reference
            updateForm({ jsonFile: file });

            // Now read the file and parse it
            const reader = new FileReader();
            reader.onload = async (event) => {
                try {
                    const text = event.target?.result;
                    if (typeof text === "string") {
                        // Attempt to parse and validate the JSON
                        const jsonData = JSON.parse(text);
                        const parsedData = await FormItemsJSONSchema.parseAsync(jsonData);

                        // If we have formData, merge it so we preserve older fields (e.g. csvFile, hasHeaders, etc.)
                        // If formData is undefined, just update with parsedData alone.
                        if (formData) {
                            updateForm({ ...formData, ...parsedData });
                        } else {
                            updateForm(parsedData);
                        }
                    } else {
                        setFileError("Unable to read file.");
                    }
                } catch (error) {
                    if (error instanceof z.ZodError) {
                        setFileError("Invalid JSON structure: " + error.message);
                    } else {
                        setFileError("Invalid JSON file.");
                    }
                }
            };

            reader.onerror = () => setFileError("Error reading file.");
            reader.readAsText(file);
        }
    };

    return (
        <FormWrapper
            title="Select the Mode Type"
            description="You have the option to upload a JSON or enter the configuration manually"
        >
            {/* Mode toggle group */}
            <ToggleGroup.Root
                orientation="horizontal"
                className="flex flex-col gap-3 my-2 md:flex-row md:items-center md:justify-between md:gap-8"
                type="single"
                value={modeSelected || undefined}
                onValueChange={handleValueChange}
            >
                <ToggleGroup.Item
                    value="UploadJson"
                    className="border border-neutral-600 flex items-start gap-3 p-3 h-24 rounded-md data-[state=on]:border-[#77f6aa] data-[state=on]:bg-neutral-900 focus:border-[#77f6aa] outline-none hover:border-[#77f6aa] md:h-44 md:w-1/2 md:flex-col md:justify-between md:gap-0"
                >
                    <Image src={JsonFileIcon} alt="Upload JSON" width={40} height={40} />
                    <div className="relative -top-1 flex flex-col items-start md:top-0">
                        <p className="text-white font-semibold">Upload JSON</p>
                        <p className="text-sm custom-label">Set configuration automatically</p>
                    </div>
                </ToggleGroup.Item>

                <ToggleGroup.Item
                    value="Manual"
                    className="border border-neutral-600 flex items-start gap-3 p-3 h-24 rounded-md data-[state=on]:border-[#77f6aa] data-[state=on]:bg-neutral-900 focus:border-[#77f6aa] outline-none hover:border-[#77f6aa] md:h-44 md:w-1/2 md:flex-col md:justify-between md:gap-0"
                >
                    <Image src={ManualIcon} alt="Manual" width={40} height={40} />
                    <div className="relative -top-1 flex flex-col items-start md:top-0">
                        <p className="text-white font-semibold">Manual</p>
                        <p className="text-sm custom-label">Set configuration manually</p>
                    </div>
                </ToggleGroup.Item>
            </ToggleGroup.Root>

            {/* Error if 'mode' is invalid */}
            {errors.mode && (
                <p className="text-red-500 text-sm mt-2">{errors.mode[0]}</p>
            )}

            {/* Only show file input if the user selected UploadJson */}
            {modeSelected === "UploadJson" && (
                <div className="w-full flex flex-col bg-neutral-900 p-3 rounded-md mt-4 custom-label">
                    <div className="flex flex-col gap-2">
                        <Label htmlFor="jsonFile">JSON File</Label>
                        {jsonFile && (
                            <p className="text-sm text-neutral-700">
                                Current file: {jsonFile.name}
                            </p>
                        )}
                        <Input
                            type="file"
                            name="jsonFile"
                            id="jsonFile"
                            accept=".json"
                            onChange={handleFileChange}
                            className="w-full"
                        />
                        {fileError && <p className="text-red-500 text-sm">{fileError}</p>}
                        {errors.jsonFile && (
                            <p className="text-red-500 text-sm">{errors.jsonFile[0]}</p>
                        )}
                    </div>

                    {/* Download JSON Template link */}
                    <a
                        href="resources/template.json"
                        download="json-template.json"
                        className="mt-4 inline-block text-center bg-[#77f6aa] text-neutral-900 px-4 py-2 rounded-md font-semibold"
                    >
                        Download JSON Template
                    </a>
                </div>
            )}
        </FormWrapper>
    );
};

export default Step2_ModeForm;
