package de.dezentralestierheim.rest;

import de.dezentralestierheim.jpa.Tier;
import de.dezentralestierheim.jpa.TierRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/tiere")
public class TierResource {
    private final TierRepository tierRepository;

    @Inject
    public TierResource(TierRepository tierRepository) {
        this.tierRepository = tierRepository;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response addTier(Tier tier) {

        tierRepository.persist(tier);

        // 201 created
        return Response.status(Response.Status.CREATED).entity(tier.id).build();
    }

    @PUT
    @Path("/{id}/verstorben")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response tierVerstorben(@PathParam("id") Long tierId) {

        // Tier existiert nicht 404
        if (tierRepository.findById(tierId) == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Tier tier = tierRepository.findById(tierId);

        // Tier als verstorben markieren
        tier.setStatus(Tier.Status.TOT);

        return Response.status(Response.Status.OK).build();
    }
}
