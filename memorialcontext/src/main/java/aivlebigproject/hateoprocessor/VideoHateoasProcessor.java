package aivlebigproject.hateoprocessor;

import aivlebigproject.model.Video;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

@Component
public class VideoHateoasProcessor
    implements RepresentationModelProcessor<EntityModel<Video>> {

    @Override
    public EntityModel<Video> process(EntityModel<Video> model) {
        return model;
    }
}
