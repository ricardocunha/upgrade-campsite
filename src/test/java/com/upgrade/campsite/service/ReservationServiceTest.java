package com.upgrade.campsite.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.upgrade.campsite.dto.Availability;
import com.upgrade.campsite.exception.ReservationNotFoundException;
import com.upgrade.campsite.model.Reservation;
import com.upgrade.campsite.repository.ReservationRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
public class ReservationServiceTest {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired    
    private ReservationService reservationService;
    
    @Test
    public void should_cancel_reservation() {
        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.now().plusDays(25));
        reservation.setEndDate(LocalDate.now().plusDays(26));        
        reservation.setGuestName("Ricardo Cunha");
        reservation.setGuestEmail("ricardocunha@gmail.com");
        reservation.setReservationCode(String.valueOf(UUID.randomUUID()));
        Reservation saved = reservationRepository.save(reservation);
        
        assertTrue(reservationService.cancel(saved.getId()));
    }



    @Test(expected = ReservationNotFoundException.class)
    public void should_not_update_reservation() {
        ReservationRepository reservationRepositoryMock = Mockito.mock(ReservationRepository.class);
        ReservationService reservationServiceMock = new ReservationService(reservationRepositoryMock);
        Long id = 9999L;
        when(reservationRepositoryMock.findById(id)).thenReturn(null);
        reservationServiceMock.update(new Reservation(), id);
    }

    @Test
    public void should_update_reservation() {
        ReservationRepository reservationRepositoryMock = Mockito.mock(ReservationRepository.class);
        ReservationService reservationServiceMock = new ReservationService(reservationRepositoryMock);

        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.now().plusDays(27));
        reservation.setEndDate(LocalDate.now().plusDays(28));        
        reservation.setGuestName("Ricardo Cunha");
        reservation.setGuestEmail("ricardocunha@gmail.com");;

        when(reservationRepositoryMock.save(reservation)).thenReturn(reservation);
        Long id = reservation.getId();
        when(reservationRepositoryMock.findById(id)).thenReturn(Optional.of(reservation));
        Reservation reservationUpdated = reservationServiceMock.update(reservation, id);

        assertEquals(reservation.getStartDate(), reservationUpdated.getStartDate());
        assertEquals(reservation.getEndDate(), reservationUpdated.getEndDate());
        assertEquals(reservation.getGuestEmail(), reservationUpdated.getGuestEmail());
        assertEquals(reservation.getGuestName(), reservationUpdated.getGuestName());
    }

    @Test
    public void should_get_reservation() {
        ReservationRepository reservationRepositoryMock = Mockito.mock(ReservationRepository.class);
        ReservationService reservationServiceMock = new ReservationService(reservationRepositoryMock);

        Reservation reservation = Mockito.mock(Reservation.class);
        Long id = 1L;
        when(reservationRepositoryMock.findById(id)).thenReturn(Optional.of(reservation));
        Reservation reserved = reservationServiceMock.find(id);

        assertEquals(reservation, reserved);
    }

    @Test(expected = ReservationNotFoundException.class)
    public void should_not_get_reservation() {
        ReservationRepository reservationRepositoryMock = Mockito.mock(ReservationRepository.class);
        ReservationService reservationServiceMock = new ReservationService(reservationRepositoryMock);
        Long id = 9999L;
        when(reservationServiceMock.find(id)).thenReturn(null);
        reservationServiceMock.find(id);
    }

    @Test
    public void should_get_all_availabilities() throws Exception {
        ReservationRepository reservationRepositoryMock = mock(ReservationRepository.class);
        ReservationService reservationServiceMock = new ReservationService(reservationRepositoryMock);
        Availability availability = null;
        LocalDate localDate = null;
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(5);

        List<Availability> availabilityList = reservationServiceMock.getAvailabilities(startDate, endDate);

        for (int day = 1; day < 5; day++) {
        	localDate = LocalDate.now();
            availability = new Availability(localDate.plusDays(day));
            assertTrue(availabilityList.contains(availability));
        }

        assertEquals(4, availabilityList.size());
    }    
}
