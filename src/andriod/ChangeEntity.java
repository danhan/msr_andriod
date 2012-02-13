package andriod;

import java.util.LinkedList;
import java.util.List;

/*
 *    <change>
      <project>device_common</project>
      <commit_hash>523e077fb8fe899680c33539155d935e0624e40a</commit_hash>
      <tree_hash>598e7a1bd070f33b1f1f8c926047edde055094cf</tree_hash>
      <parent_hashes>71b1f9be815b72f925e66e866cb7afe9c5cd3239</parent_hashes>
      <author_name>Jean-Baptiste Queru</author_name>
      <author_e-mail>jbq@google.com</author_e-mail>
      <author_date>Fri Apr 22 08:32:04 2011 -0700</author_date>
      <commiter_name>Jean-Baptiste Queru</commiter_name>
      <commiter_email>jbq@google.com</commiter_email>
      <committer_date>Fri Apr 22 08:32:04 2011 -0700</committer_date>
      <subject>chmod the output scripts</subject>
      <message>
         <line>Change-Id: Iae22c67066ba4160071aa2b30a5a1052b00a9d7f</line>
      </message>
      <target>
         <line>generate-blob-scripts.sh</line>
      </target>
   </change>
 */

public class ChangeEntity extends XEntity{

	private String project;
	private String commit_hash;
	private String tree_hash;
	private String parent_hashes;
	private String author_name;
	private String author_email;
	private String author_date;
	private String commiter_name;
	private String commiter_email;
	private String committer_date;
	private String subject;
	private List<String> messages;
	private List<String> targets;
	
	
	private String state = null; 
	private String lastState = state;
	
	public ChangeEntity() {
		this.init();
		this.messages = new LinkedList<String>();
		this.targets = new LinkedList<String>();
	}
	
	@Override
	public void action(String value) {				
		if(Constants.CHANGE_START.equalsIgnoreCase(state)){			
			this.init();
		}else if(Constants.CHANGE_AUTHOR_DATE.equalsIgnoreCase(state)){
			this.setAuthor_date(value);
		}else if(Constants.CHANGE_AUTHOR_EMAIL.equalsIgnoreCase(state)){
			this.setAuthor_email(value);
		}else if(Constants.CHANGE_AUTHOR_NAME.equalsIgnoreCase(state)){
			this.setAuthor_name(value);
		}else if(Constants.CHANGE_COMMIT_HASH.equalsIgnoreCase(state)){
			this.setCommit_hash(value);
		}else if(Constants.CHANGE_COMMITER_EMAIL.equalsIgnoreCase(state)){
			this.setCommiter_email(value);
		}else if(Constants.CHANGE_COMMITER_NAME.equalsIgnoreCase(state)){
			this.setCommiter_name(value);
		}else if(Constants.CHANGE_COMMITTER_DATE.equalsIgnoreCase(state)){
			this.setCommitter_date(value);
		}else if(Constants.CHANGE_PROJECT.equalsIgnoreCase(state)){
			this.setProject(value);
		}else if(Constants.CHANGE_SUBJECT.equalsIgnoreCase(state)){
			this.setSubject(value);
		}else if(Constants.CHANGE_TREE_HASH.equalsIgnoreCase(state)){
			this.setTree_hash(value);
		}else if(Constants.CHANGE_PARENT_HASHES.equalsIgnoreCase(state)){
			this.setParent_hashes(value);
		}else if(Constants.CHANGE_LINE.equalsIgnoreCase(state)){
			if(Constants.CHANGE_MESSAGE.equalsIgnoreCase(this.lastState)){
				this.addMsgLine(value);
			}else if(Constants.CHANGE_TARGET.equalsIgnoreCase(this.lastState)){
				this.addTargetLine(value);
			}
		}else{
			System.out.println("new element: "+state);
		}		
	}

	@Override
	public void init() {
		this.setAuthor_date(null);
		this.setAuthor_email(null);
		this.setAuthor_name(null);
		this.setCommit_hash(null);
		this.setCommiter_email(null);
		this.setCommiter_name(null);
		this.setCommitter_date(null);
		this.setParent_hashes(null);
		this.setProject(null);
		this.setSubject(null);
		this.setTree_hash(null);
		
		if (null != this.messages) this.messages.clear();
		if(null != this.targets) this.targets.clear();

		this.setState(null);		
	}

	@Override
	public void setState(String state) {
		if (this.state != null && !Constants.CHANGE_LINE.equalsIgnoreCase(this.state))
			this.lastState = this.state;
		this.state = state;			
	}

	public void addMsgLine(String line){
		if(null == messages){
			messages = new LinkedList<String>();
		}else{
			messages.add(line);
		}
	}
	
	public void addTargetLine(String line){
		if(null == targets){
			targets = new LinkedList<String>();
		}else{
			targets.add(line);
		}
	}
	
	public String toString(){
		String msg = "author_date=>"+this.author_date+";"
					+"author_email=>"+this.author_email+";"
					+"author_name=>"+this.author_name+";"
					+"commit_hash=>"+this.commit_hash+";"
					+"commiter_email=>"+this.commiter_email+";"
					+"commiter_name=>"+this.commiter_name+";"
					+"committer_date=>"+this.committer_date+";"
					+"parent_hashes=>"+this.parent_hashes+";"
					+"project=>"+this.project+";"
					+"subject=>"+this.subject+";"
					+"tree_hash=>"+this.tree_hash+";"
					+"message=>"+this.messages.toString()+";"
					+"target=>"+this.targets.toString();
		return msg;
	}
	
	private void clearList(List l){
		if(l != null){
			for(int i=0;i<l.size();i++)
				l.remove(i);
		}
	}
	
	
	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getCommit_hash() {
		return commit_hash;
	}

	public void setCommit_hash(String commitHash) {
		commit_hash = commitHash;
	}

	public String getTree_hash() {
		return tree_hash;
	}

	public void setTree_hash(String treeHash) {
		tree_hash = treeHash;
	}

	public String getParent_hashes() {
		return parent_hashes;
	}

	public void setParent_hashes(String parentHashes) {
		parent_hashes = parentHashes;
	}

	public String getAuthor_name() {
		return author_name;
	}

	public void setAuthor_name(String authorName) {
		author_name = authorName;
	}

	public String getAuthor_email() {
		return author_email;
	}

	public void setAuthor_email(String authorEmail) {
		author_email = authorEmail;
	}

	public String getAuthor_date() {
		return author_date;
	}

	public void setAuthor_date(String authorDate) {
		author_date = authorDate;
	}

	public String getCommiter_name() {
		return commiter_name;
	}

	public void setCommiter_name(String commiterName) {
		commiter_name = commiterName;
	}

	public String getCommiter_email() {
		return commiter_email;
	}

	public void setCommiter_email(String commiterEmail) {
		commiter_email = commiterEmail;
	}

	public String getCommitter_date() {
		return committer_date;
	}

	public void setCommitter_date(String committerDate) {
		committer_date = committerDate;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	
}
