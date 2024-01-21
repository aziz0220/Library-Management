package tn.ensit.miniprojetbibliotheque.database;


import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import tn.ensit.miniprojetbibliotheque.models.Livre;

public final class DatabaseHandler {

    private final static Logger LOGGER = LogManager.getLogger(DatabaseHandler.class.getName());

    private static DatabaseHandler handler = null;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/library_management";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    private static Connection conn = null;
    private static Statement stmt = null;

    static {
        createConnection();
    }

    private DatabaseHandler() {
    }

    public static DatabaseHandler getInstance() {
        if (handler == null) {
            handler = new DatabaseHandler();
        }
        return handler;
    }

    private static void createConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Connected to the database.");
        }
        catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Cant load database", "Database Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    public ResultSet execQuery(String query) {
        ResultSet result;
        try {
            stmt = conn.createStatement();
            result = stmt.executeQuery(query);
        }
        catch (SQLException ex) {
            System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
            return null;
        }
        finally {
        }
        return result;
    }

    public boolean execAction(String qu) {
        try {
            stmt = conn.createStatement();
            stmt.execute(qu);
            return true;
        }
        catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error:" + ex.getMessage(), "Error Occured", JOptionPane.ERROR_MESSAGE);
            System.out.println("Exception at execQuery:dataHandler" + ex.getLocalizedMessage());
            return false;
        }
        finally {
        }
    }

    public boolean isBookAlreadyIssued(Livre book) {
        try {
            String checkstmt = "SELECT COUNT(*) FROM detailemprunt WHERE livre_code=?";
            PreparedStatement stmt = conn.prepareStatement(checkstmt);
            stmt.setString(1, String.valueOf(book.getCode()));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                System.out.println(count);
                return (count > 0);
            }
        }
        catch (SQLException ex) {
            LOGGER.log(Level.ERROR, "{}", ex);
        }
        return false;
    }

//    public boolean deleteMember(MemberListController.Member member) {
//        try {
//            String deleteStatement = "DELETE FROM MEMBER WHERE id = ?";
//            PreparedStatement stmt = conn.prepareStatement(deleteStatement);
//            stmt.setString(1, member.getId());
//            int res = stmt.executeUpdate();
//            if (res == 1) {
//                return true;
//            }
//        }
//        catch (SQLException ex) {
//            LOGGER.log(Level.ERROR, "{}", ex);
//        }
//        return false;
//    }

//    public boolean isMemberHasAnyBooks(MemberListController.Member member) {
//        try {
//            String checkstmt = "SELECT COUNT(*) FROM ISSUE WHERE memberID=?";
//            PreparedStatement stmt = conn.prepareStatement(checkstmt);
//            stmt.setString(1, member.getId());
//            ResultSet rs = stmt.executeQuery();
//            if (rs.next()) {
//                int count = rs.getInt(1);
//                System.out.println(count);
//                return (count > 0);
//            }
//        }
//        catch (SQLException ex) {
//            LOGGER.log(Level.ERROR, "{}", ex);
//        }
//        return false;
//    }


//    public boolean updateMember(MemberListController.Member member) {
//        try {
//            String update = "UPDATE MEMBER SET NAME=?, EMAIL=?, MOBILE=? WHERE ID=?";
//            PreparedStatement stmt = conn.prepareStatement(update);
//            stmt.setString(1, member.getName());
//            stmt.setString(2, member.getEmail());
//            stmt.setString(3, member.getMobile());
//            stmt.setString(4, member.getId());
//            int res = stmt.executeUpdate();
//            return (res > 0);
//        }
//        catch (SQLException ex) {
//            LOGGER.log(Level.ERROR, "{}", ex);
//        }
//        return false;
//    }

    public static void main(String[] args) throws Exception {
        DatabaseHandler.getInstance();
    }

    public ObservableList<PieChart.Data> getBookGraphStatistics() {
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        try {
            String qu1 = "SELECT COUNT(*) FROM livre";
            String qu2 = "SELECT COUNT(*) FROM detailemprunt";
            ResultSet rs = execQuery(qu1);
            if (rs.next()) {
                int count = rs.getInt(1);
                data.add(new PieChart.Data("Total Books (" + count + ")", count));
            }
            rs = execQuery(qu2);
            if (rs.next()) {
                int count = rs.getInt(1);
                data.add(new PieChart.Data("Issued Books (" + count + ")", count));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

//    public ObservableList<PieChart.Data> getMemberGraphStatistics() {
//        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
//        try {
//            String qu1 = "SELECT COUNT(*) FROM MEMBER";
//            String qu2 = "SELECT COUNT(DISTINCT memberID) FROM ISSUE";
//            ResultSet rs = execQuery(qu1);
//            if (rs.next()) {
//                int count = rs.getInt(1);
//                data.add(new PieChart.Data("Total Members (" + count + ")", count));
//            }
//            rs = execQuery(qu2);
//            if (rs.next()) {
//                int count = rs.getInt(1);
//                data.add(new PieChart.Data("Active (" + count + ")", count));
//            }
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//        return data;
//    }

    private static void createTables(List<String> tableData) throws SQLException {
        Statement statement = conn.createStatement();
        statement.closeOnCompletion();
        for (String command : tableData) {
            System.out.println(command);
            statement.addBatch(command);
        }
        statement.executeBatch();
    }

    public Connection getConnection() {
        return conn;
    }
}
