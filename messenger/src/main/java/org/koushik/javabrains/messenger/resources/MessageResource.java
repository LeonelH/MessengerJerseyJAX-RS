package org.koushik.javabrains.messenger.resources;



import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.List;

import org.koushik.javabrains.messenger.service.MessageService;
import org.koushik.javabrains.messenger.model.Message;
import org.koushik.javabrains.messenger.resources.beans.MessageFilterBean;

/*
You can access both Json and XML, but your program might call specific methods when Json is sent or call specific methods when XML is sent
With the idea above you can use "@Produces(value = { MediaType.APPLICATION_JSON, MediaType.TEXT_XML})" but as said above your program might 
return a specific content when Json or xml are called. So next up we are going to overrride the getMessages.. 
Remember what you did on the accept messages you can do the same thing to consumes in order to receive different formats like JSon or XMl
*/

@Path("/messages")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MessageResource {
	
	MessageService messageService = new MessageService();

//	@GET
//	public List<Message> getMessages(@QueryParam("year")int year,
//									 @QueryParam("start") int start,	
//									 @QueryParam("size") int size){
//		if(year > 0 ){
//			return messageService.getAllMessagesForYear(year);
//		}
//		if(start >= 0 && size >= 0){
//			return messageService.getAllMessagesPaginated(start, size);
//		}
//		return messageService.getAllMessages();
//	}
	
	//Rather than calling all the three @QueryParam from the top method, we can actually create a bean with all three params 
	//then we can pass them in into this method, Thats why I re-copied the whole method and didn't erase it. Both methods work
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Message> getJsonMessages(@BeanParam MessageFilterBean filterBean){
		System.out.println("JSon method called");
		if (filterBean.getYear() > 0) {
			return messageService.getAllMessagesForYear(filterBean.getYear());
		}
		if (filterBean.getStart() >= 0 && filterBean.getSize() > 0) {
			return messageService.getAllMessagesPaginated(filterBean.getStart(), filterBean.getSize());
		}
		return messageService.getAllMessages();
	}
	
	@GET
	@Produces(MediaType.TEXT_XML)
	public List<Message> getXmlMessages(@BeanParam MessageFilterBean filterBean){
		System.out.println("XML method called");
		if (filterBean.getYear() > 0) {
			return messageService.getAllMessagesForYear(filterBean.getYear());
		}
		if (filterBean.getStart() >= 0 && filterBean.getSize() > 0) {
			return messageService.getAllMessagesPaginated(filterBean.getStart(), filterBean.getSize());
		}
		return messageService.getAllMessages();
	}
	
	@POST
	public Response addMessage(Message message, @Context UriInfo uriInfo) {
		
		Message newMessage = messageService.addMessage(message);
		String newId = String.valueOf(newMessage.getId());
		URI uri = uriInfo.getAbsolutePathBuilder().path(newId).build();
		return Response.created(uri)
				.entity(newMessage)
				.build();
	}
	
	@PUT
	@Path("/{messageId}")
	public Message updateMessage(@PathParam("messageId") long id, Message message) {
		message.setId(id);
		return messageService.updateMessage(message);
	}
	
	@DELETE
	@Path("/{messageId}")
	public void deleteMessage(@PathParam("messageId") long id) {
		messageService.removeMessage(id);
	}
	
	
	@GET
	@Path("/{messageId}")
	public Message getMessage(@PathParam("messageId") long id, @Context UriInfo uriInfo) {
		Message message = messageService.getMessage(id);
		message.addLink(getUriForSelf(uriInfo, message), "self");
		message.addLink(getUriForProfile(uriInfo, message), "profile");
		message.addLink(getUriForComments(uriInfo, message), "comments");
		
		return message;
		
	}

	private String getUriForComments(UriInfo uriInfo, Message message) {
		URI uri = uriInfo.getBaseUriBuilder()
				.path(MessageResource.class)
	       		.path(MessageResource.class, "getCommentResource")
	       		.path(CommentResource.class)
	       		.resolveTemplate("messageId", message.getId())
	            .build();
	    return uri.toString();
	}

	private String getUriForProfile(UriInfo uriInfo, Message message) {
		URI uri = uriInfo.getBaseUriBuilder()
       		 .path(ProfileResource.class)
       		 .path(message.getAuthor())
             .build();
        return uri.toString();
	}

	private String getUriForSelf(UriInfo uriInfo, Message message) {
		String uri = uriInfo.getBaseUriBuilder()
		 .path(MessageResource.class)
		 .path(Long.toString(message.getId()))
		 .build()
		 .toString();
		return uri;
	}
	
	@Path("/{messageId}/comments")
	public CommentResource getCommentResource() {
		return new CommentResource();
	}
	
}
