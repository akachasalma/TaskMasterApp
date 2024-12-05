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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.DB_connexion;

/**
 * FXML Controller class
 *
 * @author akach
 */
public class DashboardController implements Initializable {

    @FXML
    private AnchorPane dashboard;
    @FXML
    private ImageView x;
    @FXML
    private Button accessClient;
    @FXML
    private Button team;
    @FXML
    private ImageView test;
    @FXML
    private Button project;
    @FXML
    private Button accessemployee;
    @FXML
    private Label projectCountLabel;
    @FXML
    private Label employeeCountLabel;
    @FXML
    private Label teamCountLabel;
    @FXML
    private Label clientCountLabel;
    @FXML
    private StackedBarChart<String, Number> moneyChart;
    @FXML
    private Label projectCountLabel1;
    @FXML
    private Label projectCountLabel12;
    @FXML
    private Label totalMoneyLabel;
    @FXML
    private Button feedback;
    @FXML
    private ListView<String> completedProjectsListView;
    private ObservableList<String> completedProjectsList = FXCollections.observableArrayList();
    @FXML
    private ListView<String> employeeScoreListView;
private ObservableList<String> employeeScoresList = FXCollections.observableArrayList();

   

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        updateCounts();
        // Set up the completed projects ListView
    completedProjectsListView.setItems(completedProjectsList);
    // Fetch and display completed projects
    updateCompletedProjects();
    
    
     // Set up the employee scores ListView
    employeeScoreListView.setItems(employeeScoresList);
    // Fetch and display employee scores
    updateEmployeeScores();
    }    
    
    
    private void updateEmployeeScores() {
    try {
        Connection con = DB_connexion.con();
        Statement st = con.createStatement();

        // Fetch employee scores based on completed projects
        ResultSet employeeScoresResult = st.executeQuery(
                       "SELECT E.nom, COUNT(P.idproj) * 10 AS score " +
                        "FROM EMPLOYEE E " +
                        "LEFT JOIN EMPLOYEEEQUIPE EE ON E.idpers = EE.idpers " +
                        "LEFT JOIN PROJETEQUIPE PE ON EE.equipe_id = PE.equipe_id " +
                        "LEFT JOIN PROJET P ON PE.idproj = P.idproj " +
                        "WHERE P.dateFinReelle IS NOT NULL " +
                        "GROUP BY E.idpers, E.nom"
        );

        employeeScoresList.clear();

        while (employeeScoresResult.next()) {
            String employeeName = employeeScoresResult.getString("nom");

            int score = employeeScoresResult.getInt("score");
            employeeScoresList.add(employeeName + ": " + score + " points");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    
    
    
    
private void updateCompletedProjects() {
    try {
        Connection con = DB_connexion.con();
        Statement st = con.createStatement();

        // Fetch completed projects
        ResultSet completedProjectsResult = st.executeQuery("SELECT nomProjet FROM PROJET WHERE dateFinReelle IS NOT NULL");

        while (completedProjectsResult.next()) {
            String projectName = completedProjectsResult.getString("nomProjet");
            completedProjectsList.add(projectName);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    
     // Method to fetch counts and update labels
    private void updateCounts() {
        try {
            Connection con = DB_connexion.con();
            Statement st = con.createStatement();

            // Fetch counts from the database
            ResultSet projectCountResult = st.executeQuery("SELECT COUNT(*) FROM PROJET");
            if (projectCountResult.next()) {
                int projectCount = projectCountResult.getInt(1);
                projectCountLabel.setText(" " + projectCount);
            }

            ResultSet employeeCountResult = st.executeQuery("SELECT COUNT(*) FROM EMPLOYEE");
            if (employeeCountResult.next()) {
                int employeeCount = employeeCountResult.getInt(1);
                employeeCountLabel.setText(" " + employeeCount);
            }

            ResultSet teamCountResult = st.executeQuery("SELECT COUNT(*) FROM EQUIPE");
            if (teamCountResult.next()) {
                int teamCount = teamCountResult.getInt(1);
                teamCountLabel.setText(" " + teamCount);
            }
            
             ResultSet clientCountResult = st.executeQuery("SELECT COUNT(*) FROM CLIENT");
        if (clientCountResult.next()) {
            int clientCount = clientCountResult.getInt(1);
            clientCountLabel.setText(" " + clientCount);
        }

        
         ResultSet moneyEvolutionResult = st.executeQuery("SELECT MONTH(dateDebut) AS month, SUM(prix) FROM PROJET GROUP BY month");
XYChart.Series<String, Number> series = new XYChart.Series<>();
while (moneyEvolutionResult.next()) {
    int month = moneyEvolutionResult.getInt("month");
    double money = moneyEvolutionResult.getDouble(2);

    // Convert the numeric month to a string representation if needed
    String monthString = getMonthString(month);


    series.getData().add(new XYChart.Data<>(monthString, money));
}

        moneyChart.getData().clear();
        moneyChart.getData().add(series);
        moneyChart.setTitle("Money Evolution by Month");
        

        ResultSet totalMoneyResult = st.executeQuery("SELECT SUM(prix) FROM PROJET");
         if (totalMoneyResult.next()) {
            double totalMoney = totalMoneyResult.getDouble(1);
            totalMoneyLabel.setText( totalMoney +"$");
        }
        
        
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private String getMonthString(int month) {
    String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    return months[month - 1];
}

    
    @FXML
    private void test(MouseEvent event) {
        System.out.println("testtttttt");
    }

    @FXML
    private void accessClient(ActionEvent event) {
         try{        App.setRoot("clients");

               } catch (IOException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    @FXML
    private void team(ActionEvent event) {
         try{        App.setRoot("team");

               } catch (IOException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void quit(MouseEvent event) {
   dashboard.getScene().getWindow().hide();
         try{
             
         Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/views/login.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);      
         stage.setTitle("WELCOME TO MASTERTASK APPLICATION");
        stage.show();
               } catch (IOException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void project(ActionEvent event) {
      try{        App.setRoot("project");

               } catch (IOException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

  

  

    @FXML
    private void accessemployee(ActionEvent event) throws IOException {
            try{        App.setRoot("employee");

               } catch (IOException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }

          
    }

    @FXML
    private void feedback(ActionEvent event) {
           try{        App.setRoot("feedback");

               } catch (IOException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    

 
}