package aivlebigproject.infra;

import aivlebigproject.domain.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

@Component
public class MemorialHateoasProcessor
    implements RepresentationModelProcessor<EntityModel<Memorial>> {

    @Override
    public EntityModel<Memorial> process(EntityModel<Memorial> model) {
        return model;
    }
}
