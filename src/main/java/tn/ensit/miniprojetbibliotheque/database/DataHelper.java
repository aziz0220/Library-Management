package tn.ensit.miniprojetbibliotheque.database;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import tn.ensit.miniprojetbibliotheque.models.Livre;
import tn.ensit.miniprojetbibliotheque.models.MailServerInfo;
//import tn.ensit.miniprojetbibliotheque.listmember.MemberListController.Member;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class DataHelper {

    private final static Logger LOGGER = LogManager.getLogger(DatabaseHandler.class.getName());

    public static boolean insertNewBook(Livre livre) {
        try {
            PreparedStatement statement = DatabaseHandler.getInstance().getConnection().prepareStatement(
                    "INSERT INTO LIVRES(id,title,author,publisher,isAvail) VALUES(?,?,?,?,?)");
            statement.setString(1, String.valueOf(livre.getCode()));
            statement.setString(2, livre.getTitre());
            statement.setString(3, livre.getAuteur());
            statement.setString(4, String.valueOf(livre.getISBN()));
         //   statement.setBoolean(5, livre.getAvailability());
            return statement.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.ERROR, "{}", ex);
        }
        return false;
    }

//    public static boolean insertNewMember(Member member) {
//        try {
//            PreparedStatement statement = DatabaseHandler.getInstance().getConnection().prepareStatement(
//                    "INSERT INTO MEMBER(id,name,mobile,email) VALUES(?,?,?,?)");
//            statement.setString(1, member.getId());
//            statement.setString(2, member.getName());
//            statement.setString(3, member.getMobile());
//            statement.setString(4, member.getEmail());
//            return statement.executeUpdate() > 0;
//        } catch (SQLException ex) {
//            LOGGER.log(Level.ERROR, "{}", ex);
//        }
//        return false;
//    }



//    public static boolean isMemberExists(String id) {
//        try {
//            String checkstmt = "SELECT COUNT(*) FROM MEMBER WHERE id=?";
//            PreparedStatement stmt = DatabaseHandler.getInstance().getConnection().prepareStatement(checkstmt);
//            stmt.setString(1, id);
//            ResultSet rs = stmt.executeQuery();
//            if (rs.next()) {
//                int count = rs.getInt(1);
//                System.out.println(count);
//                return (count > 0);
//            }
//        } catch (SQLException ex) {
//            LOGGER.log(Level.ERROR, "{}", ex);
//        }
//        return false;
//    }

    public static ResultSet getBookInfoWithIssueData(String id) {
        try {
            String query = "SELECT livre.title, livre.author, livre.isAvail, ISSUE.issueTime FROM LIVRES LEFT JOIN ISSUE on livre.id = ISSUE.livreID where livre.id = ?";
            PreparedStatement stmt = DatabaseHandler.getInstance().getConnection().prepareStatement(query);
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs;
        } catch (SQLException ex) {
            LOGGER.log(Level.ERROR, "{}", ex);
        }
        return null;
    }

    public static void wipeTable(String tableName) {
        try {
            Statement statement = DatabaseHandler.getInstance().getConnection().createStatement();
            statement.execute("DELETE FROM " + tableName + " WHERE TRUE");
        } catch (SQLException ex) {
            LOGGER.log(Level.ERROR, "{}", ex);
        }
    }

    public static boolean updateMailServerInfo(MailServerInfo mailServerInfo) {
        try {
            wipeTable("MAIL_SERVER_INFO");
            PreparedStatement statement = DatabaseHandler.getInstance().getConnection().prepareStatement(
                    "INSERT INTO MAIL_SERVER_INFO(server_name,server_port,user_email,user_password,ssl_enabled) VALUES(?,?,?,?,?)");
            statement.setString(1, mailServerInfo.getMailServer());
            statement.setInt(2, mailServerInfo.getPort());
            statement.setString(3, mailServerInfo.getEmailID());
            statement.setString(4, mailServerInfo.getPassword());
            statement.setBoolean(5, mailServerInfo.getSslEnabled());
            return statement.executeUpdate() > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.ERROR, "{}", ex);
        }
        return false;
    }

    public static MailServerInfo loadMailServerInfo() {
        try {
            String checkstmt = "SELECT * FROM MAIL_SERVER_INFO";
            PreparedStatement stmt = DatabaseHandler.getInstance().getConnection().prepareStatement(checkstmt);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String mailServer = rs.getString("server_name");
                Integer port = rs.getInt("server_port");
                String emailID = rs.getString("user_email");
                String userPassword = rs.getString("user_password");
                Boolean sslEnabled = rs.getBoolean("ssl_enabled");
                return new MailServerInfo(mailServer, port, emailID, userPassword, sslEnabled);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.ERROR, "{}", ex);
        }
        return null;
    }
}
