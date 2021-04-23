package data.entity;
/** Customer Class
 * created by: Chester Solang
 */
public class Customer implements Entity {
    /**
     * Parameters corresponding to the columns in the Customers table..
     */
    private Integer CustomerId;
    private String CustFirstName;
    private String CustLastName;
    private String CustAddress;
    private String CustCity;
    private String CustProv;
    private String CustPostal;
    private String CustCountry;
    private String CustHomePhone;
    private String CustBusPhone;
    private String CustEmail;
    private Integer AgentId;

    /**
     * Empty constructor for initializing empty Customer object.
     */
    public Customer() {}

    /**
     * Constructor for Customer object.
     * @param customerId Customer ID number.
     * @param custFirstName Customer's first name.
     * @param custLastName Customer's last name.
     * @param custAddress Customer's address.
     * @param custCity Customer's city.
     * @param custProv Customer's province.
     * @param custPostal Customer's postal code.
     * @param custCountry Customer's country.
     * @param custHomePhone Customer's home phone number.
     * @param custBusPhone Customer's business phone number.
     * @param custEmail Customer's email.
     * @param agentId agent ID associated with the customer.
     */
    public Customer(Integer customerId, String custFirstName, String custLastName, String custAddress, String custCity, String custProv, String custPostal, String custCountry, String custHomePhone, String custBusPhone, String custEmail, Integer agentId) {
        CustomerId = customerId;
        CustFirstName = custFirstName;
        CustLastName = custLastName;
        CustAddress = custAddress;
        CustCity = custCity;
        CustProv = custProv;
        CustPostal = custPostal;
        CustCountry = custCountry;
        CustHomePhone = custHomePhone;
        CustBusPhone = custBusPhone;
        CustEmail = custEmail;
        AgentId = agentId;
    }

    //getters and setters
    public Integer getCustomerId() {
        return CustomerId;
    }
    public void setCustomerId(Integer customerId) {
        CustomerId = customerId;
    }
    public String getCustFirstName() {
        return CustFirstName;
    }
    public void setCustFirstName(String custFirstName) {
        CustFirstName = custFirstName;
    }
    public String getCustLastName() {
        return CustLastName;
    }
    public void setCustLastName(String custLastName) {
        CustLastName = custLastName;
    }
    public String getCustAddress() {
        return CustAddress;
    }
    public void setCustAddress(String custAddress) {
        CustAddress = custAddress;
    }
    public String getCustCity() {
        return CustCity;
    }
    public void setCustCity(String custCity) {
        CustCity = custCity;
    }
    public String getCustProv() {
        return CustProv;
    }
    public void setCustProv(String custProv) {
        CustProv = custProv;
    }
    public String getCustPostal() {
        return CustPostal;
    }
    public void setCustPostal(String custPostal) {
        CustPostal = custPostal;
    }
    public String getCustCountry() {
        return CustCountry;
    }
    public void setCustCountry(String custCountry) {
        CustCountry = custCountry;
    }
    public String getCustHomePhone() {
        return CustHomePhone;
    }
    public void setCustHomePhone(String custHomePhone) {
        CustHomePhone = custHomePhone;
    }
    public String getCustBusPhone() {
        return CustBusPhone;
    }
    public void setCustBusPhone(String custBusPhone) {
        CustBusPhone = custBusPhone;
    }
    public String getCustEmail() {
        return CustEmail;
    }
    public void setCustEmail(String custEmail) {
        CustEmail = custEmail;
    }
    public Integer getAgentId() {
        return AgentId;
    }
    public void setAgentId(Integer agentId) {
        AgentId = agentId;
    }

    //toString Method
    @Override
    public String toString() {
        return CustFirstName +  CustLastName;
    }

    public Object getPrimaryKey() {
        return this.CustomerId;
    }

}//class customer
