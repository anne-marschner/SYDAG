"use client";
import React from "react";
import FormWrapper from "./FormWrapper";
import {FormItems} from "@/components/types/formTypes";

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

                               // Step 5
                               datasetASchemaNoise,
                               datasetASchemaNoiseValue,
                               datasetASchemaKeyNoise,
                               datasetASchemaDeleteSchema,
                               datasetASchemaMultiselect,

                               datasetBSchemaNoise,
                               datasetBSchemaNoiseValue,
                               datasetBSchemaKeyNoise,
                               datasetBSchemaDeleteSchema,
                               datasetBSchemaMultiselect,

                               datasetCSchemaNoise,
                               datasetCSchemaNoiseValue,
                               datasetCSchemaKeyNoise,
                               datasetCSchemaDeleteSchema,
                               datasetCSchemaMultiselect,

                               datasetDSchemaNoise,
                               datasetDSchemaNoiseValue,
                               datasetDSchemaKeyNoise,
                               datasetDSchemaDeleteSchema,
                               datasetDSchemaMultiselect,

                               // Step 6
                               datasetADataNoise,
                               datasetADataNoiseValue,
                               datasetADataKeyNoise,
                               datasetADataNoiseInside,
                               datasetADataMultiselect,

                               datasetBDataNoise,
                               datasetBDataNoiseValue,
                               datasetBDataKeyNoise,
                               datasetBDataNoiseInside,
                               datasetBDataMultiselect,

                               datasetCDataNoise,
                               datasetCDataNoiseValue,
                               datasetCDataKeyNoise,
                               datasetCDataNoiseInside,
                               datasetCDataMultiselect,

                               datasetDDataNoise,
                               datasetDDataNoiseValue,
                               datasetDDataKeyNoise,
                               datasetDDataNoiseInside,
                               datasetDDataMultiselect,

                               // Step 7
                               datasetAShuffleOption,
                               datasetBShuffleOption,
                               datasetCShuffleOption,
                               datasetDShuffleOption,
                               goTo,
                           }: StepProps) => {
    return (
        <FormWrapper
            title="Summary"
            description="Please review all your entered information before confirming."
        >
            <div
                className="pr-6 gap-y-4 gap-x-4 p-2 mt-0 rounded-md h-[600px] max-w-full mx-auto grid grid-cols-2 overflow-y-auto scrollbar-custom">

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
                            <button onClick={() => goTo(0)}
                                    className="text-[#6fe79f] text-xs mt-1 hover:text-green-300">
                                Change
                            </button>
                        </div>

                        <div className="mb-3">
                            <h4 className="font-semibold text-white text-md">Has Headers</h4>
                            <p className="text-neutral-300 mt-1 text-sm">{hasHeaders ? "Yes" : "No"}</p>
                            <button onClick={() => goTo(0)}
                                    className="text-[#6fe79f] text-xs mt-1 hover:text-green-300">
                                Change
                            </button>
                        </div>

                        <div className="mb-3">
                            <h4 className="font-semibold text-white text-md">Separator</h4>
                            <p className="text-neutral-300 mt-1 text-sm">{separator || "No separator specified."}</p>
                            <button onClick={() => goTo(0)}
                                    className="text-[#6fe79f] text-xs mt-1 hover:text-green-300">
                                Change
                            </button>
                        </div>

                        <div className="mb-3">
                            <h4 className="font-semibold text-white text-md">Quote</h4>
                            <p className="text-neutral-300 mt-1 text-sm">{quote || "No quote specified."}</p>
                            <button onClick={() => goTo(0)}
                                    className="text-[#6fe79f] text-xs mt-1 hover:text-green-300">
                                Change
                            </button>
                        </div>

                        <div className="mb-3">
                            <h4 className="font-semibold text-white text-md">Escape</h4>
                            <p className="text-neutral-300 mt-1 text-sm">{escape || "No escape character specified."}</p>
                            <button onClick={() => goTo(0)}
                                    className="text-[#6fe79f] text-xs mt-1 hover:text-green-300">
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
                            <button onClick={() => goTo(1)}
                                    className="text-[#6fe79f] text-xs mt-1 hover:text-green-300">
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
                            <button onClick={() => goTo(2)}
                                    className="text-[#6fe79f] text-xs mt-1 hover:text-green-300">
                                Change
                            </button>
                        </div>

                        {splitType === "Horizontal" && (
                            <>
                                <div className="mb-3">
                                    <h4 className="font-semibold text-white text-md">Row Overlap</h4>
                                    <p className="text-neutral-300 mt-1 text-sm">{rowOverlapPercentage ?? "No value set"}</p>
                                    <button onClick={() => goTo(2)}
                                            className="text-[#6fe79f] text-xs mt-1 hover:text-green-300">
                                        Change
                                    </button>
                                </div>

                                <div className="mb-3">
                                    <h4 className="font-semibold text-white text-md">Row Distribution</h4>
                                    <p className="text-neutral-300 mt-1 text-sm">{rowDistribution ?? "No value set"}</p>
                                    <button onClick={() => goTo(2)}
                                            className="text-[#6fe79f] text-xs mt-1 hover:text-green-300">
                                        Change
                                    </button>
                                </div>

                                <div className="mb-3">
                                    <h4 className="font-semibold text-white text-md">Overlap Type</h4>
                                    <p className="text-neutral-300 mt-1 text-sm">{overlapType || "No overlap type selected."}</p>
                                    <button onClick={() => goTo(2)}
                                            className="text-[#6fe79f] text-xs mt-1 hover:text-green-300">
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
                                    <button onClick={() => goTo(2)}
                                            className="text-[#6fe79f] text-xs mt-1 hover:text-green-300">
                                        Change
                                    </button>
                                </div>

                                <div className="mb-3">
                                    <h4 className="font-semibold text-white text-md">Column Distribution</h4>
                                    <p className="text-neutral-300 mt-1 text-sm">{columnDistribution ?? "No value set"}</p>
                                    <button onClick={() => goTo(2)}
                                            className="text-[#6fe79f] text-xs mt-1 hover:text-green-300">
                                        Change
                                    </button>
                                </div>

                                <div className="mb-3">
                                    <h4 className="font-semibold text-white text-md">Overlap Type</h4>
                                    <p className="text-neutral-300 mt-1 text-sm">{overlapType || "No overlap type selected."}</p>
                                    <button onClick={() => goTo(2)}
                                            className="text-[#6fe79f] text-xs mt-1 hover:text-green-300">
                                        Change
                                    </button>
                                </div>
                            </>
                        )}

                        {splitType === "VerticalHorizontal" && (
                            <>
                                {/* Horizontal Fields */}
                                <div className="mb-3">
                                    <h4 className="font-semibold text-white text-md">Row Overlap</h4>
                                    <p className="text-neutral-300 mt-1 text-sm">{rowOverlapPercentage ?? "No value set"}</p>
                                    <button onClick={() => goTo(2)}
                                            className="text-[#6fe79f] text-xs mt-1 hover:text-green-300">
                                        Change
                                    </button>
                                </div>

                                <div className="mb-3">
                                    <h4 className="font-semibold text-white text-md">Row Distribution</h4>
                                    <p className="text-neutral-300 mt-1 text-sm">{rowDistribution ?? "No value set"}</p>
                                    <button onClick={() => goTo(2)}
                                            className="text-[#6fe79f] text-xs mt-1 hover:text-green-300">
                                        Change
                                    </button>
                                </div>

                                {/* Vertical Fields */}
                                <div className="mb-3">
                                    <h4 className="font-semibold text-white text-md">Column Overlap</h4>
                                    <p className="text-neutral-300 mt-1 text-sm">{columnOverlapPercentage ?? "No value set"}</p>
                                    <button onClick={() => goTo(2)}
                                            className="text-[#6fe79f] text-xs mt-1 hover:text-green-300">
                                        Change
                                    </button>
                                </div>

                                <div className="mb-3">
                                    <h4 className="font-semibold text-white text-md">Column Distribution</h4>
                                    <p className="text-neutral-300 mt-1 text-sm">{columnDistribution ?? "No value set"}</p>
                                    <button onClick={() => goTo(2)}
                                            className="text-[#6fe79f] text-xs mt-1 hover:text-green-300">
                                        Change
                                    </button>
                                </div>

                                <div className="mb-3">
                                    <h4 className="font-semibold text-white text-md">Overlap Type</h4>
                                    <p className="text-neutral-300 mt-1 text-sm">{overlapType || "No overlap type selected."}</p>
                                    <button onClick={() => goTo(2)}
                                            className="text-[#6fe79f] text-xs mt-1 hover:text-green-300">
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
                        {/* Dataset A Structure */}
                        <div className="mb-3">
                            <h4 className="font-semibold text-white text-md">Dataset A Structure</h4>
                            <p className="text-neutral-300 mt-1 text-sm">
                                {datasetAStructureType}
                                {datasetAStructureType === "BCNF"
                                    ? ` (${datasetABCNFSliderValue}%)`
                                    : datasetAStructureType === "Merge Columns"
                                        ? ` (${datasetAMergeColumnsSliderValue}%)`
                                        : ""}
                            </p>
                            <button
                                onClick={() => goTo(3)}
                                className="text-[#6fe79f] text-xs mt-1 hover:text-green-300"
                            >
                                Change
                            </button>
                        </div>

                        {/* Dataset B Structure */}
                        <div className="mb-3">
                            <h4 className="font-semibold text-white text-md">Dataset B Structure</h4>
                            <p className="text-neutral-300 mt-1 text-sm">
                                {datasetBStructureType}
                                {datasetBStructureType === "BCNF"
                                    ? ` (${datasetBBCNFSliderValue}%)`
                                    : datasetBStructureType === "Merge Columns"
                                        ? ` (${datasetBMergeColumnsSliderValue}%)`
                                        : ""}
                            </p>
                            <button
                                onClick={() => goTo(3)}
                                className="text-[#6fe79f] text-xs mt-1 hover:text-green-300"
                            >
                                Change
                            </button>
                        </div>

                        {/* Conditionally Render Dataset C and Dataset D Structures */}
                        {splitType === "VerticalHorizontal" && (
                            <>
                                {/* Dataset C Structure */}
                                <div className="mb-3">
                                    <h4 className="font-semibold text-white text-md">Dataset C Structure</h4>
                                    <p className="text-neutral-300 mt-1 text-sm">
                                        {datasetCStructureType}
                                        {datasetCStructureType === "BCNF"
                                            ? ` (${datasetCBCNFSliderValue}%)`
                                            : datasetCStructureType === "Merge Columns"
                                                ? ` (${datasetCMergeColumnsSliderValue}%)`
                                                : ""}
                                    </p>
                                    <button
                                        onClick={() => goTo(3)}
                                        className="text-[#6fe79f] text-xs mt-1 hover:text-green-300"
                                    >
                                        Change
                                    </button>
                                </div>

                                {/* Dataset D Structure */}
                                <div className="mb-3">
                                    <h4 className="font-semibold text-white text-md">Dataset D Structure</h4>
                                    <p className="text-neutral-300 mt-1 text-sm">
                                        {datasetDStructureType}
                                        {datasetDStructureType === "BCNF"
                                            ? ` (${datasetDBCNFSliderValue}%)`
                                            : datasetDStructureType === "Merge Columns"
                                                ? ` (${datasetDMergeColumnsSliderValue}%)`
                                                : ""}
                                    </p>
                                    <button
                                        onClick={() => goTo(3)}
                                        className="text-[#6fe79f] text-xs mt-1 hover:text-green-300"
                                    >
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
                        {/* Dataset A Schema Noise */}
                        <div className="mb-3">
                            <h4 className="font-semibold text-white text-md">Dataset A Schema Noise</h4>
                            <p className="text-neutral-300 mt-1 text-sm">
                                {datasetASchemaNoise
                                    ? `Enabled, Value: ${datasetASchemaNoiseValue}%`
                                    : "Disabled"}
                            </p>
                            <p className="text-neutral-300 mt-1 text-sm">
                                {`Schema Key Noise: ${datasetASchemaKeyNoise ? "Enabled" : "Disabled"
                                }`}
                            </p>
                            <p className="text-neutral-300 mt-1 text-sm">
                                {`Delete Schema: ${datasetASchemaDeleteSchema ? "Enabled" : "Disabled"
                                }`}
                            </p>

                            <p className="text-neutral-300 mt-1 text-sm">
                                Methods:
                                {datasetASchemaMultiselect && datasetASchemaMultiselect.length > 0
                                    ? ` ${datasetASchemaMultiselect.join(", ")}`
                                    : " None selected"}
                            </p>

                            <button
                                onClick={() => goTo(4)}
                                className="text-[#6fe79f] text-xs mt-1 hover:text-green-300"
                            >
                                Change
                            </button>
                        </div>

                        {/* Dataset B Schema Noise */}
                        <div className="mb-3">
                            <h4 className="font-semibold text-white text-md">Dataset B Schema Noise</h4>
                            <p className="text-neutral-300 mt-1 text-sm">
                                {datasetBSchemaNoise
                                    ? `Enabled, Value: ${datasetBSchemaNoiseValue}%`
                                    : "Disabled"}
                            </p>
                            <p className="text-neutral-300 mt-1 text-sm">
                                {`Schema Key Noise: ${datasetBSchemaKeyNoise ? "Enabled" : "Disabled"
                                }`}
                            </p>
                            <p className="text-neutral-300 mt-1 text-sm">
                                {`Delete Schema: ${datasetBSchemaDeleteSchema ? "Enabled" : "Disabled"
                                }`}
                            </p>

                            <p className="text-neutral-300 mt-1 text-sm">
                                Methods:
                                {datasetBSchemaMultiselect && datasetBSchemaMultiselect.length > 0
                                    ? ` ${datasetBSchemaMultiselect.join(", ")}`
                                    : " None selected"}
                            </p>

                            <button
                                onClick={() => goTo(4)}
                                className="text-[#6fe79f] text-xs mt-1 hover:text-green-300"
                            >
                                Change
                            </button>
                        </div>

                        {/* Conditionally Render Dataset C and Dataset D Schema Noise */}
                        {splitType === "VerticalHorizontal" && (
                            <>
                                {/* Dataset C Schema Noise */}
                                <div className="mb-3">
                                    <h4 className="font-semibold text-white text-md">Dataset C Schema Noise</h4>
                                    <p className="text-neutral-300 mt-1 text-sm">
                                        {datasetCSchemaNoise
                                            ? `Enabled, Value: ${datasetCSchemaNoiseValue}%`
                                            : "Disabled"}
                                    </p>
                                    <p className="text-neutral-300 mt-1 text-sm">
                                        {`Schema Key Noise: ${datasetCSchemaKeyNoise ? "Enabled" : "Disabled"
                                        }`}
                                    </p>
                                    <p className="text-neutral-300 mt-1 text-sm">
                                        {`Delete Schema: ${datasetCSchemaDeleteSchema ? "Enabled" : "Disabled"
                                        }`}
                                    </p>

                                    <p className="text-neutral-300 mt-1 text-sm">
                                        Methods:
                                        {datasetCSchemaMultiselect && datasetCSchemaMultiselect.length > 0
                                            ? ` ${datasetCSchemaMultiselect.join(", ")}`
                                            : " None selected"}
                                    </p>

                                    <button
                                        onClick={() => goTo(4)}
                                        className="text-[#6fe79f] text-xs mt-1 hover:text-green-300"
                                    >
                                        Change
                                    </button>
                                </div>

                                {/* Dataset D Schema Noise */}
                                <div className="mb-3">
                                    <h4 className="font-semibold text-white text-md">Dataset D Schema Noise</h4>
                                    <p className="text-neutral-300 mt-1 text-sm">
                                        {datasetDSchemaNoise
                                            ? `Enabled, Value: ${datasetDSchemaNoiseValue}%`
                                            : "Disabled"}
                                    </p>
                                    <p className="text-neutral-300 mt-1 text-sm">
                                        {`Schema Key Noise: ${datasetDSchemaKeyNoise ? "Enabled" : "Disabled"
                                        }`}
                                    </p>
                                    <p className="text-neutral-300 mt-1 text-sm">
                                        {`Delete Schema: ${datasetDSchemaDeleteSchema ? "Enabled" : "Disabled"
                                        }`}
                                    </p>

                                    <p className="text-neutral-300 mt-1 text-sm">
                                        Methods:
                                        {datasetDSchemaMultiselect && datasetDSchemaMultiselect.length > 0
                                            ? ` ${datasetDSchemaMultiselect.join(", ")}`
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
                        {/* Dataset A Data Noise */}
                        <div className="mb-3">
                            <h4 className="font-semibold text-white text-md">Dataset A Data Noise</h4>
                            <p className="text-neutral-300 mt-1 text-sm">
                                {datasetADataNoise
                                    ? `Enabled, Value: ${datasetADataNoiseValue}%`
                                    : "Disabled"}
                            </p>
                            <p className="text-neutral-300 mt-1 text-sm">
                                {`Data Key Noise: ${datasetADataKeyNoise ? "Enabled" : "Disabled"}`}
                            </p>
                            <p className="text-neutral-300 mt-1 text-sm">
                                {`Data Noise Inside: ${datasetADataNoiseInside !== null
                                    ? `${datasetADataNoiseInside}%`
                                    : "No value set"
                                }`}
                            </p>

                            <p className="text-neutral-300 mt-1 text-sm">
                                {datasetADataMultiselect && datasetADataMultiselect.length > 0
                                    ? `Methods: ${datasetADataMultiselect.join(", ")}`
                                    : "Methods: None selected"}
                            </p>

                            <button
                                onClick={() => goTo(5)}
                                className="text-[#6fe79f] text-xs mt-1 hover:text-green-300"
                            >
                                Change
                            </button>
                        </div>

                        {/* Dataset B Data Noise */}
                        <div className="mb-3">
                            <h4 className="font-semibold text-white text-md">Dataset B Data Noise</h4>
                            <p className="text-neutral-300 mt-1 text-sm">
                                {datasetBDataNoise
                                    ? `Enabled, Value: ${datasetBDataNoiseValue}%`
                                    : "Disabled"}
                            </p>
                            <p className="text-neutral-300 mt-1 text-sm">
                                {`Data Key Noise: ${datasetBDataKeyNoise ? "Enabled" : "Disabled"}`}
                            </p>
                            <p className="text-neutral-300 mt-1 text-sm">
                                {`Data Noise Inside: ${datasetBDataNoiseInside !== null
                                    ? `${datasetBDataNoiseInside}%`
                                    : "No value set"
                                }`}
                            </p>

                            <p className="text-neutral-300 mt-1 text-sm">
                                {datasetBDataMultiselect && datasetBDataMultiselect.length > 0
                                    ? `Methods: ${datasetBDataMultiselect.join(", ")}`
                                    : "Methods: None selected"}
                            </p>

                            <button
                                onClick={() => goTo(5)}
                                className="text-[#6fe79f] text-xs mt-1 hover:text-green-300"
                            >
                                Change
                            </button>
                        </div>

                        {/* Conditionally Render Dataset C and Dataset D Data Noise */}
                        {splitType === "VerticalHorizontal" && (
                            <>
                                {/* Dataset C Data Noise */}
                                <div className="mb-3">
                                    <h4 className="font-semibold text-white text-md">Dataset C Data Noise</h4>
                                    <p className="text-neutral-300 mt-1 text-sm">
                                        {datasetCDataNoise
                                            ? `Enabled, Value: ${datasetCDataNoiseValue}%`
                                            : "Disabled"}
                                    </p>
                                    <p className="text-neutral-300 mt-1 text-sm">
                                        {`Data Key Noise: ${datasetCDataKeyNoise ? "Enabled" : "Disabled"}`}
                                    </p>
                                    <p className="text-neutral-300 mt-1 text-sm">
                                        {`Data Noise Inside: ${datasetCDataNoiseInside !== null
                                            ? `${datasetCDataNoiseInside}%`
                                            : "No value set"
                                        }`}
                                    </p>

                                    <p className="text-neutral-300 mt-1 text-sm">
                                        {datasetCDataMultiselect && datasetCDataMultiselect.length > 0
                                            ? `Methods: ${datasetCDataMultiselect.join(", ")}`
                                            : "Methods: None selected"}
                                    </p>

                                    <button
                                        onClick={() => goTo(5)}
                                        className="text-[#6fe79f] text-xs mt-1 hover:text-green-300"
                                    >
                                        Change
                                    </button>
                                </div>

                                {/* Dataset D Data Noise */}
                                <div className="mb-3">
                                    <h4 className="font-semibold text-white text-md">Dataset D Data Noise</h4>
                                    <p className="text-neutral-300 mt-1 text-sm">
                                        {datasetDDataNoise
                                            ? `Enabled, Value: ${datasetDDataNoiseValue}%`
                                            : "Disabled"}
                                    </p>
                                    <p className="text-neutral-300 mt-1 text-sm">
                                        {`Data Key Noise: ${datasetDDataKeyNoise ? "Enabled" : "Disabled"}`}
                                    </p>
                                    <p className="text-neutral-300 mt-1 text-sm">
                                        {`Data Noise Inside: ${datasetDDataNoiseInside !== null
                                            ? `${datasetDDataNoiseInside}%`
                                            : "No value set"
                                        }`}
                                    </p>

                                    <p className="text-neutral-300 mt-1 text-sm">
                                        {datasetDDataMultiselect && datasetDDataMultiselect.length > 0
                                            ? `Methods: ${datasetDDataMultiselect.join(", ")}`
                                            : "Methods: None selected"}
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
                        {/* Dataset A Shuffle Option */}
                        <div className="mb-3">
                            <h4 className="font-semibold text-white text-md">Dataset A Shuffle Option</h4>
                            <p className="text-neutral-300 mt-1 text-sm">
                                {datasetAShuffleOption ? datasetAShuffleOption : "No options selected."}
                            </p>
                            <button onClick={() => goTo(6)}
                                    className="text-[#6fe79f] text-xs mt-1 hover:text-green-300">
                                Change
                            </button>
                        </div>

                        {/* Dataset B Shuffle Option */}
                        <div className="mb-3">
                            <h4 className="font-semibold text-white text-md">Dataset B Shuffle Option</h4>
                            <p className="text-neutral-300 mt-1 text-sm">
                                {datasetBShuffleOption ? datasetBShuffleOption : "No options selected."}
                            </p>
                            <button onClick={() => goTo(6)}
                                    className="text-[#6fe79f] text-xs mt-1 hover:text-green-300">
                                Change
                            </button>
                        </div>

                        {/* Conditionally Render Dataset C and Dataset D Shuffle Options */}
                        {splitType === "VerticalHorizontal" && (
                            <>
                                {/* Dataset C Shuffle Option */}
                                <div className="mb-3">
                                    <h4 className="font-semibold text-white text-md">Dataset C Shuffle Option</h4>
                                    <p className="text-neutral-300 mt-1 text-sm">
                                        {datasetCShuffleOption ? datasetCShuffleOption : "No options selected."}
                                    </p>
                                    <button onClick={() => goTo(6)}
                                            className="text-[#6fe79f] text-xs mt-1 hover:text-green-300">
                                        Change
                                    </button>
                                </div>

                                {/* Dataset D Shuffle Option */}
                                <div className="mb-3">
                                    <h4 className="font-semibold text-white text-md">Dataset D Shuffle Option</h4>
                                    <p className="text-neutral-300 mt-1 text-sm">
                                        {datasetDShuffleOption ? datasetDShuffleOption : "No options selected."}
                                    </p>
                                    <button onClick={() => goTo(6)}
                                            className="text-[#6fe79f] text-xs mt-1 hover:text-green-300">
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