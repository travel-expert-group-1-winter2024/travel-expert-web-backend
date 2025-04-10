package org.example.travelexpertwebbackend.dto;

public class PaymentRequest {
        private String tripType;
        private int travellers;
        private int packageId;
        private int packagePrice;

        // Getters and setters
        public String getTripType() {
            return tripType;
        }

        public void setTripType(String tripType) {
            this.tripType = tripType;
        }

        public int getTravellers() {
            return travellers;
        }

        public void setTravellers(int travellers) {
            this.travellers = travellers;
        }

        public int getPackageId() {
            return packageId;
        }

        public void setPackageId(int packageId) {
            this.packageId = packageId;
        }

        public int getPackagePrice(){
            return packagePrice;
        }

    public void setPackagePrice(int packagePrice) {
        this.packagePrice = packagePrice;
    }
}
