# Library Management System

## Description

Library Management System est une application Java desktop permettant la gestion complète d’une bibliothèque. 

Elle offre des fonctionnalités pour administrer les livres, les membres et les emprunts, avec un suivi des disponibilités et un calcul automatique des pénalités de retard.

L’application est structurée selon une architecture MVC (Model – View – Controller) afin d’assurer une bonne séparation des responsabilités et une maintenance facilitée.

---

## Fonctionnalités

### Gestion des livres
- Ajout, modification et suppression de livres
- Recherche par titre, auteur ou ISBN
- Suivi de la disponibilité des livres
- Affichage du nombre total et des livres disponibles

### Gestion des membres
- Ajout, modification et suppression des membres
- Activation et désactivation des membres
- Recherche par nom, prénom ou email
- Consultation de l’historique des emprunts par membre

### Gestion des emprunts
- Création de nouveaux emprunts
- Définition d’une date de retour prévue
- Retour des livres
- Calcul automatique des pénalités de retard
- Affichage de la liste des emprunts
---
## Architecture du projet
Le projet est organisé comme suit :
```text
.mvn
src
└── main
    ├── java
    │   └── com
    │       └── library
    │           └── app
    │               ├── controller
    │               ├── dao
    │               │   └── impl
    │               ├── exception
    │               ├── model
    │               ├── service
    │               ├── sql
    │               └── util
    └── resources
        └── com
            └── library
                └── app
```

## Technologies utilisées

- Java 
- JavaFX pour l’interface graphique
- JDBC pour l’accès aux données
- MySQL pour la base de données
- Maven pour la gestion des dépendances et du cycle de vie du projet
