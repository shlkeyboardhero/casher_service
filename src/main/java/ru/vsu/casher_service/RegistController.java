package ru.vsu.casher_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.vsu.casher_service.dto.ClientCardDTO;
import ru.vsu.casher_service.models.Card;
import ru.vsu.casher_service.models.Client;

import javax.sql.DataSource;
import java.io.IOException;
import java.math.BigInteger;
import java.security.Principal;
import java.sql.Types;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/reg")
public class RegistController {

    @Autowired
    JdbcTemplate jdbcTemplate;


    @PostMapping
    public String createClient(@RequestBody ClientCardDTO clientCardDTO) {
        try {
            String cardId = jdbcTemplate.queryForObject("SELECT id FROM card WHERE cardnumber = ?",
                    new Object[]{clientCardDTO.getCardNumber()}, new int[]{Types.BIGINT}, String.class);
            return "-1";
        }
        catch (EmptyResultDataAccessException e) {
        }
        String clientId = null;
        try {
            clientId = jdbcTemplate.queryForObject("SELECT id FROM client WHERE passport = ?",
                    new Object[]{clientCardDTO.getPassport()}, new int[]{Types.VARCHAR}, String.class);
        }
        catch (EmptyResultDataAccessException e){
        }
        if (clientId == null) {
            clientId  = UUID.randomUUID().toString();
            jdbcTemplate.update("INSERT INTO client (id, firstname, lastname, patronymic, passport) " +
                    "VALUES (?, ?, ?, ?, ?)", new Object[]{clientId, clientCardDTO.getFirstName(),
                    clientCardDTO.getLastName(), clientCardDTO.getPatronymic(), clientCardDTO.getPassport()});
            String cardId  = UUID.randomUUID().toString();
            jdbcTemplate.update("INSERT INTO card (id, cardnumber, counterpin, pin, cash, ban, clientid) VALUES (?, ?, 0, ?, 0, false, ?)",
                    new Object[]{cardId,clientCardDTO.getCardNumber(),clientCardDTO.getPIN(), clientId});
        }
        return clientId;
    }

    @GetMapping("/check")
    public String Check(@RequestBody Card card) {
        String result = null;
        try {
            result = jdbcTemplate.queryForObject("SELECT clientid FROM card WHERE cardnumber = ?", new Object[]{card.getCardNumber()}, new int[]{Types.BIGINT}, String.class);
        }
        catch (EmptyResultDataAccessException e){
        }
        return result;
    }
}
