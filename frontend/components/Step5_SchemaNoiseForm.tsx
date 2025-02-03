"use client";
import React, { useState, useEffect } from "react";
import { Checkbox } from "@/components/ui/checkbox";
import { Slider } from "@/components/ui/slider";
import FormWrapper from "./FormWrapper";
import { FormItems } from "@/components/types/formTypes"; 
import { MultiSelect } from "@/components/ui/multi-select";

const frameworksList = [
  { value: "generateRandomString", label: "generateRandomString" },
  { value: "abbreviateFirstLetters", label: "abbreviateFirstLetters" },
  { value: "abbreviateRandomLength", label: "abbreviateRandomLength" },
  { value: "addRandomPrefix", label: "addRandomPrefix" },
  { value: "shuffleLetters", label: "shuffleLetters" },
  { value: "replaceWithSynonyms", label: "replaceWithSynonyms" },
  { value: "replaceWithTranslation", label: "replaceWithTranslation" }
];

type StepProps = {
  splitType: "Horizontal" | "Vertical" | "VerticalHorizontal" | null;

  // Dataset 1
  dataset1SchemaNoise: boolean | null;
  dataset1SchemaNoiseValue: number | null;
  dataset1SchemaKeyNoise: boolean | null;
  dataset1SchemaDeleteSchema: boolean | null;
  dataset1SchemaMultiselect: string[] | null; 

  // Dataset 2
  dataset2SchemaNoise: boolean | null;
  dataset2SchemaNoiseValue: number | null;
  dataset2SchemaKeyNoise: boolean | null;
  dataset2SchemaDeleteSchema: boolean | null;
  dataset2SchemaMultiselect: string[] | null; 

  // Dataset 3
  dataset3SchemaNoise: boolean | null;
  dataset3SchemaNoiseValue: number | null;
  dataset3SchemaKeyNoise: boolean | null;
  dataset3SchemaDeleteSchema: boolean | null;
  dataset3SchemaMultiselect: string[] | null; 

  // Dataset 4
  dataset4SchemaNoise: boolean | null;
  dataset4SchemaNoiseValue: number | null;
  dataset4SchemaKeyNoise: boolean | null;
  dataset4SchemaDeleteSchema: boolean | null;
  dataset4SchemaMultiselect: string[] | null; 

  updateForm: (fieldToUpdate: Partial<FormItems>) => void;
  errors: Record<string, string[]>;
};

const Step5_SchemaNoiseForm = ({
  splitType,
  updateForm,
  // Dataset 1
  dataset1SchemaNoise,
  dataset1SchemaNoiseValue,
  dataset1SchemaKeyNoise,
  dataset1SchemaDeleteSchema,
  dataset1SchemaMultiselect, 
  // Dataset 2
  dataset2SchemaNoise,
  dataset2SchemaNoiseValue,
  dataset2SchemaKeyNoise,
  dataset2SchemaDeleteSchema,
  dataset2SchemaMultiselect, 
  // Dataset 3
  dataset3SchemaNoise,
  dataset3SchemaNoiseValue,
  dataset3SchemaKeyNoise,
  dataset3SchemaDeleteSchema,
  dataset3SchemaMultiselect, 
  // Dataset 4
  dataset4SchemaNoise,
  dataset4SchemaNoiseValue,
  dataset4SchemaKeyNoise,
  dataset4SchemaDeleteSchema,
  dataset4SchemaMultiselect, 
  errors,
}: StepProps) => {
  // Helper function to handle mutual exclusivity
  const handleMutualExclusivity = (
    setKeyEnabled: React.Dispatch<React.SetStateAction<boolean>>,
    setDeleteEnabled: React.Dispatch<React.SetStateAction<boolean>>,
    updateKey: (value: boolean) => void,
    updateDelete: (value: boolean) => void,
    keyChecked: boolean,
    deleteChecked: boolean
  ) => {
    if (keyChecked) {
      setDeleteEnabled(false);
      updateDelete(false);
    } else if (deleteChecked) {
      setKeyEnabled(false);
      updateKey(false);
    }
  };

  // Dataset 1 State
  const [dataset1Enabled, setDataset1Enabled] = useState<boolean>(!!dataset1SchemaNoise);
  const [dataset1Level, setDataset1Level] = useState<number>(dataset1SchemaNoiseValue ?? 0);
  const [dataset1KeyEnabled, setDataset1KeyEnabled] = useState<boolean>(!!dataset1SchemaKeyNoise);
  const [dataset1DeleteSchemaEnabled, setDataset1DeleteSchemaEnabled] = useState<boolean>(
    !!dataset1SchemaDeleteSchema
  );
  // initialize local state from props
  const [dataset1Selections, setDataset1Selections] = useState<string[]>(
    dataset1SchemaMultiselect ?? []
  );

  // Dataset 2 State
  const [dataset2Enabled, setDataset2Enabled] = useState<boolean>(!!dataset2SchemaNoise);
  const [dataset2Level, setDataset2Level] = useState<number>(dataset2SchemaNoiseValue ?? 0);
  const [dataset2KeyEnabled, setDataset2KeyEnabled] = useState<boolean>(!!dataset2SchemaKeyNoise);
  const [dataset2DeleteSchemaEnabled, setDataset2DeleteSchemaEnabled] = useState<boolean>(
    !!dataset2SchemaDeleteSchema
  );
  
  const [dataset2Selections, setDataset2Selections] = useState<string[]>(
    dataset2SchemaMultiselect ?? []
  );

  // Dataset 3 State
  const [dataset3Enabled, setDataset3Enabled] = useState<boolean>(!!dataset3SchemaNoise);
  const [dataset3Level, setDataset3Level] = useState<number>(dataset3SchemaNoiseValue ?? 0);
  const [dataset3KeyEnabled, setDataset3KeyEnabled] = useState<boolean>(!!dataset3SchemaKeyNoise);
  const [dataset3DeleteSchemaEnabled, setDataset3DeleteSchemaEnabled] = useState<boolean>(
    !!dataset3SchemaDeleteSchema
  );
  
  const [dataset3Selections, setDataset3Selections] = useState<string[]>(
    dataset3SchemaMultiselect ?? []
  );

  // Dataset 4 State
  const [dataset4Enabled, setDataset4Enabled] = useState<boolean>(!!dataset4SchemaNoise);
  const [dataset4Level, setDataset4Level] = useState<number>(dataset4SchemaNoiseValue ?? 0);
  const [dataset4KeyEnabled, setDataset4KeyEnabled] = useState<boolean>(!!dataset4SchemaKeyNoise);
  const [dataset4DeleteSchemaEnabled, setDataset4DeleteSchemaEnabled] = useState<boolean>(
    !!dataset4SchemaDeleteSchema
  );
  
  const [dataset4Selections, setDataset4Selections] = useState<string[]>(
    dataset4SchemaMultiselect ?? []
  );

  // DATASET 1
  const handleDataset1Toggle = (checked: boolean) => {
    setDataset1Enabled(checked);
    if (!checked) {
      setDataset1Level(0);
      setDataset1KeyEnabled(false);
      setDataset1DeleteSchemaEnabled(false);
      // Reset multi-select
      setDataset1Selections([]);
      updateForm({
        dataset1SchemaNoise: checked,
        dataset1SchemaNoiseValue: 0,
        dataset1SchemaKeyNoise: false,
        dataset1SchemaDeleteSchema: false,
        // also reset multiselect in parent
        dataset1SchemaMultiselect: [],
      });
    } else {
      updateForm({ dataset1SchemaNoise: checked });
    }
  };

  const handleDataset1Slider = (value: number[]) => {
    const newLevel = value[0];
    setDataset1Level(newLevel);
    updateForm({ dataset1SchemaNoiseValue: newLevel });
  };

  const handleDataset1KeyToggle = (checked: boolean) => {
    setDataset1KeyEnabled(checked);
    updateForm({ dataset1SchemaKeyNoise: checked });
    handleMutualExclusivity(
      setDataset1KeyEnabled,
      setDataset1DeleteSchemaEnabled,
      (val: boolean) => updateForm({ dataset1SchemaKeyNoise: val }),
      (val: boolean) => updateForm({ dataset1SchemaDeleteSchema: val }),
      checked,
      dataset1DeleteSchemaEnabled
    );
  };

  const handleDataset1DeleteSchemaToggle = (checked: boolean) => {
    setDataset1DeleteSchemaEnabled(checked);
    updateForm({ dataset1SchemaDeleteSchema: checked });
    handleMutualExclusivity(
      setDataset1KeyEnabled,
      setDataset1DeleteSchemaEnabled,
      (val: boolean) => updateForm({ dataset1SchemaKeyNoise: val }),
      (val: boolean) => updateForm({ dataset1SchemaDeleteSchema: val }),
      dataset1KeyEnabled,
      checked
    );
  };

  // handle multi-select changes for dataset 1
  const handleDataset1MultiselectChange = (values: string[]) => {
    setDataset1Selections(values);
    // Also store in parent form
    updateForm({ dataset1SchemaMultiselect: values });
  };

  // DATASET 2
  const handleDataset2Toggle = (checked: boolean) => {
    setDataset2Enabled(checked);
    if (!checked) {
      setDataset2Level(0);
      setDataset2KeyEnabled(false);
      setDataset2DeleteSchemaEnabled(false);
      setDataset2Selections([]);
      updateForm({
        dataset2SchemaNoise: checked,
        dataset2SchemaNoiseValue: 0,
        dataset2SchemaKeyNoise: false,
        dataset2SchemaDeleteSchema: false,
        dataset2SchemaMultiselect: [],
      });
    } else {
      updateForm({ dataset2SchemaNoise: checked });
    }
  };

  const handleDataset2Slider = (value: number[]) => {
    const newLevel = value[0];
    setDataset2Level(newLevel);
    updateForm({ dataset2SchemaNoiseValue: newLevel });
  };

  const handleDataset2KeyToggle = (checked: boolean) => {
    setDataset2KeyEnabled(checked);
    updateForm({ dataset2SchemaKeyNoise: checked });
    handleMutualExclusivity(
      setDataset2KeyEnabled,
      setDataset2DeleteSchemaEnabled,
      (val: boolean) => updateForm({ dataset2SchemaKeyNoise: val }),
      (val: boolean) => updateForm({ dataset2SchemaDeleteSchema: val }),
      checked,
      dataset2DeleteSchemaEnabled
    );
  };

  const handleDataset2DeleteSchemaToggle = (checked: boolean) => {
    setDataset2DeleteSchemaEnabled(checked);
    updateForm({ dataset2SchemaDeleteSchema: checked });
    handleMutualExclusivity(
      setDataset2KeyEnabled,
      setDataset2DeleteSchemaEnabled,
      (val: boolean) => updateForm({ dataset2SchemaKeyNoise: val }),
      (val: boolean) => updateForm({ dataset2SchemaDeleteSchema: val }),
      dataset2KeyEnabled,
      checked
    );
  };

  // handle multi-select changes for dataset 2
  const handleDataset2MultiselectChange = (values: string[]) => {
    setDataset2Selections(values);
    updateForm({ dataset2SchemaMultiselect: values });
  };

  // DATASET 3
  const handleDataset3Toggle = (checked: boolean) => {
    setDataset3Enabled(checked);
    if (!checked) {
      setDataset3Level(0);
      setDataset3KeyEnabled(false);
      setDataset3DeleteSchemaEnabled(false);
      setDataset3Selections([]);
      updateForm({
        dataset3SchemaNoise: checked,
        dataset3SchemaNoiseValue: 0,
        dataset3SchemaKeyNoise: false,
        dataset3SchemaDeleteSchema: false,
        dataset3SchemaMultiselect: [], 
      });
    } else {
      updateForm({ dataset3SchemaNoise: checked });
    }
  };

  const handleDataset3Slider = (value: number[]) => {
    const newLevel = value[0];
    setDataset3Level(newLevel);
    updateForm({ dataset3SchemaNoiseValue: newLevel });
  };

  const handleDataset3KeyToggle = (checked: boolean) => {
    setDataset3KeyEnabled(checked);
    updateForm({ dataset3SchemaKeyNoise: checked });
    handleMutualExclusivity(
      setDataset3KeyEnabled,
      setDataset3DeleteSchemaEnabled,
      (val: boolean) => updateForm({ dataset3SchemaKeyNoise: val }),
      (val: boolean) => updateForm({ dataset3SchemaDeleteSchema: val }),
      checked,
      dataset3DeleteSchemaEnabled
    );
  };

  const handleDataset3DeleteSchemaToggle = (checked: boolean) => {
    setDataset3DeleteSchemaEnabled(checked);
    updateForm({ dataset3SchemaDeleteSchema: checked });
    handleMutualExclusivity(
      setDataset3KeyEnabled,
      setDataset3DeleteSchemaEnabled,
      (val: boolean) => updateForm({ dataset3SchemaKeyNoise: val }),
      (val: boolean) => updateForm({ dataset3SchemaDeleteSchema: val }),
      dataset3KeyEnabled,
      checked
    );
  };

  // handle multi-select changes for dataset 3
  const handleDataset3MultiselectChange = (values: string[]) => {
    setDataset3Selections(values);
    updateForm({ dataset3SchemaMultiselect: values });
  };

  // DATASET 4
  const handleDataset4Toggle = (checked: boolean) => {
    setDataset4Enabled(checked);
    if (!checked) {
      setDataset4Level(0);
      setDataset4KeyEnabled(false);
      setDataset4DeleteSchemaEnabled(false);
      setDataset4Selections([]);
      updateForm({
        dataset4SchemaNoise: checked,
        dataset4SchemaNoiseValue: 0,
        dataset4SchemaKeyNoise: false,
        dataset4SchemaDeleteSchema: false,
        dataset4SchemaMultiselect: [],
      });
    } else {
      updateForm({ dataset4SchemaNoise: checked });
    }
  };

  const handleDataset4Slider = (value: number[]) => {
    const newLevel = value[0];
    setDataset4Level(newLevel);
    updateForm({ dataset4SchemaNoiseValue: newLevel });
  };

  const handleDataset4KeyToggle = (checked: boolean) => {
    setDataset4KeyEnabled(checked);
    updateForm({ dataset4SchemaKeyNoise: checked });
    handleMutualExclusivity(
      setDataset4KeyEnabled,
      setDataset4DeleteSchemaEnabled,
      (val: boolean) => updateForm({ dataset4SchemaKeyNoise: val }),
      (val: boolean) => updateForm({ dataset4SchemaDeleteSchema: val }),
      checked,
      dataset4DeleteSchemaEnabled
    );
  };

  const handleDataset4DeleteSchemaToggle = (checked: boolean) => {
    setDataset4DeleteSchemaEnabled(checked);
    updateForm({ dataset4SchemaDeleteSchema: checked });
    handleMutualExclusivity(
      setDataset4KeyEnabled,
      setDataset4DeleteSchemaEnabled,
      (val: boolean) => updateForm({ dataset4SchemaKeyNoise: val }),
      (val: boolean) => updateForm({ dataset4SchemaDeleteSchema: val }),
      dataset4KeyEnabled,
      checked
    );
  };

  // handle multi-select changes for dataset 4
  const handleDataset4MultiselectChange = (values: string[]) => {
    setDataset4Selections(values);
    updateForm({ dataset4SchemaMultiselect: values });
  };

  // Effect to reset Datasets 3 and 4 when splitType changes
  useEffect(() => {
    if (splitType !== "VerticalHorizontal") {
      // Reset Dataset 3
      setDataset3Enabled(false);
      setDataset3Level(0);
      setDataset3KeyEnabled(false);
      setDataset3DeleteSchemaEnabled(false);
      setDataset3Selections([]);

      // Reset Dataset 4
      setDataset4Enabled(false);
      setDataset4Level(0);
      setDataset4KeyEnabled(false);
      setDataset4DeleteSchemaEnabled(false);
      setDataset4Selections([]);

      // Update form
      updateForm({
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
      });
    }
  }, [splitType, updateForm]);

  return (
    <FormWrapper
      title="Select Noise Options for Schema"
      description="For each dataset, choose whether to add noise to the schema."
    >
      <div className="flex flex-col w-full h-[750px] max-h-[35vh] p-4 scrollbar-custom">
        <div className="flex flex-col md:flex-row md:gap-8 w-full">
          {/* Dataset 1 Controls */}
          <div className="flex flex-col w-full">
            <h3 className="text-lg text-white mb-2">Dataset 1</h3>
            <div className="flex items-center gap-2 custom-label">
              <Checkbox
                checked={dataset1Enabled}
                onCheckedChange={handleDataset1Toggle}
                id="dataset1SchemaNoise"
              />
              <label htmlFor="dataset1SchemaNoise" className="text-white">
                Enable Noise
              </label>
            </div>
            {errors.dataset1SchemaNoise && (
              <p className="text-red-500 text-sm mt-1">{errors.dataset1SchemaNoise[0]}</p>
            )}

            {dataset1Enabled && (
              <>
                {/* Delete Schema Checkbox */}
                <div className="flex items-center gap-2 mt-2 custom-label">
                  <Checkbox
                    checked={dataset1DeleteSchemaEnabled}
                    onCheckedChange={handleDataset1DeleteSchemaToggle}
                    id="dataset1DeleteSchema"
                    disabled={dataset1KeyEnabled}
                  />
                  <label htmlFor="dataset1DeleteSchema" className="text-white">
                    Delete Schema
                  </label>
                </div>
                {errors.dataset1SchemaDeleteSchema && (
                  <p className="text-red-500 text-sm mt-1">
                    {errors.dataset1SchemaDeleteSchema[0]}
                  </p>
                )}

                {/* Preserve Key Constraints Checkbox */}
                <div className="flex items-center gap-2 mt-2 custom-label">
                  <Checkbox
                    checked={dataset1KeyEnabled}
                    onCheckedChange={handleDataset1KeyToggle}
                    id="dataset1SchemaKeyNoise"
                    disabled={dataset1DeleteSchemaEnabled}
                  />
                  <label htmlFor="dataset1SchemaKeyNoise" className="text-white">
                    Preserve attribute name of keys
                  </label>
                </div>
                {errors.dataset1SchemaKeyNoise && (
                  <p className="text-red-500 text-sm mt-1">
                    {errors.dataset1SchemaKeyNoise[0]}
                  </p>
                )}

                {/* Multi-Select for Dataset 1 */}
                <div className="mt-4">
                  <label htmlFor="dataset1MultiSelect" className="text-white">
                    Dataset 1 Error Methods
                  </label>
                  <MultiSelect
                    id="dataset1MultiSelect"
                    options={frameworksList}
                    onValueChange={handleDataset1MultiselectChange}
                    defaultValue={dataset1Selections}
                    placeholder="Select frameworks for Dataset 1"
                    variant="inverted"
                    animation={2}
                    maxCount={3}
                  />
                  <div className="text-white text-sm mt-2">
                    Selected: {dataset1Selections.join(", ")}
                  </div>
                </div>

                {/* Noise Percentage Slider */}
                <div className="mt-4">
                  <label htmlFor="dataset1SchemaNoiseSlider" className="text-white">
                    Noise Percentage
                  </label>
                  <Slider
                    id="dataset1SchemaNoiseSlider"
                    className="my-2 w-full"
                    value={[dataset1Level]}
                    onValueChange={handleDataset1Slider}
                    min={0}
                    max={100}
                    step={1}
                    disabled={dataset1DeleteSchemaEnabled}
                  />
                  <span className="text-white text-sm">
                    Percentage: {dataset1Level} %
                  </span>
                  {errors.dataset1SchemaNoiseValue && (
                    <p className="text-red-500 text-sm mt-1">
                      {errors.dataset1SchemaNoiseValue[0]}
                    </p>
                  )}
                </div>
              </>
            )}
          </div>

          {/* Dataset 2 Controls */}
          <div className="flex flex-col w-full">
            <h3 className="text-lg text-white mb-2">Dataset 2</h3>
            <div className="flex items-center gap-2 custom-label">
              <Checkbox
                checked={dataset2Enabled}
                onCheckedChange={handleDataset2Toggle}
                id="dataset2SchemaNoise"
              />
              <label htmlFor="dataset2SchemaNoise" className="text-white">
                Enable Noise
              </label>
            </div>
            {errors.dataset2SchemaNoise && (
              <p className="text-red-500 text-sm mt-1">{errors.dataset2SchemaNoise[0]}</p>
            )}

            {dataset2Enabled && (
              <>
                {/* Delete Schema Checkbox */}
                <div className="flex items-center gap-2 mt-2 custom-label">
                  <Checkbox
                    checked={dataset2DeleteSchemaEnabled}
                    onCheckedChange={handleDataset2DeleteSchemaToggle}
                    id="dataset2DeleteSchema"
                    disabled={dataset2KeyEnabled}
                  />
                  <label htmlFor="dataset2DeleteSchema" className="text-white">
                    Delete Schema
                  </label>
                </div>
                {errors.dataset2SchemaDeleteSchema && (
                  <p className="text-red-500 text-sm mt-1">
                    {errors.dataset2SchemaDeleteSchema[0]}
                  </p>
                )}

                {/* Preserve Key Constraints Checkbox */}
                <div className="flex items-center gap-2 mt-2 custom-label">
                  <Checkbox
                    checked={dataset2KeyEnabled}
                    onCheckedChange={handleDataset2KeyToggle}
                    id="dataset2SchemaKeyNoise"
                    disabled={dataset2DeleteSchemaEnabled}
                  />
                  <label htmlFor="dataset2SchemaKeyNoise" className="text-white">
                    Preserve attribute name of keys
                  </label>
                </div>
                {errors.dataset2SchemaKeyNoise && (
                  <p className="text-red-500 text-sm mt-1">
                    {errors.dataset2SchemaKeyNoise[0]}
                  </p>
                )}

                {/* Multi-Select for Dataset 2 */}
                <div className="mt-4">
                  <label htmlFor="dataset2MultiSelect" className="text-white">
                    Dataset 2 Error Methods
                  </label>
                  <MultiSelect
                    id="dataset2MultiSelect"
                    options={frameworksList}
                    onValueChange={handleDataset2MultiselectChange}
                    defaultValue={dataset2Selections}
                    placeholder="Select frameworks for Dataset 2"
                    variant="inverted"
                    animation={2}
                    maxCount={3}
                  />
                  <div className="text-white text-sm mt-2">
                    Selected: {dataset2Selections.join(", ")}
                  </div>
                </div>

                {/* Noise Percentage Slider */}
                <div className="mt-4">
                  <label htmlFor="dataset2SchemaNoiseSlider" className="text-white">
                    Noise Percentage
                  </label>
                  <Slider
                    id="dataset2SchemaNoiseSlider"
                    className="my-2 w-full"
                    value={[dataset2Level]}
                    onValueChange={handleDataset2Slider}
                    min={0}
                    max={100}
                    step={1}
                    disabled={dataset2DeleteSchemaEnabled}
                  />
                  <span className="text-white text-sm">
                    Percentage: {dataset2Level} %
                  </span>
                  {errors.dataset2SchemaNoiseValue && (
                    <p className="text-red-500 text-sm mt-1">
                      {errors.dataset2SchemaNoiseValue[0]}
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
            {/* Dataset 3 Controls */}
            <div className="flex flex-col w-full">
              <h3 className="text-lg text-white mb-2">Dataset 3</h3>
              <div className="flex items-center gap-2 custom-label">
                <Checkbox
                  checked={dataset3Enabled}
                  onCheckedChange={handleDataset3Toggle}
                  id="dataset3SchemaNoise"
                />
                <label htmlFor="dataset3SchemaNoise" className="text-white">
                  Enable Noise
                </label>
              </div>
              {errors.dataset3SchemaNoise && (
                <p className="text-red-500 text-sm mt-1">{errors.dataset3SchemaNoise[0]}</p>
              )}

              {dataset3Enabled && (
                <>
                  {/* Delete Schema Checkbox */}
                  <div className="flex items-center gap-2 mt-2 custom-label">
                    <Checkbox
                      checked={dataset3DeleteSchemaEnabled}
                      onCheckedChange={handleDataset3DeleteSchemaToggle}
                      id="dataset3DeleteSchema"
                      disabled={dataset3KeyEnabled}
                    />
                    <label htmlFor="dataset3DeleteSchema" className="text-white">
                      Delete Schema
                    </label>
                  </div>
                  {errors.dataset3SchemaDeleteSchema && (
                    <p className="text-red-500 text-sm mt-1">
                      {errors.dataset3SchemaDeleteSchema[0]}
                    </p>
                  )}

                  {/* Preserve Key Constraints Checkbox */}
                  <div className="flex items-center gap-2 mt-2 custom-label">
                    <Checkbox
                      checked={dataset3KeyEnabled}
                      onCheckedChange={handleDataset3KeyToggle}
                      id="dataset3SchemaKeyNoise"
                      disabled={dataset3DeleteSchemaEnabled}
                    />
                    <label htmlFor="dataset3SchemaKeyNoise" className="text-white">
                      Preserve attribute name of keys
                    </label>
                  </div>
                  {errors.dataset3SchemaKeyNoise && (
                    <p className="text-red-500 text-sm mt-1">
                      {errors.dataset3SchemaKeyNoise[0]}
                    </p>
                  )}

                  {/* Multi-Select for Dataset 3 */}
                  <div className="mt-4">
                    <label htmlFor="dataset3MultiSelect" className="text-white">
                      Dataset 3 Error Methods
                    </label>
                    <MultiSelect
                      id="dataset3MultiSelect"
                      options={frameworksList}
                      onValueChange={handleDataset3MultiselectChange}
                      defaultValue={dataset3Selections}
                      placeholder="Select frameworks for Dataset 3"
                      variant="inverted"
                      animation={2}
                      maxCount={3}
                    />
                    <div className="text-white text-sm mt-2">
                      Selected: {dataset3Selections.join(", ")}
                    </div>
                  </div>

                  {/* Noise Percentage Slider */}
                  <div className="mt-4">
                    <label htmlFor="dataset3SchemaNoiseSlider" className="text-white">
                      Noise Percentage
                    </label>
                    <Slider
                      id="dataset3SchemaNoiseSlider"
                      className="my-2 w-full"
                      value={[dataset3Level]}
                      onValueChange={handleDataset3Slider}
                      min={0}
                      max={100}
                      step={1}
                      disabled={dataset3DeleteSchemaEnabled}
                    />
                    <span className="text-white text-sm">
                      Percentage: {dataset3Level} %
                    </span>
                    {errors.dataset3SchemaNoiseValue && (
                      <p className="text-red-500 text-sm mt-1">
                        {errors.dataset3SchemaNoiseValue[0]}
                      </p>
                    )}
                  </div>
                </>
              )}
            </div>

            {/* Dataset 4 Controls */}
            <div className="flex flex-col w-full">
              <h3 className="text-lg text-white mb-2">Dataset 4</h3>
              <div className="flex items-center gap-2 custom-label">
                <Checkbox
                  checked={dataset4Enabled}
                  onCheckedChange={handleDataset4Toggle}
                  id="dataset4SchemaNoise"
                />
                <label htmlFor="dataset4SchemaNoise" className="text-white">
                  Enable Noise
                </label>
              </div>
              {errors.dataset4SchemaNoise && (
                <p className="text-red-500 text-sm mt-1">{errors.dataset4SchemaNoise[0]}</p>
              )}

              {dataset4Enabled && (
                <>
                  {/* Delete Schema Checkbox */}
                  <div className="flex items-center gap-2 mt-2 custom-label">
                    <Checkbox
                      checked={dataset4DeleteSchemaEnabled}
                      onCheckedChange={handleDataset4DeleteSchemaToggle}
                      id="dataset4DeleteSchema"
                      disabled={dataset4KeyEnabled}
                    />
                    <label htmlFor="dataset4DeleteSchema" className="text-white">
                      Delete Schema
                    </label>
                  </div>
                  {errors.dataset4SchemaDeleteSchema && (
                    <p className="text-red-500 text-sm mt-1">
                      {errors.dataset4SchemaDeleteSchema[0]}
                    </p>
                  )}

                  {/* Preserve Key Constraints Checkbox */}
                  <div className="flex items-center gap-2 mt-2 custom-label">
                    <Checkbox
                      checked={dataset4KeyEnabled}
                      onCheckedChange={handleDataset4KeyToggle}
                      id="dataset4SchemaKeyNoise"
                      disabled={dataset4DeleteSchemaEnabled}
                    />
                    <label htmlFor="dataset4SchemaKeyNoise" className="text-white">
                      Preserve attribute name of keys
                    </label>
                  </div>
                  {errors.dataset4SchemaKeyNoise && (
                    <p className="text-red-500 text-sm mt-1">
                      {errors.dataset4SchemaKeyNoise[0]}
                    </p>
                  )}

                  {/* Multi-Select for Dataset 4 */}
                  <div className="mt-4">
                    <label htmlFor="dataset4MultiSelect" className="text-white">
                      Dataset 4 Error Methods
                    </label>
                    <MultiSelect
                      id="dataset4MultiSelect"
                      options={frameworksList}
                      onValueChange={handleDataset4MultiselectChange}
                      defaultValue={dataset4Selections}
                      placeholder="Select frameworks for Dataset 4"
                      variant="inverted"
                      animation={2}
                      maxCount={3}
                    />
                    <div className="text-white text-sm mt-2">
                      Selected: {dataset4Selections.join(", ")}
                    </div>
                  </div>

                  {/* Noise Percentage Slider */}
                  <div className="mt-4">
                    <label htmlFor="dataset4SchemaNoiseSlider" className="text-white">
                      Noise Percentage
                    </label>
                    <Slider
                      id="dataset4SchemaNoiseSlider"
                      className="my-2 w-full"
                      value={[dataset4Level]}
                      onValueChange={handleDataset4Slider}
                      min={0}
                      max={100}
                      step={1}
                      disabled={dataset4DeleteSchemaEnabled}
                    />
                    <span className="text-white text-sm">
                      Percentage: {dataset4Level} %
                    </span>
                    {errors.dataset4SchemaNoiseValue && (
                      <p className="text-red-500 text-sm mt-1">
                        {errors.dataset4SchemaNoiseValue[0]}
                      </p>
                    )}
                  </div>
                </>
              )}
            </div>
          </div>
        )}
      </div>
    </FormWrapper>
  );
};

export default Step5_SchemaNoiseForm;
