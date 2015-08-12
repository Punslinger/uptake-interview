package interview;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Person
{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@JsonIgnore
	@ManyToOne
	private Family family;
	
	private String firstName;
	private String lastName;
	
	public String getFirstName()
	{
		return firstName;
	}
	
	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}
	
	public String getLastName()
	{
		return lastName;
	}
	
	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	public Family getFamily()
	{
		return family;
	}
	
	public void setFamily(Family family)
	{
		if(this.family == family)
		{
			return;
		}
		
		if(this.family != null && this.family.getMembers().contains(this))
		{
			this.family.removeMember(this);
		}
		this.family = family;
		if(this.family != null && !this.family.getMembers().contains(this))
		{
			this.family.addMember(this);
		}
	}
}