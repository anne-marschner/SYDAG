// src/main/java/org/anne_marschner_project/api/FormDataWrapper.java
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
    // Dataset 1
    @NotBlank(message = "Dataset1 structure type cannot be blank")
    @Pattern(regexp = "BCNF|Join Columns|No Change", message = "Dataset1 structure type must be 'BCNF', 'Join Columns', or 'No Change'")
    private String dataset1StructureType;

    @Min(value = 0, message = "Dataset1 BCNF slider value must be at least 0")
    @Max(value = 100, message = "Dataset1 BCNF slider value cannot exceed 100")
    private Integer dataset1BCNFSliderValue;

    @Min(value = 0, message = "Dataset1 Join Columns slider value must be at least 0")
    @Max(value = 100, message = "Dataset1 Join Columns slider value cannot exceed 100")
    private Integer dataset1JoinColumnsSliderValue;

    // Dataset 2
    @NotBlank(message = "Dataset2 structure type cannot be blank")
    @Pattern(regexp = "BCNF|Join Columns|No Change", message = "Dataset2 structure type must be 'BCNF', 'Join Columns', or 'No Change'")
    private String dataset2StructureType;

    @Min(value = 0, message = "Dataset2 BCNF slider value must be at least 0")
    @Max(value = 100, message = "Dataset2 BCNF slider value cannot exceed 100")
    private Integer dataset2BCNFSliderValue;

    @Min(value = 0, message = "Dataset2 Join Columns slider value must be at least 0")
    @Max(value = 100, message = "Dataset2 Join Columns slider value cannot exceed 100")
    private Integer dataset2JoinColumnsSliderValue;

    // Dataset 3
    @NotBlank(message = "Dataset3 structure type cannot be blank")
    @Pattern(regexp = "BCNF|Join Columns|No Change", message = "Dataset3 structure type must be 'BCNF', 'Join Columns', or 'No Change'")
    private String dataset3StructureType;

    @Min(value = 0, message = "Dataset3 BCNF slider value must be at least 0")
    @Max(value = 100, message = "Dataset3 BCNF slider value cannot exceed 100")
    private Integer dataset3BCNFSliderValue;

    @Min(value = 0, message = "Dataset3 Join Columns slider value must be at least 0")
    @Max(value = 100, message = "Dataset3 Join Columns slider value cannot exceed 100")
    private Integer dataset3JoinColumnsSliderValue;

    // Dataset 4
    @NotBlank(message = "Dataset4 structure type cannot be blank")
    @Pattern(regexp = "BCNF|Join Columns|No Change", message = "Dataset4 structure type must be 'BCNF', 'Join Columns', or 'No Change'")
    private String dataset4StructureType;

    @Min(value = 0, message = "Dataset4 BCNF slider value must be at least 0")
    @Max(value = 100, message = "Dataset4 BCNF slider value cannot exceed 100")
    private Integer dataset4BCNFSliderValue;

    @Min(value = 0, message = "Dataset4 Join Columns slider value must be at least 0")
    @Max(value = 100, message = "Dataset4 Join Columns slider value cannot exceed 100")
    private Integer dataset4JoinColumnsSliderValue;

    // --------------- Step 5: Schema Noise ------------------
    // Dataset 1
    @NotNull(message = "Dataset1 schema noise flag cannot be null")
    private Boolean dataset1SchemaNoise;

    @NotNull(message = "Dataset1 schema noise value cannot be null")
    @Min(value = 0, message = "Dataset1 schema noise value must be at least 0")
    @Max(value = 100, message = "Dataset1 schema noise value cannot exceed 100")
    private Integer dataset1SchemaNoiseValue;

    @NotNull(message = "Dataset1 schema key noise flag cannot be null")
    private Boolean dataset1SchemaKeyNoise;

    @NotNull(message = "Dataset1 delete schema flag cannot be null")
    private Boolean dataset1SchemaDeleteSchema;

    // Dataset 2
    @NotNull(message = "Dataset2 schema noise flag cannot be null")
    private Boolean dataset2SchemaNoise;

    @NotNull(message = "Dataset2 schema noise value cannot be null")
    @Min(value = 0, message = "Dataset2 schema noise value must be at least 0")
    @Max(value = 100, message = "Dataset2 schema noise value cannot exceed 100")
    private Integer dataset2SchemaNoiseValue;

    @NotNull(message = "Dataset2 schema key noise flag cannot be null")
    private Boolean dataset2SchemaKeyNoise;

    @NotNull(message = "Dataset2 delete schema flag cannot be null")
    private Boolean dataset2SchemaDeleteSchema;

    // Dataset 3
    @NotNull(message = "Dataset3 schema noise flag cannot be null")
    private Boolean dataset3SchemaNoise;

    @NotNull(message = "Dataset3 schema noise value cannot be null")
    @Min(value = 0, message = "Dataset3 schema noise value must be at least 0")
    @Max(value = 100, message = "Dataset3 schema noise value cannot exceed 100")
    private Integer dataset3SchemaNoiseValue;

    @NotNull(message = "Dataset3 schema key noise flag cannot be null")
    private Boolean dataset3SchemaKeyNoise;

    @NotNull(message = "Dataset3 delete schema flag cannot be null")
    private Boolean dataset3SchemaDeleteSchema;

    // Dataset 4
    @NotNull(message = "Dataset4 schema noise flag cannot be null")
    private Boolean dataset4SchemaNoise;

    @NotNull(message = "Dataset4 schema noise value cannot be null")
    @Min(value = 0, message = "Dataset4 schema noise value must be at least 0")
    @Max(value = 100, message = "Dataset4 schema noise value cannot exceed 100")
    private Integer dataset4SchemaNoiseValue;

    @NotNull(message = "Dataset4 schema key noise flag cannot be null")
    private Boolean dataset4SchemaKeyNoise;

    @NotNull(message = "Dataset4 delete schema flag cannot be null")
    private Boolean dataset4SchemaDeleteSchema;

    // --------------- Step 6: Data Noise ------------------
    // Dataset 1
    @NotNull(message = "Dataset1 data noise flag cannot be null")
    private Boolean dataset1DataNoise;

    @NotNull(message = "Dataset1 data noise value cannot be null")
    @Min(value = 0, message = "Dataset1 data noise value must be at least 0")
    @Max(value = 100, message = "Dataset1 data noise value cannot exceed 100")
    private Integer dataset1DataNoiseValue;

    @NotNull(message = "Dataset1 data key noise flag cannot be null")
    private Boolean dataset1DataKeyNoise;

    @NotNull(message = "Dataset1 data noise inside value cannot be null")
    @Min(value = 0, message = "Dataset1 data noise inside value must be at least 0")
    @Max(value = 100, message = "Dataset1 data noise inside value cannot exceed 100")
    private Integer dataset1DataNoiseInside;

    // Dataset 2
    @NotNull(message = "Dataset2 data noise flag cannot be null")
    private Boolean dataset2DataNoise;

    @NotNull(message = "Dataset2 data noise value cannot be null")
    @Min(value = 0, message = "Dataset2 data noise value must be at least 0")
    @Max(value = 100, message = "Dataset2 data noise value cannot exceed 100")
    private Integer dataset2DataNoiseValue;

    @NotNull(message = "Dataset2 data key noise flag cannot be null")
    private Boolean dataset2DataKeyNoise;

    @NotNull(message = "Dataset2 data noise inside value cannot be null")
    @Min(value = 0, message = "Dataset2 data noise inside value must be at least 0")
    @Max(value = 100, message = "Dataset2 data noise inside value cannot exceed 100")
    private Integer dataset2DataNoiseInside;

    // Dataset 3
    @NotNull(message = "Dataset3 data noise flag cannot be null")
    private Boolean dataset3DataNoise;

    @NotNull(message = "Dataset3 data noise value cannot be null")
    @Min(value = 0, message = "Dataset3 data noise value must be at least 0")
    @Max(value = 100, message = "Dataset3 data noise value cannot exceed 100")
    private Integer dataset3DataNoiseValue;

    @NotNull(message = "Dataset3 data key noise flag cannot be null")
    private Boolean dataset3DataKeyNoise;

    @NotNull(message = "Dataset3 data noise inside value cannot be null")
    @Min(value = 0, message = "Dataset3 data noise inside value must be at least 0")
    @Max(value = 100, message = "Dataset3 data noise inside value cannot exceed 100")
    private Integer dataset3DataNoiseInside;

    // Dataset 4
    @NotNull(message = "Dataset4 data noise flag cannot be null")
    private Boolean dataset4DataNoise;

    @NotNull(message = "Dataset4 data noise value cannot be null")
    @Min(value = 0, message = "Dataset4 data noise value must be at least 0")
    @Max(value = 100, message = "Dataset4 data noise value cannot exceed 100")
    private Integer dataset4DataNoiseValue;

    @NotNull(message = "Dataset4 data key noise flag cannot be null")
    private Boolean dataset4DataKeyNoise;

    @NotNull(message = "Dataset4 data noise inside value cannot be null")
    @Min(value = 0, message = "Dataset4 data noise inside value must be at least 0")
    @Max(value = 100, message = "Dataset4 data noise inside value cannot exceed 100")
    private Integer dataset4DataNoiseInside;

    // --------------- Step 7: Shuffle Options ------------------
    @NotBlank(message = "Dataset1 shuffle option cannot be blank")
    @Pattern(regexp = "Shuffle Columns|Shuffle Rows|No Change", message = "Dataset1 shuffle option must be 'Shuffle Columns', 'Shuffle Rows', or 'No Change'")
    private String dataset1ShuffleOption;

    @NotBlank(message = "Dataset2 shuffle option cannot be blank")
    @Pattern(regexp = "Shuffle Columns|Shuffle Rows|No Change", message = "Dataset2 shuffle option must be 'Shuffle Columns', 'Shuffle Rows', or 'No Change'")
    private String dataset2ShuffleOption;

    @NotBlank(message = "Dataset3 shuffle option cannot be blank")
    @Pattern(regexp = "Shuffle Columns|Shuffle Rows|No Change", message = "Dataset3 shuffle option must be 'Shuffle Columns', 'Shuffle Rows', or 'No Change'")
    private String dataset3ShuffleOption;

    @NotBlank(message = "Dataset4 shuffle option cannot be blank")
    @Pattern(regexp = "Shuffle Columns|Shuffle Rows|No Change", message = "Dataset4 shuffle option must be 'Shuffle Columns', 'Shuffle Rows', or 'No Change'")
    private String dataset4ShuffleOption;

    // --------------- Selected Methods ------------------
    // Schema Methods
    @NotNull(message = "Selected schema methods for dataset1 cannot be null")
    private List<String> dataset1SchemaMultiselect;

    @NotNull(message = "Selected schema methods for dataset2 cannot be null")
    private List<String> dataset2SchemaMultiselect;

    @NotNull(message = "Selected schema methods for dataset3 cannot be null")
    private List<String> dataset3SchemaMultiselect;

    @NotNull(message = "Selected schema methods for dataset4 cannot be null")
    private List<String> dataset4SchemaMultiselect;

    // String Methods
    @NotNull(message = "Selected string methods for dataset1 cannot be null")
    private List<String> dataset1DataMultiselect;

    @NotNull(message = "Selected string methods for dataset2 cannot be null")
    private List<String> dataset2DataMultiselect;

    @NotNull(message = "Selected string methods for dataset3 cannot be null")
    private List<String> dataset3DataMultiselect;

    @NotNull(message = "Selected string methods for dataset4 cannot be null")
    private List<String> dataset4DataMultiselect;

    // --------------- Output Path ------------------
    @NotBlank(message = "File path output cannot be blank")
    private String filepathOutput;

    // --------------- Conditional Validations ------------------

    // Schema Noise Validations
    @AssertTrue(message = "Dataset1 schema noise value must be provided when dataset1SchemaNoise is true")
    public boolean isDataset1SchemaNoiseValueValid() {
        return !Boolean.TRUE.equals(dataset1SchemaNoise) || (dataset1SchemaNoiseValue != null && dataset1SchemaNoiseValue >= 0 && dataset1SchemaNoiseValue <= 100);
    }

    @AssertTrue(message = "Dataset2 schema noise value must be provided when dataset2SchemaNoise is true")
    public boolean isDataset2SchemaNoiseValueValid() {
        return !Boolean.TRUE.equals(dataset2SchemaNoise) || (dataset2SchemaNoiseValue != null && dataset2SchemaNoiseValue >= 0 && dataset2SchemaNoiseValue <= 100);
    }

    @AssertTrue(message = "Dataset3 schema noise value must be provided when dataset3SchemaNoise is true")
    public boolean isDataset3SchemaNoiseValueValid() {
        return !Boolean.TRUE.equals(dataset3SchemaNoise) || (dataset3SchemaNoiseValue != null && dataset3SchemaNoiseValue >= 0 && dataset3SchemaNoiseValue <= 100);
    }

    @AssertTrue(message = "Dataset4 schema noise value must be provided when dataset4SchemaNoise is true")
    public boolean isDataset4SchemaNoiseValueValid() {
        return !Boolean.TRUE.equals(dataset4SchemaNoise) || (dataset4SchemaNoiseValue != null && dataset4SchemaNoiseValue >= 0 && dataset4SchemaNoiseValue <= 100);
    }

    // Data Noise Validations
    @AssertTrue(message = "Dataset1 data noise value must be provided when dataset1DataNoise is true")
    public boolean isDataset1DataNoiseValueValid() {
        return !Boolean.TRUE.equals(dataset1DataNoise) || (dataset1DataNoiseValue != null && dataset1DataNoiseValue >= 0 && dataset1DataNoiseValue <= 100);
    }

    @AssertTrue(message = "Dataset2 data noise value must be provided when dataset2DataNoise is true")
    public boolean isDataset2DataNoiseValueValid() {
        return !Boolean.TRUE.equals(dataset2DataNoise) || (dataset2DataNoiseValue != null && dataset2DataNoiseValue >= 0 && dataset2DataNoiseValue <= 100);
    }

    @AssertTrue(message = "Dataset3 data noise value must be provided when dataset3DataNoise is true")
    public boolean isDataset3DataNoiseValueValid() {
        return !Boolean.TRUE.equals(dataset3DataNoise) || (dataset3DataNoiseValue != null && dataset3DataNoiseValue >= 0 && dataset3DataNoiseValue <= 100);
    }

    @AssertTrue(message = "Dataset4 data noise value must be provided when dataset4DataNoise is true")
    public boolean isDataset4DataNoiseValueValid() {
        return !Boolean.TRUE.equals(dataset4DataNoise) || (dataset4DataNoiseValue != null && dataset4DataNoiseValue >= 0 && dataset4DataNoiseValue <= 100);
    }

    // Add additional conditional validations as needed
}
