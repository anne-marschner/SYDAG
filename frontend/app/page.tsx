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
import Step9_Generating from "@/components/Step9_Generating";
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

        datasetAStructureType: null,
        datasetBStructureType: null,
        datasetABCNFSliderValue: 0,
        datasetAMergeColumnsSliderValue: 0,
        datasetBBCNFSliderValue: 0,
        datasetBMergeColumnsSliderValue: 0,
        datasetCStructureType: null,
        datasetDStructureType: null,
        datasetCBCNFSliderValue: 0,
        datasetCMergeColumnsSliderValue: 0,
        datasetDBCNFSliderValue: 0,
        datasetDMergeColumnsSliderValue: 0,

        datasetASchemaNoise: false,
        datasetASchemaNoiseValue: 0,
        datasetASchemaKeyNoise: false,
        datasetASchemaDeleteSchema: false,
        datasetASchemaMultiselect: [],

        datasetBSchemaNoise: false,
        datasetBSchemaNoiseValue: 0,
        datasetBSchemaKeyNoise: false,
        datasetBSchemaDeleteSchema: false,
        datasetBSchemaMultiselect: [],

        datasetCSchemaNoise: false,
        datasetCSchemaNoiseValue: 0,
        datasetCSchemaKeyNoise: false,
        datasetCSchemaDeleteSchema: false,
        datasetCSchemaMultiselect: [],

        datasetDSchemaNoise: false,
        datasetDSchemaNoiseValue: 0,
        datasetDSchemaKeyNoise: false,
        datasetDSchemaDeleteSchema: false,
        datasetDSchemaMultiselect: [],

        datasetADataNoise: false,
        datasetADataNoiseValue: 0,
        datasetADataKeyNoise: false,
        datasetADataNoiseInside: 0,
        datasetADataMultiselect: [],

        datasetBDataNoise: false,
        datasetBDataNoiseValue: 0,
        datasetBDataKeyNoise: false,
        datasetBDataNoiseInside: 0,
        datasetBDataMultiselect: [],

        datasetCDataNoise: false,
        datasetCDataNoiseValue: 0,
        datasetCDataKeyNoise: false,
        datasetCDataNoiseInside: 0,
        datasetCDataMultiselect: [],

        datasetDDataNoise: false,
        datasetDDataNoiseValue: 0,
        datasetDDataKeyNoise: false,
        datasetDDataNoiseInside: 0,
        datasetDDataMultiselect: [],

        datasetAShuffleOption: null,
        datasetBShuffleOption: null,
        datasetCShuffleOption: null,
        datasetDShuffleOption: null,
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
                    w-full max-w-7xl  h-[850px] m-1 rounded-lg border border-neutral-700 bg-[#262626] p-4`}
                >
                    {!showSuccessMsg && !isGenerating && (
                        <SideBar currentStepIndex={currentStepIndex} goTo={goTo} />
                    )}
                    <main className="w-full md:mt-5 md:w-[100%] flex flex-col justify-between">
                        {isGenerating ? (
                            <Step9_Generating />
                        ) : showSuccessMsg ? (
                            <AnimatePresence mode="wait">
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
                                            datasetAStructureType={formData.datasetAStructureType}
                                            datasetBStructureType={formData.datasetBStructureType}
                                            datasetCStructureType={formData.datasetCStructureType}
                                            datasetDStructureType={formData.datasetDStructureType}
                                            datasetABCNFSliderValue={formData.datasetABCNFSliderValue}
                                            datasetAMergeColumnsSliderValue={formData.datasetAMergeColumnsSliderValue}
                                            datasetBBCNFSliderValue={formData.datasetBBCNFSliderValue}
                                            datasetBMergeColumnsSliderValue={formData.datasetBMergeColumnsSliderValue}
                                            datasetCBCNFSliderValue={formData.datasetCBCNFSliderValue}
                                            datasetCMergeColumnsSliderValue={formData.datasetCMergeColumnsSliderValue}
                                            datasetDBCNFSliderValue={formData.datasetDBCNFSliderValue}
                                            datasetDMergeColumnsSliderValue={formData.datasetDMergeColumnsSliderValue}
                                            updateForm={updateForm}
                                            errors={errors}
                                        />
                                    )}
                                    {currentStepIndex === 4 && (
                                        <Step5_SchemaNoiseForm
                                            splitType={formData.splitType}
                                            datasetASchemaNoise={formData.datasetASchemaNoise}
                                            datasetASchemaNoiseValue={formData.datasetASchemaNoiseValue}
                                            datasetASchemaKeyNoise={formData.datasetASchemaKeyNoise}
                                            datasetASchemaDeleteSchema={formData.datasetASchemaDeleteSchema}
                                            datasetASchemaMultiselect={formData.datasetASchemaMultiselect}
                                            datasetBSchemaNoise={formData.datasetBSchemaNoise}
                                            datasetBSchemaNoiseValue={formData.datasetBSchemaNoiseValue}
                                            datasetBSchemaKeyNoise={formData.datasetBSchemaKeyNoise}
                                            datasetBSchemaDeleteSchema={formData.datasetBSchemaDeleteSchema}
                                            datasetBSchemaMultiselect={formData.datasetBSchemaMultiselect}
                                            datasetCSchemaNoise={formData.datasetCSchemaNoise}
                                            datasetCSchemaNoiseValue={formData.datasetCSchemaNoiseValue}
                                            datasetCSchemaKeyNoise={formData.datasetCSchemaKeyNoise}
                                            datasetCSchemaDeleteSchema={formData.datasetCSchemaDeleteSchema}
                                            datasetCSchemaMultiselect={formData.datasetCSchemaMultiselect}
                                            datasetDSchemaNoise={formData.datasetDSchemaNoise}
                                            datasetDSchemaNoiseValue={formData.datasetDSchemaNoiseValue}
                                            datasetDSchemaKeyNoise={formData.datasetDSchemaKeyNoise}
                                            datasetDSchemaDeleteSchema={formData.datasetDSchemaDeleteSchema}
                                            datasetDSchemaMultiselect={formData.datasetDSchemaMultiselect}
                                            updateForm={updateForm}
                                            errors={errors}
                                        />
                                    )}
                                    {currentStepIndex === 5 && (
                                        <Step6_DataNoiseForm
                                            splitType={formData.splitType}
                                            datasetADataNoise={formData.datasetADataNoise}
                                            datasetADataNoiseValue={formData.datasetADataNoiseValue}
                                            datasetADataKeyNoise={formData.datasetADataKeyNoise}
                                            datasetADataNoiseInside={formData.datasetADataNoiseInside}
                                            datasetADataMultiselect={formData.datasetADataMultiselect}
                                            datasetBDataNoise={formData.datasetBDataNoise}
                                            datasetBDataNoiseValue={formData.datasetBDataNoiseValue}
                                            datasetBDataKeyNoise={formData.datasetBDataKeyNoise}
                                            datasetBDataNoiseInside={formData.datasetBDataNoiseInside}
                                            datasetBDataMultiselect={formData.datasetBDataMultiselect}
                                            datasetCDataNoise={formData.datasetCDataNoise}
                                            datasetCDataNoiseValue={formData.datasetCDataNoiseValue}
                                            datasetCDataKeyNoise={formData.datasetCDataKeyNoise}
                                            datasetCDataNoiseInside={formData.datasetCDataNoiseInside}
                                            datasetCDataMultiselect={formData.datasetCDataMultiselect}
                                            datasetDDataNoise={formData.datasetDDataNoise}
                                            datasetDDataNoiseValue={formData.datasetDDataNoiseValue}
                                            datasetDDataKeyNoise={formData.datasetDDataKeyNoise}
                                            datasetDDataNoiseInside={formData.datasetDDataNoiseInside}
                                            datasetDDataMultiselect={formData.datasetDDataMultiselect}
                                            updateForm={updateForm}
                                            errors={errors}
                                        />
                                    )}
                                    {currentStepIndex === 6 && (
                                        <Step7_ShuffleForm
                                            splitType={formData.splitType}
                                            datasetAShuffleOption={formData.datasetAShuffleOption}
                                            datasetBShuffleOption={formData.datasetBShuffleOption}
                                            datasetCShuffleOption={formData.datasetCShuffleOption}
                                            datasetDShuffleOption={formData.datasetDShuffleOption}
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
                                            datasetAStructureType={formData.datasetAStructureType}
                                            datasetBStructureType={formData.datasetBStructureType}
                                            datasetABCNFSliderValue={formData.datasetABCNFSliderValue}
                                            datasetAMergeColumnsSliderValue={formData.datasetAMergeColumnsSliderValue}
                                            datasetBBCNFSliderValue={formData.datasetBBCNFSliderValue}
                                            datasetBMergeColumnsSliderValue={formData.datasetBMergeColumnsSliderValue}
                                            datasetCStructureType={formData.datasetCStructureType}
                                            datasetDStructureType={formData.datasetDStructureType}
                                            datasetCBCNFSliderValue={formData.datasetCBCNFSliderValue}
                                            datasetCMergeColumnsSliderValue={formData.datasetCMergeColumnsSliderValue}
                                            datasetDBCNFSliderValue={formData.datasetDBCNFSliderValue}
                                            datasetDMergeColumnsSliderValue={formData.datasetDMergeColumnsSliderValue}
                                            datasetASchemaNoise={formData.datasetASchemaNoise}
                                            datasetASchemaNoiseValue={formData.datasetASchemaNoiseValue}
                                            datasetASchemaKeyNoise={formData.datasetASchemaKeyNoise}
                                            datasetASchemaDeleteSchema={formData.datasetASchemaDeleteSchema}
                                            datasetASchemaMultiselect={formData.datasetASchemaMultiselect}
                                            datasetBSchemaNoise={formData.datasetBSchemaNoise}
                                            datasetBSchemaNoiseValue={formData.datasetBSchemaNoiseValue}
                                            datasetBSchemaKeyNoise={formData.datasetBSchemaKeyNoise}
                                            datasetBSchemaDeleteSchema={formData.datasetBSchemaDeleteSchema}
                                            datasetBSchemaMultiselect={formData.datasetBSchemaMultiselect}
                                            datasetCSchemaNoise={formData.datasetCSchemaNoise}
                                            datasetCSchemaNoiseValue={formData.datasetCSchemaNoiseValue}
                                            datasetCSchemaKeyNoise={formData.datasetCSchemaKeyNoise}
                                            datasetCSchemaDeleteSchema={formData.datasetCSchemaDeleteSchema}
                                            datasetCSchemaMultiselect={formData.datasetCSchemaMultiselect}
                                            datasetDSchemaNoise={formData.datasetDSchemaNoise}
                                            datasetDSchemaNoiseValue={formData.datasetDSchemaNoiseValue}
                                            datasetDSchemaKeyNoise={formData.datasetDSchemaKeyNoise}
                                            datasetDSchemaDeleteSchema={formData.datasetDSchemaDeleteSchema}
                                            datasetDSchemaMultiselect={formData.datasetDSchemaMultiselect}
                                            datasetADataNoise={formData.datasetADataNoise}
                                            datasetADataNoiseValue={formData.datasetADataNoiseValue}
                                            datasetADataKeyNoise={formData.datasetADataKeyNoise}
                                            datasetADataNoiseInside={formData.datasetADataNoiseInside}
                                            datasetADataMultiselect={formData.datasetADataMultiselect}
                                            datasetBDataNoise={formData.datasetBDataNoise}
                                            datasetBDataNoiseValue={formData.datasetBDataNoiseValue}
                                            datasetBDataKeyNoise={formData.datasetBDataKeyNoise}
                                            datasetBDataNoiseInside={formData.datasetBDataNoiseInside}
                                            datasetBDataMultiselect={formData.datasetBDataMultiselect}
                                            datasetCDataNoise={formData.datasetCDataNoise}
                                            datasetCDataNoiseValue={formData.datasetCDataNoiseValue}
                                            datasetCDataKeyNoise={formData.datasetCDataKeyNoise}
                                            datasetCDataNoiseInside={formData.datasetCDataNoiseInside}
                                            datasetCDataMultiselect={formData.datasetCDataMultiselect}
                                            datasetDDataNoise={formData.datasetDDataNoise}
                                            datasetDDataNoiseValue={formData.datasetDDataNoiseValue}
                                            datasetDDataKeyNoise={formData.datasetDDataKeyNoise}
                                            datasetDDataNoiseInside={formData.datasetDDataNoiseInside}
                                            datasetDDataMultiselect={formData.datasetDDataMultiselect}
                                            datasetAShuffleOption={formData.datasetAShuffleOption}
                                            datasetBShuffleOption={formData.datasetBShuffleOption}
                                            datasetCShuffleOption={formData.datasetCShuffleOption}
                                            datasetDShuffleOption={formData.datasetDShuffleOption}
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
                                        <div className="relative after:pointer-events-none after:absolute after:inset-px after:rounded-[11px] after:shadow-highlight after:shadow-white/A0 focus-within:after:shadow-[#77f6aa] after:transition">
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