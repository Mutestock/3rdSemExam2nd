package rest;

import dto.ImageDTO;
import entities.Image;
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
            @Tag(name = "Image resource", description = "API related to Images")
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
@Path("image")
public class ImageResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
    private static final MultiFacade<Image> IMAGE_FACADE = new MultiFacade(Image.class, EMF);

    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    //@RolesAllowed({"user", "admin"})
    @Operation(summary = "Basic API Response: Image",
            tags = {"Image Resource"},
            responses = {
                @ApiResponse(responseCode = "200", description = "The user successfully connected"),
                @ApiResponse(responseCode = "400", description = "The server cannot or will not process the request")})
    public String connectionCheck() {
        return "{\"msg\":\"Connection to the 'Image' api section is online\"}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @RolesAllowed({"user", "admin"})
    @Operation(summary = "Image By ID",
            tags = {"Image Resource"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ImageDTO.class))),
                @ApiResponse(responseCode = "200", description = "The requested resources was returned"),
                @ApiResponse(responseCode = "400", description = "The server cannot or will not process the request and no resources were returned")})
    public ImageDTO getRest(@PathParam("id") int id) throws IOException, InterruptedException, ExecutionException {
        return new ImageDTO(((Image) IMAGE_FACADE.find((long) (int) id)));
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "All Images",
            tags = {"Image  Resource"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ImageDTO.class))),
                @ApiResponse(responseCode = "200", description = "The requested Image resources were returned"),
                @ApiResponse(responseCode = "400", description = "The server cannot or will not process the request and no resources were returned")})
    public List<ImageDTO> getAllRest() throws IOException, InterruptedException, ExecutionException {
        List<ImageDTO> dpDTOList = new ArrayList<>();
        for (Object dp : IMAGE_FACADE.findAll()) {
            dpDTOList.add(new ImageDTO(((Image) dp)));
        }
        return dpDTOList;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    //  @Path("/create")
    @Operation(summary = "Image Creation",
            tags = {"Image Resource"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ImageDTO.class))),
                @ApiResponse(responseCode = "200", description = "The person was created and persisted"),
                @ApiResponse(responseCode = "400", description = "No users was created or persisted")})
    public void createRest(Image entity) {
        IMAGE_FACADE.create(entity);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    // @RolesAllowed("admin")
    @Operation(summary = "Image Deletion", tags = {"Image  Resource"},
            responses = {
                @ApiResponse(responseCode = "200", description = "Image Deleted"),
                @ApiResponse(responseCode = "400", description = "Not all arguments provided to delete")
            })
    public void deleteRest(@PathParam("id") Long id) {
        IMAGE_FACADE.remove(id);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    //@RolesAllowed("admin")
    @Operation(summary = "Image Editing", tags = {"Image  Resource"},
            responses = {
                @ApiResponse(responseCode = "200", description = "Image Edited"),
                @ApiResponse(responseCode = "400", description = "Not all arguments provided with the body to edit")
            })
    public void editRest(Image entity) {
        IMAGE_FACADE.edit(entity);
    }
}
