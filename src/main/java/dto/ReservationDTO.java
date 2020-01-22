package dto;

import entities.Kayak;
import entities.Reservation;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Henning
 */
public class ReservationDTO {

    private Date dateFrom;
    private Date dateTo;
    private List<Kayak> kayakList;

    public ReservationDTO(Reservation reservation) {
        this.dateFrom = reservation.getDateFrom();
        this.dateTo = reservation.getDateTo();
        this.kayakList = reservation.getKayakList();
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public List<Kayak> getKayakList() {
        return kayakList;
    }

    public void setKayakList(List<Kayak> kayakList) {
        this.kayakList = kayakList;
    }
    
    
    
}
