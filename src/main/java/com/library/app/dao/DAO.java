package com.library.app.dao;

import java.util.List;


public interface DAO<T>{

    boolean create(T obj);
    boolean update(T obj); // pour updater un objet T
    boolean delete(T obj); // pour supprimer un objet T
    T findById(int id); // pour recupere un objet T par son id 
    List<T> findAll(); //pour récupérer tous les objets de type T
}