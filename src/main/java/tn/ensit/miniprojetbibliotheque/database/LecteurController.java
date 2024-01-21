package tn.ensit.miniprojetbibliotheque.database;
//A.2.1 les methodes Ajouter, supprimer et A.2.2 rechercher les lecteurs  + methode edition lecteur

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tn.ensit.miniprojetbibliotheque.models.Lecteur;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LecteurController {

    private final static Logger LOGGER = LogManager.getLogger(DatabaseHandler.class.getName());
    private Connection connection;

    public LecteurController() {
        connection = DatabaseHandler.getInstance().getConnection();
    }



    public List<Lecteur> getAllReaders() {
        List<Lecteur> readers = new ArrayList<>();
        try {
            String query = "SELECT * FROM lecteur";
            try (ResultSet resultSet = connection.prepareStatement(query).executeQuery()) {
                while (resultSet.next()) {
                    long cin = resultSet.getLong("CIN");
                    String nom = resultSet.getString("nom");
                    String prenom = resultSet.getString("prenom");
                    Lecteur lecteur = new Lecteur(cin, nom, prenom);
                    readers.add(lecteur);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "{}", e);
        }

        return readers;
    }


    public boolean ajouterLecteur(Lecteur lecteur) {
        try {
            String query = "INSERT INTO lecteur (CIN, nom, prenom) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setLong(1, lecteur.getCIN());
                preparedStatement.setString(2, lecteur.getNom());
                preparedStatement.setString(3, lecteur.getPrenom());

                int result = preparedStatement.executeUpdate();
                return result == 1;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "{}", e);
        }
        return false;
    }
    public boolean supprimerLecteur(long CIN) {
        try {
            String query = "DELETE FROM lecteur WHERE CIN = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setLong(1, CIN);

                int result = preparedStatement.executeUpdate();
                return result == 1;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "{}", e);
        }
        return false;
    }

    public List<Lecteur> rechercher(String searchTerm) {
        List<Lecteur> result = new ArrayList<>();
        try {
            String query = "SELECT * FROM lecteur WHERE nom LIKE ? OR prenom LIKE ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, "%" + searchTerm + "%");
                preparedStatement.setString(2, "%" + searchTerm + "%");
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        Lecteur lecteur = new Lecteur();
                        lecteur.setCIN(resultSet.getLong("CIN"));
                        lecteur.setNom(resultSet.getString("nom"));
                        lecteur.setPrenom(resultSet.getString("prenom"));
                        result.add(lecteur);
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "{}", e);
        }
        return result;
    }


    public boolean editionLecteur(long CIN, String nom, String prenom) {
        try {
            String update = "UPDATE lecteur SET nom=?, prenom=? WHERE CIN=?";
            PreparedStatement stmt = connection.prepareStatement(update);
            stmt.setString(1, nom);
            stmt.setString(2, prenom);
            stmt.setLong(3, CIN);
            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException ex) {
            LOGGER.log(Level.ERROR, "{}", ex);
        }
        return false;
    }



    public static boolean isLecteurExists(long CIN) {
        try {
            String checkstmt = "SELECT COUNT(*) FROM lecteur WHERE CIN=?";
            PreparedStatement stmt = DatabaseHandler.getInstance().getConnection().prepareStatement(checkstmt);
            stmt.setLong(1, CIN);
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
