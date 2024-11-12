"use client";
import { useState } from "react";
import * as ToggleGroup from "@radix-ui/react-toggle-group";
import FormWrapper from "./FormWrapper";
import { FormItems } from "@/components/types/formTypes";

// Defining the types for the props passed to the Step4_NormalizationForm component.
type StepProps = {
    dataset1StructureType: "BCNF" | "Join Columns" | "No Change" | null;
    dataset2StructureType: "BCNF" | "Join Columns" | "No Change" | null;
    updateForm: (fieldToUpdate: Partial<FormItems>) => void;
    errors: Record<string, string[]>;
};

// Type definition for the available normalization options.
type StructureType = "BCNF" | "Join Columns" | "No Change";

// Main functional component that represents the form where the user selects between BCNF and No Change options.
const Step4_StructureForm = ({
    updateForm,
    dataset1StructureType,
    dataset2StructureType,
    errors,
}: StepProps) => {
    // State management for the selected normalization option for each dataset.
    const [planSelected1, setPlanSelected1] = useState<StructureType | null>(
        dataset1StructureType
    );
    const [planSelected2, setPlanSelected2] = useState<StructureType | null>(
        dataset2StructureType
    );

    // Handler function for changing the selected option in the first toggle group (Dataset 1).
    const handleValueChange1 = (selected: string | null) => {
        if (selected === "BCNF" || selected === "Join Columns" || selected === "No Change") {
            setPlanSelected1(selected as StructureType);
            updateForm({ dataset1StructureType: selected as StructureType });
        }
    };

    // Handler function for changing the selected option in the second toggle group (Dataset 2).
    const handleValueChange2 = (selected: string | null) => {
        if (selected === "BCNF" || selected === "Join Columns" || selected === "No Change") {
            setPlanSelected2(selected as StructureType);
            updateForm({ dataset2StructureType: selected as StructureType });
        }
    };

    return (
        <FormWrapper
            title="Select Structure Options"
            description="Choose between BCNF and No Change for each dataset"
        >
            <div className="flex flex-col md:flex-row md:gap-8 w-full">
                {/* First Toggle Group for Dataset 1 */}
                <div className="flex flex-col w-full">
                    <h3 className="text-lg text-white mb-2">Dataset 1</h3>
                    <ToggleGroup.Root
                        orientation="vertical"
                        className="flex flex-col gap-3 my-2 w-full"
                        type="single"
                        value={planSelected1 || undefined}
                        onValueChange={handleValueChange1}
                    >
                        <ToggleGroup.Item
                            value="BCNF"
                            className="border border-neutral-600 p-6 h-16 rounded-md data-[state=on]:border-green-500 data-[state=on]:bg-neutral-900 focus:border-green-500 outline-none hover:border-green-500 w-full"
                        >
                            BCNF
                        </ToggleGroup.Item>

                        <ToggleGroup.Item
                            value="Join Columns"
                            className="border border-neutral-600 p-6 h-16 rounded-md data-[state=on]:border-green-500 data-[state=on]:bg-neutral-900 focus:border-green-500 outline-none hover:border-green-500 w-full"
                        >
                            Join Columns
                        </ToggleGroup.Item>

                        <ToggleGroup.Item
                            value="No Change"
                            className="border border-neutral-600 p-6 h-16 rounded-md data-[state=on]:border-green-500 data-[state=on]:bg-neutral-900 focus:border-green-500 outline-none hover:border-green-500 w-full"
                        >
                            No Change
                        </ToggleGroup.Item>
                    </ToggleGroup.Root>
                    {/* Display validation error for Dataset 1 */}
                    {errors.dataset1StructureType && (
                        <p className="text-red-500 text-sm mt-2">
                            {errors.dataset1StructureType[0]}
                        </p>
                    )}
                </div>

                {/* Second Toggle Group for Dataset 2 */}
                <div className="flex flex-col w-full">
                    <h3 className="text-lg text-white mb-2">Dataset 2</h3>
                    <ToggleGroup.Root
                        orientation="vertical"
                        className="flex flex-col gap-3 my-2 w-full"
                        type="single"
                        value={planSelected2 || undefined}
                        onValueChange={handleValueChange2}
                    >
                        <ToggleGroup.Item
                            value="BCNF"
                            className="border border-neutral-600 p-6 h-16 rounded-md data-[state=on]:border-green-500 data-[state=on]:bg-neutral-900 focus:border-green-500 outline-none hover:border-green-500 w-full"
                        >
                            BCNF
                        </ToggleGroup.Item>

                        <ToggleGroup.Item
                            value="Join Columns"
                            className="border border-neutral-600 p-6 h-16 rounded-md data-[state=on]:border-green-500 data-[state=on]:bg-neutral-900 focus:border-green-500 outline-none hover:border-green-500 w-full"
                        >
                            Join Columns
                        </ToggleGroup.Item>

                        <ToggleGroup.Item
                            value="No Change"
                            className="border border-neutral-600 p-6 h-16 rounded-md data-[state=on]:border-green-500 data-[state=on]:bg-neutral-900 focus:border-green-500 outline-none hover:border-green-500 w-full"
                        >
                            No Change
                        </ToggleGroup.Item>
                    </ToggleGroup.Root>
                    {/* Display validation error for Dataset 2 */}
                    {errors.dataset2StructureType && (
                        <p className="text-red-500 text-sm mt-2">
                            {errors.dataset2StructureType[0]}
                        </p>
                    )}
                </div>
            </div>
        </FormWrapper>
    );
};


    export default Step4_StructureForm;
