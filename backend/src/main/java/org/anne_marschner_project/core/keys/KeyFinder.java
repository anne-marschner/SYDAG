package org.anne_marschner_project.core.keys;

import de.metanome.algorithm_integration.AlgorithmExecutionException;
import de.metanome.algorithm_integration.ColumnIdentifier;
import de.metanome.algorithms.hyucc.HyUCC;
import org.anne_marschner_project.core.data.Relation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * The KeyFinder class is responsible for identifying the key columns within a given {@link Relation}.
 * It applies the HyUCC Algorithm to find unique column combinations (UCCs).
 */
public class KeyFinder {


    /**
     * Identifies the indices of key columns in the provided {@link Relation} using the HyUCCAlgorithm.
     * The key columns uniquely identify each row within the relation.
     *
     * @param relation the relation to analyze for key columns
     * @param relationName the name of the relation for identification purposes
     * @return a list of integers representing the indices of the key columns within the relation schema
     * @throws AlgorithmExecutionException if an error occurs while executing the HyUCC-Algorithm
     */
    public List<Integer> findKeyIndices(Relation relation, String relationName) throws AlgorithmExecutionException {

        // Create correct Input format for HyUCC algorithm
        CustomRelationalInput relationalInput = new CustomRelationalInput(relation, relationName);
        CustomRelationalInputGenerator inputGenerator = new CustomRelationalInputGenerator(relationalInput);
        CustomUCCResultReceiver uccReceiver = new CustomUCCResultReceiver();

        HyUCC hyucc = new HyUCC();

        // Set needed configuration
        hyucc.setRelationalInputConfigurationValue(HyUCC.Identifier.INPUT_GENERATOR.name(), inputGenerator);
        hyucc.setResultReceiver(uccReceiver);
        hyucc.setBooleanConfigurationValue(HyUCC.Identifier.NULL_EQUALS_NULL.name(), true);
        hyucc.setBooleanConfigurationValue(HyUCC.Identifier.VALIDATE_PARALLEL.name(), true);

        // Execute HyUCC algorithm to receive UCCs
        hyucc.execute();
        System.out.println("UCCs were found successfully and sent to Result Receiver.");

        // Get the UCC with the smallest amount of columns since it represents the key (should be the first one in the list)
        List<Set<ColumnIdentifier>> allUCCs = uccReceiver.getAllUCCs();
        List<Integer> keyColumns = new ArrayList<>();

        if (!allUCCs.isEmpty()) {
            // Map the column identifiers to their parsed integer values
            keyColumns = allUCCs.get(0).stream()
                    .map(ColumnIdentifier::getColumnIdentifier)
                    .map(Integer::parseInt)
                    .toList();
        }
        return keyColumns;
    }
}




