package data.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Code written by: Texin Otinguey
 *
 * Changelog:
 * [1.01|Tony Li|05/06/21] - Converted PkgStartDate and PkgEndDate to LocalDateTime from Timestamp to fix compile error. Removed override getProps method.
 */

public class Package implements Entity {
    /**
     * Parameters corresponding to the columns in the Travel Packages table..
     */
    private Integer PackageId;
    private String PkgName;
    private LocalDateTime PkgStartDate;
    private LocalDateTime PkgEndDate;
    private String PkgDesc;
    private BigDecimal PkgBasePrice;
    private BigDecimal PkgAgencyCommission;

    /**
     * Empty constructor for initializing empty Travel Package object.
     */
    public Package() {
    }

    /**
     * Constructor for Travel Package object.
     *
     * @param packageId           Travel Package ID number.
     * @param pkgName             Travel Package Name.
     * @param pkgStartDate        Travel Package start date.
     * @param pkgEndDate          Travel Package end date.
     * @param pkgDesc             Travel Package description.
     * @param pkgBasePrice        Travel Package base price.
     * @param pkgAgencyCommission Travel Package agency commission.
     */
    public Package(
            Integer packageId,
            String pkgName,
            LocalDateTime pkgStartDate,
            LocalDateTime pkgEndDate,
            String pkgDesc,
            BigDecimal pkgBasePrice,
            BigDecimal pkgAgencyCommission) {
        PackageId = packageId;
        PkgName = pkgName;
        PkgStartDate = pkgStartDate;
        PkgEndDate = pkgEndDate;
        PkgDesc = pkgDesc;
        PkgBasePrice = pkgBasePrice;
        PkgAgencyCommission = pkgAgencyCommission;
    }

    public int getPackageId() {
        return PackageId;
    }

    public String getPkgName() {
        return PkgName;
    }

    public LocalDateTime getPkgStartDate() {
        return PkgStartDate;
    }

    public LocalDateTime getPkgEndDate() {
        return PkgEndDate;
    }

    public String getPkgDesc() {
        return PkgDesc;
    }

    public Number getPkgBasePrice() {
        return PkgBasePrice;
    }

    public Number getPkgAgencyCommission() {
        return PkgAgencyCommission;
    }

    @Override
    public String toString() {
        return this.PkgName;
    }

    public Object getPrimaryKey() {
        return this.PackageId;
    }
}