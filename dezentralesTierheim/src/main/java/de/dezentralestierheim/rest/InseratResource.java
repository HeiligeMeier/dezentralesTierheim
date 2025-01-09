package de.dezentralestierheim.rest;

import de.dezentralestierheim.jpa.Inserat;
import de.dezentralestierheim.jpa.InseratRepository;
import de.dezentralestierheim.jpa.Tier;
import de.dezentralestierheim.jpa.TierRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Map;

@Path("/inserat")
public class InseratResource {
    private final InseratRepository inseratRepository;
    private final TierRepository tierRepository;

    @Inject
    public InseratResource(InseratRepository inseratRepository, TierRepository tierRepository) {
        this.inseratRepository = inseratRepository;
        this.tierRepository = tierRepository;
    }

    // Stefan
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response addInserat(Inserat inserat) {
        Tier tier = tierRepository.findById(inserat.getTierId());

        // Tier existiert nicht
        if (tier == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Tier mit ID " + inserat.getTierId() + " nicht gefunden.")
                    .build();
        }

        // Inserat existiert bereits
        if (inseratRepository.find("tierId", inserat.getTierId()).firstResult() != null) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("Ein Inserat für das Tier mit ID " + inserat.getTierId() + " existiert bereits.")
                    .build();
        }

        inserat.setIstAktiv(true);

        inseratRepository.persist(inserat);

        return Response.status(Response.Status.CREATED)
                .entity(inserat)
                .build();
    }

    // Stefan
    @PUT
    @Path("/{id}/status")
    @Transactional
    public Response inseratSchliessen(@PathParam("id") Long id, Map<String, Boolean> body) {
        Inserat inserat = inseratRepository.findById(id);

        // Inserat existiert nicht
        if (inserat == null) {
            throw new NotFoundException(Response.status(Response.Status.NOT_FOUND)
                    .entity("Inserat " + id + " nicht gefunden")
                    .build());
        }

        if (!body.containsKey("istAktiv")) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("'istAktiv' nicht angegeben.")
                    .build();
        }

        Boolean aktiv = body.get("istAktiv");
        inserat.setIstAktiv(aktiv);

        inseratRepository.persist(inserat);

        return Response.status(Response.Status.OK)
                .entity("Inserat " + id + " wurde auf " + aktiv + " gesetzt")
                .build();
    }

    // Stefan
    @GET
    @Path("/{id}")
    public Response getInserat(@PathParam("id") Long id) {
        Inserat inserat = inseratRepository.findById(id);

        // Keine Inserate gelistet
        if (inserat == null) {
            return Response.status(Response.Status.NO_CONTENT)
                    .entity("Inserat " + id + " nicht gefunden")
                    .build();
        }

        // Mindestens ein Inserat gelistet
        return Response.ok(inserat)
                .header("Cache-Control", "max-age=300")
                .build();
    }

    // Stefan
    @GET
    public Response getInserate(@QueryParam("istAktiv") Boolean istAktiv) {
        List<Inserat> inserate;

        if (istAktiv != null) {
            // Filterung basierend auf dem Parameter istAktiv
            inserate = inseratRepository.find("istAktiv", istAktiv).list();
        } else {
            // Alle Inserate, wenn kein Filter angegeben
            inserate = inseratRepository.listAll();
        }

        // Keine Inserate gelistet
        if (inserate.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT)
                    .entity("Keine Inserate gelistet")
                    .build();
        }

        // Mindestens ein Inserat gelistet
        return Response.ok(inserate)
                .header("Cache-Control", "max-age=300")
                .build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteInserat(@PathParam("id") Long id) {
        Inserat inserat = inseratRepository.findById(id);

        // Überprüfen, ob das Inserat existiert
        if (inserat == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Inserat " + id + " nicht gefunden")
                    .build();
        }

        // Löschen des Inserats
        inseratRepository.delete(inserat);

        // Erfolgreiche Löschbestätigung
        return Response.status(Response.Status.NO_CONTENT)
                .entity("Inserat " + id + " wurde erfolgreich gelöscht")
                .build();
    }

}
