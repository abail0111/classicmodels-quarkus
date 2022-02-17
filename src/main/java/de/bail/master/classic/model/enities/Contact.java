package de.bail.master.classic.model.enities;

import javax.validation.constraints.NotNull;

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
