package ch.aaap.ca.be.medicalsupplies;

import ch.aaap.ca.be.medicalsupplies.data.CSVUtil;
import ch.aaap.ca.be.medicalsupplies.data.MSGenericNameRow;
import ch.aaap.ca.be.medicalsupplies.data.MSProductRow;
import ch.aaap.ca.be.medicalsupplies.model.GenericNameEntity;
import ch.aaap.ca.be.medicalsupplies.model.LicenseHolderEntity;
import ch.aaap.ca.be.medicalsupplies.model.ProducerEntity;
import ch.aaap.ca.be.medicalsupplies.model.ProductEntity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MSApplication {

    private static final Logger logger = Logger.getLogger(MSApplication.class.getName());

    private final Set<MSGenericNameRow> genericNames;
    private final Set<MSProductRow> registry;

    public MSApplication() {
        genericNames = CSVUtil.getGenericNames();
        registry = CSVUtil.getRegistry();
    }

    public static void main(String[] args) {
        MSApplication main = new MSApplication();

        logger.log(Level.INFO, "Generic names count: {0}", main.genericNames.size());
        logger.log(Level.INFO, "Registry count: {0}", main.registry.size());

        logger.log(Level.INFO, "1st of generic name list: {0}", main.genericNames.iterator().next());
        logger.log(Level.INFO, "1st of registry list: {0}", main.registry.iterator().next());

    }

    /**
     * Create a model / data structure that combines the input sets.
     *
     * @param genericNameRows The generic name rows
     * @param productRows     The product rows
     * @return The product entity map
     */
    public Map<String, ProductEntity> createModel(Set<MSGenericNameRow> genericNameRows, Set<MSProductRow> productRows) {

        Map<String, GenericNameEntity> genericNameEntityMap = getGenericNameEntityMap();

        Map<String, ProductEntity> productEntityMap = new HashMap<>();

        for (MSProductRow productRow : productRows) {
            ProductEntity productEntity = new ProductEntity(productRow.getId(), productRow.getName(), genericNameEntityMap.get(productRow.getGenericName()),
                    productRow.getPrimaryCategory(), new ProducerEntity(productRow.getProducerId(), productRow.getProducerName(), productRow.getProducerAddress()),
                    new LicenseHolderEntity(productRow.getLicenseHolderId(), productRow.getLicenseHolderName(), productRow.getLicenseHolderAddress()));
            productEntityMap.put(productEntity.getId(), productEntity);

        }
        return productEntityMap;
    }

    /* MS Generic Names */

    /**
     * Method find the number of unique generic names.
     *
     * @return The number of unique generic names
     */
    public int numberOfUniqueGenericNames() {
        Map<String, GenericNameEntity> genericNameEntityMap = getGenericNameEntityMap();
        return genericNameEntityMap.size();
    }

    /**
     * Method finds the number of generic names which are duplicated.
     *
     * @return The number of duplicate generic names
     */
    public int numberOfDuplicateGenericNames() {
        Map<String, GenericNameEntity> genericNameEntityMap = getGenericNameEntityMap();
        return genericNames.size() - genericNameEntityMap.size();
    }

    /* MS Products */

    /**
     * Method finds the number of products which have a generic name which can be
     * determined.
     *
     * @return The number of determined products
     */
    public int numberOfMSProductsWithGenericName() {
        Map<String, ProductEntity> productEntityMap = createModel(genericNames, registry);
        return getProductEntityCountByGeneric(Boolean.TRUE, productEntityMap);

    }

    /**
     * Method finds the number of products which have a generic name which can NOT
     * be determined.
     *
     * @return The number of undetermined products
     */
    public int numberOfMSProductsWithoutGenericName() {
        Map<String, ProductEntity> productEntityMap = createModel(genericNames, registry);
        return getProductEntityCountByGeneric(Boolean.FALSE, productEntityMap);
    }

    /**
     * Method finds the name of the company which is both the producer and license holder for the
     * most number of products.
     *
     * @return The name of most frequent company
     */
    public String nameOfCompanyWhichIsProducerAndLicenseHolderForMostNumberOfMSProducts() {
        // second way: use the plain registry set instead of map

        Map<String, ProductEntity> productEntityMap = createModel(genericNames, registry);
        Map<ProducerEntity, Integer> companyFrequencyMap = new HashMap<>();

        // calculate frequency of companies
        for (Map.Entry<String, ProductEntity> productEntityEntry : productEntityMap.entrySet()) {
            ProductEntity productEntity = productEntityEntry.getValue();
            if (productEntity.getProducer().getId().equals(productEntity.getLicenseHolder().getId())) {
                Integer count = companyFrequencyMap.get(productEntity.getProducer());
                companyFrequencyMap.put(productEntity.getProducer(), (count == null) ? 1 : count + 1);
            }

        }

        // find most frequency company
        if (companyFrequencyMap.size() > 0) {
            Map.Entry<ProducerEntity, Integer> company = null;
            for (Map.Entry<ProducerEntity, Integer> companyEntry : companyFrequencyMap.entrySet()) {
                if (company == null || companyEntry.getValue() > company.getValue()) {
                    company = companyEntry;
                }
            }

            return company.getKey().getName();
        }

        return null;

    }

    /**
     * Method finds the number of products whose producer name starts with
     * <i>companyName</i>.
     *
     * @param companyName The name of company
     * @return The number of matches
     */
    public long numberOfMSProductsByProducerName(String companyName) {
        // second way: use the plain registry set instead of map

        Map<String, ProductEntity> productEntityMap = createModel(genericNames, registry);
        int matchedProductCount = 0;
        for (Map.Entry<String, ProductEntity> productEntityEntry : productEntityMap.entrySet()) {
            if (productEntityEntry.getValue().getProducer().getName().toLowerCase().startsWith(companyName.toLowerCase())) {
                matchedProductCount += 1;
            }
        }

        return matchedProductCount;

    }

    /**
     * Method finds the products whose generic name has the category of interest.
     *
     * @param category The category name
     * @return The matched product set
     */
    public Set<ProductEntity> findMSProductsWithGenericNameCategory(String category) {
        Map<String, ProductEntity> productEntityMap = createModel(genericNames, registry);
        Set<ProductEntity> matchedProducts = new HashSet<>();
        for (Map.Entry<String, ProductEntity> productEntityEntry : productEntityMap.entrySet()) {
            GenericNameEntity genericNameEntity = productEntityEntry.getValue().getGeneric();
            if (genericNameEntity != null &&
                    (genericNameEntity.getCategory1().equalsIgnoreCase(category) ||
                            genericNameEntity.getCategory2().equalsIgnoreCase(category) ||
                            genericNameEntity.getCategory3().equalsIgnoreCase(category) ||
                            genericNameEntity.getCategory4().equalsIgnoreCase(category))) {
                matchedProducts.add(productEntityEntry.getValue());
            }
        }

        return matchedProducts;
    }

    /**
     * Get generic name entity map
     *
     * @return The map of pairs: name, generic object
     */
    private Map<String, GenericNameEntity> getGenericNameEntityMap() {

        Map<String, GenericNameEntity> genericNameEntityMap = new HashMap<>();
        for (MSGenericNameRow genericNameRow : genericNames) {
            GenericNameEntity genericNameEntity = new GenericNameEntity(genericNameRow.getId(), genericNameRow.getName(),
                    genericNameRow.getCategory1(), genericNameRow.getCategory2(),
                    genericNameRow.getCategory3(), genericNameRow.getCategory4());
            genericNameEntityMap.put(genericNameEntity.getName(), genericNameEntity);
        }

        return genericNameEntityMap;
    }

    /**
     * Get number of determined/undetermined products
     *
     * @param determined       The field indicates if product is determined or not (by generic name)
     * @param productEntityMap The product entity map
     * @return The number of determined/undetermined products
     */
    private int getProductEntityCountByGeneric(Boolean determined, Map<String, ProductEntity> productEntityMap) {
        int productCount = 0;
        for (Map.Entry<String, ProductEntity> productEntityEntry : productEntityMap.entrySet()) {
            GenericNameEntity genericNameEntity = productEntityEntry.getValue().getGeneric();
            if ((genericNameEntity != null && determined) ||
                    (genericNameEntity == null && !determined)) {
                productCount += 1;
            }
        }

        return productCount;
    }
}
