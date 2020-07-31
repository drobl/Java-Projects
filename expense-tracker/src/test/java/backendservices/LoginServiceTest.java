package backendservices;

import controllers.LoginSceneController;
import exceptions.LoginException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for LoginSceneController
 */
public class LoginServiceTest extends TestCase {
  /**
   * Create the test case
   *
   * @param testName name of the test case
   */
  public LoginServiceTest(String testName) {
    super(testName);
  }

  /**
   * @return the suite of tests being tested
   */
  public static Test suite() {
    return new TestSuite(LoginServiceTest.class);
  }

  /**
   * Tests whether a {@link exceptions.LoginException} is thrown when trying to log in with invalid
   * user credentials.
   */
  public void testLoginException() {
    try {
      LoginService.getUserByLoginCredentials("blabla", "blabla");
      fail();
    } catch (Exception e) {
      assertEquals(e.getClass(), LoginException.class);
    }
    // test if exception is NOT thrown when credentials are correct:
    try {
      LoginService.getUserByLoginCredentials("maxmus", "maxmus123");
    } catch (Exception e) {
      fail();
    }
  }
}
