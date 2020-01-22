package rest;

import dto.KayakDTO;
import entities.Kayak;
import facades.MultiFacade;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import utils.EMF_Creator;

/**
 *
 * @author Henning
 */
@OpenAPIDefinition(
        info = @Info(
                title = "3rdSemExam",
                version = "0.1",
                description = "Backend of the 3rdSemExam project"
        ),
        tags = {
            @Tag(name = "Kayak resource", description = "API related to Kayaks")
        },
        servers = {
            @Server(
                    description = "For Local host testing",
                    url = "http://localhost:8080/3rdSemExam"
            ),
            @Server(
                    description = "Server API",
                    url = "https://www.mutezone.site/3rdSemExam"
            )

        }
)
@Path("kayak")
public class KayakResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
    private static final MultiFacade<Kayak> KAYAK_FACADE = new MultiFacade(Kayak.class, EMF);

    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    //@RolesAllowed({"user", "admin"})
    @Operation(summary = "Basic API Response: Kayak",
            tags = {"Kayak Resource"},
            responses = {
                @ApiResponse(responseCode = "200", description = "The user successfully connected"),
                @ApiResponse(responseCode = "400", description = "The server cannot or will not process the request")})
    public String connectionCheck() {
        return "{\"msg\":\"Connection to the 'Kayak' api section is online\"}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @RolesAllowed({"user", "admin"})
    @Operation(summary = "Kayak By ID",
            tags = {"Kayak Resource"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = KayakDTO.class))),
                @ApiResponse(responseCode = "200", description = "The requested resources was returned"),
                @ApiResponse(responseCode = "400", description = "The server cannot or will not process the request and no resources were returned")})
    public KayakDTO getRest(@PathParam("id") int id) throws IOException, InterruptedException, ExecutionException {
        return new KayakDTO(((Kayak) KAYAK_FACADE.find((long) (int) id)));
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "All Kayaks",
            tags = {"Kayak  Resource"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = KayakDTO.class))),
                @ApiResponse(responseCode = "200", description = "The requested Kayak resources were returned"),
                @ApiResponse(responseCode = "400", description = "The server cannot or will not process the request and no resources were returned")})
    public List<KayakDTO> getAllRest() throws IOException, InterruptedException, ExecutionException {
        List<KayakDTO> dpDTOList = new ArrayList<>();
        for (Object dp : KAYAK_FACADE.findAll()) {
            dpDTOList.add(new KayakDTO(((Kayak) dp)));
        }
        return dpDTOList;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    //  @Path("/create")
    @Operation(summary = "Kayak Creation",
            tags = {"Kayak Resource"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = KayakDTO.class))),
                @ApiResponse(responseCode = "200", description = "The person was created and persisted"),
                @ApiResponse(responseCode = "400", description = "No users was created or persisted")})
    public void createRest(Kayak entity) {
        KAYAK_FACADE.create(entity);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    // @RolesAllowed("admin")
    @Operation(summary = "Kayak Deletion", tags = {"Kayak  Resource"},
            responses = {
                @ApiResponse(responseCode = "200", description = "Kayak Deleted"),
                @ApiResponse(responseCode = "400", description = "Not all arguments provided to delete")
            })
    public void deleteRest(@PathParam("id") Long id) {
        KAYAK_FACADE.remove(id);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    //@RolesAllowed("admin")
    @Operation(summary = "Kayak Editing", tags = {"Kayak  Resource"},
            responses = {
                @ApiResponse(responseCode = "200", description = "Kayak Edited"),
                @ApiResponse(responseCode = "400", description = "Not all arguments provided with the body to edit")
            })
    public void editRest(Kayak entity) {
        KAYAK_FACADE.edit(entity);
    }
}
