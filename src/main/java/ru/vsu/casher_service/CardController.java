package ru.vsu.casher_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.vsu.casher_service.dto.ClientCardDTO;
import ru.vsu.casher_service.dto.TransferDTO;
import ru.vsu.casher_service.models.Card;
import ru.vsu.casher_service.models.Client;

import javax.sql.DataSource;
import java.math.BigInteger;
import java.security.Principal;
import java.sql.Types;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/card")

public class CardController {

    @Autowired
    JdbcTemplate jdbcTemplate;


    @GetMapping("/{cardnumber}")
    public Client getOneClientByCardNumber(@PathVariable BigInteger cardnumber) {
        return jdbcTemplate.queryForObject("SELECT cl.* FROM card ca, client cl\n" +
                "WHERE ca.cardnumber = ?\n" +
                "  AND cl.id = ca.clientid  ", new Object[]{cardnumber}, new int[]{Types.BIGINT}, new BeanPropertyRowMapper<>(Client.class));
    }


    @PostMapping
    public String createClient(ClientCardDTO clientCardDTO) {
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



    @GetMapping("/login")
    public Client login() {
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

    @PostMapping("/recharge/{sum}")
    public int recharge(Principal principal, @PathVariable Integer sum) {
        String cardNumber = principal.getName();
        return jdbcTemplate.update("UPDATE card SET cash = cash + ? WHERE cardnumber = CAST(? AS bigint)", new Object[]{sum,cardNumber});
    }

    /*@PostMapping("/transfer/{sum}/{otherCard}")
    public int transfer_1(Principal principal, @PathVariable Integer sum, @PathVariable String otherCard) {
        String cardNumber = principal.getName();
        if (jdbcTemplate.update("UPDATE card SET cash = cash - ? WHERE cardnumber = CAST(? AS bigint) AND cash >= ?", new Object[]{sum,cardNumber, sum}) == 1)
            return jdbcTemplate.update("UPDATE card SET cash = cash + ? WHERE cardnumber = CAST(? AS bigint)", new Object[]{sum, otherCard});
        else
            return 0;
    }*/

    @PostMapping("/transfer")
    public int transfer(Principal principal, @RequestBody TransferDTO transferDTO) {
        String cardNumber = principal.getName();
        if (jdbcTemplate.update("UPDATE card SET cash = cash - ? WHERE cardnumber = CAST(? AS bigint) AND cash >= ?", new Object[]{transferDTO.getSum(),cardNumber, transferDTO.getSum()}) == 1)
            return jdbcTemplate.update("UPDATE card SET cash = cash + ? WHERE cardnumber = CAST(? AS bigint)", new Object[]{transferDTO.getSum(), transferDTO.getOtherCardNumber()});
        else
            return 0;
    }

    @PostMapping("/withdrawal/{sum}")
    public int withdrawal(Principal principal, @PathVariable Integer sum) {
        String cardNumber = principal.getName();
        return jdbcTemplate.update("UPDATE card SET cash = cash - ? WHERE cardnumber = CAST(? AS bigint)", new Object[]{sum,cardNumber});
    }
}
