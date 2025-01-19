"use client";
import React, { useState, useEffect } from "react";
import Image from "next/image";
import * as ToggleGroup from "@radix-ui/react-toggle-group";
import FormWrapper from "./FormWrapper";
import { Slider } from "@/components/ui/slider";
import { FormItems } from "@/components/types/formTypes";
import vertical from "../public/assets/vertical.svg";
import horizontal from "../public/assets/horizontal.svg";
import verticalHorizontal from "../public/assets/verticalHorizontal.svg";

type StepProps = {
    splitType: "Horizontal" | "Vertical" | "VerticalHorizontal" | null;
    rowOverlapPercentage: number | null;
    columnOverlapPercentage: number | null;
    rowDistribution: number | null;
    columnDistribution: number | null;
    overlapType: "Mixed Overlap" | "Block Overlap" | null;
    updateForm: (fieldToUpdate: Partial<FormItems>) => void;
    errors: Record<string, string[]>;
};

type SplitType = "Vertical" | "Horizontal" | "VerticalHorizontal";
type OverlapType = "Mixed Overlap" | "Block Overlap";

const Step3_SplitForm = ({
                             updateForm,
                             splitType,
                             rowOverlapPercentage,
                             columnOverlapPercentage,
                             rowDistribution,
                             columnDistribution,
                             overlapType,
                             errors,
                         }: StepProps) => {
    const [planSelected, setPlanSelected] = useState<SplitType | null>(splitType);
    const [rowOverlapValue, setRowOverlapValue] = useState<number>(rowOverlapPercentage ?? 0);
    const [columnOverlapValue, setColumnOverlapValue] = useState<number>(columnOverlapPercentage ?? 0);
    const [rowDistributionValue, setRowDistributionValue] = useState<number>(rowDistribution ?? 0);
    const [columnDistributionValue, setColumnDistributionValue] = useState<number>(columnDistribution ?? 0);
    const [selectedOverlapType, setSelectedOverlapType] = useState<OverlapType | null>(overlapType);

    const handleValueChange = (selectedSplitType: SplitType) => {
        if (selectedSplitType) {
            // Reset all values to 0 when a new split type is selected
            setPlanSelected(selectedSplitType);
            setRowOverlapValue(0);
            setColumnOverlapValue(0);
            setRowDistributionValue(0);
            setColumnDistributionValue(0);
            setSelectedOverlapType(null);

            // Update the form state with the reset values
            updateForm({
                splitType: selectedSplitType,
                rowOverlapPercentage: 0,
                columnOverlapPercentage: 0,
                rowDistribution: 0,
                columnDistribution: 0,
                overlapType: null,
            });
        }
    };

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

    const handleRowDistributionChange = (value: number[]) => {
        const newValue = value[0];
        setRowDistributionValue(newValue);
        updateForm({ rowDistribution: newValue });
    };

    const handleColumnDistributionChange = (value: number[]) => {
        const newValue = value[0];
        setColumnDistributionValue(newValue);
        updateForm({ columnDistribution: newValue });
    };

    const handleOverlapTypeChange = (value: "Mixed Overlap" | "Block Overlap" | null) => {
        setSelectedOverlapType(value);
        updateForm({ overlapType: value });
    };

    return (
        <FormWrapper
            title="Select Split Type"
            description="You have the option of horizontal, vertical, or both splits."
        >
            <ToggleGroup.Root
                orientation="horizontal"
                className="flex flex-col gap-3 my-2 md:flex-row md:items-center md:justify-between md:gap-8"
                type="single"
                value={planSelected || undefined}
                onValueChange={handleValueChange}
            >
                <ToggleGroup.Item
                    value="Horizontal"
                    className="border border-neutral-600 flex items-start gap-3 p-3 h-16 rounded-md data-[state=on]:border-[#77f6aa] data-[state=on]:bg-neutral-900 focus:border-[#77f6aa] outline-none hover:border-[#77f6aa] md:h-32 md:w-1/2 md:flex-col md:justify-between md:gap-0"
                >
                    <Image src={horizontal} alt="Horizontal" width="40" height="40" />
                    <div className="relative -top-1 flex flex-col items-start md:top-0">
                        <p className="text-white font-semibold">Horizontal</p>
                        <p className="text-sm custom-label">Split</p>
                    </div>
                </ToggleGroup.Item>

                <ToggleGroup.Item
                    value="Vertical"
                    className="border border-neutral-600 flex items-start gap-3 p-3 h-16 rounded-md data-[state=on]:border-[#77f6aa] data-[state=on]:bg-neutral-900 focus:border-[#77f6aa] outline-none hover:border-[#77f6aa] md:h-32 md:w-1/2 md:flex-col md:justify-between md:gap-0"
                >
                    <Image src={vertical} alt="Vertical" width="40" height="40" />
                    <div className="relative -top-1 flex flex-col items-start md:top-0">
                        <p className="text-white font-semibold">Vertical</p>
                        <p className="text-sm custom-label">Split</p>
                    </div>
                </ToggleGroup.Item>

                <ToggleGroup.Item
                    value="VerticalHorizontal"
                    className="border border-neutral-600 flex items-start gap-3 p-3 h-16 rounded-md data-[state=on]:border-[#77f6aa] data-[state=on]:bg-neutral-900 focus:border-[#77f6aa] outline-none hover:border-[#77f6aa] md:h-32 md:w-1/2 md:flex-col md:justify-between md:gap-0"
                >
                    <Image src={verticalHorizontal} alt="Vertical and Horizontal" width="40" height="40" />
                    <div className="relative -top-1 flex flex-col items-start md:top-0">
                        <p className="text-white font-semibold">Both</p>
                        <p className="text-sm custom-label">Split</p>
                    </div>
                </ToggleGroup.Item>
            </ToggleGroup.Root>

            {errors.splitType && (
                <p className="text-red-500 text-sm mt-2">{errors.splitType[0]}</p>
            )}

            {/* Overlap Type Toggle Group */}
            {(planSelected === "Horizontal" || planSelected === "VerticalHorizontal") && (
                <div className="flex flex-col w-full mt-4">
                    <h3 className="text-lg text-white mb-2">Overlap Type</h3>
                    <ToggleGroup.Root
                        orientation="horizontal"
                        className="flex gap-3 w-full custom-label"
                        type="single"
                        value={selectedOverlapType || undefined}
                        onValueChange={(value) => handleOverlapTypeChange(value as "Mixed Overlap" | "Block Overlap" | null)}
                    >
                        <ToggleGroup.Item
                            value="Mixed Overlap"
                            className="border border-neutral-600 flex-1 p-6 h-16 rounded-md data-[state=on]:border-green-500 data-[state=on]:bg-neutral-900 focus:border-green-500 outline-none hover:border-green-500 text-center"
                        >
                            Mixed Overlap
                        </ToggleGroup.Item>

                        <ToggleGroup.Item
                            value="Block Overlap"
                            className="border border-neutral-600 flex-1 p-6 h-16 rounded-md data-[state=on]:border-green-500 data-[state=on]:bg-neutral-900 focus:border-green-500 outline-none hover:border-green-500 text-center"
                        >
                            Block Overlap
                        </ToggleGroup.Item>
                    </ToggleGroup.Root>

                    {errors.overlapType && (
                        <p className="text-red-500 text-sm mt-2">{errors.overlapType[0]}</p>
                    )}
                </div>
            )}

            {/* Sliders Section */}
            <div className="mt-4 flex  flex-col">
                {/* Wrapper to control dynamic height */}
                <div
                    className={`flex-grow  space-y-6 scrollbar-custom ${
                        planSelected === "Horizontal" || planSelected === "VerticalHorizontal"
                            ? "max-h-[140px]"
                            : "max-h-[250px]"
                    }`}
                >
                    {(planSelected === "Horizontal" || planSelected === "VerticalHorizontal") && (
                        <>
                            <div className="flex flex-col w-full">
                                <h3 className="text-lg text-white">Row Overlap</h3>
                                <Slider
                                    className="my-4 w-full"
                                    value={[rowOverlapValue]}
                                    onValueChange={handleRowOverlapChange}
                                    min={0}
                                    max={100}
                                    step={1}
                                />
                                <span className="text-white text-sm">
                                    Row Overlap Percentage: {rowOverlapValue}%
                                </span>
                            </div>

                            <div className="flex flex-col w-full">
                                <h3 className="text-lg text-white">Row Distribution</h3>
                                <Slider
                                    className="my-4 w-full"
                                    value={[rowDistributionValue]}
                                    onValueChange={handleRowDistributionChange}
                                    min={0}
                                    max={100}
                                    step={1}
                                />
                                <span className="text-white text-sm">
                                    Percentage of rows in left dataset: {rowDistributionValue}%
                                </span>
                            </div>
                        </>
                    )}

                    {(planSelected === "Vertical" || planSelected === "VerticalHorizontal") && (
                        <>
                            <div className="flex flex-col w-full">
                                <h3 className="text-lg text-white">Column Overlap</h3>
                                <Slider
                                    className="my-4 w-full"
                                    value={[columnOverlapValue]}
                                    onValueChange={handleColumnOverlapChange}
                                    min={0}
                                    max={100}
                                    step={1}
                                />
                                <span className="text-white text-sm">
                                    Column Overlap Percentage: {columnOverlapValue}%
                                </span>
                            </div>

                            <div className="flex flex-col w-full">
                                <h3 className="text-lg text-white">Column Distribution</h3>
                                <Slider
                                    className="my-4 w-full"
                                    value={[columnDistributionValue]}
                                    onValueChange={handleColumnDistributionChange}
                                    min={0}
                                    max={100}
                                    step={1}
                                />
                                <span className="text-white text-sm">
                                    Percentage of columns in left dataset: {columnDistributionValue}%
                                </span>
                            </div>
                        </>
                    )}
                </div>
            </div>
        </FormWrapper>
    )};

export default Step3_SplitForm;
