package ch.aaap.ca.be.medicalsupplies.model;

import java.util.Objects;

/**
 * Producer entity
 */
public class ProducerEntity {

    private final String id;
    private final String name;
    private final String address;

    public ProducerEntity(String id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null || getClass() != object.getClass())
            return false;
        ProducerEntity producer = (ProducerEntity) object;
        return Objects.equals(id, producer.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
