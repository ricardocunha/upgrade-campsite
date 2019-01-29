package com.upgrade.campsite.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.upgrade.campsite.model.Reservation;


public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    static final String RESERVATION_WHERE =
      " where (startDate <= ?1 "
    		  + " and endDate > ?1) " +
        " or (startDate >= ?1 "
        	  + " and startDate < ?2) ";

    @Query("select count(res) from Reservation res " + RESERVATION_WHERE)
    long count(LocalDate startDate, LocalDate endDate);

    @Query("select res from Reservation res " + RESERVATION_WHERE + " order by startDate asc")
    List<Reservation> findPeriodByDates(LocalDate startDate, LocalDate endDate);
    
    @Query("select res from Reservation res where reservationCode = ?1")
    Optional<Reservation> findbyReservationCode(UUID reservationCode);

}
