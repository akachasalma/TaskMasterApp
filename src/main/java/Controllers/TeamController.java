/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package Controllers;

import app.mastertask.application.App;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
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
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.Client;
import models.DB_connexion;
import models.Employee;
import models.Equipe;

/**
 * FXML Controller class
 *
 * @author akach
 */
public class TeamController implements Initializable {

    @FXML
    private Button project;
    @FXML
    private Button team;
    @FXML
    private Button addClient;
    @FXML
    private TextField nameTeam;
    @FXML
    private TextField idTeam;
    @FXML
    private TableView<Equipe> teamTableView;
    @FXML
    private TableColumn<Equipe, String> idTeamColumn;
    @FXML
    private TableColumn<Equipe, String>  nameColumn;
    @FXML
    private TableColumn<Equipe, Void>  consultColumn;
    @FXML
    private AnchorPane intteam;
    @FXML
    private Button accessClient;
    @FXML
    private Button accessemployee;
    @FXML
    private ImageView x;
    @FXML
    private Button dashboard;
    @FXML
    private TableColumn<Equipe, Void>  membersColumn;
    @FXML
    private TextField searchEMPteam;

 private FilteredList<Equipe> filteredTeams;
    @FXML
    private TableColumn<Equipe, Void>  mothermember;
    @FXML
    private TableColumn<Equipe, Void> membersdelete;
    @FXML
    private Button feedback;
    @Override
public void initialize(URL url, ResourceBundle rb) {
    idTeamColumn.setCellValueFactory(new PropertyValueFactory<>("IDEquipe"));
    nameColumn.setCellValueFactory(new PropertyValueFactory<>("Nom"));

    // Set up the teamTableView to display the columns
       // teamTableView.getColumns().clear();
       // teamTableView.getColumns().addAll(idTeamColumn, nameColumn, createMembersButtonColumn(), createConsultButtonColumn());

       // Set up the teamTableView to display the columns
        teamTableView.getColumns().clear();
        teamTableView.getColumns().addAll(idTeamColumn, nameColumn , createMotherMemberColumn());

       
       
       
        // Call the method to update the TableView
        updateTeamTableView();
        // Créez la liste filtrée une seule fois lors de l'initialisation
        List<Equipe> allTeams = new ArrayList<>(teamTableView.getItems());
        filteredTeams = new FilteredList<>(FXCollections.observableArrayList(allTeams));
    }


    @FXML
    private void project(ActionEvent event) {
         try{        App.setRoot("project");

               } catch (IOException ex) {
            Logger.getLogger(TeamController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void team(ActionEvent event) {
    }

    
    
    //========================================================================================//
    //----------------------------------  Partie CRUD   ---------------------------------//
    //========================================================================================//

    @FXML
    private void addButton(ActionEvent event) {
         try {
            Connection con = DB_connexion.con();
            Statement st = con.createStatement();

            boolean success = st.execute("INSERT INTO EQUIPE(equipe_id, nomEquipe) VALUES('"
                    + idTeam.getText() + "','" + nameTeam.getText() + "')");

            if (!success) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Message");
                alert.setHeaderText("Team Inserted Successfully");
                alert.showAndWait();

                // Assuming idTeam is a String, parse it accordingly
               
                int IDTeam = Integer.parseInt(idTeam.getText());

                // Now, add the new team to the TableView
                Equipe newTeam = new Equipe(
                        IDTeam,
                        nameTeam.getText()
                );
                teamTableView.getItems().add(newTeam);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Error");
                alert.setContentText("An error occurred while inserting the team. Please try again.");
                alert.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
         
         
         
    }
       
       
       
        private void updateTeamTableView() {
    try {
        Connection con = DB_connexion.con();
        Statement st = con.createStatement();
        // Select all employees from the EMPLOYEE table
        ResultSet rs = st.executeQuery("SELECT * FROM EQUIPE");

        // Clear the old employees from the TableView
        teamTableView.getItems().clear();

        // Add the new employees to the TableView
        while (rs.next()) {
            // Assuming Employee class has a constructor with the required fields
            Equipe equipe = new Equipe(
                    rs.getInt("equipe_id"),
                    rs.getString("nomEquipe")
   
            );

            teamTableView.getItems().add(equipe);
        }

        rs.close();
        System.out.println("Equipe TableView updated");

    } catch (Exception e) {
        e.printStackTrace();
    }
}
   

 
 
       
       
        @FXML
private void deleteButton(ActionEvent event) {
    Equipe selectedEquipe = teamTableView.getSelectionModel().getSelectedItem();
    if (selectedEquipe != null) {
        // Supprimer les enregistrements dans la table EmployeeEquipe liés à cette équipe
        deleteEmployeeEquipeRecords(selectedEquipe.getIDEquipe());
       
        // Supprimer l'équipe de la table Equipe
        deleteEquipe(selectedEquipe);
    } else {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("No Team Selected");
        alert.setContentText("Please select a team before clicking the Delete button.");
        alert.showAndWait();
    }
}

private void deleteEmployeeEquipeRecords(int equipeId) {
    try {
        Connection con = DB_connexion.con();
        // Supprimer les enregistrements dans la table EmployeeEquipe liés à cette équipe
        PreparedStatement deleteStatement = con.prepareStatement("DELETE FROM EmployeeEquipe WHERE equipe_id = ?");
        deleteStatement.setInt(1, equipeId);
        deleteStatement.executeUpdate();
        deleteStatement.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

private void deleteEquipe(Equipe equipe) {
    try {
        Connection con = DB_connexion.con();
        Statement st = con.createStatement();
        // Supprimer l'équipe de la table Equipe
        st.executeUpdate("DELETE FROM EQUIPE WHERE equipe_id = '" + equipe.getIDEquipe() + "'");
        updateTeamTableView();
        // Vous pouvez ajouter une boîte de dialogue ou une notification pour informer l'utilisateur que l'équipe a été supprimée avec succès.
    } catch (Exception e) {
        e.printStackTrace();
    }
}
       
       

    @FXML
    private void handleTableClick(MouseEvent event) {
    }
   

   
   
private void openAddMemberDialog(Equipe equipe) {
    try {
        // Utilisez un TextInputDialog pour saisir le nom et le prénom de l'employé
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Employee to Team");
        dialog.setHeaderText("Enter Employee Details");
        dialog.setContentText("Name and LastName (separated by a space):");

        // Obtenez le résultat du dialogue (nom et prénom de l'employé)
        Optional<String> result = dialog.showAndWait();

        // Si l'utilisateur a saisi quelque chose, procédez
        result.ifPresent(input -> {
            // Divisez la saisie en nom et prénom
            String[] parts = input.trim().split("\\s+");

            // Vérifiez si la saisie est valide (contient deux parties)
            if (parts.length == 2) {
                String nom = parts[0];
                String prenom = parts[1];

                // Vérifiez si l'employé existe déjà dans la base de données (table EMPLOYEE)
                Employee existingEmployee = getEmployeeFromDatabase(nom, prenom);

                // Vérifiez si l'employé existe déjà dans l'équipe en utilisant la table EmployeeEquipe
                if (existingEmployee != null) {
                    // Vérifiez si l'employé est déjà dans l'équipe actuelle
                    if (isEmployeeInEquipe(existingEmployee.getIdpers(), equipe.getIDEquipe())) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Employee Already in Team");
                        alert.setContentText("The selected employee is already in the team.");
                        alert.showAndWait();
                    } else if (isEmployeeInAnyEquipe(existingEmployee.getIdpers())) {
                        // L'employé est affecté à une autre équipe
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Employee in Another Team");
                        alert.setContentText("The selected employee is already assigned to another team.");
                        alert.showAndWait();
                    } else {
                        // L'employé n'existe pas dans l'équipe, ajoutez-le et mettez à jour la base de données
                        equipe.getMembres().add(existingEmployee);
                        updateEmployeeEquipeTable(existingEmployee.getIdpers(), equipe.getIDEquipe());

                        // Mettez à jour la TableView des équipes
                        updateTeamTableView();

                        // Affichez une confirmation à l'utilisateur
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setTitle("Success");
                        successAlert.setHeaderText("Employee Added to Team");
                        successAlert.setContentText("The selected employee has been added to the team.");
                        successAlert.showAndWait();
                    }
                } else {
                    // L'employé n'existe pas dans la base de données
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Employee Not Found");
                    alert.setContentText("The selected employee does not exist in the database.");
                    alert.showAndWait();
                }
            } else {
                // La saisie ne contient pas deux parties (nom et prénom), affichez un message d'erreur
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Input");
                alert.setContentText("Please enter both name and lastname separated by a space.");
                alert.showAndWait();
            }
        });
    } catch (Exception e) {
        e.printStackTrace();
    }
}

private boolean isEmployeeInAnyEquipe(int employeeId) {
    try {
        boolean isEmployeeInAnyEquipe;
        try (Connection con = DB_connexion.con(); PreparedStatement checkStatement = con.prepareStatement("SELECT * FROM EmployeeEquipe WHERE idpers = ?")) {
            checkStatement.setInt(1, employeeId);
            try (ResultSet rs = checkStatement.executeQuery()) {
                isEmployeeInAnyEquipe = rs.next();
                // Fermez les ressources
            }
        }

        return isEmployeeInAnyEquipe;
    } catch (SQLException e) {
        return false;
    }
}

private boolean isEmployeeInEquipe(int employeeId, int equipeId) {
    try {
        boolean isEmployeeInEquipe;
        try (Connection con = DB_connexion.con(); PreparedStatement checkStatement = con.prepareStatement("SELECT * FROM EmployeeEquipe WHERE idpers = ? AND equipe_id = ?")) {
            checkStatement.setInt(1, employeeId);
            checkStatement.setInt(2, equipeId);
            try (ResultSet rs = checkStatement.executeQuery()) {
                isEmployeeInEquipe = rs.next();
                // Fermez les ressources
            }
        }

        return isEmployeeInEquipe;
    } catch (SQLException e) {
        return false;
    }
}



//***************************************************autre methode
    private Employee getEmployeeFromDatabase(String nom, String prenom) {
        try {
            Connection con = DB_connexion.con();
            // Utilisez une requête préparée pour éviter les attaques SQL par injection
            String selectQuery = "SELECT * FROM Employee WHERE Nom=? AND Prenom=?";
            try (PreparedStatement preparedStatement = con.prepareStatement(selectQuery)) {
                preparedStatement.setString(1, nom);
                preparedStatement.setString(2, prenom);

                ResultSet rs = preparedStatement.executeQuery();

                // Vérifiez si l'employé a été trouvé
                if (rs.next()) {
                    // Construisez et retournez un objet Employee
                    return new Employee(
                            rs.getInt("idpers"),
                            rs.getString("Nom"),
                            rs.getString("Prenom"),
                            rs.getString("Email"),
                            rs.getInt("Tel"),
                            rs.getString("Poste"),
                            rs.getInt("Score")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Retournez null si l'employé n'est pas trouvé
        return null;
    }

    public static void updateEmployeeEquipeTable(int employeeId, int equipeId) {
        try {
            Connection con = DB_connexion.con(); // Utilisez votre propre méthode pour obtenir la connexion
            PreparedStatement checkStatement = con.prepareStatement("SELECT * FROM EmployeeEquipe WHERE idpers = ? AND equipe_id = ?");
            checkStatement.setInt(1, employeeId);
            checkStatement.setInt(2, equipeId);
            ResultSet rs = checkStatement.executeQuery();

            if (!rs.next()) {
                // Si l'association n'existe pas, insérez-la
                PreparedStatement insertStatement = con.prepareStatement("INSERT INTO EmployeeEquipe (idpers, equipe_id) VALUES (?, ?)");
                insertStatement.setInt(1, employeeId);
                insertStatement.setInt(2, equipeId);
                insertStatement.executeUpdate();
                insertStatement.close();
            }

            // Fermez les ressources
            rs.close();
            checkStatement.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
   
 
    private TableColumn<Equipe, Void> createMotherMemberColumn() {
        TableColumn<Equipe, Void> column = new TableColumn<>("Members");

        TableColumn<Equipe, Void> addButtonColumn = new TableColumn<>("Add Member");
        addButtonColumn.setCellFactory(param -> new TableCell<>() {
            private final Button addButton = new Button("ADD");

            {
                addButton.setOnAction(event -> {
                    Equipe equipe = getTableView().getItems().get(getIndex());
                    openAddMemberDialog(equipe);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : addButton);
            }
        });

        TableColumn<Equipe, Void> consultButtonColumn = new TableColumn<>("Consult");
        consultButtonColumn.setCellFactory(param -> new TableCell<>() {
            private final Button consultButton = new Button("Consult");

            {
                consultButton.setOnAction(event -> {
                    Equipe equipe = getTableView().getItems().get(getIndex());
                    openConsultEmployeesDialog(equipe);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : consultButton);
            }
        });
       
        TableColumn<Equipe, Void> deleteButtonColumn = new TableColumn<>("Delete Member");
    deleteButtonColumn.setCellFactory(param -> new TableCell<>() {
        private final Button deleteButton = new Button("Delete");

        {
            deleteButton.setOnAction(event -> {
                Equipe equipe = getTableView().getItems().get(getIndex());
                openDeleteMemberDialog(equipe);
            });
        }

        @Override
        protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            setGraphic(empty ? null : deleteButton);
        }
    });

        column.getColumns().addAll(addButtonColumn, consultButtonColumn,deleteButtonColumn );

        return column;
    }


   
   

private void openConsultEmployeesDialog(Equipe equipe) {
    try {
        Connection con = DB_connexion.con();
        // Utilisez une requête préparée pour récupérer les employés de l'équipe depuis la table EmployeeEquipe
        String selectQuery = "SELECT e.idpers, e.nom, e.prenom FROM Employee e JOIN EmployeeEquipe ee ON e.idpers = ee.idpers WHERE ee.equipe_id = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(selectQuery)) {
            preparedStatement.setInt(1, equipe.getIDEquipe());

            ResultSet rs = preparedStatement.executeQuery();

            // Construisez la liste des employés de l'équipe
            StringBuilder employeesList = new StringBuilder("Employees in Team:\n");
            while (rs.next()) {
                employeesList.append(String.format("- ID  : %d  , -Name : %s ,  -LastName : %s\n", rs.getInt("idpers"), rs.getString("nom"), rs.getString("prenom")));
            }

            // Affichez la liste dans une boîte de dialogue
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Team Employees");
            alert.setHeaderText(null);
            alert.setContentText(employeesList.toString());
            alert.showAndWait();
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}

 


   
   
   
   
   
    //-----------------------------------------partie delete
   
   
   
   
   
    private void openDeleteMemberDialog(Equipe equipe) {
    try {
        // Utilisez un TextInputDialog pour saisir le nom et le prénom de l'employé à supprimer
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Delete Member from Team");
        dialog.setHeaderText("Enter Employee Details to Delete");
        dialog.setContentText("Name and LastName (separated by a space):");

        // Obtenez le résultat du dialogue (nom et prénom de l'employé)
        Optional<String> result = dialog.showAndWait();

        // Si l'utilisateur a saisi quelque chose, procédez
        result.ifPresent(input -> {
            // Divisez la saisie en nom et prénom
            String[] parts = input.trim().split("\\s+");

            // Vérifiez si la saisie est valide (contient deux parties)
            if (parts.length == 2) {
                String nom = parts[0];
                String prenom = parts[1];

                // Vérifiez si l'employé existe dans l'équipe
                Employee existingEmployee = getEmployeeFromEquipe(equipe, nom, prenom);

                if (existingEmployee != null) {
                    // L'employé existe dans l'équipe, supprimez-le et mettez à jour la base de données
                    equipe.getMembres().remove(existingEmployee);
                    deleteEmployeeFromEquipeTable(existingEmployee.getIdpers(), equipe.getIDEquipe());

                    // Mettez à jour la TableView des équipes
                    updateTeamTableView();

                    // Affichez une confirmation à l'utilisateur
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Success");
                    successAlert.setHeaderText("Employee Removed from Team");
                    successAlert.setContentText("The selected employee has been removed from the team.");
                    successAlert.showAndWait();
                } else {
                    // L'employé n'existe pas dans l'équipe
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Employee Not Found in Team");
                    alert.setContentText("The selected employee is not a member of the team.");
                    alert.showAndWait();
                }
            } else {
                // La saisie ne contient pas deux parties (nom et prénom), affichez un message d'erreur
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Input");
                alert.setContentText("Please enter both name and lastname separated by a space.");
                alert.showAndWait();
            }
        });
    } catch (Exception e) {
        e.printStackTrace();
    }
}

// Méthode pour supprimer un employé de la table EmployeeEquipe
private void deleteEmployeeFromEquipeTable(int employeeId, int equipeId) {
    try {
        Connection con = DB_connexion.con();
        PreparedStatement deleteStatement = con.prepareStatement("DELETE FROM EmployeeEquipe WHERE idpers = ? AND equipe_id = ?");
        deleteStatement.setInt(1, employeeId);
        deleteStatement.setInt(2, equipeId);
        deleteStatement.executeUpdate();
        deleteStatement.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

//========================================================================================//
    //----------------------------------  Partie Stream   ---------------------------------//
    //========================================================================================//


// Méthode pour obtenir un employé spécifique d'une équipe
private Employee getEmployeeFromEquipe(Equipe equipe, String nom, String prenom) {
    return equipe.getMembres().stream()
            .filter(employee -> employee.getNom().equalsIgnoreCase(nom) && employee.getPrenom().equalsIgnoreCase(prenom))
            .findFirst()
            .orElse(null);
}
    
    @FXML
    private void handleSearchEMPteam(KeyEvent event) {

  String searchTerm = searchEMPteam.getText().trim().toLowerCase();

    // Appliquez le prédicat de filtre en fonction du texte de recherche
    filteredTeams.setPredicate(equipe -> {
        // Implémentez ici votre logique de filtre
        // Vous pouvez personnaliser cela en fonction de vos besoins

        // Vérifiez si le nom de l'équipe contient le terme de recherche
        boolean teamNameMatch = equipe.getNom().toLowerCase().contains(searchTerm);

        // Vérifiez si le prénom ou le nom de chaque membre de l'équipe contient le terme de recherche
        boolean employeeMatch = equipe.getMembres().stream()
                .anyMatch(employee -> employee.getNom().toLowerCase().contains(searchTerm)
                        || employee.getPrenom().toLowerCase().contains(searchTerm));

        // Retournez true si le terme de recherche correspond à l'équipe ou à l'un de ses membres
        return teamNameMatch || employeeMatch;
    });

    // Mettez à jour le TableView avec les équipes filtrées
    teamTableView.setItems(FXCollections.observableArrayList(filteredTeams));
}


//========================================================================================//
    //----------------------------------  Partie interfaces   ---------------------------------//
    //========================================================================================//

    @FXML
    private void feedback(ActionEvent event) {
         try{        App.setRoot("feedback");

               } catch (IOException ex) {
            Logger.getLogger(TeamController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

       @FXML
    private void accessClient(ActionEvent event) {
          try{        App.setRoot("clients");

               } catch (IOException ex) {
            Logger.getLogger(TeamController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void accessemployee(ActionEvent event) {
         try{        App.setRoot("employee");

               } catch (IOException ex) {
            Logger.getLogger(TeamController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void quit(MouseEvent event) {
    }

    @FXML
    private void dashboard(ActionEvent event) {
         try{        App.setRoot("Dashboard");

               } catch (IOException ex) {
            Logger.getLogger(TeamController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    }
