package org.anne_marschner_project.api;

import org.anne_marschner_project.core.Generator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@RestController
@RequestMapping("/api")
public class FormDataController {

    private static final Logger logger = LoggerFactory.getLogger(FormDataController.class);
    private static final String OUTPUT_PATH_BASE = "results";
    private static final String TEMP_PATH = "temp";
    private static final int BUFFER_SIZE = 1024;

    // List of accepted CSV MIME types.
    private static final List<String> ACCEPTED_CSV_MIME_TYPES = Arrays.asList("text/csv", "application/vnd.ms-excel", "application/csv", "text/plain");

    private final Generator generator;

    @Autowired
    public FormDataController(Generator generator) {
        this.generator = generator;
    }

    /**
     * Endpoint to handle the form submission for running SYDAG.
     * This method validates the input, runs the generator, and streams
     * a ZIP archive of the generated files from the results directory.
     */
    @PostMapping(value = "/runSYDAG", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<StreamingResponseBody> handleFormSubmit(@RequestPart("parameters") FormDataWrapper formDataWrapper, BindingResult bindingResult, @RequestPart(value = "csvFile", required = false) MultipartFile csvFile) throws IOException {

        // Check for binding errors.
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(null);
        }

        // Validate CSV file.
        ResponseEntity<?> fileValidationResponse = validateCsvFile(csvFile);
        if (fileValidationResponse != null) {
            return ResponseEntity.badRequest().body(null);
        }

        GeneratorParameters params = new GeneratorParameters();
        params.setCsvFile(csvFile);
        params.setFormDataWrapper(formDataWrapper);

        // Clean directories before execution.
        cleanupDirectories(TEMP_PATH, OUTPUT_PATH_BASE);

        try {
            String generatorOutputPath = createGeneratorOutputPath(csvFile);
            generator.execute(params, generatorOutputPath);

            // Build the streaming response by reading from the results directory on-the-fly.
            StreamingResponseBody stream = outputStream -> {
                try (ZipOutputStream zos = new ZipOutputStream(outputStream)) {
                    File directory = new File(OUTPUT_PATH_BASE);
                    File[] files = directory.listFiles();
                    if (files != null) {
                        for (File file : files) {
                            if (!file.isDirectory() && !"application.yml".equals(file.getName())) {
                                zos.putNextEntry(new ZipEntry(file.getName()));
                                try (FileInputStream fis = new FileInputStream(file)) {
                                    byte[] buffer = new byte[BUFFER_SIZE];
                                    int length;
                                    while ((length = fis.read(buffer)) != -1) {
                                        zos.write(buffer, 0, length);
                                    }
                                }
                                zos.closeEntry();
                            }
                        }
                    }
                    zos.finish();
                } catch (IOException e) {
                    logger.error("Error streaming zip file", e);
                    throw e;
                } finally {
                    // Clean up temporary directories after streaming completes.
                    cleanupDirectories(TEMP_PATH, OUTPUT_PATH_BASE);
                }
            };

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=datasets.zip");

            return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_OCTET_STREAM).body(stream);

        } catch (Exception e) {
            logger.error("Error processing request", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Validates the uploaded CSV file.
     */
    private ResponseEntity<?> validateCsvFile(MultipartFile csvFile) {
        if (csvFile == null || csvFile.isEmpty()) {
            return createSingleErrorResponse("CSV file is required");
        }
        String contentType = csvFile.getContentType();
        if (!ACCEPTED_CSV_MIME_TYPES.contains(contentType)) {
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
     * Creates the generator output path (folder name based on the CSV file name).
     */
    private String createGeneratorOutputPath(MultipartFile csvFile) {
        String fileName = (csvFile.getOriginalFilename() != null) ? csvFile.getOriginalFilename().replace(".csv", "") : "defaultName";
        return OUTPUT_PATH_BASE + "/" + fileName;
    }

    /**
     * Deletes all non-directory files (excluding application.yml) within the specified directory.
     */
    private void cleanupDirectory(String directoryPath) {
        File directory = new File(directoryPath);
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (!file.isDirectory() && !"application.yml".equals(file.getName())) {
                        if (!file.delete()) {
                            logger.warn("Failed to delete file: {}", file.getAbsolutePath());
                        }
                    }
                }
            }
        }
    }

    /**
     * Utility method to clean up multiple directories.
     */
    private void cleanupDirectories(String... directories) {
        for (String dir : directories) {
            cleanupDirectory(dir);
        }
    }
}