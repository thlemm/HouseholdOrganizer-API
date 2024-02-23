package de.thlemm.householdorganizer.controller;

import de.thlemm.householdorganizer.controller.request.CheckCasinoCodeRequest;
import de.thlemm.householdorganizer.controller.response.MessageResponse;
import de.thlemm.householdorganizer.model.CasinoCode;
import de.thlemm.householdorganizer.repository.CasinoCodeRepository;
import de.thlemm.householdorganizer.service.CasinoCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v2")
public class CasinoController {

    @Autowired
    CasinoCodeRepository casinoCodeRepository;
    @Autowired
    CasinoCodeService casinoCodeService;

    @PostMapping("/code/check")
    public ResponseEntity<?> checkCode(@Valid @RequestBody CheckCasinoCodeRequest checkCasinoCodeRequest) {

        CasinoCode casinoCode = casinoCodeRepository.findByCode(checkCasinoCodeRequest.getCode());
        if (casinoCode==null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(new MessageResponse("true"));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/code/new")
    public ResponseEntity<?> newCode() {
        CasinoCode casinoCode = casinoCodeService.createNewCode();
        return ResponseEntity.ok(new MessageResponse(casinoCode.getCode()));
    }
}
