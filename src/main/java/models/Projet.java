/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author akach
 */
public class Projet implements interfaceFonctionnelle {
      private String nomProjet;
    private int idproj;
    private java.sql.Date dateDebut;
    private java.sql.Date dateFinPrevu;
    private java.sql.Date dateFinReelle = null;
    private String description;
    private double prix;
    private Feedback avis;
    private List<Equipe> equipes;
    

    public void setDateDebut(java.sql.Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFinPrevu(java.sql.Date dateFinPrevu) {
        this.dateFinPrevu = dateFinPrevu;
    }

    public void setDateFinReelle(java.sql.Date dateFinReelle) {
        this.dateFinReelle = dateFinReelle;
    }

    public void setEquipes(List<Equipe> equipes) {
        this.equipes = equipes;
    }

    
    
    

    
      public Projet(int idproj,String nomProjet,  java.sql.Date dateDebut, java.sql.Date dateFinPrevu,
                  double prix) {
        this.nomProjet = nomProjet;
        this.idproj = idproj;
        this.dateDebut = dateDebut;
        this.dateFinPrevu = dateFinPrevu;
        this.dateFinReelle = null;
        this.description = "";
        this.prix = prix;
        this.avis = null;
        this.equipes = new ArrayList<>();
       
    }
    
    
    public void setNomProjet(String nomProjet) {
        this.nomProjet = nomProjet;
    }

    public void setIdproj(int idproj) {
        this.idproj = idproj;
    }

  

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public void setAvis(Feedback avis) {
        this.avis = avis;
    }



    public String getNomProjet() {
        return nomProjet;
    }

    public int getIdproj() {
        return idproj;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public Date getDateFinPrevu() {
        return dateFinPrevu;
    }

    public Date getDateFinReelle() {
        return dateFinReelle;
    }

    public String getDescription() {
        return description;
    }

    public double getPrix() {
        return prix;
    }

    public Feedback getAvis() {
        return avis;
    }

 
    
    
   

    @Override
    public void retrieveFromDatabase()
    {
        Employee employee = new Employee(1 , "a", "aa", "a", 452, "a",0 );
  try {
        Connection con = DB_connexion.con();
        String query = "SELECT e.* FROM Equipe e " +
                       "JOIN EmployeeEquipe ee ON e.equipe_id = ee.equipe_id " +
                       "WHERE ee.idpers = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setInt(1, employee.getIdpers());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int equipeId = resultSet.getInt("equipe_id");
                String nomEquipe = resultSet.getString("nomEquipe");
                // Autres colonnes de l'équipe...

                // Créer une instance d'Equipe avec les informations récupérées
                Equipe equipe = new Equipe(equipeId, nomEquipe /* autres paramètres */);

                // Ajouter l'équipe à la liste d'équipes de l'employé
               // employee.getEquipes().add(equipe);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    }

}