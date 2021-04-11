package data.entity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Agent {
    /**
     * Parameters corresponding to the columns in the Agents table..
     */
    private Integer AgentId;
    private String FirstName;
    private String MiddleInitial;
    private String LastName;
    private String BusPhone;
    private String Email;
    private String Position;
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
     * @param position Agent's positon/title.
     * @param agencyId Agent's Agency ID number.
     */
    public Agent(Integer agentId, String firstName, String middleInitial, String lastName, String busPhone, String email, String position, Integer agencyId) {
        AgentId = agentId;
        FirstName = firstName;
        MiddleInitial = middleInitial;
        LastName = lastName;
        BusPhone = busPhone;
        Email = email;
        Position = position;
        AgencyId = agencyId;
    }

    public int getAgentId() {
        return AgentId;
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getMiddleInitial() {
        return MiddleInitial;
    }

    public String getLastName() {
        return LastName;
    }

    public String getBusPhone() {
        return BusPhone;
    }

    public String getEmail() {
        return Email;
    }

    public String getPosition() {
        return Position;
    }

    public int getAgencyId() {
        return AgencyId;
    }

    @Override
    public String toString() {
        if (this.MiddleInitial == "" || this.MiddleInitial == null) {
            return this.FirstName + " " + this.LastName;
        } else {
            return this.FirstName + " " + this.MiddleInitial + " " + this.LastName;
        }
    }
}