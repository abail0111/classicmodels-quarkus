package de.bail.master.classic.util;

/**
 * Abstract Entity class with string identifier
 * Generic types are not accepted in graphql schema validation
 */
public abstract class GenericEntityStr {

    /**
     * Get identifier
     * @return identifier
     */
    public abstract String getId();

    /**
     * Set identifier
     * @param id identifier
     */
    public abstract void setId(String id);

}
