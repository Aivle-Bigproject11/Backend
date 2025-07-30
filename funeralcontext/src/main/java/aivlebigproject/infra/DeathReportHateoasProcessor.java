package aivlebigproject.infra;

import aivlebigproject.domain.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

@Component
public class DeathReportHateoasProcessor
    implements RepresentationModelProcessor<EntityModel<DeathReport>> {

    @Override
    public EntityModel<DeathReport> process(EntityModel<DeathReport> model) {
        return model;
    }
}
