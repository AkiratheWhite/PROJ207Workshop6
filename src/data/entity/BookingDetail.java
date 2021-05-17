package data.entity;

import java.time.LocalDate;
import java.util.Date;

public class BookingDetail implements Entity{
    private Integer BookingDetailId;
    private Double ItineraryNo;
    private String TripStart;
    private String TripEnd;
    private String Description;
    private String Destination;
    private Double BasePrice;
    private Double AgencyCommission;
    private Integer BookingId;
    private String RegionId;
    private String ClassId;
    private String FeeId;
    private Integer ProductSupplierId;

    public BookingDetail(Integer bookingDetailId, Double itineraryNo, String tripStart, String tripEnd, String description, String destination, Double basePrice, Double agencyCommission, Integer bookingId, String regionId, String classId, String feeId, Integer productSupplierId) {
        BookingDetailId = bookingDetailId;
        ItineraryNo = itineraryNo;
        TripStart = tripStart;
        TripEnd = tripEnd;
        Description = description;
        Destination = destination;
        BasePrice = basePrice;
        AgencyCommission = agencyCommission;
        BookingId = bookingId;
        RegionId = regionId;
        ClassId = classId;
        FeeId = feeId;
        ProductSupplierId = productSupplierId;
    }

    public Integer getBookingDetailId() {
        return BookingDetailId;
    }

    public Double getItineraryNo() {
        return ItineraryNo;
    }

    public String getTripStart() {
        return TripStart;
    }

    public String getTripEnd() {
        return TripEnd;
    }

    public String getDescription() {
        return Description;
    }

    public String getDestination() {
        return Destination;
    }

    public Double getBasePrice() {
        return BasePrice;
    }

    public Double getAgencyCommission() {
        return AgencyCommission;
    }

    public Integer getBookingId() {
        return BookingId;
    }

    public String getRegionId() {
        return RegionId;
    }

    public String getClassId() {
        return ClassId;
    }

    public String getFeeId() {
        return FeeId;
    }

    public Integer getProductSupplierId() {
        return ProductSupplierId;
    }

    @Override
    public String toString() {
        return "BookingDetailId=" + BookingDetailId +
                ", Description=" + Description ;
    }

    @Override
    public Object getPrimaryKey() {
        return this.BookingDetailId;
    }
}
