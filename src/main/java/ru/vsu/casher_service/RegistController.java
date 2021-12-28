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

    private Methods methods = new Methods();

    @Autowired
    JdbcTemplate jdbcTemplate;


    @PostMapping
    public String createClient(@RequestBody ClientCardDTO clientCardDTO) {
        return methods.createClient1(clientCardDTO, jdbcTemplate);
    }



    @GetMapping("/check")
    public String Check(@RequestBody Card card) {
        return methods.Check(card, jdbcTemplate);
    }

}
