
package com.fancode;

import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertTrue;


public class UtilsTest {

    @Test
    public void testIsFanCodeUser() {
        User user1 = new User();
        Address address1 = new Address();
        Geo geo1 = new Geo();
        geo1.setLat("-10");
        geo1.setLng("50");
        address1.setGeo(geo1);
        user1.setAddress(address1);

        assertTrue(Utils.isFanCodeUser(user1));

        User user2 = new User();
        Address address2 = new Address();
        Geo geo2 = new Geo();
        geo2.setLat("-50");
        geo2.setLng("150");
        address2.setGeo(geo2);
        user2.setAddress(address2);

        assertFalse(Utils.isFanCodeUser(user2));
    }

    @Test
    public void testCalculateCompletionPercentage() {
        List<Todo> todos = Arrays.asList(
                new Todo() {{ setCompleted(true); }},
                new Todo() {{ setCompleted(false); }},
                new Todo() {{ setCompleted(true); }}
        );

        assertTrue(Utils.calculateCompletionPercentage(todos) == 66.66666666666667);

        todos = Arrays.asList(
                new Todo() {{ setCompleted(false); }},
                new Todo() {{ setCompleted(false); }}
        );

        assertTrue(Utils.calculateCompletionPercentage(todos) == 0);
    }
}
