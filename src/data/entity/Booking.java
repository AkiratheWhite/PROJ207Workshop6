package data.entity;

import java.util.Date;
/**
 * Code written by:Dhval shah
 * Last modified on (MM/DD/YY): 05/14/21
 */
public class Booking implements Entity {
    private Integer BookingId;
    private Date BookingDate;
    private String BookingNo;
    private Integer TravelerCount;
    private Integer CustomerId;
    private String TripTypeId;
    private Integer PackageId;

    public Booking(Integer bookingId, Date bookingDate, String bookingNo, Integer travelerCount, Integer customerId, String tripTypeId, Integer packageId) {
        BookingId = bookingId;
        BookingDate = bookingDate;
        BookingNo = bookingNo;
        TravelerCount = travelerCount;
        CustomerId = customerId;
        TripTypeId = tripTypeId;
        PackageId = packageId;
    }

    public Integer getBookingId() {
        return BookingId;
    }

    public void setBookingId(Integer bookingId) {
        BookingId = bookingId;
    }

    public Date getBookingDate() {
        return BookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        BookingDate = bookingDate;
    }

    public String getBookingNo() {
        return BookingNo;
    }

    public void setBookingNo(String bookingNo) {
        BookingNo = bookingNo;
    }

    public Integer getTravelerCount() {
        return TravelerCount;
    }

    public void setTravelerCount(Integer travelerCount) {
        TravelerCount = travelerCount;
    }

    public Integer getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(Integer customerId) {
        CustomerId = customerId;
    }

    public String getTripTypeId() {
        return TripTypeId;
    }

    public void setTripTypeId(String tripTypeId) {
        TripTypeId = tripTypeId;
    }

    public Integer getPackageId() {
        return PackageId;
    }

    public void setPackageId(Integer packageId) {
        PackageId = packageId;
    }

    @Override
    public String toString() {
        if (this.TripTypeId.equals("B")) {
            return " Booking ID:"+this.getBookingId() + " Booking Date:" + this.getBookingDate() + "  Business Trip CustomerID->" + this.CustomerId;
        }
        else if(this.TripTypeId.equals("G")){
            return " Booking ID:"+this.getBookingId() + " Booking Date:" + this.getBookingDate() + "  Group Trip of people="+this.TravelerCount +" CustomerID->"+ this.CustomerId;
        }
        else
        {
            return " Booking ID:"+this.getBookingId() + " Booking Date:" + this.getBookingDate() + "  Leisure Trip CustomerID->" + this.CustomerId;
        }
    }

    public Object getPrimaryKey() {
        return this.BookingId;
    }
}

