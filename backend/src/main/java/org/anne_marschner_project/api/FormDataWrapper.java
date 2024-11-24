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

    // new
    private char quoteChar;

    // new
    private char escapeChar;

    // new
    private Boolean ignoreLeadingWhitespace;

    @NotNull(message = "manualInput cannot be null")
    private Boolean manualInput;

    @NotBlank(message = "Mode cannot be blank")
    private String mode;

    // chaged to array
    @NotBlank(message = "Split type cannot be blank")
    private String[] splitType;

    @NotNull(message = "Row overlap percentage cannot be null")
    @Min(value = 0, message = "Row overlap percentage must be at least 0")
    @Max(value = 100, message = "Row overlap percentage cannot exceed 100")
    private Integer rowOverlapPercentage;

    @NotNull(message = "Column overlap percentage cannot be null")
    @Min(value = 0, message = "Column overlap percentage must be at least 0")
    @Max(value = 100, message = "Column overlap percentage cannot exceed 100")
    private Integer columnOverlapPercentage;

    // new
    private Integer columnDistribution;
    private Integer rowDistribution;
    private Boolean mixedOverlap;

    @NotBlank(message = "Dataset1 structure type cannot be blank")
    private String dataset1StructureType;

    @NotBlank(message = "Dataset2 structure type cannot be blank")
    private String dataset2StructureType;

    // new
    @NotBlank(message = "Dataset3 structure type cannot be blank")
    private String dataset3StructureType;

    // new
    @NotBlank(message = "Dataset4 structure type cannot be blank")
    private String dataset4StructureType;

    // new
    private Integer joinPercentage1;
    private Integer joinPercentage2;
    private Integer joinPercentage3;
    private Integer joinPercentage4;
    private Integer normalizePercentage1;
    private Integer normalizePercentage2;
    private Integer normalizePercentage3;
    private Integer normalizePercentage4;


    // SCHEMA NOISE

    @NotNull(message = "Dataset1 schema noise flag cannot be null")
    private Boolean dataset1SchemaNoise;

    @Min(value = 0, message = "Dataset1 schema noise value must be at least 0")
    private Integer dataset1SchemaNoiseValue;

    @NotNull(message = "Dataset1 schema key noise flag cannot be null")
    private Boolean dataset1SchemaKeyNoise;

    // new
    private Boolean deleteSchema1;

    @NotNull(message = "Dataset2 schema noise flag cannot be null")
    private Boolean dataset2SchemaNoise;

    @Min(value = 0, message = "Dataset2 schema noise value must be at least 0")
    private Integer dataset2SchemaNoiseValue;

    @NotNull(message = "Dataset2 schema key noise flag cannot be null")
    private Boolean dataset2SchemaKeyNoise;

    // new
    private Boolean deleteSchema2;

    @NotNull(message = "Dataset3 schema noise flag cannot be null")
    private Boolean dataset3SchemaNoise;

    @Min(value = 0, message = "Dataset3 schema noise value must be at least 0")
    private Integer dataset3SchemaNoiseValue;

    @NotNull(message = "Dataset3 schema key noise flag cannot be null")
    private Boolean dataset3SchemaKeyNoise;

    // new
    private Boolean deleteSchema3;

    @NotNull(message = "Dataset4 schema noise flag cannot be null")
    private Boolean dataset4SchemaNoise;

    @Min(value = 0, message = "Dataset4 schema noise value must be at least 0")
    private Integer dataset4SchemaNoiseValue;

    @NotNull(message = "Dataset4 schema key noise flag cannot be null")
    private Boolean dataset4SchemaKeyNoise;

    // new
    private Boolean deleteSchema4;


    // DATA NOISE

    @NotNull(message = "Dataset1 data noise flag cannot be null")
    private Boolean dataset1DataNoise;

    @NotNull(message = "Dataset2 data noise flag cannot be null")
    private Boolean dataset2DataNoise;

    // new
    @NotNull(message = "Dataset3 data noise flag cannot be null")
    private Boolean dataset3DataNoise;

    // new
    @NotNull(message = "Dataset4 data noise flag cannot be null")
    private Boolean dataset4DataNoise;



    @Min(value = 0, message = "Dataset1 data noise value must be at least 0")
    private Integer dataset1DataNoiseValue;

    @Min(value = 0, message = "Dataset2 data noise value must be at least 0")
    private Integer dataset2DataNoiseValue;

    // new
    @Min(value = 0, message = "Dataset1 data noise value must be at least 0")
    private Integer dataset3DataNoiseValue;

    // new
    @Min(value = 0, message = "Dataset1 data noise value must be at least 0")
    private Integer dataset4DataNoiseValue;


    @NotNull(message = "Dataset1 data key noise flag cannot be null")
    private Boolean dataset1DataKeyNoise;

    @NotNull(message = "Dataset2 data key noise flag cannot be null")
    private Boolean dataset2DataKeyNoise;

    // new
    @NotNull(message = "Dataset1 data key noise flag cannot be null")
    private Boolean dataset3DataKeyNoise;

    // new
    @NotNull(message = "Dataset1 data key noise flag cannot be null")
    private Boolean dataset4DataKeyNoise;

    // new
    private Integer dataset1NoiseInside;
    private Integer dataset2NoiseInside;
    private Integer dataset3NoiseInside;
    private Integer dataset4NoiseInside;


    @NotBlank(message = "Dataset1 shuffle option cannot be blank")
    private String dataset1ShuffleOption;

    @NotBlank(message = "Dataset2 shuffle option cannot be blank")
    private String dataset2ShuffleOption;

    // new
    @NotBlank(message = "Dataset1 shuffle option cannot be blank")
    private String dataset3ShuffleOption;

    // new
    @NotBlank(message = "Dataset2 shuffle option cannot be blank")
    private String dataset4ShuffleOption;

    @NotNull(message = "Selected schema methods for dataset1 cannot be null")
    private List<String> selectedSchemaMethods1;

    @NotNull(message = "Selected schema methods for dataset2 cannot be null")
    private List<String> selectedSchemaMethods2;

    // new
    @NotNull(message = "Selected schema methods for dataset3 cannot be null")
    private List<String> selectedSchemaMethods3;

    // new
    @NotNull(message = "Selected schema methods for dataset4 cannot be null")
    private List<String> selectedSchemaMethods4;

    @NotNull(message = "Selected string methods for dataset1 cannot be null")
    private List<String> selectedStringMethods1;

    @NotNull(message = "Selected string methods for dataset2 cannot be null")
    private List<String> selectedStringMethods2;

    // new
    @NotNull(message = "Selected string methods for dataset3 cannot be null")
    private List<String> selectedStringMethods3;

    // new
    @NotNull(message = "Selected string methods for dataset4 cannot be null")
    private List<String> selectedStringMethods4;

    @NotNull(message = "Selected numeric methods for dataset1 cannot be null")
    private List<String> selectedNumericMethods1;

    @NotNull(message = "Selected numeric methods for dataset2 cannot be null")
    private List<String> selectedNumericMethods2;

    // new
    @NotNull(message = "Selected numeric methods for dataset3 cannot be null")
    private List<String> selectedNumericMethods3;

    // new
    @NotNull(message = "Selected numeric methods for dataset4 cannot be null")
    private List<String> selectedNumericMethods4;

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
