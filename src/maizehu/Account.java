package maizehu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Account {
    private String cardid;
    private String uesrname;
    private char sex;
    private String passWord;
    private double money;
    private double limit;

    public Account() {}

    public Account(String cardid, String uesrname, char sex, String passWord, double money, double limit) {
        this.cardid = cardid;
        this.uesrname = uesrname;
        this.sex = sex;
        this.passWord = passWord;
        this.money = money;
        this.limit = limit;
    }

    public String getCardid() {
        return cardid;
    }

    public void setCardid(String cardid) {
        this.cardid = cardid;
    }

    public String getUesrname() {
        return uesrname + (sex == '男' ? "先生" : "女士");
    }

    public void setUesrname(String uesrname) {
        this.uesrname = uesrname;
    }

    public char getSex() {
        return sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public double getLimit() {
        return limit;
    }

    public void setLimit(double limit) {
        this.limit = limit;
    }

    // 从数据库中获取账户信息
    public static Account getAccountByCardId(String cardId) {
        String sql = "SELECT * FROM accounts WHERE cardid = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cardId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String username = rs.getString("username");
                char sex = rs.getString("sex").charAt(0);
                String password = rs.getString("password");
                double money = rs.getDouble("money");
                double limit = rs.getDouble("limit_amount");
                return new Account(cardId, username, sex, password, money, limit);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 将账户信息保存到数据库
    public void saveToDatabase() {
        String sql = "INSERT INTO accounts (cardid, username, sex, password, money, limit_amount) VALUES (?,?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cardid);
            pstmt.setString(2, uesrname);
            pstmt.setString(3, String.valueOf(sex));
            pstmt.setString(4, passWord);
            pstmt.setDouble(5, money);
            pstmt.setDouble(6, limit);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 更新账户信息到数据库
    public void updateInDatabase() {
        String sql = "UPDATE accounts SET username = ?, sex = ?, password = ?, money = ?, limit_amount = ? WHERE cardid = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, uesrname);
            pstmt.setString(2, String.valueOf(sex));
            pstmt.setString(3, passWord);
            pstmt.setDouble(4, money);
            pstmt.setDouble(5, limit);
            pstmt.setString(6, cardid);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 从数据库中删除账户信息
    public void deleteFromDatabase() {
        String sql = "DELETE FROM accounts WHERE cardid = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cardid);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}