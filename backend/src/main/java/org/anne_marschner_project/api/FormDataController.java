package org.anne_marschner_project.api;

import org.anne_marschner_project.core.Generator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.context.support.DefaultMessageSourceResolvable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Controller for handling form data submissions.
 */
@RestController
@RequestMapping("/api")
public class FormDataController {

    private static final Logger logger = LoggerFactory.getLogger(FormDataController.class);

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
    public ResponseEntity<?> handleFormSubmit(@RequestPart("parameters") FormDataWrapper formDataWrapper, BindingResult bindingResult, @RequestPart(value = "csvFile", required = false) MultipartFile csvFile) {

        // Validate form data
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("errors", errors);
            return ResponseEntity.badRequest().body(errorResponse);
        }

        // Validate the CSV file
        if (csvFile == null || csvFile.isEmpty()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("errors", List.of("CSV file is required"));
            return ResponseEntity.badRequest().body(errorResponse);
        } else if (!Objects.equals(csvFile.getContentType(), "text/csv")) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("errors", List.of("Invalid file type. Only CSV files are accepted."));
            return ResponseEntity.badRequest().body(errorResponse);
        }

        try {
            // Create parameter object
            GeneratorParameters params = new GeneratorParameters();
            params.setCsvFile(csvFile);
            params.setFormDataWrapper(formDataWrapper);

            // Execute the generator logic
            generator.execute(params);

            // Return a success response
            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("status", "success");
            successResponse.put("message", "Form data processed successfully");
            return ResponseEntity.ok(successResponse);

        } catch (Exception e) {
            logger.error("Error processing request", e);
            throw e; // Let GlobalExceptionHandler handle it
        }
    }
}
