"use client";
import React from "react";
import { Button } from "@/components/ui/button";
import { AnimatePresence } from "framer-motion";
import Step0_Start from "@/components/Step0_Start";
import Step1_FileUploadForm from "@/components/Step1_FileUploadForm";
import Step2_ModeForm from "@/components/Step2_ModeForm";
import Step3_SplitForm from "@/components/Step3_SplitForm";
import Step4_StructureForm from "@/components/Step4_StructureForm";
import Step5_SchemaNoiseForm from "@/components/Step5_SchemaNoiseForm";
import Step6_DataNoiseForm from "@/components/Step6_DataNoiseForm";
import Step7_ShuffleForm from "@/components/Step7_ShuffelForm";
import Step8_SummaryForm from "@/components/Step8_SummaryForm";
import Step10_SuccessMessage from "@/components/Step10_SuccessMessage";
import SideBar from "@/components/SideBar";
import { useFormHandler } from "@/hooks/useFormHandler";
import { FormItems } from "@/components/types/formTypes";

export default function Home() {
    const initialValues: FormItems = {
        csvFile: null,
        hasHeaders: false,
        separator: null,
        quote: null,
        escape: null,

        jsonFile: null,
        manualInput: null,
        mode: null,

        splitType: null,
        rowOverlapPercentage: 0,
        columnOverlapPercentage: 0,
        rowDistribution: 0,
        columnDistribution: 0,
        overlapType: null,

        dataset1StructureType: null,
        dataset2StructureType: null,
        dataset1BCNFSliderValue: 0,
        dataset1JoinColumnsSliderValue: 0,
        dataset2BCNFSliderValue: 0,
        dataset2JoinColumnsSliderValue: 0,
        dataset3StructureType: null,
        dataset4StructureType: null,
        dataset3BCNFSliderValue: 0,
        dataset3JoinColumnsSliderValue: 0,
        dataset4BCNFSliderValue: 0,
        dataset4JoinColumnsSliderValue: 0,

        dataset1SchemaNoise: false,
        dataset1SchemaNoiseValue: 0,
        dataset1SchemaKeyNoise: false,
        dataset1SchemaDeleteSchema: false,
        dataset1SchemaMultiselect: [],

        dataset2SchemaNoise: false,
        dataset2SchemaNoiseValue: 0,
        dataset2SchemaKeyNoise: false,
        dataset2SchemaDeleteSchema: false,
        dataset2SchemaMultiselect: [],

        dataset3SchemaNoise: false,
        dataset3SchemaNoiseValue: 0,
        dataset3SchemaKeyNoise: false,
        dataset3SchemaDeleteSchema: false,
        dataset3SchemaMultiselect: [],

        dataset4SchemaNoise: false,
        dataset4SchemaNoiseValue: 0,
        dataset4SchemaKeyNoise: false,
        dataset4SchemaDeleteSchema: false,
        dataset4SchemaMultiselect: [],

        dataset1DataNoise: false,
        dataset1DataNoiseValue: 0,
        dataset1DataKeyNoise: false,
        dataset1DataNoiseInside: 0,
        dataset1DataMultiselect: [],

        dataset2DataNoise: false,
        dataset2DataNoiseValue: 0,
        dataset2DataKeyNoise: false,
        dataset2DataNoiseInside: 0,
        dataset2DataMultiselect: [],

        dataset3DataNoise: false,
        dataset3DataNoiseValue: 0,
        dataset3DataKeyNoise: false,
        dataset3DataNoiseInside: 0,
        dataset3DataMultiselect: [],

        dataset4DataNoise: false,
        dataset4DataNoiseValue: 0,
        dataset4DataKeyNoise: false,
        dataset4DataNoiseInside: 0,
        dataset4DataMultiselect: [],

        dataset1ShuffleOption: null,
        dataset2ShuffleOption: null,
        dataset3ShuffleOption: null,
        dataset4ShuffleOption: null,
    };

    const {
        formData,
        errors,
        showSuccessMsg,
        isGenerating,
        downloadUrl,
        currentStepIndex,
        isFirstStep,
        isLastStep,
        previousStep,
        goTo,
        startForm,
        isIntroScreen,
        updateForm,
        handleOnSubmit,
    } = useFormHandler(initialValues, 8);

    return (
        <div className="flex justify-center items-center w-full min-h-screen bg-[#0d0f12]">
            {isIntroScreen ? (
                <Step0_Start onStart={startForm} />
            ) : (
                <div
                    className={`flex gap-12
                    w-full max-w-7xl relative min-h-[1200px] m-1 rounded-lg border border-neutral-700 bg-[#262626] p-4`}
                >
                    {!showSuccessMsg && (
                        <SideBar currentStepIndex={currentStepIndex} goTo={goTo} />
                    )}
                    <main className="w-full md:mt-5 md:w-[100%] flex flex-col justify-between">
                        {showSuccessMsg ? (
                            <AnimatePresence mode="wait">
                                {/* Ensure downloadUrl is passed correctly */}
                                <Step10_SuccessMessage downloadUrl={downloadUrl} />
                            </AnimatePresence>
                        ) : (
                            <form
                                onSubmit={handleOnSubmit}
                                className="w-full flex flex-col justify-between flex-grow p-5"
                            >
                                <AnimatePresence mode="wait">
                                    {currentStepIndex === 0 && (
                                        <Step1_FileUploadForm
                                            csvFile={formData.csvFile}
                                            hasHeaders={formData.hasHeaders}
                                            separator={formData.separator}
                                            quote={formData.quote}
                                            escape={formData.escape}
                                            updateForm={updateForm}
                                            errors={errors}
                                        />
                                    )}
                                    {currentStepIndex === 1 && (
                                        <Step2_ModeForm

                                            mode={formData.mode}
                                            jsonFile={formData.jsonFile}
                                            manualInput={formData.manualInput}
                                            updateForm={updateForm}
                                            errors={errors}
                                        />
                                    )}
                                    {currentStepIndex === 2 && (
                                        <Step3_SplitForm
                                            splitType={formData.splitType}
                                            columnOverlapPercentage={formData.columnOverlapPercentage}
                                            rowOverlapPercentage={formData.rowOverlapPercentage}
                                            rowDistribution={formData.rowDistribution}
                                            columnDistribution={formData.columnDistribution}
                                            overlapType={formData.overlapType}
                                            updateForm={updateForm}
                                            errors={errors}
                                        />
                                    )}
                                    {currentStepIndex === 3 && (
                                        <Step4_StructureForm
                                            splitType={formData.splitType}
                                            dataset1StructureType={formData.dataset1StructureType}
                                            dataset2StructureType={formData.dataset2StructureType}
                                            dataset3StructureType={formData.dataset3StructureType}
                                            dataset4StructureType={formData.dataset4StructureType}
                                            dataset1BCNFSliderValue={formData.dataset1BCNFSliderValue}
                                            dataset1JoinColumnsSliderValue={formData.dataset1JoinColumnsSliderValue}
                                            dataset2BCNFSliderValue={formData.dataset2BCNFSliderValue}
                                            dataset2JoinColumnsSliderValue={formData.dataset2JoinColumnsSliderValue}
                                            dataset3BCNFSliderValue={formData.dataset3BCNFSliderValue}
                                            dataset3JoinColumnsSliderValue={formData.dataset3JoinColumnsSliderValue}
                                            dataset4BCNFSliderValue={formData.dataset4BCNFSliderValue}
                                            dataset4JoinColumnsSliderValue={formData.dataset4JoinColumnsSliderValue}
                                            updateForm={updateForm}
                                            errors={errors}
                                        />
                                    )}

                                    {currentStepIndex === 4 && (
                                        <Step5_SchemaNoiseForm
                                            splitType={formData.splitType}
                                            dataset1SchemaNoise={formData.dataset1SchemaNoise}
                                            dataset1SchemaNoiseValue={formData.dataset1SchemaNoiseValue}
                                            dataset1SchemaKeyNoise={formData.dataset1SchemaKeyNoise}
                                            dataset1SchemaDeleteSchema={formData.dataset1SchemaDeleteSchema}
                                            dataset1SchemaMultiselect={formData.dataset1SchemaMultiselect}

                                            dataset2SchemaNoise={formData.dataset2SchemaNoise}
                                            dataset2SchemaNoiseValue={formData.dataset2SchemaNoiseValue}
                                            dataset2SchemaKeyNoise={formData.dataset2SchemaKeyNoise}
                                            dataset2SchemaDeleteSchema={formData.dataset2SchemaDeleteSchema}
                                            dataset2SchemaMultiselect={formData.dataset2SchemaMultiselect}

                                            dataset3SchemaNoise={formData.dataset3SchemaNoise}
                                            dataset3SchemaNoiseValue={formData.dataset3SchemaNoiseValue}
                                            dataset3SchemaKeyNoise={formData.dataset3SchemaKeyNoise}
                                            dataset3SchemaDeleteSchema={formData.dataset3SchemaDeleteSchema}
                                            dataset3SchemaMultiselect={formData.dataset3SchemaMultiselect}

                                            dataset4SchemaNoise={formData.dataset4SchemaNoise}
                                            dataset4SchemaNoiseValue={formData.dataset4SchemaNoiseValue}
                                            dataset4SchemaKeyNoise={formData.dataset4SchemaKeyNoise}
                                            dataset4SchemaDeleteSchema={formData.dataset4SchemaDeleteSchema}
                                            dataset4SchemaMultiselect={formData.dataset4SchemaMultiselect}

                                            updateForm={updateForm}
                                            errors={errors}
                                        />
                                    )}
                                    {currentStepIndex === 5 && (
                                        <Step6_DataNoiseForm
                                            splitType={formData.splitType}

                                            dataset1DataNoise={formData.dataset1DataNoise}
                                            dataset1DataNoiseValue={formData.dataset1DataNoiseValue}
                                            dataset1DataKeyNoise={formData.dataset1DataKeyNoise}
                                            dataset1DataNoiseInside={formData.dataset1DataNoiseInside}
                                            dataset1DataMultiselect={formData.dataset1DataMultiselect}

                                            dataset2DataNoise={formData.dataset2DataNoise}
                                            dataset2DataNoiseValue={formData.dataset2DataNoiseValue}
                                            dataset2DataKeyNoise={formData.dataset2DataKeyNoise}
                                            dataset2DataNoiseInside={formData.dataset2DataNoiseInside}
                                            dataset2DataMultiselect={formData.dataset2DataMultiselect}

                                            dataset3DataNoise={formData.dataset3DataNoise}
                                            dataset3DataNoiseValue={formData.dataset3DataNoiseValue}
                                            dataset3DataKeyNoise={formData.dataset3DataKeyNoise}
                                            dataset3DataNoiseInside={formData.dataset3DataNoiseInside}
                                            dataset3DataMultiselect={formData.dataset3DataMultiselect}

                                            dataset4DataNoise={formData.dataset4DataNoise}
                                            dataset4DataNoiseValue={formData.dataset4DataNoiseValue}
                                            dataset4DataKeyNoise={formData.dataset4DataKeyNoise}
                                            dataset4DataNoiseInside={formData.dataset4DataNoiseInside}
                                            dataset4DataMultiselect={formData.dataset4DataMultiselect}

                                            updateForm={updateForm}
                                            errors={errors}
                                        />
                                    )}
                                    {currentStepIndex === 6 && (
                                        <Step7_ShuffleForm
                                            splitType={formData.splitType}
                                            dataset1ShuffleOption={formData.dataset1ShuffleOption}
                                            dataset2ShuffleOption={formData.dataset2ShuffleOption}
                                            dataset3ShuffleOption={formData.dataset3ShuffleOption}
                                            dataset4ShuffleOption={formData.dataset4ShuffleOption}
                                            updateForm={updateForm}
                                            errors={errors}
                                        />
                                    )}
                                    {currentStepIndex === 7 && (
                                        <Step8_SummaryForm
                                            csvFile={formData.csvFile}
                                            hasHeaders={formData.hasHeaders}
                                            separator={formData.separator}
                                            quote={formData.quote}
                                            escape={formData.escape}

                                            jsonFile={formData.jsonFile}
                                            manualInput={formData.manualInput}
                                            mode={formData.mode}

                                            splitType={formData.splitType}
                                            columnOverlapPercentage={formData.columnOverlapPercentage}
                                            rowOverlapPercentage={formData.rowOverlapPercentage}
                                            rowDistribution={formData.rowDistribution}
                                            columnDistribution={formData.columnDistribution}
                                            overlapType={formData.overlapType}

                                            dataset1StructureType={formData.dataset1StructureType}
                                            dataset2StructureType={formData.dataset2StructureType}
                                            dataset1BCNFSliderValue={formData.dataset1BCNFSliderValue}
                                            dataset1JoinColumnsSliderValue={formData.dataset1JoinColumnsSliderValue}
                                            dataset2BCNFSliderValue={formData.dataset2BCNFSliderValue}
                                            dataset2JoinColumnsSliderValue={formData.dataset2JoinColumnsSliderValue}
                                            dataset3StructureType={formData.dataset3StructureType}
                                            dataset4StructureType={formData.dataset4StructureType}
                                            dataset3BCNFSliderValue={formData.dataset3BCNFSliderValue}
                                            dataset3JoinColumnsSliderValue={formData.dataset3JoinColumnsSliderValue}
                                            dataset4BCNFSliderValue={formData.dataset4BCNFSliderValue}
                                            dataset4JoinColumnsSliderValue={formData.dataset4JoinColumnsSliderValue}

                                            dataset1SchemaNoise={formData.dataset1SchemaNoise}
                                            dataset1SchemaNoiseValue={formData.dataset1SchemaNoiseValue}
                                            dataset1SchemaKeyNoise={formData.dataset1SchemaKeyNoise}
                                            dataset1SchemaDeleteSchema={formData.dataset1SchemaDeleteSchema}
                                            dataset1SchemaMultiselect={formData.dataset1SchemaMultiselect}

                                            dataset2SchemaNoise={formData.dataset2SchemaNoise}
                                            dataset2SchemaNoiseValue={formData.dataset2SchemaNoiseValue}
                                            dataset2SchemaKeyNoise={formData.dataset2SchemaKeyNoise}
                                            dataset2SchemaDeleteSchema={formData.dataset2SchemaDeleteSchema}
                                            dataset2SchemaMultiselect={formData.dataset2SchemaMultiselect}

                                            dataset3SchemaNoise={formData.dataset3SchemaNoise}
                                            dataset3SchemaNoiseValue={formData.dataset3SchemaNoiseValue}
                                            dataset3SchemaKeyNoise={formData.dataset3SchemaKeyNoise}
                                            dataset3SchemaDeleteSchema={formData.dataset3SchemaDeleteSchema}
                                            dataset3SchemaMultiselect={formData.dataset3SchemaMultiselect}

                                            dataset4SchemaNoise={formData.dataset4SchemaNoise}
                                            dataset4SchemaNoiseValue={formData.dataset4SchemaNoiseValue}
                                            dataset4SchemaKeyNoise={formData.dataset4SchemaKeyNoise}
                                            dataset4SchemaDeleteSchema={formData.dataset4SchemaDeleteSchema}
                                            dataset4SchemaMultiselect={formData.dataset4SchemaMultiselect}

                                            dataset1DataNoise={formData.dataset1DataNoise}
                                            dataset1DataNoiseValue={formData.dataset1DataNoiseValue}
                                            dataset1DataKeyNoise={formData.dataset1DataKeyNoise}
                                            dataset1DataNoiseInside={formData.dataset1DataNoiseInside}
                                            dataset1DataMultiselect={formData.dataset1DataMultiselect}

                                            dataset2DataNoise={formData.dataset2DataNoise}
                                            dataset2DataNoiseValue={formData.dataset2DataNoiseValue}
                                            dataset2DataKeyNoise={formData.dataset2DataKeyNoise}
                                            dataset2DataNoiseInside={formData.dataset2DataNoiseInside}
                                            dataset2DataMultiselect={formData.dataset2DataMultiselect}

                                            dataset3DataNoise={formData.dataset3DataNoise}
                                            dataset3DataNoiseValue={formData.dataset3DataNoiseValue}
                                            dataset3DataKeyNoise={formData.dataset3DataKeyNoise}
                                            dataset3DataNoiseInside={formData.dataset3DataNoiseInside}
                                            dataset3DataMultiselect={formData.dataset3DataMultiselect}

                                            dataset4DataNoise={formData.dataset4DataNoise}
                                            dataset4DataNoiseValue={formData.dataset4DataNoiseValue}
                                            dataset4DataKeyNoise={formData.dataset4DataKeyNoise}
                                            dataset4DataNoiseInside={formData.dataset4DataNoiseInside}
                                            dataset4DataMultiselect={formData.dataset4DataMultiselect}

                                            dataset1ShuffleOption={formData.dataset1ShuffleOption}
                                            dataset2ShuffleOption={formData.dataset2ShuffleOption}
                                            dataset3ShuffleOption={formData.dataset3ShuffleOption}
                                            dataset4ShuffleOption={formData.dataset4ShuffleOption}
                                            goTo={goTo}
                                        />
                                    )}
                                </AnimatePresence>
                                <div className="w-full flex justify-between mt-5">
                                    <div>
                                        <Button
                                            onClick={previousStep}
                                            type="button"
                                            variant="ghost"
                                            className={`${
                                                isFirstStep
                                                    ? "invisible"
                                                    : "visible p-0 text-neutral-200 hover:text-white"
                                            }`}
                                        >
                                            Go Back
                                        </Button>
                                    </div>
                                    <div className="flex items-center">
                                        <div className="relative after:pointer-events-none after:absolute after:inset-px after:rounded-[11px] after:shadow-highlight after:shadow-white/10 focus-within:after:shadow-[#77f6aa] after:transition">
                                            <Button
                                                type="submit"
                                                className="relative text-neutral-200 bg-neutral-900 border border-black/20 shadow-input shadow-black/10 rounded-xl hover:text-white"
                                            >
                                                {isLastStep ? "Confirm" : "Next Step"}
                                            </Button>
                                        </div>
                                    </div>
                                </div>
                            </form>
                        )}
                    </main>
                </div>
            )}
        </div>
    );
}
