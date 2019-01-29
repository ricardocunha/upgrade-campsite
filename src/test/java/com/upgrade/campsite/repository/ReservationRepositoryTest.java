package com.upgrade.campsite.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.upgrade.campsite.model.Reservation;

@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional
public class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    public void should_save_reservation() {
        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.now().plusDays(25));
        reservation.setEndDate(LocalDate.now().plusDays(26));        
        reservation.setGuestName("Ricardo Cunha");
        reservation.setGuestEmail("ricardocunha@gmail.com");
        reservation.setReservationCode(String.valueOf(UUID.randomUUID()));
        
        Reservation persisted = reservationRepository.save(reservation);
        Optional<Reservation> r = reservationRepository.findById(persisted.getId());
        assertEquals(persisted, r.get());
    }
    
    @Test
    public void should_remove_reservation() {
        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.now().plusDays(25));
        reservation.setEndDate(LocalDate.now().plusDays(26));        
        reservation.setGuestName("Ricardo Cunha");
        reservation.setGuestEmail("ricardocunha@gmail.com");
        reservation.setReservationCode(String.valueOf(UUID.randomUUID()));
        
        reservation = reservationRepository.saveAndFlush(reservation);
        reservationRepository.deleteById(reservation.getId());
        Optional<Reservation> r = reservationRepository.findById(reservation.getId());
        assertTrue(!r.isPresent());
    }    
}
