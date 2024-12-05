/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author 21620
 */
public class Equipe implements interfaceFonctionnelle{
 private int IDEquipe;
    private String Nom;
    private Set<Employee> membres;

    public Equipe(int IDEquipe, String Nom) {
        this.IDEquipe = IDEquipe;
        this.Nom = Nom;
        this.membres = new HashSet<>(); 
        retrieveFromDatabase();
    }

    public int getIDEquipe() {
        return IDEquipe;
    }

    public String getNom() {
        return Nom;
    }

    public Set<Employee> getMembres() {
        return membres;
    }

    public void setIDEquipe(int IDEquipe) {
        this.IDEquipe = IDEquipe;
    }

    public void setNom(String Nom) {
        this.Nom = Nom;
    }

    public void setMembres(Set<Employee> membres) {
        this.membres = membres;
    }

    
    public void addItem(Employee item) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void removeItem(Employee item) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public Collection<Employee> getCollection() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
 
    
    //========================================================================================//
    //----------------------------------  Methode interface fonctionnelle   ----------------------------//
    //========================================================================================//
    
    
 @Override
    public void retrieveFromDatabase() {
        try {
            Connection con = DB_connexion.con();
            String query = "SELECT e.* FROM Employee e " +
                           "JOIN EmployeeEquipe ee ON e.idpers = ee.idpers " +
                           "WHERE ee.equipe_id = ?";
            try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
                preparedStatement.setInt(1, this.IDEquipe);
                ResultSet rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    Employee employee = new Employee(
                        rs.getInt("idpers"),
                        rs.getString("Nom"),
                        rs.getString("Prenom"),
                        rs.getString("Email"),
                        rs.getInt("Tel"),
                        rs.getString("Poste"),
                        rs.getInt("Score")
                    );
                    this.membres.add(employee);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ... autres méthodes de la classe ...

}