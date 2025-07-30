package aivlebigproject.infra;

import aivlebigproject.domain.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

@Component
public class FuneralPythonHateoasProcessor
    implements RepresentationModelProcessor<EntityModel<FuneralPython>> {

    @Override
    public EntityModel<FuneralPython> process(
        EntityModel<FuneralPython> model
    ) {
        return model;
    }
}
