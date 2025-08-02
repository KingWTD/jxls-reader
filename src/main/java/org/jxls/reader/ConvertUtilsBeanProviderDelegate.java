package org.jxls.reader;

import org.apache.commons.beanutils.ConvertUtilsBean;

public class ConvertUtilsBeanProviderDelegate implements ConvertUtilsBeanProvider{

	private ConvertUtilsBeanProvider delegate;
	
	public ConvertUtilsBeanProviderDelegate(){
		
	}
	
	public ConvertUtilsBeanProviderDelegate(final ConvertUtilsBean convertUtilsBean) {
		this.delegate = new ConvertUtilsBeanProvider() {
			@Override
			public ConvertUtilsBean getConvertUtilsBean() {
				return convertUtilsBean;
			}
		};
	}


	public ConvertUtilsBean getConvertUtilsBean(){
		ConvertUtilsBean result;
		if( delegate != null ){
			result = delegate.getConvertUtilsBean();
		}else{
			result = ReaderConfig.getInstance().getConvertUtilsBean();
		}
		return result;
	}
	
	public void setDelegate( ConvertUtilsBeanProvider convertUtilsBean){
		delegate = convertUtilsBean;
	}
}
