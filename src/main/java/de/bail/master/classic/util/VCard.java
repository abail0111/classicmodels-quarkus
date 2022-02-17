package de.bail.master.classic.util;

import de.bail.master.classic.model.enities.Contact;
import de.bail.master.classic.model.enities.Customer;
import de.bail.master.classic.model.enities.Employee;

import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class VCard {

    private static final String VCARD_NL = "\r\n";
    private static final String OL_LB = "=0D=0A=\r\n";

    public static String createFromEmployee(Employee employee) {
        return toVCard(employee);
    }

    public static String createFromCustomer(Customer customer) {
        return toVCard(customer);
    }

    private static String toVCard(Contact contact) {
        if (contact == null || contact.getFirstName() == null || contact.getLastName() == null) {
            throw new IllegalStateException("There is insufficient data to create a vcard from this contact");
        }
        // vcard header
        final StringBuilder vcard = new StringBuilder();
        final SimpleDateFormat iso8601DateFormat = new SimpleDateFormat("yyyyMMdd'T'HH:mm:'00Z'");
        vcard.append("BEGIN:VCARD" + VCARD_NL);
        vcard.append("VERSION:2.1" + VCARD_NL);
        // short name
        vcard.append("N;CHARSET=ISO-8859-1;LANGUAGE=en:")
                .append(contact.getLastName())
                .append(";")
                .append(contact.getFirstName())
                .append(VCARD_NL);
        // full name
        vcard.append("FN;CHARSET=ISO-8859-1:")
                .append(contact.getLastName())
                .append(",")
                .append(contact.getFirstName())
                .append(VCARD_NL);

        // create fields for employees
        if (contact instanceof Employee) {
            Employee employee = (Employee) contact;
            // job title
            vcard.append("TITLE;CHARSET=ISO-8859-1:")
                    .append(employee.getJobTitle())
                    .append(VCARD_NL);
            // organization
            vcard.append("ORG;CHARSET=ISO-8859-1:")
                    .append("Classic Models")
                    .append(VCARD_NL);
            // email
            vcard.append("EMAIL;INTERNET:")
                    .append(employee.getEmail())
                    .append(VCARD_NL);
        }

        // create fields for customer
        if (contact instanceof Customer) {
            Customer customer = (Customer) contact;
            // organization
            vcard.append("TITLE;CHARSET=ISO-8859-1;LANGUAGE=en:")
                    .append(customer.getCustomerName())
                    .append(VCARD_NL);
            // phone
            vcard.append("TEL;WORK;VOICE:")
                    .append(customer.getPhone())
                    .append(VCARD_NL);
            // address
            vcard.append("ADR;WORK;PREF;ENCODING=QUOTED-PRINTABLE:;;")
                    .append(customer.getCustomerName())
                    .append(OL_LB)
                    .append(customer.getAddressLine1())
                    .append(";")
                    .append(customer.getCity())
                    .append(";")
                    .append(customer.getState())
                    .append(";")
                    .append(customer.getPostalCode())
                    .append(";")
                    .append(customer.getCountry())
                    .append(VCARD_NL);
            // address label
            vcard.append("LABEL;WORK;PREF;ENCODING=QUOTED-PRINTABLE:")
                    .append(customer.getCustomerName())
                    .append(OL_LB)
                    .append(customer.getAddressLine1())
                    .append(OL_LB)
                    .append(customer.getPostalCode())
                    .append("  ")
                    .append(customer.getCity())
                    .append(OL_LB)
                    .append(customer.getState())
                    .append(OL_LB)
                    .append(customer.getCountry())
                    .append(VCARD_NL);
        }
        // vcard footer
        vcard.append("REV:").
                append(LocalDate.now()).
                append(VCARD_NL);
        vcard.append("END:VCARD");
        return vcard.toString();
    }

}
