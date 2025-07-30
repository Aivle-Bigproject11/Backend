package aivlebigproject.infra;

import aivlebigproject.domain.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

@Component
public class FamilyHateoasProcessor
    implements RepresentationModelProcessor<EntityModel<Family>> {

    @Override
    public EntityModel<Family> process(EntityModel<Family> model) {
        model.add(
            Link
                .of(model.getRequiredLink("self").getHref() + "/approvefamily")
                .withRel("approvefamily")
        );

        return model;
    }
}
