package de.dezentralestierheim.rest;

import de.dezentralestierheim.jpa.Tier;
import de.dezentralestierheim.jpa.TierRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

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

    @GET
    @Path("/{id}")
    public Response getTier(@PathParam("id") Long id) {
        Tier tier = tierRepository.findById(id);

        // Tier existiert nicht
        if (tier == null) {
            throw new NotFoundException(Response.status(Response.Status.NOT_FOUND)
                    .entity("Tier " + id + " nicht gefunden")
                    .build());
        }

        // Tier existiert
        return Response.ok(tier)
                .header("Cache-Control", "max-age=300")
                .build();
    }

    @GET
    public Response getAllTiers() {
        List<Tier> tiere = tierRepository.listAll();

        // Keine Tiere gelistet
        if (tiere.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT)
                    .entity("Keine Tiere gelistet")
                    .build();
        }

        // mind. 1 Tier gelistet
        return Response.ok(tiere)
                .header("Cache-Control", "max-age=300")
                .build();
    }

    @PUT
    @Path("/{id}/rueckzieher")
    @Transactional
    public Response changeToRueckzieher(@PathParam("id") Long id) {
        Tier tier = tierRepository.findById(id);

        // Tier existiert nicht
        if (tier == null) {
            throw new NotFoundException(Response.status(Response.Status.NOT_FOUND)
                    .entity("Tier " + id + " nicht gefunden")
                    .build());
        }

        tier.setAufnahmeNichtMoeglich(Tier.AufnahmeNichtMoeglich.RUECKZIEHER_VOM_BESITZER);

        return Response.status(Response.Status.OK)
                .entity("Tier " + id + " wurde zurückgezogen")
                .build();
    }

    // TODO: Wechsel in Inserat Datei
    @PUT
    @Path("/{id}/close")
    @Transactional
    public Response inseratSchliessen(@PathParam("id") Long id) {
        Tier tier = tierRepository.findById(id);

        // Tier existiert nicht
        if (tier == null) {
            throw new NotFoundException(Response.status(Response.Status.NOT_FOUND)
                    .entity("Tier " + id + " nicht gefunden")
                    .build());
        }

        // TODO: Wie schließt man ein Inserat? Text und Bild auf null setzen? Oder in eigener Datei dann einfach deleten?
        tier.setInseratText(null);
        // tier.setBild(null)

        return Response.status(Response.Status.OK)
                .entity("Inserat von Tier " + id + " wurde geschlossen")
                .build();
    }

    @PUT
    @Path("/{id}/adopted")
    @Transactional
    public Response adopted(@PathParam("id") Long id) {
        Tier tier = tierRepository.findById(id);

        // Tier existiert nicht
        if (tier == null) {
            throw new NotFoundException(Response.status(Response.Status.NOT_FOUND)
                    .entity("Tier " + id + " nicht gefunden")
                    .build());
        }

        tier.setIstAdoptiert(true);

        return Response.status(Response.Status.OK)
                .entity("Tier " + id + " wurde adoptiert")
                .build();
    }
}
