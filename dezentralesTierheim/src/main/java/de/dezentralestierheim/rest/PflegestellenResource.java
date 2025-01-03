package de.dezentralestierheim.rest;

import de.dezentralestierheim.jpa.Pflegestelle;
import de.dezentralestierheim.jpa.PflegestelleRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDate;

@Path("/pflegestellen")
public class PflegestellenResource {
    private final PflegestelleRepository pflegestellenRepository;

    @Inject
    public PflegestellenResource(PflegestelleRepository pflegestellenRepository) {
        this.pflegestellenRepository = pflegestellenRepository;
    }

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
        }else{
            // Pflegestelle hat Max Kapazität schon erreicht --> Kapa nicht erhöhen - aber das ist ja okay, muss kein Fehler sein oder?
            return Response.status(Response.Status.OK).build();
        }
        // Pflegestelle erfolgreich besetzt 200
        return Response.status(Response.Status.OK).build();
    }

}
