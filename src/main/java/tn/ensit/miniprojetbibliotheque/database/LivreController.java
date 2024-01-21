package tn.ensit.miniprojetbibliotheque.database;
//A.2.1 les methodes Ajouter, supprimer et A.2.2 rechercher les livres  + methode edition livre

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tn.ensit.miniprojetbibliotheque.models.Livre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LivreController {

    private final static Logger LOGGER = LogManager.getLogger(DatabaseHandler.class.getName());

    private Connection connection;

    public LivreController() {
        connection = DatabaseHandler.getInstance().getConnection();
    }



    public List<Livre> getAllBooks() {
        List<Livre> books = new ArrayList<>();
        try {
            String query = "SELECT * FROM livre";
            try (ResultSet resultSet = connection.prepareStatement(query).executeQuery()) {
                while (resultSet.next()) {
                    long code = resultSet.getLong("code");
                    String titre = resultSet.getString("titre");
                    String auteur = resultSet.getString("auteur");
                    long ISBN = resultSet.getLong("ISBN");
                    Livre livre = new Livre(code, titre, auteur, ISBN);
                    books.add(livre);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "{}", e);
        }

        return books;
    }


    public boolean ajouterLivre(Livre livre) {
        try {
            String query = "INSERT INTO livre (titre, auteur, ISBN) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, livre.getTitre());
                preparedStatement.setString(2, livre.getAuteur());
                preparedStatement.setLong(3, livre.getISBN());
                int result = preparedStatement.executeUpdate();
                return result == 1;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "{}", e);
        }
        return false;
    }


    public boolean supprimerLivre(long code) {
        try {
            System.out.println(code);
            String query = "DELETE FROM livre WHERE code = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setLong(1, code);
                int res = preparedStatement.executeUpdate();
                if (res == 1) {
                    return true;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "{}", e);
        }
        return false;
    }


    public List<Livre> rechercher(String searchTerm) {
        List<Livre> result = new ArrayList<>();
        try {
            String query = "SELECT * FROM livre WHERE titre LIKE ? OR auteur LIKE ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, "%" + searchTerm + "%");
                preparedStatement.setString(2, "%" + searchTerm + "%");
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Livre livre = new Livre();
                        livre.setCode(resultSet.getLong("code"));
                        livre.setTitre(resultSet.getString("titre"));
                        livre.setAuteur(resultSet.getString("auteur"));
                        livre.setISBN(resultSet.getLong("ISBN"));
                        result.add(livre);
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "{}", e);
        }
        return result;
    }

    public boolean editionLivre(long code ,String titre, String auteur, long ISBN) {
        try {
            String update = "UPDATE livre SET titre=?, auteur=?, ISBN=? WHERE code=?";
            PreparedStatement stmt = connection.prepareStatement(update);
            stmt.setString(1, titre);
            stmt.setString(2, auteur);
            stmt.setString(3, String.valueOf(ISBN));
            stmt.setString(4, String.valueOf(code));
            int res = stmt.executeUpdate();
            System.out.println(res);
            return (res > 0);
        }
        catch (SQLException ex) {
            LOGGER.log(Level.ERROR, "{}", ex);
        }
        return false;
    }


    public static boolean isBookExists(String id) {
        try {
            String checkstmt = "SELECT COUNT(*) FROM livre WHERE ISBN=?";
            PreparedStatement stmt = DatabaseHandler.getInstance().getConnection().prepareStatement(checkstmt);
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                System.out.println(count);
                return (count > 0);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.ERROR, "{}", ex);
        }
        return false;
    }

}
