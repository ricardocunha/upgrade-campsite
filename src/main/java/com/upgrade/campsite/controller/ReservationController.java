package com.upgrade.campsite.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity.BodyBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.upgrade.campsite.dto.Availability;
import com.upgrade.campsite.model.Reservation;
import com.upgrade.campsite.service.ReservationService;
import com.upgrade.campsite.util.ReservationEnum;

@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController {
    private ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/reservation", params = "reservationCode")
    public Reservation find(@NotNull String reservationCode) {
        return reservationService.findByReservationCode(UUID.fromString(reservationCode));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/reservation")
    public ResponseEntity<String> create(@Valid @RequestBody Reservation reservation) {
        Reservation reserve = reservationService.create(reservation);
        return ResponseEntity.status(HttpStatus.CREATED).body(reserve.getReservationCode());
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/reservation", params = "reservationId")
    public ResponseEntity<BodyBuilder> update(@NotNull Long reservationId,
                                 @Valid @RequestBody Reservation reservation) {
        reservationService.update(reservation, reservationId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/reservation", params = "reservationId")
    public ResponseEntity<BodyBuilder> cancel(@NotNull Long reservationId) {
        reservationService.cancel(reservationId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/availabilities", params = "from")
    public List<Availability> getAvailabilities(@NotNull @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                                           @RequestParam(value = "end", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        if (to == null) {
            to = from.plusDays(ReservationEnum.MAX_PERIOD_AHEAD.getValue());
        }
        return reservationService.getAvailabilities(from, to);
    }    
}
