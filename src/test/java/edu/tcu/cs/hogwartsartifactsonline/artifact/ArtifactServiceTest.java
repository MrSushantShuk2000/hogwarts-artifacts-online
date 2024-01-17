package edu.tcu.cs.hogwartsartifactsonline.artifact;

import edu.tcu.cs.hogwartsartifactsonline.artifact.utils.IdWorker;
import edu.tcu.cs.hogwartsartifactsonline.wizard.Wizard;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.assertj.core.api.Assertions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArtifactServiceTest {
    @Mock
    ArtifactRepository artifactRepository;

    @Mock
    IdWorker idWorker;
    @InjectMocks
    ArtifactService artifactService;

    List<Artifact> artifacts;
    @BeforeEach
    void setUp() {
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
        this.artifacts = new ArrayList<>();
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
    void testFindByIdSuccess() {
        // Given
        Artifact artifact = new Artifact();
        artifact.setId("123");
        artifact.setName("Invisibility Cloak");
        artifact.setDescription("Makes the wizard invisible to others.");
        artifact.setImageUrl("randomURL");

        Wizard wizard = new Wizard();
        wizard.setId(2);
        wizard.setName("Harry Potter");
        artifact.setOwner(wizard);

        given(artifactRepository.findById("123")).willReturn(Optional.of(artifact));
        // When
        Artifact returnedArtifact =  artifactService.findById("123");
        // Then
        Assertions.assertThat(returnedArtifact.getId()).isEqualTo(artifact.getId());
        Assertions.assertThat(returnedArtifact.getName()).isEqualTo(artifact.getName());
        Assertions.assertThat(returnedArtifact.getDescription()).isEqualTo(artifact.getDescription());
        Assertions.assertThat(returnedArtifact.getImageUrl()).isEqualTo(artifact.getImageUrl());
        verify(artifactRepository, times(1)).findById("123");



    }

    @Test
    void testFindByIdNotFound(){
        // Given
        given(artifactRepository.findById(Mockito.anyString())).willReturn(Optional.empty());
        // When
        Throwable thrown = catchThrowable(()->{
            Artifact returnedArtifact = artifactService.findById("123");
        });
        // Then
        assertThat(thrown)
                .isInstanceOf(ArtifactNotFoundException.class)
                .hasMessage("Could not find artifact with Id 123");
        verify(artifactRepository, times(1)).findById("123");
    }

    @Test
    void testFindAllSuccess(){
        // Given
        given(artifactRepository.findAll()).willReturn(this.artifacts);
        // When
       List<Artifact> actualArtifacts =  artifactService.findAll();
        // Then
        assertThat(actualArtifacts.size()).isEqualTo(this.artifacts.size());
        verify(artifactRepository, times(1)).findAll();
    }

    @Test
    void testSaveSuccess(){
        // Given
        Artifact newArtifact = new Artifact();
        newArtifact.setName("Artifact 7");
        newArtifact.setDescription("Description...");
        newArtifact.setImageUrl("ImageUrl...");

        given(idWorker.nextId()).willReturn(123456L);
        given(artifactRepository.save(newArtifact)).willReturn(newArtifact);

        // When
        Artifact savedArtifact = artifactService.save(newArtifact);

        // Then
        assertThat(savedArtifact.getId()).isEqualTo("123456");
        assertThat(savedArtifact.getName()).isEqualTo(newArtifact.getName());
        assertThat(savedArtifact.getDescription()).isEqualTo(newArtifact.getDescription());
        assertThat(savedArtifact.getImageUrl()).isEqualTo(newArtifact.getImageUrl());
        verify(artifactRepository, times(1)).save(newArtifact);
    }

    @Test
    void testUpdateSuccess(){
        // Given
        Artifact oldArtifact = new Artifact();
        oldArtifact.setId("123");
        oldArtifact.setName("Invisibility Cloak");
        oldArtifact.setDescription("Makes the wizard invisible to others.");
        oldArtifact.setImageUrl("randomURL");

        Artifact update = new Artifact();
        update.setId(oldArtifact.getId());
        update.setName("Invisibility Cloak");
        update.setDescription("New Description");
        update.setImageUrl("randomURL");

        given(artifactRepository.findById("123")).willReturn(Optional.of(oldArtifact));
        given(artifactRepository.save(oldArtifact)).willReturn(oldArtifact);
        // When
        Artifact updatedArtifact = artifactService.update("123", update);
        // Then
        assertThat(updatedArtifact.getId()).isEqualTo(update.getId());
        assertThat(updatedArtifact.getDescription()).isEqualTo(update.getDescription());

        verify(artifactRepository, times(1)).findById("123");
        verify(artifactRepository, times(1)).save(oldArtifact);
    }

    @Test
    void testUpdateNotFound(){
        // given
        Artifact update = new Artifact();
        update.setName("Invisibility Cloak");
        update.setDescription("New Description");
        update.setImageUrl("randomURL");

        given(artifactRepository.findById("9999")).willReturn(Optional.empty());
        // when
        assertThrows(ArtifactNotFoundException.class, ()->{
            artifactService.update("9999", update);
        });
        // then
        verify(artifactRepository, times(1)).findById("9999");
    }

    @Test
    void testDeleteSuccess(){
        // given
        Artifact artifact = new Artifact();
        artifact.setId("123");
        artifact.setName("Invisibility Cloak");
        artifact.setDescription("Makes the wizard invisible to others.");
        artifact.setImageUrl("randomURL");

        given(artifactRepository.findById("123")).willReturn(Optional.of(artifact));
        doNothing().when(artifactRepository).deleteById("123");
        // when
        artifactService.delete("123");
        // then
        verify(artifactRepository, times(1)).deleteById("123");

    }

    @Test
    void testDeleteNotFound(){
        // given
        given(artifactRepository.findById("123")).willReturn(Optional.empty());
        // when
        assertThrows(ArtifactNotFoundException.class, ()->{
            artifactService.delete("123");
        });
        // then
        verify(artifactRepository, times(1)).findById("123");

    }

}