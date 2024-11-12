"use client";
import React from "react";
import FormWrapper from "./FormWrapper";
import { FormItems } from "@/components/types/formTypes";

type StepProps = FormItems & {
    goTo: (index: number) => void;
};

const Step8_SummaryForm = ({
                               csvFile,
                               hasHeaders,
                               separator,
                               jsonFile,
                               manualInput,
                               mode,
                               splitType,
                               rowOverlapPercentage,
                               columnOverlapPercentage,
                               dataset1StructureType,
                               dataset2StructureType,

                               dataset1SchemaNoise,
                               dataset1SchemaNoiseValue,
                               dataset2SchemaNoise,
                               dataset2SchemaNoiseValue,
                               dataset1SchemaKeyNoise,
                               dataset2SchemaKeyNoise,

                               dataset1DataNoise,
                               dataset1DataNoiseValue,
                               dataset2DataNoise,
                               dataset2DataNoiseValue,
                               dataset1DataKeyNoise,
                               dataset2DataKeyNoise,

                               dataset1ShuffleOption,
                               dataset2ShuffleOption,
                               goTo,
                           }: StepProps) => {
    return (
        <FormWrapper
            title="Summary"
            description="Please review all your entered information before confirming."
        >
            <div className="pr-6 gap-y-4 gap-x-4 p-2 mt-0 rounded-md  h-[450px] max-w-full mx-auto grid grid-cols-2 overflow-y-auto scrollbar-custom">

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
                            <div className="mb-3">
                                <h4 className="font-semibold text-white text-md">Row Overlap</h4>
                                <p className="text-neutral-300 mt-1 text-sm">{rowOverlapPercentage ?? "No value set"}</p>
                                <button onClick={() => goTo(2)} className="text-[#6fe79f] text-xs mt-1 hover:text-green-300">
                                    Change
                                </button>
                            </div>
                        )}

                        {splitType === "Vertical" && (
                            <div className="mb-3">
                                <h4 className="font-semibold text-white text-md">Column Overlap</h4>
                                <p className="text-neutral-300 mt-1 text-sm">{columnOverlapPercentage ?? "No value set"}</p>
                                <button onClick={() => goTo(2)} className="text-[#6fe79f] text-xs mt-1 hover:text-green-300">
                                    Change
                                </button>
                            </div>
                        )}
                    </div>
                </div>

                {/* Step 4: Dataset Structure */}
                <div className="bg-neutral-900 mb-5 border border-neutral-600 rounded-lg p-4">
                    <h3 className="font-semibold text-xl text-white mb-3">Step 4: Dataset Structure</h3>
                    <div className="grid grid-cols-1 gap-4">
                        <div className="mb-3">
                            <h4 className="font-semibold text-white text-md">Dataset 1 Structure</h4>
                            <p className="text-neutral-300 mt-1 text-sm">{dataset1StructureType}</p>
                            <button onClick={() => goTo(3)} className="text-[#6fe79f] text-xs mt-1 hover:text-green-300">
                                Change
                            </button>
                        </div>

                        <div className="mb-3">
                            <h4 className="font-semibold text-white text-md">Dataset 2 Structure</h4>
                            <p className="text-neutral-300 mt-1 text-sm">{dataset2StructureType}</p>
                            <button onClick={() => goTo(3)} className="text-[#6fe79f] text-xs mt-1 hover:text-green-300">
                                Change
                            </button>
                        </div>
                    </div>
                </div>

                {/* Step 5: Noise Settings */}
                <div className="bg-neutral-900 mb-5 border border-neutral-600 rounded-lg p-4">
                    <h3 className="font-semibold text-xl text-white mb-3">Step 5: Schema Noise Settings</h3>
                    <div className="grid grid-cols-1 gap-4">
                        {/* Dataset 1 Noise */}
                        <div className="mb-3">
                            <h4 className="font-semibold text-white text-md">Dataset 1 Noise</h4>
                            <p className="text-neutral-300 mt-1 text-sm">
                                {dataset1SchemaNoise ? `Enabled, Value: ${dataset1SchemaNoiseValue}` : "Disabled"}
                            </p>
                            <p className="text-neutral-300 mt-1 text-sm">
                                {`Schema Key Noise: ${dataset1SchemaKeyNoise ? "Enabled" : "Disabled"}`}
                            </p>
                            <button onClick={() => goTo(4)} className="text-[#6fe79f] text-xs mt-1 hover:text-green-300">
                                Change
                            </button>
                        </div>

                        {/* Dataset 2 Noise */}
                        <div className="mb-3">
                            <h4 className="font-semibold text-white text-md">Dataset 2 Noise</h4>
                            <p className="text-neutral-300 mt-1 text-sm">
                                {dataset2SchemaNoise ? `Enabled, Value: ${dataset2SchemaNoiseValue}` : "Disabled"}
                            </p>
                            <p className="text-neutral-300 mt-1 text-sm">
                                {`Schema Key Noise: ${dataset2SchemaKeyNoise ? "Enabled" : "Disabled"}`}
                            </p>
                            <button onClick={() => goTo(4)} className="text-[#6fe79f] text-xs mt-1 hover:text-green-300">
                                Change
                            </button>
                        </div>
                    </div>
                </div>


                {/* Step 6: Data Noise Settings */}
                <div className="bg-neutral-900 mb-5 border border-neutral-600 rounded-lg p-4">
                    <h3 className="font-semibold text-xl text-white mb-3">Step 6: Data Noise Settings</h3>
                    <div className="grid grid-cols-1 gap-4">
                        {/* Dataset 1 Noise */}
                        <div className="mb-3">
                            <h4 className="font-semibold text-white text-md">Dataset 1 Noise</h4>
                            <p className="text-neutral-300 mt-1 text-sm">
                                {dataset1DataNoise ? `Enabled, Value: ${dataset1DataNoiseValue}` : "Disabled"}
                            </p>
                            <p className="text-neutral-300 mt-1 text-sm">
                                {`Data Key Noise: ${dataset1DataKeyNoise ? "Enabled" : "Disabled"}`}
                            </p>
                            <button onClick={() => goTo(4)} className="text-[#6fe79f] text-xs mt-1 hover:text-green-300">
                                Change
                            </button>
                        </div>

                        {/* Dataset 2 Noise */}
                        <div className="mb-3">
                            <h4 className="font-semibold text-white text-md">Dataset 2 Noise</h4>
                            <p className="text-neutral-300 mt-1 text-sm">
                                {dataset2DataNoise ? `Enabled, Value: ${dataset2DataNoiseValue}` : "Disabled"}
                            </p>
                            <p className="text-neutral-300 mt-1 text-sm">
                                {`Data Key Noise: ${dataset2DataKeyNoise ? "Enabled" : "Disabled"}`}
                            </p>
                            <button onClick={() => goTo(4)} className="text-[#6fe79f] text-xs mt-1 hover:text-green-300">
                                Change
                            </button>
                        </div>
                    </div>
                </div>



                {/* Step 7: Shuffle Options */}
                <div className="bg-neutral-900 mb-5 border border-neutral-600 rounded-lg p-4">
                    <h3 className="font-semibold text-xl text-white mb-3">Step 7: Shuffle Options</h3>
                    <div className="grid grid-cols-1 gap-4">
                        <div className="mb-3">
                            <h4 className="font-semibold text-white text-md">Shuffle Options for Dataset 1</h4>
                            <p className="text-neutral-300 mt-1 text-sm">
                                {dataset1ShuffleOption ? dataset1ShuffleOption : "No options selected."}
                            </p>
                            <button onClick={() => goTo(5)} className="text-[#6fe79f] text-xs mt-1 hover:text-green-300">
                                Change
                            </button>
                        </div>

                        <div className="mb-3">
                            <h4 className="font-semibold text-white text-md">Shuffle Options for Dataset 2</h4>
                            <p className="text-neutral-300 mt-1 text-sm">
                                {dataset2ShuffleOption ? dataset2ShuffleOption : "No options selected."}
                            </p>
                            <button onClick={() => goTo(5)} className="text-[#6fe79f] text-xs mt-1 hover:text-green-300">
                                Change
                            </button>
                        </div>
                    </div>
                </div>

            </div>
        </FormWrapper>
    );
};

export default Step8_SummaryForm;
