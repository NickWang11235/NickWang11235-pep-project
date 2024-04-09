package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {

    private AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }

    /**
     * validates the format of username and password
     * verify username is not taken
     * insert new account into account table
     * 
     * @param act
     * @return Account
     */
    public Account postRegister(Account act) {
        // validates format of username and password
        if (act.username.isBlank() || act.password.length() < 4) {
            return null;
        }
        // verify username is not taken
        Account usernameLpokup = accountDAO.selectAccountByUsername(act.username);
        if (usernameLpokup != null) {
            return null;
        }
        // inserts into table
        return accountDAO.insertAccount(act);
    }

    /**
     * verify username exists
     * authenticate password
     * 
     * @param act
     * @return Account
     */
    public Account postLogin(Account act) {
        // verify username exists
        Account usernameLoopup = accountDAO.selectAccountByUsername(act.username);
        if (usernameLoopup == null) {
            return null;
        }
        // authenticate password
        if (!act.password.equals(usernameLoopup.password)) {
            return null;
        }
        return usernameLoopup;
    }

}
