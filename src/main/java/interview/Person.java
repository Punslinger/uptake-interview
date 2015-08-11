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
		System.out.println("Person.setFamily() invoked");
		if(this.family == family)
		{
			System.out.println("Person.setFamily(): this.family = family, returning");
			return;
		}
		
		if(this.family != null && this.family.getMembers().contains(this))
		{
			System.out.println("Person.setFamily(): removing from existing family " + this.family.getName());
			this.family.removeMember(this);
			System.out.println("Person.setFamily(): this.family.removeMember() returned");
		}
		this.family = family;
		System.out.println("Person.setFamily(): this.family = " + family.getName());
		if(this.family != null && !this.family.getMembers().contains(this))
		{
			System.out.println("Person.setFamily(): adding to new family " + this.family.getName());
			this.family.addMember(this);
			System.out.println("Person.setFamily(): this.family.addMember() returned");
		}
	}
}