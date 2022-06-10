package hellojpa.domain;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Address {

    private String city;
    private String street;
    private String zipcode;


    // 기본생성자 만들어줘야함
    public Address(){

    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getZipcode() {
        return zipcode;
    }
    // 아예 setter설계 안하던가, private으로 바꿔버리기


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(city, address.city)
                && Objects.equals(street, address.street)
                && Objects.equals(zipcode, address.zipcode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, street, zipcode);
    }
}
