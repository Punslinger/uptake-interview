package interview;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootApplication
public class Application
{
	public static void main(String[] args)
	{
		SpringApplication.run(Application.class, args);
	}
}

class PersonOp
{
	private String firstName;
	private String lastName;
	private boolean adding;
	
	public String getFirstName()
	{
		return firstName;
	}
	
	public String getLastName()
	{
		return lastName;
	}
	
	public boolean isAdding()
	{
		return adding;
	}
}

@RestController
@RequestMapping("/family/{name}")
class FamilyRestController
{
	private final FamilyRepository familyRepository;
	private final PersonRepository personRepository;
	
	@RequestMapping(method = RequestMethod.PUT)
	ResponseEntity<?> add(@PathVariable String name, @RequestBody PersonOp personOp)
	{
		return this.familyRepository.findByName(name).map(family -> {
			List<Person> people = personRepository.findByFirstNameAndLastName(personOp.getFirstName(), personOp.getLastName());
			System.out.println("People matched for family op: " + people.size());
			for(Person p : people)
			{
				if(personOp.isAdding())
				{
					family.addMember(p);		//add a person to the family
					System.out.println("Added " + p.getFirstName() + " " + p.getLastName() + " to family " + family.getName());
				}
				else
				{
					family.removeMember(p);	//remove a person from the family
					System.out.println("Removed " + p.getFirstName() + " " + p.getLastName() + " from family " + family.getName());
				}
			}
			familyRepository.save(family);
			HttpHeaders headers = new HttpHeaders();
			//headers.setLocation(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(family.getId()).toUri());
			return new ResponseEntity<>(null, headers, HttpStatus.OK);
		}).get();
	}

	//get a list of family members
	@RequestMapping(method = RequestMethod.GET)
	Collection<Person> getFamilyMembers(@PathVariable String name)
	{
		System.out.println("Attempting to display family " + name);
		Optional<Family> familyOpt = this.familyRepository.findByName(name);
		if(familyOpt.isPresent())
		{
			Family fam = familyOpt.get();
			Collection<Person> members = fam.getMembers();
			System.out.println("Listing " + members.size() + " members in family " + fam.getName() + ":");
			for(Person p : members)
				System.out.println(p.getFirstName() + " " + p.getLastName());
			return members;
		}
		else
			System.out.println("No family by that name in the database.");
		
		return null;
	}
	
	@Autowired
	FamilyRestController(FamilyRepository familyRepository, PersonRepository personRepository)
	{
		this.familyRepository = familyRepository;
		this.personRepository = personRepository;
	}
}