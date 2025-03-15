package org.anne_marschner_project.core.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a dataset that contains a list of {@link Relation}.
 * The Dataset class is used to store and manage multiple relations.
 */
public class Dataset {

    private List<Relation> relations;


    /**
     * Constructs an empty Dataset with no relations.
     *
     * Initializes the relations list as an empty ArrayList.
     */
    public Dataset() {
        this.relations = new ArrayList<>();
    }


    /**
     * Constructs a Dataset with a specified list of relations.
     *
     * @param relations the list of relations to be included in the dataset.
     */
    public Dataset(List<Relation> relations) {
        this.relations = relations;
    }


    /**
     * Returns the list of relations contained in this dataset.
     *
     * @return a list of Relation objects.
     */
    public List<Relation> getRelations() {
        return relations;
    }


    /**
     * Sets the list of relations for this dataset.
     *
     * @param relations the list of Relation objects to be set.
     */
    public void setRelations(List<Relation> relations) {
        this.relations = relations;
    }
}

