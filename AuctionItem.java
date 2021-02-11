/**
 * AuctionItem is the entity class which represents either the auction itself or auction item
 */
public class AuctionItem {

    private String itemTitle;
    private String itemDescription;
    
    /**
     * Constructor of the AuctionItem
     * @param itemTitle
     * @param itemDescription
     */
    protected AuctionItem(String itemTitle, String itemDescription) {
        this.itemTitle = itemTitle;
        this.itemDescription = itemDescription;
    }


    protected String getItemTitle() {
        return itemTitle;
    }

    protected void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    protected String getItemDescription() {
        return itemDescription;
    }

    protected void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }
    /**
     * @return info about the item
     */
    protected String getItemInfo(){
        return  "\nItem title: "+ itemTitle + "\nItem description: " + itemDescription +  "\n\n**********\n";
    }
}
