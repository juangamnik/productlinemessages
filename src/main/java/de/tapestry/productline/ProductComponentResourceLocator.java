package de.tapestry.productline;

import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.model.ComponentModel;
import org.apache.tapestry5.services.ComponentClassResolver;
import org.apache.tapestry5.services.pageload.ComponentResourceLocator;
import org.apache.tapestry5.services.pageload.ComponentResourceSelector;

import java.util.LinkedList;
import java.util.List;

/**
 * Adds the product axis to the default component resource locator.
 */
public class ProductComponentResourceLocator implements ComponentResourceLocator {

    private final ComponentResourceLocator delegate;

    public ProductComponentResourceLocator(ComponentResourceLocator delegate) {
	    this.delegate = delegate;
    }

	public Resource locateTemplate(ComponentModel model, ComponentResourceSelector selector) {
		return delegate.locateTemplate(model, selector);
	}

	public List<Resource> locateMessageCatalog(Resource baseResource, ComponentResourceSelector selector) {
		Product product = selector.getAxis(Product.class);

		if (product != null) {
			String fileName = baseResource.getFile();
			fileName = fileName.replace(".properties", "-" + product + ".properties");
			Resource productResource = baseResource.forFile(fileName);

			if (productResource.exists()) {
				List<Resource> messageCatalogs = new LinkedList<Resource>();
				messageCatalogs.addAll(delegate.locateMessageCatalog(productResource, selector));
				messageCatalogs.addAll(delegate.locateMessageCatalog(baseResource, selector));
				return messageCatalogs;
			}
		}

		return delegate.locateMessageCatalog(baseResource, selector);
	}
}
