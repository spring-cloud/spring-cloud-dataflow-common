/*
 * Copyright 2018-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.cloud.dataflow.common.test.docker.compose.configuration;

import static org.springframework.cloud.dataflow.common.test.docker.compose.configuration.EnvironmentVariables.DOCKER_CERT_PATH;
import static org.springframework.cloud.dataflow.common.test.docker.compose.configuration.EnvironmentVariables.DOCKER_HOST;
import static org.springframework.cloud.dataflow.common.test.docker.compose.configuration.EnvironmentVariables.DOCKER_TLS_VERIFY;

import java.util.HashMap;
import java.util.Map;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.cloud.dataflow.common.test.docker.compose.configuration.RemoteEnvironmentValidator;

public class RemoteEnvironmentValidatorTests {

	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void throw_exception_if_docker_host_is_not_set() {
		Map<String, String> variables = new HashMap<>();
		variables.put("SOME_VARIABLE", "SOME_VALUE");

		exception.expect(IllegalStateException.class);
		exception.expectMessage("Missing required environment variables: ");
		exception.expectMessage(DOCKER_HOST);
		RemoteEnvironmentValidator.instance().validateEnvironmentVariables(variables);
	}

	@Test
	public void throw_exception_if_docker_cert_path_is_missing_and_tls_is_on() {
		Map<String, String> variables = new HashMap<>();
		variables.put(DOCKER_HOST, "tcp://192.168.99.100:2376");
		variables.put(DOCKER_TLS_VERIFY, "1");

		exception.expect(IllegalStateException.class);
		exception.expectMessage("Missing required environment variables: ");
		exception.expectMessage(DOCKER_CERT_PATH);
		RemoteEnvironmentValidator.instance().validateEnvironmentVariables(variables);
	}

	@Test
	public void validate_environment_with_all_valid_variables_set_without_tls() {
		Map<String, String> variables = new HashMap<>();
		variables.put(DOCKER_HOST, "tcp://192.168.99.100:2376");
		variables.put("SOME_VARIABLE", "SOME_VALUE");

		RemoteEnvironmentValidator.instance().validateEnvironmentVariables(variables);
	}

	@Test
	public void validate_environment_with_all_valid_variables_set_with_tls() {
		Map<String, String> variables = new HashMap<>();
		variables.put(DOCKER_HOST, "tcp://192.168.99.100:2376");
		variables.put(DOCKER_TLS_VERIFY, "1");
		variables.put(DOCKER_CERT_PATH, "/path/to/certs");
		variables.put("SOME_VARIABLE", "SOME_VALUE");

		RemoteEnvironmentValidator.instance().validateEnvironmentVariables(variables);
	}
}
