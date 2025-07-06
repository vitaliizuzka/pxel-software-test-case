package software.pxel.pxelsoftwaretestcase.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Size(min = 8, max = 500)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmailData> emails = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PhoneData> phones = new ArrayList<>();
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Account account;

    public User() {
    }

    public void addEmail(EmailData emailData){
        emails.add(emailData);
        emailData.setUser(this);
    }

    public void removeEmail(EmailData emailData){
        emails.remove(emailData);
        emailData.setUser(null);
    }

    public void addPhone(PhoneData phoneData){
        phones.add(phoneData);
        phoneData.setUser(this);
    }

    public void removePhone(PhoneData phoneData){
        phones.remove(phoneData);
        phoneData.setUser(null);
    }

    public Long getId() {
        return id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<EmailData> getEmails() {
        return emails;
    }

    public void setEmails(List<EmailData> emails) {
        this.emails = emails;
    }

    public List<PhoneData> getPhones() {
        return phones;
    }

    public void setPhones(List<PhoneData> phones) {
        this.phones = phones;
    }
}
