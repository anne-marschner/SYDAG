"use client";
import { useState } from "react";
import { Checkbox } from "@/components/ui/checkbox";
import { Slider } from "@/components/ui/slider";
import FormWrapper from "./FormWrapper";
import { FormItems } from "@/components/types/formTypes";

// Defining the types for the props passed to the SchemaNoiseForm component.
type StepProps = {
    dataset1SchemaNoise: boolean | null;
    dataset1SchemaNoiseValue: number | null;
    dataset1SchemaKeyNoise: boolean | null;
    dataset2SchemaNoise: boolean | null;
    dataset2SchemaNoiseValue: number | null;
    dataset2SchemaKeyNoise: boolean | null;
    updateForm: (fieldToUpdate: Partial<FormItems>) => void; // Function to update form fields.
    errors: Record<string, string[]>;
};

const Step5_SchemaNoiseForm = ({
    updateForm,
    dataset1SchemaNoise,
    dataset1SchemaNoiseValue,
    dataset1SchemaKeyNoise,
    dataset2SchemaNoise,
    dataset2SchemaNoiseValue,
    dataset2SchemaKeyNoise,
    errors,
}: StepProps) => {
    // Initialize state directly from props
    const [dataset1SchemaEnabled, setDataset1SchemaEnabled] = useState<boolean | null>(dataset1SchemaNoise);
    const [dataset1SchemaLevel, setDataset1SchemaLevel] = useState<number | null>(dataset1SchemaNoiseValue);
    const [dataset1SchemaKeyEnabled, setDataset1SchemaKeyEnabled] = useState<boolean | null>(dataset1SchemaKeyNoise);

    const [dataset2SchemaEnabled, setDataset2SchemaEnabled] = useState<boolean | null>(dataset2SchemaNoise);
    const [dataset2SchemaLevel, setDataset2SchemaLevel] = useState<number | null>(dataset2SchemaNoiseValue);
    const [dataset2SchemaKeyEnabled, setDataset2SchemaKeyEnabled] = useState<boolean | null>(dataset2SchemaKeyNoise);


    // Handler functions for the checkbox and slider value changes
    const handleDataset1SchemaToggle = (checked: boolean) => {
        setDataset1SchemaEnabled(checked);
        if (!checked) {
            setDataset1SchemaLevel(0);
            setDataset1SchemaKeyEnabled(false);
            updateForm({
                dataset1SchemaNoise: checked,
                dataset1SchemaNoiseValue: 0,
                dataset1SchemaKeyNoise: false,
            });
        } else {
            updateForm({ dataset1SchemaNoise: checked });
        }
    };

    const handleDataset1SchemaSliderChange = (value: number[]) => {
        const newLevel = value[0];
        setDataset1SchemaLevel(newLevel);
        updateForm({ dataset1SchemaNoiseValue: newLevel });
    };

    const handleDataset1SchemaKeyToggle = (checked: boolean) => {
        setDataset1SchemaKeyEnabled(checked);
        updateForm({ dataset1SchemaKeyNoise: checked });
    };

    const handleDataset2SchemaToggle = (checked: boolean) => {
        setDataset2SchemaEnabled(checked);
        if (!checked) {
            setDataset2SchemaLevel(0);
            setDataset2SchemaKeyEnabled(false);
            updateForm({
                dataset2SchemaNoise: checked,
                dataset2SchemaNoiseValue: 0,
                dataset2SchemaKeyNoise: false,
            });
        } else {
            updateForm({ dataset2SchemaNoise: checked });
        }
    };

    const handleDataset2SchemaSliderChange = (value: number[]) => {
        const newLevel = value[0];
        setDataset2SchemaLevel(newLevel);
        updateForm({ dataset2SchemaNoiseValue: newLevel });
    };

    const handleDataset2SchemaKeyToggle = (checked: boolean) => {
        setDataset2SchemaKeyEnabled(checked);
        updateForm({ dataset2SchemaKeyNoise: checked });
    };

    return (
        <FormWrapper
            title="Select Noise Options for Schema"
            description="You can choose if you want to add noise into the schema of each dataset"
        >
            <div className="flex flex-col md:flex-row md:gap-8 w-full">
                {/* First Checkbox and Slider */}
                <div className="flex flex-col w-full">
                    <h3 className="text-lg text-white mb-2">Dataset 1</h3>
                    <div className="flex items-center gap-2">
                        <Checkbox
                            checked={!!dataset1SchemaEnabled} // Ensure it's a boolean
                            onCheckedChange={handleDataset1SchemaToggle}
                            id="dataset1SchemaNoise"
                        />
                        <label htmlFor="dataset1SchemaNoise" className="text-white">
                            Enable Option
                        </label>
                    </div>
                    {errors.dataset1SchemaNoise && (
                        <p className="text-red-500 text-sm mt-1">{errors.dataset1SchemaNoise[0]}</p>
                    )}

                    {dataset1SchemaEnabled && (
                        <>
                            <div className="flex items-center gap-2 mt-2">
                                <Checkbox
                                    checked={!!dataset1SchemaKeyEnabled} // Ensure it's a boolean
                                    onCheckedChange={handleDataset1SchemaKeyToggle}
                                    id="dataset1SchemaKeyNoise"
                                />
                                <label htmlFor="dataset1SchemaKeyNoise" className="text-white">
                                    Enable Schema Noise in Key´s
                                </label>
                            </div>
                            {errors.dataset1SchemaKeyNoise && (
                                <p className="text-red-500 text-sm mt-1">
                                    {errors.dataset1SchemaKeyNoise[0]}
                                </p>
                            )}

                            <Slider
                                className="my-4 w-full"
                                value={[dataset1SchemaLevel ?? 0]}
                                onValueChange={handleDataset1SchemaSliderChange}
                                min={0}
                                max={100}
                                step={1}
                            />
                            {/* Display the current value of the slider */}
                            <span className="text-white text-sm ">
                                Percentage: {dataset1SchemaLevel ?? 0} %
                            </span>
                            {errors.dataset1NoiseValue && (
                                <p className="text-red-500 text-sm ">
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
                            checked={!!dataset2SchemaEnabled} // Ensure it's a boolean
                            onCheckedChange={handleDataset2SchemaToggle}
                            id="dataset2SchemaNoise"
                        />
                        <label htmlFor="dataset2SchemaNoise" className="text-white">
                            Enable Option
                        </label>
                    </div>
                    {errors.dataset2Noise && (
                        <p className="text-red-500 text-sm mt-1">{errors.dataset2Noise[0]}</p>
                    )}

                    {dataset2SchemaEnabled && (
                        <>
                            <div className="flex items-center gap-2 mt-2">
                                <Checkbox
                                    checked={!!dataset2SchemaKeyEnabled} // Ensure it's a boolean
                                    onCheckedChange={handleDataset2SchemaKeyToggle}
                                    id="dataset2SchemaKeyNoise"
                                />
                                <label htmlFor="dataset2SchemaKeyNoise" className="text-white">
                                    Enable Schema Noise in Key´s
                                </label>
                            </div>
                            {errors.dataset2SchemaKeyNoise && (
                                <p className="text-red-500 text-sm mt-1">
                                    {errors.dataset2SchemaKeyNoise[0]}
                                </p>
                            )}

                            <Slider
                                className="my-4 w-full"
                                value={[dataset2SchemaLevel ?? 0]}
                                onValueChange={handleDataset2SchemaSliderChange}
                                min={0}
                                max={100}
                                step={1}
                            />
                            {/* Display the current value of the slider */}
                            <span className="text-white text-sm ">
                                Percentage: {dataset2SchemaLevel ?? 0} %
                            </span>
                            {errors.dataset1NoiseValue && (
                                <p className="text-red-500 text-sm ">
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

export default Step5_SchemaNoiseForm;
