package id.ac.ui.cs.advprog.authgateway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthController {

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestParam(name = "uid") String uid) {
        // TODO: Handle Logout
        return ResponseEntity.ok("Dari auth-gateway");
    }
}
