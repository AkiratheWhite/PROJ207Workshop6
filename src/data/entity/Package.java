package data.entity;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;

/**
	* Code written by: Texin Otinguey
	*/

public class Package implements Entity{
				/**
					* Parameters corresponding to the columns in the Travel Packages table..
					*/
				private Integer PackageId;
				private String PkgName;
				private Timestamp PkgStartDate;
				private Timestamp PkgEndDate;
				private String PkgDesc;
				private BigDecimal PkgBasePrice;
				private BigDecimal PkgAgencyCommission;
				
				/**
					* Empty constructor for initializing empty Travel Package object.
					*/
				public Package() {}
				
				/**
					* Constructor for Travel Package object.
					* @param packageId Travel Package ID number.
					* @param pkgName Travel Package Name.
					* @param pkgStartDate Travel Package start date.
					* @param pkgEndDate Travel Package end date.
					* @param pkgDesc Travel Package description.
					* @param pkgBasePrice Travel Package base price.
					* @param pkgAgencyCommission Travel Package agency commission.
					*/
				public Package(
												Integer packageId,
												String pkgName,
												Timestamp pkgStartDate,
												Timestamp pkgEndDate,
												String pkgDesc,
												BigDecimal pkgBasePrice,
												BigDecimal pkgAgencyCommission)
				{
								PackageId = packageId;
								PkgName = pkgName;
								PkgStartDate = pkgStartDate;
								PkgEndDate = pkgEndDate;
								PkgDesc = pkgDesc;
								PkgBasePrice = pkgBasePrice;
								PkgAgencyCommission = pkgAgencyCommission;
				}
				
				public int getPackageId()
				{
								return PackageId;
				}
				
				public String getPkgName()
				{
								return PkgName;
				}
				
				public Timestamp  getPkgStartDate()
				{
								return PkgStartDate;
				}
				
				public Timestamp getPkgEndDate()
				{
								return PkgEndDate;
				}
				
				public String getPkgDesc()
				{
								return PkgDesc;
				}
				
				public Number getPkgBasePrice()
				{
								return PkgBasePrice;
				}
				
				public Number getPkgAgencyCommission()
				{
								return PkgAgencyCommission;
				}
				
				
				@Override
				public String toString() {
								return this.PkgName;
				}
				
				public HashMap<String, Object> allProps() throws IllegalAccessException {
								HashMap<String, Object> Props = new HashMap<>();
								
								for (Field Property : Package.class.getDeclaredFields()) {
												Property.setAccessible(true);
												Props.put(Property.getName(), Property.get(this));
								}
								
								return Props;
				}
				
				public Object getPrimaryKey() {
								return this.PackageId;
				}
}