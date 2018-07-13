package com.maimob.server.data.task;

public class dayData {
	double register = 0;// 注册人数

	double activation = 0;// 登录激活
	double upload = 0;// 进件人数
	double account = 0;// 开户数
	double loan = 0;// 放款人数
	double credit = 0;// 授信总额
	double perCapitaCredit = 0;// 人均额度
	double channelSum = 0;// 渠道提现总额

	double firstGetPer = 0;// 首提人数
	double firstGetSum = 0;// 首提总额

	double secondGetPer = 0;// 复贷人数
	double secondGetPi = 0;// 复贷笔数
	double secondGetSum = 0;// 复贷总额
	double outFirstGetSum2 = 0;// 首贷总额减少

	double outRegister = 0;// 注册人数
	double outActivation = 0;// 登录激活
	double outAccount = 0;// 开户数
	double outUpload = 0;// 进件人数
	double outLoan = 0;// 放款人数
	double outCredit = 0;// 授信总额
	double outFirstGetPer = 0;// 首提人数
	double outFirstGetSum = 0;// 首提总额
	double outChannelSum = 0;// 渠道提现总额

	String hour = "";
	public dayData(double dd[],String hh) {
		this(dd);
		hour = hh;
	}
	
	
	public dayData(double dd[]) {

		register = dd[0];// 注册人数

		activation = dd[12];// 登录激活
		upload = dd[1];// 进件人数
		account = dd[2];// 开户数
		loan = dd[3];// 放款人数
		credit = dd[4];// 授信总额
		perCapitaCredit = dd[5];// 人均额度
		channelSum = dd[8];// 渠道提现总额

		firstGetPer = dd[6];// 首提人数
		firstGetSum = dd[7];// 首提总额

		secondGetPer = dd[10];// 复贷人数
		secondGetPi = dd[10];// 复贷笔数
		secondGetSum = dd[11];// 复贷总额
		outFirstGetSum2 = dd[16];// 首贷总额减少

		outRegister = dd[35];
		outActivation = dd[36];
		outAccount = dd[37];
		outUpload = dd[38];
		outLoan = dd[39];
		outCredit = dd[40];
		outFirstGetPer = dd[41];
		outFirstGetSum = dd[42];
		outChannelSum = dd[43];
	}
	
	public void add(dayData dd)
	{
		this.outRegister += dd.outRegister;
		this.outActivation += dd.outActivation;
		this.outAccount += dd.outAccount;
		this.outUpload += dd.outUpload;
		this.outLoan += dd.outLoan;
		this.outCredit += dd.outCredit;
		this.outFirstGetPer += dd.outFirstGetPer;
		this.outFirstGetSum += dd.outFirstGetSum;
		this.outChannelSum += dd.outChannelSum;
	}

	public void minus(dayData dd)
	{
		this.outRegister -= dd.outRegister;
		this.outActivation -= dd.outActivation;
		this.outAccount -= dd.outAccount;
		this.outUpload -= dd.outUpload;
		this.outLoan -= dd.outLoan;
		this.outCredit -= dd.outCredit;
		this.outFirstGetPer -= dd.outFirstGetPer;
		this.outFirstGetSum -= dd.outFirstGetSum;
		this.outChannelSum -= dd.outChannelSum;
	}
	

	public void minus2(dayData dd)
	{
		
		double[] res = One_minus_two(this.outRegister,dd.outRegister);
		this.outRegister = res[0];
		dd.outRegister = res[1];

		 res = One_minus_two(this.outActivation,dd.outActivation);
		this.outActivation = res[0];
		dd.outActivation = res[1];

		 res = One_minus_two(this.outAccount,dd.outAccount);
		this.outAccount = res[0];
		dd.outAccount = res[1];
		 res = One_minus_two(this.outUpload,dd.outUpload);
		this.outUpload = res[0];
		dd.outUpload = res[1];
		 res = One_minus_two(this.outLoan,dd.outLoan);
		this.outLoan = res[0];
		dd.outLoan = res[1];
		 res = One_minus_two(this.outCredit,dd.outCredit);
		this.outCredit = res[0];
		dd.outCredit = res[1];
		 res = One_minus_two(this.outFirstGetPer,dd.outFirstGetPer);
		this.outFirstGetPer = res[0];
		dd.outFirstGetPer = res[1];
		 res = One_minus_two(this.outFirstGetSum,dd.outFirstGetSum);
		this.outFirstGetSum = res[0];
		dd.outFirstGetSum = res[1];
		 res = One_minus_two(this.outChannelSum,dd.outChannelSum);
		this.outChannelSum = res[0];
		dd.outChannelSum = res[1];
		
		
	}
	

	public boolean Equal(dayData dd)
	{
		if(this.outRegister == dd.outRegister && this.outActivation == dd.outActivation && this.outAccount == dd.outAccount && this.outUpload == dd.outUpload && this.outLoan == dd.outLoan && 
				this.outCredit == dd.outCredit && this.outFirstGetPer == dd.outFirstGetPer && this.outFirstGetSum == dd.outFirstGetSum && this.outChannelSum == dd.outChannelSum)
			return true;
		else 
			return false;
	}
	
	
	
	
	public boolean is0()
	{
		boolean isok = true;
		if(this.outActivation!=0)
			isok = false;
		else if(this.outAccount!=0)
			isok = false;
		else if(this.outUpload!=0)
			isok = false;
		else if(this.outLoan!=0)
			isok = false;
		else if(this.outCredit!=0)
			isok = false;
		else if(this.outFirstGetPer!=0)
			isok = false;
		else if(this.outFirstGetSum!=0)
			isok = false;
		else if(this.outChannelSum!=0)
			isok = false;
		else if(this.outRegister!=0)
			isok = false;
		
		return isok;
	}
	
	public double[] One_minus_two(double one,double two)
	{
		double[] res = new double[2];
		if(two > 0)
		{
			if(one >= two)
			{
				one -= two;
				two=0;
			}
			else
			{
				one = 0;
				two -= one;
			}
		}
		else if(two < 0)
		{
			if(one >= Math.abs(two))
			{
				one += two;
				two=0;
			}
			else
			{
				one = 0;
				two += one;
			}
		}
		
		res[0] = one;
		res[1] = two;
		
		return res;
		
	}
	
	
	
	
	
	
}