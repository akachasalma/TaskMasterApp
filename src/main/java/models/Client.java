/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author 21620
 */
public final class Client extends Personne{
           
    String NomEntreprise;
    //ListeProj:ArrayList<Projet>

    public Client( int idpers, String nom, String prenom, String email, int tel, String NomEntreprise) {
        super(idpers, nom, prenom, email, tel);
        this.NomEntreprise = NomEntreprise;
    }

    public String getNomEntreprise() {
        return NomEntreprise;
    }

    public void setNomEntreprise(String NomEntreprise) {
        this.NomEntreprise = NomEntreprise;
    }
}