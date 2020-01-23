package rest;

import dto.BookingDTO;
import entities.Booking;
import entities.Image;
import entities.Kayak;
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
import java.util.ArrayList;
import java.util.Date;
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
            @Tag(name = "Booking Resource", description = "API related to Bookings")
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
@Path("booking")
public class BookingResource {
    
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
    private static final MultiFacade<Booking> BOOKING_FACADE = new MultiFacade(Booking.class, EMF);
    private static final MultiFacade<Kayak> KAYAK_FACADE = new MultiFacade(Kayak.class, EMF);
    private static final MultiFacade<Image> IMAGE_FACADE = new MultiFacade(Image.class, EMF);
    private static final MultiFacade<User> USER_FACADE = new MultiFacade(User.class, EMF);
    
    @Context
    private UriInfo context;
    
    @Context
    SecurityContext securityContext;
    
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    //@RolesAllowed({"user", "admin"})
    @Operation(summary = "Basic API Response: Booking",
            tags = {"Booking Resource"},
            responses = {
                @ApiResponse(responseCode = "200", description = "The user successfully connected"),
                @ApiResponse(responseCode = "400", description = "The server cannot or will not process the request")})
    public String connectionCheck() {
        return "{\"msg\":\"Connection to the 'Booking' api section is online\"}";
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @RolesAllowed({"user", "admin"})
    @Operation(summary = "Booking By ID",
            tags = {"Booking Resource"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookingDTO.class))),
                @ApiResponse(responseCode = "200", description = "The requested resources was returned"),
                @ApiResponse(responseCode = "400", description = "The server cannot or will not process the request and no resources were returned")})
    public BookingDTO getRest(@PathParam("id") int id) throws IOException, InterruptedException, ExecutionException {
        return new BookingDTO(((Booking) BOOKING_FACADE.find((long) (int) id)));
    }
    
    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "All Bookings",
            tags = {"Booking Resource"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookingDTO.class))),
                @ApiResponse(responseCode = "200", description = "The requested Booking resources were returned"),
                @ApiResponse(responseCode = "400", description = "The server cannot or will not process the request and no resources were returned")})
    public List<BookingDTO> getAllRest() throws IOException, InterruptedException, ExecutionException {
        List<BookingDTO> dpDTOList = new ArrayList<>();
        for (Object dp : BOOKING_FACADE.findAll()) {
            dpDTOList.add(new BookingDTO(((Booking) dp)));
        }
        return dpDTOList;
    }
    
    @PUT
    @Path("/pop/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Populate Bookings",
            tags = {"Booking Resource"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookingDTO.class))),
                @ApiResponse(responseCode = "200", description = "The requested Booking resources were returned"),
                @ApiResponse(responseCode = "400", description = "The server cannot or will not process the request and no resources were returned")})
    public String populate(@PathParam("id") String id) throws IOException, InterruptedException, ExecutionException {
        User user = (User) USER_FACADE.find(id);
        
        Image image01 = new Image("https://3.bp.blogspot.com/-jkkWjthJZH8/UWrtwFbqLwI/AAAAAAAAA2w/3QQtV9fOip4/s1600/IMG_0816.JPG");
        Image image02 = new Image("https://i.ytimg.com/vi/7HWSVU5uucY/maxresdefault.jpg");
        Image image03 = new Image("http://www.boatstogo.com/images/Kayaks/inflatable-kayak-OK420/inflatable-kayak-OK420-15.JPG");
        
        Kayak kayak01 = new Kayak("Kay", "Boat", "sails", "913", "dark yellow", 2);
        Kayak kayak02 = new Kayak("Mark", "Destroyer", "Destroys", "2000", "red", 2);
        Kayak kayak03 = new Kayak("just a boat", "kayak 9000 special edition", "The best if you're into sinking", "2005", "dark black", 11);
        
        kayak01.setImageList(Stream.of(image01).collect(Collectors.toList()));
        kayak02.setImageList(Stream.of(image02).collect(Collectors.toList()));
        kayak03.setImageList(Stream.of(image03).collect(Collectors.toList()));
        
        Booking booking01 = new Booking(new Date(2012, 1, 11));
        Booking booking02 = new Booking(new Date(1992, 2, 2));
        Booking booking03 = new Booking(new Date(1232, 3, 5));
        
        booking01.setKayakList(Stream.of(kayak01).collect(Collectors.toList()));
        booking02.setKayakList(Stream.of(kayak02).collect(Collectors.toList()));
        booking03.setKayakList(Stream.of(kayak03).collect(Collectors.toList()));
        
        Stream.of(
                image01,
                image02,
                image03
        ).forEach(o -> {
            IMAGE_FACADE.create(o);
        });
        
        
        //possibly of no importance
        Stream.of(
                kayak01,
                kayak02,
                kayak03
        ).forEach(o -> {
            KAYAK_FACADE.create(o);
        });
        
        Stream.of(
                booking01,
                booking02,
                booking03
        ).forEach(o -> {
            BOOKING_FACADE.create(o);
        });
        
        user.setBookingList(
                Stream.of(
                        booking01,
                        booking02,
                        booking03
                ).collect(Collectors.toList())
        );
        USER_FACADE.edit(user);
        return "{\"msg\":\"Populated\"}";
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    //  @Path("/create")
    @Operation(summary = "Booking Creation",
            tags = {"Booking Resource"},
            responses = {
                @ApiResponse(
                        content = @Content(mediaType = "application/json", schema = @Schema(implementation = BookingDTO.class))),
                @ApiResponse(responseCode = "200", description = "The person was created and persisted"),
                @ApiResponse(responseCode = "400", description = "No users was created or persisted")})
    public void createRest(Booking entity) {
        BOOKING_FACADE.create(entity);
    }
    
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    // @RolesAllowed("admin")
    @Operation(summary = "Booking Deletion", tags = {"Booking Resource"},
            responses = {
                @ApiResponse(responseCode = "200", description = "Booking Deleted"),
                @ApiResponse(responseCode = "400", description = "Not all arguments provided to delete")
            })
    public void deleteRest(@PathParam("id") Long id) {
        BOOKING_FACADE.remove(id);
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    //@RolesAllowed("admin")
    @Operation(summary = "Booking Editing", tags = {"Booking Resource"},
            responses = {
                @ApiResponse(responseCode = "200", description = "Booking Edited"),
                @ApiResponse(responseCode = "400", description = "Not all arguments provided with the body to edit")
            })
    public void editRest(Booking entity) {
        BOOKING_FACADE.edit(entity);
    }
}
