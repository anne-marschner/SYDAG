"use client";
import { useState } from "react";
import * as ToggleGroup from "@radix-ui/react-toggle-group";
import FormWrapper from "./FormWrapper";
import { FormItems } from "@/components/types/formTypes";

// Defining the types for the props passed to the Step7_ShuffleForm component.
type StepProps = {
    dataset1ShuffleOption: "Shuffle Columns" | "Shuffle Rows" | "No Change" | null;
    dataset2ShuffleOption: "Shuffle Columns" | "Shuffle Rows" | "No Change" | null;
    updateForm: (fieldToUpdate: Partial<FormItems>) => void; // Function to update form fields.
    errors: Record<string, string[]>;
};

type ShuffleType = "Shuffle Columns" | "Shuffle Rows" | "No Change" | null;

// Main functional component for selecting shuffle options.
const Step7_ShuffleForm = ({
                               updateForm,
                               dataset1ShuffleOption,
                               dataset2ShuffleOption,
                               errors,
                           }: StepProps) => {
    // State management for the selected shuffle options.
    const [shuffleOptionSelected1, setShuffleOptionSelected1] = useState<ShuffleType | null>(
        dataset1ShuffleOption
    );
    const [shuffleOptionSelected2, setShuffleOptionSelected2] = useState<ShuffleType | null>(
        dataset2ShuffleOption
    );

    // Handler function for changing the selected shuffle option for Dataset 1.
    const handleValueChange1 = (selected: string | null) => {
        if (selected === "Shuffle Columns" || selected === "Shuffle Rows" || selected === "No Change") {
            setShuffleOptionSelected1(selected as ShuffleType);
            updateForm({ dataset1ShuffleOption: selected as ShuffleType });
        }
    };

    // Handler function for changing the selected shuffle option for Dataset 2.
    const handleValueChange2 = (selected: string | null) => {
        if (selected === "Shuffle Columns" || selected === "Shuffle Rows" || selected === "No Change") {
            setShuffleOptionSelected2(selected as ShuffleType);
            updateForm({ dataset2ShuffleOption: selected as ShuffleType });
        }
    };

    return (
        <FormWrapper
            title="Select Shuffle Options"
            description="Choose between 'Shuffle Columns', 'Shuffle Rows', and 'No Change' for each dataset."
        >
            <div className="flex flex-col md:flex-row md:gap-8 w-full">
                {/* Shuffle Options Toggle for Dataset 1 */}
                <div className="flex flex-col w-full">
                    <h3 className="text-lg text-white mb-2">Shuffle Options for Dataset 1</h3>
                    <ToggleGroup.Root
                        orientation="vertical"
                        className="flex flex-col gap-3 my-2 w-full"
                        type="single"
                        value={shuffleOptionSelected1 || undefined}
                        onValueChange={handleValueChange1}
                    >
                        <ToggleGroup.Item
                            value="Shuffle Columns"
                            className="border border-neutral-600 p-6 h-16 rounded-md data-[state=on]:border-green-500 data-[state=on]:bg-neutral-900 focus:border-green-500 outline-none hover:border-green-500 w-full"
                        >
                            Shuffle Columns
                        </ToggleGroup.Item>

                        <ToggleGroup.Item
                            value="Shuffle Rows"
                            className="border border-neutral-600 p-6 h-16 rounded-md data-[state=on]:border-green-500 data-[state=on]:bg-neutral-900 focus:border-green-500 outline-none hover:border-green-500 w-full"
                        >
                            Shuffle Rows
                        </ToggleGroup.Item>

                        <ToggleGroup.Item
                            value="No Change"
                            className="border border-neutral-600 p-6 h-16 rounded-md data-[state=on]:border-green-500 data-[state=on]:bg-neutral-900 focus:border-green-500 outline-none hover:border-green-500 w-full"
                        >
                            No Change
                        </ToggleGroup.Item>
                    </ToggleGroup.Root>
                    {/* Display validation error for Dataset 1 */}
                    {errors.dataset1ShuffleOption && (
                        <p className="text-red-500 text-sm mt-2">
                            {errors.dataset1ShuffleOption[0]}
                        </p>
                    )}
                </div>

                {/* Shuffle Options Toggle for Dataset 2 */}
                <div className="flex flex-col w-full">
                    <h3 className="text-lg text-white mb-2">Shuffle Options for Dataset 2</h3>
                    <ToggleGroup.Root
                        orientation="vertical"
                        className="flex flex-col gap-3 my-2 w-full"
                        type="single"
                        value={shuffleOptionSelected2 || undefined}
                        onValueChange={handleValueChange2}
                    >
                        <ToggleGroup.Item
                            value="Shuffle Columns"
                            className="border border-neutral-600 p-6 h-16 rounded-md data-[state=on]:border-green-500 data-[state=on]:bg-neutral-900 focus:border-green-500 outline-none hover:border-green-500 w-full"
                        >
                            Shuffle Columns
                        </ToggleGroup.Item>

                        <ToggleGroup.Item
                            value="Shuffle Rows"
                            className="border border-neutral-600 p-6 h-16 rounded-md data-[state=on]:border-green-500 data-[state=on]:bg-neutral-900 focus:border-green-500 outline-none hover:border-green-500 w-full"
                        >
                            Shuffle Rows
                        </ToggleGroup.Item>

                        <ToggleGroup.Item
                            value="No Change"
                            className="border border-neutral-600 p-6 h-16 rounded-md data-[state=on]:border-green-500 data-[state=on]:bg-neutral-900 focus:border-green-500 outline-none hover:border-green-500 w-full"
                        >
                            No Change
                        </ToggleGroup.Item>
                    </ToggleGroup.Root>
                    {/* Display validation error for Dataset 2 */}
                    {errors.dataset2ShuffleOption && (
                        <p className="text-red-500 text-sm mt-2">
                            {errors.dataset2ShuffleOption[0]}
                        </p>
                    )}
                </div>
            </div>
        </FormWrapper>
    );
};

export default Step7_ShuffleForm;
