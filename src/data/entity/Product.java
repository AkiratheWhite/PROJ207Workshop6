package data.entity;

import java.lang.reflect.Field;
import java.util.HashMap;

public class Product implements Entity
{
				/**
					* Parameters corresponding to the columns in the Agents table..
					*/
				private Integer ProductId;
				private String ProdName;
				
				/**
					* Empty constructor for initializing empty Product object.
					*/
				public Product(){ }
				
				/**
					* Constructor for Agent object.
					* @param productId Product ID.
					* @param prodName Product Name.
					*/
				public Product(Integer productId, String prodName)
				{
								ProductId = productId;
								ProdName = prodName;
				}
				
				public int getProductId()
				{
								return ProductId;
				}
				
				public String getProdName()
				{
								return ProdName;
				}
				
				@Override
				public String toString()
				{
								return ProdName;
				}
				
				public HashMap<String, Object> allProps() throws IllegalAccessException {
								HashMap<String, Object> Props = new HashMap<>();
								
								for (Field Property : Product.class.getDeclaredFields()) {
												Property.setAccessible(true);
												Props.put(Property.getName(), Property.get(this));
								}
								
								return Props;
				}
				
				public Object getPrimaryKey() {
								return this.ProductId;
				}
}
