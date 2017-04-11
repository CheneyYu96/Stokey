package org.xeon.stockey.po;

/**
 * 
 * @author nians
 *
 */
public class  StockInfoPO{
	/**
	 * 
	 */
	//股票代码（8位，前两位表示沪深或大盘，后6位为代码）
	private String symbol; 
	//股票名称
	private String name;
	//公司简要信息
	private String info;
	//交易市场（沪、深）
	private String market;
	//所属地域板块
	private String region;
	//所属行业板块
	private String type;
    //英文名称
    private String englishName;
    //注册资本
    private String registerCapital;
    //组织形式
    private String organize;


	
	
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
	public StockInfoPO(String symbol, String name, String info, String market, String region, String type) {
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


	public String getMarket() {
		return market;
	}


	public void setMarket(String market) {
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
	
	@Override
	public String toString(){
		String result = "insert into stockinfopo values(";
		result += "\""+this.symbol+"\",";
		result += "\""+this.name+"\",";
		result += "\""+this.info+"\",";
		result += "\""+this.market.toString()+"\",";
		result += "\""+this.region+"\",";
		result += "\""+this.type+"\");";
		return result;
	}

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getRegisterCapital() {
        return registerCapital;
    }

    public void setRegisterCapital(String registerCapital) {
        this.registerCapital = registerCapital;
    }

    public String getOrganize() {
        return organize;
    }

    public void setOrganize(String organize) {
        this.organize = organize;
    }
}
