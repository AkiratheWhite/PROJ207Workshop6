package data.entity;

import java.util.HashMap;
import java.lang.reflect.Field;


/**
 * Code written by: Tony (Zongzheng) Li
 * Last Modified:
 */

public class Agent implements Entity{
    /**
     * Parameters corresponding to the columns in the Agents table..
     */
    private Integer AgentId;
    private String AgtFirstName;
    private String AgtMiddleInitial;
    private String AgtLastName;
    private String AgtBusPhone;
    private String AgtEmail;
    private String AgtPosition;
    private Integer AgencyId;

    /**
     * Empty constructor for initializing empty Agent object.
     */
    public Agent() {}

    /**
     * Constructor for Agent object.
     * @param agentId Agent's Agent ID number.
     * @param firstName Agent's first name.
     * @param middleInitial Agent's middle initial.
     * @param lastName Agent's last name.
     * @param busPhone Agent's business phone number.
     * @param email Agent's email address.
     * @param position Agent's position/title.
     * @param agencyId Agent's Agency ID number.
     */
    public Agent(Integer agentId, String firstName, String middleInitial, String lastName, String busPhone, String email, String position, Integer agencyId) {
        AgentId = agentId;
        AgtFirstName = firstName;
        AgtMiddleInitial = middleInitial;
        AgtLastName = lastName;
        AgtBusPhone = busPhone;
        AgtEmail = email;
        AgtPosition = position;
        AgencyId = agencyId;
    }

    public int getAgentId() {
        return AgentId;
    }

    public String getAgtFirstName() {
        return AgtFirstName;
    }

    public String getAgtMiddleInitial() {
        return AgtMiddleInitial;
    }

    public String getAgtLastName() {
        return AgtLastName;
    }

    public String getAgtBusPhone() {
        return AgtBusPhone;
    }

    public String getAgtEmail() {
        return AgtEmail;
    }

    public String getAgtPosition() {
        return AgtPosition;
    }

    public int getAgencyId() {
        return AgencyId;
    }

    @Override
    public String toString() {
        if (this.AgtMiddleInitial == null || this.AgtMiddleInitial.equals("")) {
            return this.AgtFirstName + " " + this.AgtLastName;
        } else {
            return this.AgtFirstName + " " + this.AgtMiddleInitial + " " + this.AgtLastName;
        }
    }

    public HashMap<String, Object> allProps() throws IllegalAccessException {
        HashMap<String, Object> Props = new HashMap<>();

        for (Field Property : Agent.class.getDeclaredFields()) {
            Property.setAccessible(true);
            Props.put(Property.getName(), Property.get(this));
        }

        return Props;
    }

    public Object getPrimaryKey() {
        return this.AgentId;
    }
}