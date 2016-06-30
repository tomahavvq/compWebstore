package computer.webstore.web.rest.dto;

import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the Address entity.
 */
public class AddressDTO implements Serializable {

    private Long id;

    private String city;

    private String zipCode;

    private String street;

    private String streetNumber;

    private String localNumber;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }
    public String getLocalNumber() {
        return localNumber;
    }

    public void setLocalNumber(String localNumber) {
        this.localNumber = localNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AddressDTO addressDTO = (AddressDTO) o;

        if ( ! Objects.equals(id, addressDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AddressDTO{" +
            "id=" + id +
            ", city='" + city + "'" +
            ", zipCode='" + zipCode + "'" +
            ", street='" + street + "'" +
            ", streetNumber='" + streetNumber + "'" +
            ", localNumber='" + localNumber + "'" +
            '}';
    }
}
