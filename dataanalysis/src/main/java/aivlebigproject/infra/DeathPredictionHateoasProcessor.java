package aivlebigproject.infra;

import aivlebigproject.domain.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

@Component
public class DeathPredictionHateoasProcessor
    implements RepresentationModelProcessor<EntityModel<DeathPrediction>> {

    @Override
    public EntityModel<DeathPrediction> process(
        EntityModel<DeathPrediction> model
    ) {
        return model;
    }
}
