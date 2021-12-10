package ch.aaap.ca.be.medicalsupplies.model;

import java.util.Objects;

/**
 * License holder entity
 */
public class LicenseHolderEntity {

    private final String id;
    private final String name;
    private final String address;

    public LicenseHolderEntity(String id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null || getClass() != object.getClass())
            return false;
        LicenseHolderEntity licenceHolder = (LicenseHolderEntity) object;
        return Objects.equals(id, licenceHolder.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
