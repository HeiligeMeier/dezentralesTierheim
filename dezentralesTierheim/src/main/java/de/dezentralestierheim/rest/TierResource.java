package de.dezentralestierheim.rest;

import de.dezentralestierheim.dto.TierUpdateDto;
import de.dezentralestierheim.jpa.Pflegestelle;
import de.dezentralestierheim.jpa.PflegestelleRepository;
import de.dezentralestierheim.jpa.Tier;
import de.dezentralestierheim.jpa.TierRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Map;

// Melanie
@Path("/tiere")
public class TierResource {
    private final TierRepository tierRepository;
    private final PflegestelleRepository pflegestelleRepository;

    @Inject
    public TierResource(TierRepository tierRepository, PflegestelleRepository pflegestelleRepository) {
        this.tierRepository = tierRepository;
        this.pflegestelleRepository = pflegestelleRepository;
    }

    // Melanie
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response addTier(Tier tier) {

        tierRepository.persist(tier);

        // 201 created
        return Response.status(Response.Status.CREATED).entity(tier).build();
    }

    // Melanie
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

        tierRepository.persist(tier);
        return Response.status(Response.Status.OK).build();
    }

    // Melanie
    @PUT
    @Path("/{id}/keineKapazitaet")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response keineKapazitaet(@PathParam("id") Long tierId) {

        // Tier existiert nicht 404
        if (tierRepository.findById(tierId) == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Tier tier = tierRepository.findById(tierId);

        // Tier als verstorben markieren
        tier.setAufnahmeNichtMoeglich(Tier.AufnahmeNichtMoeglich.KEINE_KAPAZITAET);

        tierRepository.persist(tier);
        return Response.status(Response.Status.OK).build();
    }

    // Stefan & Melanie
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

        if (tier.getPflegestellenID() != null) {
            tier.setPflegestellenID(null);
            Pflegestelle pflegestelle = pflegestelleRepository.findById(tier.getPflegestellenID());
            pflegestelle.setKapazitaet(pflegestelle.getKapazitaet() + 1);
            pflegestelleRepository.persist(pflegestelle);
            tierRepository.persist(tier);
            return Response.status(Response.Status.OK).entity("Pflegestelle vorhanden").build();
        }

        tierRepository.persist(tier);
        return Response.status(Response.Status.OK)
                .entity("Keine Pflegestelle vorhanden").build();
    }

    // Stefan
    @PUT
    @Path("/{id}/adopted")
    @Transactional
    public Response adopted(@PathParam("id") Long id, Map<String, Boolean> body) {
        Tier tier = tierRepository.findById(id);

        // Tier existiert nicht
        if (tier == null) {
            throw new NotFoundException(Response.status(Response.Status.NOT_FOUND)
                    .entity("Tier " + id + " nicht gefunden")
                    .build());
        }

        if (!body.containsKey("istAdoptiert")) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("'aufnahmebereit' nicht angegeben.")
                    .build();
        }

        Boolean istAdoptiert = body.get("istAdoptiert");
        tier.setIstAdoptiert(istAdoptiert);

        tierRepository.persist(tier);

        return Response.status(Response.Status.OK)
                .entity("Tier " + id + ", adoptiert = " + istAdoptiert)
                .build();
    }

    // Raluca
    @PATCH
    @Path("/{id}")
    @Transactional
    public Response pflegestellenIdAktualisieren(@PathParam("id") Long tierId, TierUpdateDto toUpdateFields) {
        Tier tier = tierRepository.findById(tierId);

        if (tier == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Es wurde kein Tier mit dieser Id gefunden").build();
        }

        Long pflegestellenId = toUpdateFields.getId();

        if (pflegestellenId == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Feld \"id\" fehlt.").build();
        }

        Pflegestelle pflegestelle = pflegestelleRepository.findById(pflegestellenId);
        if (pflegestelle == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Pflegestelle existiert nicht.").build();
        }
        tier.setPflegestellenID(pflegestellenId);

        tierRepository.persist(tier);

        return Response.status(Response.Status.OK).entity(tier).build();
    }

    // Raluca
    @GET
    @Path("/{id}/istAdoptiert")
    public Response istTierAdoptiert(@PathParam("id") Long id) {
        Tier tier = tierRepository.findById(id);

        // Tier existiert nicht
        if (tier == null) {
            throw new NotFoundException(Response.status(Response.Status.NOT_FOUND)
                    .entity("Tier " + id + " nicht gefunden")
                    .build());
        }

        // Tier existiert
        return Response.ok(tier.getIstAdoptiert()).build();
    }

    // Stefan
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

    // Stefan
    @GET
    public Response getTiere() {
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
}
