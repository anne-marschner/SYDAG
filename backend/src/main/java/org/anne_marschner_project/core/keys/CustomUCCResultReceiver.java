package org.anne_marschner_project.core.keys;

import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithm_integration.result_receiver.UniqueColumnCombinationResultReceiver;
import de.metanome.algorithm_integration.results.UniqueColumnCombination;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


/**
 * A custom implementation of the {@link UniqueColumnCombinationResultReceiver} interface,
 * used for collecting and managing unique column combinations (UCCs) found in a dataset.
 * This implementation stores UCCs in a list and provides access to them for further processing.
 */
public class CustomUCCResultReceiver implements UniqueColumnCombinationResultReceiver {

    private List<Set<ColumnIdentifier>> allUCCs = new ArrayList<>();


    /**
     * Receives a UniqueColumnCombination result, logs it, and stores it in a list.
     *
     * @param uniqueColumnCombination the unique column combination to be stored.
     */
    @Override
    public void receiveResult(UniqueColumnCombination uniqueColumnCombination) {
        System.out.println("Found UCC: "
                + uniqueColumnCombination.getColumnCombination().getColumnIdentifiers());

        // Add Sets of UniqueColumnIdentifiers
        allUCCs.add(uniqueColumnCombination.getColumnCombination().getColumnIdentifiers());
    }


    /**
     * Determines if a given UniqueColumnCombination is accepted by this receiver.
     * In this implementation, all UCCs are accepted.
     *
     * @param uniqueColumnCombination the unique column combination to be checked.
     * @return true, indicating that the result is accepted.
     */
    @Override
    public Boolean acceptedResult(UniqueColumnCombination uniqueColumnCombination) {
        return true;
    }


    /**
     * Retrieves all unique column combinations (UCCs) collected by this receiver.
     *
     * @return a list of sets of ColumnIdentifier objects representing unique column combinations.
     */
    public List<Set<ColumnIdentifier>> getAllUCCs() {
        return allUCCs;
    }
}


