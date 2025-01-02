package de.dezentralestierheim.rest;

import de.dezentralestierheim.jpa.Interessent;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/interessenten")
public class InteressentenResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response addInteressent(Interessent interessent) {

        interessent.persist();

        // 201 created
        return Response.status(Response.Status.CREATED).entity(interessent.id).build();
    }
}