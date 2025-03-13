"use client";
import React, { useState, useEffect, useCallback } from "react";
import * as ToggleGroup from "@radix-ui/react-toggle-group";
import FormWrapper from "./FormWrapper";
import { FormItems } from "@/components/types/formTypes";
import { Slider } from "@/components/ui/slider";

type StepProps = {
    splitType: "Horizontal" | "Vertical" | "VerticalHorizontal" | null;

    datasetAStructureType: "BCNF" | "Join Columns" | "No Change" | null;
    datasetBStructureType: "BCNF" | "Join Columns" | "No Change" | null;
    datasetCStructureType: "BCNF" | "Join Columns" | "No Change" | null;
    datasetDStructureType: "BCNF" | "Join Columns" | "No Change" | null;

    // Slider values for Dataset A
    datasetABCNFSliderValue: number | null;
    datasetAJoinColumnsSliderValue: number | null;

    // Slider values for Dataset B
    datasetBBCNFSliderValue: number | null;
    datasetBJoinColumnsSliderValue: number | null;

    // Slider values for Dataset C
    datasetCBCNFSliderValue: number | null;
    datasetCJoinColumnsSliderValue: number | null;

    // Slider values for Dataset D
    datasetDBCNFSliderValue: number | null;
    datasetDJoinColumnsSliderValue: number | null;
    updateForm: (fieldToUpdate: Partial<FormItems>) => void;
    errors: Record<string, string[]>;
};

type StructureType = "BCNF" | "Join Columns" | "No Change";

const Step4_StructureForm = ({
                                 splitType,
                                 updateForm,
                                 datasetAStructureType,
                                 datasetBStructureType,
                                 datasetCStructureType,
                                 datasetDStructureType,
                                 datasetABCNFSliderValue,
                                 datasetAJoinColumnsSliderValue,
                                 datasetBBCNFSliderValue,
                                 datasetBJoinColumnsSliderValue,
                                 datasetCBCNFSliderValue,
                                 datasetCJoinColumnsSliderValue,
                                 datasetDBCNFSliderValue,
                                 datasetDJoinColumnsSliderValue,
                                 errors,
                             }: StepProps) => {
    // State for selected structure type per dataset
    const [planSelectedA, setPlanSelectedA] = useState<StructureType | null>(datasetAStructureType);
    const [planSelectedB, setPlanSelectedB] = useState<StructureType | null>(datasetBStructureType);
    const [planSelectedC, setPlanSelectedC] = useState<StructureType | null>(datasetCStructureType);
    const [planSelectedD, setPlanSelectedD] = useState<StructureType | null>(datasetDStructureType);

    // State for sliders
    const [datasetABCNFValue, setDatasetABCNFValue] = useState<number>(datasetABCNFSliderValue || 0);
    const [datasetAJoinColumnsValue, setDatasetAJoinColumnsValue] = useState<number>(
        datasetAJoinColumnsSliderValue || 0
    );

    const [datasetBBCNFValue, setDatasetBBCNFValue] = useState<number>(datasetBBCNFSliderValue || 0);
    const [datasetBJoinColumnsValue, setDatasetBJoinColumnsValue] = useState<number>(
        datasetBJoinColumnsSliderValue || 0
    );

    const [datasetCBCNFValue, setDatasetCBCNFValue] = useState<number>(datasetCBCNFSliderValue || 0);
    const [datasetCJoinColumnsValue, setDatasetCJoinColumnsValue] = useState<number>(
        datasetCJoinColumnsSliderValue || 0
    );

    const [datasetDBCNFValue, setDatasetDBCNFValue] = useState<number>(datasetDBCNFSliderValue || 0);
    const [datasetDJoinColumnsValue, setDatasetDJoinColumnsValue] = useState<number>(
        datasetDJoinColumnsSliderValue || 0
    );

    /**
     * Handlers for structure type toggles
     */
    const handleValueChangeA = useCallback(
        (selected: string | null) => {
            if (selected === "BCNF" || selected === "Join Columns" || selected === "No Change") {
                setPlanSelectedA(selected as StructureType);
                updateForm({ datasetAStructureType: selected as StructureType });

                // Reset sliders if not relevant
                if (selected !== "BCNF") {
                    setDatasetABCNFValue(0);
                    updateForm({ datasetABCNFSliderValue: 0 });
                }
                if (selected !== "Join Columns") {
                    setDatasetAJoinColumnsValue(0);
                    updateForm({ datasetAJoinColumnsSliderValue: 0 });
                }
            }
        },
        [updateForm]
    );

    const handleValueChangeB = useCallback(
        (selected: string | null) => {
            if (selected === "BCNF" || selected === "Join Columns" || selected === "No Change") {
                setPlanSelectedB(selected as StructureType);
                updateForm({ datasetBStructureType: selected as StructureType });

                if (selected !== "BCNF") {
                    setDatasetBBCNFValue(0);
                    updateForm({ datasetBBCNFSliderValue: 0 });
                }
                if (selected !== "Join Columns") {
                    setDatasetBJoinColumnsValue(0);
                    updateForm({ datasetBJoinColumnsSliderValue: 0 });
                }
            }
        },
        [updateForm]
    );

    const handleValueChangeC = useCallback(
        (selected: string | null) => {
            if (selected === "BCNF" || selected === "Join Columns" || selected === "No Change") {
                setPlanSelectedC(selected as StructureType);
                updateForm({ datasetCStructureType: selected as StructureType });

                if (selected !== "BCNF") {
                    setDatasetCBCNFValue(0);
                    updateForm({ datasetCBCNFSliderValue: 0 });
                }
                if (selected !== "Join Columns") {
                    setDatasetCJoinColumnsValue(0);
                    updateForm({ datasetCJoinColumnsSliderValue: 0 });
                }
            }
        },
        [updateForm]
    );

    const handleValueChangeD = useCallback(
        (selected: string | null) => {
            if (selected === "BCNF" || selected === "Join Columns" || selected === "No Change") {
                setPlanSelectedD(selected as StructureType);
                updateForm({ datasetDStructureType: selected as StructureType });

                if (selected !== "BCNF") {
                    setDatasetDBCNFValue(0);
                    updateForm({ datasetDBCNFSliderValue: 0 });
                }
                if (selected !== "Join Columns") {
                    setDatasetDJoinColumnsValue(0);
                    updateForm({ datasetDJoinColumnsSliderValue: 0 });
                }
            }
        },
        [updateForm]
    );

    /**
     * Slider handlers
     */
    const handleDatasetABCNFSliderChange = useCallback(
        (value: number[]) => {
            const newValue = value[0];
            setDatasetABCNFValue(newValue);
            updateForm({ datasetABCNFSliderValue: newValue });
        },
        [updateForm]
    );

    const handleDatasetAJoinColumnsSliderChange = useCallback(
        (value: number[]) => {
            const newValue = value[0];
            setDatasetAJoinColumnsValue(newValue);
            updateForm({ datasetAJoinColumnsSliderValue: newValue });
        },
        [updateForm]
    );

    const handleDatasetBBCNFSliderChange = useCallback(
        (value: number[]) => {
            const newValue = value[0];
            setDatasetBBCNFValue(newValue);
            updateForm({ datasetBBCNFSliderValue: newValue });
        },
        [updateForm]
    );

    const handleDatasetBJoinColumnsSliderChange = useCallback(
        (value: number[]) => {
            const newValue = value[0];
            setDatasetBJoinColumnsValue(newValue);
            updateForm({ datasetBJoinColumnsSliderValue: newValue });
        },
        [updateForm]
    );

    const handleDatasetCBCNFSliderChange = useCallback(
        (value: number[]) => {
            const newValue = value[0];
            setDatasetCBCNFValue(newValue);
            updateForm({ datasetCBCNFSliderValue: newValue });
        },
        [updateForm]
    );

    const handleDatasetCJoinColumnsSliderChange = useCallback(
        (value: number[]) => {
            const newValue = value[0];
            setDatasetCJoinColumnsValue(newValue);
            updateForm({ datasetCJoinColumnsSliderValue: newValue });
        },
        [updateForm]
    );

    const handleDatasetDBCNFSliderChange = useCallback(
        (value: number[]) => {
            const newValue = value[0];
            setDatasetDBCNFValue(newValue);
            updateForm({ datasetDBCNFSliderValue: newValue });
        },
        [updateForm]
    );

    const handleDatasetDJoinColumnsSliderChange = useCallback(
        (value: number[]) => {
            const newValue = value[0];
            setDatasetDJoinColumnsValue(newValue);
            updateForm({ datasetDJoinColumnsSliderValue: newValue });
        },
        [updateForm]
    );

    /**
     * Reset Datasets C and D if splitType != "VerticalHorizontal"
     */
    useEffect(() => {
        if (splitType !== "VerticalHorizontal") {
            setPlanSelectedC(null);
            setDatasetCBCNFValue(0);
            setDatasetCJoinColumnsValue(0);
            updateForm({
                datasetCStructureType: null,
                datasetCBCNFSliderValue: 0,
                datasetCJoinColumnsSliderValue: 0,
            });

            setPlanSelectedD(null);
            setDatasetDBCNFValue(0);
            setDatasetDJoinColumnsValue(0);
            updateForm({
                datasetDStructureType: null,
                datasetDBCNFSliderValue: 0,
                datasetDJoinColumnsSliderValue: 0,
            });
        }
    }, [splitType, updateForm]);

    /**
     * A. Count how many sliders are visible
     *    Each dataset can have either BCNF or Join Columns => A slider
     *    If it's "No Change", 0 sliders
     * B. Multiply that count by A00 for the container height
     */
    let visibleSlidersCount = 0;

    if (planSelectedA === "BCNF" || planSelectedA === "Join Columns") {
        visibleSlidersCount++;
    }
    if (planSelectedB === "BCNF" || planSelectedB === "Join Columns") {
        visibleSlidersCount++;
    }
    if (splitType === "VerticalHorizontal") {
        if (planSelectedC === "BCNF" || planSelectedC === "Join Columns") {
            visibleSlidersCount++;
        }
        if (planSelectedD === "BCNF" || planSelectedD === "Join Columns") {
            visibleSlidersCount++;
        }
    }

    const sliderContainerHeight = visibleSlidersCount * 130;

    return (
        <FormWrapper
            title="Select Structure Options"
            description="Choose between 'No Change', 'Normalize to BCNF', and 'Join Columns' for each dataset that SYDAG creates."
        >
            {/* Outer scrollable container for everything */}
            <div className="max-h-[600px] overflow-y-auto scrollbar-custom p-4 space-y-6">
                {/* Main Flex Container for Dataset Controls */}
                <div className="flex flex-col md:flex-row md:gap-8 w-full">
                    {/* Dataset A Controls */}
                    <div className="flex flex-col w-full md:w-1/2 lg:w-1/2 mb-4">
                        <h3 className="text-lg text-white mb-2">Dataset A</h3>
                        <ToggleGroup.Root
                            orientation="vertical"
                            className="flex flex-col gap-3 my-2 w-full"
                            type="single"
                            value={planSelectedA || undefined}
                            onValueChange={handleValueChangeA}
                        >
                            <ToggleGroup.Item
                                value="BCNF"
                                className="custom-label border border-neutral-600 p-4 h-16 rounded-md data-[state=on]:border-green-500 data-[state=on]:bg-neutral-900 focus:border-green-500 outline-none hover:border-green-500 w-full"
                            >
                                Normalize to BCNF
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
                        {/* Display validation error for Dataset A */}
                        {errors.datasetAStructureType && (
                            <p className="text-red-500 text-sm mt-2">
                                {errors.datasetAStructureType[0]}
                            </p>
                        )}
                    </div>

                    {/* Dataset B Controls */}
                    <div className="flex flex-col w-full md:w-1/2 lg:w-1/2 mb-4">
                        <h3 className="text-lg text-white mb-2">Dataset B</h3>
                        <ToggleGroup.Root
                            orientation="vertical"
                            className="flex flex-col gap-3 my-2 w-full"
                            type="single"
                            value={planSelectedB || undefined}
                            onValueChange={handleValueChangeB}
                        >
                            <ToggleGroup.Item
                                value="BCNF"
                                className="custom-label border border-neutral-600 p-4 h-16 rounded-md data-[state=on]:border-green-500 data-[state=on]:bg-neutral-900 focus:border-green-500 outline-none hover:border-green-500 w-full"
                            >
                                Normalize to BCNF
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
                        {/* Display validation error for Dataset B */}
                        {errors.datasetBStructureType && (
                            <p className="text-red-500 text-sm mt-2">
                                {errors.datasetBStructureType[0]}
                            </p>
                        )}
                    </div>

                    {/* Conditionally Render Dataset C and Dataset D Controls */}
                    {splitType === "VerticalHorizontal" && (
                        <>
                            {/* Dataset C Controls */}
                            <div className="flex flex-col w-full md:w-1/2 lg:w-1/2 mb-4">
                                <h3 className="text-lg text-white mb-2">Dataset C</h3>
                                <ToggleGroup.Root
                                    orientation="vertical"
                                    className="flex flex-col gap-3 my-2 w-full"
                                    type="single"
                                    value={planSelectedC || undefined}
                                    onValueChange={handleValueChangeC}
                                >
                                    <ToggleGroup.Item
                                        value="BCNF"
                                        className="custom-label border border-neutral-600 p-4 h-16 rounded-md data-[state=on]:border-green-500 data-[state=on]:bg-neutral-900 focus:border-green-500 outline-none hover:border-green-500 w-full"
                                    >
                                        Normalize to BCNF
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
                                {/* Display validation error for Dataset C */}
                                {errors.datasetCStructureType && (
                                    <p className="text-red-500 text-sm mt-2">
                                        {errors.datasetCStructureType[0]}
                                    </p>
                                )}
                            </div>

                            {/* Dataset D Controls */}
                            <div className="flex flex-col w-full md:w-1/2 lg:w-1/2 mb-4">
                                <h3 className="text-lg text-white mb-2">Dataset D</h3>
                                <ToggleGroup.Root
                                    orientation="vertical"
                                    className="flex flex-col gap-3 my-2 w-full"
                                    type="single"
                                    value={planSelectedD || undefined}
                                    onValueChange={handleValueChangeD}
                                >
                                    <ToggleGroup.Item
                                        value="BCNF"
                                        className="custom-label border border-neutral-600 p-4 h-16 rounded-md data-[state=on]:border-green-500 data-[state=on]:bg-neutral-900 focus:border-green-500 outline-none hover:border-green-500 w-full"
                                    >
                                        Normalize to BCNF
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
                                {/* Display validation error for Dataset D */}
                                {errors.datasetDStructureType && (
                                    <p className="text-red-500 text-sm mt-2">
                                        {errors.datasetDStructureType[0]}
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
                        {/* Dataset A Sliders */}
                        {planSelectedA === "BCNF" && (
                            <div className="flex flex-col w-full mb-10">
                                <h3 className="text-lg text-white">Dataset A - BCNF Percentage</h3>
                                <Slider
                                    className="my-4 w-full"
                                    value={[datasetABCNFValue]}
                                    onValueChange={handleDatasetABCNFSliderChange}
                                    min={0}
                                    max={100}
                                    step={1}
                                />
                                <span className="text-white text-sm">
                  BCNF Percentage: {datasetABCNFValue}%
                </span>
                                {errors.datasetABCNFSliderValue && (
                                    <p className="text-red-500 text-sm">
                                        {errors.datasetABCNFSliderValue[0]}
                                    </p>
                                )}
                            </div>
                        )}

                        {planSelectedA === "Join Columns" && (
                            <div className="flex flex-col w-full mb-10">
                                <h3 className="text-lg text-white">Dataset A - Join Columns Percentage</h3>
                                <Slider
                                    className="my-4 w-full"
                                    value={[datasetAJoinColumnsValue]}
                                    onValueChange={handleDatasetAJoinColumnsSliderChange}
                                    min={0}
                                    max={100}
                                    step={1}
                                />
                                <span className="text-white text-sm">
                  Join Columns Percentage: {datasetAJoinColumnsValue}%
                </span>
                                {errors.datasetAJoinColumnsSliderValue && (
                                    <p className="text-red-500 text-sm">
                                        {errors.datasetAJoinColumnsSliderValue[0]}
                                    </p>
                                )}
                            </div>
                        )}

                        {/* Dataset B Sliders */}
                        {planSelectedB === "BCNF" && (
                            <div className="flex flex-col w-full mb-10">
                                <h3 className="text-lg text-white">Dataset B - BCNF Percentage</h3>
                                <Slider
                                    className="my-4 w-full"
                                    value={[datasetBBCNFValue]}
                                    onValueChange={handleDatasetBBCNFSliderChange}
                                    min={0}
                                    max={100}
                                    step={1}
                                />
                                <span className="text-white text-sm">
                  BCNF Percentage: {datasetBBCNFValue}%
                </span>
                                {errors.datasetBBCNFSliderValue && (
                                    <p className="text-red-500 text-sm">
                                        {errors.datasetBBCNFSliderValue[0]}
                                    </p>
                                )}
                            </div>
                        )}

                        {planSelectedB === "Join Columns" && (
                            <div className="flex flex-col w-full mb-10">
                                <h3 className="text-lg text-white">Dataset B - Join Columns Percentage</h3>
                                <Slider
                                    className="my-4 w-full"
                                    value={[datasetBJoinColumnsValue]}
                                    onValueChange={handleDatasetBJoinColumnsSliderChange}
                                    min={0}
                                    max={100}
                                    step={1}
                                />
                                <span className="text-white text-sm">
                  Join Columns Percentage: {datasetBJoinColumnsValue}%
                </span>
                                {errors.datasetBJoinColumnsSliderValue && (
                                    <p className="text-red-500 text-sm">
                                        {errors.datasetBJoinColumnsSliderValue[0]}
                                    </p>
                                )}
                            </div>
                        )}

                        {/* Dataset C Sliders */}
                        {splitType === "VerticalHorizontal" && planSelectedC === "BCNF" && (
                            <div className="flex flex-col w-full mb-10">
                                <h3 className="text-lg text-white">Dataset C - BCNF Percentage</h3>
                                <Slider
                                    className="my-4 w-full"
                                    value={[datasetCBCNFValue]}
                                    onValueChange={handleDatasetCBCNFSliderChange}
                                    min={0}
                                    max={100}
                                    step={1}
                                />
                                <span className="text-white text-sm">
                  BCNF Percentage: {datasetCBCNFValue}%
                </span>
                                {errors.datasetCBCNFSliderValue && (
                                    <p className="text-red-500 text-sm">
                                        {errors.datasetCBCNFSliderValue[0]}
                                    </p>
                                )}
                            </div>
                        )}

                        {splitType === "VerticalHorizontal" && planSelectedC === "Join Columns" && (
                            <div className="flex flex-col w-full mb-10">
                                <h3 className="text-lg text-white">Dataset C - Join Columns Percentage</h3>
                                <Slider
                                    className="my-4 w-full"
                                    value={[datasetCJoinColumnsValue]}
                                    onValueChange={handleDatasetCJoinColumnsSliderChange}
                                    min={0}
                                    max={100}
                                    step={1}
                                />
                                <span className="text-white text-sm">
                  Join Columns Percentage: {datasetCJoinColumnsValue}%
                </span>
                                {errors.datasetCJoinColumnsSliderValue && (
                                    <p className="text-red-500 text-sm">
                                        {errors.datasetCJoinColumnsSliderValue[0]}
                                    </p>
                                )}
                            </div>
                        )}

                        {/* Dataset D Sliders */}
                        {splitType === "VerticalHorizontal" && planSelectedD === "BCNF" && (
                            <div className="flex flex-col w-full mb-10">
                                <h3 className="text-lg text-white">Dataset D - BCNF Percentage</h3>
                                <Slider
                                    className="my-4 w-full"
                                    value={[datasetDBCNFValue]}
                                    onValueChange={handleDatasetDBCNFSliderChange}
                                    min={0}
                                    max={100}
                                    step={1}
                                />
                                <span className="text-white text-sm">
                  BCNF Percentage: {datasetDBCNFValue}%
                </span>
                                {errors.datasetDBCNFSliderValue && (
                                    <p className="text-red-500 text-sm">
                                        {errors.datasetDBCNFSliderValue[0]}
                                    </p>
                                )}
                            </div>
                        )}

                        {splitType === "VerticalHorizontal" && planSelectedD === "Join Columns" && (
                            <div className="flex flex-col w-full mb-10">
                                <h3 className="text-lg text-white">Dataset D - Join Columns Percentage</h3>
                                <Slider
                                    className="my-4 w-full"
                                    value={[datasetDJoinColumnsValue]}
                                    onValueChange={handleDatasetDJoinColumnsSliderChange}
                                    min={0}
                                    max={100}
                                    step={1}
                                />
                                <span className="text-white text-sm">
                  Join Columns Percentage: {datasetDJoinColumnsValue}%
                </span>
                                {errors.datasetDJoinColumnsSliderValue && (
                                    <p className="text-red-500 text-sm">
                                        {errors.datasetDJoinColumnsSliderValue[0]}
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
