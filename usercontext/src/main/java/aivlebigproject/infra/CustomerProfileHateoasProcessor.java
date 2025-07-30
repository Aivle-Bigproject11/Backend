package aivlebigproject.infra;

import aivlebigproject.domain.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

@Component
public class CustomerProfileHateoasProcessor
    implements RepresentationModelProcessor<EntityModel<CustomerProfile>> {

    @Override
    public EntityModel<CustomerProfile> process(
        EntityModel<CustomerProfile> model
    ) {
        return model;
    }
}
