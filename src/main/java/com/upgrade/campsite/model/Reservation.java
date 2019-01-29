package com.upgrade.campsite.model;

import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.upgrade.campsite.validator.ReservationPeriodValidator;
import com.upgrade.campsite.validator.ReservationWarningValidator;

import lombok.Getter;
import lombok.Setter;

@Entity
@ReservationPeriodValidator
@ReservationWarningValidator
@Getter
@Setter
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column(name = "reservation_code", unique = true)
    private String reservationCode;
    
    @NotNull(message = "startDate must be declared")
    @Column(name = "start_date", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate startDate;

    @NotNull(message = "endDate must be declared")
    @Column(name = "end_date", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @NotNull(message = "a valid name must be declared")
    @Column(name = "guest_name", nullable = false)
    private String guestName;
    
    @NotNull(message = "a valid email must be declared")
    @Column(name = "guest_email", nullable = false)
    private String guestEmail;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return getId().equals(that.getId()) &&
                getGuestName().equals(that.getGuestName()) &&
                getStartDate().equals(that.getStartDate()) &&
                getEndDate().equals(that.getEndDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getGuestName(), getStartDate(), getEndDate());
    }
}
