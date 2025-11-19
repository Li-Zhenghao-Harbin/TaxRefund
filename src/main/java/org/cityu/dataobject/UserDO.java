package org.cityu.dataobject;

public class UserDO {
    private Integer id;

    private String name;

    private String password;

    private Integer role;

    private Integer available;

    private String sellerTaxId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public String getSellerTaxId() {
        return sellerTaxId;
    }

    public void setSellerTaxId(String sellerTaxId) {
        this.sellerTaxId = sellerTaxId;
    }
}