/**
 * 
 */
package notifications;

import org.junit.Test;
import junit.framework.TestCase;

/**
 * This test the correctness of the notifications the user receives
 * 
 * @author Daniel Robles
 *
 */
public class AccountWarningsTest extends TestCase {

  /**
   * Create the test case for AccountWarnings class
   *
   * @param testName name of the test case
   */
  public AccountWarningsTest(String testName) {
    super(testName);
  }


  AccountWarnings notifier = new AccountWarnings();


  @Test
  public void testCorrectMsgFormattingTest() {
    /*
    String returnedMSG = notifier.notifyUserTestingOnly("Test_9_Account",
        "This message should equal that in test", 1345);

    String accountName = "Test_9_Account";
    String message = "This message should equal that in test";
    String amount = "1,345.00";
    String composedMSG = "Alert! Attention Needed on account: " + accountName + "\n Amount "
        + amount + "\n Additional Info: " + message;
    assertTrue(returnedMSG.equals(composedMSG));
    // System.out.println(returnedMSG);
    // System.out.println(composedMSG);
  */

  }


}
