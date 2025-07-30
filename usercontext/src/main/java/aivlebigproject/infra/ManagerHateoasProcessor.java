package aivlebigproject.infra;

import aivlebigproject.domain.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

@Component
public class ManagerHateoasProcessor
    implements RepresentationModelProcessor<EntityModel<Manager>> {

    @Override
    public EntityModel<Manager> process(EntityModel<Manager> model) {
        return model;
    }
}
