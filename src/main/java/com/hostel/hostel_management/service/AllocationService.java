package com.hostel.hostel_management.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.CallableStatement;

/**
 * Allocation rules now live in the database:
 *  - stored procedures allocate_room / vacate_room check the rules
 *  - triggers update the room occupancy automatically
 * This class only calls the procedures through JDBC.
 */
@Service
public class AllocationService {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void allocate(Integer studentId, Integer roomId) {
        try {
            em.unwrap(Session.class).doWork(connection -> {
                try (CallableStatement cs = connection.prepareCall("{call allocate_room(?, ?)}")) {
                    cs.setInt(1, studentId);
                    cs.setInt(2, roomId);
                    cs.execute();
                }
            });
        } catch (Exception e) {
            throw new IllegalStateException(rootMessage(e));
        }
    }

    @Transactional
    public void vacate(Integer allocId) {
        try {
            em.unwrap(Session.class).doWork(connection -> {
                try (CallableStatement cs = connection.prepareCall("{call vacate_room(?)}")) {
                    cs.setInt(1, allocId);
                    cs.execute();
                }
            });
        } catch (Exception e) {
            throw new IllegalStateException(rootMessage(e));
        }
    }

    // The message raised inside MySQL (SIGNAL ... MESSAGE_TEXT) is the root cause
    private String rootMessage(Throwable t) {
        while (t.getCause() != null) {
            t = t.getCause();
        }
        return t.getMessage();
    }
}
