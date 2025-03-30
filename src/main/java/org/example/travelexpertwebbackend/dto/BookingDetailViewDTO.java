//package org.example.travelexpertwebbackend.dto;
//
//import org.example.travelexpertwebbackend.entity.BookingDetailsView;
//import org.example.travelexpertwebbackend.entity.Fee;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.Date;
//
//public class BookingDetailViewDTO {
//    private Long bookingDetailId;
//    private int itineraryNo;
//    private LocalDateTime tripStart;
//    private LocalDateTime tripEnd;
//    private String description;
//    private String destination;
//    private BigDecimal basePrice;
//    private BigDecimal agencyCommission;
//    private Integer bookingId;
//    private Integer agentId;
//    private String region;
//    private String className;
//    private Fee fee;
//    private String product;
//    private String supplier;
//
//    // Default constructor
//    public BookingDetailViewDTO() {}
//
//    // Constructor for initializing with all properties
//    public BookingDetailViewDTO(Long bookingDetailId, int itineraryNo, LocalDateTime tripStart, Date tripEnd,
//                                String description, String destination, Double basePrice, Double agencyCommission,
//                                Integer bookingId, Integer agentId, String region, String className, Fee fee,
//                                String product, String supplier) {
//        this.bookingDetailId = bookingDetailId;
//        this.itineraryNo = itineraryNo;
//        this.tripStart = tripStart;
//        this.tripEnd = tripEnd;
//        this.description = description;
//        this.destination = destination;
//        this.basePrice = basePrice;
//        this.agencyCommission = agencyCommission;
//        this.bookingId = bookingId;
//        this.agentId = agentId;
//        this.region = region;
//        this.className = className;
//        this.fee = fee;
//        this.product = product;
//        this.supplier = supplier;
//    }
//
//    public BookingDetailViewDTO(BookingDetailsView bookingDetailsView) {
//    }
//
//    // Constructor to map from an entity
//    public void BookingDetailsViewDTO(BookingDetailsView bookingDetailsView) {
//        this.bookingDetailId = bookingDetailsView.getBookingDetailId();
//        this.itineraryNo = Integer.parseInt(bookingDetailsView.getItineraryNo());
//        this.tripStart = bookingDetailsView.getTripStart();
//        this.tripEnd = bookingDetailsView.getTripEnd();
//        this.description = bookingDetailsView.getDescription();
//        this.destination = bookingDetailsView.getDestination();
//        this.basePrice = bookingDetailsView.getBasePrice();
//        this.agencyCommission = bookingDetailsView.getAgencyCommission();
//        this.bookingId = bookingDetailsView.getBooking().getId();
//        this.agentId = bookingDetailsView.getAgentId();
//        this.region = bookingDetailsView.getRegion();
//        this.className = bookingDetailsView.getClassName();
//        this.fee = bookingDetailsView.getFee();
//        this.product = bookingDetailsView.getProduct();
//        this.supplier = bookingDetailsView.getSupplier();
//    }
//
//    // Getters and Setters
//    public Long getBookingDetailId() {
//        return bookingDetailId;
//    }
//
//    public void setBookingDetailId(Long bookingDetailId) {
//        this.bookingDetailId = bookingDetailId;
//    }
//
//    public int getItineraryNo() {
//        return itineraryNo;
//    }
//
//    public void setItineraryNo(int itineraryNo) {
//        this.itineraryNo = itineraryNo;
//    }
//
//    public LocalDateTime getTripStart() {
//        return tripStart;
//    }
//
//    public void setTripStart(LocalDateTime tripStart) {
//        this.tripStart = tripStart;
//    }
//
//    public Date getTripEnd() {
//        return tripEnd;
//    }
//
//    public void setTripEnd(Date tripEnd) {
//        this.tripEnd = tripEnd;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public String getDestination() {
//        return destination;
//    }
//
//    public void setDestination(String destination) {
//        this.destination = destination;
//    }
//
//    public Double getBasePrice() {
//        return basePrice;
//    }
//
//    public void setBasePrice(Double basePrice) {
//        this.basePrice = basePrice;
//    }
//
//    public Double getAgencyCommission() {
//        return agencyCommission;
//    }
//
//    public void setAgencyCommission(Double agencyCommission) {
//        this.agencyCommission = agencyCommission;
//    }
//
//    public Integer getBookingId() {
//        return bookingId;
//    }
//
//    public void setBookingId(Integer bookingId) {
//        this.bookingId = bookingId;
//    }
//
//    public Integer getAgentId() {
//        return agentId;
//    }
//
//    public void setAgentId(Integer agentId) {
//        this.agentId = agentId;
//    }
//
//    public String getRegion() {
//        return region;
//    }
//
//    public void setRegion(String region) {
//        this.region = region;
//    }
//
//    public String getClassName() {
//        return className;
//    }
//
//    public void setClassName(String className) {
//        this.className = className;
//    }
//
//    public Fee getFee() {
//        return fee;
//    }
//
//    public void setFee(Fee fee) {
//        this.fee = fee;
//    }
//
//    public String getProduct() {
//        return product;
//    }
//
//    public void setProduct(String product) {
//        this.product = product;
//    }
//
//    public String getSupplier() {
//        return supplier;
//    }
//
//    public void setSupplier(String supplier) {
//        this.supplier = supplier;
//    }
//}
