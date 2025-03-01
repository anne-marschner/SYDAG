"use client";
import React, { useState, useEffect } from "react";
import { Checkbox } from "@/components/ui/checkbox";
import { Slider } from "@/components/ui/slider";
import FormWrapper from "./FormWrapper";
import { FormItems } from "@/components/types/formTypes";
import { MultiSelect } from "@/components/ui/multi-select";

// Multi-select options
const frameworksList = [
  { value: "replaceWithSynonyms", label: "replaceWithSynonyms" },
  { value: "addRandomPrefix", label: "addRandomPrefix" },
  { value: "replaceWithTranslation", label: "replaceWithTranslation" },
  { value: "shuffleWords", label: "shuffleWords" },
  { value: "generateMissingValue", label: "generateMissingValue" },
  { value: "generatePhoneticError", label: "generatePhoneticError" },
  { value: "generateOCRError", label: "generateOCRError" },
  { value: "changeFormat", label: "changeFormat" },
  { value: "generateTypingError", label: "generateTypingError" },
  { value: "generateRandomString", label: "generateRandomString" },
  { value: "changeValue", label: "changeValue" },
  { value: "changeValueToOutlier", label: "changeValueToOutlier" },
  { value: "mapColumn", label: "mapColumn" }
];

type StepProps = {
  splitType: "Horizontal" | "Vertical" | "VerticalHorizontal" | null;

  // Dataset 1
  dataset1DataNoise: boolean | null;
  dataset1DataNoiseValue: number | null;
  dataset1DataKeyNoise: boolean | null;
  dataset1DataNoiseInside: number | null;
  dataset1DataMultiselect: string[] | null; 

  // Dataset 2
  dataset2DataNoise: boolean | null;
  dataset2DataNoiseValue: number | null;
  dataset2DataKeyNoise: boolean | null;
  dataset2DataNoiseInside: number | null;
  dataset2DataMultiselect: string[] | null; // NEW

  // Dataset 3
  dataset3DataNoise: boolean | null;
  dataset3DataNoiseValue: number | null;
  dataset3DataKeyNoise: boolean | null;
  dataset3DataNoiseInside: number | null;
  dataset3DataMultiselect: string[] | null; // NEW

  // Dataset 4
  dataset4DataNoise: boolean | null;
  dataset4DataNoiseValue: number | null;
  dataset4DataKeyNoise: boolean | null;
  dataset4DataNoiseInside: number | null;
  dataset4DataMultiselect: string[] | null; // NEW

  updateForm: (fieldToUpdate: Partial<FormItems>) => void;
  errors: Record<string, string[]>;
};

const Step6_DataNoiseForm = ({
  splitType,
  updateForm,
  // Dataset 1
  dataset1DataNoise,
  dataset1DataNoiseValue,
  dataset1DataKeyNoise,
  dataset1DataNoiseInside,
  dataset1DataMultiselect,
  // Dataset 2
  dataset2DataNoise,
  dataset2DataNoiseValue,
  dataset2DataKeyNoise,
  dataset2DataNoiseInside,
  dataset2DataMultiselect,
  // Dataset 3
  dataset3DataNoise,
  dataset3DataNoiseValue,
  dataset3DataKeyNoise,
  dataset3DataNoiseInside,
  dataset3DataMultiselect,
  // Dataset 4
  dataset4DataNoise,
  dataset4DataNoiseValue,
  dataset4DataKeyNoise,
  dataset4DataNoiseInside,
  dataset4DataMultiselect,
  errors,
}: StepProps) => {
  // DATASET 1 STATE
  const [dataset1Enabled, setDataset1Enabled] = useState<boolean>(!!dataset1DataNoise);
  const [dataset1Level, setDataset1Level] = useState<number>(dataset1DataNoiseValue ?? 0);
  const [dataset1KeyEnabled, setDataset1KeyEnabled] = useState<boolean>(!!dataset1DataKeyNoise);
  const [dataset1DataNoiseInsideState, setDataset1DataNoiseInsideState] = useState<number>(
    dataset1DataNoiseInside ?? 0
  );

  // multi-select local state
  const [dataset1Selections, setDataset1Selections] = useState<string[]>(
    dataset1DataMultiselect ?? []
  );

  // DATASET 2 STATE
  const [dataset2Enabled, setDataset2Enabled] = useState<boolean>(!!dataset2DataNoise);
  const [dataset2Level, setDataset2Level] = useState<number>(dataset2DataNoiseValue ?? 0);
  const [dataset2KeyEnabled, setDataset2KeyEnabled] = useState<boolean>(!!dataset2DataKeyNoise);
  const [dataset2DataNoiseInsideState, setDataset2DataNoiseInsideState] = useState<number>(
    dataset2DataNoiseInside ?? 0
  );

  const [dataset2Selections, setDataset2Selections] = useState<string[]>(
    dataset2DataMultiselect ?? []
  );

  // DATASET 3 STATE 
  const [dataset3Enabled, setDataset3Enabled] = useState<boolean>(!!dataset3DataNoise);
  const [dataset3Level, setDataset3Level] = useState<number>(dataset3DataNoiseValue ?? 0);
  const [dataset3KeyEnabled, setDataset3KeyEnabled] = useState<boolean>(!!dataset3DataKeyNoise);
  const [dataset3DataNoiseInsideState, setDataset3DataNoiseInsideState] = useState<number>(
    dataset3DataNoiseInside ?? 0
  );

  const [dataset3Selections, setDataset3Selections] = useState<string[]>(
    dataset3DataMultiselect ?? []
  );

  // DATASET 4 STATE
  const [dataset4Enabled, setDataset4Enabled] = useState<boolean>(!!dataset4DataNoise);
  const [dataset4Level, setDataset4Level] = useState<number>(dataset4DataNoiseValue ?? 0);
  const [dataset4KeyEnabled, setDataset4KeyEnabled] = useState<boolean>(!!dataset4DataKeyNoise);
  const [dataset4DataNoiseInsideState, setDataset4DataNoiseInsideState] = useState<number>(
    dataset4DataNoiseInside ?? 0
  );

  const [dataset4Selections, setDataset4Selections] = useState<string[]>(
    dataset4DataMultiselect ?? []
  );

  // DATASET 1 HANDLERS
  const handleDataset1Toggle = (checked: boolean) => {
    setDataset1Enabled(checked);
    if (!checked) {
      setDataset1Level(0);
      setDataset1KeyEnabled(false);
      setDataset1DataNoiseInsideState(0);
      // Reset multi-select
      setDataset1Selections([]);

      updateForm({
        dataset1DataNoise: checked,
        dataset1DataNoiseValue: 0,
        dataset1DataKeyNoise: false,
        dataset1DataNoiseInside: 0,
        dataset1DataMultiselect: [],
      });
    } else {
      updateForm({ dataset1DataNoise: checked });
    }
  };

  const handleDataset1Slider = (value: number[]) => {
    const newLevel = value[0];
    setDataset1Level(newLevel);
    updateForm({ dataset1DataNoiseValue: newLevel });
  };

  const handleDataset1KeyToggle = (checked: boolean) => {
    setDataset1KeyEnabled(checked);
    updateForm({ dataset1DataKeyNoise: checked });
  };

  const handleDataset1DataNoiseInsideSlider = (value: number[]) => {
    const newValue = value[0];
    setDataset1DataNoiseInsideState(newValue);
    updateForm({ dataset1DataNoiseInside: newValue });
  };

  // handle multi-select changes for dataset 1
  const handleDataset1MultiselectChange = (values: string[]) => {
    setDataset1Selections(values);
    updateForm({ dataset1DataMultiselect: values });
  };

  // DATASET 2 HANDLERS
  const handleDataset2Toggle = (checked: boolean) => {
    setDataset2Enabled(checked);
    if (!checked) {
      setDataset2Level(0);
      setDataset2KeyEnabled(false);
      setDataset2DataNoiseInsideState(0);
      setDataset2Selections([]);

      updateForm({
        dataset2DataNoise: checked,
        dataset2DataNoiseValue: 0,
        dataset2DataKeyNoise: false,
        dataset2DataNoiseInside: 0,
        dataset2DataMultiselect: [],
      });
    } else {
      updateForm({ dataset2DataNoise: checked });
    }
  };

  const handleDataset2Slider = (value: number[]) => {
    const newLevel = value[0];
    setDataset2Level(newLevel);
    updateForm({ dataset2DataNoiseValue: newLevel });
  };

  const handleDataset2KeyToggle = (checked: boolean) => {
    setDataset2KeyEnabled(checked);
    updateForm({ dataset2DataKeyNoise: checked });
  };

  const handleDataset2DataNoiseInsideSlider = (value: number[]) => {
    const newValue = value[0];
    setDataset2DataNoiseInsideState(newValue);
    updateForm({ dataset2DataNoiseInside: newValue });
  };

  // handle multi-select changes for dataset 2
  const handleDataset2MultiselectChange = (values: string[]) => {
    setDataset2Selections(values);
    updateForm({ dataset2DataMultiselect: values });
  };

  // DATASET 3 HANDLERS
  const handleDataset3Toggle = (checked: boolean) => {
    setDataset3Enabled(checked);
    if (!checked) {
      setDataset3Level(0);
      setDataset3KeyEnabled(false);
      setDataset3DataNoiseInsideState(0);
      setDataset3Selections([]);

      updateForm({
        dataset3DataNoise: checked,
        dataset3DataNoiseValue: 0,
        dataset3DataKeyNoise: false,
        dataset3DataNoiseInside: 0,
        dataset3DataMultiselect: [],
      });
    } else {
      updateForm({ dataset3DataNoise: checked });
    }
  };

  const handleDataset3Slider = (value: number[]) => {
    const newLevel = value[0];
    setDataset3Level(newLevel);
    updateForm({ dataset3DataNoiseValue: newLevel });
  };

  const handleDataset3KeyToggle = (checked: boolean) => {
    setDataset3KeyEnabled(checked);
    updateForm({ dataset3DataKeyNoise: checked });
  };

  const handleDataset3DataNoiseInsideSlider = (value: number[]) => {
    const newValue = value[0];
    setDataset3DataNoiseInsideState(newValue);
    updateForm({ dataset3DataNoiseInside: newValue });
  };

  // handle multi-select changes for dataset 3
  const handleDataset3MultiselectChange = (values: string[]) => {
    setDataset3Selections(values);
    updateForm({ dataset3DataMultiselect: values });
  };

  // DATASET 4 HANDLERS
  const handleDataset4Toggle = (checked: boolean) => {
    setDataset4Enabled(checked);
    if (!checked) {
      setDataset4Level(0);
      setDataset4KeyEnabled(false);
      setDataset4DataNoiseInsideState(0);
      setDataset4Selections([]);

      updateForm({
        dataset4DataNoise: checked,
        dataset4DataNoiseValue: 0,
        dataset4DataKeyNoise: false,
        dataset4DataNoiseInside: 0,
        dataset4DataMultiselect: [],
      });
    } else {
      updateForm({ dataset4DataNoise: checked });
    }
  };

  const handleDataset4Slider = (value: number[]) => {
    const newLevel = value[0];
    setDataset4Level(newLevel);
    updateForm({ dataset4DataNoiseValue: newLevel });
  };

  const handleDataset4KeyToggle = (checked: boolean) => {
    setDataset4KeyEnabled(checked);
    updateForm({ dataset4DataKeyNoise: checked });
  };

  const handleDataset4DataNoiseInsideSlider = (value: number[]) => {
    const newValue = value[0];
    setDataset4DataNoiseInsideState(newValue);
    updateForm({ dataset4DataNoiseInside: newValue });
  };

  // handle multi-select changes for dataset 4
  const handleDataset4MultiselectChange = (values: string[]) => {
    setDataset4Selections(values);
    updateForm({ dataset4DataMultiselect: values });
  };

  // Reset Datasets 3 and 4 if splitType != "VerticalHorizontal"
  useEffect(() => {
    if (splitType !== "VerticalHorizontal") {
      // Reset Dataset 3
      setDataset3Enabled(false);
      setDataset3Level(0);
      setDataset3KeyEnabled(false);
      setDataset3DataNoiseInsideState(0);
      setDataset3Selections([]);

      // Reset Dataset 4
      setDataset4Enabled(false);
      setDataset4Level(0);
      setDataset4KeyEnabled(false);
      setDataset4DataNoiseInsideState(0);
      setDataset4Selections([]);

      // Update form
      updateForm({
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
      });
    }
  }, [splitType, updateForm]);

  
  return (
    <FormWrapper
      title="Select Noise Options for Data"
      description="For each dataset, choose whether to add noise to the data. If enabled, specify the error methods."
    >
      <div className="max-h-[750px] overflow-y-auto scrollbar-custom p-4 space-y-6">
        <div className="flex flex-col w-full h-full max-h-[50vh] p-4 scrollbar-custom">
        <div className="flex flex-col md:flex-row md:gap-8 w-full">
          {/* Dataset 1 */}
          <div className="flex flex-col w-full">
            <h3 className="text-lg text-white mb-2">Dataset 1</h3>
            <div className="flex items-center gap-2 custom-label">
              <Checkbox
                checked={dataset1Enabled}
                onCheckedChange={handleDataset1Toggle}
                id="dataset1Noise"
              />
              <label htmlFor="dataset1Noise" className="text-white">
                Enable Noise
              </label>
            </div>
            {errors.dataset1DataNoise && (
              <p className="text-red-500 text-sm mt-1">
                {errors.dataset1DataNoise[0]}
              </p>
            )}

            {dataset1Enabled && (
              <>
                {/* Break Key Constraints Checkbox */}
                <div className="flex items-center gap-2 mt-2 custom-label">
                  <Checkbox
                    checked={dataset1KeyEnabled}
                    onCheckedChange={handleDataset1KeyToggle}
                    id="dataset1DataKeyNoise"
                  />
                  <label htmlFor="dataset1DataKeyNoise" className="text-white">
                    Break Key Constraints
                  </label>
                </div>
                {errors.dataset1DataKeyNoise && (
                  <p className="text-red-500 text-sm mt-1">
                    {errors.dataset1DataKeyNoise[0]}
                  </p>
                )}

                {/* Multi-Select for Dataset 1 */}
                <div className="mt-4">
                  <label htmlFor="dataset1DataMultiSelect" className="text-white">
                    Dataset 1 Error Methods
                  </label>
                  <MultiSelect
                    id="dataset1DataMultiSelect"
                    options={frameworksList}
                    onValueChange={handleDataset1MultiselectChange}
                    defaultValue={dataset1Selections}
                    placeholder="Select methods for Dataset 1"
                    variant="inverted"
                    animation={2}
                    maxCount={3}
                  />
                  <div className="text-white text-sm mt-2">
                    Selected: {dataset1Selections.join(", ")}
                  </div>
                </div>

                {/* Noise Slider */}
                <div className="mt-4">
                  <label htmlFor="dataset1DataNoiseSlider" className="text-white">
                    Percentage of noisy Columns/ Rows
                  </label>
                  <Slider
                    id="dataset1DataNoiseSlider"
                    className="my-2 w-full"
                    value={[dataset1Level]}
                    onValueChange={handleDataset1Slider}
                    min={0}
                    max={100}
                    step={1}
                  />
                  <span className="text-white text-sm">
                    Percentage: {dataset1Level} %
                  </span>
                  {errors.dataset1DataNoiseValue && (
                    <p className="text-red-500 text-sm mt-1">
                      {errors.dataset1DataNoiseValue[0]}
                    </p>
                  )}
                </div>

                {/* Noise Inside Slider */}
                <div className="mt-4">
                  <label
                    htmlFor="dataset1DataNoiseInsideSlider"
                    className="text-white"
                  >
                    Percentage of noisy entries in Column/ Row
                  </label>
                  <Slider
                    id="dataset1DataNoiseInsideSlider"
                    className="my-2 w-full"
                    value={[dataset1DataNoiseInsideState]}
                    onValueChange={handleDataset1DataNoiseInsideSlider}
                    min={0}
                    max={100}
                    step={1}
                  />
                  <span className="text-white text-sm">
                    Percentage: {dataset1DataNoiseInsideState} %
                  </span>
                  {errors.dataset1DataNoiseInside && (
                    <p className="text-red-500 text-sm mt-1">
                      {errors.dataset1DataNoiseInside[0]}
                    </p>
                  )}
                </div>
              </>
            )}
          </div>

          {/* Dataset 2 */}
          <div className="flex flex-col w-full">
            <h3 className="text-lg text-white mb-2">Dataset 2</h3>
            <div className="flex items-center gap-2 custom-label">
              <Checkbox
                checked={dataset2Enabled}
                onCheckedChange={handleDataset2Toggle}
                id="dataset2Noise"
              />
              <label htmlFor="dataset2Noise" className="text-white">
                Enable Noise
              </label>
            </div>
            {errors.dataset2DataNoise && (
              <p className="text-red-500 text-sm mt-1">
                {errors.dataset2DataNoise[0]}
              </p>
            )}

            {dataset2Enabled && (
              <>
                {/* Break Key Constraints Checkbox */}
                <div className="flex items-center gap-2 mt-2 custom-label">
                  <Checkbox
                    checked={dataset2KeyEnabled}
                    onCheckedChange={handleDataset2KeyToggle}
                    id="dataset2DataKeyNoise"
                  />
                  <label htmlFor="dataset2DataKeyNoise" className="text-white">
                    Break Key Constraints
                  </label>
                </div>
                {errors.dataset2DataKeyNoise && (
                  <p className="text-red-500 text-sm mt-1">
                    {errors.dataset2DataKeyNoise[0]}
                  </p>
                )}

                {/* Multi-Select for Dataset 2 */}
                <div className="mt-4">
                  <label htmlFor="dataset2DataMultiSelect" className="text-white">
                    Dataset 2 Error Methods
                  </label>
                  <MultiSelect
                    id="dataset2DataMultiSelect"
                    options={frameworksList}
                    onValueChange={handleDataset2MultiselectChange}
                    defaultValue={dataset2Selections}
                    placeholder="Select methods for Dataset 2"
                    variant="inverted"
                    animation={2}
                    maxCount={3}
                  />
                  <div className="text-white text-sm mt-2">
                    Selected: {dataset2Selections.join(", ")}
                  </div>
                </div>

                {/* Noise Slider */}
                <div className="mt-4">
                  <label htmlFor="dataset2DataNoiseSlider" className="text-white">
                    Percentage of noisy Columns/ Rows
                  </label>
                  <Slider
                    id="dataset2DataNoiseSlider"
                    className="my-2 w-full"
                    value={[dataset2Level]}
                    onValueChange={handleDataset2Slider}
                    min={0}
                    max={100}
                    step={1}
                  />
                  <span className="text-white text-sm">
                    Percentage: {dataset2Level} %
                  </span>
                  {errors.dataset2DataNoiseValue && (
                    <p className="text-red-500 text-sm mt-1">
                      {errors.dataset2DataNoiseValue[0]}
                    </p>
                  )}
                </div>

                {/* Noise Inside Slider */}
                <div className="mt-4">
                  <label
                    htmlFor="dataset2DataNoiseInsideSlider"
                    className="text-white"
                  >
                    Percentage of noisy entries in Column/ Row
                  </label>
                  <Slider
                    id="dataset2DataNoiseInsideSlider"
                    className="my-2 w-full"
                    value={[dataset2DataNoiseInsideState]}
                    onValueChange={handleDataset2DataNoiseInsideSlider}
                    min={0}
                    max={100}
                    step={1}
                  />
                  <span className="text-white text-sm">
                    Percentage: {dataset2DataNoiseInsideState} %
                  </span>
                  {errors.dataset2DataNoiseInside && (
                    <p className="text-red-500 text-sm mt-1">
                      {errors.dataset2DataNoiseInside[0]}
                    </p>
                  )}
                </div>
              </>
            )}
          </div>
        </div>

        {/* Conditionally Render Dataset 3 and Dataset 4 Controls */}
        {splitType === "VerticalHorizontal" && (
          <div className="flex flex-col md:flex-row md:gap-8 w-full mt-8">
            {/* Dataset 3 */}
            <div className="flex flex-col w-full">
              <h3 className="text-lg text-white mb-2">Dataset 3</h3>
              <div className="flex items-center gap-2 custom-label">
                <Checkbox
                  checked={dataset3Enabled}
                  onCheckedChange={handleDataset3Toggle}
                  id="dataset3Noise"
                />
                <label htmlFor="dataset3Noise" className="text-white">
                  Enable Noise
                </label>
              </div>
              {errors.dataset3DataNoise && (
                <p className="text-red-500 text-sm mt-1">
                  {errors.dataset3DataNoise[0]}
                </p>
              )}

              {dataset3Enabled && (
                <>
                  {/* Break Key Constraints Checkbox */}
                  <div className="flex items-center gap-2 mt-2 custom-label">
                    <Checkbox
                      checked={dataset3KeyEnabled}
                      onCheckedChange={handleDataset3KeyToggle}
                      id="dataset3DataKeyNoise"
                    />
                    <label htmlFor="dataset3DataKeyNoise" className="text-white">
                      Break Key Constraints
                    </label>
                  </div>
                  {errors.dataset3DataKeyNoise && (
                    <p className="text-red-500 text-sm mt-1">
                      {errors.dataset3DataKeyNoise[0]}
                    </p>
                  )}

                  {/* Multi-Select for Dataset 3 */}
                  <div className="mt-4">
                    <label htmlFor="dataset3DataMultiSelect" className="text-white">
                      Dataset 3 Error Methods
                    </label>
                    <MultiSelect
                      id="dataset3DataMultiSelect"
                      options={frameworksList}
                      onValueChange={handleDataset3MultiselectChange}
                      defaultValue={dataset3Selections}
                      placeholder="Select methods for Dataset 3"
                      variant="inverted"
                      animation={2}
                      maxCount={3}
                    />
                    <div className="text-white text-sm mt-2">
                      Selected: {dataset3Selections.join(", ")}
                    </div>
                  </div>

                  {/* Noise Slider */}
                  <div className="mt-4">
                    <label htmlFor="dataset3DataNoiseSlider" className="text-white">
                      Percentage of noisy Columns/ Rows
                    </label>
                    <Slider
                      id="dataset3DataNoiseSlider"
                      className="my-2 w-full"
                      value={[dataset3Level]}
                      onValueChange={handleDataset3Slider}
                      min={0}
                      max={100}
                      step={1}
                    />
                    <span className="text-white text-sm">
                      Percentage: {dataset3Level} %
                    </span>
                    {errors.dataset3DataNoiseValue && (
                      <p className="text-red-500 text-sm mt-1">
                        {errors.dataset3DataNoiseValue[0]}
                      </p>
                    )}
                  </div>

                  {/* Noise Inside Slider */}
                  <div className="mt-4">
                    <label
                      htmlFor="dataset3DataNoiseInsideSlider"
                      className="text-white"
                    >
                      Percentage of noisy entries in Column/ Row
                    </label>
                    <Slider
                      id="dataset3DataNoiseInsideSlider"
                      className="my-2 w-full"
                      value={[dataset3DataNoiseInsideState]}
                      onValueChange={handleDataset3DataNoiseInsideSlider}
                      min={0}
                      max={100}
                      step={1}
                    />
                    <span className="text-white text-sm">
                      Percentage: {dataset3DataNoiseInsideState} %
                    </span>
                    {errors.dataset3DataNoiseInside && (
                      <p className="text-red-500 text-sm mt-1">
                        {errors.dataset3DataNoiseInside[0]}
                      </p>
                    )}
                  </div>
                </>
              )}
            </div>

            {/* Dataset 4 */}
            <div className="flex flex-col w-full">
              <h3 className="text-lg text-white mb-2">Dataset 4</h3>
              <div className="flex items-center gap-2 custom-label">
                <Checkbox
                  checked={dataset4Enabled}
                  onCheckedChange={handleDataset4Toggle}
                  id="dataset4Noise"
                />
                <label htmlFor="dataset4Noise" className="text-white">
                  Enable Noise
                </label>
              </div>
              {errors.dataset4DataNoise && (
                <p className="text-red-500 text-sm mt-1">
                  {errors.dataset4DataNoise[0]}
                </p>
              )}

              {dataset4Enabled && (
                <>
                  {/* Break Key Constraints Checkbox */}
                  <div className="flex items-center gap-2 mt-2 custom-label">
                    <Checkbox
                      checked={dataset4KeyEnabled}
                      onCheckedChange={handleDataset4KeyToggle}
                      id="dataset4DataKeyNoise"
                    />
                    <label htmlFor="dataset4DataKeyNoise" className="text-white">
                      Break Key Constraints
                    </label>
                  </div>
                  {errors.dataset4DataKeyNoise && (
                    <p className="text-red-500 text-sm mt-1">
                      {errors.dataset4DataKeyNoise[0]}
                    </p>
                  )}

                  {/* Multi-Select for Dataset 4 */}
                  <div className="mt-4">
                    <label htmlFor="dataset4DataMultiSelect" className="text-white">
                      Dataset 4 Error Methods
                    </label>
                    <MultiSelect
                      id="dataset4DataMultiSelect"
                      options={frameworksList}
                      onValueChange={handleDataset4MultiselectChange}
                      defaultValue={dataset4Selections}
                      placeholder="Select methods for Dataset 4"
                      variant="inverted"
                      animation={2}
                      maxCount={3}
                    />
                    <div className="text-white text-sm mt-2">
                      Selected: {dataset4Selections.join(", ")}
                    </div>
                  </div>

                  {/* Noise Slider */}
                  <div className="mt-4">
                    <label htmlFor="dataset4DataNoiseSlider" className="text-white">
                    Percentage of noisy Columns/ Rows
                    </label>
                    <Slider
                      id="dataset4DataNoiseSlider"
                      className="my-2 w-full"
                      value={[dataset4Level]}
                      onValueChange={handleDataset4Slider}
                      min={0}
                      max={100}
                      step={1}
                    />
                    <span className="text-white text-sm">
                      Percentage: {dataset4Level} %
                    </span>
                    {errors.dataset4DataNoiseValue && (
                      <p className="text-red-500 text-sm mt-1">
                        {errors.dataset4DataNoiseValue[0]}
                      </p>
                    )}
                  </div>

                  {/* Noise Inside Slider */}
                  <div className="mt-4">
                    <label
                      htmlFor="dataset4DataNoiseInsideSlider"
                      className="text-white"
                    >
                      Percentage of noisy entries in Column/ Row
                    </label>
                    <Slider
                      id="dataset4DataNoiseInsideSlider"
                      className="my-2 w-full"
                      value={[dataset4DataNoiseInsideState]}
                      onValueChange={handleDataset4DataNoiseInsideSlider}
                      min={0}
                      max={100}
                      step={1}
                    />
                    <span className="text-white text-sm">
                      Percentage: {dataset4DataNoiseInsideState} %
                    </span>
                    {errors.dataset4DataNoiseInside && (
                      <p className="text-red-500 text-sm mt-1">
                        {errors.dataset4DataNoiseInside[0]}
                      </p>
                    )}
                  </div>
                </>
              )}
            </div>
          </div>
        )}
      </div>
      </div>
    </FormWrapper>
  );
};

export default Step6_DataNoiseForm;
