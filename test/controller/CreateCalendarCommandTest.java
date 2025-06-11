// Dreshta Boghra & Aaron Zhou
// CS3500 HW5

package controller;

/**
 * Testing class for the CreateCalendarCommandTest class in the Controller directory.
 */
public class CreateCalendarCommandTest {
  //create a calendar with an invalid zone -> error
  //create a calendar with a name of a calendar that has already been made -> error
  //create a calendar with a valid name and zone
  //Null calendar name -> Should throw an IllegalArgumentException or similar.
  //Empty calendar name -> error
  //Null timezone -> should throw an exception if passed.
  //Create "Work" then try to create "work" → allowed
  //Create multiple calendars in succession -> should work fine
}
