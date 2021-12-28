package ru.vsu.casher_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.vsu.casher_service.dto.ClientCardDTO;
import ru.vsu.casher_service.dto.TransferDTO;
import ru.vsu.casher_service.models.Client;

import java.math.BigInteger;
import java.security.Principal;
import java.sql.Types;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/card")

public class CardController {
    private Methods methods = new Methods();

    @Autowired
    JdbcTemplate jdbcTemplate;


    @GetMapping("/{cardnumber}")
    public Client getOneClientByCardNumber(@PathVariable BigInteger cardnumber) {
        return methods.getOneClient(cardnumber, jdbcTemplate);
    }




    @PostMapping
    public String createClient(ClientCardDTO clientCardDTO) {
        return methods.createClient2(clientCardDTO, jdbcTemplate);
    }




    @GetMapping("/login")
    public Client login() {
        return methods.getClient(jdbcTemplate);
    }



    @PostMapping("/recharge/{sum}")
    public int recharge(Principal principal, @PathVariable Integer sum) {
        return methods.recharge(principal, sum, jdbcTemplate);
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
        return methods.transfer(principal, transferDTO, jdbcTemplate);
    }



    @PostMapping("/withdrawal/{sum}")
    public int withdrawal(Principal principal, @PathVariable Integer sum) {
        return methods.withdrawal(principal, sum, jdbcTemplate);
    }


}
