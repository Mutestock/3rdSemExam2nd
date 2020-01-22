package facades;

import entities.Booking;
import entities.Image;
import entities.Kayak;
import entities.User;
import entities.Reservation;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Henning
 */
public class MultiFacade<T> extends AbstractFacade {

    private static EntityManagerFactory emf;
    //Create BasicExtensionFacade with null value
    private static BasicExtensionFacade<Image> basicImageFacade;
    private static BasicExtensionFacade<User> basicUserFacade;
    private static BasicExtensionFacade<Booking> basicBookingFacade;
    private static BasicExtensionFacade<Kayak> basicKayakFacade;
    private static BasicExtensionFacade<Reservation> basicReservationFacade;

    public MultiFacade(Class entityClass) {
        super(entityClass);
    }

    public MultiFacade(Class entityClass, EntityManagerFactory emf) {
        super(entityClass);
        this.emf = emf;

        //Delete if switch under is fully functional
        //String simpleEntityClassName = ENTITY_CLASS.getSimpleName().toUpperCase();
        switch (ENTITY_CLASS.getSimpleName().toUpperCase()) {

            case "USER":
                basicBookingFacade = new BasicExtensionFacade(Booking.class, emf);
                break;

            case "BOOKING":
                basicUserFacade = new BasicExtensionFacade(User.class, emf);
                basicKayakFacade = new BasicExtensionFacade(Kayak.class, emf);
                break;

            case "KAYAK":
                basicBookingFacade = new BasicExtensionFacade(Booking.class, emf);
                basicReservationFacade = new BasicExtensionFacade(Reservation.class, emf);
                break;

            case "IMAGE":
                basicKayakFacade = new BasicExtensionFacade(Kayak.class, emf);
                break;

            case "RESERVATION":
                basicKayakFacade = new BasicExtensionFacade(Booking.class, emf);
                break;
        }
    }

    @Override
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    @Override
    public void remove(Object entity) {
        System.out.println(find(entity).getClass().getSimpleName().toUpperCase());
        Uprooter.valueOf(find(entity).getClass().getSimpleName().toUpperCase()).uproot(find(entity));
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.remove(em.find(ENTITY_CLASS, entity));
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    //This enum "uproots" entities to prevent parent/child removal collision
    private enum Uprooter {
        USER {
            @Override
            public void uproot(Object obj) {
                try {
                    for (Booking booking : ((List<Booking>) basicBookingFacade.findAll())) {
                        if (booking.getUser().getUserName().equals(((User) obj).getUserName())) {
                            booking.setUser(null);
                            basicBookingFacade.edit(booking);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Unable to uproot USER from BOOKING - " + e);
                }
            }
        },
        BOOKING {
            @Override
            public void uproot(Object obj) {
                try {
                    for (User user : ((List<User>) basicUserFacade.findAll())) {
                        for (Booking booking : user.getBookingList()) {
                            if (booking.getUser().getUserName().equals(((Booking) obj).getUser().getUserName())) {
                                List<Booking> switcheroo = user.getBookingList();
                                switcheroo.remove(obj);
                                user.setBookingList(switcheroo);
                                basicUserFacade.edit(user);
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Unable to uproot USER from BOOKING - " + e);
                }
                try {
                    for (Kayak kayak : ((List<Kayak>) basicKayakFacade.findAll())) {
                        if (kayak.getBooking().getId().equals(((Booking) obj).getId())) {
                            kayak.setBooking(null);
                            basicKayakFacade.edit(kayak);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Unable to uproot KAYAK from BOOKING - " + e);
                }
            }
        },
        KAYAK {
            @Override
            public void uproot(Object obj) {
                try {
                    for (Booking booking : ((List<Booking>) basicBookingFacade.findAll())) {
                        for (Kayak kayak : booking.getKayakList()) {
                            if (kayak.getId().equals(((Kayak) obj).getId())) {
                                List<Kayak> switcheroo = booking.getKayakList();
                                switcheroo.remove(obj);
                                booking.setKayakList(switcheroo);
                                basicBookingFacade.edit(booking);
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Unable to uproot Booking from KAYAK - " + e);
                }
                try {
                    for (Reservation reservation : ((List<Reservation>) basicReservationFacade.findAll())) {
                        for (Kayak kayak : reservation.getKayakList()) {
                            if (kayak.getId().equals(((Kayak) obj).getId())) {
                                List<Kayak> switcheroo = reservation.getKayakList();
                                switcheroo.remove(obj);
                                reservation.setKayakList(switcheroo);
                                basicReservationFacade.edit(reservation);
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Unable to uproot RESERVATION from KAYAK - " + e);
                }
            }
        },
        IMAGE {
            @Override
            public void uproot(Object obj) {
                for (Kayak kayak : ((List<Kayak>) basicKayakFacade.findAll())) {
                    for (Image image : kayak.getImageList()) {
                        if (image.getId().equals(((Image) obj).getId())) {
                            List<Image> switcheroo = kayak.getImageList();
                            switcheroo.remove(obj);
                            kayak.setImageList(switcheroo);
                            basicKayakFacade.edit(kayak);
                        }
                    }
                }
            }
        },
        RESERVATION {
            @Override
            public void uproot(Object obj) {
                try {
                    for (Kayak kayak : ((List<Kayak>) basicKayakFacade.findAll())) {
                        for (Reservation reservation : kayak.getReservationList()) {
                            if (reservation.getId().equals(((Reservation) obj).getId())) {
                                List<Reservation> switcheroo = kayak.getReservationList();
                                switcheroo.remove(obj);
                                kayak.setReservationList(switcheroo);
                                basicKayakFacade.edit(kayak);
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Unable to uproot KAYAK from RESERVATION- " + e);
                }
            }
        };

        public abstract void uproot(Object obj);
    }
}
