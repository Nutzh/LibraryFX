package com.library.app.dao.impl;

import com.library.app.dao.EmpruntDAO;
import com.library.app.model.*;
import com.library.app.util.DatabaseConnection;

import java.sql.*;
import java.util.*;

public class EmpruntDAOImpl implements EmpruntDAO {

    private final Connection connection =
            DatabaseConnection.getConnection();

    @Override
    public void save(Emprunt e) {
        String sql = """
            INSERT INTO emprunts
            (livre_id, membre_id, date_emprunt, date_retour_prevue)
            VALUES (?, ?, ?, ?)
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, e.getLivre().getId());
            ps.setInt(2, e.getMembre().getId());
            ps.setDate(3, new java.sql.Date(e.getDateEmprunt().getTime()));
            ps.setDate(4, new java.sql.Date(e.getDateRetourPrevue().getTime()));
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void update(Emprunt e) {
        String sql = """
            UPDATE emprunts
            SET date_retour_effective = ?
            WHERE id = ?
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, e.getDateRetourEffective() == null
                    ? null
                    : new java.sql.Date(e.getDateRetourEffective().getTime()));
            ps.setInt(2, e.getId());
            ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Emprunt findById(int id) {
        String sql = "SELECT * FROM emprunts WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Emprunt e = new Emprunt();
                e.setId(rs.getInt("id"));
                e.setDateEmprunt(rs.getDate("date_emprunt"));
                e.setDateRetourPrevue(rs.getDate("date_retour_prevue"));
                e.setDateRetourEffective(rs.getDate("date_retour_effective"));
                return e;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Emprunt> findAll() {
        List<Emprunt> list = new ArrayList<>();
        String sql = "SELECT * FROM emprunts";

        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Emprunt e = new Emprunt();
                e.setId(rs.getInt("id"));
                e.setDateEmprunt(rs.getDate("date_emprunt"));
                e.setDateRetourPrevue(rs.getDate("date_retour_prevue"));
                e.setDateRetourEffective(rs.getDate("date_retour_effective"));
                list.add(e);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Emprunt> findEnCours() {
        List<Emprunt> list = new ArrayList<>();

        String sql = """
            SELECT * FROM emprunts
            WHERE date_retour_effective IS NULL
        """;

        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Emprunt e = new Emprunt();
                e.setId(rs.getInt("id"));
                e.setDateEmprunt(rs.getDate("date_emprunt"));
                e.setDateRetourPrevue(rs.getDate("date_retour_prevue"));
                list.add(e);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Emprunt> findByMembre(int membreId) {
        List<Emprunt> list = new ArrayList<>();

        String sql = "SELECT * FROM emprunts WHERE membre_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, membreId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Emprunt e = new Emprunt();
                e.setId(rs.getInt("id"));
                e.setDateEmprunt(rs.getDate("date_emprunt"));
                e.setDateRetourPrevue(rs.getDate("date_retour_prevue"));
                e.setDateRetourEffective(rs.getDate("date_retour_effective"));
                list.add(e);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @Override
    public int countEmpruntsEnCours(int membreId) {
        String sql = """
            SELECT COUNT(*) FROM emprunts
            WHERE membre_id = ?
            AND date_retour_effective IS NULL
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, membreId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }
}
