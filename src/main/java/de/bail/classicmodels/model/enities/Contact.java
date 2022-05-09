package de.bail.classicmodels.model.enities;

import javax.validation.constraints.NotNull;

/**
 * Contact Interface
 */
public interface Contact {

    Integer getId();

    @NotNull
    String getFirstName();

    void setFirstName(String firstName);

    @NotNull
    String getLastName();

    void setLastName(String lastName);

}
