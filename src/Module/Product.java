package Module;

public class Product {

    private String pName;
    private String pID;
    private int pQuantity;
    private int pBasePrice;
    private String pSuplier;

    /**
     * Constructor for product
     * @param pName
     * @param pID
     * @param pQuantity
     * @param pBasePrice
     * @param pSuplier
     */

    public Product(String pName, String pID, int pQuantity, int pBasePrice, String pSuplier){
        this.pName = pName;
        this.pID = pID;
        this.pQuantity = pQuantity;
        this.pBasePrice = pBasePrice;
        this.pSuplier = pSuplier;
    }

    public void decreaseQuantity(String pID, int decreasingQuantity){
        pQuantity = pQuantity - decreasingQuantity;
    }
    public void increaseQuantity(String pID, int increaseQuantity){
        pQuantity = pQuantity + increaseQuantity;
    }

}
