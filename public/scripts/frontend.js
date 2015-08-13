function addFamily()
{
	var jsonData = { "name" : $("#familyName").val() };
	$.ajax({
		type: "POST",
		url: "families",
		data: JSON.stringify(jsonData),
		contentType: "application/json",
		success: function() {
			listFamilies();
		},
		error: function(xhr, status, errorThrown) {
			alert("Error: HTTP status " + status);
		}
	});
}

function addPerson()
{
	var jsonData = { "firstName" : $("#firstName").val(), "lastName" : $("#lastName").val() };
	$.ajax({
		type: "POST",
		url: "people",
		data: JSON.stringify(jsonData),
		contentType: "application/json",
		success: function() {
			listPeople();
		},
		error: function(xhr, status, errorThrown) {
			alert("Error: HTTP status " + status);
		}
	});
}

var delBtnRegex = /del_(\d+)/;
function deletePerson(event)
{
	var elem = event.target;
	var matchResults = elem.id.match(delBtnRegex);
	if(matchResults != null)
	{
		var id = matchResults[1];
		
		$.ajax({
			type: "DELETE",
			url: $("#link_" + id).val(),
			success: function() {
				//$("#entry_" + id).remove();
				listPeople();
			},
			error: function(xhr, status, errorThrown) {
			alert("Error: HTTP status " + status);
			}
		});
	}
}

function deleteFamily()
{
	var index = $("#familyList").get(0).selectedIndex;
	$.ajax({
		type: "DELETE",
		url: $("#fam_" + index).val(),
		success: function() {
			listFamilies();
		},
		error: function(xhr, status, errorThrown) {
			alert("Error: HTTP status " + status);
		}
	});
}

var addFamBtnRegex = /addfam_(\d+)/;
function addPersonToFamily(event)
{
	var elem = event.target;
	var matchResults = elem.id.match(addFamBtnRegex);
	if(matchResults != null)
	{
		var id = matchResults[1];
		var jsonData = { "adding" : true, "firstName" : $("#fname_" + id).val(), "lastName" : $("#lname_" + id).val() };
		$.ajax({
			type: "PUT",
			url: "family/" + getSelectedFamilyName(),
			data: JSON.stringify(jsonData),
			contentType: "application/json",
			success: function() {
				listFamilyMembers();
			},
			error: function(xhr, status, errorThrown) {
			alert("Error: HTTP status " + status);
			}
		});
	}
}

var remFamBtnRegex = /remfam_(\d+)/;
function removePersonFromFamily(event)
{
	var elem = event.target;
	var matchResults = elem.id.match(remFamBtnRegex);
	if(matchResults != null)
	{
		var id = matchResults[1];
		var jsonData = { "adding" : false, "firstName" : $("#famFname_" + id).val(), "lastName" : $("#famLname_" + id).val() };
		$.ajax({
			type: "PUT",
			url: "family/" + getSelectedFamilyName(),
			data: JSON.stringify(jsonData),
			contentType: "application/json",
			success: function() {
				listFamilyMembers();
			},
			error: function(xhr, status, errorThrown) {
			alert("Error: HTTP status " + status);
			}
		});
	}
}

function listFamilies()
{
	$.ajax({
		url: "families",
		type: "GET",
		dataType: "json",
		success: function(json) {
			//$("#familyList").text(JSON.stringify(json));
			$("#familyList").empty();
			$("#familyMembersList").empty();
			if(json._embedded != null)
			{
				var families = json._embedded.families;
				if(families != null)
				{
					for(var i=0; i < families.length; i++)
					{
						var entry = document.createElement("OPTION");
						entry.id = "fam_" + i;
						entry.value = families[i]._links.self.href;
						entry.appendChild(document.createTextNode(families[i].name));
						$("#familyList").get(0).appendChild(entry);
					}
					listFamilyMembers();
				}
			}
		},
		error: function(xhr, status, errorThrown) {
			alert("Error: HTTP status " + status);
		}
	});
}

function listPeople()
{
	$.ajax({
		url: "people",
		type: "GET",
		dataType: "json",
		success: function(json) {
			//$("#peopleList").text(JSON.stringify(json));
			$("#peopleList").empty();
			if(json._embedded != null)
			{
				var people = json._embedded.people;
				if(people != null)
				{
					for(var i=0; i < people.length; i++)
					{
						var entry = document.createElement("DIV");
						entry.appendChild(document.createTextNode(people[i].firstName + " " + people[i].lastName));
						entry.id = "person_" + i;
						var delBtn = document.createElement("BUTTON");
						delBtn.appendChild(document.createTextNode("delete"));
						delBtn.id = "del_" + i;
						entry.appendChild(delBtn);
						var addFamBtn = document.createElement("BUTTON");
						addFamBtn.appendChild(document.createTextNode("add to family"));
						addFamBtn.id = "addfam_" + i;
						entry.appendChild(addFamBtn);
						var entryLink = document.createElement("INPUT");
						entryLink.setAttribute("type", "hidden");
						entryLink.id = "link_" + i;
						entryLink.value = people[i]._links.self.href;
						entry.appendChild(entryLink);
						var entryFName = document.createElement("INPUT");
						entryFName.setAttribute("type", "hidden");
						entryFName.id = "fname_" + i;
						entryFName.value = people[i].firstName;
						entry.appendChild(entryFName);
						var entryLName = document.createElement("INPUT");
						entryLName.setAttribute("type", "hidden");
						entryLName.id = "lname_" + i;
						entryLName.value = people[i].lastName;
						entry.appendChild(entryLName);
						$("#peopleList").get(0).appendChild(entry);
						delBtn.addEventListener("click", deletePerson);
						addFamBtn.addEventListener("click", addPersonToFamily);
					}
				}
			}
		},
		error: function(xhr, status, errorThrown) {
			alert("Error: HTTP status " + status);
		}
	});
}

function listFamilyMembers()
{
	var name = getSelectedFamilyName();
	$.ajax({
		url: "family/" + name,
		type: "GET",
		dataType: "json",
		success: function(json) {
			//$("#familyMembersList").text(JSON.stringify(json));
			$("#familyMembersList").empty();
			for(var i=0; i < json.length; i++)
			{
				var entry = document.createElement("DIV");
				entry.appendChild(document.createTextNode(json[i].firstName + " " + json[i].lastName));
				var remFamBtn = document.createElement("BUTTON");
				remFamBtn.appendChild(document.createTextNode("remove"));
				remFamBtn.id = "remfam_" + i;
				entry.appendChild(remFamBtn);
				var entryFName = document.createElement("INPUT");
				entryFName.setAttribute("type", "hidden");
				entryFName.id = "famFname_" + i;
				entryFName.value = json[i].firstName;
				entry.appendChild(entryFName);
				var entryLName = document.createElement("INPUT");
				entryLName.setAttribute("type", "hidden");
				entryLName.id = "famLname_" + i;
				entryLName.value = json[i].lastName;
				entry.appendChild(entryLName);
				$("#familyMembersList").get(0).appendChild(entry);
				remFamBtn.addEventListener("click", removePersonFromFamily);
			}
		},
		error: function(xhr, status, errorThrown) {
			alert("Error: HTTP status " + status);
		}
	});
}

function getSelectedFamilyName()
{
	var index = $("#familyList").get(0).selectedIndex;
	return $("#fam_" + index).text();
}

$(document).ready(init);
function init()
{
	listPeople();
	listFamilies();
}
