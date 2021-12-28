package ru.vsu.casher_service;

import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import ru.vsu.casher_service.models.Client;

import javax.sql.DataSource;
import java.math.BigInteger;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/client")
public class ClientController {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private Methods methods = new Methods();



    @GetMapping("/card/{cardnumber}")
    public Client getOneClientByCardNumber(@PathVariable BigInteger cardnumber){
        return methods.getOneClientByCardNumber(cardnumber, jdbcTemplate);
    }


}
