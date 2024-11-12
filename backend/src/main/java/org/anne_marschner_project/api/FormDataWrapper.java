package org.anne_marschner_project.api;

import lombok.Data;

import javax.validation.constraints.*;
import java.util.List;

/**
 * Data Transfer Object for form data.
 */
@Data
public class FormDataWrapper {

    @NotNull(message = "hasHeaders cannot be null")
    private Boolean hasHeaders;

    @NotBlank(message = "Separator cannot be blank")
    @Pattern(regexp = "[,;]", message = "Separator must be a comma or semicolon")
    private String separator;

    @NotNull(message = "manualInput cannot be null")
    private Boolean manualInput;

    @NotBlank(message = "Mode cannot be blank")
    private String mode;

    @NotBlank(message = "Split type cannot be blank")
    private String splitType;

    @NotNull(message = "Row overlap percentage cannot be null")
    @Min(value = 0, message = "Row overlap percentage must be at least 0")
    @Max(value = 100, message = "Row overlap percentage cannot exceed 100")
    private Integer rowOverlapPercentage;

    @NotNull(message = "Column overlap percentage cannot be null")
    @Min(value = 0, message = "Column overlap percentage must be at least 0")
    @Max(value = 100, message = "Column overlap percentage cannot exceed 100")
    private Integer columnOverlapPercentage;

    @NotBlank(message = "Dataset1 structure type cannot be blank")
    private String dataset1StructureType;

    @NotBlank(message = "Dataset2 structure type cannot be blank")
    private String dataset2StructureType;

    @NotNull(message = "Dataset1 schema noise flag cannot be null")
    private Boolean dataset1SchemaNoise;

    @Min(value = 0, message = "Dataset1 schema noise value must be at least 0")
    private Integer dataset1SchemaNoiseValue;

    @NotNull(message = "Dataset1 schema key noise flag cannot be null")
    private Boolean dataset1SchemaKeyNoise;

    @NotNull(message = "Dataset2 schema noise flag cannot be null")
    private Boolean dataset2SchemaNoise;

    @Min(value = 0, message = "Dataset2 schema noise value must be at least 0")
    private Integer dataset2SchemaNoiseValue;

    @NotNull(message = "Dataset2 schema key noise flag cannot be null")
    private Boolean dataset2SchemaKeyNoise;

    @NotNull(message = "Dataset1 data noise flag cannot be null")
    private Boolean dataset1DataNoise;

    @Min(value = 0, message = "Dataset1 data noise value must be at least 0")
    private Integer dataset1DataNoiseValue;

    @NotNull(message = "Dataset1 data key noise flag cannot be null")
    private Boolean dataset1DataKeyNoise;

    @NotNull(message = "Dataset2 data noise flag cannot be null")
    private Boolean dataset2DataNoise;

    @Min(value = 0, message = "Dataset2 data noise value must be at least 0")
    private Integer dataset2DataNoiseValue;

    @NotNull(message = "Dataset2 data key noise flag cannot be null")
    private Boolean dataset2DataKeyNoise;

    @NotBlank(message = "Dataset1 shuffle option cannot be blank")
    private String dataset1ShuffleOption;

    @NotBlank(message = "Dataset2 shuffle option cannot be blank")
    private String dataset2ShuffleOption;

    @NotNull(message = "Selected schema methods for dataset1 cannot be null")
    private List<String> selectedSchemaMethods1;

    @NotNull(message = "Selected schema methods for dataset2 cannot be null")
    private List<String> selectedSchemaMethods2;

    @NotNull(message = "Selected string methods for dataset1 cannot be null")
    private List<String> selectedStringMethods1;

    @NotNull(message = "Selected string methods for dataset2 cannot be null")
    private List<String> selectedStringMethods2;

    @NotNull(message = "Selected numeric methods for dataset1 cannot be null")
    private List<String> selectedNumericMethods1;

    @NotNull(message = "Selected numeric methods for dataset2 cannot be null")
    private List<String> selectedNumericMethods2;

    @NotBlank(message = "File path output cannot be blank")
    private String filepathOutput;

    // Conditional validations

    @AssertTrue(message = "Dataset1 schema noise value must be provided when dataset1SchemaNoise is true")
    public boolean isDataset1SchemaNoiseValueValid() {
        return !dataset1SchemaNoise || (dataset1SchemaNoiseValue != null && dataset1SchemaNoiseValue >= 0);
    }

    @AssertTrue(message = "Dataset2 schema noise value must be provided when dataset2SchemaNoise is true")
    public boolean isDataset2SchemaNoiseValueValid() {
        return !dataset2SchemaNoise || (dataset2SchemaNoiseValue != null && dataset2SchemaNoiseValue >= 0);
    }

    // Add similar methods for other conditional validations as needed
}
