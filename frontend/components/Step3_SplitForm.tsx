"use client";
import React, { useState } from "react";
import Image from "next/image";
import * as ToggleGroup from "@radix-ui/react-toggle-group";
import FormWrapper from "./FormWrapper";
import { Slider } from "@/components/ui/slider"; // Assuming you're using the same Slider component
import { FormItems } from "@/components/types/formTypes";
import vertical from "../public/assets/vertical.svg";
import horizontal from "../public/assets/horizontal.svg";

// Defining the types for the props passed to the Step3_SplitForm component.
type StepProps = {
    splitType: "Horizontal" | "Vertical" | null;
    rowOverlapPercentage: number | null;
    columnOverlapPercentage: number | null;
    updateForm: (fieldToUpdate: Partial<FormItems>) => void;
    errors: Record<string, string[]>;
};

// Type definition for the available split options.
type SplitType = "Vertical" | "Horizontal";

// Main functional component that represents the form where the user selects the split type.
const Step3_SplitForm = ({ updateForm, splitType, rowOverlapPercentage, columnOverlapPercentage, errors }: StepProps) => {
    // State management for the selected split type.
    const [planSelected, setPlanSelected] = useState<SplitType | null>(splitType);
    const [rowOverlapValue, setRowOverlapValue] = useState<number | null>(rowOverlapPercentage);
    const [columnOverlapValue, setColumnOverlapValue] = useState<number | null>(columnOverlapPercentage);

    // Handler function for changing the selected split type.
    const handleValueChange = (selectedSplitType: SplitType) => {
        if (selectedSplitType) {
            setPlanSelected(selectedSplitType); // Update the selected plan in the local state.
            updateForm({ splitType: selectedSplitType }); // Update the form state with the new selected plan.
        }
    };

    // Handlers for row and column overlap sliders
    const handleRowOverlapChange = (value: number[]) => {
        const newValue = value[0];
        setRowOverlapValue(newValue);
        updateForm({ rowOverlapPercentage: newValue });
    };

    const handleColumnOverlapChange = (value: number[]) => {
        const newValue = value[0];
        setColumnOverlapValue(newValue);
        updateForm({ columnOverlapPercentage: newValue });
    };

    return (
        <FormWrapper
            title="Select Split Type"
            description="You have the option of horizontal or vertical split"
        >
            <ToggleGroup.Root
                orientation="horizontal"
                className="flex flex-col gap-3 my-2 md:flex-row md:items-center md:justify-between md:gap-8"
                type="single"
                value={planSelected || undefined}
                onValueChange={handleValueChange}
            >
                {/* Toggle button for selecting the "Horizontal" option */}
                <ToggleGroup.Item
                    value="Horizontal"
                    className="border border-neutral-600 flex items-start gap-3 p-3 h-24 rounded-md data-[state=on]:border-[#77f6aa] data-[state=on]:bg-neutral-900 focus:border-[#77f6aa] outline-none hover:border-[#77f6aa] md:h-44 md:w-1/2 md:flex-col md:justify-between md:gap-0"
                >
                    <Image src={horizontal} alt="Horizontal" width="40" height="40" />
                    <div className="relative -top-1 flex flex-col items-start md:top-0">
                        <p className="text-white font-semibold">Horizontal</p>
                        <p className="text-sm">Split</p>
                    </div>
                </ToggleGroup.Item>

                {/* Toggle button for selecting the "Vertical" option */}
                <ToggleGroup.Item
                    value="Vertical"
                    className="border border-neutral-600 flex items-start gap-3 p-3 h-24 rounded-md data-[state=on]:border-[#77f6aa] data-[state=on]:bg-neutral-900 focus:border-[#77f6aa] outline-none hover:border-[#77f6aa] md:h-44 md:w-1/2 md:flex-col md:justify-between md:gap-0"
                >
                    <Image src={vertical} alt="Vertical" width="40" height="40" />
                    <div className="relative -top-1 flex flex-col items-start md:top-0">
                        <p className="text-white font-semibold">Vertical</p>
                        <p className="text-sm">Split</p>
                    </div>
                </ToggleGroup.Item>
            </ToggleGroup.Root>

            {errors.splitType && (
                <p className="text-red-500 text-sm mt-2">{errors.splitType[0]}</p>
            )}


            {/* Conditionally render Row Overlap Slider if Horizontal is selected */}
            {planSelected === "Horizontal" && (
                <div className="flex flex-col w-full mt-2">
                    <h3 className="text-lg text-white ">Row Overlap</h3>
                    <Slider
                        className="my-4 w-full"
                        value={[rowOverlapValue ?? 0]}
                        onValueChange={handleRowOverlapChange}
                        min={0}
                        max={100}
                        step={1}
                    />
                    {/* Display the current value of the slider */}
                    <span className="text-white text-sm ">
                        Row Overlap Procentage: {rowOverlapValue ?? 0} %
                    </span>
                    {errors.rowOverLab && (
                        <p className="text-red-500 text-sm">{errors.rowOverLab[0]}</p>
                    )}
                </div>
            )}


            {/* Conditionally render Column Overlap Slider if Vertical is selected */}
            {planSelected === "Vertical" && (
                <div className="flex flex-col w-full mt-2">
                    <h3 className="text-lg text-white">Column Overlap</h3>
                    <Slider
                        className="my-4 w-full"
                        value={[columnOverlapValue ?? 0]}
                        onValueChange={handleColumnOverlapChange}
                        min={0}
                        max={100}
                        step={1}
                    />
                    {/* Display the current value of the slider */}
                    <span className="text-white text-sm">
                     Column Overlap Procentage: {columnOverlapValue ?? 0} %
                    </span>
                    {errors.columnOverLab && (
                        <p className="text-red-500 text-sm">{errors.columnOverLab[0]}</p>
                    )}
                </div>
            )}

        </FormWrapper>
    );
};

export default Step3_SplitForm;
