package org.anne_marschner_project.api;

import lombok.Data;
import javax.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

/**
 * Data Transfer Object for form data.
 */
@Data
public class FormDataWrapper {

    // --------------- Step 1: CSV ------------------
    @NotNull(message = "CSV file cannot be null")
    private MultipartFile csvFile;

    @NotNull(message = "hasHeaders cannot be null")
    private Boolean hasHeaders;

    @NotBlank(message = "Separator cannot be blank")
    @Pattern(regexp = "[,;]", message = "Separator must be a comma or semicolon")
    private String separator;

    @NotBlank(message = "Quote character cannot be blank")
    @Pattern(regexp = "[\"']", message = "Quote character must be a single or double quote")
    private String quote;

    @NotBlank(message = "Escape character cannot be blank")
    @Pattern(regexp = "[\\\\/]", message = "Escape character must be a backslash or forward slash")
    private String escape;

    // --------------- Step 2: JSON ------------------
    @NotNull(message = "JSON file cannot be null")
    private MultipartFile jsonFile;

    @NotNull(message = "manualInput cannot be null")
    private Boolean manualInput;

    @NotBlank(message = "Mode cannot be blank")
    @Pattern(regexp = "UploadJson|Manual", message = "Mode must be either 'UploadJson' or 'Manual'")
    private String mode;

    // --------------- Step 3: Split ------------------
    @NotBlank(message = "Split type cannot be blank")
    @Pattern(regexp = "Horizontal|Vertical|VerticalHorizontal", message = "Split type must be 'Horizontal', 'Vertical', or 'VerticalHorizontal'")
    private String splitType;

    @NotNull(message = "Row overlap percentage cannot be null")
    @Min(value = 0, message = "Row overlap percentage must be at least 0")
    @Max(value = 100, message = "Row overlap percentage cannot exceed 100")
    private Integer rowOverlapPercentage;

    @NotNull(message = "Column overlap percentage cannot be null")
    @Min(value = 0, message = "Column overlap percentage must be at least 0")
    @Max(value = 100, message = "Column overlap percentage cannot exceed 100")
    private Integer columnOverlapPercentage;

    @NotNull(message = "Row distribution cannot be null")
    @Min(value = 0, message = "Row distribution must be at least 0")
    @Max(value = 100, message = "Row distribution cannot exceed 100")
    private Integer rowDistribution;

    @NotNull(message = "Column distribution cannot be null")
    @Min(value = 0, message = "Column distribution must be at least 0")
    @Max(value = 100, message = "Column distribution cannot exceed 100")
    private Integer columnDistribution;

    @NotBlank(message = "Overlap type cannot be blank")
    @Pattern(regexp = "Mixed Overlap|Block Overlap", message = "Overlap type must be 'Mixed Overlap' or 'Block Overlap'")
    private String overlapType;

    // --------------- Step 4: Structure ------------------
    // Dataset A
    @NotBlank(message = "datasetA structure type cannot be blank")
    @Pattern(regexp = "BCNF|Merge Columns|No Change", message = "datasetA structure type must be 'BCNF', 'Merge Columns', or 'No Change'")
    private String datasetAStructureType;

    @Min(value = 0, message = "datasetA BCNF slider value must be at least 0")
    @Max(value = 100, message = "datasetA BCNF slider value cannot exceed 100")
    private Integer datasetABCNFSliderValue;

    @Min(value = 0, message = "datasetA Merge Columns slider value must be at least 0")
    @Max(value = 100, message = "datasetA Merge Columns slider value cannot exceed 100")
    private Integer datasetAMergeColumnsSliderValue;

    // Dataset B
    @NotBlank(message = "datasetB structure type cannot be blank")
    @Pattern(regexp = "BCNF|Merge Columns|No Change", message = "datasetB structure type must be 'BCNF', 'Merge Columns', or 'No Change'")
    private String datasetBStructureType;

    @Min(value = 0, message = "datasetB BCNF slider value must be at least 0")
    @Max(value = 100, message = "datasetB BCNF slider value cannot exceed 100")
    private Integer datasetBBCNFSliderValue;

    @Min(value = 0, message = "datasetB Merge Columns slider value must be at least 0")
    @Max(value = 100, message = "datasetB Merge Columns slider value cannot exceed 100")
    private Integer datasetBMergeColumnsSliderValue;

    // Dataset C
    @NotBlank(message = "datasetC structure type cannot be blank")
    @Pattern(regexp = "BCNF|Merge Columns|No Change", message = "datasetC structure type must be 'BCNF', 'Merge Columns', or 'No Change'")
    private String datasetCStructureType;

    @Min(value = 0, message = "datasetC BCNF slider value must be at least 0")
    @Max(value = 100, message = "datasetC BCNF slider value cannot exceed 100")
    private Integer datasetCBCNFSliderValue;

    @Min(value = 0, message = "datasetC Merge Columns slider value must be at least 0")
    @Max(value = 100, message = "datasetC Merge Columns slider value cannot exceed 100")
    private Integer datasetCMergeColumnsSliderValue;

    // Dataset D
    @NotBlank(message = "datasetD structure type cannot be blank")
    @Pattern(regexp = "BCNF|Merge Columns|No Change", message = "datasetD structure type must be 'BCNF', 'Merge Columns', or 'No Change'")
    private String datasetDStructureType;

    @Min(value = 0, message = "datasetD BCNF slider value must be at least 0")
    @Max(value = 100, message = "datasetD BCNF slider value cannot exceed 100")
    private Integer datasetDBCNFSliderValue;

    @Min(value = 0, message = "datasetD Merge Columns slider value must be at least 0")
    @Max(value = 100, message = "datasetD Merge Columns slider value cannot exceed 100")
    private Integer datasetDMergeColumnsSliderValue;

    // --------------- Step 5: Schema Noise ------------------
    // Dataset A
    @NotNull(message = "datasetA schema noise flag cannot be null")
    private Boolean datasetASchemaNoise;

    @NotNull(message = "datasetA schema noise value cannot be null")
    @Min(value = 0, message = "datasetA schema noise value must be at least 0")
    @Max(value = 100, message = "datasetA schema noise value cannot exceed 100")
    private Integer datasetASchemaNoiseValue;

    @NotNull(message = "datasetA schema key noise flag cannot be null")
    private Boolean datasetASchemaKeyNoise;

    @NotNull(message = "datasetA delete schema flag cannot be null")
    private Boolean datasetASchemaDeleteSchema;

    // Dataset B
    @NotNull(message = "datasetB schema noise flag cannot be null")
    private Boolean datasetBSchemaNoise;

    @NotNull(message = "datasetB schema noise value cannot be null")
    @Min(value = 0, message = "datasetB schema noise value must be at least 0")
    @Max(value = 100, message = "datasetB schema noise value cannot exceed 100")
    private Integer datasetBSchemaNoiseValue;

    @NotNull(message = "datasetB schema key noise flag cannot be null")
    private Boolean datasetBSchemaKeyNoise;

    @NotNull(message = "datasetB delete schema flag cannot be null")
    private Boolean datasetBSchemaDeleteSchema;

    // Dataset C
    @NotNull(message = "datasetC schema noise flag cannot be null")
    private Boolean datasetCSchemaNoise;

    @NotNull(message = "datasetC schema noise value cannot be null")
    @Min(value = 0, message = "datasetC schema noise value must be at least 0")
    @Max(value = 100, message = "datasetC schema noise value cannot exceed 100")
    private Integer datasetCSchemaNoiseValue;

    @NotNull(message = "datasetC schema key noise flag cannot be null")
    private Boolean datasetCSchemaKeyNoise;

    @NotNull(message = "datasetC delete schema flag cannot be null")
    private Boolean datasetCSchemaDeleteSchema;

    // Dataset D
    @NotNull(message = "datasetD schema noise flag cannot be null")
    private Boolean datasetDSchemaNoise;

    @NotNull(message = "datasetD schema noise value cannot be null")
    @Min(value = 0, message = "datasetD schema noise value must be at least 0")
    @Max(value = 100, message = "datasetD schema noise value cannot exceed 100")
    private Integer datasetDSchemaNoiseValue;

    @NotNull(message = "datasetD schema key noise flag cannot be null")
    private Boolean datasetDSchemaKeyNoise;

    @NotNull(message = "datasetD delete schema flag cannot be null")
    private Boolean datasetDSchemaDeleteSchema;

    // --------------- Step 6: Data Noise ------------------
    // Dataset A
    @NotNull(message = "datasetA data noise flag cannot be null")
    private Boolean datasetADataNoise;

    @NotNull(message = "datasetA data noise value cannot be null")
    @Min(value = 0, message = "datasetA data noise value must be at least 0")
    @Max(value = 100, message = "datasetA data noise value cannot exceed 100")
    private Integer datasetADataNoiseValue;

    @NotNull(message = "datasetA data key noise flag cannot be null")
    private Boolean datasetADataKeyNoise;

    @NotNull(message = "datasetA data noise inside value cannot be null")
    @Min(value = 0, message = "datasetA data noise inside value must be at least 0")
    @Max(value = 100, message = "datasetA data noise inside value cannot exceed 100")
    private Integer datasetADataNoiseInside;

    // Dataset B
    @NotNull(message = "datasetB data noise flag cannot be null")
    private Boolean datasetBDataNoise;

    @NotNull(message = "datasetB data noise value cannot be null")
    @Min(value = 0, message = "datasetB data noise value must be at least 0")
    @Max(value = 100, message = "datasetB data noise value cannot exceed 100")
    private Integer datasetBDataNoiseValue;

    @NotNull(message = "datasetB data key noise flag cannot be null")
    private Boolean datasetBDataKeyNoise;

    @NotNull(message = "datasetB data noise inside value cannot be null")
    @Min(value = 0, message = "datasetB data noise inside value must be at least 0")
    @Max(value = 100, message = "datasetB data noise inside value cannot exceed 100")
    private Integer datasetBDataNoiseInside;

    // Dataset C
    @NotNull(message = "datasetC data noise flag cannot be null")
    private Boolean datasetCDataNoise;

    @NotNull(message = "datasetC data noise value cannot be null")
    @Min(value = 0, message = "datasetC data noise value must be at least 0")
    @Max(value = 100, message = "datasetC data noise value cannot exceed 100")
    private Integer datasetCDataNoiseValue;

    @NotNull(message = "datasetC data key noise flag cannot be null")
    private Boolean datasetCDataKeyNoise;

    @NotNull(message = "datasetC data noise inside value cannot be null")
    @Min(value = 0, message = "datasetC data noise inside value must be at least 0")
    @Max(value = 100, message = "datasetC data noise inside value cannot exceed 100")
    private Integer datasetCDataNoiseInside;

    // Dataset D
    @NotNull(message = "datasetD data noise flag cannot be null")
    private Boolean datasetDDataNoise;

    @NotNull(message = "datasetD data noise value cannot be null")
    @Min(value = 0, message = "datasetD data noise value must be at least 0")
    @Max(value = 100, message = "datasetD data noise value cannot exceed 100")
    private Integer datasetDDataNoiseValue;

    @NotNull(message = "datasetD data key noise flag cannot be null")
    private Boolean datasetDDataKeyNoise;

    @NotNull(message = "datasetD data noise inside value cannot be null")
    @Min(value = 0, message = "datasetD data noise inside value must be at least 0")
    @Max(value = 100, message = "datasetD data noise inside value cannot exceed 100")
    private Integer datasetDDataNoiseInside;

    // --------------- Step 7: Shuffle Options ------------------
    @NotBlank(message = "datasetA shuffle option cannot be blank")
    @Pattern(regexp = "Shuffle Columns|Shuffle Rows|No Change", message = "datasetA shuffle option must be 'Shuffle Columns', 'Shuffle Rows', or 'No Change'")
    private String datasetAShuffleOption;

    @NotBlank(message = "datasetB shuffle option cannot be blank")
    @Pattern(regexp = "Shuffle Columns|Shuffle Rows|No Change", message = "datasetB shuffle option must be 'Shuffle Columns', 'Shuffle Rows', or 'No Change'")
    private String datasetBShuffleOption;

    @NotBlank(message = "datasetC shuffle option cannot be blank")
    @Pattern(regexp = "Shuffle Columns|Shuffle Rows|No Change", message = "datasetC shuffle option must be 'Shuffle Columns', 'Shuffle Rows', or 'No Change'")
    private String datasetCShuffleOption;

    @NotBlank(message = "datasetD shuffle option cannot be blank")
    @Pattern(regexp = "Shuffle Columns|Shuffle Rows|No Change", message = "datasetD shuffle option must be 'Shuffle Columns', 'Shuffle Rows', or 'No Change'")
    private String datasetDShuffleOption;

    // --------------- Selected Methods ------------------
    // Schema Methods
    @NotNull(message = "Selected schema methods for datasetA cannot be null")
    private List<String> datasetASchemaMultiselect;

    @NotNull(message = "Selected schema methods for datasetB cannot be null")
    private List<String> datasetBSchemaMultiselect;

    @NotNull(message = "Selected schema methods for datasetC cannot be null")
    private List<String> datasetCSchemaMultiselect;

    @NotNull(message = "Selected schema methods for datasetD cannot be null")
    private List<String> datasetDSchemaMultiselect;

    // String Methods
    @NotNull(message = "Selected string methods for datasetA cannot be null")
    private List<String> datasetADataMultiselect;

    @NotNull(message = "Selected string methods for datasetB cannot be null")
    private List<String> datasetBDataMultiselect;

    @NotNull(message = "Selected string methods for datasetC cannot be null")
    private List<String> datasetCDataMultiselect;

    @NotNull(message = "Selected string methods for datasetD cannot be null")
    private List<String> datasetDDataMultiselect;

    // --------------- Output Path ------------------
    @NotBlank(message = "File path output cannot be blank")
    private String filepathOutput;

    // --------------- Conditional Validations ------------------

    // Schema Noise Validations
    @AssertTrue(message = "datasetA schema noise value must be provided when datasetASchemaNoise is true")
    public boolean isdatasetASchemaNoiseValueValid() {
        return !Boolean.TRUE.equals(datasetASchemaNoise) || (datasetASchemaNoiseValue != null && datasetASchemaNoiseValue >= 0 && datasetASchemaNoiseValue <= 100);
    }

    @AssertTrue(message = "datasetB schema noise value must be provided when datasetBSchemaNoise is true")
    public boolean isdatasetBSchemaNoiseValueValid() {
        return !Boolean.TRUE.equals(datasetBSchemaNoise) || (datasetBSchemaNoiseValue != null && datasetBSchemaNoiseValue >= 0 && datasetBSchemaNoiseValue <= 100);
    }

    @AssertTrue(message = "datasetC schema noise value must be provided when datasetCSchemaNoise is true")
    public boolean isdatasetCSchemaNoiseValueValid() {
        return !Boolean.TRUE.equals(datasetCSchemaNoise) || (datasetCSchemaNoiseValue != null && datasetCSchemaNoiseValue >= 0 && datasetCSchemaNoiseValue <= 100);
    }

    @AssertTrue(message = "datasetD schema noise value must be provided when datasetDSchemaNoise is true")
    public boolean isdatasetDSchemaNoiseValueValid() {
        return !Boolean.TRUE.equals(datasetDSchemaNoise) || (datasetDSchemaNoiseValue != null && datasetDSchemaNoiseValue >= 0 && datasetDSchemaNoiseValue <= 100);
    }

    // Data Noise Validations
    @AssertTrue(message = "datasetA data noise value must be provided when datasetADataNoise is true")
    public boolean isdatasetADataNoiseValueValid() {
        return !Boolean.TRUE.equals(datasetADataNoise) || (datasetADataNoiseValue != null && datasetADataNoiseValue >= 0 && datasetADataNoiseValue <= 100);
    }

    @AssertTrue(message = "datasetB data noise value must be provided when datasetBDataNoise is true")
    public boolean isdatasetBDataNoiseValueValid() {
        return !Boolean.TRUE.equals(datasetBDataNoise) || (datasetBDataNoiseValue != null && datasetBDataNoiseValue >= 0 && datasetBDataNoiseValue <= 100);
    }

    @AssertTrue(message = "datasetC data noise value must be provided when datasetCDataNoise is true")
    public boolean isdatasetCDataNoiseValueValid() {
        return !Boolean.TRUE.equals(datasetCDataNoise) || (datasetCDataNoiseValue != null && datasetCDataNoiseValue >= 0 && datasetCDataNoiseValue <= 100);
    }

    @AssertTrue(message = "datasetD data noise value must be provided when datasetDDataNoise is true")
    public boolean isdatasetDDataNoiseValueValid() {
        return !Boolean.TRUE.equals(datasetDDataNoise) || (datasetDDataNoiseValue != null && datasetDDataNoiseValue >= 0 && datasetDDataNoiseValue <= 100);
    }
}
