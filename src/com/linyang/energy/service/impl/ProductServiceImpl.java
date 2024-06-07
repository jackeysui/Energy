package com.linyang.energy.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linyang.energy.mapping.authmanager.ProductBeanMapper;
import com.linyang.energy.model.ProductsBean;
import com.linyang.energy.service.ProductService;
import com.linyang.util.CommonMethod;

@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	private ProductBeanMapper productMapper;

	@Override
	public List<ProductsBean> getProductListData() {
		List<ProductsBean> list = productMapper.getProductList();
		return list == null ? new ArrayList<ProductsBean>() : list;
	}

	@Override
	public boolean deleteProductData(Long productId,Long picId) {
		boolean flag = false;
		if(CommonMethod.isNotEmpty(productId)){
			productMapper.deletePowerDetail(productId);
			productMapper.deleteProduct(productId);
			productMapper.deletePicData(picId);
			flag = true;
		}
		return flag;
	}
	
	@Override
	public boolean getProductLink(Long productId) {
		if(productMapper.getProductLink(productId)==0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean checkProductName(String productName) {
		boolean flag = false;
		if(CommonMethod.isNotEmpty(productName)){
			//检查是否有名称重复
			if(productMapper.checkProductName(productName)==0) {
				flag = true;
			}
		}
		return flag;
	}

	@Override
	public boolean addProductData(ProductsBean product,String powerIds){
		boolean flag = false;
		if(product!=null) {
			productMapper.insertPicData(product);
			productMapper.insertProductData(product);
			if(CommonMethod.isNotEmpty(powerIds)) {
				String[] str = powerIds.split(",");
				long productId = product.getProductId();
				for(String s : str) {
					Map<String,Object> map = new HashMap<String, Object>();
					map.put("productId", productId);
					map.put("typeId", Long.valueOf(s));
					productMapper.insertPowerData(map);
				}
			}
			flag=true;
		}
		return flag;
	}

	@Override
	public ProductsBean getProductData(Long productId) {
		ProductsBean product = productMapper.getProdInfo(productId);
		return product == null?new ProductsBean():product;
	}

	@Override
	public List<Map<String,Object>> getPowerData(Long productId) {
		List<Map<String,Object>> list = productMapper.getPowerDataById(productId);
		return list==null?new ArrayList<Map<String,Object>> ():list;
	}

	@Override
	public boolean updateProductData(ProductsBean product,String powerIds) {
		boolean flag = false;
		if(product!=null) {
			productMapper.updatePicInfo(product);
			productMapper.updateProductInfo(product);
			if(CommonMethod.isNotEmpty(powerIds)) {
				String[] str = powerIds.split(",");
				long productId = product.getProductId();
				productMapper.deletePowerDetail(productId);
				for(String s : str) {
					Map<String,Object> map = new HashMap<String, Object>();
					map.put("productId", productId);
					map.put("typeId", Long.valueOf(s));
					productMapper.insertPowerData(map);
				}
			}
			flag=true;
		}
		return flag;
	}

	@Override
	public List<Map<Long, String>> getPowerData() {
		List<Map<Long, String>> list = productMapper.getPowerData();
		return (list==null?new ArrayList<Map<Long, String>> (): list);
	}

	@Override
	public List<Map<Long, String>> getUnitData() {
		List<Map<Long, String>> list = productMapper.getUnitData();
		return list==null?new ArrayList<Map<Long, String>> (): list;
	}
	

}
