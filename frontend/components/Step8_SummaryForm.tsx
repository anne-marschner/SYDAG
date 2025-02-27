"use client";
import React from "react";
import FormWrapper from "./FormWrapper";
import { FormItems } from "@/components/types/formTypes";

type StepProps = FormItems & {
    goTo: (index: number) => void;
};

const Step8_SummaryForm = ({
    // Step 1
    csvFile,
    hasHeaders,
    separator,
    quote,
    escape,

    // Step 2
    jsonFile,
    manualInput,
    mode,

    // Step 3
    splitType,
    rowOverlapPercentage,
    columnOverlapPercentage,
    rowDistribution,
    columnDistribution,
    overlapType,

    // Step 4
    dataset1StructureType,
    dataset2StructureType,
    dataset3StructureType,
    dataset4StructureType,

    // Step 5
    dataset1SchemaNoise,
    dataset1SchemaNoiseValue,
    dataset1SchemaKeyNoise,
    dataset1SchemaDeleteSchema,
    dataset1SchemaMultiselect,

    dataset2SchemaNoise,
    dataset2SchemaNoiseValue,
    dataset2SchemaKeyNoise,
    dataset2SchemaDeleteSchema,
    dataset2SchemaMultiselect,

    dataset3SchemaNoise,
    dataset3SchemaNoiseValue,
    dataset3SchemaKeyNoise,
    dataset3SchemaDeleteSchema,
    dataset3SchemaMultiselect,

    dataset4SchemaNoise,
    dataset4SchemaNoiseValue,
    dataset4SchemaKeyNoise,
    dataset4SchemaDeleteSchema,
    dataset4SchemaMultiselect,

    // Step 6
    dataset1DataNoise,
    dataset1DataNoiseValue,
    dataset1DataKeyNoise,
    dataset1DataNoiseInside,
    dataset1DataMultiselect,

    dataset2DataNoise,
    dataset2DataNoiseValue,
    dataset2DataKeyNoise,
    dataset2DataNoiseInside,
    dataset2DataMultiselect,

    dataset3DataNoise,
    dataset3DataNoiseValue,
    dataset3DataKeyNoise,
    dataset3DataNoiseInside,
    dataset3DataMultiselect,

    dataset4DataNoise,
    dataset4DataNoiseValue,
    dataset4DataKeyNoise,
    dataset4DataNoiseInside,
    dataset4DataMultiselect,

    // Step 7
    dataset1ShuffleOption,
    dataset2ShuffleOption,
    dataset3ShuffleOption,
    dataset4ShuffleOption,
    goTo,
}: StepProps) => {
    return (
        <FormWrapper
            title="Summary"
            description="Please review all your entered information before confirming."
        >
            <div className="pr-6 gap-y-4 gap-x-4 p-2 mt-0 rounded-md h-[600px] max-w-full mx-auto grid grid-cols-2 overflow-y-auto scrollbar-custom">

                {/* Step 1: File Upload */}
                <div className="bg-neutral-900 mb-5 border border-neutral-600 rounded-lg p-4">
                    <h3 className="font-semibold text-xl text-white mb-3">Step 1: File Upload</h3>
                    <div className="grid grid-cols-1 gap-4">
                        <div className="mb-3">
                            <h4 className="font-semibold text-white text-md">CSV File</h4>
                            {csvFile ? (
                                <p className="text-neutral-300 mt-1 text-sm">{csvFile.name}</p>
                            ) : (
                                <p className="text-neutral-500 mt-1 text-sm">No CSV file uploaded.</p>
                            )}
                            <button onClick={() => goTo(0)} className="text-[#6fe79f] text-xs mt-1 hover:text-green-300">
                                Change
                            </button>
                        </div>

                        <div className="mb-3">
                            <h4 className="font-semibold text-white text-md">Has Headers</h4>
                            <p className="text-neutral-300 mt-1 text-sm">{hasHeaders ? "Yes" : "No"}</p>
                            <button onClick={() => goTo(0)} className="text-[#6fe79f] text-xs mt-1 hover:text-green-300">
                                Change
                            </button>
                        </div>

                        <div className="mb-3">
                            <h4 className="font-semibold text-white text-md">Separator</h4>
                            <p className="text-neutral-300 mt-1 text-sm">{separator || "No separator specified."}</p>
                            <button onClick={() => goTo(0)} className="text-[#6fe79f] text-xs mt-1 hover:text-green-300">
                                Change
                            </button>
                        </div>

                        <div className="mb-3">
                            <h4 className="font-semibold text-white text-md">Quote</h4>
                            <p className="text-neutral-300 mt-1 text-sm">{quote || "No quote specified."}</p>
                            <button onClick={() => goTo(0)} className="text-[#6fe79f] text-xs mt-1 hover:text-green-300">
                                Change
                            </button>
                        </div>

                        <div className="mb-3">
                            <h4 className="font-semibold text-white text-md">Escape</h4>
                            <p className="text-neutral-300 mt-1 text-sm">{escape || "No escape character specified."}</p>
                            <button onClick={() => goTo(0)} className="text-[#6fe79f] text-xs mt-1 hover:text-green-300">
                                Change
                            </button>
                        </div>
                    </div>
                </div>


                {/* Step 2: Mode Selection */}
                <div className="bg-neutral-900 mb-5 border border-neutral-600 rounded-lg p-4">
                    <h3 className="font-semibold text-xl text-white mb-3">Step 2: Mode Selection</h3>
                    <div className="grid grid-cols-1 gap-4">
                        <div className="mb-3">
                            <h4 className="font-semibold text-white text-md">Mode</h4>
                            <p className="text-neutral-300 mt-1 text-sm">{mode}</p>
                            {mode === "UploadJson" && (
                                <div className="mt-2">
                                    <h5 className="font-semibold text-white text-md">JSON File</h5>
                                    {jsonFile ? (
                                        <p className="text-neutral-300 mt-1 text-sm">{jsonFile.name}</p>
                                    ) : (
                                        <p className="text-neutral-500 mt-1 text-sm">No JSON file uploaded.</p>
                                    )}
                                </div>
                            )}
                            <button onClick={() => goTo(1)} className="text-[#6fe79f] text-xs mt-1 hover:text-green-300">
                                Change
                            </button>
                        </div>
                    </div>
                </div>

                {/* Step 3: Split Options */}
                <div className="bg-neutral-900 mb-5 border border-neutral-600 rounded-lg p-4">
                    <h3 className="font-semibold text-xl text-white mb-3">Step 3: Split Options</h3>
                    <div className="grid grid-cols-1 gap-4">
                        <div className="mb-3">
                            <h4 className="font-semibold text-white text-md">Split Type</h4>
                            <p className="text-neutral-300 mt-1 text-sm">{splitType}</p>
                            <button onClick={() => goTo(2)} className="text-[#6fe79f] text-xs mt-1 hover:text-green-300">
                                Change
                            </button>
                        </div>

                        {splitType === "Horizontal" && (
                            <>
                                <div className="mb-3">
                                    <h4 className="font-semibold text-white text-md">Row Overlap</h4>
                                    <p className="text-neutral-300 mt-1 text-sm">{rowOverlapPercentage ?? "No value set"}</p>
                                    <button onClick={() => goTo(2)} className="text-[#6fe79f] text-xs mt-1 hover:text-green-300">
                                        Change
                                    </button>
                                </div>

                                <div className="mb-3">
                                    <h4 className="font-semibold text-white text-md">Row Distribution</h4>
                                    <p className="text-neutral-300 mt-1 text-sm">{rowDistribution ?? "No value set"}</p>
                                    <button onClick={() => goTo(2)} className="text-[#6fe79f] text-xs mt-1 hover:text-green-300">
                                        Change
                                    </button>
                                </div>

                                <div className="mb-3">
                                    <h4 className="font-semibold text-white text-md">Overlap Type</h4>
                                    <p className="text-neutral-300 mt-1 text-sm">{overlapType || "No overlap type selected."}</p>
                                    <button onClick={() => goTo(2)} className="text-[#6fe79f] text-xs mt-1 hover:text-green-300">
                                        Change
                                    </button>
                                </div>
                            </>
                        )}

                        {splitType === "Vertical" && (
                            <>
                                <div className="mb-3">
                                    <h4 className="font-semibold text-white text-md">Column Overlap</h4>
                                    <p className="text-neutral-300 mt-1 text-sm">{columnOverlapPercentage ?? "No value set"}</p>
                                    <button onClick={() => goTo(2)} className="text-[#6fe79f] text-xs mt-1 hover:text-green-300">
                                        Change
                                    </button>
                                </div>

                                <div className="mb-3">
                                    <h4 className="font-semibold text-white text-md">Column Distribution</h4>
                                    <p className="text-neutral-300 mt-1 text-sm">{columnDistribution ?? "No value set"}</p>
                                    <button onClick={() => goTo(2)} className="text-[#6fe79f] text-xs mt-1 hover:text-green-300">
                                        Change
                                    </button>
                                </div>

                                <div className="mb-3">
                                    <h4 className="font-semibold text-white text-md">Overlap Type</h4>
                                    <p className="text-neutral-300 mt-1 text-sm">{overlapType || "No overlap type selected."}</p>
                                    <button onClick={() => goTo(2)} className="text-[#6fe79f] text-xs mt-1 hover:text-green-300">
                                        Change
                                    </button>
                                </div>
                            </>
                        )}

                        {splitType === "VerticalHorizontal" && (
                            <>
                                {/* Horizontal Related Fields */}
                                <div className="mb-3">
                                    <h4 className="font-semibold text-white text-md">Row Overlap</h4>
                                    <p className="text-neutral-300 mt-1 text-sm">{rowOverlapPercentage ?? "No value set"}</p>
                                    <button onClick={() => goTo(2)} className="text-[#6fe79f] text-xs mt-1 hover:text-green-300">
                                        Change
                                    </button>
                                </div>

                                <div className="mb-3">
                                    <h4 className="font-semibold text-white text-md">Row Distribution</h4>
                                    <p className="text-neutral-300 mt-1 text-sm">{rowDistribution ?? "No value set"}</p>
                                    <button onClick={() => goTo(2)} className="text-[#6fe79f] text-xs mt-1 hover:text-green-300">
                                        Change
                                    </button>
                                </div>

                                {/* Vertical Related Fields */}
                                <div className="mb-3">
                                    <h4 className="font-semibold text-white text-md">Column Overlap</h4>
                                    <p className="text-neutral-300 mt-1 text-sm">{columnOverlapPercentage ?? "No value set"}</p>
                                    <button onClick={() => goTo(2)} className="text-[#6fe79f] text-xs mt-1 hover:text-green-300">
                                        Change
                                    </button>
                                </div>

                                <div className="mb-3">
                                    <h4 className="font-semibold text-white text-md">Column Distribution</h4>
                                    <p className="text-neutral-300 mt-1 text-sm">{columnDistribution ?? "No value set"}</p>
                                    <button onClick={() => goTo(2)} className="text-[#6fe79f] text-xs mt-1 hover:text-green-300">
                                        Change
                                    </button>
                                </div>

                                <div className="mb-3">
                                    <h4 className="font-semibold text-white text-md">Overlap Type</h4>
                                    <p className="text-neutral-300 mt-1 text-sm">{overlapType || "No overlap type selected."}</p>
                                    <button onClick={() => goTo(2)} className="text-[#6fe79f] text-xs mt-1 hover:text-green-300">
                                        Change
                                    </button>
                                </div>
                            </>
                        )}
                    </div>
                </div>

                {/* Step 4: Dataset Structure */}
                <div className="bg-neutral-900 mb-5 border border-neutral-600 rounded-lg p-4">
                    <h3 className="font-semibold text-xl text-white mb-3">Step 4: Dataset Structure</h3>
                    <div className="grid grid-cols-1 gap-4">
                        {/* Dataset 1 Structure */}
                        <div className="mb-3">
                            <h4 className="font-semibold text-white text-md">Dataset 1 Structure</h4>
                            <p className="text-neutral-300 mt-1 text-sm">{dataset1StructureType}</p>
                            <button onClick={() => goTo(3)} className="text-[#6fe79f] text-xs mt-1 hover:text-green-300">
                                Change
                            </button>
                        </div>

                        {/* Dataset 2 Structure */}
                        <div className="mb-3">
                            <h4 className="font-semibold text-white text-md">Dataset 2 Structure</h4>
                            <p className="text-neutral-300 mt-1 text-sm">{dataset2StructureType}</p>
                            <button onClick={() => goTo(3)} className="text-[#6fe79f] text-xs mt-1 hover:text-green-300">
                                Change
                            </button>
                        </div>

                        {/* Conditionally Render Dataset 3 and Dataset 4 Structures */}
                        {splitType === "VerticalHorizontal" && (
                            <>
                                {/* Dataset 3 Structure */}
                                <div className="mb-3">
                                    <h4 className="font-semibold text-white text-md">Dataset 3 Structure</h4>
                                    <p className="text-neutral-300 mt-1 text-sm">{dataset3StructureType}</p>
                                    <button onClick={() => goTo(3)} className="text-[#6fe79f] text-xs mt-1 hover:text-green-300">
                                        Change
                                    </button>
                                </div>

                                {/* Dataset 4 Structure */}
                                <div className="mb-3">
                                    <h4 className="font-semibold text-white text-md">Dataset 4 Structure</h4>
                                    <p className="text-neutral-300 mt-1 text-sm">{dataset4StructureType}</p>
                                    <button onClick={() => goTo(3)} className="text-[#6fe79f] text-xs mt-1 hover:text-green-300">
                                        Change
                                    </button>
                                </div>
                            </>
                        )}
                    </div>
                </div>

                {/* Step 5: Schema Noise Settings */}
                <div className="bg-neutral-900 mb-5 border border-neutral-600 rounded-lg p-4">
                    <h3 className="font-semibold text-xl text-white mb-3">
                        Step 5: Schema Noise Settings
                    </h3>
                    <div className="grid grid-cols-1 gap-4">
                        {/* Dataset 1 Schema Noise */}
                        <div className="mb-3">
                            <h4 className="font-semibold text-white text-md">Dataset 1 Schema Noise</h4>
                            <p className="text-neutral-300 mt-1 text-sm">
                                {dataset1SchemaNoise
                                    ? `Enabled, Value: ${dataset1SchemaNoiseValue}%`
                                    : "Disabled"}
                            </p>
                            <p className="text-neutral-300 mt-1 text-sm">
                                {`Schema Key Noise: ${dataset1SchemaKeyNoise ? "Enabled" : "Disabled"
                                    }`}
                            </p>
                            <p className="text-neutral-300 mt-1 text-sm">
                                {`Delete Schema: ${dataset1SchemaDeleteSchema ? "Enabled" : "Disabled"
                                    }`}
                            </p>

                            {/* multi-select */}
                            <p className="text-neutral-300 mt-1 text-sm">
                                Multi-Select:
                                {dataset1SchemaMultiselect && dataset1SchemaMultiselect.length > 0
                                    ? ` ${dataset1SchemaMultiselect.join(", ")}`
                                    : " None selected"}
                            </p>

                            <button
                                onClick={() => goTo(4)}
                                className="text-[#6fe79f] text-xs mt-1 hover:text-green-300"
                            >
                                Change
                            </button>
                        </div>

                        {/* Dataset 2 Schema Noise */}
                        <div className="mb-3">
                            <h4 className="font-semibold text-white text-md">Dataset 2 Schema Noise</h4>
                            <p className="text-neutral-300 mt-1 text-sm">
                                {dataset2SchemaNoise
                                    ? `Enabled, Value: ${dataset2SchemaNoiseValue}%`
                                    : "Disabled"}
                            </p>
                            <p className="text-neutral-300 mt-1 text-sm">
                                {`Schema Key Noise: ${dataset2SchemaKeyNoise ? "Enabled" : "Disabled"
                                    }`}
                            </p>
                            <p className="text-neutral-300 mt-1 text-sm">
                                {`Delete Schema: ${dataset2SchemaDeleteSchema ? "Enabled" : "Disabled"
                                    }`}
                            </p>

                            {/* multi-select */}
                            <p className="text-neutral-300 mt-1 text-sm">
                                Multi-Select:
                                {dataset2SchemaMultiselect && dataset2SchemaMultiselect.length > 0
                                    ? ` ${dataset2SchemaMultiselect.join(", ")}`
                                    : " None selected"}
                            </p>

                            <button
                                onClick={() => goTo(4)}
                                className="text-[#6fe79f] text-xs mt-1 hover:text-green-300"
                            >
                                Change
                            </button>
                        </div>

                        {/* Conditionally Render Dataset 3 and Dataset 4 Schema Noise */}
                        {splitType === "VerticalHorizontal" && (
                            <>
                                {/* Dataset 3 Schema Noise */}
                                <div className="mb-3">
                                    <h4 className="font-semibold text-white text-md">Dataset 3 Schema Noise</h4>
                                    <p className="text-neutral-300 mt-1 text-sm">
                                        {dataset3SchemaNoise
                                            ? `Enabled, Value: ${dataset3SchemaNoiseValue}%`
                                            : "Disabled"}
                                    </p>
                                    <p className="text-neutral-300 mt-1 text-sm">
                                        {`Schema Key Noise: ${dataset3SchemaKeyNoise ? "Enabled" : "Disabled"
                                            }`}
                                    </p>
                                    <p className="text-neutral-300 mt-1 text-sm">
                                        {`Delete Schema: ${dataset3SchemaDeleteSchema ? "Enabled" : "Disabled"
                                            }`}
                                    </p>

                                    {/* multi-select */}
                                    <p className="text-neutral-300 mt-1 text-sm">
                                        Multi-Select:
                                        {dataset3SchemaMultiselect && dataset3SchemaMultiselect.length > 0
                                            ? ` ${dataset3SchemaMultiselect.join(", ")}`
                                            : " None selected"}
                                    </p>

                                    <button
                                        onClick={() => goTo(4)}
                                        className="text-[#6fe79f] text-xs mt-1 hover:text-green-300"
                                    >
                                        Change
                                    </button>
                                </div>

                                {/* Dataset 4 Schema Noise */}
                                <div className="mb-3">
                                    <h4 className="font-semibold text-white text-md">Dataset 4 Schema Noise</h4>
                                    <p className="text-neutral-300 mt-1 text-sm">
                                        {dataset4SchemaNoise
                                            ? `Enabled, Value: ${dataset4SchemaNoiseValue}%`
                                            : "Disabled"}
                                    </p>
                                    <p className="text-neutral-300 mt-1 text-sm">
                                        {`Schema Key Noise: ${dataset4SchemaKeyNoise ? "Enabled" : "Disabled"
                                            }`}
                                    </p>
                                    <p className="text-neutral-300 mt-1 text-sm">
                                        {`Delete Schema: ${dataset4SchemaDeleteSchema ? "Enabled" : "Disabled"
                                            }`}
                                    </p>

                                    {/* multi-select */}
                                    <p className="text-neutral-300 mt-1 text-sm">
                                        Multi-Select:
                                        {dataset4SchemaMultiselect && dataset4SchemaMultiselect.length > 0
                                            ? ` ${dataset4SchemaMultiselect.join(", ")}`
                                            : " None selected"}
                                    </p>

                                    <button
                                        onClick={() => goTo(4)}
                                        className="text-[#6fe79f] text-xs mt-1 hover:text-green-300"
                                    >
                                        Change
                                    </button>
                                </div>
                            </>
                        )}
                    </div>
                </div>



                {/* Step 6: Data Noise Settings */}
                <div className="bg-neutral-900 mb-5 border border-neutral-600 rounded-lg p-4">
                    <h3 className="font-semibold text-xl text-white mb-3">Step 6: Data Noise Settings</h3>
                    <div className="grid grid-cols-1 gap-4">
                        {/* Dataset 1 Data Noise */}
                        <div className="mb-3">
                            <h4 className="font-semibold text-white text-md">Dataset 1 Data Noise</h4>
                            <p className="text-neutral-300 mt-1 text-sm">
                                {dataset1DataNoise
                                    ? `Enabled, Value: ${dataset1DataNoiseValue}%`
                                    : "Disabled"}
                            </p>
                            <p className="text-neutral-300 mt-1 text-sm">
                                {`Data Key Noise: ${dataset1DataKeyNoise ? "Enabled" : "Disabled"}`}
                            </p>
                            <p className="text-neutral-300 mt-1 text-sm">
                                {`Data Noise Inside: ${dataset1DataNoiseInside !== null
                                        ? `${dataset1DataNoiseInside}%`
                                        : "No value set"
                                    }`}
                            </p>

                            {/* Multi-Select Summary */}
                            <p className="text-neutral-300 mt-1 text-sm">
                                {dataset1DataMultiselect && dataset1DataMultiselect.length > 0
                                    ? `Multi-Select: ${dataset1DataMultiselect.join(", ")}`
                                    : "Multi-Select: None selected"}
                            </p>

                            <button
                                onClick={() => goTo(5)}
                                className="text-[#6fe79f] text-xs mt-1 hover:text-green-300"
                            >
                                Change
                            </button>
                        </div>

                        {/* Dataset 2 Data Noise */}
                        <div className="mb-3">
                            <h4 className="font-semibold text-white text-md">Dataset 2 Data Noise</h4>
                            <p className="text-neutral-300 mt-1 text-sm">
                                {dataset2DataNoise
                                    ? `Enabled, Value: ${dataset2DataNoiseValue}%`
                                    : "Disabled"}
                            </p>
                            <p className="text-neutral-300 mt-1 text-sm">
                                {`Data Key Noise: ${dataset2DataKeyNoise ? "Enabled" : "Disabled"}`}
                            </p>
                            <p className="text-neutral-300 mt-1 text-sm">
                                {`Data Noise Inside: ${dataset2DataNoiseInside !== null
                                        ? `${dataset2DataNoiseInside}%`
                                        : "No value set"
                                    }`}
                            </p>

                            {/* Multi-Select Summary */}
                            <p className="text-neutral-300 mt-1 text-sm">
                                {dataset2DataMultiselect && dataset2DataMultiselect.length > 0
                                    ? `Multi-Select: ${dataset2DataMultiselect.join(", ")}`
                                    : "Multi-Select: None selected"}
                            </p>

                            <button
                                onClick={() => goTo(5)}
                                className="text-[#6fe79f] text-xs mt-1 hover:text-green-300"
                            >
                                Change
                            </button>
                        </div>

                        {/* Conditionally Render Dataset 3 and Dataset 4 Data Noise */}
                        {splitType === "VerticalHorizontal" && (
                            <>
                                {/* Dataset 3 Data Noise */}
                                <div className="mb-3">
                                    <h4 className="font-semibold text-white text-md">Dataset 3 Data Noise</h4>
                                    <p className="text-neutral-300 mt-1 text-sm">
                                        {dataset3DataNoise
                                            ? `Enabled, Value: ${dataset3DataNoiseValue}%`
                                            : "Disabled"}
                                    </p>
                                    <p className="text-neutral-300 mt-1 text-sm">
                                        {`Data Key Noise: ${dataset3DataKeyNoise ? "Enabled" : "Disabled"}`}
                                    </p>
                                    <p className="text-neutral-300 mt-1 text-sm">
                                        {`Data Noise Inside: ${dataset3DataNoiseInside !== null
                                                ? `${dataset3DataNoiseInside}%`
                                                : "No value set"
                                            }`}
                                    </p>

                                    {/* Multi-Select Summary */}
                                    <p className="text-neutral-300 mt-1 text-sm">
                                        {dataset3DataMultiselect && dataset3DataMultiselect.length > 0
                                            ? `Multi-Select: ${dataset3DataMultiselect.join(", ")}`
                                            : "Multi-Select: None selected"}
                                    </p>

                                    <button
                                        onClick={() => goTo(5)}
                                        className="text-[#6fe79f] text-xs mt-1 hover:text-green-300"
                                    >
                                        Change
                                    </button>
                                </div>

                                {/* Dataset 4 Data Noise */}
                                <div className="mb-3">
                                    <h4 className="font-semibold text-white text-md">Dataset 4 Data Noise</h4>
                                    <p className="text-neutral-300 mt-1 text-sm">
                                        {dataset4DataNoise
                                            ? `Enabled, Value: ${dataset4DataNoiseValue}%`
                                            : "Disabled"}
                                    </p>
                                    <p className="text-neutral-300 mt-1 text-sm">
                                        {`Data Key Noise: ${dataset4DataKeyNoise ? "Enabled" : "Disabled"}`}
                                    </p>
                                    <p className="text-neutral-300 mt-1 text-sm">
                                        {`Data Noise Inside: ${dataset4DataNoiseInside !== null
                                                ? `${dataset4DataNoiseInside}%`
                                                : "No value set"
                                            }`}
                                    </p>

                                    {/* Multi-Select Summary */}
                                    <p className="text-neutral-300 mt-1 text-sm">
                                        {dataset4DataMultiselect && dataset4DataMultiselect.length > 0
                                            ? `Multi-Select: ${dataset4DataMultiselect.join(", ")}`
                                            : "Multi-Select: None selected"}
                                    </p>

                                    <button
                                        onClick={() => goTo(5)}
                                        className="text-[#6fe79f] text-xs mt-1 hover:text-green-300"
                                    >
                                        Change
                                    </button>
                                </div>
                            </>
                        )}
                    </div>
                </div>




                {/* Step 7: Shuffle Options */}
                <div className="bg-neutral-900 mb-5 border border-neutral-600 rounded-lg p-4">
                    <h3 className="font-semibold text-xl text-white mb-3">Step 7: Shuffle Options</h3>
                    <div className="grid grid-cols-1 gap-4">
                        {/* Dataset 1 Shuffle Option */}
                        <div className="mb-3">
                            <h4 className="font-semibold text-white text-md">Shuffle Option for Dataset 1</h4>
                            <p className="text-neutral-300 mt-1 text-sm">
                                {dataset1ShuffleOption ? dataset1ShuffleOption : "No options selected."}
                            </p>
                            <button onClick={() => goTo(6)} className="text-[#6fe79f] text-xs mt-1 hover:text-green-300">
                                Change
                            </button>
                        </div>

                        {/* Dataset 2 Shuffle Option */}
                        <div className="mb-3">
                            <h4 className="font-semibold text-white text-md">Shuffle Option for Dataset 2</h4>
                            <p className="text-neutral-300 mt-1 text-sm">
                                {dataset2ShuffleOption ? dataset2ShuffleOption : "No options selected."}
                            </p>
                            <button onClick={() => goTo(6)} className="text-[#6fe79f] text-xs mt-1 hover:text-green-300">
                                Change
                            </button>
                        </div>

                        {/* Conditionally Render Dataset 3 and Dataset 4 Shuffle Options */}
                        {splitType === "VerticalHorizontal" && (
                            <>
                                {/* Dataset 3 Shuffle Option */}
                                <div className="mb-3">
                                    <h4 className="font-semibold text-white text-md">Shuffle Option for Dataset 3</h4>
                                    <p className="text-neutral-300 mt-1 text-sm">
                                        {dataset3ShuffleOption ? dataset3ShuffleOption : "No options selected."}
                                    </p>
                                    <button onClick={() => goTo(6)} className="text-[#6fe79f] text-xs mt-1 hover:text-green-300">
                                        Change
                                    </button>
                                </div>

                                {/* Dataset 4 Shuffle Option */}
                                <div className="mb-3">
                                    <h4 className="font-semibold text-white text-md">Shuffle Option for Dataset 4</h4>
                                    <p className="text-neutral-300 mt-1 text-sm">
                                        {dataset4ShuffleOption ? dataset4ShuffleOption : "No options selected."}
                                    </p>
                                    <button onClick={() => goTo(6)} className="text-[#6fe79f] text-xs mt-1 hover:text-green-300">
                                        Change
                                    </button>
                                </div>
                            </>
                        )}
                    </div>
                </div>


            </div>
        </FormWrapper>
    );
};

export default Step8_SummaryForm;
