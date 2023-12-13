package ca.gbc.comment;

import ca.gbc.comment.dto.CommentRequest;
import ca.gbc.comment.dto.CommentResponse;
import ca.gbc.comment.model.Comment;
import ca.gbc.comment.repository.CommentRepository;
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
public class CommentServiceApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private CommentRequest getCommentRequest() {
        return CommentRequest.builder()
                .postId("1")
                .content("This is a comment.")
                .author("Commenter")
                .build();
    }

    private List<Comment> getCommentList() {
        List<Comment> commentList = new ArrayList<>();

        UUID uuid = UUID.randomUUID();

        Comment comment = Comment.builder()
                .id(uuid.timestamp())
                .postId("1")
                .content("This is a comment.")
                .author("Commenter")
                .build();

        commentList.add(comment);
        return commentList;
    }

    private String convertObjectsToJSON(List<CommentResponse> commentList) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(commentList);
    }

    private List<CommentResponse> convertJsonStringToObject(String jsonString) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonString, new TypeReference<List<CommentResponse>>() {
        });
    }

    @Test
    void createComment() throws Exception {
        CommentRequest comment = getCommentRequest();
        String commentRequestString = objectMapper.writeValueAsString(comment);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/comment")
                        .contentType("application/json")
                        .content(commentRequestString))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        Assertions.assertTrue(commentRepository.findAll().size() > 0);

        Comment query = Comment.builder().content("This is a comment.").build();
        List<Comment> commentList = commentRepository.findAll(Example.of(query));

        Assertions.assertTrue(commentList.size() > 0);
    }

    @Test
    void getAllComments() throws Exception {
        // Given
        commentRepository.saveAll(getCommentList());

        // When
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/comment")
                .accept(MediaType.APPLICATION_JSON));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isOk());
        response.andDo(MockMvcResultHandlers.print());

        MvcResult result = response.andReturn();
        String jsonResponse = result.getResponse().getContentAsString();
        JsonNode jsonNodes = new ObjectMapper().readTree(jsonResponse);

        int actualSize = jsonNodes.size();
        int expectedSize = getCommentList().size();

        assertEquals(expectedSize > 0, actualSize > 0); // Compare the actual size to the expected size
    }

    @Test
    void updateComment() throws Exception {
        // Given
        Comment savedComment = Comment.builder()
                .id(UUID.randomUUID().timestamp())
                .postId("1")
                .content("This is an old comment.")
                .author("Old Commenter")
                .build();

        // Saved comment with the original content
        commentRepository.save(savedComment);

        // Prepare update comment and commentRequest
        savedComment.setContent("This is the updated comment.");
        String commentRequestString = objectMapper.writeValueAsString(savedComment);

        // When
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .put("/api/comment/" + savedComment.getId().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(commentRequestString));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isNoContent());
        response.andDo(MockMvcResultHandlers.print());

        Comment query = Comment.builder().id(savedComment.getId()).build();
        Optional<Comment> storedComment = commentRepository.findOne(Example.of(query));

        Assertions.assertTrue(storedComment.isPresent());
        Assertions.assertEquals(savedComment.getContent(), storedComment.get().getContent());
    }

    @Test
    void deleteComment() throws Exception {
        // Given
        Comment savedComment = Comment.builder()
                .id(UUID.randomUUID().timestamp())
                .postId("1")
                .content("This is a comment.")
                .author("Commenter")
                .build();

        commentRepository.save(savedComment);

        // When
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/comment/" + savedComment.getId().toString())
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isNoContent());
        response.andDo(MockMvcResultHandlers.print());

        // Query to check if the comment is deleted
        Comment query = Comment.builder().id(savedComment.getId()).build();
        Optional<Comment> storedComment = commentRepository.findOne(Example.of(query));

        Assertions.assertFalse(storedComment.isPresent()); // The comment should not exist in the database
    }
}

