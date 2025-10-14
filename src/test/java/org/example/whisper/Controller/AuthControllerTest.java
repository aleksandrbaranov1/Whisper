package org.example.whisper.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.whisper.DTO.LoginRequest;
import org.example.whisper.DTO.UserRegistrationDTO;
import org.example.whisper.Security.MyUserDetails;
import org.example.whisper.Security.MyUserDetailsService;
import org.example.whisper.Service.EmailVerificationService;
import org.example.whisper.Service.JwtService;
import org.example.whisper.Service.RegistrationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RegistrationService registrationService;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private EmailVerificationService emailVerificationService;

    @MockitoBean
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void testRegister() throws Exception {
        UserRegistrationDTO dto = new UserRegistrationDTO();
        dto.setName("User");
        dto.setEmail("user@gmail.com");
        dto.setPassword("user");
        dto.setRole("USER");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "name": "User",
                            "email": "user@gmail.com",
                            "password": "user",
                            "role": "USER"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered successfully"));
        verify(registrationService, times(1)).register(any(UserRegistrationDTO.class));
    }

    @Test
    public void testLoginSuccess() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("user@gmail.com");
        loginRequest.setPassword("password");

        Authentication authentication = Mockito.mock(Authentication.class);
        MyUserDetails myUserDetails = Mockito.mock(MyUserDetails.class);

        Mockito.when(authentication.getPrincipal()).thenReturn(myUserDetails);
        Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        Mockito.when(jwtService.generateToken(myUserDetails)).thenReturn("mocked-jwt-token");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mocked-jwt-token"));

    }

    @Test
    public void testLoginFailed() throws Exception{
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("user@gmail.com");
        loginRequest.setPassword("wrongpassword");

        Authentication authentication = Mockito.mock(Authentication.class);
        MyUserDetails myUserDetails = Mockito.mock(MyUserDetails.class);

        Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Bad credentials"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Authentication failed: Bad credentials"));

    }

    @Test
    public void testRequestRegistration() throws Exception{
        UserRegistrationDTO request = new UserRegistrationDTO();
        request.setName("user");
        request.setEmail("user@gmail.com");
        request.setPassword("user");
        request.setRole("USER");

        Mockito.doNothing().when(emailVerificationService).sendVerificationCode(request.getEmail());

        mockMvc.perform(post("/api/auth/register/request")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Код отправлен на почту"));
    }
//    @PostMapping("/register/request")
//    public ResponseEntity<?> requestRegistration(@RequestBody UserRegistrationDTO request) {
//        emailVerificationService.sendVerificationCode(request.getEmail());
//        return ResponseEntity.ok("Код отправлен на почту");
//    }
}
