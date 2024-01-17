package edu.tcu.cs.hogwartsartifactsonline.artifact.convertor;

import edu.tcu.cs.hogwartsartifactsonline.artifact.Artifact;
import edu.tcu.cs.hogwartsartifactsonline.artifact.dto.ArtifactDto;
import edu.tcu.cs.hogwartsartifactsonline.wizard.convertor.WizardToWizardDtoConvertor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ArtifactToArtifactDtoConvertor implements Converter<Artifact, ArtifactDto> {

    private final WizardToWizardDtoConvertor wizardToWizardDtoConvertor;

    public ArtifactToArtifactDtoConvertor(WizardToWizardDtoConvertor wizardToWizardDtoConvertor) {
        this.wizardToWizardDtoConvertor = wizardToWizardDtoConvertor;
    }

    @Override
    public ArtifactDto convert(Artifact source) {
        return new ArtifactDto(source.getId(),
                source.getName(),
                source.getDescription(),
                source.getImageUrl(),
                source.getOwner() != null
                        ? this.wizardToWizardDtoConvertor.convert(source.getOwner())
                        : null);
    }
}
