package hellojpa.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable // 값타입이니까
public class Address {

    @Column(length = 10) // 열자리 까지 적어라
    private String city;
    @Column(length = 20)
    private String street;
    @Column(length = 5)
    private String zipcode;

    // 이런식으로 비지니스 메서드 만들기 가능
    public String fullAddress(){
        return getCity()+" "+getStreet()+" "+getZipcode();
    }

    public boolean isValid(){
        return true;
    }


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
    private void setCity(String city) {
        this.city = city;
    }

    private void setStreet(String street) {
        this.street = street;
    }

    private void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    // Uses getters during code generation 체크해서 만들어주기
    // -> getter에 접근가능! 선택 안하면 필드에 직접 접근하는데 이 때 프록시일때는 계산이 안됨.

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(getCity(), address.getCity())
                && Objects.equals(getStreet(), address.getStreet())
                && Objects.equals(getZipcode(), address.getZipcode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCity(), getStreet(), getZipcode());
    }
}
