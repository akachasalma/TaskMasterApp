/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.util.stream.Stream;

/**
 *
 * @author 21620
 */
public final class Employee extends Personne{
   private String poste;
    private int score=0;

   public Employee(int idpers, String nom, String prenom, String email, int tel, String poste, int score) {
        super(idpers, nom, prenom, email, tel);
        this.poste = poste;
        this.score = score;
    }

 
    @Override
    public String toString() {
        return "Employee{id=" + getIdpers() + ", name='" + getNom() + '\'' + '}';
    }


     public String getPoste() {
        return poste;
    }

    public void setPoste(String poste) {
        this.poste = poste;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }
    
    //========================================================================================//
    //----------------------------------  Partie Stream  ---------------------------------//
    //========================================================================================//
    
       @Override
    public int hashCode() {
        return Integer.hashCode(getIdpers());
    }

    public boolean contientEmployee(Employee employee) {
        return Stream.of(employee).anyMatch(e -> e.equals(this));
        
    }
        @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Employee employee = (Employee) obj;
        return employee.getIdpers() == getIdpers();
    }

}
