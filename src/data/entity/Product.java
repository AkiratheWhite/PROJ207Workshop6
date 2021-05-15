package data.entity;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 *  Code Written by: Texin D. Otinguey 
 *  * Changelog:
 *  * [1.01|Tony Li|05/06/21] - Removed override getProps method.
 */
public class Product implements Entity {
    /**
     * Parameters corresponding to the columns in the Product table..
     */
    private Integer ProductId;
    private String ProdName;

    /**
     * Empty constructor for initializing empty Product object.
     */
    public Product() {
    }

    /**
     * Constructor for Product object.
     *
     * @param productId Product ID.
     * @param prodName  Product Name.
     */
    public Product(Integer productId, String prodName) {
        ProductId = productId;
        ProdName = prodName;
    }

    public int getProductId() {
        return ProductId;
    }

    public String getProdName() {
        return ProdName;
    }

    @Override
    public String toString() {
        return ProdName;
    }


    public Object getPrimaryKey() {
        return this.ProductId;
    }
}
