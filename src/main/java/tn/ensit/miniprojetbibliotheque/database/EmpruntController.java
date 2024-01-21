package tn.ensit.miniprojetbibliotheque.database;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tn.ensit.miniprojetbibliotheque.models.DetailEmprunt;
import tn.ensit.miniprojetbibliotheque.models.Lecteur;
import tn.ensit.miniprojetbibliotheque.models.Livre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EmpruntController {
    private final static Logger LOGGER = LogManager.getLogger(DatabaseHandler.class.getName());
    private Connection connection;

    public EmpruntController() {
        connection = DatabaseHandler.getInstance().getConnection();
    }

    public boolean isLivreDisponible(Livre livre) {
        try {
            String query = "SELECT COUNT(*) FROM detailemprunt WHERE livre_code = ? AND dateRetour IS NULL";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setLong(1, livre.getCode());

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int count = resultSet.getInt(1);
                        return count == 0;
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "{}", e);
        }

        return false;
    }

    public  void emprunterLivre(Lecteur lecteur, Livre livre) {
        try {
            if (isLivreDisponible(livre)) {
                String query = "INSERT INTO detailemprunt (livre_code, lecteur_CIN, dateEmprunt, dateRetour) VALUES (?, ?, ?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setLong(1, livre.getCode());
                    preparedStatement.setLong(2, lecteur.getCIN());
                    preparedStatement.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
                    preparedStatement.setDate(4, null);

                    preparedStatement.executeUpdate();
                }
            } else {
                System.out.println("The book is not available for borrowing.");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "{}", e);
        }
    }

    public void retournerLivre(Lecteur lecteur, Livre livre) {
        try {
            if (!isLivreDisponible(livre)) {
                String query = "UPDATE detailemprunt SET dateRetour = ? WHERE livre_code = ? AND lecteur_CIN = ? AND dateRetour IS NULL";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
                    preparedStatement.setLong(2, livre.getCode());
                    preparedStatement.setLong(3, lecteur.getCIN());

                    preparedStatement.executeUpdate();
                    System.out.println("Book returned successfully.");
                }
            } else {
                System.out.println("You cannot return the book. It is either not borrowed by you or already returned.");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "{}", e);
        }
    }

    public boolean isLivreEmprunteByUser(Livre livre, Lecteur lecteur) {
        boolean isBorrowed = false;

        try {
            String query = "SELECT COUNT(*) AS count FROM detailemprunt WHERE livre_code = ? AND lecteur_CIN = ? AND dateRetour IS NULL";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setLong(1, livre.getCode());
                preparedStatement.setLong(2, lecteur.getCIN());

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int count = resultSet.getInt("count");
                        isBorrowed = count > 0;
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "{}", e);
        }

        return isBorrowed;
    }


    public List<DetailEmprunt> loadEmpruntsData() {
        List<DetailEmprunt> empruntsList = new ArrayList<>();

        try {
            String query = "SELECT detailemprunt.id, detailemprunt.livre_code, detailemprunt.lecteur_CIN, detailemprunt.dateEmprunt, detailemprunt.dateRetour, lecteur.nom, lecteur.prenom, livre.titre " +
                    "FROM detailemprunt " +
                    "JOIN lecteur ON detailemprunt.lecteur_CIN = lecteur.CIN " +
                    "JOIN livre ON detailemprunt.livre_code = livre.code";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query);
                 ResultSet rs = preparedStatement.executeQuery()) {

                while (rs.next()) {
                    long id = rs.getLong("id");
                    long lecteur_CIN = rs.getLong("lecteur_CIN");
                    String lecteurName = rs.getString("nom") + " " + rs.getString("prenom");
                    long bookID = rs.getLong("livre_code");
                    String bookTitle = rs.getString("titre");
                    Date dateemprunte = rs.getDate("dateEmprunt");
                    Date dateretour = rs.getDate("dateRetour");
                    DetailEmprunt detailEmprunt = new DetailEmprunt(id, bookID, bookTitle, lecteur_CIN, lecteurName, dateemprunte, dateretour);
                    empruntsList.add(detailEmprunt);
                }
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.ERROR, "{}", ex);
        }

        return empruntsList;
    }


    public boolean isLivreNotReturned(Livre livre, Lecteur lecteur) {
        try {
            String query = "SELECT COUNT(*) AS count FROM detailemprunt WHERE livre_code = ? AND lecteur_CIN = ? AND dateRetour IS NULL";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setLong(1, livre.getCode());
                preparedStatement.setLong(2, lecteur.getCIN());

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int count = resultSet.getInt("count");
                        return count > 0;
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "{}", e);
        }

        return false;
    }


//    public static void main(String[] args) {
//        // Create an instance of LivreController
//        LivreController livreController = new LivreController();
//
//        // Search books by title
//        List<Livre> booksWithTitle = livreController.rechercher("Java").stream()
//                .filter(book -> book.getTitre().contains("Java"))
//                .collect(Collectors.toList());
//
//        // Search books by author
//        List<Livre> booksWithAuthor = livreController.rechercher("Harper Lee").stream()
//                .filter(book -> book.getAuteur().contains("Harper Lee"))
//                .collect(Collectors.toList());
//
//        // Search books by first few letters of the title
//        List<Livre> booksWithFirstLetters = livreController.rechercher("Harry").stream()
//                .filter(book -> book.getTitre().startsWith("Harry"))
//                .collect(Collectors.toList());
//
//        System.out.println(booksWithTitle);
//        System.out.println(booksWithAuthor);
//        System.out.println(booksWithFirstLetters);
//
//        // Create an instance of EmpruntController
//        EmpruntController empruntController = new EmpruntController();
//
//        // Test emprunterLivre method
//        List<Livre> booksWithFirstLettersToBorrow = livreController.rechercher("Brave").stream()
//                .filter(book -> book.getTitre().startsWith("Brave"))
//                .collect(Collectors.toList());
//
//        Livre livreToReturn = booksWithFirstLettersToBorrow.get(0);
//        Lecteur lecteur = new Lecteur(777777777, "Djebby", "Amine");
//
//        // Test isLivreDisponible before borrowing
//        System.out.println("Is Livre Disponible (Before Borrowing): " + empruntController.isLivreDisponible(livreToReturn));
//
//        // Borrow the book
//        empruntController.retournerLivre(lecteur, livreToReturn);
//
//        // Test isLivreDisponible after borrowing
//        System.out.println("Is Livre Disponible (After Borrowing): " + empruntController.isLivreDisponible(livreToReturn));
//    }


}
