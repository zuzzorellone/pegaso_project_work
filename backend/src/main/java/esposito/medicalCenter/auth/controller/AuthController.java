package esposito.medicalCenter.auth.controller;

import esposito.medicalCenter.auth.controller.dto.LoginRequestDTO;
import esposito.medicalCenter.auth.controller.dto.LoginResponseDTO;
import esposito.medicalCenter.auth.controller.jwt.JwtUtils;
import esposito.medicalCenter.staff.api.StaffFacade;
import esposito.medicalCenter.staff.api.RequestStaffDTO;
import esposito.medicalCenter.staff.api.ResponseStaffDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Autenticazione", description = "Login e registrazione dello staff (medici e admin)")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtil;
    private final StaffFacade staffFacade;

    @Operation(summary = "Login staff", description = "Controlla email e password e restituisce un token JWT valido 10 ore, da usare come Bearer token nelle chiamate successive.")
    @ApiResponse(responseCode = "200", description = "Login riuscito, token restituito")
    @ApiResponse(responseCode = "401", description = "Credenziali non valide")
    @ApiResponse(responseCode = "403", description = "Account disabilitato dall'admin")
    @SecurityRequirements
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password())
            );
        } catch (DisabledException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Account disabilitato");
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenziali non valide");
        }

        UserDetails staff = (UserDetails) authentication.getPrincipal();
        String role = staff.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");
        String token = jwtUtil.generateToken(staff.getUsername(), role);

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @Operation(summary = "Registra un nuovo medico o admin", description = "Richiede un token con ruolo ADMIN. Se il ruolo è DOCTOR va specificato anche il reparto (medicalExaminationTypeId).")
    @ApiResponse(responseCode = "201", description = "Staff registrato con successo")
    @ApiResponse(responseCode = "403", description = "Il chiamante non ha ruolo ADMIN")
    @ApiResponse(responseCode = "409", description = "Esiste già un account con questa email")
    @PostMapping("/register")
    public ResponseEntity<ResponseStaffDTO> register(@Valid @RequestBody RequestStaffDTO requestStaffDTO) {
        ResponseStaffDTO response = staffFacade.registerStaff(requestStaffDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
