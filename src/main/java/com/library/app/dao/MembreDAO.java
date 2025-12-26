package com.library.app.dao;
import com.library.model.Membre;
import java.util.List;

public class MembreDAO extends dao<Membre>  
{Membre findByEmail(String email) throws SQLException;
    List<Membre> findActifs() throws SQLException;
    List<Membre> findByNom() throws SQLException;
    void desactiverMembre(int id) throws SQLException;
    void activerMembre(int id) throws SQLException;

    
}
