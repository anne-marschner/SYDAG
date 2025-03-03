package org.anne_marschner_project.api;

import org.anne_marschner_project.core.Generator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Controller for handling form data submissions.
 */
@RestController
@RequestMapping("/api")
public class FormDataController {

    private static final Logger logger = LoggerFactory.getLogger(FormDataController.class);

    private static final String CSV_MIME_TYPE = "text/csv";
    private static final String OUTPUT_PATH_BASE = "src/main/resources";
    private static final String TEMP_PATH = "temp";
    private static final int BUFFER_SIZE = 1024;

    private final Generator generator;

    @Autowired
    public FormDataController(Generator generator) {
        this.generator = generator;
    }

    /**
     * Endpoint to handle the form submission for running SYDAG.
     *
     * @param formDataWrapper The form data.
     * @param bindingResult   The binding result for validation errors.
     * @param csvFile         The uploaded CSV file.
     * @return A ResponseEntity with the result of the operation.
     */
    @PostMapping(value = "/runSYDAG", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> handleFormSubmit(@RequestPart("parameters") FormDataWrapper formDataWrapper, BindingResult bindingResult, @RequestPart(value = "csvFile", required = false) MultipartFile csvFile) throws IOException {
        // Check for binding errors
        if (bindingResult.hasErrors()) {
            return createBindingErrorResponse(bindingResult);
        }

        // Validate CSV file
        ResponseEntity<?> fileValidationResponse = validateCsvFile(csvFile);
        if (fileValidationResponse != null) {
            return fileValidationResponse;
        }

        // Prepare output path and generator parameters

        GeneratorParameters params = new GeneratorParameters();
        params.setCsvFile(csvFile);
        params.setFormDataWrapper(formDataWrapper);

        // Cleanup before generating
        deleteFilesInDirectory(TEMP_PATH);
        deleteFilesInDirectory(OUTPUT_PATH_BASE);

        // Execute generator and build ZIP response
        try {
            String generatorOutputPath = createGeneratorOutputPath(csvFile);
            generator.execute(params, generatorOutputPath);

            Path zipFilePath = createZipFromDirectory(OUTPUT_PATH_BASE);
            return buildZipResponse(zipFilePath);

        } catch (Exception e) {
            logger.error("Error processing request", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while generating datasets.");
        } finally {
            // Cleanup
            deleteFilesInDirectory(TEMP_PATH);
            deleteFilesInDirectory(OUTPUT_PATH_BASE);
        }
    }

    /**
     * Creates a standardized error response for form binding results.
     */
    private ResponseEntity<Map<String, Object>> createBindingErrorResponse(BindingResult bindingResult) {
        List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", "error");
        errorResponse.put("errors", errors);

        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Validates the CSV file, returns an error response if invalid.
     * If null is returned, it means the file is valid.
     */
    private ResponseEntity<?> validateCsvFile(MultipartFile csvFile) {
        if (csvFile == null || csvFile.isEmpty()) {
            return createSingleErrorResponse("CSV file is required");
        } else if (!Objects.equals(csvFile.getContentType(), CSV_MIME_TYPE)) {
            return createSingleErrorResponse("Invalid file type. Only CSV files are accepted.");
        }
        return null;
    }

    /**
     * Creates a single-message error response.
     */
    private ResponseEntity<Map<String, Object>> createSingleErrorResponse(String errorMessage) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", "error");
        errorResponse.put("errors", Collections.singletonList(errorMessage));
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Prints (logs) all fields from FormDataWrapper using reflection.
     */
    private void printFormData(FormDataWrapper formDataWrapper) {
        Field[] fields = FormDataWrapper.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(formDataWrapper);
                if (value instanceof MultipartFile multipartFile) {
                    System.out.println(field.getName() + ": " + multipartFile.getOriginalFilename());
                } else if (value instanceof List<?> list) {
                    System.out.println(field.getName() + ":");
                    if (list.isEmpty()) {
                        System.out.println(" - (empty list)");
                    } else {
                        for (Object elem : list) {
                            System.out.println(" - " + elem);
                        }
                    }
                } else {
                    System.out.println(field.getName() + ": " + (value != null ? value.toString() : "null"));
                }
            } catch (IllegalAccessException e) {
                System.err.println("Failed to access field " + field.getName() + ": " + e.getMessage());
            }
        }
    }

    /**
     * Creates the generator output path (folder name based on the CSV file).
     */
    private String createGeneratorOutputPath(MultipartFile csvFile) {
        String fileName = (csvFile.getOriginalFilename() != null) ? csvFile.getOriginalFilename().replace(".csv", "") : "defaultName";
        return OUTPUT_PATH_BASE + "/" + fileName;
    }

    /**
     * Zips all files in a given directory and returns the path to the zipped file.
     */
    private Path createZipFromDirectory(String directoryPath) throws IOException {
        Path zipFilePath = Files.createTempFile("datasets-", ".zip");

        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipFilePath))) {
            File outputFolder = new File(directoryPath);
            File[] files = outputFolder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (!file.isDirectory() && !file.getName().equals("application.yml")) {
                        zipSingleFile(zos, file);
                    }
                }
            }
        }
        return zipFilePath;
    }

    /**
     * Zips a single file into the given ZipOutputStream.
     */
    private void zipSingleFile(ZipOutputStream zos, File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            ZipEntry zipEntry = new ZipEntry(file.getName());
            zos.putNextEntry(zipEntry);

            byte[] buffer = new byte[BUFFER_SIZE];
            int length;
            while ((length = fis.read(buffer)) >= 0) {
                zos.write(buffer, 0, length);
            }

            zos.closeEntry();
        }
    }

    /**
     * Builds the HTTP response for the newly created ZIP file.
     */
    private ResponseEntity<InputStreamResource> buildZipResponse(Path zipFilePath) throws FileNotFoundException {
        InputStreamResource resource = new InputStreamResource(new FileInputStream(zipFilePath.toFile()));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=datasets.zip");

        return ResponseEntity.ok().headers(headers).contentLength(zipFilePath.toFile().length()).contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
    }

    /**
     * Deletes all files (non-directories) in the specified directory.
     */
    private void deleteFilesInDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    // Exclude application.yml from deletion
                    if (!file.isDirectory() && !file.getName().equals("application.yml")) {
                        file.delete();
                    }
                }
            }
        }
    }
}
