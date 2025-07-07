package software.pxel.pxelsoftwaretestcase.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import software.pxel.pxelsoftwaretestcase.model.dto.PhoneCreateDeleteDto;
import software.pxel.pxelsoftwaretestcase.model.dto.PhoneUpdateDto;
import software.pxel.pxelsoftwaretestcase.model.entity.PhoneData;
import software.pxel.pxelsoftwaretestcase.model.entity.User;
import software.pxel.pxelsoftwaretestcase.repository.PhoneDataRepository;
import software.pxel.pxelsoftwaretestcase.repository.UserRepository;
import software.pxel.pxelsoftwaretestcase.security.AppUserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@Transactional
class UserPhoneControllerIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PhoneDataRepository phoneDataRepository;

    private User testUser;

    private AppUserDetails testUserDetails;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        testUser = new User();
        testUser.setName("testuser");
        testUser.setPassword("password");
        testUser = userRepository.save(testUser);

        PhoneData phoneData1 = new PhoneData();
        phoneData1.setPhone("79990000000");
        phoneData1.setUser(testUser);
        testUser.addPhone(phoneData1);
        PhoneData phoneData2 = new PhoneData();
        phoneData2.setPhone("79990000001");
        phoneData2.setUser(testUser);
        testUser.addPhone(phoneData2);
        userRepository.save(testUser);

        testUserDetails = new AppUserDetails(testUser);
    }

    private String toJson(Object obj) throws Exception {
        return objectMapper.writeValueAsString(obj);
    }


    private RequestPostProcessor userAuth() {
        return user(testUserDetails);
    }

    @Test
    void testAddPhoneSuccess() throws Exception {
        var dto = new PhoneCreateDeleteDto("79991112233");

        mockMvc.perform(post("/api/users/phones")
                        .with(userAuth())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(dto)))
                .andExpect(status().isCreated());

        assertTrue(phoneDataRepository.existsByPhone("79991112233"));
    }

    @Test
    void testAddPhoneAlreadyExists() throws Exception {
        var dto = new PhoneCreateDeleteDto("79990000000"); // Уже есть

        mockMvc.perform(post("/api/users/phones")
                        .with(userAuth())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(dto)))
                .andExpect(status().isBadRequest()); // Предполагается, что контроллер обрабатывает IllegalArgumentException и возвращает 400
    }

    @Test
    void testUpdatePhoneSuccess() throws Exception {
        var dto = new PhoneUpdateDto("79990000000", "79992223344");

        mockMvc.perform(put("/api/users/phones")
                        .with(userAuth())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(dto)))
                .andExpect(status().isOk());

        assertFalse(phoneDataRepository.existsByPhone("79990000000"));
        assertTrue(phoneDataRepository.existsByPhone("79992223344"));
    }

    @Test
    void testUpdatePhoneOldPhoneNotFound() throws Exception {
        var dto = new PhoneUpdateDto("70000000000", "79992223344");

        mockMvc.perform(put("/api/users/phones")
                        .with(userAuth())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDeletePhoneSuccess() throws Exception {


        var dto = new PhoneCreateDeleteDto("79990000000");

        mockMvc.perform(delete("/api/users/phones")
                        .with(userAuth())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(dto)))
                .andExpect(status().isNoContent());

        assertFalse(phoneDataRepository.existsByPhone("79990000000"));
    }

    @Test
    void testDeletePhoneLastPhoneThrows() throws Exception {


        var dto1 = new PhoneCreateDeleteDto("79990000001");
        var dto2 = new PhoneCreateDeleteDto("79990000001");

        mockMvc.perform(delete("/api/users/phones")
                        .with(userAuth())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(dto1)))
                .andExpect(status().isNoContent());

        mockMvc.perform(delete("/api/users/phones")
                        .with(userAuth())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(dto2)))
                .andExpect(status().isBadRequest());
    }
}