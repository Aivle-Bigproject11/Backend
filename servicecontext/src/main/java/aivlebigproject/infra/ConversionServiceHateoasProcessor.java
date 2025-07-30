package aivlebigproject.infra;

import aivlebigproject.domain.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

@Component
public class ConversionServiceHateoasProcessor
    implements RepresentationModelProcessor<EntityModel<ConversionService>> {

    @Override
    public EntityModel<ConversionService> process(
        EntityModel<ConversionService> model
    ) {
        return model;
    }
}
