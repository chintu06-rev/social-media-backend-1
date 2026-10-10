package com.socialsync.controller;

import com.socialsync.dto.ConnectAccountRequest;
import com.socialsync.dto.SocialAccountDto;
import com.socialsync.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@CrossOrigin(origins = "*")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public ResponseEntity<List<SocialAccountDto>> getAccounts(@RequestParam(required = false) Long userId) {
        return ResponseEntity.ok(accountService.getAccounts(userId));
    }

    @PostMapping("/connect")
    public ResponseEntity<SocialAccountDto> connectAccount(@Valid @RequestBody ConnectAccountRequest request) {
        SocialAccountDto created = accountService.connectAccount(request);
        return ResponseEntity.ok(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> disconnectAccount(@PathVariable Long id) {
        accountService.disconnectAccount(id);
        return ResponseEntity.noContent().build();
    }
}
