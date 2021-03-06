package pos.controller;

import pos.model.Sale;
import pos.integration.ItemRegistry;
import pos.integration.ItemDTO;
import pos.integration.ItemRegistryException;
import pos.model.InvalidIDException;
import pos.model.SaleDTO;
import pos.model.Payment;
import pos.model.SaleInfo;
import pos.model.TotalRevenue;
import pos.model.TotalRevenueObserver;


/**
 * Only controller in the application. All calls to the model pass through here.
 * 
 * @author Josh
 */
public class Controller {
    private Sale sale;
    private ItemRegistry itemRegistry;
    private ItemDTO itemDTO;
    private SaleDTO saleDTO;
    private final Payment payment = new Payment(saleDTO); 
    private TotalRevenue totalRevenue = new TotalRevenue();
    private SaleInfo saleInfo  = new SaleInfo();
    
    /**
     * Creates an empty instance of {@link Sale}, which will be used for all information regarding
     * the sale that is now started.
     */
    public void startSale() {
        sale = new Sale();
        itemRegistry = new ItemRegistry();
        
        
    }
    
    
    
    
    /**
     * Will register a valid item to the customers cart. 
     * @param itemID The numbers used to identify each item. 
     * @throws InvalidIDException if the itemID does not match a item. 
     * @throws OperationFailedException if unable to register item for some reason, example database failure. 
     * @return itemDTO that holds details about the item. 
     */
     public ItemDTO identifyAndRegItem(int itemID) throws InvalidIDException, Exception {
         
         try {
             itemDTO = itemRegistry.verifyItem(itemID);
             sale.addItem2Sale(itemDTO);
             sale.runningTotal();
             sale.showRunningTotal();
         } 
         
         catch(InvalidIDException e) {
                 System.out.println(e.getMessage());
                 }
         catch (ItemRegistryException eE) {
             System.out.println(eE.getMessage());
             throw new OperationFailedException("Could not search for the item.");
         }
         
         return itemDTO;
                
    }
    
    /**
     * 
     * @return The total cost of all items purchased including tax
     */
    public double fetchTotal() {
        return sale.totalWithTax();
    }
    
    /**
     * Observer gets total revenue. 
     * @param obs The observer to notify 
     */
    public void addTotalRevenueObserver(TotalRevenueObserver obs) {
        totalRevenue.addTotalRevenueObserver(obs);
    }
    
    /**
     * Ends up printing total revenue. 
     * @param saleDTO has the total. 
     */
    public void callTotalRevenue(SaleDTO saleDTO) {
        totalRevenue.revenue(saleDTO.getTotalWTax());
    }
    
    
    /**
     * 
     * @param amount The amount of gold given by the customer.
     */
    public void payment (double amount) {
         payment.payNChange(amount);
    }
    
    /**
     * 
     * @return The saleDTO containing the info on the receipt.
     */
    public SaleDTO getSaleDTO(){
        return payment.getSaleDTO();
    }
}
