package br.com.ControleFinanceiroCapote.rest;

import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import org.codehaus.jackson.map.ObjectMapper;

public class UtilRest {

	@Context
	HttpServletRequest request = null;

	public int userId() {
		return Integer.parseInt((String) request.getSession().getAttribute("id"));
	}

	public Response buildResponse(Object result) {

		StringWriter fw = new StringWriter();
		try {

			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(fw, result);

			return Response.ok(fw.toString()).build();
		} catch (Exception ex) {
			return this.buildResponse(ex.getMessage());
		}

	}

	public Response buildErrorResponse(String str) {
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(str).type("text/plain").build();
	}

}
