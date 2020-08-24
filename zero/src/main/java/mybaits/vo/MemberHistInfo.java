package mybaits.vo;

import java.util.Date;

public class MemberHistInfo {
	public Integer getTblId() {
		return tblId;
	}
	public void setTblId(Integer tblId) {
		this.tblId = tblId;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getEnterDate() {
		return enterDate;
	}
	public void setEnterDate(Date enterDate) {
		this.enterDate = enterDate;
	}
	public Integer getEnterOld() {
		return enterOld;
	}
	public void setEnterOld(Integer enterOld) {
		this.enterOld = enterOld;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getRetirementDate() {
		return retirementDate;
	}
	public void setRetirementDate(Date retirementDate) {
		this.retirementDate = retirementDate;
	}
	public Integer getRetirementType() {
		return retirementType;
	}
	public void setRetirementType(Integer retirementType) {
		this.retirementType = retirementType;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public Integer getFleshOrNot() {
		return fleshOrNot;
	}
	public void setFleshOrNot(Integer fleshOrNot) {
		this.fleshOrNot = fleshOrNot;
	}
	public Integer getDepartment() {
		return department;
	}
	public void setDepartment(Integer department) {
		this.department = department;
	}
	public Integer getPosition() {
		return position;
	}
	public void setPosition(Integer position) {
		this.position = position;
	}
	//	tbl_id	tbl_id	serial
	private Integer tblId;

	//	社員ID	member_id	text
	private String memberId;
	//	開始日付	start_date	date
	private Date startDate;
	//	名前	name	text
	private String name;
	//	入社日付	enter_date	date
	private Date enterDate;
	//	入社時年齢	enter_old	smallint
	private Integer enterOld;
	//	在籍状態	status	smallint 	 0:在籍、1:退社
	private Integer status;
	//	退職日日付	retirement_date	date
	private Date retirementDate;
	//	退職種別	retirement_type	smallint	  0:自己都合、1:会社判断
	private Integer retirementType;
	//	性別	sex	smallint	  0:男、1:女
	private Integer sex;
	//	新卒、既卒	flesh_or_not	smallint	  0:新卒、1:既卒
	private Integer fleshOrNot;
	//	所属部署	department	smallint	  部署カテゴリコード
	private Integer department;
	//	役職	position	smallint	  役職カテゴリコード
	private Integer position;

}
