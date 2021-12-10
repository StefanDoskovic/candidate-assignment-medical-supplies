package ch.aaap.ca.be.medicalsupplies.model;

import java.util.Objects;

/**
 * Product entity
 */
public class ProductEntity {

    private final String id;
    private final String name;
    private final GenericNameEntity generic;
    private final String primaryCategory;
    private final ProducerEntity producer;
    private final LicenseHolderEntity licenseHolder;

    public ProductEntity(String id, String name, GenericNameEntity generic, String primaryCategory,
                         ProducerEntity producer, LicenseHolderEntity licenseHolder) {
        this.id = id;
        this.name = name;
        this.generic = generic;
        this.primaryCategory = primaryCategory;
        this.producer = producer;
        this.licenseHolder = licenseHolder;
    }

    public String getId() {
        return id;
    }

    public GenericNameEntity getGeneric() {
        return generic;
    }

    public ProducerEntity getProducer() {
        return producer;
    }

    public LicenseHolderEntity getLicenseHolder() {
        return licenseHolder;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null || getClass() != object.getClass())
            return false;
        ProductEntity productEntity = (ProductEntity) object;
        return id.equals(productEntity.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
