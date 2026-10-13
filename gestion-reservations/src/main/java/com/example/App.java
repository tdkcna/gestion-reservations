package com.example;

import com.example.model.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDateTime;

public class App {

    public static void main(String[] args) {

        // 1. Créer l'EntityManagerFactory à partir du persistence-unit
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("gestion-reservations-pu");

        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            // ─────────────────────────────────────────────
            // 2. Créer et remplir les Equipements
            // ─────────────────────────────────────────────
            Equipement projecteur = new Equipement();
            projecteur.setNom("Projecteur HD");

            Equipement tableauBlanc = new Equipement();
            tableauBlanc.setNom("Tableau blanc");

            Equipement visio = new Equipement();
            visio.setNom("Système de visioconférence");

            em.persist(projecteur);
            em.persist(tableauBlanc);
            em.persist(visio);

            // ─────────────────────────────────────────────
            // 3. Créer et remplir les Salles
            // ─────────────────────────────────────────────
            Salle amphiA = new Salle();
            amphiA.setNom("Amphi A");
            amphiA.setCapacite(120);
            amphiA.addEquipement(projecteur);
            amphiA.addEquipement(visio);

            Salle salleB12 = new Salle();
            salleB12.setNom("Salle B12");
            salleB12.setCapacite(30);
            salleB12.addEquipement(tableauBlanc);

            em.persist(amphiA);
            em.persist(salleB12);

            // ─────────────────────────────────────────────
            // 4. Créer et remplir les Utilisateurs
            // ─────────────────────────────────────────────
            Utilisateur alice = new Utilisateur();
            alice.setEmail("alice@example.com");
            alice.setNom("Alice Dupont");

            Utilisateur bob = new Utilisateur();
            bob.setEmail("bob@example.com");
            bob.setNom("Bob Martin");

            em.persist(alice);
            em.persist(bob);

            // ─────────────────────────────────────────────
            // 5. Créer et remplir les Reservations
            // ─────────────────────────────────────────────
            Reservation r1 = new Reservation();
            r1.setDateDebut(LocalDateTime.of(2026, 10, 20, 9, 0));
            r1.setDateFin(LocalDateTime.of(2026, 10, 20, 11, 0));
            r1.setSalle(amphiA);
            alice.addReservation(r1);   // met aussi r1.utilisateur = alice

            Reservation r2 = new Reservation();
            r2.setDateDebut(LocalDateTime.of(2026, 10, 21, 14, 0));
            r2.setDateFin(LocalDateTime.of(2026, 10, 21, 16, 0));
            r2.setSalle(salleB12);
            bob.addReservation(r2);

            em.persist(r1);
            em.persist(r2);

            // ─────────────────────────────────────────────
            // 6. Commit
            // ─────────────────────────────────────────────
            em.getTransaction().commit();

            System.out.println("✅ Données insérées avec succès !");
            System.out.println("   - 3 équipements");
            System.out.println("   - 2 salles");
            System.out.println("   - 2 utilisateurs");
            System.out.println("   - 2 réservations");

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("❌ Erreur : " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }
}