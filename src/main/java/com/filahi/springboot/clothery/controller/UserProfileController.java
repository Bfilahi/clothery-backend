package com.filahi.springboot.clothery.controller;

import com.filahi.springboot.clothery.dto.UserProfileDTO;
import com.filahi.springboot.clothery.entity.Customer;
import com.filahi.springboot.clothery.service.IAuth0UserSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserProfileController {
    private final IAuth0UserSyncService auth0UserSyncService;

    @Autowired
    public UserProfileController(IAuth0UserSyncService auth0UserSyncService) {
        this.auth0UserSyncService = auth0UserSyncService;
    }

    @PostMapping("/sync")
    public ResponseEntity<Map<String, Object>> syncUserProfile(@RequestBody UserProfileDTO userProfile) {
        try {
            Customer customer = auth0UserSyncService.syncUser(
                    userProfile.getAuth0Id(),
                    userProfile.getEmail(),
                    userProfile.getFirstName(),
                    userProfile.getLastName()
            );

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("customerId", customer.getId());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "error");
            response.put("message", e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
