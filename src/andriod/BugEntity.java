package andriod;

import java.util.LinkedList;
import java.util.List;
/*
 * <bug>
<bugid>8563</bugid>
<title>Browser/WebView doesnQ&apos;Et set InputType.TYPE_TEXT_VARIATION_WEB_EDIT_TEXT on Forms in Android 2.2</title>
<status>Reviewed</status>
<owner>cary%and...@gtempaccount.com</owner>
<closedOn>null</closedOn>
<type>Defect</type>
<priority>Medium</priority>
<component>Browser</component>
<stars>5</stars>
<reportedBy>jonasl1...@gmail.com</reportedBy>
<openedDate>Mon, 24 May 2010 21:13:18 +0000</openedDate>
<description>Prior to Android 2.2 developers of soft keyboards could detect that the
user was typing in a web form by looking for the
</description>
<comment>
<author>cccandr...@gmail.com</author>
<when>Mon, 24 May 2010 21:24:39 +0000</when>
<what>This change was an unintended oversight -- unfortunately, itQ&apos;Es too late to fix it in Froyo.</what>
</comment>
<comment>
<author>jonasl1...@gmail.com</author>
<when>Mon, 24 May 2010 21:35:08 +0000</when>
<what>Bummer. Thanks for the quick confirmation anyway. Please consider removing the flag
IME_FLAG_NO_EXTRACT_UI that was added in the web forms in 2.2 as well.</what>
</comment>
<comment>
<author>morri...@google.com</author>
<when>Fri, 20 Aug 2010 22:00:38 +0000</when>
<what></what>
</comment>
</bug>
 */


public class BugEntity extends XEntity{

	private String bugid;
	private String title;
	private String status;
	private String owner;
	private String closedOn;
	private String type;
	private String priority;
	private String component;
	private String stars;
	private String reportedBy;
	private String openedDate;
	private String description;

	private List<BugComment> comments = null;
	
	private String state = null;
	//private String lastState = state;
	
	public BugEntity() {
		this.init();
		comments = new LinkedList<BugComment>();
	}
	
	public void init(){
		this.setBugid(null);
		this.setTitle(null);
		this.setStatus(null);
		this.setOwner(null);
		this.setClosedOn(null);
		this.setType(null);
		this.setPriority(null);
		this.setComponent(null);
		this.setStars(null);
		this.setReportedBy(null);
		this.setOpenedDate(null);
		this.setDescription(null);
		this.setState(null);
		if(null != comments) this.comments.clear();	
	}
	/*
	 * indicate the element with the SAX parser
	 */
	public void setState(String state){
		//this.lastState = this.state;
		this.state = state;
	}
	
	public void action(String value){
	//	System.out.println("state is "+state + "; value: "+ value);
		if(Constants.BUG_START.equalsIgnoreCase(state)){
			this.init();
		}else if(Constants.BUG_BUGID.equalsIgnoreCase(state)){
			this.setBugid(value);
		}else if(Constants.BUG_CLOSEON.equalsIgnoreCase(state)){
			this.setClosedOn(value);
		}else if(Constants.BUG_COMPONENT.equalsIgnoreCase(state)){
			this.setComponent(value);
		}else if(Constants.BUG_DESCRIPTION.equalsIgnoreCase(state)){
			this.setDescription(value);
		}else if(Constants.BUG_OPENED_DATE.equalsIgnoreCase(state)){
			this.setOpenedDate(value);
		}else if(Constants.BUG_OWNER.equalsIgnoreCase(state)){
			this.setOwner(value);
		}else if(Constants.BUG_PRIORITY.equalsIgnoreCase(state)){
			this.setPriority(value);
		}else if(Constants.BUG_REPORTED_BY.equalsIgnoreCase(state)){
			this.setReportedBy(value);
		}else if(Constants.BUG_STARS.equalsIgnoreCase(state)){
			this.setStars(value);
		}else if(Constants.BUG_STATUS.equalsIgnoreCase(state)){
			this.setStatus(value);
		}else if(Constants.BUG_TITLE.equalsIgnoreCase(state)){
			this.setTitle(value);
		}else if(Constants.BUG_TYPE.equalsIgnoreCase(state)){
			this.setType(value);
		}else if(Constants.BUG_COMMENT_AUTHOR.equalsIgnoreCase(state)){
			this.getLastestComment().setAuthor(value);
		}else if(Constants.BUG_COMMENT_WHAT.equalsIgnoreCase(state)){
			this.getLastestComment().setWhat(value);
		}else if(Constants.BUG_COMMENT_WHEN.equalsIgnoreCase(state)){
			this.getLastestComment().setWhen(value);
		}else{
			if (null != state) System.out.println("the state is "+state);
		}
	}
	
	public void newComment(){
		if (comments == null){
			comments = new LinkedList<BugComment>();
		}	
		comments.add(new BugComment());	
	}
	public BugComment getLastestComment(){
		if (null != comments){
			if(comments.size() == 0)
				this.newComment();			
			return comments.get(comments.size()-1);

		}else
			return null;
	}	
	private String comment_to_s(){
		String comment_str = "[ ";
		for(BugComment c:this.comments){
			comment_str +="{ author=>"+c.getAuthor()+";"
						+"what=>"+c.getWhat()+";"
						+"when=>"+c.getWhen()+"},";
		}
		comment_str += " ]";
		return comment_str;
	}
	
	public String toString(){
		String msg = "bugid=>"+this.bugid+";"
					+"closedOn=>"+this.closedOn+";"
					+"component=>"+this.component+";"
					+"description=>"+this.description+";"
					+"openedDate=>"+this.openedDate+";"
					+"owner=>"+this.owner+";"
					+"priority=>"+this.priority+";"
					+"reportedBy=>"+this.reportedBy+";"
					+"stars=>"+this.stars+";"
					+"status=>"+this.status+";"
					+"title=>"+this.title+";"
					+"type=>"+this.type+";"
					+"comments=>"+this.comment_to_s();
		return msg;
	}
	
	
	public String getBugid() {
		return bugid;
	}

	public String getTitle() {
		return title;
	}

	public String getStatus() {
		return status;
	}

	public String getOwner() {
		return owner;
	}

	public String getClosedOn() {
		return closedOn;
	}

	public String getType() {
		return type;
	}

	public String getPriority() {
		return priority;
	}

	public String getComponent() {
		return component;
	}

	public String getStars() {
		return stars;
	}

	public String getReportedBy() {
		return reportedBy;
	}

	public String getOpenedDate() {
		return openedDate;
	}

	public String getDescription() {
		return description;
	}

	public List<BugComment> getComments() {
		return comments;
	}
	
	public void setBugid(String bugid) {
		this.bugid = bugid;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public void setClosedOn(String closedOn) {
		this.closedOn = closedOn;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public void setStars(String stars) {
		this.stars = stars;
	}

	public void setReportedBy(String reportedBy) {
		this.reportedBy = reportedBy;
	}

	public void setOpenedDate(String openedDate) {
		this.openedDate = openedDate;
	}

	public void setDescription(String description) {
		if(null != this.description)
			this.description += description;
		else
			this.description = description;
	}	
	



	private class BugComment {
		private String author = null;
		private String when = null;
		private String what = null;

		public BugComment(){}
		public BugComment(String author,String when,String what){
			this.setAuthor(author);
			this.setWhen(when);
			this.setWhat(what);
		}
		public String getAuthor() {
			return author;
		}

		public void setAuthor(String author) {
			this.author = author;
		}

		public String getWhen() {
			return when;
		}

		public void setWhen(String when) {
			this.when = when;
		}

		public String getWhat() {
			return what;
		}

		public void setWhat(String what) {
			if(null != this.what) 
				this.what += what;
			else
				this.what = what;
		}

	}

}
