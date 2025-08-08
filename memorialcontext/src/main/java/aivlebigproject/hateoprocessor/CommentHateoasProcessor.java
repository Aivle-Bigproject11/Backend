package aivlebigproject.hateoprocessor;

import aivlebigproject.model.Comment;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

@Component
public class CommentHateoasProcessor
    implements RepresentationModelProcessor<EntityModel<Comment>> {

    @Override
    public EntityModel<Comment> process(EntityModel<Comment> model) {
        return model;
    }
}
