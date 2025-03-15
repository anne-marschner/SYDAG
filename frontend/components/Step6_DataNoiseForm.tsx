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

// Define the types for the props 
type StepProps = {
  splitType: "Horizontal" | "Vertical" | "VerticalHorizontal" | null;

  // Dataset A
  datasetADataNoise: boolean | null;
  datasetADataNoiseValue: number | null;
  datasetADataKeyNoise: boolean | null;
  datasetADataNoiseInside: number | null;
  datasetADataMultiselect: string[] | null;

  // Dataset B
  datasetBDataNoise: boolean | null;
  datasetBDataNoiseValue: number | null;
  datasetBDataKeyNoise: boolean | null;
  datasetBDataNoiseInside: number | null;
  datasetBDataMultiselect: string[] | null;

  // Dataset C
  datasetCDataNoise: boolean | null;
  datasetCDataNoiseValue: number | null;
  datasetCDataKeyNoise: boolean | null;
  datasetCDataNoiseInside: number | null;
  datasetCDataMultiselect: string[] | null;

  // Dataset D
  datasetDDataNoise: boolean | null;
  datasetDDataNoiseValue: number | null;
  datasetDDataKeyNoise: boolean | null;
  datasetDDataNoiseInside: number | null;
  datasetDDataMultiselect: string[] | null;

  updateForm: (fieldToUpdate: Partial<FormItems>) => void;
  errors: Record<string, string[]>;
};

const Step6_DataNoiseForm = ({
                               splitType,
                               updateForm,
                               // Dataset A
                               datasetADataNoise,
                               datasetADataNoiseValue,
                               datasetADataKeyNoise,
                               datasetADataNoiseInside,
                               datasetADataMultiselect,
                               // Dataset B
                               datasetBDataNoise,
                               datasetBDataNoiseValue,
                               datasetBDataKeyNoise,
                               datasetBDataNoiseInside,
                               datasetBDataMultiselect,
                               // Dataset C
                               datasetCDataNoise,
                               datasetCDataNoiseValue,
                               datasetCDataKeyNoise,
                               datasetCDataNoiseInside,
                               datasetCDataMultiselect,
                               // Dataset D
                               datasetDDataNoise,
                               datasetDDataNoiseValue,
                               datasetDDataKeyNoise,
                               datasetDDataNoiseInside,
                               datasetDDataMultiselect,
                               errors,
                             }: StepProps) => {
  // Dataset A State
  const [datasetAEnabled, setDatasetAEnabled] = useState<boolean>(!!datasetADataNoise);
  const [datasetALevel, setDatasetALevel] = useState<number>(datasetADataNoiseValue ?? 0);
  const [datasetAKeyEnabled, setDatasetAKeyEnabled] = useState<boolean>(!!datasetADataKeyNoise);
  const [datasetADataNoiseInsideState, setDatasetADataNoiseInsideState] = useState<number>(
      datasetADataNoiseInside ?? 0
  );

  const [datasetASelections, setDatasetASelections] = useState<string[]>(
      datasetADataMultiselect ?? []
  );

  // Dataset B State
  const [datasetBEnabled, setDatasetBEnabled] = useState<boolean>(!!datasetBDataNoise);
  const [datasetBLevel, setDatasetBLevel] = useState<number>(datasetBDataNoiseValue ?? 0);
  const [datasetBKeyEnabled, setDatasetBKeyEnabled] = useState<boolean>(!!datasetBDataKeyNoise);
  const [datasetBDataNoiseInsideState, setDatasetBDataNoiseInsideState] = useState<number>(
      datasetBDataNoiseInside ?? 0
  );

  const [datasetBSelections, setDatasetBSelections] = useState<string[]>(
      datasetBDataMultiselect ?? []
  );

  // Dataset C State
  const [datasetCEnabled, setDatasetCEnabled] = useState<boolean>(!!datasetCDataNoise);
  const [datasetCLevel, setDatasetCLevel] = useState<number>(datasetCDataNoiseValue ?? 0);
  const [datasetCKeyEnabled, setDatasetCKeyEnabled] = useState<boolean>(!!datasetCDataKeyNoise);
  const [datasetCDataNoiseInsideState, setDatasetCDataNoiseInsideState] = useState<number>(
      datasetCDataNoiseInside ?? 0
  );

  const [datasetCSelections, setDatasetCSelections] = useState<string[]>(
      datasetCDataMultiselect ?? []
  );

  // Dataset D State
  const [datasetDEnabled, setDatasetDEnabled] = useState<boolean>(!!datasetDDataNoise);
  const [datasetDLevel, setDatasetDLevel] = useState<number>(datasetDDataNoiseValue ?? 0);
  const [datasetDKeyEnabled, setDatasetDKeyEnabled] = useState<boolean>(!!datasetDDataKeyNoise);
  const [datasetDDataNoiseInsideState, setDatasetDDataNoiseInsideState] = useState<number>(
      datasetDDataNoiseInside ?? 0
  );

  const [datasetDSelections, setDatasetDSelections] = useState<string[]>(
      datasetDDataMultiselect ?? []
  );

  // Dataset A Handlers
  const handleDatasetAToggle = (checked: boolean) => {
    setDatasetAEnabled(checked);
    if (!checked) {
      setDatasetALevel(0);
      setDatasetAKeyEnabled(false);
      setDatasetADataNoiseInsideState(0);
      setDatasetASelections([]);

      updateForm({
        datasetADataNoise: checked,
        datasetADataNoiseValue: 0,
        datasetADataKeyNoise: false,
        datasetADataNoiseInside: 0,
        datasetADataMultiselect: [],
      });
    } else {
      updateForm({ datasetADataNoise: checked });
    }
  };

  const handleDatasetASlider = (value: number[]) => {
    const newLevel = value[0];
    setDatasetALevel(newLevel);
    updateForm({ datasetADataNoiseValue: newLevel });
  };

  const handleDatasetAKeyToggle = (checked: boolean) => {
    setDatasetAKeyEnabled(checked);
    updateForm({ datasetADataKeyNoise: checked });
  };

  const handleDatasetADataNoiseInsideSlider = (value: number[]) => {
    const newValue = value[0];
    setDatasetADataNoiseInsideState(newValue);
    updateForm({ datasetADataNoiseInside: newValue });
  };

  // handle multi-select changes for dataset A
  const handleDatasetAMultiselectChange = (values: string[]) => {
    setDatasetASelections(values);
    updateForm({ datasetADataMultiselect: values });
  };

  // Dataset B Handlers
  const handleDatasetBToggle = (checked: boolean) => {
    setDatasetBEnabled(checked);
    if (!checked) {
      setDatasetBLevel(0);
      setDatasetBKeyEnabled(false);
      setDatasetBDataNoiseInsideState(0);
      setDatasetBSelections([]);

      updateForm({
        datasetBDataNoise: checked,
        datasetBDataNoiseValue: 0,
        datasetBDataKeyNoise: false,
        datasetBDataNoiseInside: 0,
        datasetBDataMultiselect: [],
      });
    } else {
      updateForm({ datasetBDataNoise: checked });
    }
  };

  const handleDatasetBSlider = (value: number[]) => {
    const newLevel = value[0];
    setDatasetBLevel(newLevel);
    updateForm({ datasetBDataNoiseValue: newLevel });
  };

  const handleDatasetBKeyToggle = (checked: boolean) => {
    setDatasetBKeyEnabled(checked);
    updateForm({ datasetBDataKeyNoise: checked });
  };

  const handleDatasetBDataNoiseInsideSlider = (value: number[]) => {
    const newValue = value[0];
    setDatasetBDataNoiseInsideState(newValue);
    updateForm({ datasetBDataNoiseInside: newValue });
  };

  // handle multi-select changes for dataset B
  const handleDatasetBMultiselectChange = (values: string[]) => {
    setDatasetBSelections(values);
    updateForm({ datasetBDataMultiselect: values });
  };

  // Dataset C Handlers
  const handleDatasetCToggle = (checked: boolean) => {
    setDatasetCEnabled(checked);
    if (!checked) {
      setDatasetCLevel(0);
      setDatasetCKeyEnabled(false);
      setDatasetCDataNoiseInsideState(0);
      setDatasetCSelections([]);

      updateForm({
        datasetCDataNoise: checked,
        datasetCDataNoiseValue: 0,
        datasetCDataKeyNoise: false,
        datasetCDataNoiseInside: 0,
        datasetCDataMultiselect: [],
      });
    } else {
      updateForm({ datasetCDataNoise: checked });
    }
  };

  const handleDatasetCSlider = (value: number[]) => {
    const newLevel = value[0];
    setDatasetCLevel(newLevel);
    updateForm({ datasetCDataNoiseValue: newLevel });
  };

  const handleDatasetCKeyToggle = (checked: boolean) => {
    setDatasetCKeyEnabled(checked);
    updateForm({ datasetCDataKeyNoise: checked });
  };

  const handleDatasetCDataNoiseInsideSlider = (value: number[]) => {
    const newValue = value[0];
    setDatasetCDataNoiseInsideState(newValue);
    updateForm({ datasetCDataNoiseInside: newValue });
  };

  // handle multi-select changes for dataset C
  const handleDatasetCMultiselectChange = (values: string[]) => {
    setDatasetCSelections(values);
    updateForm({ datasetCDataMultiselect: values });
  };

  // Dataset D Handlers
  const handleDatasetDToggle = (checked: boolean) => {
    setDatasetDEnabled(checked);
    if (!checked) {
      setDatasetDLevel(0);
      setDatasetDKeyEnabled(false);
      setDatasetDDataNoiseInsideState(0);
      setDatasetDSelections([]);

      updateForm({
        datasetDDataNoise: checked,
        datasetDDataNoiseValue: 0,
        datasetDDataKeyNoise: false,
        datasetDDataNoiseInside: 0,
        datasetDDataMultiselect: [],
      });
    } else {
      updateForm({ datasetDDataNoise: checked });
    }
  };

  const handleDatasetDSlider = (value: number[]) => {
    const newLevel = value[0];
    setDatasetDLevel(newLevel);
    updateForm({ datasetDDataNoiseValue: newLevel });
  };

  const handleDatasetDKeyToggle = (checked: boolean) => {
    setDatasetDKeyEnabled(checked);
    updateForm({ datasetDDataKeyNoise: checked });
  };

  const handleDatasetDDataNoiseInsideSlider = (value: number[]) => {
    const newValue = value[0];
    setDatasetDDataNoiseInsideState(newValue);
    updateForm({ datasetDDataNoiseInside: newValue });
  };

  // handle multi-select changes for dataset D
  const handleDatasetDMultiselectChange = (values: string[]) => {
    setDatasetDSelections(values);
    updateForm({ datasetDDataMultiselect: values });
  };

  // Reset Datasets C and D if splitType != "VerticalHorizontal"
  useEffect(() => {
    if (splitType !== "VerticalHorizontal") {
      // Reset Dataset C
      setDatasetCEnabled(false);
      setDatasetCLevel(0);
      setDatasetCKeyEnabled(false);
      setDatasetCDataNoiseInsideState(0);
      setDatasetCSelections([]);

      // Reset Dataset D
      setDatasetDEnabled(false);
      setDatasetDLevel(0);
      setDatasetDKeyEnabled(false);
      setDatasetDDataNoiseInsideState(0);
      setDatasetDSelections([]);

      // Update form
      updateForm({
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
              {/* Dataset A */}
              <div className="flex flex-col w-full">
                <h3 className="text-lg text-white mb-2">Dataset A</h3>
                <div className="flex items-center gap-2 custom-label">
                  <Checkbox
                      checked={datasetAEnabled}
                      onCheckedChange={handleDatasetAToggle}
                      id="datasetANoise"
                  />
                  <label htmlFor="datasetANoise" className="text-white">
                    Enable Noise
                  </label>
                </div>
                {errors.datasetADataNoise && (
                    <p className="text-red-500 text-sm mt-1">
                      {errors.datasetADataNoise[0]}
                    </p>
                )}

                {datasetAEnabled && (
                    <>
                      {/* Break Key Constraints Checkbox */}
                      <div className="flex items-center gap-2 mt-2 custom-label">
                        <Checkbox
                            checked={datasetAKeyEnabled}
                            onCheckedChange={handleDatasetAKeyToggle}
                            id="datasetADataKeyNoise"
                        />
                        <label htmlFor="datasetADataKeyNoise" className="text-white">
                          Break Key Constraints
                        </label>
                      </div>
                      {errors.datasetADataKeyNoise && (
                          <p className="text-red-500 text-sm mt-1">
                            {errors.datasetADataKeyNoise[0]}
                          </p>
                      )}

                      {/* Multi-Select for Dataset A */}
                      <div className="mt-4">
                        <label htmlFor="datasetADataMultiSelect" className="text-white">
                          Dataset A Error Methods
                        </label>
                        <MultiSelect
                            id="datasetADataMultiSelect"
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

                      {/* Noise Slider */}
                      <div className="mt-4">
                        <label htmlFor="datasetADataNoiseSlider" className="text-white">
                          Percentage of noisy Columns/ Rows
                        </label>
                        <Slider
                            id="datasetADataNoiseSlider"
                            className="my-2 w-full"
                            value={[datasetALevel]}
                            onValueChange={handleDatasetASlider}
                            min={0}
                            max={100}
                            step={1}
                        />
                        <span className="text-white text-sm">
                    Percentage: {datasetALevel} %
                  </span>
                        {errors.datasetADataNoiseValue && (
                            <p className="text-red-500 text-sm mt-1">
                              {errors.datasetADataNoiseValue[0]}
                            </p>
                        )}
                      </div>

                      {/* Noise Inside Slider */}
                      <div className="mt-4">
                        <label
                            htmlFor="datasetADataNoiseInsideSlider"
                            className="text-white"
                        >
                          Percentage of noisy entries in Column/ Row
                        </label>
                        <Slider
                            id="datasetADataNoiseInsideSlider"
                            className="my-2 w-full"
                            value={[datasetADataNoiseInsideState]}
                            onValueChange={handleDatasetADataNoiseInsideSlider}
                            min={0}
                            max={100}
                            step={1}
                        />
                        <span className="text-white text-sm">
                    Percentage: {datasetADataNoiseInsideState} %
                  </span>
                        {errors.datasetADataNoiseInside && (
                            <p className="text-red-500 text-sm mt-1">
                              {errors.datasetADataNoiseInside[0]}
                            </p>
                        )}
                      </div>
                    </>
                )}
              </div>

              {/* Dataset B */}
              <div className="flex flex-col w-full">
                <h3 className="text-lg text-white mb-2">Dataset B</h3>
                <div className="flex items-center gap-2 custom-label">
                  <Checkbox
                      checked={datasetBEnabled}
                      onCheckedChange={handleDatasetBToggle}
                      id="datasetBNoise"
                  />
                  <label htmlFor="datasetBNoise" className="text-white">
                    Enable Noise
                  </label>
                </div>
                {errors.datasetBDataNoise && (
                    <p className="text-red-500 text-sm mt-1">
                      {errors.datasetBDataNoise[0]}
                    </p>
                )}

                {datasetBEnabled && (
                    <>
                      {/* Break Key Constraints Checkbox */}
                      <div className="flex items-center gap-2 mt-2 custom-label">
                        <Checkbox
                            checked={datasetBKeyEnabled}
                            onCheckedChange={handleDatasetBKeyToggle}
                            id="datasetBDataKeyNoise"
                        />
                        <label htmlFor="datasetBDataKeyNoise" className="text-white">
                          Break Key Constraints
                        </label>
                      </div>
                      {errors.datasetBDataKeyNoise && (
                          <p className="text-red-500 text-sm mt-1">
                            {errors.datasetBDataKeyNoise[0]}
                          </p>
                      )}

                      {/* Multi-Select for Dataset B */}
                      <div className="mt-4">
                        <label htmlFor="datasetBDataMultiSelect" className="text-white">
                          Dataset B Error Methods
                        </label>
                        <MultiSelect
                            id="datasetBDataMultiSelect"
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

                      {/* Noise Slider */}
                      <div className="mt-4">
                        <label htmlFor="datasetBDataNoiseSlider" className="text-white">
                          Percentage of noisy Columns/ Rows
                        </label>
                        <Slider
                            id="datasetBDataNoiseSlider"
                            className="my-2 w-full"
                            value={[datasetBLevel]}
                            onValueChange={handleDatasetBSlider}
                            min={0}
                            max={100}
                            step={1}
                        />
                        <span className="text-white text-sm">
                    Percentage: {datasetBLevel} %
                  </span>
                        {errors.datasetBDataNoiseValue && (
                            <p className="text-red-500 text-sm mt-1">
                              {errors.datasetBDataNoiseValue[0]}
                            </p>
                        )}
                      </div>

                      {/* Noise Inside Slider */}
                      <div className="mt-4">
                        <label
                            htmlFor="datasetBDataNoiseInsideSlider"
                            className="text-white"
                        >
                          Percentage of noisy entries in Column/ Row
                        </label>
                        <Slider
                            id="datasetBDataNoiseInsideSlider"
                            className="my-2 w-full"
                            value={[datasetBDataNoiseInsideState]}
                            onValueChange={handleDatasetBDataNoiseInsideSlider}
                            min={0}
                            max={100}
                            step={1}
                        />
                        <span className="text-white text-sm">
                    Percentage: {datasetBDataNoiseInsideState} %
                  </span>
                        {errors.datasetBDataNoiseInside && (
                            <p className="text-red-500 text-sm mt-1">
                              {errors.datasetBDataNoiseInside[0]}
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
                  {/* Dataset C */}
                  <div className="flex flex-col w-full">
                    <h3 className="text-lg text-white mb-2">Dataset C</h3>
                    <div className="flex items-center gap-2 custom-label">
                      <Checkbox
                          checked={datasetCEnabled}
                          onCheckedChange={handleDatasetCToggle}
                          id="datasetCNoise"
                      />
                      <label htmlFor="datasetCNoise" className="text-white">
                        Enable Noise
                      </label>
                    </div>
                    {errors.datasetCDataNoise && (
                        <p className="text-red-500 text-sm mt-1">
                          {errors.datasetCDataNoise[0]}
                        </p>
                    )}

                    {datasetCEnabled && (
                        <>
                          {/* Break Key Constraints Checkbox */}
                          <div className="flex items-center gap-2 mt-2 custom-label">
                            <Checkbox
                                checked={datasetCKeyEnabled}
                                onCheckedChange={handleDatasetCKeyToggle}
                                id="datasetCDataKeyNoise"
                            />
                            <label htmlFor="datasetCDataKeyNoise" className="text-white">
                              Break Key Constraints
                            </label>
                          </div>
                          {errors.datasetCDataKeyNoise && (
                              <p className="text-red-500 text-sm mt-1">
                                {errors.datasetCDataKeyNoise[0]}
                              </p>
                          )}

                          {/* Multi-Select for Dataset C */}
                          <div className="mt-4">
                            <label htmlFor="datasetCDataMultiSelect" className="text-white">
                              Dataset C Error Methods
                            </label>
                            <MultiSelect
                                id="datasetCDataMultiSelect"
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

                          {/* Noise Slider */}
                          <div className="mt-4">
                            <label htmlFor="datasetCDataNoiseSlider" className="text-white">
                              Percentage of noisy Columns/ Rows
                            </label>
                            <Slider
                                id="datasetCDataNoiseSlider"
                                className="my-2 w-full"
                                value={[datasetCLevel]}
                                onValueChange={handleDatasetCSlider}
                                min={0}
                                max={100}
                                step={1}
                            />
                            <span className="text-white text-sm">
                      Percentage: {datasetCLevel} %
                    </span>
                            {errors.datasetCDataNoiseValue && (
                                <p className="text-red-500 text-sm mt-1">
                                  {errors.datasetCDataNoiseValue[0]}
                                </p>
                            )}
                          </div>

                          {/* Noise Inside Slider */}
                          <div className="mt-4">
                            <label
                                htmlFor="datasetCDataNoiseInsideSlider"
                                className="text-white"
                            >
                              Percentage of noisy entries in Column/ Row
                            </label>
                            <Slider
                                id="datasetCDataNoiseInsideSlider"
                                className="my-2 w-full"
                                value={[datasetCDataNoiseInsideState]}
                                onValueChange={handleDatasetCDataNoiseInsideSlider}
                                min={0}
                                max={100}
                                step={1}
                            />
                            <span className="text-white text-sm">
                      Percentage: {datasetCDataNoiseInsideState} %
                    </span>
                            {errors.datasetCDataNoiseInside && (
                                <p className="text-red-500 text-sm mt-1">
                                  {errors.datasetCDataNoiseInside[0]}
                                </p>
                            )}
                          </div>
                        </>
                    )}
                  </div>

                  {/* Dataset D */}
                  <div className="flex flex-col w-full">
                    <h3 className="text-lg text-white mb-2">Dataset D</h3>
                    <div className="flex items-center gap-2 custom-label">
                      <Checkbox
                          checked={datasetDEnabled}
                          onCheckedChange={handleDatasetDToggle}
                          id="datasetDNoise"
                      />
                      <label htmlFor="datasetDNoise" className="text-white">
                        Enable Noise
                      </label>
                    </div>
                    {errors.datasetDDataNoise && (
                        <p className="text-red-500 text-sm mt-1">
                          {errors.datasetDDataNoise[0]}
                        </p>
                    )}

                    {datasetDEnabled && (
                        <>
                          {/* Break Key Constraints Checkbox */}
                          <div className="flex items-center gap-2 mt-2 custom-label">
                            <Checkbox
                                checked={datasetDKeyEnabled}
                                onCheckedChange={handleDatasetDKeyToggle}
                                id="datasetDDataKeyNoise"
                            />
                            <label htmlFor="datasetDDataKeyNoise" className="text-white">
                              Break Key Constraints
                            </label>
                          </div>
                          {errors.datasetDDataKeyNoise && (
                              <p className="text-red-500 text-sm mt-1">
                                {errors.datasetDDataKeyNoise[0]}
                              </p>
                          )}

                          {/* Multi-Select for Dataset D */}
                          <div className="mt-4">
                            <label htmlFor="datasetDDataMultiSelect" className="text-white">
                              Dataset D Error Methods
                            </label>
                            <MultiSelect
                                id="datasetDDataMultiSelect"
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

                          {/* Noise Slider */}
                          <div className="mt-4">
                            <label htmlFor="datasetDDataNoiseSlider" className="text-white">
                              Percentage of noisy Columns/ Rows
                            </label>
                            <Slider
                                id="datasetDDataNoiseSlider"
                                className="my-2 w-full"
                                value={[datasetDLevel]}
                                onValueChange={handleDatasetDSlider}
                                min={0}
                                max={100}
                                step={1}
                            />
                            <span className="text-white text-sm">
                      Percentage: {datasetDLevel} %
                    </span>
                            {errors.datasetDDataNoiseValue && (
                                <p className="text-red-500 text-sm mt-1">
                                  {errors.datasetDDataNoiseValue[0]}
                                </p>
                            )}
                          </div>

                          {/* Noise Inside Slider */}
                          <div className="mt-4">
                            <label
                                htmlFor="datasetDDataNoiseInsideSlider"
                                className="text-white"
                            >
                              Percentage of noisy entries in Column/ Row
                            </label>
                            <Slider
                                id="datasetDDataNoiseInsideSlider"
                                className="my-2 w-full"
                                value={[datasetDDataNoiseInsideState]}
                                onValueChange={handleDatasetDDataNoiseInsideSlider}
                                min={0}
                                max={100}
                                step={1}
                            />
                            <span className="text-white text-sm">
                      Percentage: {datasetDDataNoiseInsideState} %
                    </span>
                            {errors.datasetDDataNoiseInside && (
                                <p className="text-red-500 text-sm mt-1">
                                  {errors.datasetDDataNoiseInside[0]}
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
