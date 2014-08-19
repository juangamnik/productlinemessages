package de.tapestry.productline;

import de.tapestry.productline.services.ProductIndicator;
import org.apache.tapestry5.ioc.services.ThreadLocale;
import org.apache.tapestry5.services.pageload.ComponentRequestSelectorAnalyzer;
import org.apache.tapestry5.services.pageload.ComponentResourceSelector;

import java.util.Locale;

/**
 * Determines the {@link org.apache.tapestry5.services.pageload.ComponentResourceSelector} by means
 * of the currently set product. The selected product (from a product line) is added as an
 * axis of the resource selector.
 */
public class ProductComponentRequestSelectorAnalyzer implements ComponentRequestSelectorAnalyzer {

	private final ThreadLocale threadLocale;

	private ProductIndicator productIndicator;

	public ProductComponentRequestSelectorAnalyzer(
            ThreadLocale threadLocale, ProductIndicator productIndicator) {

		this.threadLocale = threadLocale;
		this.productIndicator = productIndicator;
	}

	public ComponentResourceSelector buildSelectorForRequest() {

		Locale locale = threadLocale.getLocale();
		Product product = productIndicator.getProduct();

		return new ComponentResourceSelector(locale).withAxis(Product.class, product);
	}
}
