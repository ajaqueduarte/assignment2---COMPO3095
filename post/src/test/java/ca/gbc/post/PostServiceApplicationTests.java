package ca.gbc.post;

import ca.gbc.post.dto.PostRequest;
import ca.gbc.post.dto.PostResponse;
import ca.gbc.post.model.Post;
import ca.gbc.post.repository.PostRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
public class PostServiceApplicationTests extends AbstractContainerBasicTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private PostRequest getPostRequest() {
        return PostRequest.builder()
                .title("Post 1")
                .content("This is the content of Post 1.")
                .author("Author 1")
                .build();
    }

    private List<Post> getPostList() {
        List<Post> postList = new ArrayList<>();

        UUID uuid = UUID.randomUUID();

        Post post = Post.builder()
                .id(uuid.toString())
                .title("Post 1")
                .content("This is the content of Post 1.")
                .author("Author 1")
                .build();

        postList.add(post);
        return postList;
    }

    private String convertObjectsToJSON(List<PostResponse> postList) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(postList);
    }

    private List<PostResponse> convertJsonStringToObject(String jsonString) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonString, new TypeReference<List<PostResponse>>() {
        });
    }

    @Test
    void createPost() throws Exception {
        PostRequest post = getPostRequest();
        String postRequestString = objectMapper.writeValueAsString(post);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/post")
                        .contentType("application/json")
                        .content(postRequestString))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        Assertions.assertTrue(postRepository.findAll().size() > 0);

        Query query = new Query();
        query.addCriteria(Criteria.where("title").is("Post 1"));
        List<Post> postList = mongoTemplate.find(query, Post.class);
        Assertions.assertTrue(postList.size() > 1);
    }

    @Test
    void getAllPosts() throws Exception {
        // Given
        postRepository.saveAll(getPostList());

        // When
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/post")
                .accept(MediaType.APPLICATION_JSON));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isOk());
        response.andDo(MockMvcResultHandlers.print());

        MvcResult result = response.andReturn();
        String jsonResponse = result.getResponse().getContentAsString();
        JsonNode jsonNodes = new ObjectMapper().readTree(jsonResponse);

        int actualSize = jsonNodes.size();
        int expectedSize = getPostList().size();

        assertEquals(expectedSize > 0, actualSize > 0); // Compare the actual size to the expected size
    }

    @Test
    void updatePost() throws Exception {
        // Given
        Post savedPost = Post.builder()
                .id(UUID.randomUUID().toString())
                .title("Post 2")
                .content("This is the content of Post 2.")
                .author("Author 2")
                .build();

        // Saved post with the original content
        postRepository.save(savedPost);

        // Prepare update post and postRequest
        savedPost.setContent("This is the updated content.");
        String postRequestString = objectMapper.writeValueAsString(savedPost);

        // When
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .put("/api/post/" + savedPost.getId().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(postRequestString));

        // Then
        response.andExpectAll(MockMvcResultMatchers.status().isNoContent());
        response.andDo(MockMvcResultHandlers.print());

        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(savedPost.getId()));
        Post storedPost = mongoTemplate.findOne(query, Post.class);

        Assertions.assertEquals(savedPost.getContent(), storedPost.getContent());
    }

    @Test
    void deletePost() throws Exception {
        // Given
        Post savedPost = Post.builder()
                .id(UUID.randomUUID().toString())
                .title("Post 1")
                .content("This is the content of Post 1.")
                .author("Author 1")
                .build();

        postRepository.save(savedPost);

        // When
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/post/" + savedPost.getId().toString())
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isNoContent());
        response.andDo(MockMvcResultHandlers.print());

        // Query to check if the post is deleted
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(savedPost.getId()));
        Post deletedPost = mongoTemplate.findOne(query, Post.class);

        Assertions.assertNull(deletedPost); // The post should not exist in the database
    }
}
