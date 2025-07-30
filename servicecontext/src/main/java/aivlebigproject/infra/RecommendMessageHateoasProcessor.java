package aivlebigproject.infra;

import aivlebigproject.domain.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

@Component
public class RecommendMessageHateoasProcessor
    implements RepresentationModelProcessor<EntityModel<RecommendMessage>> {

    @Override
    public EntityModel<RecommendMessage> process(
        EntityModel<RecommendMessage> model
    ) {
        return model;
    }
}
