package de.dezentralestierheim.rest;

import de.dezentralestierheim.jpa.Pflegestelle;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.time.LocalDate;

@Path("/pflegestellen")
public class PflegestellenResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response addPflegestelle(Pflegestelle pflegestelle) {

        // Neue Pflegestelle ist frei seit Tag der Anmeldung beim Tierschutz
        if (pflegestelle.getFreiSeit() == null) {
            pflegestelle.setFreiSeit(LocalDate.now());
        }

        // stimmt das so?
        pflegestelle.persist();

        // 201 created
        return Response.status(Response.Status.CREATED).entity(pflegestelle.id).build();
    }
}
