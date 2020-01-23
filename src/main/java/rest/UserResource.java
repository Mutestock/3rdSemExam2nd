package rest;

import dto.UserDTO;
import entities.Role;
import entities.User;
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
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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

@OpenAPIDefinition(
        info = @Info(
                title = "3rdSemExam",
                version = "0.1",
                description = "Backend of the CA3 project"
        ),
        tags = {
            @Tag(name = "User Management Resource", description = "API containing various functionalities connected to handling users")
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
@Path("user")
public class UserResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
    private static final MultiFacade<User> USER_FACADE = new MultiFacade(User.class, EMF);
    private static final MultiFacade<Role> ROLE_FACADE = new MultiFacade(Role.class, EMF);

    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    //@RolesAllowed({"user", "admin"})
    @Operation(summary = "Basic API Response: User",
            tags = {"User Resource"},
            responses = {
                @ApiResponse(responseCode = "200", description = "The user successfully connected"),
                @ApiResponse(responseCode = "400", description = "The server cannot or will not process the request")})
    public String connectionCheck() {
        return "{\"msg\":\"Connection to the 'User' api section is online\"}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    // @RolesAllowed({"user", "admin"})
    @Operation(summary = "Read User by ID",
            tags = {"User Resource"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
                @ApiResponse(responseCode = "200", description = "The requested resources was returned"),
                @ApiResponse(responseCode = "400", description = "The server cannot or will not process the request and no resources were returned")})
    public UserDTO getUser(@PathParam("id") String id) throws IOException, InterruptedException, ExecutionException {
        return new UserDTO(((User) USER_FACADE.find(id)));
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"admin"})
    @Operation(summary = "Read all users",
            tags = {"User Resource"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
                @ApiResponse(responseCode = "200", description = "The requested User resources were returned"),
                @ApiResponse(responseCode = "400", description = "The server cannot or will not process the request and no resources were returned")})
    public List<UserDTO> getAllUsers() {
        return (List<UserDTO>) USER_FACADE.findAll()
                .stream()
                .map(User -> new UserDTO((User) User))
                .collect(Collectors.toList());
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    //@Path("")
    @Operation(summary = "Create User",
            tags = {"User Resource"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class))),
                @ApiResponse(responseCode = "200", description = "The person was created and persisted"),
                @ApiResponse(responseCode = "400", description = "No users was created or persisted")})
    public void createUser(User entity) {
        Role roleInsertion = (Role) ROLE_FACADE.find("user");
        if (roleInsertion == null) {
            ROLE_FACADE.create(new Role("user"));
        }
        if (entity.getRoleList().isEmpty()) {
            entity.setRoleList(
                    Stream.of(
                            roleInsertion
                    ).collect(Collectors.toList())
            );
        }
        USER_FACADE.create(entity);
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    // @RolesAllowed("admin")
    @Operation(summary = "Delete User", tags = {"User Resource"},
            responses = {
                @ApiResponse(responseCode = "200", description = "User Deleted"),
                @ApiResponse(responseCode = "400", description = "Not all arguments provided to delete")
            })
    public void deleteRest(@PathParam("id") Long id) {
        USER_FACADE.remove(id);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    //@RolesAllowed("admin")
    @Operation(summary = "Update User", tags = {"User Resource"},
            responses = {
                @ApiResponse(responseCode = "200", description = "User Edited"),
                @ApiResponse(responseCode = "400", description = "Not all arguments provided with the body to edit")
            })
    public void editRest(User entity) {
        USER_FACADE.edit(entity);
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    //@RolesAllowed("admin")
    @Path("/promote/{id}")
    @Operation(summary = "Promotes User to admin. For testing", tags = {"User Resource"},
            responses = {
                @ApiResponse(responseCode = "200", description = "User Edited"),
                @ApiResponse(responseCode = "400", description = "Not all arguments provided with the body to edit")
            })
    public void promote(@PathParam("id") String id) throws IOException, InterruptedException, ExecutionException {
        User user = (User) USER_FACADE.find(id);
        Role roleInsertion = (Role) ROLE_FACADE.find("admin");
        if (roleInsertion == null) {
            ROLE_FACADE.create(new Role("admin"));
        }
        user.addRole(roleInsertion);
        USER_FACADE.edit(user);
    }
}
