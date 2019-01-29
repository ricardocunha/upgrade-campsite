package com.upgrade.campsite.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.anarsoft.vmlens.concurrent.junit.ConcurrentTestRunner;
import com.upgrade.campsite.model.Reservation;
import com.upgrade.campsite.repository.ReservationRepository;


@RunWith(ConcurrentTestRunner.class)
public class ReservationConcurrencyTest {

    @Test(timeout = 1000L)
    public void should_create_reservation() throws InterruptedException {
        ReservationRepository reservationRepositoryMock = Mockito.mock(ReservationRepository.class);
        ReservationService reservationServiceMock = new ReservationService(reservationRepositoryMock);
        
        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.now().plusDays(2));
        reservation.setEndDate(LocalDate.now().plusDays(4));        
        reservation.setGuestName("Ricardo Cunha");
        reservation.setGuestEmail("ricardocunha@gmail.com");

        reservationServiceMock.create(reservation);
    }

    @Test(timeout = 1000L)
    public void try_create_overlap_reservation() throws InterruptedException {
        ReservationRepository reservationRepositoryMock = Mockito.mock(ReservationRepository.class);
        ReservationService reservationServiceMock = new ReservationService(reservationRepositoryMock);

        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.now().plusDays(3));
        reservation.setEndDate(LocalDate.now().plusDays(5));        
        reservation.setGuestName("Ricardo Cunha");
        reservation.setGuestEmail("ricardocunha@gmail.com");

        reservationServiceMock.create(reservation);
    }




    @Test(timeout = 1000L)
    public void should_reserve_unavailable() throws InterruptedException {
        ReservationRepository reservationRepositoryMock = Mockito.mock(ReservationRepository.class);
        ReservationService reservationServiceMock = new ReservationService(reservationRepositoryMock);

        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.now().plusDays(7));
        reservation.setEndDate(LocalDate.now().plusDays(9));        
        reservation.setGuestName("Ricardo Cunha");
        reservation.setGuestEmail("ricardocunha@gmail.com");
        when(reservationRepositoryMock.count(anyObject(), anyObject())).thenReturn(0L);
        when(reservationRepositoryMock.save(reservation)).thenReturn(reservation);

        Reservation savedReservation = reservationServiceMock.create(reservation);
        assertEquals(reservation, savedReservation);
    }
}
