/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

import app.mastertask.application.App;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.DB_connexion;
import models.*;
/**
 * FXML Controller class
 *
 * @author akach
 */
public class FeedbackController implements Initializable {

    @FXML
    private AnchorPane intfeed;
    @FXML
    private Button addClient;
    @FXML
    private TextField rating;
    @FXML
    private TextField idfeed;
    @FXML
    private TableView<Feedback> FEEDBACKtable;
    @FXML
    private TableColumn<Feedback, String> ID;
    @FXML
    private TableColumn<Feedback, String> description;
    @FXML
    private Button project;
    @FXML
    private Button team;
    @FXML
    private TableColumn<Feedback, String>ratingcol;
    @FXML
    private TextArea descrarea;
    @FXML
    private Button feedback;
    @FXML
    private ImageView x;
    @FXML
    private Button accessClient;
    @FXML
    private Button accessemployee;
    @FXML
    private Button Dashboard;

     @Override
    public void initialize(URL url, ResourceBundle rb) {
        ID.setCellValueFactory(new PropertyValueFactory<>("idfeed"));
        description.setCellValueFactory(new PropertyValueFactory<>("content"));
        ratingcol.setCellValueFactory(new PropertyValueFactory<>("rating"));
        updatefeedTableView();
    
    } 
    
    //========================================================================================//
    //----------------------------------  Partie CRUD   ---------------------------------//
    //========================================================================================//
      

    @FXML
    private void addButton(ActionEvent event) {
        try {
        Connection con = DB_connexion.con();
        String query = "INSERT INTO Feedback(idfeedback,rating, comment) VALUES (?, ?, ?)";
        PreparedStatement st = con.prepareStatement(query);
        
        // Récupérer les valeurs des champs
        int id = Integer.parseInt(idfeed.getText());
        st.setInt(1, id);
        st.setInt(2, Integer.parseInt(rating.getText()));
        st.setString(3, descrarea.getText());
    

        // Exécuter la requête SQL pour insérer un nouveau feedback
        int rowsAffected = st.executeUpdate();

        if (rowsAffected > 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Message");
            alert.setHeaderText("Feedback Inserted Successfully");
            alert.showAndWait();

            Feedback f = new Feedback(descrarea.getText(), Integer.parseInt(rating.getText()), id);
            FEEDBACKtable.getItems().add(f);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Error");
            alert.setContentText("An error occurred while inserting the feedback. Please try again.");
            alert.showAndWait();
        }
    } catch (NumberFormatException e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Invalid Input");
        alert.setContentText("Please enter valid numbers for ID and Rating.");
        alert.showAndWait();
    } catch (SQLException e) {
        e.printStackTrace();
        // Gérer les autres exceptions SQL ici
    }
}
    

// Méthode pour mettre à jour la table des feedbacks
 private void updatefeedTableView() {
     try {
        Connection con = DB_connexion.con();
        Statement st = con.createStatement();
        // Select all employees from the EMPLOYEE table
        ResultSet rs = st.executeQuery("SELECT * FROM FEEDBACK");
            // Clear the old feedback from the ObservableList
           FEEDBACKtable.getItems().clear();

            while (rs.next()) {
                Feedback f = new Feedback(
    rs.getString("comment"),  // Utilisation de nom de colonne correct pour le champ de commentaire
    rs.getInt("rating"),
    rs.getInt("idfeedback")
                );

                FEEDBACKtable.getItems().add(f); // Add the new feedback to the ObservableList
            }

            rs.close();
            System.out.println("Feedback TableView updated");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteButtonClicked(ActionEvent event) {
         // Récupération du feedback sélectionné dans la TableView
    Feedback selectedFeedback = FEEDBACKtable.getSelectionModel().getSelectedItem();

    if (selectedFeedback == null) {
        // Aucun feedback sélectionné, afficher un avertissement
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("No Feedback Selected");
        alert.setContentText("Please select a feedback to delete.");
        alert.showAndWait();
        return;
    }

    try {
        Connection con = DB_connexion.con();
        String query = "DELETE FROM Feedback WHERE idfeedback = ?";
        PreparedStatement st = con.prepareStatement(query);
        
        // Paramètre pour la clause WHERE (idfeedback du feedback sélectionné)
        st.setInt(1, selectedFeedback.getIdfeed());

        // Exécuter la requête SQL pour supprimer le feedback
        int rowsAffected = st.executeUpdate();

        if (rowsAffected > 0) {
            // Suppression réussie, retirer le feedback de la TableView
            FEEDBACKtable.getItems().remove(selectedFeedback);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Message");
            alert.setHeaderText("Feedback Deleted Successfully");
            alert.showAndWait();
        } else {
            // Avertissement en cas d'échec de la suppression
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Error");
            alert.setContentText("An error occurred while deleting the feedback. Please try again.");
            alert.showAndWait();
        }
    } catch (SQLException e) {
        e.printStackTrace();
        // Gérer les autres exceptions SQL ici
    }
    }

    @FXML
    private void handleTableClick(MouseEvent event) {
    }

   //========================================================================================//
    //----------------------------------  Partie interfaces   ---------------------------------//
    //========================================================================================//
    @FXML
    private void project(ActionEvent event) {
          try{        App.setRoot("project");

               } catch (IOException ex) {
            Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void team(ActionEvent event) {
          try{        App.setRoot("team");

               } catch (IOException ex) {
            Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void Dashboard(ActionEvent event) {
        try{        App.setRoot("Dashboard");

               } catch (IOException ex) {
            Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void feedback(ActionEvent event) {
        try{        App.setRoot("feedback");

               } catch (IOException ex) {
            Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void quit(MouseEvent event) {
        try{        App.setRoot("login");

               } catch (IOException ex) {
            Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void accessClient(ActionEvent event) {
        try{        App.setRoot("clients");

               } catch (IOException ex) {
            Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void accessemployee(ActionEvent event) {
        try{        App.setRoot("employee");

               } catch (IOException ex) {
            Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    
    
    
    
    
}
    
    
