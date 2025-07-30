package aivlebigproject.infra;

import aivlebigproject.domain.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

@Component
public class ObituaryHateoasProcessor
    implements RepresentationModelProcessor<EntityModel<Obituary>> {

    @Override
    public EntityModel<Obituary> process(EntityModel<Obituary> model) {
        return model;
    }
}
