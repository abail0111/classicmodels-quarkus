package de.bail.master.classic.util;

/**
 * Abstract Entity class with integer identifier
 * Generic types are not accepted in graphql schema validation
 */
public abstract class GenericEntity {

    /**
     * Get identifier
     * @return identifier
     */
    public abstract Integer getId();

    /**
     * Set identifier
     * @param id identifier
     */
    public abstract void setId(Integer id);

}
