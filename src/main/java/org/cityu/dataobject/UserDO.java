package org.cityu.dataobject;

public class UserDO {

    private String name;

    private String password;

    private Integer role;

    private Integer available;

    private String company;

    private String sellerTaxId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public Integer getAvailable() {
        return available;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getSellerTaxId() {
        return sellerTaxId;
    }

    public void setSellerTaxId(String sellerTaxId) {
        this.sellerTaxId = sellerTaxId;
    }
}