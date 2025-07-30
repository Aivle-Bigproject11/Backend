package aivlebigproject.infra;

import aivlebigproject.domain.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

@Component
public class TributeHateoasProcessor
    implements RepresentationModelProcessor<EntityModel<Tribute>> {

    @Override
    public EntityModel<Tribute> process(EntityModel<Tribute> model) {
        return model;
    }
}
