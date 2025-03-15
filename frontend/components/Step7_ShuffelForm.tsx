"use client";
import React, { useState, useCallback, useEffect } from "react";
import * as ToggleGroup from "@radix-ui/react-toggle-group";
import FormWrapper from "./FormWrapper";
import { FormItems } from "@/components/types/formTypes";

// Define the types for the props 
type StepProps = {
    splitType: "Horizontal" | "Vertical" | "VerticalHorizontal" | null;

    datasetAShuffleOption: "Shuffle Columns" | "Shuffle Rows" | "No Change" | null;
    datasetBShuffleOption: "Shuffle Columns" | "Shuffle Rows" | "No Change" | null;
    datasetCShuffleOption: "Shuffle Columns" | "Shuffle Rows" | "No Change" | null;
    datasetDShuffleOption: "Shuffle Columns" | "Shuffle Rows" | "No Change" | null;

    updateForm: (fieldToUpdate: Partial<FormItems>) => void;
    errors: Record<string, string[]>;
};

type ShuffleType = "Shuffle Columns" | "Shuffle Rows" | "No Change" | null;

const Step7_ShuffleForm = ({
                               splitType,
                               updateForm,
                               datasetAShuffleOption,
                               datasetBShuffleOption,
                               datasetCShuffleOption,
                               datasetDShuffleOption,
                               errors,
                           }: StepProps) => {
    // States of datasets shuffle options
    const [shuffleOptionSelected1, setShuffleOptionSelected1] = useState<ShuffleType>(
        datasetAShuffleOption
    );
    const [shuffleOptionSelected2, setShuffleOptionSelected2] = useState<ShuffleType>(
        datasetBShuffleOption
    );
    const [shuffleOptionSelected3, setShuffleOptionSelected3] = useState<ShuffleType>(
        datasetCShuffleOption
    );
    const [shuffleOptionSelected4, setShuffleOptionSelected4] = useState<ShuffleType>(
        datasetDShuffleOption
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
                updateForm({ datasetAShuffleOption: selected as ShuffleType });
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
                updateForm({ datasetBShuffleOption: selected as ShuffleType });
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
                updateForm({ datasetCShuffleOption: selected as ShuffleType });
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
                updateForm({ datasetDShuffleOption: selected as ShuffleType });
            }
        },
        [updateForm]
    );

    /**
     * Effect to reset Datasets C and D when splitType changes away from "VerticalHorizontal".
     */
    useEffect(() => {
        if (splitType !== "VerticalHorizontal") {
            // Reset Dataset C
            setShuffleOptionSelected3(null);
            updateForm({
                datasetCShuffleOption: null,
            });

            // Reset Dataset D
            setShuffleOptionSelected4(null);
            updateForm({
                datasetDShuffleOption: null,
            });
        }
    }, [splitType, updateForm]);

    return (
        <FormWrapper
            title="Select Shuffle Options"
            description="Choose between 'Shuffle Columns', 'Shuffle Rows', and 'No Change' for each dataset."
        >
            <div className="max-h-[600px] overflow-y-auto scrollbar-custom p-4 space-y-6">
                <div className="flex flex-col w-full h-[750px] max-h-[60vh] p-4 scrollbar-custom">
                    <div className="flex flex-col md:flex-row md:gap-8 w-full">
                        {/* Shuffle Options Toggle for Dataset A */}
                        <div className="flex flex-col w-full">
                            <h3 className="text-lg text-white mb-2">Dataset A</h3>
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
                            {/* Display validation error for Dataset A */}
                            {errors.datasetAShuffleOption && (
                                <p className="text-red-500 text-sm mt-2">
                                    {errors.datasetAShuffleOption[0]}
                                </p>
                            )}
                        </div>

                        {/* Shuffle Options Toggle for Dataset B */}
                        <div className="flex flex-col w-full">
                            <h3 className="text-lg text-white mb-2">Dataset B</h3>
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
                            {/* Display validation error for Dataset B */}
                            {errors.datasetBShuffleOption && (
                                <p className="text-red-500 text-sm mt-2">
                                    {errors.datasetBShuffleOption[0]}
                                </p>
                            )}
                        </div>
                    </div>

                    {/* Conditionally Render Dataset C and Dataset D Controls */}
                    {splitType === "VerticalHorizontal" && (
                        <div className="flex flex-col md:flex-row md:gap-8 w-full mt-8">
                            {/* Shuffle Options Toggle for Dataset C */}
                            <div className="flex flex-col w-full">
                                <h3 className="text-lg text-white mb-2">Dataset C</h3>
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
                                {/* Display validation error for Dataset C */}
                                {errors.datasetCShuffleOption && (
                                    <p className="text-red-500 text-sm mt-2">
                                        {errors.datasetCShuffleOption[0]}
                                    </p>
                                )}
                            </div>

                            {/* Shuffle Options Toggle for Dataset D */}
                            <div className="flex flex-col w-full">
                                <h3 className="text-lg text-white mb-2">Dataset D</h3>
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
                                {/* Display validation error for Dataset D */}
                                {errors.datasetDShuffleOption && (
                                    <p className="text-red-500 text-sm mt-2">
                                        {errors.datasetDShuffleOption[0]}
                                    </p>
                                )}
                            </div>
                        </div>
                    )}
                </div>
            </div>
        </FormWrapper>
    );
};

export default React.memo(Step7_ShuffleForm);
