package Controllers;

import app.mastertask.application.App;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.DB_connexion;
import models.Employee;
import models.Equipe;
import models.Projet;


/**
 * FXML Controller class
 *
 * @author akach
 */
public class ProjectController implements Initializable {
 
    @FXML
    private ImageView x;
    @FXML
    private Button accessClient;
    @FXML
    private TableView<Projet> projectTableView;
    @FXML
    private TableColumn<Projet, String> idcolumn;
    @FXML
    private TableColumn<Projet, String> namecolumn;
    @FXML
    private TableColumn<Projet, String> startcolumn;
    @FXML
    private TableColumn<Projet, String> finishcolumn;
    @FXML
    private TableColumn<Projet, String> pricecolumn;
   
    private TableColumn<?, ?> taskcolumn;
    private TableColumn<Projet, Void>feedbackcolumn;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button addButton;
    @FXML
    private TextField idprojet;
    @FXML
    private TextField nomprojet;
    @FXML
    private TextField prixprojet;
    @FXML
    private DatePicker debutprojet;
    @FXML
    private DatePicker finprojet;
    @FXML
    private Button project;
    @FXML
    private Button accessemployee;
    @FXML
    private Button team;
    @FXML
    private Button dashboard;
private Projet selectedProjet;
    @FXML
    private AnchorPane intproject;
    @FXML
    private Button end;
    @FXML
    private Button feedback;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
     // Set up the TableView columns
    idcolumn.setCellValueFactory(new PropertyValueFactory<>("idproj"));
    namecolumn.setCellValueFactory(new PropertyValueFactory<>("nomProjet"));
    startcolumn.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
    finishcolumn.setCellValueFactory(new PropertyValueFactory<>("dateFinPrevu"));
    pricecolumn.setCellValueFactory(new PropertyValueFactory<>("prix"));
   
    // Ajoute la nouvelle TableColumn pour ajouter des membres à une équipe
    projectTableView.getColumns().addAll(motherteamColumn(), feedbackColumn());

    // Appelle la méthode pour mettre à jour la projectTableView
    updateProjectTableView();
    }
    
    
    private void updateProjectTableView() {
    try {
        Connection con = DB_connexion.con();
        Statement st = con.createStatement();
        // Select all projects from the PROJET table
        ResultSet rs = st.executeQuery("SELECT * FROM PROJET");

        // Clear the old projects from the TableView
        projectTableView.getItems().clear();

        // Add the new projects to the TableView
        while (rs.next()) {
            // Assuming Project class has a constructor with the required fields
            Projet project = new Projet(
                    rs.getInt("idproj"),
                    rs.getString("nomProjet"),
                    rs.getDate("dateDebut"),
                    rs.getDate("dateFinPrevu"),
                    rs.getDouble("prix")
                    
            );

            projectTableView.getItems().add(project);       
        }

        rs.close();
        System.out.println("Project TableView updated");

    } catch (Exception e) {
        e.printStackTrace();
    }
}

 
    
private void handleTableClick(MouseEvent event) {
    Projet selectedProjet = projectTableView.getSelectionModel().getSelectedItem();
    if (selectedProjet != null) {
        // Assuming idprojet is a TextField, set its text accordingly
        idprojet.setText(Integer.toString(selectedProjet.getIdproj()));
        nomprojet.setText(selectedProjet.getNomProjet());

       // Assuming debutprojet and finprojet are DatePicker, set their values accordingly
        debutprojet.setValue(dateToLocalDate((Date) selectedProjet.getDateDebut()));
        finprojet.setValue(dateToLocalDate((Date) selectedProjet.getDateFinPrevu()));

    }
}
// Helper method to convert java.util.Date to java.time.LocalDate
private LocalDate dateToLocalDate(Date date) {
    if (date != null) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    } else {
        return null;
    }
}

   //==========================================================================================
/*private TableColumn<Projet, Void> motherteamColumn() {
    TableColumn<Projet, Void> column = new TableColumn<>("Teams");

    column.setCellFactory(param -> new TableCell<>() {
        private final Button addButton = new Button("+");
        private final Button dellButton = new Button("-");

        {
            addButton.setOnAction(event -> {
                Projet projet = getTableView().getItems().get(getIndex());
                openAddMemberDialog(projet);
            });

            dellButton.setOnAction(event -> {
                Projet projet = getTableView().getItems().get(getIndex());
                deleteTeamFromProject(projet);
            });
        }

        @Override
        protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
            } else {
                HBox buttons = new HBox(addButton, dellButton);
                setGraphic(buttons);
            }
        }
    });

    return column;
}*/
private TableColumn<Projet, Void> motherteamColumn() {
    TableColumn<Projet, Void> column = new TableColumn<>("Teams");

    column.setCellFactory(param -> new TableCell<>() {
        private final Button addButton = new Button("+");
        private final Button dellButton = new Button("-");
        private final Button viewButton = new Button("=");

        {
            addButton.setOnAction(event -> {
                Projet projet = getTableView().getItems().get(getIndex());
                openAddMemberDialog(projet);
            });

            dellButton.setOnAction(event -> {
                Projet projet = getTableView().getItems().get(getIndex());
                deleteTeamFromProject(projet);
            });

            viewButton.setOnAction(event -> {
                Projet projet = getTableView().getItems().get(getIndex());
                viewTeamDetails(projet);
            });
        }

        @Override
        protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
            } else {
                HBox buttons = new HBox(addButton, dellButton, viewButton);
                setGraphic(buttons);
            }
        }
    });

    return column;
}

private void viewTeamDetails(Projet projet) {
    try {
        Connection con = DB_connexion.con();
        String query = "SELECT e.equipe_id, e.nomEquipe " +
                       "FROM Equipe e " +
                       "JOIN ProjetEquipe pe ON e.equipe_id = pe.equipe_id " +
                       "WHERE pe.idproj = ?";

        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setInt(1, projet.getIdproj());
            ResultSet resultSet = preparedStatement.executeQuery();

            StringBuilder teamDetails = new StringBuilder();

            while (resultSet.next()) {
                int equipeId = resultSet.getInt("equipe_id");
                String nomEquipe = resultSet.getString("nomEquipe");

                teamDetails.append("Equipe ID: ").append(equipeId).append("\n");
                teamDetails.append("Nom d'équipe: ").append(nomEquipe).append("\n\n");
            }

            if (teamDetails.length() > 0) {
                // Affiche les données d'équipe dans une boîte d'information
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Team Information");
                alert.setHeaderText("Team for Project: " + projet.getNomProjet());
                alert.setContentText(teamDetails.toString());
                alert.showAndWait();
            } else {
                // Aucune équipe associée au projet
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Team Information");
                alert.setHeaderText("No Team for Project: " + projet.getNomProjet());
                alert.showAndWait();
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


 private void openAddMemberDialog(Projet projet) {
    try {
        // Utilize a TextInputDialog to enter the team name
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Team to Project");
        dialog.setHeaderText("Enter Team Name");
        dialog.setContentText("Team Name:");

        // Get the result of the dialog (team name)
        Optional<String> result = dialog.showAndWait();

        // If the user entered something, proceed
        result.ifPresent(teamName -> {
            // Check if the team exists in the database
            Equipe existingTeam = getTeamFromDatabase(teamName);

            // Check if the team is already assigned to this project using the ProjetEquipe table
            if (existingTeam != null) {
                if (isTeamAssignedToProject(existingTeam.getIDEquipe(), projet.getIdproj())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Team Already Assigned");
                    alert.setContentText("The selected team is already assigned to this project.");
                    alert.showAndWait();
                } else {
                    // The team exists, add it and update the ProjetEquipe table
                    assignTeamToProject(existingTeam.getIDEquipe(), projet.getIdproj());
                    // Update the TableView of projects
                    updateProjectTableView();

                    // Show a confirmation to the user
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Success");
                    successAlert.setHeaderText("Team Assigned to Project");
                    successAlert.setContentText("The selected team has been assigned to the project.");
                    successAlert.showAndWait();
                }
            } else {
                // The team does not exist in the database
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Team Not Found");
                alert.setContentText("The selected team does not exist in the database.");
                alert.showAndWait();
            }
        });
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    private boolean isTeamAssignedToProject(int teamId, int projectId) {
        // Query the ProjetEquipe table to check if the team is already assigned to the project
        try (Connection con = DB_connexion.con();
             PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM ProjetEquipe WHERE equipe_id = ? AND idproj = ?")) {

            preparedStatement.setInt(1, teamId);
            preparedStatement.setInt(2, projectId);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private void assignTeamToProject(int teamId, int projectId) {
    try {
        Connection con = DB_connexion.con(); // Utilisez votre propre méthode pour obtenir la connexion
        PreparedStatement checkStatement = con.prepareStatement("SELECT * FROM ProjetEquipe WHERE equipe_id = ? AND idproj = ?");
        checkStatement.setInt(1, teamId);
        checkStatement.setInt(2, projectId);
        ResultSet rs = checkStatement.executeQuery();

        if (!rs.next()) {
            // Si l'association n'existe pas, insérez-la
            PreparedStatement insertStatement = con.prepareStatement("INSERT INTO ProjetEquipe (equipe_id, idproj) VALUES (?, ?)");
            insertStatement.setInt(1, teamId);
            insertStatement.setInt(2, projectId);
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
   //================================delete ===============================================
    
    
    private void deleteTeamFromProject(Projet projet) {
    try {
        // Récupérer le nom de l'équipe à partir de l'utilisateur (via un dialogue, etc.)
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Delete Team from Project");
        dialog.setHeaderText("Enter Team Name to Delete from Project");
        dialog.setContentText("Team Name:");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(teamName -> {
            // Vérifier si l'équipe est déjà affectée à ce projet
            Equipe existingTeam = getTeamFromDatabase(teamName);

            if (existingTeam != null) {
                if (isTeamAssignedToProject(existingTeam.getIDEquipe(), projet.getIdproj())) {
                    // L'équipe est affectée au projet, supprimez-la
                    removeTeamFromProject(existingTeam.getIDEquipe(), projet.getIdproj());

                    // Mise à jour de la TableView des projets
                    updateProjectTableView();

                    // Afficher une confirmation à l'utilisateur
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Success");
                    successAlert.setHeaderText("Team Removed from Project");
                    successAlert.setContentText("The selected team has been removed from the project.");
                    successAlert.showAndWait();
                } else {
                    // L'équipe n'est pas affectée au projet
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Team Not Assigned");
                    alert.setContentText("The selected team is not assigned to this project.");
                    alert.showAndWait();
                }
            } else {
                // L'équipe n'existe pas dans la base de données
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Team Not Found");
                alert.setContentText("The selected team does not exist in the database.");
                alert.showAndWait();
            }
        });
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    
    
    
private Equipe getTeamFromDatabase(String teamName) {
   
 try (Connection con = DB_connexion.con();
         PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM Equipe WHERE nomEquipe = ?")) {

        preparedStatement.setString(1, teamName);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            // Log information about the team found
            System.out.println("Team found in database: " + teamName);

            return new Equipe(
                    resultSet.getInt("equipe_id"),
                    resultSet.getString("nomEquipe")
                    // ... (other fields)
            );
        } else {
            // Log information about team not found
            System.out.println("Team not found in database: " + teamName);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return null;
}
private void removeTeamFromProject(int teamId, int projectId) {
    try {
        Connection con = DB_connexion.con();
        PreparedStatement deleteStatement = con.prepareStatement("DELETE FROM ProjetEquipe WHERE equipe_id = ? AND idproj = ?");
        deleteStatement.setInt(1, teamId);
        deleteStatement.setInt(2, projectId);
        deleteStatement.executeUpdate();
        deleteStatement.close();
        con.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
   
}

//================================================partie feedback==================================================

private TableColumn<Projet, Void> feedbackColumn() {
    TableColumn<Projet, Void> column = new TableColumn<>("Feedback");

    column.setCellFactory(param -> new TableCell<>() {
        private final Button addFeedbackButton = new Button("+");
        private final Button deleteFeedbackButton = new Button("-");
       private final Button viewFeedbackButton = new Button("=");

        {
            addFeedbackButton.setOnAction(event -> {
                Projet projet = getTableView().getItems().get(getIndex());
                openAddFeedbackDialog(projet);
            });

            deleteFeedbackButton.setOnAction(event -> {
                Projet projet = getTableView().getItems().get(getIndex());
                deleteFeedbackFromProject(projet);
            });

            viewFeedbackButton.setOnAction(event -> {
                Projet projet = getTableView().getItems().get(getIndex());
                viewFeedback(projet);
            });
        }

        @Override
        protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
            } else {
                // Utilisez un VBox pour aligner verticalement les boutons
                HBox buttons = new HBox(addFeedbackButton, deleteFeedbackButton, viewFeedbackButton);
                setGraphic(buttons);
            }
        }
    });

    return column;
}
private void deleteFeedbackFromProject(Projet projet) {
    try {
        // Assurez-vous de récupérer l'ID du feedback que vous souhaitez supprimer
        int feedbackId = getFeedbackIdFromUser();

        if (isFeedbackAssignedToProject(feedbackId, projet.getIdproj())) {
            // Supprimez le feedback du projet
            removeFeedbackFromProject(feedbackId, projet.getIdproj());

            // Mettez à jour la TableView des projets
            updateProjectTableView();

            // Affiche une confirmation à l'utilisateur
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Success");
            successAlert.setHeaderText("Feedback Removed from Project");
            successAlert.setContentText("The selected feedback has been removed from the project.");
            successAlert.showAndWait();
        } else {
            // Le feedback n'est pas assigné au projet
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Feedback Not Assigned");
            alert.setContentText("The selected feedback is not assigned to this project.");
            alert.showAndWait();
        }
    } catch (NumberFormatException e) {
        // Gérer l'exception si la conversion en int échoue
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Invalid Feedback ID");
        alert.setContentText("Please enter a valid integer for the feedback ID.");
        alert.showAndWait();
    }
}

private int getFeedbackIdFromUser() {
    TextInputDialog dialog = new TextInputDialog();
    dialog.setTitle("Remove Feedback");
    dialog.setHeaderText("Enter Feedback ID to Remove from Project");
    dialog.setContentText("Feedback ID:");

    Optional<String> result = dialog.showAndWait();

    return result.map(Integer::parseInt).orElse(-1);
}

private void removeFeedbackFromProject(int feedbackId, int projectId) {
    try {
        Connection con = DB_connexion.con();
        String query = "DELETE FROM FeedbackProjet WHERE idfeedback = ? AND idproj = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setInt(1, feedbackId);
            preparedStatement.setInt(2, projectId);

            preparedStatement.executeUpdate();
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
private boolean isFeedbackAssignedToProject(int feedbackId, int projectId) {
    try {
        Connection con = DB_connexion.con();
        String query = "SELECT * FROM FeedbackProjet WHERE idfeedback = ? AND idproj = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setInt(1, feedbackId);
            preparedStatement.setInt(2, projectId);

            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next(); // Retourne vrai si le résultat contient une ligne (le feedback est assigné au projet)
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return false; // Retourne faux par défaut (en cas d'erreur ou si le feedback n'est pas assigné au projet)
}
private void openAddFeedbackDialog(Projet projet) {
    try {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Feedback");
        dialog.setHeaderText("Enter Feedback ID");
        dialog.setContentText("Feedback ID:");

        // Afficher la boîte de dialogue et attendre la réponse de l'utilisateur
        dialog.showAndWait().ifPresent(feedbackIdString -> {
            try {
                int feedbackId = Integer.parseInt(feedbackIdString);

                // Vérifiez si le feedback avec l'ID spécifié existe dans la base de données
                if (getFeedbackIdFromDatabase(feedbackId) != -1) {
                    // Le feedback existe, vérifiez s'il est déjà assigné au projet
                    if (!isFeedbackAssignedToProject(feedbackId, projet.getIdproj())) {
                        // Le feedback n'est pas encore assigné au projet, procédez avec l'assignation
                        assignFeedbackToProject(feedbackId, projet.getIdproj());

                        // Mettez à jour la TableView des projets
                        updateProjectTableView();

                        // Affiche une confirmation à l'utilisateur
                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setTitle("Success");
                        successAlert.setHeaderText("Feedback Assigned to Project");
                        successAlert.setContentText("The selected feedback has been assigned to the project.");
                        successAlert.showAndWait();
                    } else {
                        // Le feedback est déjà assigné au projet
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Feedback Already Assigned");
                        alert.setContentText("The selected feedback is already assigned to this project.");
                        alert.showAndWait();
                    }
                } else {
                    // Le feedback avec l'ID spécifié n'existe pas
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setHeaderText("Feedback Not Found");
                    alert.setContentText("The selected feedback ID does not exist in the database.");
                    alert.showAndWait();
                }
            } catch (NumberFormatException e) {
                // Gérer l'exception si la conversion en int échoue
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Feedback ID");
                alert.setContentText("Please enter a valid integer for the feedback ID.");
                alert.showAndWait();
            }
        });
    } catch (Exception e) {
        e.printStackTrace();
    }
}
private int getFeedbackIdFromDatabase(int feedbackId) {
    try {
        Connection con = DB_connexion.con();
        String query = "SELECT idfeedback FROM Feedback WHERE idfeedback = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setInt(1, feedbackId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Le feedback existe, retournez son ID
                return resultSet.getInt("idfeedback");
            } else {
                // Le feedback n'existe pas
                return -1;
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return -1;
}   
private void assignFeedbackToProject(int feedbackId, int projectId) {
    try {
        Connection con = DB_connexion.con();
        String query = "INSERT INTO FeedbackProjet (idfeedback, idproj) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setInt(1, feedbackId);
            preparedStatement.setInt(2, projectId);

            preparedStatement.executeUpdate();
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

private void viewFeedback(Projet projet) {
    try {
        Connection con = DB_connexion.con();
        String query = "SELECT f.idfeedback, f.comment, f.rating " +
                       "FROM Feedback f " +
                       "JOIN FeedbackProjet fp ON f.idfeedback = fp.idfeedback " +
                       "WHERE fp.idproj = ?";

        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setInt(1, projet.getIdproj());
            ResultSet resultSet = preparedStatement.executeQuery();

            StringBuilder feedbackInfo = new StringBuilder();

            while (resultSet.next()) {
                int feedbackId = resultSet.getInt("idfeedback");
                String comment = resultSet.getString("comment");
                int rating = resultSet.getInt("rating");

                feedbackInfo.append("Feedback ID: ").append(feedbackId).append("\n");
                feedbackInfo.append("Comment: ").append(comment).append("\n");
                feedbackInfo.append("Rating: ").append(rating).append("\n\n");
            }

            if (feedbackInfo.length() > 0) {
                // Affiche les données de feedback dans une boîte d'information
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Feedback Information");
                alert.setHeaderText("Feedback for Project: " + projet.getNomProjet());
                alert.setContentText(feedbackInfo.toString());
                alert.showAndWait();
            } else {
                // Aucun feedback n'est associé au projet
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Feedback Information");
                alert.setHeaderText("No Feedback for Project: " + projet.getNomProjet());
                alert.showAndWait();
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
//====================================== end project ==================================================/
private void endProject(Projet projet) {
    try {
        Connection con = DB_connexion.con();
        String updateQuery = "UPDATE Projet SET dateFinReelle = ? WHERE idproj = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(updateQuery)) {
            preparedStatement.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
            preparedStatement.setInt(2, projet.getIdproj());
            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                // Afficher un message pour informer que le projet est terminé
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Project Complete");
                alert.setHeaderText("The project is complete.");
                alert.showAndWait();
                
                // Actualiser la TableView après la mise à jour
               // updateProjetTableView();
            } else {
                // Gérer le cas où la mise à jour n'a pas réussi
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText("Error");
                alert.setContentText("An error occurred while updating the project. Please try again.");
                alert.showAndWait();
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}



    @FXML
    private void end(ActionEvent event) {
        
         Projet selectedProjet = projectTableView.getSelectionModel().getSelectedItem();
    if (selectedProjet != null) {
        endProject(selectedProjet);
    } else {
        // Gérer le cas où aucune ligne n'est sélectionnée
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("No Project Selected");
        alert.setContentText("Please select a project before clicking the 'End Project' button.");
        alert.showAndWait();
    }
}
    
//=====================================crud projet====================================================/
 // Sample method for deleting an employee
    private void deleteProject(Projet projet) {
        try {
            Connection con = DB_connexion.con();
            Statement st = con.createStatement();
            st.executeUpdate("DELETE FROM PROJET WHERE idproj = '" + projet.getIdproj() + "'");
            
            updateProjectTableView();
            // You can add a dialog or notification to inform the user that the employee has been deleted successfully.
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void updateButton(ActionEvent event) {
        Projet selectedProjet = (Projet) projectTableView.getSelectionModel().getSelectedItem();
    if (selectedProjet != null) { 
        // Get updated data from text fields
        String newName = nomprojet.getText();
        LocalDate newStartDate = debutprojet.getValue();
        LocalDate newEndDate = finprojet.getValue();
        double  newPrice = Double.parseDouble(prixprojet.getText());
        

          // Check if dates are not null
        if (newStartDate != null && newEndDate != null) {
            // Convert LocalDate to Date
            Date startDate = java.sql.Date.valueOf(newStartDate);
            Date endDate = java.sql.Date.valueOf(newEndDate);

            // Call the method to update the employee in the database
            updateProjectInDatabase(
                    selectedProjet.getIdproj(),
                    newName,
                    startDate,
                    endDate,
                    newPrice
            );
        } else {
            // Handle the case where dates are not selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Invalid Dates");
            alert.setContentText("Please select start and end dates before clicking the Update button.");
            alert.showAndWait();
        }
    } else {
        // Show a message to the user to select an employee before clicking the Update button.
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("No Employee Selected");
        alert.setContentText("Please select an employee before clicking the Update button.");
        alert.showAndWait();
    }
    }
    
    // Sample method for updating a project in the database
private void updateProjectInDatabase(int id, String newName, Date newStartDate, Date newEndDate, double newPrice) {
    try {
        Connection con = DB_connexion.con();
        Statement st = con.createStatement();

        // Use SQL UPDATE query to update the project in the database
        String updateQuery = "UPDATE Projet SET nomProjet='" + newName + "', dateDebut='" + newStartDate +
                "', dateFinPrevu='" + newEndDate + "', prix='" + newPrice +
                "' WHERE idproj='" + id + "'";
        st.executeUpdate(updateQuery);

        // Call a method to update the TableView with the latest data
        updateProjectTableView();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

    @FXML
    private void deleteButton(ActionEvent event) {
          Projet selectedProjet = (Projet) projectTableView.getSelectionModel().getSelectedItem();
        if (selectedProjet != null) {
            deleteProject(selectedProjet);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("No Employee Selected");
            alert.setContentText("Please select an employee before clicking the Delete button.");
            alert.showAndWait();
        }
    }

   @FXML
private void addButton(ActionEvent event) {
    try {
        Connection con = DB_connexion.con();
        Statement st = con.createStatement();

        // Assuming idprojet is an int, parse it accordingly
        int projectId = Integer.parseInt(idprojet.getText());

        // Assuming prixprojet is a double, parse it accordingly
        double projectPrice = Double.parseDouble(prixprojet.getText());

        // Assuming debutprojet and finprojet are LocalDate, parse them accordingly
        LocalDate startDate = debutprojet.getValue();
        LocalDate endDate = finprojet.getValue();

        // Check if dates are not null
        if (startDate != null && endDate != null) {
            // Convert LocalDate to Date
            java.sql.Date startSqlDate = java.sql.Date.valueOf(startDate);
            java.sql.Date endSqlDate = java.sql.Date.valueOf(endDate);

            // Use a prepared statement to avoid SQL injection
String insertQuery = "INSERT INTO PROJET(idproj, nomProjet, dateDebut, dateFinPrevu, description, prix) VALUES(?, ?, ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = con.prepareStatement(insertQuery)) {
                preparedStatement.setInt(1, projectId);
                preparedStatement.setString(2, nomprojet.getText());
                preparedStatement.setDate(3, startSqlDate);
                preparedStatement.setDate(4, endSqlDate);
                preparedStatement.setString(5, "YourDescriptionHere");  // Replace with actual description
                preparedStatement.setDouble(6, projectPrice);

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows > 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Message");
                    alert.setHeaderText("Project Inserted Successfully");
                    alert.showAndWait();

                    // Now, add the new project to the TableView
                    Projet newProject = new Projet(
                            projectId,
                            nomprojet.getText(),
                            startSqlDate,
                            endSqlDate,
                            projectPrice
                    );
                    projectTableView.getItems().add(newProject);
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setHeaderText("Error");
                    alert.setContentText("An error occurred while inserting the project. Please try again.");
                    alert.showAndWait();
                }
            }
        } else {
            // Handle the case where dates are not selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("Invalid Dates");
            alert.setContentText("Please select start and end dates before clicking the Add button.");
            alert.showAndWait();
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}

//====================================== partie acces sections  ========================================//
    @FXML
    private void accessClient(ActionEvent event) {
          try{        App.setRoot("clients");

               } catch (IOException ex) {
            Logger.getLogger(ProjectController .class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void employee(ActionEvent event) {
         try{        App.setRoot("employee");

               } catch (IOException ex) {
            Logger.getLogger(ProjectController .class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void teams(ActionEvent event) {
          try{        App.setRoot("team");

               } catch (IOException ex) {
            Logger.getLogger(ProjectController .class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void quit(MouseEvent event) {
    }

  
    
    @FXML
    private void project(ActionEvent event) {
        
    }

    @FXML
    private void accessemployee(ActionEvent event) {
         try{        App.setRoot("employee");

               } catch (IOException ex) {
            Logger.getLogger(ProjectController .class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void team(ActionEvent event) {
         try{        App.setRoot("team");

               } catch (IOException ex) {
            Logger.getLogger(ProjectController .class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void dashboard(ActionEvent event) {
               try{        App.setRoot("Dashboard");

               } catch (IOException ex) {
            Logger.getLogger(EmployeeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void feedback(ActionEvent event) {
         try{        App.setRoot("feedback");

               } catch (IOException ex) {
            Logger.getLogger(ProjectController .class.getName()).log(Level.SEVERE, null, ex);
        }
    }

  
}
    