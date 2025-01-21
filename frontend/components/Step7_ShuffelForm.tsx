"use client";
import React, { useState, useCallback, useEffect } from "react";
import * as ToggleGroup from "@radix-ui/react-toggle-group";
import FormWrapper from "./FormWrapper";
import { FormItems } from "@/components/types/formTypes";

type StepProps = {
    splitType: "Horizontal" | "Vertical" | "VerticalHorizontal" | null; 

    dataset1ShuffleOption: "Shuffle Columns" | "Shuffle Rows" | "No Change" | null;
    dataset2ShuffleOption: "Shuffle Columns" | "Shuffle Rows" | "No Change" | null;
    dataset3ShuffleOption: "Shuffle Columns" | "Shuffle Rows" | "No Change" | null;
    dataset4ShuffleOption: "Shuffle Columns" | "Shuffle Rows" | "No Change" | null;

    updateForm: (fieldToUpdate: Partial<FormItems>) => void;
    errors: Record<string, string[]>;
};

type ShuffleType = "Shuffle Columns" | "Shuffle Rows" | "No Change" | null;

// Main functional component for selecting shuffle options
const Step7_ShuffleForm = ({
                               splitType,
                               updateForm,
                               dataset1ShuffleOption,
                               dataset2ShuffleOption,
                               dataset3ShuffleOption,
                               dataset4ShuffleOption,
                               errors,
                           }: StepProps) => {
    // State management for the selected shuffle options
    const [shuffleOptionSelected1, setShuffleOptionSelected1] = useState<ShuffleType>(
        dataset1ShuffleOption
    );
    const [shuffleOptionSelected2, setShuffleOptionSelected2] = useState<ShuffleType>(
        dataset2ShuffleOption
    );
    const [shuffleOptionSelected3, setShuffleOptionSelected3] = useState<ShuffleType>(
        dataset3ShuffleOption
    );
    const [shuffleOptionSelected4, setShuffleOptionSelected4] = useState<ShuffleType>(
        dataset4ShuffleOption
    );

    /**
     * Handler functions for changing the selected shuffle option.
     */
    const handleValueChange1 = useCallback(
        (selected: string | null) => {
            if (
                selected === "Shuffle Columns" ||
                selected === "Shuffle Rows" ||
                selected === "No Change"
            ) {
                setShuffleOptionSelected1(selected as ShuffleType);
                updateForm({ dataset1ShuffleOption: selected as ShuffleType });
            }
        },
        [updateForm]
    );

    const handleValueChange2 = useCallback(
        (selected: string | null) => {
            if (
                selected === "Shuffle Columns" ||
                selected === "Shuffle Rows" ||
                selected === "No Change"
            ) {
                setShuffleOptionSelected2(selected as ShuffleType);
                updateForm({ dataset2ShuffleOption: selected as ShuffleType });
            }
        },
        [updateForm]
    );

    const handleValueChange3 = useCallback(
        (selected: string | null) => {
            if (
                selected === "Shuffle Columns" ||
                selected === "Shuffle Rows" ||
                selected === "No Change"
            ) {
                setShuffleOptionSelected3(selected as ShuffleType);
                updateForm({ dataset3ShuffleOption: selected as ShuffleType });
            }
        },
        [updateForm]
    );

    const handleValueChange4 = useCallback(
        (selected: string | null) => {
            if (
                selected === "Shuffle Columns" ||
                selected === "Shuffle Rows" ||
                selected === "No Change"
            ) {
                setShuffleOptionSelected4(selected as ShuffleType);
                updateForm({ dataset4ShuffleOption: selected as ShuffleType });
            }
        },
        [updateForm]
    );

    /**
     * Effect to reset Datasets 3 and 4 when splitType changes away from "VerticalHorizontal".
     */
    useEffect(() => {
        if (splitType !== "VerticalHorizontal") {
            // Reset Dataset 3
            setShuffleOptionSelected3(null);
            updateForm({
                dataset3ShuffleOption: null,
            });

            // Reset Dataset 4
            setShuffleOptionSelected4(null);
            updateForm({
                dataset4ShuffleOption: null,
            });
        }
    }, [splitType, updateForm]);

    return (
        <FormWrapper
            title="Select Shuffle Options"
            description="Choose between 'Shuffle Columns', 'Shuffle Rows', and 'No Change' for each dataset."
        >
            <div className="flex flex-col w-full h-[750px] max-h-[35vh] p-4 scrollbar-custom">
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
                                className="custom-label border border-neutral-600 p-6 h-16 rounded-md data-[state=on]:border-green-500 data-[state=on]:bg-neutral-900 focus:border-green-500 outline-none hover:border-green-500 w-full"
                            >
                                Shuffle Columns
                            </ToggleGroup.Item>

                            <ToggleGroup.Item
                                value="Shuffle Rows"
                                className="custom-label border border-neutral-600 p-6 h-16 rounded-md data-[state=on]:border-green-500 data-[state=on]:bg-neutral-900 focus:border-green-500 outline-none hover:border-green-500 w-full"
                            >
                                Shuffle Rows
                            </ToggleGroup.Item>

                            <ToggleGroup.Item
                                value="No Change"
                                className="custom-label border border-neutral-600 p-6 h-16 rounded-md data-[state=on]:border-green-500 data-[state=on]:bg-neutral-900 focus:border-green-500 outline-none hover:border-green-500 w-full"
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
                                className="custom-label border border-neutral-600 p-6 h-16 rounded-md data-[state=on]:border-green-500 data-[state=on]:bg-neutral-900 focus:border-green-500 outline-none hover:border-green-500 w-full"
                            >
                                Shuffle Columns
                            </ToggleGroup.Item>

                            <ToggleGroup.Item
                                value="Shuffle Rows"
                                className="custom-label border border-neutral-600 p-6 h-16 rounded-md data-[state=on]:border-green-500 data-[state=on]:bg-neutral-900 focus:border-green-500 outline-none hover:border-green-500 w-full"
                            >
                                Shuffle Rows
                            </ToggleGroup.Item>

                            <ToggleGroup.Item
                                value="No Change"
                                className="custom-label border border-neutral-600 p-6 h-16 rounded-md data-[state=on]:border-green-500 data-[state=on]:bg-neutral-900 focus:border-green-500 outline-none hover:border-green-500 w-full"
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

                {/* Conditionally Render Dataset 3 and Dataset 4 Controls */}
                {splitType === "VerticalHorizontal" && (
                    <div className="flex flex-col md:flex-row md:gap-8 w-full mt-8">
                        {/* Shuffle Options Toggle for Dataset 3 */}
                        <div className="flex flex-col w-full">
                            <h3 className="text-lg text-white mb-2">Shuffle Options for Dataset 3</h3>
                            <ToggleGroup.Root
                                orientation="vertical"
                                className="flex flex-col gap-3 my-2 w-full"
                                type="single"
                                value={shuffleOptionSelected3 || undefined}
                                onValueChange={handleValueChange3}
                            >
                                <ToggleGroup.Item
                                    value="Shuffle Columns"
                                    className="custom-label border border-neutral-600 p-6 h-16 rounded-md data-[state=on]:border-green-500 data-[state=on]:bg-neutral-900 focus:border-green-500 outline-none hover:border-green-500 w-full"
                                >
                                    Shuffle Columns
                                </ToggleGroup.Item>

                                <ToggleGroup.Item
                                    value="Shuffle Rows"
                                    className="custom-label border border-neutral-600 p-6 h-16 rounded-md data-[state=on]:border-green-500 data-[state=on]:bg-neutral-900 focus:border-green-500 outline-none hover:border-green-500 w-full"
                                >
                                    Shuffle Rows
                                </ToggleGroup.Item>

                                <ToggleGroup.Item
                                    value="No Change"
                                    className="custom-label border border-neutral-600 p-6 h-16 rounded-md data-[state=on]:border-green-500 data-[state=on]:bg-neutral-900 focus:border-green-500 outline-none hover:border-green-500 w-full"
                                >
                                    No Change
                                </ToggleGroup.Item>
                            </ToggleGroup.Root>
                            {/* Display validation error for Dataset 3 */}
                            {errors.dataset3ShuffleOption && (
                                <p className="text-red-500 text-sm mt-2">
                                    {errors.dataset3ShuffleOption[0]}
                                </p>
                            )}
                        </div>

                        {/* Shuffle Options Toggle for Dataset 4 */}
                        <div className="flex flex-col w-full">
                            <h3 className="text-lg text-white mb-2">Shuffle Options for Dataset 4</h3>
                            <ToggleGroup.Root
                                orientation="vertical"
                                className="flex flex-col gap-3 my-2 w-full"
                                type="single"
                                value={shuffleOptionSelected4 || undefined}
                                onValueChange={handleValueChange4}
                            >
                                <ToggleGroup.Item
                                    value="Shuffle Columns"
                                    className="custom-label border border-neutral-600 p-6 h-16 rounded-md data-[state=on]:border-green-500 data-[state=on]:bg-neutral-900 focus:border-green-500 outline-none hover:border-green-500 w-full"
                                >
                                    Shuffle Columns
                                </ToggleGroup.Item>

                                <ToggleGroup.Item
                                    value="Shuffle Rows"
                                    className="custom-label border border-neutral-600 p-6 h-16 rounded-md data-[state=on]:border-green-500 data-[state=on]:bg-neutral-900 focus:border-green-500 outline-none hover:border-green-500 w-full"
                                >
                                    Shuffle Rows
                                </ToggleGroup.Item>

                                <ToggleGroup.Item
                                    value="No Change"
                                    className="custom-label border border-neutral-600 p-6 h-16 rounded-md data-[state=on]:border-green-500 data-[state=on]:bg-neutral-900 focus:border-green-500 outline-none hover:border-green-500 w-full"
                                >
                                    No Change
                                </ToggleGroup.Item>
                            </ToggleGroup.Root>
                            {/* Display validation error for Dataset 4 */}
                            {errors.dataset4ShuffleOption && (
                                <p className="text-red-500 text-sm mt-2">
                                    {errors.dataset4ShuffleOption[0]}
                                </p>
                            )}
                        </div>
                    </div>
                )}
            </div>
        </FormWrapper>
);
};

export default React.memo(Step7_ShuffleForm);
