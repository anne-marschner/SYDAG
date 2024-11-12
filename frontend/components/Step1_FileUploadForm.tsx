"use client";
import React, { useState } from "react";
import FormWrapper from "./FormWrapper";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { FormItems } from "@/components/types/formTypes";
import { Toggle } from "@/components/ui/toggle";

type StepProps = {
    csvFile: File | null;
    hasHeaders: boolean | null;
    separator: string | null;
    updateForm: (fieldToUpdate: Partial<FormItems>) => void;
    errors: Record<string, string[]>;
};

const Step1_FileUploadForm = ({
    csvFile,
    hasHeaders,
    separator,
    errors,
    updateForm,
}: StepProps) => {
    // State for tracking whether the CSV file has headers or not
    const [hasHeadersState, setHasHeadersState] = useState<boolean | null>(
        hasHeaders
    );

    // State for the separator
    const [separatorState, setSeparatorState] = useState<string | null>(separator);

    const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        if (e.target.files && e.target.files.length > 0) {
            const file = e.target.files[0];
            updateForm({ csvFile: file });
        }
    };

    const handleHasHeadersToggle = (pressed: boolean) => {
        setHasHeadersState(pressed);
        updateForm({ hasHeaders: pressed });
    };

    const handleSeparatorChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const newSeparator = e.target.value;
        setSeparatorState(newSeparator);
        updateForm({ separator: newSeparator });
    };

    return (
        <FormWrapper
            title="Upload your Datasource"
            description="Please provide a CSV File"
        >
            <div className="w-full flex flex-col gap-10">
                {/* File Upload Section */}
                <div className="flex flex-col gap-2">
                    <Label htmlFor="file">CSV File</Label>

                    {/* Display the uploaded file name if available */}
                    {csvFile && (
                        <p className="text-sm text-neutral-700">
                            Current file: {csvFile.name}
                        </p>
                    )}

                    {/* Input field for file uploads */}
                    <Input
                        autoFocus
                        type="file"
                        name="file"
                        id="file"
                        accept=".csv,.sql"
                        onChange={handleFileChange}
                        className="w-full"
                    />

                    {/* Displays an error message if there's a validation error for the file input */}
                    {errors.csvFile && (
                        <p className="text-red-500 text-sm">{errors.csvFile[0]}</p>
                    )}
                </div>

                {/* Toggle for indicating whether the CSV has headers */}
                <div className="flex flex-col gap-5">
                    <Label htmlFor="hasHeadersToggle">
                        Does the CSV file have headers?
                    </Label>
                    <div className="flex items-center gap-5">
                        <Toggle
                            pressed={hasHeadersState ?? undefined}
                            onPressedChange={handleHasHeadersToggle}
                            id="hasHeadersToggle"
                            className={
                                hasHeadersState === null
                                    ? "border border-red-500"
                                    : "border border-neutral-600"
                            }
                        />
                        <span className="text-sm text-neutral-200">
                            {hasHeadersState ? "Yes" : "No"}
                        </span>
                    </div>
                </div>




                {/* Display an error message if the user hasn't made a selection */}
                {hasHeadersState === null && errors.hasHeaders && (
                    <p className="text-red-500 text-sm">{errors.hasHeaders[0]}</p>
                )}

                {/* Separator input field */}
                <div className="flex flex-col gap-2">
                    <Label htmlFor="separator">Separator</Label>
                    <Input
                        type="text"
                        name="separator"
                        id="separator"
                        value={separatorState ?? ""}
                        onChange={handleSeparatorChange}
                        placeholder="e.g., ',' or ';'"
                        className="w-full"
                    />

                    {/* Displays an error message if there's a validation error for the separator input */}
                    {errors.separator && (
                        <p className="text-red-500 text-sm">{errors.separator[0]}</p>
                    )}
                </div>
            </div>
        </FormWrapper>
    );
};

export default Step1_FileUploadForm;
