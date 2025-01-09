package de.dezentralestierheim.rest;

import de.dezentralestierheim.jpa.*;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.Period;

import java.time.LocalDate;
import java.util.List;

@Path("/interessenten")
public class InteressentenResource {
    private final InteressentRepository interessentenRepository;
    private final TierRepository tierRepository;
    private final PflegestelleRepository pflegestellenRepository;

    @Inject
    public InteressentenResource(InteressentRepository interessentenRepository, TierRepository tierRepository, PflegestelleRepository pflegestellenRepository) {
        this.interessentenRepository = interessentenRepository;
        this.tierRepository = tierRepository;
        this.pflegestellenRepository = pflegestellenRepository;
    }

    // Melanie
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response addInteressent(Interessent interessent) {

        interessentenRepository.persist(interessent);

        // 201 created
        return Response.status(Response.Status.CREATED).entity(interessent).build();
    }

    // Melanie
    @GET
    @Path("/{id}/check-eignung")
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkInteressentEignung(@PathParam("id") Long interessentId) {
        // Fetch Interessent
        Interessent interessent = interessentenRepository.findById(interessentId);
        if (interessent == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Interessent nicht gefunden.").build();
        }

        // Fetch Tier
        Tier tier = tierRepository.findById(interessent.getInteressiertAnTierID());
        if (tier == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Tier existiert nicht in unserem Tierschutzverein.").build();
        }

        if (tier.getIstAdoptiert()) {
            return Response.status(Response.Status.NOT_FOUND).entity("Tier wurde bereits adoptiert.").build();
        }

        // Check Alter
        if (Period.between(interessent.getGeburtsdatum(), LocalDate.now()).getYears() < 18) {
            return Response.ok("{\"geeignet\": \"nein\"}").build();
        }

        // Check Wohnlage
        if (interessent.getWohnlage() == Interessent.Wohnlage.STARKER_VERKEHR && tier.getTierart() == Tier.Tierart.KATZE) {
            return Response.ok("{\"geeignet\": \"nein\"}").build();
        }

        // Check Quadratmeter
        int requiredSpace = (tier.getTierart() == Tier.Tierart.HUND) ? 70 : 50; // Hunde brauchen mehr Platz
        if (interessent.getQuadratmeter() < requiredSpace) {
            return Response.ok("{\"geeignet\": \"nein\"}").build();
        }

        // Check Kinderfreundlichkeit
        if (interessent.getHatKinder() && (tier.getIstKinderfreundlich() == null || !tier.getIstKinderfreundlich())) {
            return Response.ok("{\"geeignet\": \"nein\"}").build();
        }

        // Interessent hat Tiere - Tier verträgt sich aber nicht mit anderen Tieren
        if (interessent.getAndereTiere() != null && (tier.getVertraegtSichMitAnderenTieren() == null || !tier.getVertraegtSichMitAnderenTieren())) {
            return Response.ok("{\"geeignet\": \"nein\"}").build();
        }

        if (!interessent.getGartenOderBalkon() && tier.getTierart() == Tier.Tierart.KATZE && tier.getVertraegtSichMitAnderenTieren() != null && tier.getVertraegtSichMitAnderenTieren()) {
            return Response.ok("{\"geeignet\": \"nein\"}").build();
        }

        if (interessent.getWohnlage() == Interessent.Wohnlage.VERKEHRSNAH && interessent.getQuadratmeter() < 70 && tier.getTierart() == Tier.Tierart.HUND) {
            return Response.ok("{\"geeignet\": \"nein\"}").build();
        }

        if (interessent.getWohnlage() == Interessent.Wohnlage.VERKEHRSNAH && interessent.getQuadratmeter() < 50 && tier.getTierart() == Tier.Tierart.KATZE) {
            return Response.ok("{\"geeignet\": \"nein\"}").build();
        }

        // Passed all checks
        return Response.ok("{\"geeignet\": \"ja\"}").build();
    }

    //Melanie
    @GET
    @Path("/{id}/pflegestelle")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPflegestelleViaInteressent(@PathParam("id") Long interessentId) {

        Interessent interessent = interessentenRepository.findById(interessentId);
        if (interessent == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Interessent nicht gefunden.").build();
        }

        Long tierID = interessent.getInteressiertAnTierID();
        Tier tier = tierRepository.findById(tierID);
        if (tier == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Tier nicht gefunden.").build();
        }

        Long pflegestellenId = tier.getPflegestellenID();
        Pflegestelle pflegestelle = pflegestellenRepository.findById(pflegestellenId);

        return Response.ok(pflegestelle).build();
    }

    // Stefan
    @GET
    @Path("/{id}")
    public Response getInteressent(@PathParam("id") Long id) {
        Interessent interessent = interessentenRepository.findById(id);

        // Keine Interessenten gelistet
        if (interessent == null) {
            return Response.status(Response.Status.NO_CONTENT)
                    .entity("Interessent " + id + " nicht gefunden")
                    .build();
        }

        // Mindestens ein Interessent gelistet
        return Response.ok(interessent)
                .header("Cache-Control", "max-age=300")
                .build();
    }

    // Stefan
    @GET
    public Response getInteressenten() {
        List<Interessent> i = interessentenRepository.listAll();

        // Keine Interessenten gelistet
        if (i.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT)
                    .entity("Keine Interessenten gelistet")
                    .build();
        }

        // mind. 1 Interessent gelistet
        return Response.ok(i)
                .header("Cache-Control", "max-age=300")
                .build();
    }
}