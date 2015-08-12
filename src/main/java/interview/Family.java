package interview;

import java.util.Set;
import java.util.Collections;
import java.util.HashSet;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.JoinColumn;

@Entity
public class Family
{	
	@OneToMany(mappedBy = "family")
	private Set<Person> members = new HashSet<Person>();
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String name;
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String familyName)
	{
		this.name = familyName;
	}
	
	public Set<Person> getMembers()
	{
		return Collections.unmodifiableSet(members);
	}
	
	public void addMember(Person member)
	{
		if(!members.contains(member))
		{
			members.add(member);
			member.setFamily(this);
		}
	}
	
	public void removeMember(Person member)
	{
		if(members.contains(member))
		{
			members.remove(member);
			member.setFamily(null);
		}
	}
}