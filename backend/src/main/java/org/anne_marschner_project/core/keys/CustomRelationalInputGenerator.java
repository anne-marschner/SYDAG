package org.anne_marschner_project.core.keys;

import de.metanome.algorithm_integration.input.RelationalInput;
import de.metanome.algorithm_integration.input.RelationalInputGenerator;

/**
 * A custom implementation of the {@link RelationalInputGenerator} interface that provides
 * a predefined {@link RelationalInput} instance.
 */
public class CustomRelationalInputGenerator implements RelationalInputGenerator {

    RelationalInput relationalInput;


    /**
     * Constructs a new CustomRelationalInputGenerator with a specified RelationalInput.
     *
     * @param relationalInput the RelationalInput to be used by this generator.
     */
    public CustomRelationalInputGenerator(RelationalInput relationalInput) {
        this.relationalInput = relationalInput;
    }


    /**
     * Returns the predefined RelationalInput instance.
     * In this use case we do not need to return a copy.
     *
     * @return the RelationalInput instance specified at construction.
     */
    @Override
    public RelationalInput generateNewCopy() {
        return relationalInput;
    }


    /**
     * Closes the input generator. This implementation does not manage resources, so this method
     * performs no actions. It had to be overwritten anyway.
     */
    @Override
    public void close() {
    }
}
