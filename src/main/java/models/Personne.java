/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import app.mastertask.application.App;
import java.io.IOException;

/**
 *
 * @author akach
 */
public sealed class Personne permits Admin, Employee, Client {
  
  private int idpers;
  private String nom;
  private String prenom;
  private String email;
  private int tel;

  //=================================================================================================//
  //************************************* getters ***************************************************//
  //=================================================================================================//
    public int getIdpers() {
        return idpers;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getEmail() {
        return email;
    }

    public int getTel() {
        return tel;
    }

    
  //=================================================================================================//
  //************************************* setters ***************************************************//
  //=================================================================================================//    
    public void setIdpers(int idpers) {
        this.idpers = idpers;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTel(int tel) {
        this.tel = tel;
    }
//=================================================================================================//
  //************************************* constructeur ***************************************************//
  //=================================================================================================//
    public Personne(int idpers, String nom, String prenom, String email, int tel) {
        this.idpers = idpers;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.tel = tel;
    }
    
    
   
    
}
