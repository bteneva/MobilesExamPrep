package softuni.exam.models.dto;


import com.google.gson.annotations.Expose;
import org.hibernate.validator.constraints.Length;

public class SellerImportDto {

//      {
//    "firstName": "John",
//    "lastName": "Harrison",
//    "personalNumber": "123123"
//  }
    @Expose
    @Length(min = 2, max = 30)
    private String firstName;
    @Expose
    @Length(min = 2, max = 30)
    private String lastName;
    @Expose
    @Length(min = 3, max = 6)
    private String personalNumber;

    public SellerImportDto() {
    }

    public @Length(min = 2, max = 30) String getFirstName() {
        return firstName;
    }

    public void setFirstName(@Length(min = 2, max = 30) String firstName) {
        this.firstName = firstName;
    }

    public @Length(min = 2, max = 30) String getLastName() {
        return lastName;
    }

    public void setLastName(@Length(min = 2, max = 30) String lastName) {
        this.lastName = lastName;
    }

    public @Length(min = 3, max = 6) String getPersonalNumber() {
        return personalNumber;
    }

    public void setPersonalNumber(@Length(min = 3, max = 6) String personalNumber) {
        this.personalNumber = personalNumber;
    }
}
