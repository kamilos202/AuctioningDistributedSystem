import java.io.Serializable;

public class Auction implements Serializable{
    private int auctionId;// set at the server side
    private String itemTitle;
    private String itemDescription;
    
    private int ownerId;
    private double reservePrice;
    private double currentBid = 0;

    private int highestBidderId = -1;
    private String highestBidderEmail = "";
    private String highestBidderName = "";

    private boolean ifActive;

    public Auction(int ownerId, String itemTitle, String itemDescription, double reservePrice, double startingPrice){
        this.ownerId = ownerId;
        this.reservePrice = reservePrice;
        currentBid = startingPrice;
        this.itemTitle = itemTitle;
        this.itemDescription = itemDescription;
        //this.auctionItem = new AuctionItem(itemTitle, itemDescription);
        ifActive = true;
    }


    public String getHighestBidderName() {
        return highestBidderName;
    }

    public void setHighestBidderName(String highestBidderName) {
        this.highestBidderName = highestBidderName;
    }

    public String getHighestBidderEmail() {
        return highestBidderEmail;
    }

    public void setHighestBidderEmail(String highestBidderEmail) {
        this.highestBidderEmail = highestBidderEmail;
    }

    public int getHighestBidderId() {
        return highestBidderId;
    }

    public void setHighestBidderId(int highestBidderId) {
        this.highestBidderId = highestBidderId;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public int getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(int auctionId) {
        this.auctionId = auctionId;
    }

    public double getReservePrice() {
        return reservePrice;
    }

    public void setReservePrice(double reservePrice) {
        this.reservePrice = reservePrice;
    }

    public double getCurrentBid() {
        return currentBid;
    }

    public void setCurrentBid(double currentBid) {
        this.currentBid = currentBid;
    }



    public String getAuctionInfo(){
        return "\n**********\n\nAuction ID: " + auctionId + "\nCurrent Bid: " + currentBid + "\nItem title: "+ itemTitle + "\nItem description: " + itemDescription +  "\n\n**********\n";
    }


}
