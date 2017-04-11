package org.xeon.stockey.po;

import java.io.Serializable;

/**
 * 
 * @author nians
 *
 */
public class  StockInfoPO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5890093981942736610L;
	//股票代码（8位，前两位表示沪深或大盘，后6位为代码）
	private String symbol; 
	//股票名称
	private String name;
	//公司简要信息
	private String info;
	//交易市场（沪、深）
	private ExchangeEnum market;
	//所属地域板块
	private String region;
	//所属行业板块
	private String type;
	
	
	/**
	 * 
	 * @param symbol 股票代码
	 * @param name 公司简要信息
	 * @param info 公司简要信息
	 * @param market 交易市场（沪、深）
	 * @param region 所属地域板块
	 * @param type 所属行业板块
	 */
	public StockInfoPO(){

	}
	public StockInfoPO(String symbol, String name, String info, ExchangeEnum market, String region, String type) {
		super();
		this.symbol = symbol;
		this.name = name;
		this.info = info;
		this.market = market;
		this.region = region;
		this.type = type;
	}


	public String getSymbol() {
		return symbol;
	}


	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getInfo() {
		return info;
	}


	public void setInfo(String info) {
		this.info = info;
	}


	public ExchangeEnum getMarket() {
		return market;
	}


	public void setMarket(ExchangeEnum market) {
		this.market = market;
	}


	public String getRegion() {
		return region;
	}


	public void setRegion(String region) {
		this.region = region;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}
	
	public void print(){
		System.out.println(this.getInfo()+" "+this.getName()+" "+this.getRegion()
		+" "+this.getSymbol()+" "+this.getType()+" "+this.getMarket());
	}
	
	
	
}
