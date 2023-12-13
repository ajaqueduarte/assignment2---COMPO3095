package ca.gbc.user;

import ca.gbc.user.dto.UserRequest;
import ca.gbc.user.dto.UserResponse;
import ca.gbc.user.model.User;
import ca.gbc.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
public class UserServiceApplicationTests extends AbstractContainerBasicTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private UserRequest getUserRequest() {
        return UserRequest.builder()
                .username("user 1")
                .email("user1@gmail.com")
                .password("user123")
                .build();
    }

    private List<User> getUserList() {
        List<User> userList = new ArrayList<>();
        User user = User.builder()
                .id(UUID.randomUUID().timestamp())
                .username("user 1")
                .email("user1@gmail.com")
                .password("user123")
                .build();
        userList.add(user);
        return userList;
    }

    private String convertObjectsToJSON(List<UserResponse> productList) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(productList);
    }

    private List<UserResponse> convertJsonStringToObject(String jsonString) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonString, new TypeReference<List<UserResponse>>() {
        });
    }
    @Test
    void createUser() throws Exception {
        UserRequest user = getUserRequest();
        String userRequestString = objectMapper.writeValueAsString(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users")  // Updated the URL
                        .contentType("application/json")
                        .content(userRequestString))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        Assertions.assertFalse(userRepository.findAll().isEmpty());

        User query = User.builder().username("user 1").build();
        List<User> userList = userRepository.findAll(Example.of(query));
        Assertions.assertFalse(userList.isEmpty());
    }

    @Test
    void getAllUser() throws Exception {
        userRepository.saveAll(getUserList());

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/users")  // Updated the URL
                .accept(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk());
        response.andDo(MockMvcResultHandlers.print());

        MvcResult result = response.andReturn();
        String jsonResponse = result.getResponse().getContentAsString();
        JsonNode jsonNodes = new ObjectMapper().readTree(jsonResponse);

        assertEquals(!getUserList().isEmpty(), jsonNodes.size() > 0);
    }

    @Test
    void updateUser() throws Exception {
        User savedUser = userRepository.save(User.builder()
                .id(UUID.randomUUID().timestamp())
                .username("user 2")
                .email("user2@gmail.com")
                .password("123user")
                .build());

        savedUser.setPassword("newPassword");
        String userRequestString = objectMapper.writeValueAsString(savedUser);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/users/" + savedUser.getId())  // Updated the URL
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRequestString))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        User query = User.builder().id(savedUser.getId()).build();
        Optional<User> storedUser = userRepository.findOne(Example.of(query));

        Assertions.assertTrue(storedUser.isPresent());
        Assertions.assertEquals("newPassword", storedUser.get().getPassword());
    }

    @Test
    void deleteUser() throws Exception {
        User savedUser = userRepository.save(User.builder()
                .id(UUID.randomUUID().timestamp())
                .username("user 3")
                .email("user3@gmail.com")
                .password("user123")
                .build());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/users/" + savedUser.getId())  // Updated the URL
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        User query = User.builder().id(savedUser.getId()).build();
        Long userCount = userRepository.count(Example.of(query));

        assertEquals(0, userCount);
    }
}
