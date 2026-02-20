package com.abnamro.recipe.response;

public class CreateEntityResponse {

	private int id;
	private String name;

	public CreateEntityResponse(int id) {
		this.id = id;
	}

	public CreateEntityResponse() {
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		CreateEntityResponse that = (CreateEntityResponse) o;

		return id == that.id;
	}

	@Override
	public int hashCode() {
		return id;
	}
}
