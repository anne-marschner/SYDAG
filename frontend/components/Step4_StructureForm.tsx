"use client";
import React, { useState, useEffect, useCallback } from "react";
import * as ToggleGroup from "@radix-ui/react-toggle-group";
import FormWrapper from "./FormWrapper";
import { FormItems } from "@/components/types/formTypes";
import { Slider } from "@/components/ui/slider";

// Define the types for the props 
type StepProps = {
    splitType: "Horizontal" | "Vertical" | "VerticalHorizontal" | null;

    datasetAStructureType: "BCNF" | "Merge Columns" | "No Change" | null;
    datasetBStructureType: "BCNF" | "Merge Columns" | "No Change" | null;
    datasetCStructureType: "BCNF" | "Merge Columns" | "No Change" | null;
    datasetDStructureType: "BCNF" | "Merge Columns" | "No Change" | null;

    // Slider values for Dataset A
    datasetABCNFSliderValue: number | null;
    datasetAMergeColumnsSliderValue: number | null;

    // Slider values for Dataset B
    datasetBBCNFSliderValue: number | null;
    datasetBMergeColumnsSliderValue: number | null;

    // Slider values for Dataset C
    datasetCBCNFSliderValue: number | null;
    datasetCMergeColumnsSliderValue: number | null;

    // Slider values for Dataset D
    datasetDBCNFSliderValue: number | null;
    datasetDMergeColumnsSliderValue: number | null;

    updateForm: (fieldToUpdate: Partial<FormItems>) => void;
    errors: Record<string, string[]>;
};

type StructureType = "BCNF" | "Merge Columns" | "No Change";

const Step4_StructureForm = ({
                                 splitType,
                                 updateForm,
                                 datasetAStructureType,
                                 datasetBStructureType,
                                 datasetCStructureType,
                                 datasetDStructureType,
                                 datasetABCNFSliderValue,
                                 datasetAMergeColumnsSliderValue,
                                 datasetBBCNFSliderValue,
                                 datasetBMergeColumnsSliderValue,
                                 datasetCBCNFSliderValue,
                                 datasetCMergeColumnsSliderValue,
                                 datasetDBCNFSliderValue,
                                 datasetDMergeColumnsSliderValue,
                                 errors,
                             }: StepProps) => {
    // State for selected structure type of each dataset
    const [planSelectedA, setPlanSelectedA] = useState<StructureType | null>(datasetAStructureType);
    const [planSelectedB, setPlanSelectedB] = useState<StructureType | null>(datasetBStructureType);
    const [planSelectedC, setPlanSelectedC] = useState<StructureType | null>(datasetCStructureType);
    const [planSelectedD, setPlanSelectedD] = useState<StructureType | null>(datasetDStructureType);

    // State for sliders
    const [datasetABCNFValue, setDatasetABCNFValue] = useState<number>(datasetABCNFSliderValue || 0);
    const [datasetAMergeColumnsValue, setDatasetAMergeColumnsValue] = useState<number>(
        datasetAMergeColumnsSliderValue || 0
    );

    const [datasetBBCNFValue, setDatasetBBCNFValue] = useState<number>(datasetBBCNFSliderValue || 0);
    const [datasetBMergeColumnsValue, setDatasetBMergeColumnsValue] = useState<number>(
        datasetBMergeColumnsSliderValue || 0
    );

    const [datasetCBCNFValue, setDatasetCBCNFValue] = useState<number>(datasetCBCNFSliderValue || 0);
    const [datasetCMergeColumnsValue, setDatasetCMergeColumnsValue] = useState<number>(
        datasetCMergeColumnsSliderValue || 0
    );

    const [datasetDBCNFValue, setDatasetDBCNFValue] = useState<number>(datasetDBCNFSliderValue || 0);
    const [datasetDMergeColumnsValue, setDatasetDMergeColumnsValue] = useState<number>(
        datasetDMergeColumnsSliderValue || 0
    );

    /**
     * Handlers for structure type toggles
     */
    const handleValueChangeA = useCallback(
        (selected: string | null) => {
            if (selected === "BCNF" || selected === "Merge Columns" || selected === "No Change") {
                setPlanSelectedA(selected as StructureType);
                updateForm({ datasetAStructureType: selected as StructureType });

                if (selected !== "BCNF") {
                    setDatasetABCNFValue(0);
                    updateForm({ datasetABCNFSliderValue: 0 });
                }
                if (selected !== "Merge Columns") {
                    setDatasetAMergeColumnsValue(0);
                    updateForm({ datasetAMergeColumnsSliderValue: 0 });
                }
            }
        },
        [updateForm]
    );

    const handleValueChangeB = useCallback(
        (selected: string | null) => {
            if (selected === "BCNF" || selected === "Merge Columns" || selected === "No Change") {
                setPlanSelectedB(selected as StructureType);
                updateForm({ datasetBStructureType: selected as StructureType });

                if (selected !== "BCNF") {
                    setDatasetBBCNFValue(0);
                    updateForm({ datasetBBCNFSliderValue: 0 });
                }
                if (selected !== "Merge Columns") {
                    setDatasetBMergeColumnsValue(0);
                    updateForm({ datasetBMergeColumnsSliderValue: 0 });
                }
            }
        },
        [updateForm]
    );

    const handleValueChangeC = useCallback(
        (selected: string | null) => {
            if (selected === "BCNF" || selected === "Merge Columns" || selected === "No Change") {
                setPlanSelectedC(selected as StructureType);
                updateForm({ datasetCStructureType: selected as StructureType });

                if (selected !== "BCNF") {
                    setDatasetCBCNFValue(0);
                    updateForm({ datasetCBCNFSliderValue: 0 });
                }
                if (selected !== "Merge Columns") {
                    setDatasetCMergeColumnsValue(0);
                    updateForm({ datasetCMergeColumnsSliderValue: 0 });
                }
            }
        },
        [updateForm]
    );

    const handleValueChangeD = useCallback(
        (selected: string | null) => {
            if (selected === "BCNF" || selected === "Merge Columns" || selected === "No Change") {
                setPlanSelectedD(selected as StructureType);
                updateForm({ datasetDStructureType: selected as StructureType });

                if (selected !== "BCNF") {
                    setDatasetDBCNFValue(0);
                    updateForm({ datasetDBCNFSliderValue: 0 });
                }
                if (selected !== "Merge Columns") {
                    setDatasetDMergeColumnsValue(0);
                    updateForm({ datasetDMergeColumnsSliderValue: 0 });
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

    const handleDatasetAMergeColumnsSliderChange = useCallback(
        (value: number[]) => {
            const newValue = value[0];
            setDatasetAMergeColumnsValue(newValue);
            updateForm({ datasetAMergeColumnsSliderValue: newValue });
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

    const handleDatasetBMergeColumnsSliderChange = useCallback(
        (value: number[]) => {
            const newValue = value[0];
            setDatasetBMergeColumnsValue(newValue);
            updateForm({ datasetBMergeColumnsSliderValue: newValue });
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

    const handleDatasetCMergeColumnsSliderChange = useCallback(
        (value: number[]) => {
            const newValue = value[0];
            setDatasetCMergeColumnsValue(newValue);
            updateForm({ datasetCMergeColumnsSliderValue: newValue });
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

    const handleDatasetDMergeColumnsSliderChange = useCallback(
        (value: number[]) => {
            const newValue = value[0];
            setDatasetDMergeColumnsValue(newValue);
            updateForm({ datasetDMergeColumnsSliderValue: newValue });
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
            setDatasetCMergeColumnsValue(0);
            updateForm({
                datasetCStructureType: null,
                datasetCBCNFSliderValue: 0,
                datasetCMergeColumnsSliderValue: 0,
            });

            setPlanSelectedD(null);
            setDatasetDBCNFValue(0);
            setDatasetDMergeColumnsValue(0);
            updateForm({
                datasetDStructureType: null,
                datasetDBCNFSliderValue: 0,
                datasetDMergeColumnsSliderValue: 0,
            });
        }
    }, [splitType, updateForm]);

    /**
     * Count how many sliders are visible.
     */
    let visibleSlidersCount = 0;

    if (planSelectedA === "BCNF" || planSelectedA === "Merge Columns") {
        visibleSlidersCount++;
    }
    if (planSelectedB === "BCNF" || planSelectedB === "Merge Columns") {
        visibleSlidersCount++;
    }
    if (splitType === "VerticalHorizontal") {
        if (planSelectedC === "BCNF" || planSelectedC === "Merge Columns") {
            visibleSlidersCount++;
        }
        if (planSelectedD === "BCNF" || planSelectedD === "Merge Columns") {
            visibleSlidersCount++;
        }
    }

    const sliderContainerHeight = visibleSlidersCount * 130;

    return (
        <FormWrapper
            title="Select Structure Options"
            description="Choose between 'No Change', 'Normalize to BCNF', and 'Merge Columns' for each dataset that SYDAG creates."
        >
            {/* Begin outer scrollable container for everything */}
            <div className="max-h-[600px] overflow-y-auto scrollbar-custom p-4 space-y-6">
                {/* Begin Container for Dataset Controls */}
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
                                value="Merge Columns"
                                className="custom-label border border-neutral-600 p-4 h-16 rounded-md data-[state=on]:border-green-500 data-[state=on]:bg-neutral-900 focus:border-green-500 outline-none hover:border-green-500 w-full"
                            >
                                Merge Columns
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
                                value="Merge Columns"
                                className="custom-label border border-neutral-600 p-4 h-16 rounded-md data-[state=on]:border-green-500 data-[state=on]:bg-neutral-900 focus:border-green-500 outline-none hover:border-green-500 w-full"
                            >
                                Merge Columns
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
                                        value="Merge Columns"
                                        className="custom-label border border-neutral-600 p-4 h-16 rounded-md data-[state=on]:border-green-500 data-[state=on]:bg-neutral-900 focus:border-green-500 outline-none hover:border-green-500 w-full"
                                    >
                                        Merge Columns
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
                                        value="Merge Columns"
                                        className="custom-label border border-neutral-600 p-4 h-16 rounded-md data-[state=on]:border-green-500 data-[state=on]:bg-neutral-900 focus:border-green-500 outline-none hover:border-green-500 w-full"
                                    >
                                        Merge Columns
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

                {/* Sliders */}
                <div className="flex flex-col w-full space-y-3.5">
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

                        {planSelectedA === "Merge Columns" && (
                            <div className="flex flex-col w-full mb-10">
                                <h3 className="text-lg text-white">Dataset A - Merge Columns Percentage</h3>
                                <Slider
                                    className="my-4 w-full"
                                    value={[datasetAMergeColumnsValue]}
                                    onValueChange={handleDatasetAMergeColumnsSliderChange}
                                    min={0}
                                    max={100}
                                    step={1}
                                />
                                <span className="text-white text-sm">
                  Merge Columns Percentage: {datasetAMergeColumnsValue}%
                </span>
                                {errors.datasetAMergeColumnsSliderValue && (
                                    <p className="text-red-500 text-sm">
                                        {errors.datasetAMergeColumnsSliderValue[0]}
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

                        {planSelectedB === "Merge Columns" && (
                            <div className="flex flex-col w-full mb-10">
                                <h3 className="text-lg text-white">Dataset B - Merge Columns Percentage</h3>
                                <Slider
                                    className="my-4 w-full"
                                    value={[datasetBMergeColumnsValue]}
                                    onValueChange={handleDatasetBMergeColumnsSliderChange}
                                    min={0}
                                    max={100}
                                    step={1}
                                />
                                <span className="text-white text-sm">
                  Merge Columns Percentage: {datasetBMergeColumnsValue}%
                </span>
                                {errors.datasetBMergeColumnsSliderValue && (
                                    <p className="text-red-500 text-sm">
                                        {errors.datasetBMergeColumnsSliderValue[0]}
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

                        {splitType === "VerticalHorizontal" && planSelectedC === "Merge Columns" && (
                            <div className="flex flex-col w-full mb-10">
                                <h3 className="text-lg text-white">Dataset C - Merge Columns Percentage</h3>
                                <Slider
                                    className="my-4 w-full"
                                    value={[datasetCMergeColumnsValue]}
                                    onValueChange={handleDatasetCMergeColumnsSliderChange}
                                    min={0}
                                    max={100}
                                    step={1}
                                />
                                <span className="text-white text-sm">
                  Merge Columns Percentage: {datasetCMergeColumnsValue}%
                </span>
                                {errors.datasetCMergeColumnsSliderValue && (
                                    <p className="text-red-500 text-sm">
                                        {errors.datasetCMergeColumnsSliderValue[0]}
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

                        {splitType === "VerticalHorizontal" && planSelectedD === "Merge Columns" && (
                            <div className="flex flex-col w-full mb-10">
                                <h3 className="text-lg text-white">Dataset D - Merge Columns Percentage</h3>
                                <Slider
                                    className="my-4 w-full"
                                    value={[datasetDMergeColumnsValue]}
                                    onValueChange={handleDatasetDMergeColumnsSliderChange}
                                    min={0}
                                    max={100}
                                    step={1}
                                />
                                <span className="text-white text-sm">
                  Merge Columns Percentage: {datasetDMergeColumnsValue}%
                </span>
                                {errors.datasetDMergeColumnsSliderValue && (
                                    <p className="text-red-500 text-sm">
                                        {errors.datasetDMergeColumnsSliderValue[0]}
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
