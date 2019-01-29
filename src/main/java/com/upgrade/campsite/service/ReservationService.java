package com.upgrade.campsite.service;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.upgrade.campsite.dto.Availability;
import com.upgrade.campsite.exception.ReservationNotFoundException;
import com.upgrade.campsite.exception.ReservationOverlapException;
import com.upgrade.campsite.model.Reservation;
import com.upgrade.campsite.repository.ReservationRepository;

@Service
public class ReservationService {
    private static final int ADD_DAYS = 1;	
	
    @Autowired
	private ReservationRepository reservationRepository;
	
	private static final ReentrantLock LOCK = new ReentrantLock();
			
	@Autowired
	public ReservationService(ReservationRepository reservationRepository) {
		this.reservationRepository = reservationRepository;
	}

	public Reservation find(Long reservationId) {
		return reservationRepository.findById(reservationId)
				.orElseThrow(() -> new ReservationNotFoundException("No Reservation with id '" + reservationId + "'"));
	}

	public Reservation findByReservationCode(UUID reservationCode) {
		return reservationRepository.findbyReservationCode(reservationCode)
				.orElseThrow(() -> new ReservationNotFoundException("No Reservation with code '" + reservationCode + "'"));
	}
	@Transactional
	public Reservation create(Reservation reservation) {

		if (!isAvailable(reservation)) {
			throw new ReservationOverlapException("The requested period are not available");
		}

		Reservation savedReservation = null;
 	
        try {
			LOCK.lockInterruptibly();
              	
			if (!isAvailable(reservation)) {
				throw new ReservationOverlapException("The requested period are not available");
			}
			reservation.setReservationCode(String.valueOf(UUID.randomUUID()));
			savedReservation = reservationRepository.save(reservation);
		} catch (Exception e) {
			throw new ReservationOverlapException("The requested period are not available");
        } finally {
        	LOCK.unlock();
        }

		return savedReservation;
	}

	public Reservation update(Reservation reservation, Long reservationId) {
		Optional<Reservation> oReservation = Optional.ofNullable(reservationRepository.findById(reservationId))
				.orElseThrow(() -> new ReservationNotFoundException("No Reservation with id '" + reservationId + "'"));
		Reservation entity = oReservation.get();
		entity.setStartDate(reservation.getStartDate());
		entity.setEndDate(reservation.getEndDate());
		entity.setGuestName(reservation.getGuestName());
		entity.setGuestEmail(reservation.getGuestEmail());
		return reservationRepository.save(entity);
	}

	public boolean cancel(Long reservationId) {
		Reservation entity = reservationRepository.findById(reservationId)
				.orElseThrow(() -> new ReservationNotFoundException("No Reservation with id '" + reservationId + "'"));		
		reservationRepository.delete(entity);
		return true;
	}

	private boolean isAvailable(Reservation reservation) {
		return reservationRepository.count(reservation.getStartDate(), reservation.getEndDate()) == 0;
	}
	
    public List<Availability> getAvailabilities(LocalDate beginDate, LocalDate endDate) {
        Assert.notNull(beginDate,"beginDate cannot be null ");
        Assert.notNull(endDate,"endDate cannot be null ");
        Assert.isTrue(beginDate.isBefore(endDate),"startDate cannot be before endDate ");


        List<Reservation> sortedReservations = reservationRepository.findPeriodByDates(beginDate, endDate);
        List<Availability> availabilities = new LinkedList<>();
        LocalDate current = beginDate;

        for (Reservation reservation : sortedReservations) {
            availabilities.addAll(getAvailabilities(current, endDate, reservation.getStartDate()));
            current = reservation.getEndDate();
        }
        availabilities.addAll(getAvailabilities(current, endDate, endDate));

        return availabilities;
    }

    private List<Availability> getAvailabilities(LocalDate start, LocalDate end, LocalDate firstReservation) {
        List<Availability> availabilities = new LinkedList<>();
        if (firstReservation.isBefore(end)) {
            end = firstReservation;
        }
        LocalDate current = start;
        while (current.isBefore(end)) {
            availabilities.add(new Availability(current));
            current = current.plusDays(ADD_DAYS);
        }
        return availabilities;
    }	
}
