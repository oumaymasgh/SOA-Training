package webservices;

import entities.UniteEnseignement;
import metiers.UniteEnseignementBusiness;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/ue")
public class UniteEnseignementRessource {

    // static: created once, so the data survives between requests
    private static final UniteEnseignementBusiness business = new UniteEnseignementBusiness();

    // CREATE: POST /api/ue
    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response add(UniteEnseignement ue) {
        if (business.addUniteEnseignement(ue))
            return Response.status(Response.Status.CREATED).build();
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // READ: GET /api/ue  |  GET /api/ue?semestre=2  |  GET /api/ue?code=123
    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@QueryParam("semestre") Integer semestre,
                        @QueryParam("code") Integer code) {
        if (code != null) {
            UniteEnseignement ue = business.getUEByCode(code);
            return ue == null ? Response.status(Response.Status.NOT_FOUND).build()
                    : Response.ok(ue).build();
        }
        if (semestre != null)
            return Response.ok(business.getUEBySemestre(semestre)).build();
        return Response.ok(business.getListeUE()).build();
    }

    // READ ONE: GET /api/ue/5
    @GET
    @Path("/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByCode(@PathParam("code") int code) {
        UniteEnseignement ue = business.getUEByCode(code);
        return ue == null ? Response.status(Response.Status.NOT_FOUND).build()
                : Response.ok(ue).build();
    }

    // UPDATE: PUT /api/ue/5
    @PUT
    @Path("/{code}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response update(@PathParam("code") int code, UniteEnseignement ue) {
        if (business.updateUniteEnseignement(code, ue))
            return Response.ok().build();
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    // DELETE: DELETE /api/ue/5
    @DELETE
    @Path("/{code}")
    public Response delete(@PathParam("code") int code) {
        if (business.deleteUniteEnseignement(code))
            return Response.ok().build();
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}