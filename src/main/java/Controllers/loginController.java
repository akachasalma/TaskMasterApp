/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
 */
package Controllers;

import app.mastertask.application.App;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.*;
import static models.SoundPlayer.playSound;


/**
 *
 * @author akach
 */
public class loginController implements Initializable {
    
private SendSimpleEmail mail = new SendSimpleEmail();    
    
    private Label label;
    @FXML
    private Button intTOTLogin;
  
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private AnchorPane forgotpassword;
    
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText("Hello World!");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    private void signup(ActionEvent event) throws IOException {
      //====================================METHODE MR IYED ==========================/  
  intTOTLogin.getScene().getWindow().hide();
          try{
              Stage st = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/views/signup.fxml"));
        
        Scene scene = new Scene(root);
        
        st.setScene(scene);
         st.setTitle("WELCOME TO MASTERTASK APPLICATION");
        st.show();
               } catch (IOException ex) {
            Logger.getLogger(loginController.class.getName()).log(Level.SEVERE, null, ex);
        }
  
      
    }



 @FXML
    private void LOGIN(ActionEvent event)throws IOException, SQLException {
       
     try (Connection connection = DB_connexion.con()) {
             String sql = "SELECT * FROM admin_table WHERE username = ? AND password = ?"; 
            
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                // el ? loula 3awathha bel username.gettext (el champ ely id mtaa o username 
                statement.setString(1, username.getText());
                statement.setString(2, password.getText());
                //resultSet variable hethy stockina fyha les donnees ly dakhalnehom mel sql query ly lawjet l admin w l mot de passe 
                try (ResultSet resultSet = statement.executeQuery()) {
                    //hethy tshoufli l var fyheshy au moins row donc les donnees shah
                    if (resultSet.next()) {
                        // Successful login
                        App.setRoot("Dashboard");
                        playSound("C:\\Users\\21620\\Documents\\NetBeansProjects\\MasterTask-APPLICATION\\src\\main\\resources\\pics\\Hello.mp3");
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Login or PASSWORD ERROR");
            alert.setContentText("Please try again");
            alert.showAndWait();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showLoginError() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("Login or PASSWORD ERROR");
        alert.setContentText("Please try again");
        alert.showAndWait();
    }
     

    public Admin getAdminInfo() {
        String sql = "SELECT * FROM admin_table WHERE username = ? ";
        Admin admin = null;

        try (Connection connection = DB_connexion.con();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username.getText());
           

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // Construire un objet Admin à partir des données récupérées de la base de données
                    int idpers = resultSet.getInt("idpers");
                    String nom = resultSet.getString("nom");
                    String prenom = resultSet.getString("prenom");
                    String email = resultSet.getString("email");
                    int tel = resultSet.getInt("tel");
                    String password = resultSet.getNString("password");
                    

                    // Ajoutez d'autres colonnes au besoin

                    // Utilisez le constructeur de la classe Admin pour créer un objet Admin
                  admin = new Admin(idpers, nom, prenom, email, tel, username.getText(), password);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return admin;
    }
    

    
     public void updateAdminEmail( String newpassword) {
        String sql = "UPDATE admin_table SET password = ? WHERE username = ?";

        try (Connection connection = DB_connexion.con();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, newpassword);
            statement.setString(2, username.getText());
            
            // Exécutez la mise à jour
            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("La mise à jour a été effectuée avec succès !");
            } else {
                System.out.println("Aucune mise à jour effectuée. L'ID de l'administrateur n'a pas été trouvé.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
   
     private String generatePasswordFromAdminInfo(String adminNom , int adminTel) {
    // Convertissez le numéro de téléphone en chaîne de caractères et concaténez-le avec le nom de l'admin
    return adminNom + String.valueOf(adminTel);
   

}

@FXML
    private void forgotpassword(MouseEvent event) {
         // Générez un nouveau mot de passe basé sur le nom et le numéro de téléphone de l'admin
        String newPassword = generatePasswordFromAdminInfo(getAdminInfo().getNom(), getAdminInfo().getTel());
        updateAdminEmail(newPassword);
        String recipientEmail = getAdminInfo().getEmail(); // Replace with the recipient's email address
        String subject = "NEW PASSWORD";
        String messageText = "Your new passwors is " + newPassword;
        mail.sendEmail(recipientEmail, subject, messageText);
        showpasssent();
    }
private void showpasssent() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("INFO");
        alert.setHeaderText("PASSWORD sent to your email");
        alert.setContentText("Please check it out");
        alert.showAndWait();
    }
    
}
    
    
    
    
    
