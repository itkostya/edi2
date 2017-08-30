package model;

/*
 * Created by kostya on 2/22/2017.
 */
public enum ElementStatus {
    CREATE,  // New element creation
    ERROR,   // Error on page
    STORE,   // Save element
    UPDATE,  // Update parent's page
    CLOSE    // Close current page
}
