/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author akach
 */
public final class Admin extends Personne {
    private String username;
    private String password;
    
    
  //=================================================================================================//
  //************************************* getters ***************************************************//
  //=================================================================================================//

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    
    
   //=================================================================================================//
  //************************************* getters ***************************************************//
  //=================================================================================================//
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
   //=================================================================================================//
  //************************************* constructeur ***************************************************//
  //=================================================================================================//
    public Admin(int idpers,String nom,String prenom, String email, int tel, String username, String password) {
        super(idpers, nom, prenom, email, tel);
        this.username = username;
        this.password = password;
    }
   
    
    
}
