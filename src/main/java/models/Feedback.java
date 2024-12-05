/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author 21620
 */
public record Feedback(String content, int rating, int idfeed) {
   
    public String getFeedback() {
        return "Rating: " + rating + ", Content: " + content ;
    }

    public String getContent() {
        return content;
    }

    public int getRating() {
        return rating;
    }

    public int getIdfeed() {
        return idfeed;
    }
    

    
}
