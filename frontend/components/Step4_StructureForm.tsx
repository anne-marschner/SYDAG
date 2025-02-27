"use client";
import React, { useState, useEffect, useCallback } from "react";
import * as ToggleGroup from "@radix-ui/react-toggle-group";
import FormWrapper from "./FormWrapper";
import { FormItems } from "@/components/types/formTypes";
import { Slider } from "@/components/ui/slider";

type StepProps = {
    splitType: "Horizontal" | "Vertical" | "VerticalHorizontal" | null;

    dataset1StructureType: "BCNF" | "Join Columns" | "No Change" | null;
    dataset2StructureType: "BCNF" | "Join Columns" | "No Change" | null;
    dataset3StructureType: "BCNF" | "Join Columns" | "No Change" | null;
    dataset4StructureType: "BCNF" | "Join Columns" | "No Change" | null;

    // Slider values for Dataset 1
    dataset1BCNFSliderValue: number | null;
    dataset1JoinColumnsSliderValue: number | null;

    // Slider values for Dataset 2
    dataset2BCNFSliderValue: number | null;
    dataset2JoinColumnsSliderValue: number | null;

    // Slider values for Dataset 3
    dataset3BCNFSliderValue: number | null;
    dataset3JoinColumnsSliderValue: number | null;

    // Slider values for Dataset 4
    dataset4BCNFSliderValue: number | null;
    dataset4JoinColumnsSliderValue: number | null;
    updateForm: (fieldToUpdate: Partial<FormItems>) => void;
    errors: Record<string, string[]>;
};

type StructureType = "BCNF" | "Join Columns" | "No Change";

const Step4_StructureForm = ({
                                 splitType,
                                 updateForm,
                                 dataset1StructureType,
                                 dataset2StructureType,
                                 dataset3StructureType,
                                 dataset4StructureType,
                                 dataset1BCNFSliderValue,
                                 dataset1JoinColumnsSliderValue,
                                 dataset2BCNFSliderValue,
                                 dataset2JoinColumnsSliderValue,
                                 dataset3BCNFSliderValue,
                                 dataset3JoinColumnsSliderValue,
                                 dataset4BCNFSliderValue,
                                 dataset4JoinColumnsSliderValue,
                                 errors,
                             }: StepProps) => {
    // State for selected structure type per dataset
    const [planSelected1, setPlanSelected1] = useState<StructureType | null>(dataset1StructureType);
    const [planSelected2, setPlanSelected2] = useState<StructureType | null>(dataset2StructureType);
    const [planSelected3, setPlanSelected3] = useState<StructureType | null>(dataset3StructureType);
    const [planSelected4, setPlanSelected4] = useState<StructureType | null>(dataset4StructureType);

    // State for sliders
    const [dataset1BCNFValue, setDataset1BCNFValue] = useState<number>(dataset1BCNFSliderValue || 0);
    const [dataset1JoinColumnsValue, setDataset1JoinColumnsValue] = useState<number>(
        dataset1JoinColumnsSliderValue || 0
    );

    const [dataset2BCNFValue, setDataset2BCNFValue] = useState<number>(dataset2BCNFSliderValue || 0);
    const [dataset2JoinColumnsValue, setDataset2JoinColumnsValue] = useState<number>(
        dataset2JoinColumnsSliderValue || 0
    );

    const [dataset3BCNFValue, setDataset3BCNFValue] = useState<number>(dataset3BCNFSliderValue || 0);
    const [dataset3JoinColumnsValue, setDataset3JoinColumnsValue] = useState<number>(
        dataset3JoinColumnsSliderValue || 0
    );

    const [dataset4BCNFValue, setDataset4BCNFValue] = useState<number>(dataset4BCNFSliderValue || 0);
    const [dataset4JoinColumnsValue, setDataset4JoinColumnsValue] = useState<number>(
        dataset4JoinColumnsSliderValue || 0
    );

    /**
     * Handlers for structure type toggles
     */
    const handleValueChange1 = useCallback(
        (selected: string | null) => {
            if (selected === "BCNF" || selected === "Join Columns" || selected === "No Change") {
                setPlanSelected1(selected as StructureType);
                updateForm({ dataset1StructureType: selected as StructureType });

                // Reset sliders if not relevant
                if (selected !== "BCNF") {
                    setDataset1BCNFValue(0);
                    updateForm({ dataset1BCNFSliderValue: 0 });
                }
                if (selected !== "Join Columns") {
                    setDataset1JoinColumnsValue(0);
                    updateForm({ dataset1JoinColumnsSliderValue: 0 });
                }
            }
        },
        [updateForm]
    );

    const handleValueChange2 = useCallback(
        (selected: string | null) => {
            if (selected === "BCNF" || selected === "Join Columns" || selected === "No Change") {
                setPlanSelected2(selected as StructureType);
                updateForm({ dataset2StructureType: selected as StructureType });

                if (selected !== "BCNF") {
                    setDataset2BCNFValue(0);
                    updateForm({ dataset2BCNFSliderValue: 0 });
                }
                if (selected !== "Join Columns") {
                    setDataset2JoinColumnsValue(0);
                    updateForm({ dataset2JoinColumnsSliderValue: 0 });
                }
            }
        },
        [updateForm]
    );

    const handleValueChange3 = useCallback(
        (selected: string | null) => {
            if (selected === "BCNF" || selected === "Join Columns" || selected === "No Change") {
                setPlanSelected3(selected as StructureType);
                updateForm({ dataset3StructureType: selected as StructureType });

                if (selected !== "BCNF") {
                    setDataset3BCNFValue(0);
                    updateForm({ dataset3BCNFSliderValue: 0 });
                }
                if (selected !== "Join Columns") {
                    setDataset3JoinColumnsValue(0);
                    updateForm({ dataset3JoinColumnsSliderValue: 0 });
                }
            }
        },
        [updateForm]
    );

    const handleValueChange4 = useCallback(
        (selected: string | null) => {
            if (selected === "BCNF" || selected === "Join Columns" || selected === "No Change") {
                setPlanSelected4(selected as StructureType);
                updateForm({ dataset4StructureType: selected as StructureType });

                if (selected !== "BCNF") {
                    setDataset4BCNFValue(0);
                    updateForm({ dataset4BCNFSliderValue: 0 });
                }
                if (selected !== "Join Columns") {
                    setDataset4JoinColumnsValue(0);
                    updateForm({ dataset4JoinColumnsSliderValue: 0 });
                }
            }
        },
        [updateForm]
    );

    /**
     * Slider handlers
     */
    const handleDataset1BCNFSliderChange = useCallback(
        (value: number[]) => {
            const newValue = value[0];
            setDataset1BCNFValue(newValue);
            updateForm({ dataset1BCNFSliderValue: newValue });
        },
        [updateForm]
    );

    const handleDataset1JoinColumnsSliderChange = useCallback(
        (value: number[]) => {
            const newValue = value[0];
            setDataset1JoinColumnsValue(newValue);
            updateForm({ dataset1JoinColumnsSliderValue: newValue });
        },
        [updateForm]
    );

    const handleDataset2BCNFSliderChange = useCallback(
        (value: number[]) => {
            const newValue = value[0];
            setDataset2BCNFValue(newValue);
            updateForm({ dataset2BCNFSliderValue: newValue });
        },
        [updateForm]
    );

    const handleDataset2JoinColumnsSliderChange = useCallback(
        (value: number[]) => {
            const newValue = value[0];
            setDataset2JoinColumnsValue(newValue);
            updateForm({ dataset2JoinColumnsSliderValue: newValue });
        },
        [updateForm]
    );

    const handleDataset3BCNFSliderChange = useCallback(
        (value: number[]) => {
            const newValue = value[0];
            setDataset3BCNFValue(newValue);
            updateForm({ dataset3BCNFSliderValue: newValue });
        },
        [updateForm]
    );

    const handleDataset3JoinColumnsSliderChange = useCallback(
        (value: number[]) => {
            const newValue = value[0];
            setDataset3JoinColumnsValue(newValue);
            updateForm({ dataset3JoinColumnsSliderValue: newValue });
        },
        [updateForm]
    );

    const handleDataset4BCNFSliderChange = useCallback(
        (value: number[]) => {
            const newValue = value[0];
            setDataset4BCNFValue(newValue);
            updateForm({ dataset4BCNFSliderValue: newValue });
        },
        [updateForm]
    );

    const handleDataset4JoinColumnsSliderChange = useCallback(
        (value: number[]) => {
            const newValue = value[0];
            setDataset4JoinColumnsValue(newValue);
            updateForm({ dataset4JoinColumnsSliderValue: newValue });
        },
        [updateForm]
    );

    /**
     * Reset Datasets 3 and 4 if splitType != "VerticalHorizontal"
     */
    useEffect(() => {
        if (splitType !== "VerticalHorizontal") {
            setPlanSelected3(null);
            setDataset3BCNFValue(0);
            setDataset3JoinColumnsValue(0);
            updateForm({
                dataset3StructureType: null,
                dataset3BCNFSliderValue: 0,
                dataset3JoinColumnsSliderValue: 0,
            });

            setPlanSelected4(null);
            setDataset4BCNFValue(0);
            setDataset4JoinColumnsValue(0);
            updateForm({
                dataset4StructureType: null,
                dataset4BCNFSliderValue: 0,
                dataset4JoinColumnsSliderValue: 0,
            });
        }
    }, [splitType, updateForm]);

    /**
     * 1. Count how many sliders are visible
     *    Each dataset can have either BCNF or Join Columns => 1 slider
     *    If it's "No Change", 0 sliders
     * 2. Multiply that count by 100 for the container height
     */
    let visibleSlidersCount = 0;

    if (planSelected1 === "BCNF" || planSelected1 === "Join Columns") {
        visibleSlidersCount++;
    }
    if (planSelected2 === "BCNF" || planSelected2 === "Join Columns") {
        visibleSlidersCount++;
    }
    if (splitType === "VerticalHorizontal") {
        if (planSelected3 === "BCNF" || planSelected3 === "Join Columns") {
            visibleSlidersCount++;
        }
        if (planSelected4 === "BCNF" || planSelected4 === "Join Columns") {
            visibleSlidersCount++;
        }
    }

    const sliderContainerHeight = visibleSlidersCount * 130;

    return (
        <FormWrapper
            title="Select Structure Options"
            description="Choose between 'No Change', 'BCNF' and 'Join Columns' for each dataset."
        >
            {/* Outer scrollable container for everything */}
            <div className="max-h-[600px] overflow-y-auto scrollbar-custom p-4 space-y-6">
                {/* Main Flex Container for Dataset Controls */}
                <div className="flex flex-col md:flex-row md:gap-8 w-full">
                    {/* Dataset 1 Controls */}
                    <div className="flex flex-col w-full md:w-1/2 lg:w-1/2 mb-4">
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
                                className="custom-label border border-neutral-600 p-4 h-16 rounded-md data-[state=on]:border-green-500 data-[state=on]:bg-neutral-900 focus:border-green-500 outline-none hover:border-green-500 w-full"
                            >
                                Normalize
                            </ToggleGroup.Item>

                            <ToggleGroup.Item
                                value="Join Columns"
                                className="custom-label border border-neutral-600 p-4 h-16 rounded-md data-[state=on]:border-green-500 data-[state=on]:bg-neutral-900 focus:border-green-500 outline-none hover:border-green-500 w-full"
                            >
                                Join Columns
                            </ToggleGroup.Item>

                            <ToggleGroup.Item
                                value="No Change"
                                className="custom-label border border-neutral-600 p-4 h-16 rounded-md data-[state=on]:border-green-500 data-[state=on]:bg-neutral-900 focus:border-green-500 outline-none hover:border-green-500 w-full"
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

                    {/* Dataset 2 Controls */}
                    <div className="flex flex-col w-full md:w-1/2 lg:w-1/2 mb-4">
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
                                className="custom-label border border-neutral-600 p-4 h-16 rounded-md data-[state=on]:border-green-500 data-[state=on]:bg-neutral-900 focus:border-green-500 outline-none hover:border-green-500 w-full"
                            >
                                Normalize
                            </ToggleGroup.Item>

                            <ToggleGroup.Item
                                value="Join Columns"
                                className="custom-label border border-neutral-600 p-4 h-16 rounded-md data-[state=on]:border-green-500 data-[state=on]:bg-neutral-900 focus:border-green-500 outline-none hover:border-green-500 w-full"
                            >
                                Join Columns
                            </ToggleGroup.Item>

                            <ToggleGroup.Item
                                value="No Change"
                                className="custom-label border border-neutral-600 p-4 h-16 rounded-md data-[state=on]:border-green-500 data-[state=on]:bg-neutral-900 focus:border-green-500 outline-none hover:border-green-500 w-full"
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

                    {/* Conditionally Render Dataset 3 and Dataset 4 Controls */}
                    {splitType === "VerticalHorizontal" && (
                        <>
                            {/* Dataset 3 Controls */}
                            <div className="flex flex-col w-full md:w-1/2 lg:w-1/2 mb-4">
                                <h3 className="text-lg text-white mb-2">Dataset 3</h3>
                                <ToggleGroup.Root
                                    orientation="vertical"
                                    className="flex flex-col gap-3 my-2 w-full"
                                    type="single"
                                    value={planSelected3 || undefined}
                                    onValueChange={handleValueChange3}
                                >
                                    <ToggleGroup.Item
                                        value="BCNF"
                                        className="custom-label border border-neutral-600 p-4 h-16 rounded-md data-[state=on]:border-green-500 data-[state=on]:bg-neutral-900 focus:border-green-500 outline-none hover:border-green-500 w-full"
                                    >
                                        Normalize
                                    </ToggleGroup.Item>

                                    <ToggleGroup.Item
                                        value="Join Columns"
                                        className="custom-label border border-neutral-600 p-4 h-16 rounded-md data-[state=on]:border-green-500 data-[state=on]:bg-neutral-900 focus:border-green-500 outline-none hover:border-green-500 w-full"
                                    >
                                        Join Columns
                                    </ToggleGroup.Item>

                                    <ToggleGroup.Item
                                        value="No Change"
                                        className="custom-label border border-neutral-600 p-4 h-16 rounded-md data-[state=on]:border-green-500 data-[state=on]:bg-neutral-900 focus:border-green-500 outline-none hover:border-green-500 w-full"
                                    >
                                        No Change
                                    </ToggleGroup.Item>
                                </ToggleGroup.Root>
                                {/* Display validation error for Dataset 3 */}
                                {errors.dataset3StructureType && (
                                    <p className="text-red-500 text-sm mt-2">
                                        {errors.dataset3StructureType[0]}
                                    </p>
                                )}
                            </div>

                            {/* Dataset 4 Controls */}
                            <div className="flex flex-col w-full md:w-1/2 lg:w-1/2 mb-4">
                                <h3 className="text-lg text-white mb-2">Dataset 4</h3>
                                <ToggleGroup.Root
                                    orientation="vertical"
                                    className="flex flex-col gap-3 my-2 w-full"
                                    type="single"
                                    value={planSelected4 || undefined}
                                    onValueChange={handleValueChange4}
                                >
                                    <ToggleGroup.Item
                                        value="BCNF"
                                        className="custom-label border border-neutral-600 p-4 h-16 rounded-md data-[state=on]:border-green-500 data-[state=on]:bg-neutral-900 focus:border-green-500 outline-none hover:border-green-500 w-full"
                                    >
                                        Normalize
                                    </ToggleGroup.Item>

                                    <ToggleGroup.Item
                                        value="Join Columns"
                                        className="custom-label border border-neutral-600 p-4 h-16 rounded-md data-[state=on]:border-green-500 data-[state=on]:bg-neutral-900 focus:border-green-500 outline-none hover:border-green-500 w-full"
                                    >
                                        Join Columns
                                    </ToggleGroup.Item>

                                    <ToggleGroup.Item
                                        value="No Change"
                                        className="custom-label border border-neutral-600 p-4 h-16 rounded-md data-[state=on]:border-green-500 data-[state=on]:bg-neutral-900 focus:border-green-500 outline-none hover:border-green-500 w-full"
                                    >
                                        No Change
                                    </ToggleGroup.Item>
                                </ToggleGroup.Root>
                                {/* Display validation error for Dataset 4 */}
                                {errors.dataset4StructureType && (
                                    <p className="text-red-500 text-sm mt-2">
                                        {errors.dataset4StructureType[0]}
                                    </p>
                                )}
                            </div>
                        </>
                    )}
                </div>

                {/* 2. Sliders Section - dynamic height based on how many are visible */}
                <div className="flex flex-col w-full space-y-3.5">
                    {/**
                     * Count how many "BCNF" or "Join Columns" are selected across all visible datasets
                     * Each is 1 slider => multiply by 100
                     */}
                    <div
                        className="overflow-hidden transition-all duration-300"
                        style={{ height: `${sliderContainerHeight}px` }}
                    >
                        {/* Dataset 1 Sliders */}
                        {planSelected1 === "BCNF" && (
                            <div className="flex flex-col w-full mb-10">
                                <h3 className="text-lg text-white">Dataset 1 - BCNF Percentage</h3>
                                <Slider
                                    className="my-4 w-full"
                                    value={[dataset1BCNFValue]}
                                    onValueChange={handleDataset1BCNFSliderChange}
                                    min={0}
                                    max={100}
                                    step={1}
                                />
                                <span className="text-white text-sm">
                  BCNF Percentage: {dataset1BCNFValue}%
                </span>
                                {errors.dataset1BCNFSliderValue && (
                                    <p className="text-red-500 text-sm">
                                        {errors.dataset1BCNFSliderValue[0]}
                                    </p>
                                )}
                            </div>
                        )}

                        {planSelected1 === "Join Columns" && (
                            <div className="flex flex-col w-full mb-10">
                                <h3 className="text-lg text-white">Dataset 1 - Join Columns Percentage</h3>
                                <Slider
                                    className="my-4 w-full"
                                    value={[dataset1JoinColumnsValue]}
                                    onValueChange={handleDataset1JoinColumnsSliderChange}
                                    min={0}
                                    max={100}
                                    step={1}
                                />
                                <span className="text-white text-sm">
                  Join Columns Percentage: {dataset1JoinColumnsValue}%
                </span>
                                {errors.dataset1JoinColumnsSliderValue && (
                                    <p className="text-red-500 text-sm">
                                        {errors.dataset1JoinColumnsSliderValue[0]}
                                    </p>
                                )}
                            </div>
                        )}

                        {/* Dataset 2 Sliders */}
                        {planSelected2 === "BCNF" && (
                            <div className="flex flex-col w-full mb-10">
                                <h3 className="text-lg text-white">Dataset 2 - BCNF Percentage</h3>
                                <Slider
                                    className="my-4 w-full"
                                    value={[dataset2BCNFValue]}
                                    onValueChange={handleDataset2BCNFSliderChange}
                                    min={0}
                                    max={100}
                                    step={1}
                                />
                                <span className="text-white text-sm">
                  BCNF Percentage: {dataset2BCNFValue}%
                </span>
                                {errors.dataset2BCNFSliderValue && (
                                    <p className="text-red-500 text-sm">
                                        {errors.dataset2BCNFSliderValue[0]}
                                    </p>
                                )}
                            </div>
                        )}

                        {planSelected2 === "Join Columns" && (
                            <div className="flex flex-col w-full mb-10">
                                <h3 className="text-lg text-white">Dataset 2 - Join Columns Percentage</h3>
                                <Slider
                                    className="my-4 w-full"
                                    value={[dataset2JoinColumnsValue]}
                                    onValueChange={handleDataset2JoinColumnsSliderChange}
                                    min={0}
                                    max={100}
                                    step={1}
                                />
                                <span className="text-white text-sm">
                  Join Columns Percentage: {dataset2JoinColumnsValue}%
                </span>
                                {errors.dataset2JoinColumnsSliderValue && (
                                    <p className="text-red-500 text-sm">
                                        {errors.dataset2JoinColumnsSliderValue[0]}
                                    </p>
                                )}
                            </div>
                        )}

                        {/* Dataset 3 Sliders */}
                        {splitType === "VerticalHorizontal" && planSelected3 === "BCNF" && (
                            <div className="flex flex-col w-full mb-10">
                                <h3 className="text-lg text-white">Dataset 3 - BCNF Percentage</h3>
                                <Slider
                                    className="my-4 w-full"
                                    value={[dataset3BCNFValue]}
                                    onValueChange={handleDataset3BCNFSliderChange}
                                    min={0}
                                    max={100}
                                    step={1}
                                />
                                <span className="text-white text-sm">
                  BCNF Percentage: {dataset3BCNFValue}%
                </span>
                                {errors.dataset3BCNFSliderValue && (
                                    <p className="text-red-500 text-sm">
                                        {errors.dataset3BCNFSliderValue[0]}
                                    </p>
                                )}
                            </div>
                        )}

                        {splitType === "VerticalHorizontal" && planSelected3 === "Join Columns" && (
                            <div className="flex flex-col w-full mb-10">
                                <h3 className="text-lg text-white">Dataset 3 - Join Columns Percentage</h3>
                                <Slider
                                    className="my-4 w-full"
                                    value={[dataset3JoinColumnsValue]}
                                    onValueChange={handleDataset3JoinColumnsSliderChange}
                                    min={0}
                                    max={100}
                                    step={1}
                                />
                                <span className="text-white text-sm">
                  Join Columns Percentage: {dataset3JoinColumnsValue}%
                </span>
                                {errors.dataset3JoinColumnsSliderValue && (
                                    <p className="text-red-500 text-sm">
                                        {errors.dataset3JoinColumnsSliderValue[0]}
                                    </p>
                                )}
                            </div>
                        )}

                        {/* Dataset 4 Sliders */}
                        {splitType === "VerticalHorizontal" && planSelected4 === "BCNF" && (
                            <div className="flex flex-col w-full mb-10">
                                <h3 className="text-lg text-white">Dataset 4 - BCNF Percentage</h3>
                                <Slider
                                    className="my-4 w-full"
                                    value={[dataset4BCNFValue]}
                                    onValueChange={handleDataset4BCNFSliderChange}
                                    min={0}
                                    max={100}
                                    step={1}
                                />
                                <span className="text-white text-sm">
                  BCNF Percentage: {dataset4BCNFValue}%
                </span>
                                {errors.dataset4BCNFSliderValue && (
                                    <p className="text-red-500 text-sm">
                                        {errors.dataset4BCNFSliderValue[0]}
                                    </p>
                                )}
                            </div>
                        )}

                        {splitType === "VerticalHorizontal" && planSelected4 === "Join Columns" && (
                            <div className="flex flex-col w-full mb-10">
                                <h3 className="text-lg text-white">Dataset 4 - Join Columns Percentage</h3>
                                <Slider
                                    className="my-4 w-full"
                                    value={[dataset4JoinColumnsValue]}
                                    onValueChange={handleDataset4JoinColumnsSliderChange}
                                    min={0}
                                    max={100}
                                    step={1}
                                />
                                <span className="text-white text-sm">
                  Join Columns Percentage: {dataset4JoinColumnsValue}%
                </span>
                                {errors.dataset4JoinColumnsSliderValue && (
                                    <p className="text-red-500 text-sm">
                                        {errors.dataset4JoinColumnsSliderValue[0]}
                                    </p>
                                )}
                            </div>
                        )}
                    </div>
                </div>
            </div>
        </FormWrapper>
    );
};

export default Step4_StructureForm;
