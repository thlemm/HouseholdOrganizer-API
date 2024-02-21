package de.thlemm.householdorganizer.service.Impl;

import de.thlemm.householdorganizer.controller.request.SetTransactionRequest;
import de.thlemm.householdorganizer.model.*;
import de.thlemm.householdorganizer.repository.*;
import de.thlemm.householdorganizer.service.CasinoCodeService;
import de.thlemm.householdorganizer.service.InterestService;
import de.thlemm.householdorganizer.service.TransactionService;
import de.thlemm.householdorganizer.service.exception.TransactionServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Random;

@Service
public class CasinoCodeServiceImpl implements CasinoCodeService {

    @Autowired
    CasinoCodeRepository casinoCodeRepository;

    @Override
    public CasinoCode createNewCode() {
        CasinoCode casinoCode = new CasinoCode();
        String code;
        do {
            code = generateRandomDigits(6);
        } while (casinoCodeRepository.existsByCode(code));

        casinoCode.setCode(code);

        casinoCodeRepository.save(casinoCode);

        return casinoCode;
    }

    private static String generateRandomDigits(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10)); // Generates a random number between 0 and 9
        }

        return sb.toString();
    }

}
