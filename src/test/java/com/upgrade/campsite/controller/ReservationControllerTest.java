package com.upgrade.campsite.controller;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockReset;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.upgrade.campsite.model.Reservation;
import com.upgrade.campsite.repository.ReservationRepository;
import com.upgrade.campsite.service.ReservationService;

@RunWith(SpringRunner.class)
@WebMvcTest(ReservationController.class)
public class ReservationControllerTest {
	private static final String API_PREFIX = "/api/v1/reservations/";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationService reservationService;

    @MockBean(reset = MockReset.BEFORE)
    private ReservationRepository reservationRepository;

    private DateTimeFormatter formatter = null;
    @Before
    public void setupDateConverter() {
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }
    @Test
    public void should_create_reservation() throws Exception {
        Reservation reservation = new Reservation();
        reservation.setId(99L);
        
        when(reservationService.create(any(Reservation.class))).thenReturn(reservation);

        this.mockMvc.perform(post(API_PREFIX+"/reservation")
          .contentType("application/json")
          .content("{ " +
            "\"startDate\":\""  + LocalDate.now().plusDays(1).format(formatter) + "\"," +            
            "\"endDate\":\"" + LocalDate.now().plusDays(3).format(formatter) + "\"," +
            "\"guestName\":\"John Smith\"," +
            "\"guestEmail\":\"john@smith.com\"" +            
            "}"))
          .andExpect(status().isCreated());
    }

    @Test
    public void should_not_reserve() throws Exception {
        Reservation reservation = new Reservation();
        reservation.setId(99L);
        when(reservationService.create(any(Reservation.class))).thenReturn(reservation);

        this.mockMvc.perform(post(API_PREFIX+"/reservation")
          .contentType("application/json")
          .contentType("application/json")
          .content("{ " +
            "\"startDate\":\""  + LocalDate.now().plusDays(2).format(formatter) + "\"," +            
            "\"endDate\":\"" + LocalDate.now().plusDays(10).format(formatter) + "\"," +
            "\"guestName\":\"John Smith\"," +
            "\"guestEmail\":\"john@smith.com\"" +            
            "}"))
          .andExpect(status().isBadRequest());
    }

    @Test
    public void should_get_reservation() throws Exception {
        Reservation reservation = new Reservation();
        reservation.setStartDate(LocalDate.now().plusDays(25));
        reservation.setEndDate(LocalDate.now().plusDays(26));        
        reservation.setGuestName("Ricardo Cunha");
        reservation.setGuestEmail("ricardocunha@gmail.com");
        UUID reservationCode = UUID.randomUUID();
        reservation.setReservationCode(String.valueOf(reservationCode));
        String code = String.valueOf(reservationCode);

        when(reservationService.findByReservationCode(reservationCode)).thenReturn(reservation);

        String json = this.mockMvc.perform(get(API_PREFIX+"/reservation").param("reservationCode", code)
        ).andReturn().getResponse().getContentAsString();

        assertTrue(json.contains("\"startDate\":\"" + LocalDate.now().plusDays(25).format(formatter)  +
                "\""));
        assertTrue(json.contains("\"endDate\":\"" + LocalDate.now().plusDays(26).format(formatter)  +
          "\""));
        assertTrue(json.contains("\"reservationCode\":\""+code+"\""));
        assertTrue(json.contains("\"guestName\":\"Ricardo Cunha\""));
        assertTrue(json.contains("\"guestEmail\":\"ricardocunha@gmail.com\""));
    }

    @Test
    public void should_cancel_reservation() throws Exception {
        this.mockMvc.perform(delete(API_PREFIX+"/reservation")
                .param("reservationId", "99"))
                .andExpect(status().isNoContent());
    }
    
    @Test
    public void should_update_reservation() throws Exception {
        this.mockMvc.perform(put(API_PREFIX+"/reservation")
 		
          .param("reservationId", "99")
          .contentType("application/json")                
          .content("{\"id\":99," +
            "\"startDate\":\"" + LocalDate.now().plusDays(2).format(formatter) + "\"," +
            "\"endDate\":\""  + LocalDate.now().plusDays(5).format(formatter) +"\"," +
            "\"guestName\":\"John Smith\"," +
            "\"guestEmail\":\"john@smith.com\"" +            
            "}"))
          .andExpect(status().isNoContent());
    }
}
