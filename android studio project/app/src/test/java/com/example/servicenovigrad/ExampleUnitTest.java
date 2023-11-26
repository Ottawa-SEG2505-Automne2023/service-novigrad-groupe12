package com.example.servicenovigrad;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.servicenovigrad.backend.account.Account;
import com.example.servicenovigrad.backend.services.ElementType;
import com.example.servicenovigrad.backend.services.ExtraFormData;
import com.example.servicenovigrad.backend.services.FormElement;
import com.example.servicenovigrad.backend.services.ServiceForm;
import com.example.servicenovigrad.backend.util.validators.OldDateValidator;
import com.example.servicenovigrad.backend.util.validators.UserPassValidator;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void testValidateText() {
        UserPassValidator validator = new UserPassValidator(null, null, null);

        // Alphanumeric with allowed specials
        assertTrue(validator.validateText("abc123!@"));

        // Alphanumeric with disallowed specials
        assertFalse(validator.validateText("abc/123"));

        // Alphanumeric with space (disallowed)
        assertFalse(validator.validateText("abc 123"));

        // Empty (currently disallowed)
        assertFalse(validator.validateText(""));

        // Allow empty and try again
        validator.allowEmpty();
        assertTrue(validator.validateText(""));

        // Random test case
        assertTrue(validator.validateText("GIaSFeLFeBReHBéR$090020"));

        System.out.println("testValidateText passed!");
    }

    @Test
    public void testCompareAccount() {
        Account admin1 = new Account("", "A", "", "administrateur", "p");
        Account admin2 = new Account("", "B", "", "administrateur", "p");
        Account employ1 = new Account("", "A", "", "Employé de la succursale", "p");
        Account employ2 = new Account("", "B", "", "Employé de la succursale", "p");
        Account client1 = new Account("", "A", "", "Client", "p");
        Account client2 = new Account("", "B", "", "Client", "p");
        Account client3 = new Account("", "B", "", "Client", "p");

        assertEquals(-1, admin1.compareTo(admin2));
        assertEquals(1, admin2.compareTo(admin1));
        assertEquals(-1, admin2.compareTo(client1));
        assertEquals(-1, employ1.compareTo(client1));
        assertEquals(1, employ1.compareTo(admin1));
        assertEquals(0, client2.compareTo(client3));
        assertEquals(-1, admin2.compareTo(employ2));

        System.out.println("testCompareAccount passed!");
    }

    @Test
    // DateValidator won't really be used until we implement the client's ability to fill the form
    // But always a good idea to preemptively test it!
    // Should be valid if the entered date (yyyy-mm-dd) is at least 18 years ago
    public void testDateValidator() {
        OldDateValidator validator = new OldDateValidator(null, null, null);

        // Valid date
        assertTrue(validator.validateText("2004-02-19"));

        // Invalid date (month 00)
        assertFalse(validator.validateText("1980-00-01"));

        // Valid date
        assertTrue(validator.validateText("1980-01-01"));

        // Invalid date (too recent)
        assertFalse(validator.validateText("2020-12-25"));

        // Invalid format
        assertFalse(validator.validateText("11/11/2000"));

        System.out.println("testDateValidator passed!");
    }

    @Test
    // Two services should be equal if their IDs are the same (IDs should be unique)
    public void testEquateServices() {
        ServiceForm s1 = new ServiceForm();
        s1.setId("A");
        ServiceForm s2 = new ServiceForm();
        s2.setId("A");
        assertTrue(s1.equals(s2));

        System.out.println("testEquateServices passed!");
    }

    @Test
    public void testFillService() {
        // Create a service
        ServiceForm s = new ServiceForm();
        s.setId(s.toString());
        s.setName("testService");

        // Add a bunch of elements
        for (int i = 0; i < 10; i++) {
            ExtraFormData extra = new ExtraFormData();
            for (int j = 0; j < 5; j++) {extra.getElements().add("elem"+j);}
            FormElement elem = new FormElement(ElementType.SPINNER, "spinner" + i, extra);
            s.getElements().add(elem);
        }
        for (int i = 0; i < 10; i++) {
            ExtraFormData extra = new ExtraFormData();
            extra.setCharLimit(100);
            extra.setValidatorClass(2);
            FormElement elem = new FormElement(ElementType.TEXTFIELD, "field" + i, extra);
            s.getElements().add(elem);
        }
        for (int i = 0; i < 10; i++) {
            FormElement elem = new FormElement(ElementType.DOCUMENT, "document" + i, null);
            s.getElements().add(elem);
        }

        assertEquals(30, s.getElements().size());

        System.out.println("testFillService passed!");
    }
}