package rest;

import dto.CommentDTO;
import entities.Role;
import entities.JSONPlaceholderComment;
import facades.MultiFacade;
import interfaces.IExternalAPIManagement;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
import utils.ExternalAPIManagement;

@OpenAPIDefinition(
        info = @Info(
                title = "3rdSemExam",
                version = "0.1",
                description = "Backend of the CA3 project"
        ),
        tags = {
            @Tag(name = "Placeholder External API Fetch Resource", description = "Placeholder External API Fetch Resource")
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
@Path("comment")
public class JSONPlaceholderCommentResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
    private final ExternalAPIManagement EM = new ExternalAPIManagement();

    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    //@RolesAllowed({"comment", "admin"})
    @Operation(summary = "Basic API Response: Comment",
            tags = {"Comment Resource"},
            responses = {
                @ApiResponse(responseCode = "200", description = "The comment successfully connected"),
                @ApiResponse(responseCode = "400", description = "The server cannot or will not process the request")})
    public String connectionCheck() {
        return "{\"msg\":\"Connection to the 'Comment' api section is online\"}";
    }

    //Currently rather pointless since Comments don't use IDs
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    // @RolesAllowed({"comment", "admin"})
    @Operation(summary = "Read Comment by ID",
            tags = {"Comment Resource"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentDTO.class))),
                @ApiResponse(responseCode = "200", description = "The requested resources was returned"),
                @ApiResponse(responseCode = "400", description = "The server cannot or will not process the request and no resources were returned")})
    public CommentDTO getComment(@PathParam("id") int id) throws IOException, InterruptedException, ExecutionException {
        JSONPlaceholderComment comment = EM.readObject(ExternalAPIManagement.ExternalURL.JSON_PLACEHOLDER
                .URLConcat("comments/" + id), JSONPlaceholderComment.class);
        return new CommentDTO(comment);
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Read all Comments",
            tags = {"Comment Resource"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommentDTO.class))),
                @ApiResponse(responseCode = "200", description = "The requested Comment resources were returned"),
                @ApiResponse(responseCode = "400", description = "The server cannot or will not process the request and no resources were returned")})
    public List<CommentDTO> getAllComments() {
        List<JSONPlaceholderComment> commentList = EM.readObjects(ExternalAPIManagement.ExternalURL.JSON_PLACEHOLDER
                .URLConcat("comments"), JSONPlaceholderComment.class);
        return commentList
                .stream()
                .map(comment -> new CommentDTO(comment))
                .collect(Collectors.toList());
    }
}
