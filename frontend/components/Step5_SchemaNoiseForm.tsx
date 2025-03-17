"use client";
import React, { useState, useEffect } from "react";
import { Checkbox } from "@/components/ui/checkbox";
import { Slider } from "@/components/ui/slider";
import FormWrapper from "./FormWrapper";
import { FormItems } from "@/components/types/formTypes";
import { MultiSelect } from "@/components/ui/multi-select";

// Multi-select options
const frameworksList = [
  { value: "generateRandomString", label: "generateRandomString" },
  { value: "abbreviateFirstLetters", label: "abbreviateFirstLetters" },
  { value: "abbreviateRandomLength", label: "abbreviateRandomLength" },
  { value: "addRandomPrefix", label: "addRandomPrefix" },
  { value: "shuffleLetters", label: "shuffleLetters" },
  { value: "shuffleWords", label: "shuffleWords" },
  { value: "replaceWithSynonyms", label: "replaceWithSynonyms" },
  { value: "replaceWithTranslation", label: "replaceWithTranslation" },
  { value: "removeVowels", label: "removeVowels" }
];

// Define the types for the props 
type StepProps = {
  splitType: "Horizontal" | "Vertical" | "VerticalHorizontal" | null;

  // Dataset A
  datasetASchemaNoise: boolean | null;
  datasetASchemaNoiseValue: number | null;
  datasetASchemaKeyNoise: boolean | null;
  datasetASchemaDeleteSchema: boolean | null;
  datasetASchemaMultiselect: string[] | null;

  // Dataset B
  datasetBSchemaNoise: boolean | null;
  datasetBSchemaNoiseValue: number | null;
  datasetBSchemaKeyNoise: boolean | null;
  datasetBSchemaDeleteSchema: boolean | null;
  datasetBSchemaMultiselect: string[] | null;

  // Dataset C
  datasetCSchemaNoise: boolean | null;
  datasetCSchemaNoiseValue: number | null;
  datasetCSchemaKeyNoise: boolean | null;
  datasetCSchemaDeleteSchema: boolean | null;
  datasetCSchemaMultiselect: string[] | null;

  // Dataset D
  datasetDSchemaNoise: boolean | null;
  datasetDSchemaNoiseValue: number | null;
  datasetDSchemaKeyNoise: boolean | null;
  datasetDSchemaDeleteSchema: boolean | null;
  datasetDSchemaMultiselect: string[] | null;

  updateForm: (fieldToUpdate: Partial<FormItems>) => void;
  errors: Record<string, string[]>;
};

const Step5_SchemaNoiseForm = ({
                                 splitType,
                                 updateForm,
                                 // Dataset A
                                 datasetASchemaNoise,
                                 datasetASchemaNoiseValue,
                                 datasetASchemaKeyNoise,
                                 datasetASchemaDeleteSchema,
                                 datasetASchemaMultiselect,
                                 // Dataset B
                                 datasetBSchemaNoise,
                                 datasetBSchemaNoiseValue,
                                 datasetBSchemaKeyNoise,
                                 datasetBSchemaDeleteSchema,
                                 datasetBSchemaMultiselect,
                                 // Dataset C
                                 datasetCSchemaNoise,
                                 datasetCSchemaNoiseValue,
                                 datasetCSchemaKeyNoise,
                                 datasetCSchemaDeleteSchema,
                                 datasetCSchemaMultiselect,
                                 // Dataset D
                                 datasetDSchemaNoise,
                                 datasetDSchemaNoiseValue,
                                 datasetDSchemaKeyNoise,
                                 datasetDSchemaDeleteSchema,
                                 datasetDSchemaMultiselect,
                                 errors,
                               }: StepProps) => {
  // Helper function for exclusivity
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

  // Dataset A State
  const [datasetAEnabled, setDatasetAEnabled] = useState<boolean>(!!datasetASchemaNoise);
  const [datasetALevel, setDatasetALevel] = useState<number>(datasetASchemaNoiseValue ?? 0);
  const [datasetAKeyEnabled, setDatasetAKeyEnabled] = useState<boolean>(!!datasetASchemaKeyNoise);
  const [datasetADeleteSchemaEnabled, setDatasetADeleteSchemaEnabled] = useState<boolean>(
      !!datasetASchemaDeleteSchema
  );
  const [datasetASelections, setDatasetASelections] = useState<string[]>(
      datasetASchemaMultiselect ?? []
  );

  // Dataset B State
  const [datasetBEnabled, setDatasetBEnabled] = useState<boolean>(!!datasetBSchemaNoise);
  const [datasetBLevel, setDatasetBLevel] = useState<number>(datasetBSchemaNoiseValue ?? 0);
  const [datasetBKeyEnabled, setDatasetBKeyEnabled] = useState<boolean>(!!datasetBSchemaKeyNoise);
  const [datasetBDeleteSchemaEnabled, setDatasetBDeleteSchemaEnabled] = useState<boolean>(
      !!datasetBSchemaDeleteSchema
  );

  const [datasetBSelections, setDatasetBSelections] = useState<string[]>(
      datasetBSchemaMultiselect ?? []
  );

  // Dataset C State
  const [datasetCEnabled, setDatasetCEnabled] = useState<boolean>(!!datasetCSchemaNoise);
  const [datasetCLevel, setDatasetCLevel] = useState<number>(datasetCSchemaNoiseValue ?? 0);
  const [datasetCKeyEnabled, setDatasetCKeyEnabled] = useState<boolean>(!!datasetCSchemaKeyNoise);
  const [datasetCDeleteSchemaEnabled, setDatasetCDeleteSchemaEnabled] = useState<boolean>(
      !!datasetCSchemaDeleteSchema
  );

  const [datasetCSelections, setDatasetCSelections] = useState<string[]>(
      datasetCSchemaMultiselect ?? []
  );

  // Dataset D State
  const [datasetDEnabled, setDatasetDEnabled] = useState<boolean>(!!datasetDSchemaNoise);
  const [datasetDLevel, setDatasetDLevel] = useState<number>(datasetDSchemaNoiseValue ?? 0);
  const [datasetDKeyEnabled, setDatasetDKeyEnabled] = useState<boolean>(!!datasetDSchemaKeyNoise);
  const [datasetDDeleteSchemaEnabled, setDatasetDDeleteSchemaEnabled] = useState<boolean>(
      !!datasetDSchemaDeleteSchema
  );

  const [datasetDSelections, setDatasetDSelections] = useState<string[]>(
      datasetDSchemaMultiselect ?? []
  );

  // Dataset A
  const handleDatasetAToggle = (checked: boolean) => {
    setDatasetAEnabled(checked);
    if (!checked) {
      setDatasetALevel(0);
      setDatasetAKeyEnabled(false);
      setDatasetADeleteSchemaEnabled(false);
      setDatasetASelections([]);
      updateForm({
        datasetASchemaNoise: checked,
        datasetASchemaNoiseValue: 0,
        datasetASchemaKeyNoise: false,
        datasetASchemaDeleteSchema: false,
        datasetASchemaMultiselect: [],
      });
    } else {
      updateForm({ datasetASchemaNoise: checked });
    }
  };

  const handleDatasetASlider = (value: number[]) => {
    const newLevel = value[0];
    setDatasetALevel(newLevel);
    updateForm({ datasetASchemaNoiseValue: newLevel });
  };

  const handleDatasetAKeyToggle = (checked: boolean) => {
    setDatasetAKeyEnabled(checked);
    updateForm({ datasetASchemaKeyNoise: checked });
    handleMutualExclusivity(
        setDatasetAKeyEnabled,
        setDatasetADeleteSchemaEnabled,
        (val: boolean) => updateForm({ datasetASchemaKeyNoise: val }),
        (val: boolean) => updateForm({ datasetASchemaDeleteSchema: val }),
        checked,
        datasetADeleteSchemaEnabled
    );
  };

  const handleDatasetADeleteSchemaToggle = (checked: boolean) => {
    setDatasetADeleteSchemaEnabled(checked);
    updateForm({ datasetASchemaDeleteSchema: checked });
    handleMutualExclusivity(
        setDatasetAKeyEnabled,
        setDatasetADeleteSchemaEnabled,
        (val: boolean) => updateForm({ datasetASchemaKeyNoise: val }),
        (val: boolean) => updateForm({ datasetASchemaDeleteSchema: val }),
        datasetAKeyEnabled,
        checked
    );
  };

  // handle multi-select changes for dataset A
  const handleDatasetAMultiselectChange = (values: string[]) => {
    setDatasetASelections(values);
    updateForm({ datasetASchemaMultiselect: values });
  };

  // Dataset B
  const handleDatasetBToggle = (checked: boolean) => {
    setDatasetBEnabled(checked);
    if (!checked) {
      setDatasetBLevel(0);
      setDatasetBKeyEnabled(false);
      setDatasetBDeleteSchemaEnabled(false);
      setDatasetBSelections([]);
      updateForm({
        datasetBSchemaNoise: checked,
        datasetBSchemaNoiseValue: 0,
        datasetBSchemaKeyNoise: false,
        datasetBSchemaDeleteSchema: false,
        datasetBSchemaMultiselect: [],
      });
    } else {
      updateForm({ datasetBSchemaNoise: checked });
    }
  };

  const handleDatasetBSlider = (value: number[]) => {
    const newLevel = value[0];
    setDatasetBLevel(newLevel);
    updateForm({ datasetBSchemaNoiseValue: newLevel });
  };

  const handleDatasetBKeyToggle = (checked: boolean) => {
    setDatasetBKeyEnabled(checked);
    updateForm({ datasetBSchemaKeyNoise: checked });
    handleMutualExclusivity(
        setDatasetBKeyEnabled,
        setDatasetBDeleteSchemaEnabled,
        (val: boolean) => updateForm({ datasetBSchemaKeyNoise: val }),
        (val: boolean) => updateForm({ datasetBSchemaDeleteSchema: val }),
        checked,
        datasetBDeleteSchemaEnabled
    );
  };

  const handleDatasetBDeleteSchemaToggle = (checked: boolean) => {
    setDatasetBDeleteSchemaEnabled(checked);
    updateForm({ datasetBSchemaDeleteSchema: checked });
    handleMutualExclusivity(
        setDatasetBKeyEnabled,
        setDatasetBDeleteSchemaEnabled,
        (val: boolean) => updateForm({ datasetBSchemaKeyNoise: val }),
        (val: boolean) => updateForm({ datasetBSchemaDeleteSchema: val }),
        datasetBKeyEnabled,
        checked
    );
  };

  // handle multi-select changes for dataset B
  const handleDatasetBMultiselectChange = (values: string[]) => {
    setDatasetBSelections(values);
    updateForm({ datasetBSchemaMultiselect: values });
  };

  // Dataset C
  const handleDatasetCToggle = (checked: boolean) => {
    setDatasetCEnabled(checked);
    if (!checked) {
      setDatasetCLevel(0);
      setDatasetCKeyEnabled(false);
      setDatasetCDeleteSchemaEnabled(false);
      setDatasetCSelections([]);
      updateForm({
        datasetCSchemaNoise: checked,
        datasetCSchemaNoiseValue: 0,
        datasetCSchemaKeyNoise: false,
        datasetCSchemaDeleteSchema: false,
        datasetCSchemaMultiselect: [],
      });
    } else {
      updateForm({ datasetCSchemaNoise: checked });
    }
  };

  const handleDatasetCSlider = (value: number[]) => {
    const newLevel = value[0];
    setDatasetCLevel(newLevel);
    updateForm({ datasetCSchemaNoiseValue: newLevel });
  };

  const handleDatasetCKeyToggle = (checked: boolean) => {
    setDatasetCKeyEnabled(checked);
    updateForm({ datasetCSchemaKeyNoise: checked });
    handleMutualExclusivity(
        setDatasetCKeyEnabled,
        setDatasetCDeleteSchemaEnabled,
        (val: boolean) => updateForm({ datasetCSchemaKeyNoise: val }),
        (val: boolean) => updateForm({ datasetCSchemaDeleteSchema: val }),
        checked,
        datasetCDeleteSchemaEnabled
    );
  };

  const handleDatasetCDeleteSchemaToggle = (checked: boolean) => {
    setDatasetCDeleteSchemaEnabled(checked);
    updateForm({ datasetCSchemaDeleteSchema: checked });
    handleMutualExclusivity(
        setDatasetCKeyEnabled,
        setDatasetCDeleteSchemaEnabled,
        (val: boolean) => updateForm({ datasetCSchemaKeyNoise: val }),
        (val: boolean) => updateForm({ datasetCSchemaDeleteSchema: val }),
        datasetCKeyEnabled,
        checked
    );
  };

  // handle multi-select changes for dataset C
  const handleDatasetCMultiselectChange = (values: string[]) => {
    setDatasetCSelections(values);
    updateForm({ datasetCSchemaMultiselect: values });
  };

  // Dataset D
  const handleDatasetDToggle = (checked: boolean) => {
    setDatasetDEnabled(checked);
    if (!checked) {
      setDatasetDLevel(0);
      setDatasetDKeyEnabled(false);
      setDatasetDDeleteSchemaEnabled(false);
      setDatasetDSelections([]);
      updateForm({
        datasetDSchemaNoise: checked,
        datasetDSchemaNoiseValue: 0,
        datasetDSchemaKeyNoise: false,
        datasetDSchemaDeleteSchema: false,
        datasetDSchemaMultiselect: [],
      });
    } else {
      updateForm({ datasetDSchemaNoise: checked });
    }
  };

  const handleDatasetDSlider = (value: number[]) => {
    const newLevel = value[0];
    setDatasetDLevel(newLevel);
    updateForm({ datasetDSchemaNoiseValue: newLevel });
  };

  const handleDatasetDKeyToggle = (checked: boolean) => {
    setDatasetDKeyEnabled(checked);
    updateForm({ datasetDSchemaKeyNoise: checked });
    handleMutualExclusivity(
        setDatasetDKeyEnabled,
        setDatasetDDeleteSchemaEnabled,
        (val: boolean) => updateForm({ datasetDSchemaKeyNoise: val }),
        (val: boolean) => updateForm({ datasetDSchemaDeleteSchema: val }),
        checked,
        datasetDDeleteSchemaEnabled
    );
  };

  const handleDatasetDDeleteSchemaToggle = (checked: boolean) => {
    setDatasetDDeleteSchemaEnabled(checked);
    updateForm({ datasetDSchemaDeleteSchema: checked });
    handleMutualExclusivity(
        setDatasetDKeyEnabled,
        setDatasetDDeleteSchemaEnabled,
        (val: boolean) => updateForm({ datasetDSchemaKeyNoise: val }),
        (val: boolean) => updateForm({ datasetDSchemaDeleteSchema: val }),
        datasetDKeyEnabled,
        checked
    );
  };

  // handle multi-select changes for dataset D
  const handleDatasetDMultiselectChange = (values: string[]) => {
    setDatasetDSelections(values);
    updateForm({ datasetDSchemaMultiselect: values });
  };

  // Effect to reset Datasets C and D when splitType changes
  useEffect(() => {
    if (splitType !== "VerticalHorizontal") {
      // Reset Dataset C
      setDatasetCEnabled(false);
      setDatasetCLevel(0);
      setDatasetCKeyEnabled(false);
      setDatasetCDeleteSchemaEnabled(false);
      setDatasetCSelections([]);

      // Reset Dataset D
      setDatasetDEnabled(false);
      setDatasetDLevel(0);
      setDatasetDKeyEnabled(false);
      setDatasetDDeleteSchemaEnabled(false);
      setDatasetDSelections([]);

      // Update form
      updateForm({
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
      });
    }
  }, [splitType, updateForm]);

  return (
      <FormWrapper
          title="Select Noise Options for Schema"
          description="For each dataset, choose whether to add noise to the schema. If enabled, specify the error methods."
      >
        <div className="max-h-[750px] overflow-y-auto scrollbar-custom p-4 space-y-6">
          <div className="flex flex-col w-full h-full max-h-[50vh] p-4 scrollbar-custom">
            <div className="flex flex-col md:flex-row md:gap-8 w-full">
              {/* Dataset A Controls */}
              <div className="flex flex-col w-full">
                <h3 className="text-lg text-white mb-2">Dataset A</h3>
                <div className="flex items-center gap-2 custom-label">
                  <Checkbox
                      checked={datasetAEnabled}
                      onCheckedChange={handleDatasetAToggle}
                      id="datasetASchemaNoise"
                  />
                  <label htmlFor="datasetASchemaNoise" className="text-white">
                    Enable Noise
                  </label>
                </div>
                {errors.datasetASchemaNoise && (
                    <p className="text-red-500 text-sm mt-1">{errors.datasetASchemaNoise[0]}</p>
                )}

                {datasetAEnabled && (
                    <>
                      {/* Delete Schema Checkbox */}
                      <div className="flex items-center gap-2 mt-2 custom-label">
                        <Checkbox
                            checked={datasetADeleteSchemaEnabled}
                            onCheckedChange={handleDatasetADeleteSchemaToggle}
                            id="datasetADeleteSchema"
                            disabled={datasetAKeyEnabled}
                        />
                        <label htmlFor="datasetADeleteSchema" className="text-white">
                          Delete Schema
                        </label>
                      </div>
                      {errors.datasetASchemaDeleteSchema && (
                          <p className="text-red-500 text-sm mt-1">
                            {errors.datasetASchemaDeleteSchema[0]}
                          </p>
                      )}

                      {/* Noise to Key Attributes Checkbox */}
                      <div className="flex items-center gap-2 mt-2 custom-label">
                        <Checkbox
                            checked={datasetAKeyEnabled}
                            onCheckedChange={handleDatasetAKeyToggle}
                            id="datasetASchemaKeyNoise"
                            disabled={datasetADeleteSchemaEnabled}
                        />
                        <label htmlFor="datasetASchemaKeyNoise" className="text-white">
                          Add Noise to Key Attributes
                        </label>
                      </div>
                      {errors.datasetASchemaKeyNoise && (
                          <p className="text-red-500 text-sm mt-1">
                            {errors.datasetASchemaKeyNoise[0]}
                          </p>
                      )}

                      {/* Multi-Select for Dataset A */}
                      <div className="mt-4">
                        <label htmlFor="datasetAMultiSelect" className="text-white">
                          Dataset A Error Methods
                        </label>
                        <MultiSelect
                            id="datasetAMultiSelect"
                            options={frameworksList}
                            onValueChange={handleDatasetAMultiselectChange}
                            defaultValue={datasetASelections}
                            placeholder="Select methods for Dataset A"
                            variant="inverted"
                            animation={2}
                            maxCount={3}
                        />
                        <div className="text-white text-sm mt-2">
                          Selected: {datasetASelections.join(", ")}
                        </div>
                      </div>

                      {/* Noise Percentage Slider */}
                      <div className="mt-4">
                        <label htmlFor="datasetASchemaNoiseSlider" className="text-white">
                          Noise Percentage
                        </label>
                        <Slider
                            id="datasetASchemaNoiseSlider"
                            className="my-2 w-full"
                            value={[datasetALevel]}
                            onValueChange={handleDatasetASlider}
                            min={0}
                            max={100}
                            step={1}
                            disabled={datasetADeleteSchemaEnabled}
                        />
                        <span className="text-white text-sm">
                    Percentage: {datasetALevel} %
                  </span>
                        {errors.datasetASchemaNoiseValue && (
                            <p className="text-red-500 text-sm mt-1">
                              {errors.datasetASchemaNoiseValue[0]}
                            </p>
                        )}
                      </div>
                    </>
                )}
              </div>

              {/* Dataset B Controls */}
              <div className="flex flex-col w-full">
                <h3 className="text-lg text-white mb-2">Dataset B</h3>
                <div className="flex items-center gap-2 custom-label">
                  <Checkbox
                      checked={datasetBEnabled}
                      onCheckedChange={handleDatasetBToggle}
                      id="datasetBSchemaNoise"
                  />
                  <label htmlFor="datasetBSchemaNoise" className="text-white">
                    Enable Noise
                  </label>
                </div>
                {errors.datasetBSchemaNoise && (
                    <p className="text-red-500 text-sm mt-1">{errors.datasetBSchemaNoise[0]}</p>
                )}

                {datasetBEnabled && (
                    <>
                      {/* Delete Schema Checkbox */}
                      <div className="flex items-center gap-2 mt-2 custom-label">
                        <Checkbox
                            checked={datasetBDeleteSchemaEnabled}
                            onCheckedChange={handleDatasetBDeleteSchemaToggle}
                            id="datasetBDeleteSchema"
                            disabled={datasetBKeyEnabled}
                        />
                        <label htmlFor="datasetBDeleteSchema" className="text-white">
                          Delete Schema
                        </label>
                      </div>
                      {errors.datasetBSchemaDeleteSchema && (
                          <p className="text-red-500 text-sm mt-1">
                            {errors.datasetBSchemaDeleteSchema[0]}
                          </p>
                      )}

                      {/* Noise to Key Attributes Checkbox */}
                      <div className="flex items-center gap-2 mt-2 custom-label">
                        <Checkbox
                            checked={datasetBKeyEnabled}
                            onCheckedChange={handleDatasetBKeyToggle}
                            id="datasetBSchemaKeyNoise"
                            disabled={datasetBDeleteSchemaEnabled}
                        />
                        <label htmlFor="datasetBSchemaKeyNoise" className="text-white">
                          Add Noise to Key Attributes
                        </label>
                      </div>
                      {errors.datasetBSchemaKeyNoise && (
                          <p className="text-red-500 text-sm mt-1">
                            {errors.datasetBSchemaKeyNoise[0]}
                          </p>
                      )}

                      {/* Multi-Select for Dataset B */}
                      <div className="mt-4">
                        <label htmlFor="datasetBMultiSelect" className="text-white">
                          Dataset B Error Methods
                        </label>
                        <MultiSelect
                            id="datasetBMultiSelect"
                            options={frameworksList}
                            onValueChange={handleDatasetBMultiselectChange}
                            defaultValue={datasetBSelections}
                            placeholder="Select methods for Dataset B"
                            variant="inverted"
                            animation={2}
                            maxCount={3}
                        />
                        <div className="text-white text-sm mt-2">
                          Selected: {datasetBSelections.join(", ")}
                        </div>
                      </div>

                      {/* Noise Percentage Slider */}
                      <div className="mt-4">
                        <label htmlFor="datasetBSchemaNoiseSlider" className="text-white">
                          Noise Percentage
                        </label>
                        <Slider
                            id="datasetBSchemaNoiseSlider"
                            className="my-2 w-full"
                            value={[datasetBLevel]}
                            onValueChange={handleDatasetBSlider}
                            min={0}
                            max={100}
                            step={1}
                            disabled={datasetBDeleteSchemaEnabled}
                        />
                        <span className="text-white text-sm">
                    Percentage: {datasetBLevel} %
                  </span>
                        {errors.datasetBSchemaNoiseValue && (
                            <p className="text-red-500 text-sm mt-1">
                              {errors.datasetBSchemaNoiseValue[0]}
                            </p>
                        )}
                      </div>
                    </>
                )}
              </div>
            </div>

            {/* Conditionally Render Dataset C and Dataset D Controls */}
            {splitType === "VerticalHorizontal" && (
                <div className="flex flex-col md:flex-row md:gap-8 w-full mt-8">
                  {/* Dataset C Controls */}
                  <div className="flex flex-col w-full">
                    <h3 className="text-lg text-white mb-2">Dataset C</h3>
                    <div className="flex items-center gap-2 custom-label">
                      <Checkbox
                          checked={datasetCEnabled}
                          onCheckedChange={handleDatasetCToggle}
                          id="datasetCSchemaNoise"
                      />
                      <label htmlFor="datasetCSchemaNoise" className="text-white">
                        Enable Noise
                      </label>
                    </div>
                    {errors.datasetCSchemaNoise && (
                        <p className="text-red-500 text-sm mt-1">{errors.datasetCSchemaNoise[0]}</p>
                    )}

                    {datasetCEnabled && (
                        <>
                          {/* Delete Schema Checkbox */}
                          <div className="flex items-center gap-2 mt-2 custom-label">
                            <Checkbox
                                checked={datasetCDeleteSchemaEnabled}
                                onCheckedChange={handleDatasetCDeleteSchemaToggle}
                                id="datasetCDeleteSchema"
                                disabled={datasetCKeyEnabled}
                            />
                            <label htmlFor="datasetCDeleteSchema" className="text-white">
                              Delete Schema
                            </label>
                          </div>
                          {errors.datasetCSchemaDeleteSchema && (
                              <p className="text-red-500 text-sm mt-1">
                                {errors.datasetCSchemaDeleteSchema[0]}
                              </p>
                          )}

                          {/* Noise to Key Attributes Checkbox */}
                          <div className="flex items-center gap-2 mt-2 custom-label">
                            <Checkbox
                                checked={datasetCKeyEnabled}
                                onCheckedChange={handleDatasetCKeyToggle}
                                id="datasetCSchemaKeyNoise"
                                disabled={datasetCDeleteSchemaEnabled}
                            />
                            <label htmlFor="datasetCSchemaKeyNoise" className="text-white">
                              Add Noise to Key Attributes
                            </label>
                          </div>
                          {errors.datasetCSchemaKeyNoise && (
                              <p className="text-red-500 text-sm mt-1">
                                {errors.datasetCSchemaKeyNoise[0]}
                              </p>
                          )}

                          {/* Multi-Select for Dataset C */}
                          <div className="mt-4">
                            <label htmlFor="datasetCMultiSelect" className="text-white">
                              Dataset C Error Methods
                            </label>
                            <MultiSelect
                                id="datasetCMultiSelect"
                                options={frameworksList}
                                onValueChange={handleDatasetCMultiselectChange}
                                defaultValue={datasetCSelections}
                                placeholder="Select methods for Dataset C"
                                variant="inverted"
                                animation={2}
                                maxCount={3}
                            />
                            <div className="text-white text-sm mt-2">
                              Selected: {datasetCSelections.join(", ")}
                            </div>
                          </div>

                          {/* Noise Percentage Slider */}
                          <div className="mt-4">
                            <label htmlFor="datasetCSchemaNoiseSlider" className="text-white">
                              Noise Percentage
                            </label>
                            <Slider
                                id="datasetCSchemaNoiseSlider"
                                className="my-2 w-full"
                                value={[datasetCLevel]}
                                onValueChange={handleDatasetCSlider}
                                min={0}
                                max={100}
                                step={1}
                                disabled={datasetCDeleteSchemaEnabled}
                            />
                            <span className="text-white text-sm">
                      Percentage: {datasetCLevel} %
                    </span>
                            {errors.datasetCSchemaNoiseValue && (
                                <p className="text-red-500 text-sm mt-1">
                                  {errors.datasetCSchemaNoiseValue[0]}
                                </p>
                            )}
                          </div>
                        </>
                    )}
                  </div>

                  {/* Dataset D Controls */}
                  <div className="flex flex-col w-full">
                    <h3 className="text-lg text-white mb-2">Dataset D</h3>
                    <div className="flex items-center gap-2 custom-label">
                      <Checkbox
                          checked={datasetDEnabled}
                          onCheckedChange={handleDatasetDToggle}
                          id="datasetDSchemaNoise"
                      />
                      <label htmlFor="datasetDSchemaNoise" className="text-white">
                        Enable Noise
                      </label>
                    </div>
                    {errors.datasetDSchemaNoise && (
                        <p className="text-red-500 text-sm mt-1">{errors.datasetDSchemaNoise[0]}</p>
                    )}

                    {datasetDEnabled && (
                        <>
                          {/* Delete Schema Checkbox */}
                          <div className="flex items-center gap-2 mt-2 custom-label">
                            <Checkbox
                                checked={datasetDDeleteSchemaEnabled}
                                onCheckedChange={handleDatasetDDeleteSchemaToggle}
                                id="datasetDDeleteSchema"
                                disabled={datasetDKeyEnabled}
                            />
                            <label htmlFor="datasetDDeleteSchema" className="text-white">
                              Delete Schema
                            </label>
                          </div>
                          {errors.datasetDSchemaDeleteSchema && (
                              <p className="text-red-500 text-sm mt-1">
                                {errors.datasetDSchemaDeleteSchema[0]}
                              </p>
                          )}

                          {/* Noise to Key Attributes Checkbox */}
                          <div className="flex items-center gap-2 mt-2 custom-label">
                            <Checkbox
                                checked={datasetDKeyEnabled}
                                onCheckedChange={handleDatasetDKeyToggle}
                                id="datasetDSchemaKeyNoise"
                                disabled={datasetDDeleteSchemaEnabled}
                            />
                            <label htmlFor="datasetDSchemaKeyNoise" className="text-white">
                              Add Noise to Key Attributes
                            </label>
                          </div>
                          {errors.datasetDSchemaKeyNoise && (
                              <p className="text-red-500 text-sm mt-1">
                                {errors.datasetDSchemaKeyNoise[0]}
                              </p>
                          )}

                          {/* Multi-Select for Dataset D */}
                          <div className="mt-4">
                            <label htmlFor="datasetDMultiSelect" className="text-white">
                              Dataset D Error Methods
                            </label>
                            <MultiSelect
                                id="datasetDMultiSelect"
                                options={frameworksList}
                                onValueChange={handleDatasetDMultiselectChange}
                                defaultValue={datasetDSelections}
                                placeholder="Select methods for Dataset D"
                                variant="inverted"
                                animation={2}
                                maxCount={3}
                            />
                            <div className="text-white text-sm mt-2">
                              Selected: {datasetDSelections.join(", ")}
                            </div>
                          </div>

                          {/* Noise Percentage Slider */}
                          <div className="mt-4">
                            <label htmlFor="datasetDSchemaNoiseSlider" className="text-white">
                              Noise Percentage
                            </label>
                            <Slider
                                id="datasetDSchemaNoiseSlider"
                                className="my-2 w-full"
                                value={[datasetDLevel]}
                                onValueChange={handleDatasetDSlider}
                                min={0}
                                max={100}
                                step={1}
                                disabled={datasetDDeleteSchemaEnabled}
                            />
                            <span className="text-white text-sm">
                      Percentage: {datasetDLevel} %
                    </span>
                            {errors.datasetDSchemaNoiseValue && (
                                <p className="text-red-500 text-sm mt-1">
                                  {errors.datasetDSchemaNoiseValue[0]}
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

export default Step5_SchemaNoiseForm;
