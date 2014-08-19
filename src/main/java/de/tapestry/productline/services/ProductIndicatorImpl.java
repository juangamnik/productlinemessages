package de.tapestry.productline.services;

import de.tapestry.productline.Product;
import org.apache.tapestry5.ioc.ScopeConstants;
import org.apache.tapestry5.ioc.annotations.Scope;

@Scope(ScopeConstants.PERTHREAD)
public class ProductIndicatorImpl implements ProductIndicator {

	private Product product;

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
}
