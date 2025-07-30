package aivlebigproject.infra;

import aivlebigproject.domain.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

@Component
public class ScheduleHateoasProcessor
    implements RepresentationModelProcessor<EntityModel<Schedule>> {

    @Override
    public EntityModel<Schedule> process(EntityModel<Schedule> model) {
        return model;
    }
}
