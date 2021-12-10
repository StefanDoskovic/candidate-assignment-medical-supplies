package ch.aaap.ca.be.medicalsupplies;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MSTest {

    /* The system under test */
    MSApplication sut = new MSApplication();

    @Test
    public void returnsCorrectNumberOfUniqueGenericNames() {
        assertEquals("Number of unique generic names", 7255, sut.numberOfUniqueGenericNames());
    }

    @Test
    public void returnsCorrectNumberOfDuplicateGenericNames() {
        assertEquals("Number of duplicate generic names", 12, sut.numberOfDuplicateGenericNames());
    }

    @Test
    public void returnsCorrectNumberOfMSProductsWithGenericName() {
        assertEquals("Number of products with generic name", 36966, sut.numberOfMSProductsWithGenericName());
    }

    @Test
    public void returnsCorrectNumberOfMSProductsWithoutGenericName() {
        assertEquals("Number of products without generic name", 3638, sut.numberOfMSProductsWithoutGenericName());
    }

    /**
     * Multiple names for same company: NEOMEDICA DOO NIŠ, Neomedica d.o.o. Niš
     * Test is slightly modified
     */
    @Test
    public void returnsCorrectCompanyNameForMostNumberOfMSProducts() {
        assertTrue("Company name that is both licence holder and producer for most number of products", Arrays.asList("NEOMEDICA DOO NIŠ", "Neomedica d.o.o. Niš").contains(
                sut.nameOfCompanyWhichIsProducerAndLicenseHolderForMostNumberOfMSProducts()));
    }

    @Test
    public void returnsCorrectNumberOfMSProductsByCompanyName() {
        assertEquals("Number products by company name", 1124, sut.numberOfMSProductsByProducerName("roche"));
    }

    @Test
    public void returnsCorrectNumberOfMSProductsByGenericNameCategory() {
        assertEquals("Number of products whose generic name has a category", 2671,
                sut.findMSProductsWithGenericNameCategory("05 - Bolnička, aparaturna oprema").size());
    }
}
