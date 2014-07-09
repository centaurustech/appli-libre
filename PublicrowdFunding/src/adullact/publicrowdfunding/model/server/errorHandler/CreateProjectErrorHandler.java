package adullact.publicrowdfunding.model.server.errorHandler;

import adullact.publicrowdfunding.model.server.event.CreateProjectEvent;
import adullact.publicrowdfunding.model.server.request.CreateProjectRequest;

public class CreateProjectErrorHandler extends AuthenticatedErrorHandler<CreateProjectRequest, CreateProjectEvent, CreateProjectErrorHandler> {

}