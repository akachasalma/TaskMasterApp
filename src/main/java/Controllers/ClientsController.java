 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controllers;
import app.mastertask.application.App;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import models.*;
import models.DB_connexion;

/**
 * FXML Controller class
 *
 * @author akach
 */
public class ClientsController implements Initializable {

    @FXML
    private AnchorPane intclient;
    @FXML
    private Button accessClient;
    @FXML
    private Button project1;
    @FXML
    private Button employee1;
    @FXML
    private Button team;
    @FXML
    private ImageView x;
    @FXML
    private Button addClient;
    @FXML
    private TextField EmailClient;
    @FXML
    private TextField PhoneClient;
    @FXML
    private TextField nameClient;
    @FXML
    private TextField idClient;
    @FXML
    private TextField LastNameClient;
    @FXML
private TableView<Client> clientTableView;
@FXML
private TableColumn<Client, String> idClientColumn;
@FXML
private TableColumn<Client, String> nameColumn;
@FXML
private TableColumn<Client, String> lastNameColumn;
@FXML
private TableColumn<Client, String> emailColumn;
@FXML
private TableColumn<Client, String> telColumn;
   
    @FXML
    private TableColumn<Client, String> entrepriseColumn;
    @FXML
    private TextField entreprise;
    @FXML
    private Button feedback;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    idClientColumn.setCellValueFactory(new PropertyValueFactory<>("idpers"));
    nameColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
    lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
    entrepriseColumn.setCellValueFactory(new PropertyValueFactory<>("NomEntreprise"));
    emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
    telColumn.setCellValueFactory(new PropertyValueFactory<>("tel"));


    // Call the method to update the TableView
    updateClientTableView();
     clientTableView.setOnMouseClicked(this::handleTableClick);
}



 private void updateClientTableView() {
    try {
        Connection con = DB_connexion.con();
        Statement st = con.createStatement();
        // Select all employees from the EMPLOYEE table
        ResultSet rs = st.executeQuery("SELECT * FROM CLIENT");

        // Clear the old employees from the TableView
        clientTableView.getItems().clear();

        // Add the new employees to the TableView
        while (rs.next()) {
            // Assuming Employee class has a constructor with the required fields
            Client equipe = new Client(
                    rs.getInt("idpers"),
                    rs.getString("Nom"),
                    rs.getString("Prenom"),
                    rs.getString("Email"),
                    rs.getInt("Tel"),
                    rs.getString("Entreprise")
                  
    
            );

            clientTableView.getItems().add(equipe);
        }

        rs.close();
        System.out.println("Client TableView updated");

    } catch (Exception e) {
        e.printStackTrace();
    }
}
    

   

    @FXML
    private void accessClient(ActionEvent event) {
          try{        App.setRoot("clients");

               } catch (IOException ex) {
            Logger.getLogger(ClientsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void project(ActionEvent event) {
         try{        App.setRoot("project");

               } catch (IOException ex) {
            Logger.getLogger(ClientsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void employee(ActionEvent event) {
           try{        App.setRoot("employee");

               } catch (IOException ex) {
            Logger.getLogger(ClientsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void teams(ActionEvent event) {
           try{        App.setRoot("team");

               } catch (IOException ex) {
            Logger.getLogger(ClientsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void quit(MouseEvent event) {
    }

    @FXML
    private void Dashboard(ActionEvent event) {
           try{        App.setRoot("Dashboard");

               } catch (IOException ex) {
            Logger.getLogger(ClientsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

  

  @FXML
private void addButton(ActionEvent event) {
    try {
        Connection con = DB_connexion.con();
        Statement st = con.createStatement();

        boolean success = st.execute("INSERT INTO CLIENT(idpers, Nom, Prenom, Email, Tel, Entreprise) VALUES('"
                + idClient.getText() + "','" + nameClient.getText() + "','" + LastNameClient.getText() + "','"
                + EmailClient.getText() + "','" + PhoneClient.getText() + "','" + entreprise.getText() + "')");

        if (!success) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Message");
            alert.setHeaderText("Client Inserted Successfully");
            alert.showAndWait();

            // Assuming idClient is an int, parse it accordingly
            int clientId = Integer.parseInt(idClient.getText());

            // Assuming PhoneClient is an int, parse it accordingly
            int clientPhone = Integer.parseInt(PhoneClient.getText());

            // Now, add the new client to the TableView
            Client newClient = new Client(
                    clientId,
                    nameClient.getText(),
                    LastNameClient.getText(),
                    EmailClient.getText(),
                    clientPhone,
                    entreprise.getText()
            );
            clientTableView.getItems().add(newClient);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Error");
            alert.setContentText("An error occurred while inserting the client. Please try again.");
            alert.showAndWait();
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}

   @FXML
private void handleTableClick(MouseEvent event) {
    Client selectedClient = clientTableView.getSelectionModel().getSelectedItem();
    if (selectedClient != null) {
        // Assuming idEmployee is a TextField, set its text accordingly
        idClient.setText(Integer.toString(selectedClient.getIdpers()));
        nameClient.setText(selectedClient.getNom());
        LastNameClient.setText(selectedClient.getPrenom());
        EmailClient.setText(selectedClient.getEmail());
        PhoneClient.setText(Integer.toString(selectedClient.getTel()));
        entreprise.setText(selectedClient.getNomEntreprise());
        
    }
}

    @FXML
    private void deleteButton(ActionEvent event) {
        Client selectedClient = clientTableView.getSelectionModel().getSelectedItem();
        if (selectedClient != null) {
            deleteClient(selectedClient);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("No Client Selected");
            alert.setContentText("Please select a client before clicking the Delete button.");
            alert.showAndWait();
        }
    }

    private void deleteClient(Client client) {
        try {
            Connection con = DB_connexion.con();
            Statement st = con.createStatement();
            st.executeUpdate("DELETE FROM CLIENT WHERE idpers = '" + client.getIdpers() + "'");
            updateClientTableView();
            // You can add a dialog or notification to inform the user that the client has been deleted successfully.
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void updateButton(ActionEvent event) {
         Client selectedClient = clientTableView.getSelectionModel().getSelectedItem();
    if (selectedClient != null) { 
        // Get updated data from text fields
        String newName = nameClient.getText();
        String newLastName = LastNameClient.getText();
        String newEmail = EmailClient.getText();
        String newTel = PhoneClient.getText();
        String newEntreprise = entreprise.getText();
        

        // Call the method to update the employee in the database
        updateClientInDatabase(
                selectedClient.getIdpers(),
                newName,
                newLastName,
                newEmail,
                newTel,
                newEntreprise
        );
    } else {
        // Show a message to the user to select an employee before clicking the Update button.
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("No Employee Selected");
        alert.setContentText("Please select an employee before clicking the Update button.");
        alert.showAndWait();
    }
    }

    
    
    
    // Sample method for updating an employee in the database
private void updateClientInDatabase(int id, String newName, String newLastName, String newEmail, String newTel, String newEntreprise) {
    try {
        Connection con = DB_connexion.con();
        Statement st = con.createStatement();

        // Use SQL UPDATE query to update the employee in the database
        String updateQuery = "UPDATE CLIENT SET Nom='" + newName + "', Prenom='" + newLastName +
                "', Email='" + newEmail + "', Tel='" + newTel + "', Entreprise='" + newEntreprise +
                "' WHERE idpers='" + id + "'";
        st.executeUpdate(updateQuery);

        // Call a method to update the TableView with the latest data
        updateClientTableView();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    @FXML
    private void feedback(ActionEvent event) {
         try{        App.setRoot("feedback");

               } catch (IOException ex) {
            Logger.getLogger(ClientsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    } 

   
 
