package ch.aaap.ca.be.medicalsupplies.model;

import java.util.Objects;

/**
 * Generic name entity
 */
public class GenericNameEntity {

    private final Long id;
    private final String name;
    private final String category1;
    private final String category2;
    private final String category3;
    private final String category4;

    public GenericNameEntity(Long id, String name, String category1, String category2,
                             String category3, String category4) {
        this.id = id;
        this.name = name;
        this.category1 = category1;
        this.category2 = category2;
        this.category3 = category3;
        this.category4 = category4;
    }

    public String getName() {
        return name;
    }

    public String getCategory1() {
        return category1;
    }

    public String getCategory2() {
        return category2;
    }

    public String getCategory3() {
        return category3;
    }

    public String getCategory4() {
        return category4;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (object == null || getClass() != object.getClass())
            return false;
        GenericNameEntity genericName = (GenericNameEntity) object;
        return name.equals(genericName.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
