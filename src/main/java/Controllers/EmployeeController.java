/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controllers;

import app.mastertask.application.App;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
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
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.Client;
import models.DB_connexion;
import models.Employee;

/**
 * FXML Controller class
 *
 * @author akach
 */
public class EmployeeController implements Initializable {

  
  @FXML
    private TableView<Employee> employeeTableView;
    @FXML
    private TableColumn<Employee, String> idEmployeeColumn;
@FXML
private TableColumn<Employee, String> nameColumn;
    @FXML
    private TableColumn<Employee, String> lastNameColumn;
    @FXML
    private TableColumn<Employee, String> emailColumn;
    @FXML
    private TableColumn<Employee, String> telColumn;
    @FXML
    private TableColumn<Employee, String> posteColumn;
    @FXML
    private TableColumn<Employee, String> scoreColumn;
   
    @FXML
    private Button addClient;
 
    @FXML
    private TextField emailEmployee;
    @FXML
    private TextField telEmployee;
    @FXML
    private TextField nameEmployee;
    @FXML
    private TextField idEmployee;
    @FXML
    private TextField lastNameEmployee;
    @FXML
    private TextField posteEmployee;
    @FXML
    private TextField scoreEmployee;
    @FXML
    private TableColumn<?, ?> consultColumn;
    @FXML
    private AnchorPane intemployee;
    @FXML
    private Button accessClient;
    @FXML
    private Button project1;
    @FXML
    private Button employee1;
    @FXML
    private Button team1;
    @FXML
    private ImageView x;
    @FXML
    private Button feedback;
 

    /**
     * Initializes the controller class.
     */
  
    
    
        @Override
    public void initialize(URL url, ResourceBundle rb) {
    idEmployeeColumn.setCellValueFactory(new PropertyValueFactory<>("idpers"));
    nameColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
    lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
    emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
    telColumn.setCellValueFactory(new PropertyValueFactory<>("tel"));
    posteColumn.setCellValueFactory(new PropertyValueFactory<>("poste"));
    scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));   
    updateEmployeeTableView();
    
    } 
    
    //========================================================================================//
    //-------------------------------------  Partie DB   -------------------------------------//
    //========================================================================================//
    
    
    
       // Sample method for deleting an employee
    private void deleteEmployee(Employee employee) {
        try {
            Connection con = DB_connexion.con();
            Statement st = con.createStatement();
            st.executeUpdate("DELETE FROM EMPLOYEE WHERE idpers = '" + employee.getIdpers() + "'");
            updateEmployeeTableView();
            // You can add a dialog or notification to inform the user that the employee has been deleted successfully.
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
//----------------------------------------------------------------------------------------------------------------------------------//     
    
    // Sample method for updating an employee in the database
    private void updateEmployeeInDatabase(int id, String newName, String newLastName, String newEmail, String newTel, String newPoste, String newScore) {
        try {
            Connection con = DB_connexion.con();
            Statement st = con.createStatement();

        // Use SQL UPDATE query to update the employee in the database
            String updateQuery = "UPDATE EMPLOYEE SET Nom='" + newName + "', Prenom='" + newLastName +
                "', Email='" + newEmail + "', Tel='" + newTel + "', Poste='" + newPoste + "', Score='" + newScore +
                "' WHERE idpers='" + id + "'";
            st.executeUpdate(updateQuery);

        // Call a method to update the TableView with the latest data
            updateEmployeeTableView();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

 
    //========================================================================================//
    //----------------------------------  Partie TABLEVIEW   ---------------------------------//
    //========================================================================================//
    
    private void updateEmployeeTableView() {
    try {
        Connection con = DB_connexion.con();
        Statement st = con.createStatement();
        // Select all employees from the EMPLOYEE table
        ResultSet rs = st.executeQuery("SELECT * FROM EMPLOYEE");

        // Clear the old employees from the TableView
        employeeTableView.getItems().clear();

        // Add the new employees to the TableView
        while (rs.next()) {
            // Assuming Employee class has a constructor with the required fields
            Employee employee = new Employee(
                    rs.getInt("idpers"),
                    rs.getString("Nom"),
                    rs.getString("Prenom"),
                    rs.getString("Email"),
                    rs.getInt("Tel"),
                    rs.getString("Poste"),
                    rs.getInt("Score")
            );

            employeeTableView.getItems().add(employee);
        }

        rs.close();
        System.out.println("Employee TableView updated");

    } catch (Exception e) {
        e.printStackTrace();
    }
}
    
//----------------------------------------------------------------------------------------------------------------------------------//     

 @FXML
private void handleTableClick(MouseEvent event) {
    Employee selectedEmployee = employeeTableView.getSelectionModel().getSelectedItem();
    if (selectedEmployee != null) {
        // Assuming idEmployee is a TextField, set its text accordingly
        idEmployee.setText(Integer.toString(selectedEmployee.getIdpers()));
        nameEmployee.setText(selectedEmployee.getNom());
        lastNameEmployee.setText(selectedEmployee.getPrenom());
        emailEmployee.setText(selectedEmployee.getEmail());
        telEmployee.setText(Integer.toString(selectedEmployee.getTel()));
        posteEmployee.setText(selectedEmployee.getPoste());
        scoreEmployee.setText(Integer.toString(selectedEmployee.getScore()));
    }
}
//========================================================================================//
//-----------------------------------  buttons CRUD  -------------------------------------//
//========================================================================================//
 
    // Sample method for handling the delete button click
    @FXML
    private void deleteButtonClicked(ActionEvent event) {
        Employee selectedEmployee = employeeTableView.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            deleteEmployee(selectedEmployee);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("No Employee Selected");
            alert.setContentText("Please select an employee before clicking the Delete button.");
            alert.showAndWait();
        }
    }
    
 //-----------------------------------------------------------------------------------------------------------------------------//    
     // Sample method for handling the update button click
    @FXML
    private void updateButtonClicked(ActionEvent event) {
    Employee selectedEmployee = employeeTableView.getSelectionModel().getSelectedItem();
    if (selectedEmployee != null) { 
        // Get updated data from text fields
        String newName = nameEmployee.getText();
        String newLastName = lastNameEmployee.getText();
        String newEmail = emailEmployee.getText();
        String newTel = telEmployee.getText();
        String newScore = scoreEmployee.getText();
        String newPoste = posteEmployee.getText();

        // Call the method to update the employee in the database
        updateEmployeeInDatabase(
                selectedEmployee.getIdpers(),
                newName,
                newLastName,
                newEmail,
                newTel,
                newPoste,
                newScore
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
    @FXML
private void addButton(ActionEvent event) {
    try {
        Connection con = DB_connexion.con();
        Statement st = con.createStatement();

        boolean success = st.execute("INSERT INTO EMPLOYEE(idpers, Nom, Prenom, Email, Tel, Poste, Score) VALUES('"
                + idEmployee.getText() + "','" + nameEmployee.getText() + "','" + lastNameEmployee.getText() + "','"
                + emailEmployee.getText() + "','" + telEmployee.getText() + "','" + posteEmployee.getText() + "','"
                + scoreEmployee.getText() + "')");

        if (!success) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Message");
            alert.setHeaderText("Employee Inserted Successfully");
            alert.showAndWait();

                  // Assuming idEmployee is an int, parse it accordingly
        int employeeId = Integer.parseInt(idEmployee.getText());

        // Assuming telEmployee is an int, parse it accordingly
        int employeeTel = Integer.parseInt(telEmployee.getText());

        // Assuming scoreEmployee is an int, parse it accordingly
        int employeeScore = Integer.parseInt(scoreEmployee.getText());

        // Now, add the new employee to the TableView
        Employee newEmployee = new Employee(
                employeeId,
                nameEmployee.getText(),
                lastNameEmployee.getText(),
                emailEmployee.getText(),
                employeeTel,
                posteEmployee.getText(),
                employeeScore
        );
            employeeTableView.getItems().add(newEmployee);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Error");
            alert.setContentText("An error occurred while inserting the employee. Please try again.");
            alert.showAndWait();
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}
//========================================================================================//
//--------------------------------  buttons barre de menu    ------------------------------//
//========================================================================================//
   
    
    @FXML
     private void accessClient(ActionEvent event) {
         try{        App.setRoot("clients");

               } catch (IOException ex) {
            Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
//-------------------------------------------------------------------------------------------------------//     

    @FXML
    private void project(ActionEvent event) {
            try{        App.setRoot("project");

               } catch (IOException ex) {
            Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
//-------------------------------------------------------------------------------------------------------// 
    @FXML
    private void employee(ActionEvent event) {
            try{        App.setRoot("employee");

               } catch (IOException ex) {
            Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
//---------------------------------------------------------------------------------------------------------// 
    @FXML
    private void teams(ActionEvent event) {
            try{        App.setRoot("team");

               } catch (IOException ex) {
            Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//--------------------------------------------------------------------------------------------------------// 
    @FXML
    private void Dashboard(ActionEvent event) {
            try{        App.setRoot("Dashboard");

               } catch (IOException ex) {
            Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
//---------------------------------------------------------------------------------------------------------// 
   
//----------------------------------------------------------------------------------------------------------------// 

    @FXML
    private void quit(MouseEvent event) {
    }

    @FXML
    private void feedback(ActionEvent event) {
         try{        App.setRoot("feedback");

               } catch (IOException ex) {
            Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
  


}

   
  

   