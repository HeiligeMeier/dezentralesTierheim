package de.dezentralestierheim.rest;

import de.dezentralestierheim.jpa.Pflegestelle;
import de.dezentralestierheim.jpa.PflegestelleRepository;
import de.dezentralestierheim.jpa.Tier;
import de.dezentralestierheim.jpa.TierRepository;
import io.quarkus.panache.common.Sort;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Path("/pflegestellen")
public class PflegestellenResource {
    private final PflegestelleRepository pflegestellenRepository;
    private final TierRepository tierRepository;

    @Inject
    public PflegestellenResource(PflegestelleRepository pflegestellenRepository, TierRepository tierRepository) {
        this.pflegestellenRepository = pflegestellenRepository;
        this.tierRepository = tierRepository;
    }

    // Melanie
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response addPflegestelle(Pflegestelle pflegestelle) {

        // Neue Pflegestelle ist frei seit Tag der Anmeldung beim Tierschutz
        if (pflegestelle.getZuletztBelegtAm() == null) {
            pflegestelle.setZuletztBelegtAm(LocalDate.now());
        }

        if (pflegestelle.getMaxKapazitaet() != 0) {
            pflegestelle.setKapazitaet(pflegestelle.getMaxKapazitaet());
        }

        pflegestellenRepository.persist(pflegestelle);

        // 201 created
        return Response.status(Response.Status.CREATED).entity(pflegestelle.id).build();
    }

    // Stefan
    @PUT
    @Path("/{id}/Aufnahmebereit")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response changeAufnahmebereit(@PathParam("id") Long pflegestellenId, Map<String, Boolean> body) {
        Pflegestelle pflegestelle = pflegestellenRepository.findById(pflegestellenId);

        // Pflegestelle nicht gefunden
        if (pflegestelle == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Pflegestelle mit ID " + pflegestellenId + " nicht gefunden.")
                    .build();
        }

        if (!body.containsKey("aufnahmebereit")) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("'aufnahmebereit' nicht angegeben.")
                    .build();
        }

        Boolean aufnahmebereit = body.get("aufnahmebereit");
        pflegestelle.setAufnahmebereit(aufnahmebereit);

        return Response.ok("Pflegestelle " + pflegestellenId + " wurde auf 'aufnahmebereit' = " + aufnahmebereit + " gesetzt.")
                .build();
    }

    // Melanie
    @PUT
    @Path("/{id}/besetzen")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response besetzePflegestelle(@PathParam("id") Long pflegestellenId) {

        // Pflegestelle existiert nicht 404
        if (pflegestellenRepository.findById(pflegestellenId) == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Pflegestelle pflegestelle = pflegestellenRepository.findById(pflegestellenId);

        // Kapazität der Pflegestelle anpassen -1 Tier
        if (pflegestelle.getKapazitaet() > 0) {
            pflegestelle.setKapazitaet(pflegestelle.getKapazitaet() - 1);
        } else {
            // Pflegestelle ist bereits voll 400
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        // Pflegestelle erfolgreich besetzt 200
        return Response.status(Response.Status.OK).build();
    }

    // Melanie
    @PUT
    @Path("/{id}/erhoehen")
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response kapazitaetPflegestelleErhoehen(@PathParam("id") Long pflegestellenId) {

        // Pflegestelle existiert nicht 404
        if (pflegestellenRepository.findById(pflegestellenId) == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        Pflegestelle pflegestelle = pflegestellenRepository.findById(pflegestellenId);

        // Kapazität der Pflegestelle anpassen +1 Tier
        if (pflegestelle.getKapazitaet() < pflegestelle.getMaxKapazitaet()) {
            pflegestelle.setKapazitaet(pflegestelle.getKapazitaet() + 1);
        } else {
            // Pflegestelle hat Max Kapazität schon erreicht --> Kapa nicht erhöhen - aber das ist ja okay, muss kein Fehler sein oder?
            return Response.status(Response.Status.OK).build();
        }
        // Pflegestelle erfolgreich besetzt 200
        return Response.status(Response.Status.OK).build();
    }

    // Raluca
    @GET
    @Path("/auswaehlen/tier/{id}")
    public Response freiePflegestelleAuswahl(@PathParam("id") Long tierId) {
        Tier tier = tierRepository.findById(tierId);

        if (tier == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Keine Tierdaten").build();
        }
        if (tier.getStatus() == Tier.Status.TOT || (tier.getAufnahmeNichtMoeglich() != null && tier.getAufnahmeNichtMoeglich() != Tier.AufnahmeNichtMoeglich.FALSE)) {
            return Response.status(Response.Status.BAD_REQUEST).entity(tier.getStatus() == Tier.Status.TOT ? "Tier ist tot" : "Aufnahme ist nicht mehr möglich").build();
        }
        String queryString = "tierart = ?1 and aufnahmebereit = true and kapazitaet > 0";
        if (tier.getStatus() == Tier.Status.KRANK) {
            queryString += " and nurGesund = false";
        }

        Pflegestelle pflegestelle = pflegestellenRepository.find(queryString, Sort.by("zuletztBelegtAm"), tier.getTierart()).firstResult();
        if (pflegestelle == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Es wurde keine freie Pflegestelle gefunden").build();
        }
        // Pflegestelle nicht mehr aufnahmebereit, da schon angefragt wird
        pflegestelle.setAufnahmebereit(false);
        pflegestellenRepository.persist(pflegestelle);

        return Response.status(Response.Status.OK).entity(pflegestelle).build();
    }

    // Stefan
    @GET
    @Path("/{id}")
    public Response getPflegestelle(@PathParam("id") Long id) {
        Pflegestelle p = pflegestellenRepository.findById(id);

        // Pflegestelle existiert nicht
        if (p == null) {
            throw new NotFoundException(Response.status(Response.Status.NOT_FOUND)
                    .entity("Pflegestelle " + id + " nicht gefunden")
                    .build());
        }

        // Pflegestelle existiert
        return Response.ok(p)
                .header("Cache-Control", "max-age=300")
                .build();
    }

    // Stefan
    @GET
    public Response getPflegestellen() {
        List<Pflegestelle> p = pflegestellenRepository.listAll();

        // Keine Pflegestellen gelistet
        if (p.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT)
                    .entity("Keine Pflegestellen gelistet")
                    .build();
        }

        // mind. 1 Tier gelistet 200
        return Response.ok(p)
                .header("Cache-Control", "max-age=300")
                .build();
    }

}
