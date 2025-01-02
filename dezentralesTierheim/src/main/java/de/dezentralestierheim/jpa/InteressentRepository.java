package de.dezentralestierheim.jpa;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class InteressentRepository implements PanacheRepository<Interessent> {

}
