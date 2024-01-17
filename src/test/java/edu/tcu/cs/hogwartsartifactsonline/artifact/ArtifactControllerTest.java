package edu.tcu.cs.hogwartsartifactsonline.artifact;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tcu.cs.hogwartsartifactsonline.artifact.dto.ArtifactDto;
import edu.tcu.cs.hogwartsartifactsonline.system.StatusCode;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class ArtifactControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ArtifactService artifactService;

    @Autowired
    ObjectMapper objectMapper;
    StatusCode statusCode;

    List<Artifact> artifacts;

    @BeforeEach
    void setUp() {
        this.artifacts = new ArrayList<>();

        Artifact artifact1 = new Artifact();
        artifact1.setId("1");
        artifact1.setName("Invisibility Cloak");
        artifact1.setDescription("Makes the wizard invisible to others.");
        artifact1.setImageUrl("randomURL");

        Artifact artifact2 = new Artifact();
        artifact2.setId("2");
        artifact2.setName("Elder wand");
        artifact2.setDescription("Makes the wizard invisible to others.");
        artifact2.setImageUrl("randomURL");

        Artifact artifact3 = new Artifact();
        artifact3.setId("3");
        artifact3.setName("Remembrall");
        artifact3.setDescription("Makes the wizard invisible to others.");
        artifact3.setImageUrl("randomURL");

        Artifact artifact4 = new Artifact();
        artifact4.setId("4");
        artifact4.setName("Alohomora");
        artifact4.setDescription("Makes the wizard invisible to others.");
        artifact4.setImageUrl("randomURL");

        Artifact artifact5 = new Artifact();
        artifact5.setId("5");
        artifact5.setName("Gringotts");
        artifact5.setDescription("Makes the wizard invisible to others.");
        artifact5.setImageUrl("randomURL");

        Artifact artifact6 = new Artifact();
        artifact6.setId("6");
        artifact6.setName("Owl");
        artifact6.setDescription("Makes the wizard invisible to others.");
        artifact6.setImageUrl("randomURL");

        this.artifacts.add(artifact1);
        this.artifacts.add(artifact2);
        this.artifacts.add(artifact3);
        this.artifacts.add(artifact4);
        this.artifacts.add(artifact5);
        this.artifacts.add(artifact6);

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindArtifactByIdSuccess() throws Exception {
        // Given

        given(this.artifactService.findById("2")).willReturn(this.artifacts.get(1));

        // When
         this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/artifacts/2").accept(MediaType.APPLICATION_JSON))
                 .andExpect(jsonPath("$.flag").value(true))
                 .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                 .andExpect(jsonPath("$.message").value("Find One Success"))
                 .andExpect(jsonPath("$.data.id").value("2"))
                 .andExpect(jsonPath("$.data.name").value("Elder wand"));
        // Then
    }

    @Test
    void testFindArtifactByIdNotFound() throws Exception {
        // Given

        given(this.artifactService.findById("2")).willThrow(new ArtifactNotFoundException("2"));

        // When
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/artifacts/2").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find artifact with Id 2"))
                .andExpect(jsonPath("$.data").isEmpty());

        // Then
    }

    @Test
    void testFindAllArtifactsSuccess() throws Exception {
        // Given
        given(this.artifactService.findAll()).willReturn(this.artifacts);
        // When
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/artifacts").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data").value(Matchers.hasSize(this.artifacts.size())))
                .andExpect(jsonPath("$.data[0].id").value("1"))
                .andExpect(jsonPath("$.data[0].name").value("Invisibility Cloak"))
                .andExpect(jsonPath("$.data[1].id").value("2"))
                .andExpect(jsonPath("$.data[1].name").value("Elder wand"));
        // Then
    }

    @Test
    void testAddArtifactSuccess() throws Exception {
        // Given
        ArtifactDto artifactDto = new ArtifactDto(null,
                    "Remembr",
                    "helps to remember forgotten things",
                    "randomURL",
                    null);
        String json = this.objectMapper.writeValueAsString(artifactDto);
        Artifact savedArtifact = new Artifact();
        savedArtifact.setId("8");
        savedArtifact.setName("New Artifact");
        savedArtifact.setDescription("Desc. of new Artifact");
        savedArtifact.setImageUrl("RandomURL");

        given(this.artifactService.save(Mockito.any(Artifact.class))).willReturn(savedArtifact);



        // When and then
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/artifacts").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value(savedArtifact.getName()))
                .andExpect(jsonPath("$.data.description").value(savedArtifact.getDescription()))
                .andExpect(jsonPath("$.data.imageUrl").value(savedArtifact.getImageUrl()));
    }

    @Test
    void testUpdateArtifactSuccess() throws Exception {
        // given
        ArtifactDto artifactDto = new ArtifactDto("1",
                "Invisibility Cloak",
                "hides person",
                "randomURL",
                null);
        String json = this.objectMapper.writeValueAsString(artifactDto);
        Artifact updatedArtifact = new Artifact();
        updatedArtifact.setId("1");
        updatedArtifact.setName("Invisibility Cloak");
        updatedArtifact.setDescription("hides person");
        updatedArtifact.setImageUrl("RandomURL");

        given(this.artifactService.update(eq("1"), Mockito.any(Artifact.class))).willReturn(updatedArtifact);



        // When and then
        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/artifacts/1").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.id").value(updatedArtifact.getId()))
                .andExpect(jsonPath("$.data.name").value(updatedArtifact.getName()))
                .andExpect(jsonPath("$.data.description").value(updatedArtifact.getDescription()))
                .andExpect(jsonPath("$.data.imageUrl").value(updatedArtifact.getImageUrl()));


        // when and then
    }

    @Test
    void testUpdateArtifactErrorWithNonExistentId() throws Exception {

        ArtifactDto artifactDto = new ArtifactDto("1",
                "Invisibility Cloak",
                "hides person",
                "randomURL",
                null);
        String json = this.objectMapper.writeValueAsString(artifactDto);


        given(this.artifactService.update(eq("1"), Mockito.any(Artifact.class))).willThrow(new ArtifactNotFoundException("1"));



        // When and then
        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/artifacts/1").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find artifact with Id 1"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteArtifactSuccess() throws Exception {
        // given
        doNothing().when(this.artifactService).delete("123");
        // when and then
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/artifacts/123").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testDeleteArtifactErrorWithNonExistentId() throws Exception {
        // given
        doThrow(new ArtifactNotFoundException("123")).when(this.artifactService).delete("123");
        // when and then
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/artifacts/123").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find artifact with Id 123"))
                .andExpect(jsonPath("$.data").isEmpty());
    }
}