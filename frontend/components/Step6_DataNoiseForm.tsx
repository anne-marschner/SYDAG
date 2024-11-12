"use client";
import { useState } from "react";
import { Checkbox } from "@/components/ui/checkbox";
import { Slider } from "@/components/ui/slider";
import FormWrapper from "./FormWrapper";
import { FormItems } from "@/components/types/formTypes";

// Defining the types for the props passed to the DataNoiseForm component.
type StepProps = {
    dataset1DataNoise: boolean | null;
    dataset1DataNoiseValue: number | null;
    dataset1DataKeyNoise: boolean | null;
    dataset2DataNoise: boolean | null;
    dataset2DataNoiseValue: number | null;
    dataset2DataKeyNoise: boolean | null;
    updateForm: (fieldToUpdate: Partial<FormItems>) => void; // Function to update form fields.
    errors: Record<string, string[]>;
};

const Step6_DataNoiseForm = ({
    updateForm,
    dataset1DataNoise,
    dataset1DataNoiseValue,
    dataset1DataKeyNoise,
    dataset2DataNoise,
    dataset2DataNoiseValue,
    dataset2DataKeyNoise,
    errors,
}: StepProps) => {
    // State management for checkbox and slider values for both datasets
    const [dataset1NoiseEnabled, setDataset1NoiseEnabled] = useState<boolean | null>(dataset1DataNoise);
    const [dataset1NoiseLevel, setDataset1NoiseLevel] = useState<number | null>(dataset1DataNoiseValue);
    const [dataset1KeyNoiseEnabled, setDataset1KeyNoiseEnabled] = useState<boolean | null>(dataset1DataKeyNoise);

    const [dataset2NoiseEnabled, setDataset2NoiseEnabled] = useState<boolean | null>(dataset2DataNoise);
    const [dataset2NoiseLevel, setDataset2NoiseLevel] = useState<number | null>(dataset2DataNoiseValue);
    const [dataset2KeyNoiseEnabled, setDataset2KeyNoiseEnabled] = useState<boolean | null>(dataset2DataKeyNoise);


    // Handler functions for the checkbox and slider value changes
    const handleDataset1NoiseToggle = (checked: boolean) => {
        setDataset1NoiseEnabled(checked);
        if (!checked) {
            setDataset1NoiseLevel(0);
            setDataset1KeyNoiseEnabled(false);
            updateForm({
                dataset1DataNoise: checked,
                dataset1DataNoiseValue: 0,
                dataset1DataKeyNoise: false,
            });
        } else {
            updateForm({ dataset1DataNoise: checked });
        }
    };

    const handleDataset1SliderChange = (value: number[]) => {
        const newLevel = value[0];
        setDataset1NoiseLevel(newLevel);
        updateForm({ dataset1DataNoiseValue: newLevel });
    };

    const handleDataset1KeyNoiseToggle = (checked: boolean) => {
        setDataset1KeyNoiseEnabled(checked);
        updateForm({ dataset1DataKeyNoise: checked });
    };

    const handleDataset2NoiseToggle = (checked: boolean) => {
        setDataset2NoiseEnabled(checked);
        if (!checked) {
            setDataset2NoiseLevel(0);
            setDataset2KeyNoiseEnabled(false);
            updateForm({
                dataset2DataNoise: checked,
                dataset2DataNoiseValue: 0,
                dataset2DataKeyNoise: false,
            });
        } else {
            updateForm({ dataset2DataNoise: checked });
        }
    };

    const handleDataset2SliderChange = (value: number[]) => {
        const newLevel = value[0];
        setDataset2NoiseLevel(newLevel);
        updateForm({ dataset2DataNoiseValue: newLevel });
    };

    const handleDataset2KeyNoiseToggle = (checked: boolean) => {
        setDataset2KeyNoiseEnabled(checked);
        updateForm({ dataset2DataKeyNoise: checked });
    };

    return (
        <FormWrapper
            title="Select Noise Options for Data"
            description="You can choose if you want to add noise into the data of each dataset"
        >
            <div className="flex flex-col md:flex-row md:gap-8 w-full">
                {/* First Checkbox and Slider */}
                <div className="flex flex-col w-full">
                    <h3 className="text-lg text-white mb-2">Dataset 1</h3>
                    <div className="flex items-center gap-2">
                        <Checkbox
                            checked={!!dataset1NoiseEnabled} // Ensure it's a boolean
                            onCheckedChange={handleDataset1NoiseToggle}
                            id="dataset1Noise"
                        />
                        <label htmlFor="dataset1Noise" className="text-white">
                            Enable Option
                        </label>
                    </div>
                    {errors.dataset1Noise && (
                        <p className="text-red-500 text-sm mt-1">{errors.dataset1Noise[0]}</p>
                    )}

                    {dataset1NoiseEnabled && (
                        <>
                            <div className="flex items-center gap-2 mt-2">
                                <Checkbox
                                    checked={!!dataset1KeyNoiseEnabled} // Ensure it's a boolean
                                    onCheckedChange={handleDataset1KeyNoiseToggle}
                                    id="dataset1DataKeyNoise"
                                />
                                <label htmlFor="dataset1DataKeyNoise" className="text-white">
                                Enable Data Noise in Key´s 
                                </label>
                            </div>
                            {errors.dataset1DataKeyNoise && (
                                <p className="text-red-500 text-sm mt-1">
                                    {errors.dataset1DataKeyNoise[0]}
                                </p>
                            )}

                            <Slider
                                className="my-4 w-full"
                                value={[dataset1NoiseLevel ?? 0]}
                                onValueChange={handleDataset1SliderChange}
                                min={0}
                                max={100}
                                step={1}
                            />
                            {/* Display the current value of the slider */}
                            <span className="text-white text-sm">
                                Percentage: {dataset1NoiseLevel ?? 0} %
                            </span>
                            {errors.dataset1NoiseValue && (
                                <p className="text-red-500 text-sm mt-1">
                                    {errors.dataset1NoiseValue[0]}
                                </p>
                            )}

                        </>
                    )}
                </div>

                {/* Second Checkbox and Slider */}
                <div className="flex flex-col w-full">
                    <h3 className="text-lg text-white mb-2">Dataset 2</h3>
                    <div className="flex items-center gap-2">
                        <Checkbox
                            checked={!!dataset2NoiseEnabled} // Ensure it's a boolean
                            onCheckedChange={handleDataset2NoiseToggle}
                            id="dataset2Noise"
                        />
                        <label htmlFor="dataset2Noise" className="text-white">
                            Enable Option
                        </label>
                    </div>
                    {errors.dataset2Noise && (
                        <p className="text-red-500 text-sm mt-1">{errors.dataset2Noise[0]}</p>
                    )}

                    {dataset2NoiseEnabled && (
                        <>
                            <div className="flex items-center gap-2 mt-2">
                                <Checkbox
                                    checked={!!dataset2KeyNoiseEnabled} // Ensure it's a boolean
                                    onCheckedChange={handleDataset2KeyNoiseToggle}
                                    id="dataset2DataKeyNoise"
                                />
                                <label htmlFor="dataset2DataKeyNoise" className="text-white">
                                    Enable Data Noise in Key´s 
                                </label>
                            </div>
                            {errors.dataset2DataKeyNoise && (
                                <p className="text-red-500 text-sm mt-1">
                                    {errors.dataset2DataKeyNoise[0]}
                                </p>
                            )}

                            <Slider
                                className="my-4 w-full"
                                value={[dataset2NoiseLevel ?? 0]}
                                onValueChange={handleDataset2SliderChange}
                                min={0}
                                max={100}
                                step={1}
                            />
                            {/* Display the current value of the slider */}
                            <span className="text-white text-sm">
                                Percentage: {dataset2NoiseLevel ?? 0} %
                            </span>
                            {errors.dataset2NoiseValue && (
                                <p className="text-red-500 text-sm mt-1">
                                    {errors.dataset2NoiseValue[0]}
                                </p>
                            )}

                        </>
                    )}
                </div>
            </div>


        </FormWrapper>
    );
};

export default Step6_DataNoiseForm;
