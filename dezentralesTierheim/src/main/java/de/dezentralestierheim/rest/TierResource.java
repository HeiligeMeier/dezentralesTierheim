package de.dezentralestierheim.rest;

import de.dezentralestierheim.jpa.Tier;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/tiere")
public class TierResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response addTier(Tier tier) {

        tier.persist();

        // 201 created
        return Response.status(Response.Status.CREATED).entity(tier.id).build();
    }
}
