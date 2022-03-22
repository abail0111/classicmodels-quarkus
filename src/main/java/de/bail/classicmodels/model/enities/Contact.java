package de.bail.classicmodels.model.enities;

import javax.validation.constraints.NotNull;

/**
 * Contact Interface
 */
public interface Contact {

    Integer getId();

    @NotNull
    String getFirstName();

    @NotNull
    void setFirstName(String firstName);

    @NotNull
    String getLastName();

    @NotNull
    void setLastName(String lastName);

}
