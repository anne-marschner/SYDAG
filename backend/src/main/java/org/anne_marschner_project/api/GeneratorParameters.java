package org.anne_marschner_project.api;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * Encapsulates parameters for the Generator execution.
 */
@Data
public class GeneratorParameters {

    private MultipartFile csvFile;
    private FormDataWrapper formDataWrapper;

}
