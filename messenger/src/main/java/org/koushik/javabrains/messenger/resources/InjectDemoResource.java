package org.koushik.javabrains.messenger.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

@Path("/injectdemo")
@Consumes(MediaType.TEXT_PLAIN)
@Produces(MediaType.TEXT_PLAIN)
public class InjectDemoResource {
	//The @MatrixParam is just a different format then when using regular params. The only diffrence is that you change the "&" into a ";".
	//The @HeaderParam is used for security purposes which enables you to look for specific Header values in postman under the tab Headers.
	//The @Cookie lest you get the cookie value.
	//The @FormParam lets you access form submissions. This is usually through a HTML form. 
	@GET
	@Path("annotations")
	public String getParamsUsingAnnotations(@MatrixParam("param") String matrixParam,
											@HeaderParam("authSessionID")String header,
											@CookieParam("name") String cookie){
		return "Matrix param: " + matrixParam + " and Header Param: " + header+ "\n"+"Cookie: " + cookie;
	}
	
	@GET
	@Path("context")
	public String getParamsUsingContext(@Context UriInfo uriInfo, @Context HttpHeaders headers){
		//When you do "uriInfo." you will get a lot of methods that are useful in accessing URI information. In this example you got the compete URI
		String path = uriInfo.getAbsolutePath().toString();
		String cookies = headers.getCookies().toString();
		return "Path is: " + path +"\n"+"Cookie is: " + cookies;
	}
}
