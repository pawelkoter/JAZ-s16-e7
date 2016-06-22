package rest.services;

import domain.Person;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("people")
@Stateless
public class PersonResource {

//    private PersonRepository repo = new HSQLPersonRepository();

    @PersistenceContext
    private EntityManager em;

    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public List<Person> getAllPersons() {
//        return repo.getAll();
        return em.createNamedQuery( "person.all", Person.class ).getResultList();
    }

    @POST
    @Consumes( MediaType.APPLICATION_JSON )
    public Response addPerson( Person person ) {
//        repo.add( person );
        em.persist( person );
        em.flush();

        return Response.ok().build();
    }

    @PUT
    @Path( "/{id}" )
    @Consumes( MediaType.APPLICATION_JSON )
    public Response updatePerson( @PathParam( "id" ) int personId, Person person ) {
//        repo.update( personId, person );
        Person toUpdate = em.createNamedQuery( "person.byId", Person.class )
                .setParameter( "id", personId )
                .getSingleResult();

        if ( toUpdate != null ) {
            toUpdate.setAge( person.getAge() );
            toUpdate.setBirthday( person.getBirthday() );
            toUpdate.setEmail( person.getEmail() );
            toUpdate.setFirstName( person.getFirstName() );
            toUpdate.setLastName( person.getLastName() );
            toUpdate.setGender( person.getGender() );
            em.persist( toUpdate );
            em.flush();
        }
        else {
            return Response.status( Response.Status.NOT_FOUND ).build();
        }
        return Response.ok().build();
    }

    @DELETE
    @Path( "/{id}" )
    public Response deletePerson( @PathParam( "id" ) int personId ) {
//        repo.delete( personId );
        Person toDelete = em.createNamedQuery( "person.byId", Person.class )
                .setParameter( "id", personId )
                .getSingleResult();

        em.remove( toDelete );
        em.flush();

        return Response.ok().build();
    }
}
