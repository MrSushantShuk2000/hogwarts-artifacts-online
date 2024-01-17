package edu.tcu.cs.hogwartsartifactsonline.system;

import edu.tcu.cs.hogwartsartifactsonline.artifact.Artifact;
import edu.tcu.cs.hogwartsartifactsonline.artifact.ArtifactRepository;
import edu.tcu.cs.hogwartsartifactsonline.wizard.Wizard;
import edu.tcu.cs.hogwartsartifactsonline.wizard.WizardRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DBDataInitializer implements CommandLineRunner {

    private final ArtifactRepository artifactRepository;
    private final WizardRepository wizardRepository;

    public DBDataInitializer(ArtifactRepository artifactRepository, WizardRepository wizardRepository) {
        this.artifactRepository = artifactRepository;
        this.wizardRepository = wizardRepository;
    }

    @Override
    public void run(String... args) throws Exception {
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

        Wizard wizard1 = new Wizard();
        wizard1.setId(1);
        wizard1.setName("Albus Dumbledore");
        wizard1.addArtifact(artifact1);
        wizard1.addArtifact(artifact2);

        Wizard wizard2 = new Wizard();
        wizard2.setId(2);
        wizard2.setName("Harry Potter");
        wizard2.addArtifact(artifact3);
        wizard2.addArtifact(artifact4);
        wizard2.addArtifact(artifact5);

        Wizard wizard3 = new Wizard();
        wizard3.setId(3);
        wizard3.setName("Ron Weasley");


        wizardRepository.save(wizard1);
        wizardRepository.save(wizard2);
        wizardRepository.save(wizard3);

        artifactRepository.save(artifact6);

    }
}
