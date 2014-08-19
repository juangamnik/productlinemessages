package de.tapestry.productline.services;

import de.tapestry.productline.Product;
import de.tapestry.productline.ProductComponentRequestSelectorAnalyzer;
import de.tapestry.productline.ProductComponentResourceLocator;
import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.Decorate;
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.apache.tapestry5.ioc.services.ServiceOverride;
import org.apache.tapestry5.services.*;
import org.apache.tapestry5.services.pageload.ComponentRequestSelectorAnalyzer;
import org.apache.tapestry5.services.pageload.ComponentResourceLocator;
import org.slf4j.Logger;

import java.io.IOException;

/**
 * This module is automatically included as part of the Tapestry IoC Registry, it's a good place to
 * configure and extend Tapestry, or to place your own service definitions.
 */
public class AppModule
{
    public static void bind(ServiceBinder binder)
    {

        // Make bind() calls on the binder object to define most IoC services.
        // Use service builder methods (example below) when the implementation
        // is provided inline, or requires more initialization than simply
        // invoking the constructor.
	    binder.bind(ProductIndicator.class, ProductIndicatorImpl.class);
	    binder.bind(ComponentRequestSelectorAnalyzer.class, ProductComponentRequestSelectorAnalyzer.class)
			    .withId("ProductComponentRequestSelectorAnalyzer");
    }

    public static void contributeFactoryDefaults(
            MappedConfiguration<String, Object> configuration)
    {
        // The application version number is incorprated into URLs for some
        // assets. Web browsers will cache assets because of the far future expires
        // header. If existing assets are changed, the version number should also
        // change, to force the browser to download new versions. This overrides Tapesty's default
        // (a random hexadecimal number)
        configuration.override(SymbolConstants.APPLICATION_VERSION, "1.0-SNAPSHOT");
    }

    public static void contributeApplicationDefaults(
            MappedConfiguration<String, Object> configuration)
    {
        // Contributions to ApplicationDefaults will override any contributions to
        // FactoryDefaults (with the same key). Here we're restricting the supported
        // locales to just "en" (English). As you add localised message catalogs and other assets,
        // you can extend this list of locales (it's a comma separated series of locale names;
        // the first locale name is the default when there's no reasonable match).
        configuration.add(SymbolConstants.SUPPORTED_LOCALES, "en,de");
    }


	@Decorate(serviceInterface = ComponentResourceLocator.class)
	public static Object productComponentResourceLocator(
			ComponentResourceLocator delegate) {

		return new ProductComponentResourceLocator(delegate);
	}

	@Contribute(ServiceOverride.class)
	public static void overrideSelectorAnalyzer(
			MappedConfiguration<Class, Object> cfg,

			@InjectService("ProductComponentRequestSelectorAnalyzer")
			ComponentRequestSelectorAnalyzer analyzer){

		cfg.add(ComponentRequestSelectorAnalyzer.class, analyzer);
	}

	public PageRenderRequestFilter buildProductFilter(final Logger log, final ProductIndicator productIndicator) {
		return new PageRenderRequestFilter() {
			public void handle(PageRenderRequestParameters parameters,
					PageRenderRequestHandler handler) throws IOException {

				EventContext activationContext = parameters.getActivationContext();
				String productName = activationContext.get(String.class, 0);

                log.info("Product name: " + productName);

                if(!(productName == null || productName.isEmpty())) {
                    productIndicator.setProduct(Product.valueOf(productName));
                }
				// A call to the filter chain, so that the next filter is executed.
				handler.handle(parameters);
			}
		};
	}

	public void contributePageRenderRequestHandler(OrderedConfiguration<PageRenderRequestFilter> configuration,
			@InjectService("ProductFilter") PageRenderRequestFilter productFilter) {
		configuration.add("productFilter", productFilter);
	}
}
