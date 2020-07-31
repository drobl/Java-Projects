/**
 * 
 */
package user;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.Test;
import account.Account;
import account.CashAccount;
import account.Currency;
import expensetracker.JavaFXApp;
import junit.framework.TestCase;

/**
 * This tests different aspects of the user class, including creating new user, and traversing
 * accounts
 * 
 * @author DAN
 *
 */
public class UserTest extends TestCase {

  /**
   * Create the test case for user class
   *
   * @param testName name of the test case
   */
  public UserTest(String testName) {
    super(testName);
  }

  /**
   * Test objects
   */
  User testUser1 = new User(1, "Johnny", "Walker");
  User testUser2 = new User(2, "Mary", "Lamb");
  User testUser3 = new User(3, "Citizen", "Kane");
  User testUser4 = new User(4, "Homer", "Simpson");
  User testUser5 = new User(5, "Stan", "Marsh");



  /**
   * Test method for {@link user.User#getFirstName()}.
   */
  @Test
  public void testGetFirstName() {
    assertTrue(testUser1.getFirstName().equals("Johnny"));
    assertTrue(testUser3.getFirstName().equals("Citizen"));

  }

  /**
   * Test method for {@link user.User#getLastName()}.
   */
  @Test
  public void testGetLastName() {
    assertTrue(testUser2.getLastName().equals("Lamb"));
    assertTrue(testUser4.getLastName().equals("Simpson"));
  }

  /**
   * Test method for {@link user.User#getId()}.
   */
  @Test
  public void testGetId() {
    assertTrue(testUser1.getId() <= 5 && testUser1.getId() > 0);
    assertTrue(testUser2.getId() <= 5 && testUser2.getId() > 0);
    assertTrue(testUser3.getId() <= 5 && testUser3.getId() > 0);
    assertTrue(testUser4.getId() <= 5 && testUser4.getId() > 0);
    assertTrue(testUser5.getId() <= 5 && testUser5.getId() > 0);
  }

  /**
   * Test method for {@link user.User#getAccountList()}. Checks that the account exists and returns
   * it
   * 
   * @return
   */
  @Test
  public List<Account> testGetAccountList(User user) {
    assertTrue(user.getAccountList() != null);
    return user.getAccountList();
  }

  /**
   * Test method to test adding accounts, retrieving them, and getting formatted sums Also tests
   * {@link user.User#getBalanceSum()}
   */
  @Test
  public void testAccountTest() {

    List<Account> user1AccountList = new ArrayList<>();
    user1AccountList.add(new CashAccount(1, "TestAccount", 1200, new Date(), Currency.USD));
    user1AccountList.add(new CashAccount(1, "TestAccount2", 1456, new Date(), Currency.EUR));
    testUser1.setAccountList(user1AccountList);

    List<Account> user2AccountList = new ArrayList<>();
    user2AccountList.add(new CashAccount(1, "TestAccount3", 5555.66, new Date(), Currency.GBP));
    testUser2.setAccountList(user2AccountList);

    assertTrue(testGetAccountList(testUser1).size() == 2);
    assertEquals(2, testUser1.getAccountCount());

    assertTrue(testUser2.getAccountCount() == 1);

    assertTrue(testGetAccountList(testUser3).size() == 0);
    assertTrue(testGetAccountList(testUser4).size() == 0);


    assertTrue(testUser5.getAccountCount() == 0);

  }


}
