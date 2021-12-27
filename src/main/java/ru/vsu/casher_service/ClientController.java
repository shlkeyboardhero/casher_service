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


    @GetMapping("/test1")
    public String findAll2() {
        int result = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM client", Integer.class);
        return Integer.toString(result);
    }

    @GetMapping("/show")
    public List getList() {
        ArrayList clients = new ArrayList();
        clients.add("Bla");
        clients.add("Ara");
        return clients;
    }

    @PostMapping("/pay")
    public String pay(@RequestParam(value = "key") String key) {
        return key + "Just a string";
    }

    @GetMapping
    public List<Client> getClient() {
        return jdbcTemplate.query("SELECT * FROM client", new BeanPropertyRowMapper<>(Client.class));
    }

    @GetMapping("/{id}")
    public Client getOneClient(@PathVariable String id){
        return jdbcTemplate.queryForObject("SELECT * FROM client WHERE id = ? ", new Object[]{id}, new int[]{Types.VARCHAR}, new BeanPropertyRowMapper<>(Client.class));
    }

    @GetMapping("/card/{cardnumber}")
    public Client getOneClientByCardNumber(@PathVariable BigInteger cardnumber){
        return jdbcTemplate.queryForObject("SELECT cl.* FROM card ca, client cl\n" +
                "WHERE ca.cardnumber = ?\n" +
                "  AND cl.id = ca.clientid  ", new Object[]{cardnumber}, new int[]{Types.BIGINT}, new BeanPropertyRowMapper<>(Client.class));
    }
}
