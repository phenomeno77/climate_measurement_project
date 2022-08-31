package at.qe.skeleton.tests;

import at.qe.skeleton.model.Room;
import at.qe.skeleton.model.Sensor;
import at.qe.skeleton.model.Users;
import at.qe.skeleton.model.UserRole;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

/**
 * Tests to ensure that each entity's implementation of equals conforms to the
 * contract. See {@linkplain http://www.jqno.nl/equalsverifier/} for more
 * information.
 *
 * This class is part of the skeleton project provided for students of the
 * courses "Software Architecture" and "Software Engineering" offered by the
 * University of Innsbruck.
 */
class EqualsImplementationTest {

    @Test
    void testUserEqualsContract() {
        Users users1 = new Users();
        users1.setUsername("user1");
        Users users2 = new Users();
        users2.setUsername("user2");



        Sensor sensor1 = new Sensor();
        sensor1.setSensorId("1");

        Sensor sensor2 = new Sensor();
        sensor2.setSensorId("2");

        Room room1 = new Room();
        room1.setRoomId(111);

        Room room2 = new Room();
        room2.setRoomId(222);


        EqualsVerifier.forClass(Users.class).withPrefabValues(Room.class,room1,room2).withPrefabValues(Users.class, users1, users2).withPrefabValues(Sensor.class, sensor1, sensor2).suppress(Warning.STRICT_INHERITANCE, Warning.ALL_FIELDS_SHOULD_BE_USED).verify();
    }

    @Test
    void testUserRoleEqualsContract() {
        EqualsVerifier.forClass(UserRole.class).verify();
    }

}