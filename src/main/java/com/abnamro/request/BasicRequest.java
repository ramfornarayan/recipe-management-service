package com.abnamro.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class BasicRequest {

	@NotNull(message = "{id.notNull}")
	@Positive(message = "{id.positive}")
	@Schema(description = "Id of the attribute", example = "1")
	private Integer id;

	public Integer getId() {
		return id;
	}

	public BasicRequest() {
	}

	public BasicRequest(Integer id) {
		this.id = id;
	}
}
