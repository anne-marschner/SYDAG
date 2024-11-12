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
import Step7_ShuffelForm from "@/components/Step7_ShuffelForm";
import Step8_SummaryForm from "@/components/Step8_SummaryForm";
import Step9_SuccessMessage from "@/components/Step9_SuccessMessage";
import SideBar from "@/components/SideBar";
import { useFormHandler } from "@/hooks/useFormHandler";
import { FormItems } from "@/components/types/formTypes";

export default function Home() {

    const initialValues: FormItems = {
        csvFile: null,
        hasHeaders: false,
        separator: null,
        jsonFile: null,
        manualInput: null,
        mode: null,
        splitType: null,
        rowOverlapPercentage:0,
        columnOverlapPercentage:0,

        dataset1StructureType: null,
        dataset2StructureType: null,

        dataset1SchemaNoise: false,
        dataset1SchemaNoiseValue: 0,
        dataset1SchemaKeyNoise: false,
        
        dataset2SchemaNoise: false,
        dataset2SchemaNoiseValue: 0,
        dataset2SchemaKeyNoise: false,

        dataset1DataNoise: false,
        dataset1DataNoiseValue: 0,
        dataset2DataNoise: false,
        dataset2DataNoiseValue: 0,
        dataset1DataKeyNoise: false,
        dataset2DataKeyNoise: false,

        dataset1ShuffleOption: null,
        dataset2ShuffleOption: null,
    };

    const {
        formData,
        errors,
        showSuccessMsg,
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
        <div className="flex justify-center items-center w-full h-screen bg-[#0d0f12]">
            {isIntroScreen ? (
                <Step0_Start onStart={startForm} />
            ) : (
                <div
                    className={`flex gap-12
                    h-[700px] w-11/12 max-w-4xl relative m-1 rounded-lg border border-neutral-700 bg-[#262626] p-4`}
                >
                    {!showSuccessMsg && (
                        <SideBar currentStepIndex={currentStepIndex} goTo={goTo} />
                    )}
                    <main
                        className={`${
                            showSuccessMsg ? "w-full" : "w-full md:mt-5 md:w-[75%]"
                        }`}
                    >
                        {showSuccessMsg ? (
                            <AnimatePresence mode="wait">
                                <Step9_SuccessMessage />
                            </AnimatePresence>
                        ) : (
                            <form
                                onSubmit={handleOnSubmit}
                                className="w-full flex flex-col justify-between h-full p-5"
                            >
                                <AnimatePresence mode="wait">
                                    {currentStepIndex === 0 && (
                                        <Step1_FileUploadForm
                                            csvFile={formData.csvFile}
                                            hasHeaders={formData.hasHeaders}
                                            separator={formData.separator}
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
                                            rowOverlapPercentage={formData.rowOverlapPercentage}
                                            columnOverlapPercentage={formData.columnOverlapPercentage}
                                            updateForm={updateForm}
                                            errors={errors}
                                        />
                                    )}
                                    {currentStepIndex === 3 && (
                                        <Step4_StructureForm
                                            dataset1StructureType={formData.dataset1StructureType}
                                            dataset2StructureType={formData.dataset2StructureType}
                                            updateForm={updateForm}
                                            errors={errors}
                                        />
                                    )}
                                    {currentStepIndex === 4 && (
                                        <Step5_SchemaNoiseForm
                                            dataset1SchemaNoise={formData.dataset1SchemaNoise}
                                            dataset1SchemaNoiseValue={formData.dataset1SchemaNoiseValue}
                                            dataset2SchemaNoise={formData.dataset2SchemaNoise}
                                            dataset2SchemaNoiseValue={formData.dataset2SchemaNoiseValue}
                                            dataset1SchemaKeyNoise={formData.dataset1SchemaKeyNoise}
                                            dataset2SchemaKeyNoise={formData.dataset2SchemaKeyNoise}
                                            updateForm={updateForm}
                                            errors={errors}
                                        />
                                    )}
                                    {currentStepIndex === 5 && (
                                        <Step6_DataNoiseForm
                                            dataset1DataNoise={formData.dataset1DataNoise}
                                            dataset1DataNoiseValue={formData.dataset1DataNoiseValue}
                                            dataset2DataNoise={formData.dataset2DataNoise}
                                            dataset2DataNoiseValue={formData.dataset2DataNoiseValue}
                                            dataset1DataKeyNoise={formData.dataset1DataKeyNoise}
                                            dataset2DataKeyNoise={formData.dataset2DataKeyNoise}
                                            updateForm={updateForm}
                                            errors={errors}
                                        />
                                    )}
                                    {currentStepIndex === 6 && (
                                        <Step7_ShuffelForm
                                            dataset1ShuffleOption={formData.dataset1ShuffleOption}
                                            dataset2ShuffleOption={formData.dataset2ShuffleOption}
                                            updateForm={updateForm}
                                            errors={errors}
                                        />
                                    )}
                                    {currentStepIndex === 7 && (
                                        <Step8_SummaryForm
                                            csvFile={formData.csvFile}
                                            hasHeaders={formData.hasHeaders}
                                            separator={formData.separator}
                                            jsonFile={formData.jsonFile}
                                            manualInput={formData.manualInput}
                                            mode={formData.mode}
                                            splitType={formData.splitType}
                                            columnOverlapPercentage={formData.columnOverlapPercentage}
                                            rowOverlapPercentage={formData.rowOverlapPercentage}
                                            dataset1StructureType={formData.dataset1StructureType}
                                            dataset2StructureType={formData.dataset2StructureType}

                                            dataset1SchemaNoise={formData.dataset1SchemaNoise}
                                            dataset1SchemaNoiseValue={formData.dataset1SchemaNoiseValue}
                                            dataset2SchemaNoise={formData.dataset2SchemaNoise}
                                            dataset2SchemaNoiseValue={formData.dataset2SchemaNoiseValue}
                                            dataset1SchemaKeyNoise={formData.dataset1SchemaKeyNoise}
                                            dataset2SchemaKeyNoise={formData.dataset2SchemaKeyNoise}

                                            dataset1DataNoise={formData.dataset1DataNoise}
                                            dataset1DataNoiseValue={formData.dataset1DataNoiseValue}
                                            dataset2DataNoise={formData.dataset2DataNoise}
                                            dataset2DataNoiseValue={formData.dataset2DataNoiseValue}
                                            dataset1DataKeyNoise={formData.dataset1DataKeyNoise}
                                            dataset2DataKeyNoise={formData.dataset2DataKeyNoise}

                                            dataset1ShuffleOption={formData.dataset1ShuffleOption}
                                            dataset2ShuffleOption={formData.dataset2ShuffleOption}
                                            goTo={goTo}
                                         />
                                    )}
                                </AnimatePresence>
                                <div className="w-full items-center flex justify-between mt-5">
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
