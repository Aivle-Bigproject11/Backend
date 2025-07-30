package aivlebigproject.infra;

import aivlebigproject.domain.*;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.stereotype.Component;

@Component
public class FuneralInfoHateoasProcessor
    implements RepresentationModelProcessor<EntityModel<FuneralInfo>> {

    @Override
    public EntityModel<FuneralInfo> process(EntityModel<FuneralInfo> model) {
        model.add(
            Link
                .of(model.getRequiredLink("self").getHref() + "/createobituary")
                .withRel("createobituary")
        );
        model.add(
            Link
                .of(
                    model.getRequiredLink("self").getHref() +
                    "/createdeathreport"
                )
                .withRel("createdeathreport")
        );
        model.add(
            Link
                .of(model.getRequiredLink("self").getHref() + "/createschedule")
                .withRel("createschedule")
        );
        model.add(
            Link
                .of(
                    model.getRequiredLink("self").getHref() +
                    "/createalldocuments"
                )
                .withRel("createalldocuments")
        );
        model.add(
            Link
                .of(
                    model.getRequiredLink("self").getHref() +
                    "/updatefuneralinfo"
                )
                .withRel("updatefuneralinfo")
        );

        return model;
    }
}
