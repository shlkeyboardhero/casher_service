package ru.vsu.casher_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import ru.vsu.casher_service.dto.ClientCardDTO;
import ru.vsu.casher_service.dto.TransferDTO;
import ru.vsu.casher_service.models.Card;
import ru.vsu.casher_service.models.Client;

import java.math.BigInteger;
import java.security.Principal;
import java.sql.Types;
import java.util.UUID;

public class Methods {



    public int transfer(Principal principal, TransferDTO transferDTO, JdbcTemplate jdbcTemplate) {
        String cardNumber = principal.getName();
        if (jdbcTemplate.update("UPDATE card SET cash = cash - ? WHERE cardnumber = CAST(? AS bigint) AND cash >= ?", new Object[]{transferDTO.getSum(),cardNumber, transferDTO.getSum()}) == 1)
            return jdbcTemplate.update("UPDATE card SET cash = cash + ? WHERE cardnumber = CAST(? AS bigint)", new Object[]{transferDTO.getSum(), transferDTO.getOtherCardNumber()});
        else
            return 0;
    }

    public int withdrawal(Principal principal, @PathVariable Integer sum, JdbcTemplate jdbcTemplate) {
        String cardNumber = principal.getName();
        return jdbcTemplate.update("UPDATE card SET cash = cash - ? WHERE cardnumber = CAST(? AS bigint)", new Object[]{sum,cardNumber});
    }

    public int recharge(Principal principal, Integer sum, JdbcTemplate jdbcTemplate) {
        String cardNumber = principal.getName();
        return jdbcTemplate.update("UPDATE card SET cash = cash + ? WHERE cardnumber = CAST(? AS bigint)", new Object[]{sum,cardNumber});
    }

    public Client getClient(JdbcTemplate jdbcTemplate) {
        String cardNumber;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            cardNumber= ((UserDetails)principal).getUsername();
        } else {
            cardNumber = principal.toString();
        }

        return jdbcTemplate.queryForObject("SELECT cl.* FROM card ca, client cl\n" +
                "WHERE ca.cardnumber = ?\n" +
                "  AND cl.id = ca.clientid  ", new Object[]{cardNumber}, new int[]{Types.BIGINT}, new BeanPropertyRowMapper<>(Client.class));
    }

    public String createClient2(ClientCardDTO clientCardDTO, JdbcTemplate jdbcTemplate) {
        String cardId = jdbcTemplate.queryForObject("SELECT id FROM card WHERE cardnumber = ?",
                new Object[]{clientCardDTO.getCardNumber()}, new int[]{Types.BIGINT}, String.class);
        if (cardId != null)
            return "-1";
        String clientId = jdbcTemplate.queryForObject("SELECT id FROM clinet WHERE passport = ?",
                new Object[]{clientCardDTO.getPassport()}, new int[]{Types.VARCHAR}, String.class);
        if (clientId == null) {
            clientId  = UUID.randomUUID().toString();
            jdbcTemplate.update("INSERT INTO client (id, firstname, secondname, patronymic, passport) " +
                    "VALUES (?, ?, ?, ?, ?)", new Object[]{clientId, clientCardDTO.getFirstName(),
                    clientCardDTO.getLastName(), clientCardDTO.getPatronymic(), clientCardDTO.getPassport()});
        }

        return clientId;
    }

    public Client getOneClient(BigInteger cardnumber, JdbcTemplate jdbcTemplate) {
        return jdbcTemplate.queryForObject("SELECT cl.* FROM card ca, client cl\n" +
                "WHERE ca.cardnumber = ?\n" +
                "  AND cl.id = ca.clientid  ", new Object[]{cardnumber}, new int[]{Types.BIGINT}, new BeanPropertyRowMapper<>(Client.class));
    }

    public Client getOneClientByCardNumber(BigInteger cardnumber,JdbcTemplate jdbcTemplate) {
        return jdbcTemplate.queryForObject("SELECT cl.* FROM card ca, client cl\n" +
                "WHERE ca.cardnumber = ?\n" +
                "  AND cl.id = ca.clientid  ", new Object[]{cardnumber}, new int[]{Types.BIGINT}, new BeanPropertyRowMapper<>(Client.class));
    }

    public String Check(Card card, JdbcTemplate jdbcTemplate) {
        String result = null;
        try {
            result = jdbcTemplate.queryForObject("SELECT clientid FROM card WHERE cardnumber = ?", new Object[]{card.getCardNumber()}, new int[]{Types.BIGINT}, String.class);
        }
        catch (EmptyResultDataAccessException e){
        }
        return result;
    }

    public String createClient1(ClientCardDTO clientCardDTO, JdbcTemplate jdbcTemplate) {
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
                    new Object[]{cardId, clientCardDTO.getCardNumber(), clientCardDTO.getPIN(), clientId});
        }
        return clientId;
    }

}
