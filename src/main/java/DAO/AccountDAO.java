package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {

    /**
     * select the account with username
     * 
     * @param username
     * @return Account the account with username. Null if username does not exist
     */
    public Account selectAccountByUsername(String username) {
        Connection connection = ConnectionUtil.getConnection();
        Account account = null;
        try {
            // select statement
            String sql = "SELECT * FROM account WHERE username=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                account = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return account;
    }

    /**
     * select the account with account_id
     * 
     * @param account_id
     * @return Account the account with account_id. Null if account_id does not
     *         exist
     */
    public Account selectAccountByAccountId(int account_id) {
        Connection connection = ConnectionUtil.getConnection();
        Account account = null;
        try {
            // select statement
            String sql = "SELECT * FROM account WHERE account_id=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, account_id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                account = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return account;
    }

    /**
     * insert new account into the account table
     * 
     * @param act
     * @return Account the account to be generated. Null if insertion fails
     */
    public Account insertAccount(Account act) {
        Connection connection = ConnectionUtil.getConnection();
        Account account = null;
        try {
            String sql = "INSERT INTO account (username, password) VALUES (?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, act.username);
            ps.setString(2, act.password);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int generatedId = (int) rs.getLong("account_id");
                account = new Account(generatedId, act.username, act.password);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return account;
    }
}
